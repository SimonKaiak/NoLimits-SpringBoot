package com.example.NoLimits.service.venta;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.producto.request.DetalleVentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.venta.request.VentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.venta.update.VentaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoEnvioRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.service.venta.VentaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class VentaServiceTest {

    @Autowired
    private VentaService ventaService;

    @MockBean private VentaRepository ventaRepository;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private MetodoPagoRepository metodoPagoRepository;
    @MockBean private MetodoEnvioRepository metodoEnvioRepository;
    @MockBean private EstadoRepository estadoRepository;
    @MockBean private ProductoRepository productoRepository;

    // ===================== HELPERS =====================

    private UsuarioModel usuarioEntity() {
        UsuarioModel u = new UsuarioModel();
        u.setId(1L);
        u.setNombre("Juan");
        u.setApellidos("Pérez");
        u.setCorreo("correo@test.com");
        u.setTelefono(123456789L);
        u.setPassword("password");
        return u;
    }

    private MetodoPagoModel metodoPagoEntity() {
        MetodoPagoModel m = new MetodoPagoModel();
        m.setId(1L);
        m.setNombre("Tarjeta de Crédito");
        m.setActivo(true);
        return m;
    }

    private MetodoEnvioModel metodoEnvioEntity() {
        MetodoEnvioModel m = new MetodoEnvioModel();
        m.setId(1L);
        m.setNombre("Despacho a domicilio");
        m.setDescripcion("Entrega a domicilio");
        m.setActivo(true);
        return m;
    }

    private EstadoModel estadoEntity() {
        EstadoModel e = new EstadoModel();
        e.setId(1L);
        e.setNombre("Pagada");
        e.setActivo(true);
        return e;
    }

    private VentaModel ventaEntity() {
        VentaModel v = new VentaModel();
        v.setId(1L);
        v.setFechaCompra(LocalDate.of(2025, 7, 27));
        v.setHoraCompra(LocalTime.of(14, 30));
        v.setUsuarioModel(usuarioEntity());
        v.setMetodoPagoModel(metodoPagoEntity());
        v.setMetodoEnvioModel(metodoEnvioEntity());
        v.setEstado(estadoEntity());
        // NO se setea totalVenta: se calcula en el modelo

        // detalle opcional para probar mapeo
        ProductoModel p = new ProductoModel();
        p.setId(5L);
        p.setNombre("Teclado Mecánico");

        DetalleVentaModel d = new DetalleVentaModel();
        d.setId(100L);
        d.setVenta(v);
        d.setProducto(p);
        d.setCantidad(2);
        d.setPrecioUnitario(10000.0f);   // float, no double
        // NO se setea subtotal: lo calcula el modelo

        List<DetalleVentaModel> detalles = new ArrayList<>();
        detalles.add(d);
        v.setDetalles(detalles);

        return v;
    }

    private VentaUpdateDTO ventaUpdateDTO() {
        VentaUpdateDTO dto = new VentaUpdateDTO();
        dto.setFechaCompra(LocalDate.of(2025, 8, 1));
        dto.setHoraCompra(LocalTime.of(10, 15));
        dto.setMetodoPagoId(2L);
        dto.setMetodoEnvioId(3L);
        dto.setEstadoId(2L);
        return dto;
    }

    private VentaRequestDTO ventaRequestDTO() {
        VentaRequestDTO dto = new VentaRequestDTO();
        dto.setMetodoPagoId(1L);
        dto.setMetodoEnvioId(1L);
        dto.setEstadoId(1L);

        DetalleVentaRequestDTO det = new DetalleVentaRequestDTO();
        det.setProductoId(5L);
        det.setCantidad(2);
        det.setPrecioUnitario(10000.0f);

        dto.setDetalles(List.of(det));
        return dto;
    }

    // ===================== findAll / findById =====================

    @Test
    public void testFindAll() {
        when(ventaRepository.findAll()).thenReturn(List.of(ventaEntity()));

        List<VentaResponseDTO> ventas = ventaService.findAll();

        assertNotNull(ventas);
        assertEquals(1, ventas.size());
        VentaResponseDTO dto = ventas.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getUsuarioNombre());
        assertEquals("Tarjeta de Crédito", dto.getMetodoPagoNombre());
        assertEquals("Pagada", dto.getEstadoNombre());
        assertNotNull(dto.getDetalles());
        assertEquals(1, dto.getDetalles().size());
    }

    @Test
    public void testFindById_Existe() {
        VentaModel venta = ventaEntity();
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        VentaResponseDTO dto = ventaService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getUsuarioNombre());
        assertEquals("Tarjeta de Crédito", dto.getMetodoPagoNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.findById(99L));
    }

    // ===================== findBy* =====================

    @Test
    public void testFindByMetodoPago() {
        when(ventaRepository.findByMetodoPagoModel_Id(1L))
                .thenReturn(List.of(ventaEntity()));

        List<VentaResponseDTO> res = ventaService.findByMetodoPago(1L);

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals("Tarjeta de Crédito", res.get(0).getMetodoPagoNombre());
    }

    @Test
    public void testFindByFechaCompra() {
        VentaModel v = ventaEntity();
        when(ventaRepository.findByFechaCompra(v.getFechaCompra()))
                .thenReturn(List.of(v));

        List<VentaResponseDTO> res = ventaService.findByFechaCompra(v.getFechaCompra());

        assertEquals(1, res.size());
        assertEquals(v.getFechaCompra(), res.get(0).getFechaCompra());
    }

    @Test
    public void testFindByHoraCompra() {
        VentaModel v = ventaEntity();
        when(ventaRepository.findByHoraCompra(v.getHoraCompra()))
                .thenReturn(List.of(v));

        List<VentaResponseDTO> res = ventaService.findByHoraCompra(v.getHoraCompra());

        assertEquals(1, res.size());
        assertEquals(v.getHoraCompra(), res.get(0).getHoraCompra());
    }

    @Test
    public void testFindByUsuarioYMetodoPago() {
        when(ventaRepository.findByUsuarioModel_IdAndMetodoPagoModel_Id(1L, 1L))
                .thenReturn(List.of(ventaEntity()));

        List<VentaResponseDTO> res = ventaService.findByUsuarioYMetodoPago(1L, 1L);

        assertEquals(1, res.size());
        assertEquals("Juan", res.get(0).getUsuarioNombre());
        assertEquals("Tarjeta de Crédito", res.get(0).getMetodoPagoNombre());
    }

    // ===================== save =====================

    @Test
    public void testSave_OK() {
        VentaModel venta = new VentaModel();
        venta.setUsuarioModel(usuarioEntity());
        venta.setMetodoPagoModel(metodoPagoEntity());
        venta.setMetodoEnvioModel(metodoEnvioEntity());
        venta.setEstado(estadoEntity());

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodoPagoEntity()));
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(metodoEnvioEntity()));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEntity()));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(invocation -> {
                    VentaModel v = invocation.getArgument(0);
                    v.setId(1L);
                    return v;
                });

        VentaResponseDTO dto = ventaService.save(venta);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getUsuarioNombre());
        assertEquals("Tarjeta de Crédito", dto.getMetodoPagoNombre());
        assertEquals("Pagada", dto.getEstadoNombre());
    }

    @Test
    public void testSave_SinUsuario_LanzaExcepcion() {
        VentaModel venta = new VentaModel();
        venta.setMetodoPagoModel(metodoPagoEntity());
        venta.setMetodoEnvioModel(metodoEnvioEntity());
        venta.setEstado(estadoEntity());

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.save(venta));
    }

    @Test
    public void testSave_SinMetodoPago_LanzaExcepcion() {
        VentaModel venta = new VentaModel();
        venta.setUsuarioModel(usuarioEntity());
        venta.setMetodoEnvioModel(metodoEnvioEntity());
        venta.setEstado(estadoEntity());

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.save(venta));
    }

    // ===================== update / patch =====================

    @Test
    public void testUpdate_OK() {
        VentaModel existente = ventaEntity();

        MetodoPagoModel nuevoPago = new MetodoPagoModel();
        nuevoPago.setId(2L);
        nuevoPago.setNombre("Débito");

        MetodoEnvioModel nuevoEnvio = new MetodoEnvioModel();
        nuevoEnvio.setId(3L);
        nuevoEnvio.setNombre("Retiro en tienda");

        EstadoModel nuevoEstado = new EstadoModel();
        nuevoEstado.setId(2L);
        nuevoEstado.setNombre("Enviada");

        VentaUpdateDTO dto = ventaUpdateDTO();

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.findById(2L)).thenReturn(Optional.of(nuevoPago));
        when(metodoEnvioRepository.findById(3L)).thenReturn(Optional.of(nuevoEnvio));
        when(estadoRepository.findById(2L)).thenReturn(Optional.of(nuevoEstado));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VentaResponseDTO res = ventaService.update(1L, dto);

        assertNotNull(res);
        assertEquals("Débito", res.getMetodoPagoNombre());
        assertEquals("Retiro en tienda", res.getMetodoEnvioNombre());
        assertEquals("Enviada", res.getEstadoNombre());
        assertEquals(dto.getFechaCompra(), res.getFechaCompra());
    }

    @Test
    public void testPatch_ModificaSoloEstado() {
        VentaModel existente = ventaEntity();

        EstadoModel nuevoEstado = new EstadoModel();
        nuevoEstado.setId(2L);
        nuevoEstado.setNombre("Anulada");

        VentaUpdateDTO dto = new VentaUpdateDTO();
        dto.setEstadoId(2L);

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.findById(2L)).thenReturn(Optional.of(nuevoEstado));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VentaResponseDTO res = ventaService.patch(1L, dto);

        assertEquals("Anulada", res.getEstadoNombre());
        // usuario / metodo pago se mantienen
        assertEquals("Juan", res.getUsuarioNombre());
        assertEquals("Tarjeta de Crédito", res.getMetodoPagoNombre());
    }

    // ===================== deleteById =====================

    @Test
    public void testDeleteById_Existe() {
        when(ventaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ventaRepository).deleteById(1L);

        assertDoesNotThrow(() -> ventaService.deleteById(1L));
        verify(ventaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste() {
        when(ventaRepository.existsById(99L)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.deleteById(99L));
        verify(ventaRepository, never()).deleteById(anyLong());
    }

    // ===================== obtenerVentasConDatos =====================

    @Test
    public void testObtenerVentasConDatos() {
        LocalDate fecha = LocalDate.of(2025, 7, 27);
        LocalTime hora = LocalTime.of(14, 30);

        Object[] fila = new Object[] {
                1L,
                fecha,
                hora,
                1L,
                "Juan Pérez",
                "Tarjeta de Crédito",
                "Despacho a domicilio",
                "Pagada"
        };

        when(ventaRepository.obtenerVentasResumen())
                .thenReturn(List.<Object[]>of(fila));

        List<Map<String, Object>> resumen = ventaService.obtenerVentasConDatos();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());

        Map<String, Object> row = resumen.get(0);

        assertEquals(1L, row.get("ID"));
        assertEquals(fecha, row.get("Fecha Compra"));
        assertEquals(hora, row.get("Hora Compra"));
        assertEquals("Juan Pérez", row.get("Usuario"));
        assertEquals("Tarjeta de Crédito", row.get("Método Pago"));
        assertEquals("Despacho a domicilio", row.get("Método Envío"));
        assertEquals("Pagada", row.get("Estado"));
    }

    // ===================== crearVentaDesdeRequest =====================

    @Test
    public void testCrearVentaDesdeRequest_OK() {
        UsuarioModel usuario = usuarioEntity();
        MetodoPagoModel metodoPago = metodoPagoEntity();
        MetodoEnvioModel metodoEnvio = metodoEnvioEntity();
        EstadoModel estado = estadoEntity();

        ProductoModel producto = new ProductoModel();
        producto.setId(5L);
        producto.setNombre("Teclado Mecánico");

        VentaRequestDTO request = ventaRequestDTO();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodoPago));
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(metodoEnvio));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(productoRepository.findById(5L)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(invocation -> {
                    VentaModel v = invocation.getArgument(0);
                    v.setId(10L);
                    return v;
                });

        VentaResponseDTO dto = ventaService.crearVentaDesdeRequest(request, 1L);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getUsuarioId());
        assertEquals("Juan", dto.getUsuarioNombre());
        assertEquals("Tarjeta de Crédito", dto.getMetodoPagoNombre());
        assertNotNull(dto.getDetalles());
        assertEquals(1, dto.getDetalles().size());

        DetalleVentaResponseDTO det = dto.getDetalles().get(0);
        assertEquals(5L, det.getProductoId());
        assertEquals("Teclado Mecánico", det.getProductoNombre());
        assertEquals(2, det.getCantidad());
        assertEquals(10000.0f, det.getPrecioUnitario());
    }
}