package com.example.NoLimits.integration.producto;

import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.ClasificacionRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoProductoRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:nolimits_producto_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.id.new_generator_mappings=false"
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("T-Integración · Productos")
class ProductoIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ProductoRepository productoRepository;
    @Autowired private TipoProductoRepository tipoProductoRepository;
    @Autowired private ClasificacionRepository clasificacionRepository;
    @Autowired private EstadoRepository estadoRepository;

    private TipoProductoModel tipoProducto;
    private ClasificacionModel clasificacion;
    private EstadoModel estado;

    @BeforeEach
    void prepararDatos() {
        productoRepository.deleteAll();
        tipoProductoRepository.deleteAll();
        clasificacionRepository.deleteAll();
        estadoRepository.deleteAll();

        tipoProducto = new TipoProductoModel();
        tipoProducto.setNombre("Videojuego");
        tipoProducto.setDescripcion("Productos de tipo videojuego");
        tipoProducto.setActivo(true);
        tipoProducto = tipoProductoRepository.save(tipoProducto);

        clasificacion = new ClasificacionModel();
        clasificacion.setNombre("T");
        clasificacion.setDescripcion("Contenido apto para adolescentes");
        clasificacion.setActivo(true);
        clasificacion = clasificacionRepository.save(clasificacion);

        estado = new EstadoModel();
        estado.setNombre("Disponible");
        estado.setDescripcion("Producto disponible");
        estado.setActivo(true);
        estado = estadoRepository.save(estado);
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionProducto {

        @Test
        @DisplayName("it: crea un producto y lo persiste")
        void crearProducto_persisteEnBaseDeDatos() throws Exception {
            String body = """
                    {
                      "nombre": "Zelda Breath of the Wild",
                      "precio": 49990,
                      "sinopsis": "Aventura en mundo abierto",
                      "urlTrailer": "https://youtube.com/test",
                      "anio": 2017,
                      "tipoProductoId": %d,
                      "clasificacionId": %d,
                      "estadoId": %d,
                      "saga": "Zelda",
                      "portadaSaga": "sagas/zelda.webp"
                    }
                    """.formatted(tipoProducto.getId(), clasificacion.getId(), estado.getId());

            mockMvc.perform(post("/api/v1/productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("Zelda Breath of the Wild"))
                    .andExpect(jsonPath("$.tipoProductoNombre").value("Videojuego"))
                    .andExpect(jsonPath("$.estadoNombre").value("Disponible"));

            assertTrue(productoRepository.existsByNombreIgnoreCase("Zelda Breath of the Wild"));
        }

        @Test
        @DisplayName("it: lista productos persistidos")
        void listarProductos_retornaDatosPersistidos() throws Exception {
            String body = """
                    {
                      "nombre": "Super Mario Odyssey",
                      "precio": 39990,
                      "tipoProductoId": %d,
                      "clasificacionId": %d,
                      "estadoId": %d,
                      "saga": "Mario"
                    }
                    """.formatted(tipoProducto.getId(), clasificacion.getId(), estado.getId());

            mockMvc.perform(post("/api/v1/productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/v1/productos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nombre").value("Super Mario Odyssey"))
                    .andExpect(jsonPath("$[0].tipoProductoNombre").value("Videojuego"));
        }

        @Test
        @DisplayName("it: actualiza un producto usando Service y Repository real")
        void actualizarProducto_actualizaDatoPersistido() throws Exception {
            String crearBody = """
                    {
                      "nombre": "Minecraft",
                      "precio": 19990,
                      "tipoProductoId": %d,
                      "clasificacionId": %d,
                      "estadoId": %d
                    }
                    """.formatted(tipoProducto.getId(), clasificacion.getId(), estado.getId());

            String response = mockMvc.perform(post("/api/v1/productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(crearBody))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Long id = Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));

            String updateBody = """
                    {
                      "nombre": "Minecraft Deluxe",
                      "precio": 24990,
                      "tipoProductoId": %d,
                      "clasificacionId": %d,
                      "estadoId": %d
                    }
                    """.formatted(tipoProducto.getId(), clasificacion.getId(), estado.getId());

            mockMvc.perform(put("/api/v1/productos/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Minecraft Deluxe"));

            assertTrue(productoRepository.existsByNombreIgnoreCase("Minecraft Deluxe"));
        }
    }
}