package com.example.NoLimits.service.venta;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.producto.request.DetalleVentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.venta.request.VentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.venta.update.VentaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
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
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * VentaServiceTest — Pruebas unitarias del servicio de ventas.
 *
 * Cubre: findAll, findById, findByMetodoPago, findByFechaCompra,
 * findByHoraCompra, findByUsuarioYMetodoPago, save, update, patch,
 * deleteById, crearVentaDesdeRequest.
 */
@SpringBootTest
@ActiveProfiles("test")
public class VentaServiceTest extends AbstractContainerBaseTest {

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
        u.setCorreo("juan@test.com");
        u.setTelefono(123456789L);
        u.setPassword("$2a$10$hash");
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
        m.setDescripcion("Entrega en domicilio");
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
        v.setFechaCompra(LocalDate.of(2025, 7, 6));
        v.setHoraCompra(LocalTime.of(14, 30));
        v.setUsuarioModel(usuarioEntity());
        v.setMetodoPagoModel(metodoPagoEntity());
        v.setMetodoEnvioModel(metodoEnvioEntity());
        v.setEstado(estadoEntity());
        v.setDetalles(new ArrayList<>());
        return v;
    }

    private VentaModel ventaConCamposRequeridos() {
        VentaModel v = new VentaModel();
        UsuarioModel u = new UsuarioModel(); u.setId(1L);
        MetodoPagoModel mp = new MetodoPagoModel(); mp.setId(1L);
        MetodoEnvioModel me = new MetodoEnvioModel(); me.setId(1L);
        EstadoModel es = new EstadoModel(); es.setId(1L);
        v.setUsuarioModel(u);
        v.setMetodoPagoModel(mp);
        v.setMetodoEnvioModel(me);
        v.setEstado(es);
        return v;
    }

    // ===================== FIND ALL =====================

    @Test
    public void testFindAll_DevuelveLista() {
        when(ventaRepository.findAll()).thenReturn(List.of(ventaEntity()));

        List<VentaResponseDTO> result = ventaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_ListaVacia_DevuelveVacio() {
        when(ventaRepository.findAll()).thenReturn(List.of());

        List<VentaResponseDTO> result = ventaService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ===================== FIND BY ID =====================

    @Test
    public void testFindById_Existe_DevuelveDTO() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaEntity()));

        VentaResponseDTO result = ventaService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDate.of(2025, 7, 6), result.getFechaCompra());
    }

    @Test
    public void testFindById_NoExiste_LanzaRecursoNoEncontrado() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.findById(99L));
    }

    // ===================== FIND POR FILTROS =====================

    @Test
    public void testFindByMetodoPago_DevuelveLista() {
        when(ventaRepository.findByMetodoPagoModel_Id(1L)).thenReturn(List.of(ventaEntity()));

        List<VentaResponseDTO> result = ventaService.findByMetodoPago(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tarjeta de Crédito", result.get(0).getMetodoPagoNombre());
    }

    @Test
    public void testFindByFechaCompra_DevuelveLista() {
        LocalDate fecha = LocalDate.of(2025, 7, 6);
        when(ventaRepository.findByFechaCompra(fecha)).thenReturn(List.of(ventaEntity()));

        List<VentaResponseDTO> result = ventaService.findByFechaCompra(fecha);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(fecha, result.get(0).getFechaCompra());
    }

    @Test
    public void testFindByHoraCompra_DevuelveLista() {
        LocalTime hora = LocalTime.of(14, 30);
        when(ventaRepository.findByHoraCompra(hora)).thenReturn(List.of(ventaEntity()));

        List<VentaResponseDTO> result = ventaService.findByHoraCompra(hora);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByUsuarioYMetodoPago_DevuelveLista() {
        when(ventaRepository.findByUsuarioModel_IdAndMetodoPagoModel_Id(1L, 1L))
                .thenReturn(List.of(ventaEntity()));

        List<VentaResponseDTO> result = ventaService.findByUsuarioYMetodoPago(1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        assertEquals(1L, result.get(0).getMetodoPagoId());
    }

    // ===================== SAVE =====================

    @Test
    public void testSave_OK_GuardaVenta() {
        VentaModel venta = ventaConCamposRequeridos();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodoPagoEntity()));
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(metodoEnvioEntity()));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEntity()));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(inv -> {
                    VentaModel v = inv.getArgument(0);
                    v.setId(10L);
                    return v;
                });

        VentaResponseDTO result = ventaService.save(venta);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(ventaRepository, times(1)).save(any(VentaModel.class));
    }

    @Test
    public void testSave_SinUsuario_LanzaRecursoNoEncontrado() {
        VentaModel venta = new VentaModel();
        // sin usuario

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.save(venta));

        verify(ventaRepository, never()).save(any());
    }

    @Test
    public void testSave_SinMetodoPago_LanzaRecursoNoEncontrado() {
        VentaModel venta = new VentaModel();
        UsuarioModel u = new UsuarioModel(); u.setId(1L);
        venta.setUsuarioModel(u);
        // sin método de pago

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.save(venta));
    }

    @Test
    public void testSave_SinMetodoEnvio_LanzaRecursoNoEncontrado() {
        VentaModel venta = new VentaModel();
        UsuarioModel u = new UsuarioModel(); u.setId(1L);
        MetodoPagoModel mp = new MetodoPagoModel(); mp.setId(1L);
        venta.setUsuarioModel(u);
        venta.setMetodoPagoModel(mp);
        // sin método de envío

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.save(venta));
    }

    @Test
    public void testSave_SinEstado_LanzaRecursoNoEncontrado() {
        VentaModel venta = new VentaModel();
        UsuarioModel u = new UsuarioModel(); u.setId(1L);
        MetodoPagoModel mp = new MetodoPagoModel(); mp.setId(1L);
        MetodoEnvioModel me = new MetodoEnvioModel(); me.setId(1L);
        venta.setUsuarioModel(u);
        venta.setMetodoPagoModel(mp);
        venta.setMetodoEnvioModel(me);
        // sin estado

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.save(venta));
    }

    @Test
    public void testSave_SinFecha_AsignaFechaActual() {
        VentaModel venta = ventaConCamposRequeridos();
        // No se setea fecha ni hora

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodoPagoEntity()));
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(metodoEnvioEntity()));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEntity()));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(inv -> {
                    VentaModel v = inv.getArgument(0);
                    v.setId(5L);
                    // Verifica que la fecha se haya asignado automáticamente
                    assertNotNull(v.getFechaCompra());
                    assertNotNull(v.getHoraCompra());
                    return v;
                });

        VentaResponseDTO result = ventaService.save(venta);
        assertNotNull(result);
    }

    // ===================== UPDATE =====================

    @Test
    public void testUpdate_OK_CambiaMetodoPago() {
        MetodoPagoModel nuevoMetodo = new MetodoPagoModel();
        nuevoMetodo.setId(2L);
        nuevoMetodo.setNombre("Transferencia");
        nuevoMetodo.setActivo(true);

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaEntity()));
        when(metodoPagoRepository.findById(2L)).thenReturn(Optional.of(nuevoMetodo));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        VentaUpdateDTO dto = new VentaUpdateDTO();
        dto.setMetodoPagoId(2L);

        VentaResponseDTO result = ventaService.update(1L, dto);

        assertNotNull(result);
        assertEquals("Transferencia", result.getMetodoPagoNombre());
    }

    @Test
    public void testUpdate_CambiaFechaYHora() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaEntity()));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        LocalDate nuevaFecha = LocalDate.of(2025, 12, 25);
        LocalTime nuevaHora = LocalTime.of(18, 0);

        VentaUpdateDTO dto = new VentaUpdateDTO();
        dto.setFechaCompra(nuevaFecha);
        dto.setHoraCompra(nuevaHora);

        VentaResponseDTO result = ventaService.update(1L, dto);

        assertNotNull(result);
        assertEquals(nuevaFecha, result.getFechaCompra());
        assertEquals(nuevaHora, result.getHoraCompra());
    }

    @Test
    public void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.update(99L, new VentaUpdateDTO()));

        verify(ventaRepository, never()).save(any());
    }

    // ===================== PATCH =====================

    @Test
    public void testPatch_ModificaSoloEstado() {
        EstadoModel nuevoEstado = new EstadoModel();
        nuevoEstado.setId(2L);
        nuevoEstado.setNombre("Enviada");
        nuevoEstado.setActivo(true);

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaEntity()));
        when(estadoRepository.findById(2L)).thenReturn(Optional.of(nuevoEstado));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        VentaUpdateDTO dto = new VentaUpdateDTO();
        dto.setEstadoId(2L);

        VentaResponseDTO result = ventaService.patch(1L, dto);

        assertNotNull(result);
        assertEquals("Enviada", result.getEstadoNombre());
        // Método de pago no debe cambiar
        assertEquals("Tarjeta de Crédito", result.getMetodoPagoNombre());
    }

    // ===================== DELETE =====================

    @Test
    public void testDeleteById_Existe_EliminaOK() {
        when(ventaRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> ventaService.deleteById(1L));

        verify(ventaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(ventaRepository.existsById(99L)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> ventaService.deleteById(99L));

        verify(ventaRepository, never()).deleteById(anyLong());
    }

    // ===================== CREAR VENTA DESDE REQUEST =====================

    @Test
    public void testCrearVentaDesdeRequest_OK_ConDetalles() {
        ProductoModel producto = new ProductoModel();
        producto.setId(1L);
        producto.setNombre("The Witcher 3");

        DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
        detalle.setProductoId(1L);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(29990f);

        VentaRequestDTO request = new VentaRequestDTO();
        request.setMetodoPagoId(1L);
        request.setMetodoEnvioId(1L);
        request.setEstadoId(1L);
        request.setDetalles(List.of(detalle));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodoPagoEntity()));
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(metodoEnvioEntity()));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEntity()));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(inv -> {
                    VentaModel v = inv.getArgument(0);
                    v.setId(20L);
                    return v;
                });

        VentaResponseDTO result = ventaService.crearVentaDesdeRequest(request, 1L);

        assertNotNull(result);
        assertEquals(20L, result.getId());
        verify(productoRepository, times(1)).findById(1L);
        verify(ventaRepository, times(1)).save(any(VentaModel.class));
    }

    @Test
    public void testCrearVentaDesdeRequest_UsuarioNoEncontrado_LanzaExcepcion() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setMetodoPagoId(1L);
        request.setMetodoEnvioId(1L);
        request.setEstadoId(1L);

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> ventaService.crearVentaDesdeRequest(request, 99L));
    }

    @Test
    public void testCrearVentaDesdeRequest_ProductoNoEncontrado_LanzaExcepcion() {
        DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
        detalle.setProductoId(999L);
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(9990f);

        VentaRequestDTO request = new VentaRequestDTO();
        request.setMetodoPagoId(1L);
        request.setMetodoEnvioId(1L);
        request.setEstadoId(1L);
        request.setDetalles(List.of(detalle));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodoPagoEntity()));
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(metodoEnvioEntity()));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEntity()));
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> ventaService.crearVentaDesdeRequest(request, 1L));
    }

    @Test
    public void testCrearVentaDesdeRequest_SinDetalles_GuardaVentaVacia() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setMetodoPagoId(1L);
        request.setMetodoEnvioId(1L);
        request.setEstadoId(1L);
        request.setDetalles(null); // sin detalles

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodoPagoEntity()));
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(metodoEnvioEntity()));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEntity()));
        when(ventaRepository.save(any(VentaModel.class)))
                .thenAnswer(inv -> {
                    VentaModel v = inv.getArgument(0);
                    v.setId(21L);
                    return v;
                });

        VentaResponseDTO result = ventaService.crearVentaDesdeRequest(request, 1L);

        assertNotNull(result);
        assertEquals(21L, result.getId());
        verify(productoRepository, never()).findById(anyLong());
    }

    @Test
    public void testObtenerVentasConDatos_OK() {
        Object[] fila = new Object[] {
            1L,
            LocalDate.of(2025, 7, 6),
            LocalTime.of(14, 30),
            1L,                      // UsuarioID
            "Juan",                  // Usuario
            "Tarjeta de Crédito",    // Método Pago
            "Despacho a domicilio",  // Método Envío
            "Pagada"                 // Estado
        };
        List<Object[]> filas = new ArrayList<>();
        filas.add(fila);
        when(ventaRepository.obtenerVentasResumen()).thenReturn(filas);

        List<Map<String, Object>> result = ventaService.obtenerVentasConDatos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).get("ID"));
    }

    @Test
    public void testFindAllPaged_OK() {
        Page<VentaModel> page = new PageImpl<>(List.of(ventaEntity()));
        when(ventaRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        var result = ventaService.findAllPaged(1, 10);

        assertNotNull(result);
        assertEquals(1, result.getContenido().size());
        assertEquals(1L, result.getContenido().get(0).getId());
    }

    @Test
    public void testFindMisComprasPaged_OK() {
        Page<VentaModel> page = new PageImpl<>(List.of(ventaEntity()));
        when(ventaRepository.findByUsuarioModel_Id(eq(1L), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        var result = ventaService.findMisComprasPaged(1L, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getContenido().size());
        assertEquals(1L, result.getContenido().get(0).getId());
    }
}