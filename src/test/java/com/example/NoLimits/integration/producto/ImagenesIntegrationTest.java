package com.example.NoLimits.integration.producto;

import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.ClasificacionRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoProductoRepository;
import com.example.NoLimits.Multimedia.repository.producto.ImagenesRepository;
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
        "spring.datasource.url=jdbc:h2:mem:nolimits_imagenes_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
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
@DisplayName("T-Integración · Imágenes")
class ImagenesIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ImagenesRepository imagenesRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private TipoProductoRepository tipoProductoRepository;
    @Autowired private ClasificacionRepository clasificacionRepository;
    @Autowired private EstadoRepository estadoRepository;

    private ProductoModel producto;

    @BeforeEach
    void prepararDatos() {
        imagenesRepository.deleteAll();
        productoRepository.deleteAll();
        tipoProductoRepository.deleteAll();
        clasificacionRepository.deleteAll();
        estadoRepository.deleteAll();

        TipoProductoModel tipo = new TipoProductoModel();
        tipo.setNombre("Videojuego");
        tipo.setDescripcion("Producto tipo videojuego");
        tipo.setActivo(true);
        tipo = tipoProductoRepository.save(tipo);

        ClasificacionModel clasificacion = new ClasificacionModel();
        clasificacion.setNombre("T");
        clasificacion.setDescripcion("Adolescentes");
        clasificacion.setActivo(true);
        clasificacion = clasificacionRepository.save(clasificacion);

        EstadoModel estado = new EstadoModel();
        estado.setNombre("Disponible");
        estado.setDescripcion("Producto disponible");
        estado.setActivo(true);
        estado = estadoRepository.save(estado);

        producto = new ProductoModel();
        producto.setNombre("Zelda Breath of the Wild");
        producto.setPrecio(49990.0);
        producto.setTipoProducto(tipo);
        producto.setClasificacion(clasificacion);
        producto.setEstado(estado);
        producto = productoRepository.save(producto);
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionImagenes {

        @Test
        @DisplayName("it: crea una imagen y la persiste")
        void crearImagen_persisteEnBaseDeDatos() throws Exception {
            String body = """
                    {
                      "ruta": "/assets/img/zelda.webp",
                      "altText": "Portada Zelda",
                      "productoId": %d
                    }
                    """.formatted(producto.getId());

            mockMvc.perform(post("/api/v1/imagenes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.ruta").value("/assets/img/zelda.webp"))
                    .andExpect(jsonPath("$.altText").value("Portada Zelda"))
                    .andExpect(jsonPath("$.productoId").value(producto.getId()));

            assertTrue(imagenesRepository.existsByRuta("/assets/img/zelda.webp"));
        }

        @Test
        @DisplayName("it: lista imágenes asociadas a un producto")
        void listarImagenesPorProducto_retornaDatosPersistidos() throws Exception {
            String body = """
                    {
                      "ruta": "/assets/img/mario.webp",
                      "altText": "Portada Mario",
                      "productoId": %d
                    }
                    """.formatted(producto.getId());

            mockMvc.perform(post("/api/v1/imagenes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/v1/imagenes/producto/" + producto.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].ruta").value("/assets/img/mario.webp"))
                    .andExpect(jsonPath("$[0].productoId").value(producto.getId()));
        }

        @Test
        @DisplayName("it: actualiza una imagen usando Service y Repository real")
        void actualizarImagen_actualizaDatoPersistido() throws Exception {
            String crearBody = """
                    {
                      "ruta": "/assets/img/original.webp",
                      "altText": "Original",
                      "productoId": %d
                    }
                    """.formatted(producto.getId());

            String response = mockMvc.perform(post("/api/v1/imagenes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(crearBody))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Long id = Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));

            String updateBody = """
                    {
                      "ruta": "/assets/img/actualizada.webp",
                      "altText": "Actualizada",
                      "productoId": %d
                    }
                    """.formatted(producto.getId());

            mockMvc.perform(put("/api/v1/imagenes/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ruta").value("/assets/img/actualizada.webp"))
                    .andExpect(jsonPath("$.altText").value("Actualizada"));

            assertTrue(imagenesRepository.existsByRuta("/assets/img/actualizada.webp"));
        }
    }
}