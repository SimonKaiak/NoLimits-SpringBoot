package com.example.NoLimits.integration.producto;

import com.example.NoLimits.Multimedia.model.catalogos.*;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:nolimits_detalle_venta_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
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
@DisplayName("T-Integración · Detalle Venta")
class DetalleVentaIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private DetalleVentaRepository detalleVentaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private TipoProductoRepository tipoProductoRepository;
    @Autowired private ClasificacionRepository clasificacionRepository;
    @Autowired private EstadoRepository estadoRepository;
    @Autowired private VentaRepository ventaRepository;
    @Autowired private MetodoPagoRepository metodoPagoRepository;
    @Autowired private MetodoEnvioRepository metodoEnvioRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;

    private ProductoModel producto;
    private VentaModel venta;
    private DetalleVentaModel detalle;

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

        RolModel rol = new RolModel();
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);
        rol = rolRepository.save(rol);

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("juan.detalle@test.com");
        usuario.setTelefono(912345678L);
        usuario.setPassword("$2a$10$hashedpassword");
        usuario.setRol(rol);
        usuario = usuarioRepository.save(usuario);

        MetodoPagoModel metodoPago = new MetodoPagoModel();
        metodoPago.setNombre("Webpay");
        metodoPago.setActivo(true);
        metodoPago = metodoPagoRepository.save(metodoPago);

        MetodoEnvioModel metodoEnvio = new MetodoEnvioModel();
        metodoEnvio.setNombre("Retiro en tienda");
        metodoEnvio.setDescripcion("Retiro presencial");
        metodoEnvio.setActivo(true);
        metodoEnvio = metodoEnvioRepository.save(metodoEnvio);

        producto = new ProductoModel();
        producto.setNombre("Zelda Breath of the Wild");
        producto.setPrecio(49990.0);
        producto.setTipoProducto(tipo);
        producto.setClasificacion(clasificacion);
        producto.setEstado(estado);
        producto = productoRepository.save(producto);

        venta = new VentaModel();
        venta.setFechaCompra(LocalDate.now());
        venta.setHoraCompra(LocalTime.now());
        venta.setUsuarioModel(usuario);
        venta.setMetodoPagoModel(metodoPago);
        venta.setMetodoEnvioModel(metodoEnvio);
        venta.setEstado(estado);
        venta = ventaRepository.save(venta);

        detalle = new DetalleVentaModel();
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(12990f);
        detalle = detalleVentaRepository.save(detalle);
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionDetalleVenta {

        @Test
        @DisplayName("it: obtiene un detalle de venta por ID")
        void obtenerDetalleVenta_retornaDatoPersistido() throws Exception {
            mockMvc.perform(get("/api/v1/detalles-venta/" + detalle.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productoId").value(producto.getId()))
                    .andExpect(jsonPath("$.productoNombre").value("Zelda Breath of the Wild"))
                    .andExpect(jsonPath("$.cantidad").value(2))
                    .andExpect(jsonPath("$.subtotal").value(25980.0));
        }

        @Test
        @DisplayName("it: actualiza un detalle de venta")
        void actualizarDetalleVenta_actualizaDatoPersistido() throws Exception {
            String updateBody = """
                    {
                      "ventaId": %d,
                      "productoId": %d,
                      "cantidad": 3,
                      "precioUnitario": 15000
                    }
                    """.formatted(venta.getId(), producto.getId());

            mockMvc.perform(put("/api/v1/detalles-venta/" + detalle.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cantidad").value(3))
                    .andExpect(jsonPath("$.precioUnitario").value(15000.0))
                    .andExpect(jsonPath("$.subtotal").value(45000.0));
        }

        @Test
        @DisplayName("it: elimina un detalle de venta")
        void eliminarDetalleVenta_eliminaDatoPersistido() throws Exception {
            mockMvc.perform(delete("/api/v1/detalles-venta/" + detalle.getId()))
                    .andExpect(status().isNoContent());
        }
    }
}