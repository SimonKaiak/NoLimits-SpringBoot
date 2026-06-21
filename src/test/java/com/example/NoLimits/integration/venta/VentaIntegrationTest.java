package com.example.NoLimits.integration.venta;

import com.example.NoLimits.Multimedia.model.catalogos.*;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.catalogos.*;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:nolimits_venta_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
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
@DisplayName("T-Integración · Ventas")
class VentaIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private VentaRepository ventaRepository;
    @Autowired private DetalleVentaRepository detalleVentaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;

    @Autowired private TipoProductoRepository tipoProductoRepository;
    @Autowired private ClasificacionRepository clasificacionRepository;
    @Autowired private EstadoRepository estadoRepository;
    @Autowired private MetodoPagoRepository metodoPagoRepository;
    @Autowired private MetodoEnvioRepository metodoEnvioRepository;

    private UsuarioModel usuario;
    private ProductoModel producto;
    private MetodoPagoModel metodoPago;
    private MetodoEnvioModel metodoEnvio;
    private EstadoModel estado;

    @BeforeEach
    void prepararDatos() {
        detalleVentaRepository.deleteAll();
        ventaRepository.deleteAll();
        productoRepository.deleteAll();
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();
        metodoPagoRepository.deleteAll();
        metodoEnvioRepository.deleteAll();
        tipoProductoRepository.deleteAll();
        clasificacionRepository.deleteAll();
        estadoRepository.deleteAll();

        RolModel rol = new RolModel();
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);
        rol = rolRepository.save(rol);

        usuario = new UsuarioModel();
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("juan.venta@test.com");
        usuario.setTelefono(912345678L);
        usuario.setPassword("$2a$10$hashedpassword");
        usuario.setRol(rol);
        usuario = usuarioRepository.save(usuario);

        TipoProductoModel tipoProducto = new TipoProductoModel();
        tipoProducto.setNombre("Videojuego");
        tipoProducto.setDescripcion("Producto tipo videojuego");
        tipoProducto.setActivo(true);
        tipoProducto = tipoProductoRepository.save(tipoProducto);

        ClasificacionModel clasificacion = new ClasificacionModel();
        clasificacion.setNombre("T");
        clasificacion.setDescripcion("Adolescentes");
        clasificacion.setActivo(true);
        clasificacion = clasificacionRepository.save(clasificacion);

        estado = new EstadoModel();
        estado.setNombre("Pendiente");
        estado.setDescripcion("Venta pendiente");
        estado.setActivo(true);
        estado = estadoRepository.save(estado);

        metodoPago = new MetodoPagoModel();
        metodoPago.setNombre("Webpay");
        metodoPago.setActivo(true);
        metodoPago = metodoPagoRepository.save(metodoPago);

        metodoEnvio = new MetodoEnvioModel();
        metodoEnvio.setNombre("Retiro en tienda");
        metodoEnvio.setDescripcion("Retiro presencial");
        metodoEnvio.setActivo(true);
        metodoEnvio = metodoEnvioRepository.save(metodoEnvio);

        producto = new ProductoModel();
        producto.setNombre("Zelda Breath of the Wild");
        producto.setPrecio(49990.0);
        producto.setTipoProducto(tipoProducto);
        producto.setClasificacion(clasificacion);
        producto.setEstado(estado);
        producto = productoRepository.save(producto);
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionVenta {

        @Test
        @DisplayName("it: registra una venta real con detalles")
        void registrarVenta_creaVentaConDetalles() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("usuarioId", usuario.getId());

            String body = """
                    {
                      "metodoPagoId": %d,
                      "metodoEnvioId": %d,
                      "estadoId": %d,
                      "detalles": [
                        {
                          "productoId": %d,
                          "cantidad": 2,
                          "precioUnitario": 12990
                        }
                      ]
                    }
                    """.formatted(
                    metodoPago.getId(),
                    metodoEnvio.getId(),
                    estado.getId(),
                    producto.getId()
            );

            mockMvc.perform(post("/api/v1/ventas/registrar")
                            .session(session)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.usuarioId").value(usuario.getId()))
                    .andExpect(jsonPath("$.metodoPagoNombre").value("Webpay"))
                    .andExpect(jsonPath("$.metodoEnvioNombre").value("Retiro en tienda"))
                    .andExpect(jsonPath("$.estadoNombre").value("Pendiente"))
                    .andExpect(jsonPath("$.totalVenta").value(25980.0))
                    .andExpect(jsonPath("$.detalles[0].productoNombre").value("Zelda Breath of the Wild"));

            assertTrue(ventaRepository.countByUsuarioModel_Id(usuario.getId()) > 0);
        }

        @Test
        @DisplayName("it: rechaza registrar venta sin sesión")
        void registrarVentaSinSesion_retornaUnauthorized() throws Exception {
            String body = """
                    {
                      "metodoPagoId": %d,
                      "metodoEnvioId": %d,
                      "estadoId": %d,
                      "detalles": []
                    }
                    """.formatted(
                    metodoPago.getId(),
                    metodoEnvio.getId(),
                    estado.getId()
            );

            mockMvc.perform(post("/api/v1/ventas/registrar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("it: lista ventas registradas")
        void listarVentas_retornaDatosPersistidos() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("usuarioId", usuario.getId());

            String body = """
                    {
                      "metodoPagoId": %d,
                      "metodoEnvioId": %d,
                      "estadoId": %d,
                      "detalles": [
                        {
                          "productoId": %d,
                          "cantidad": 1,
                          "precioUnitario": 10000
                        }
                      ]
                    }
                    """.formatted(
                    metodoPago.getId(),
                    metodoEnvio.getId(),
                    estado.getId(),
                    producto.getId()
            );

            mockMvc.perform(post("/api/v1/ventas/registrar")
                            .session(session)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/ventas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].usuarioId").value(usuario.getId()))
                    .andExpect(jsonPath("$[0].metodoPagoNombre").value("Webpay"));
        }
    }
}