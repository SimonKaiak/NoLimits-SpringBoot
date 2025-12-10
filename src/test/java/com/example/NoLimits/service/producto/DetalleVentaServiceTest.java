package com.example.NoLimits.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.producto.request.DetalleVentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.DetalleVentaUpdateDTO;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.service.producto.DetalleVentaService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DetalleVentaServiceTest {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @MockBean
    private DetalleVentaRepository detalleVentaRepository;

    @MockBean
    private VentaRepository ventaRepository;

    @MockBean
    private ProductoRepository productoRepository;

    // ================== HELPERS ==================

    private VentaModel createVenta(Long id) {
        VentaModel v = new VentaModel();
        v.setId(id);
        v.setFechaCompra(LocalDate.now());
        v.setHoraCompra(LocalTime.now());
        return v;
    }

    private ProductoModel createProducto(Long id, String nombre, double precio) {
        ProductoModel p = new ProductoModel();
        p.setId(id);
        p.setNombre(nombre);
        p.setPrecio(precio);
        return p;
    }

    private DetalleVentaModel createDetalle() {
        DetalleVentaModel d = new DetalleVentaModel();
        d.setId(1L);
        d.setVenta(createVenta(1L));
        d.setProducto(createProducto(10L, "Producto test", 1000.0));
        d.setCantidad(2);
        d.setPrecioUnitario(1000f);
        return d;
    }

    // ================== FIND ==================

    @Test
    public void testFindAll() {
        when(detalleVentaRepository.findAll()).thenReturn(List.of(createDetalle()));

        List<DetalleVentaResponseDTO> lista = detalleVentaService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        DetalleVentaResponseDTO dto = lista.get(0);
        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getProductoId());
        assertEquals(2, dto.getCantidad());
        assertEquals(1000f, dto.getPrecioUnitario());
    }

    @Test
    public void testFindById_Existe() {
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(createDetalle()));

        DetalleVentaResponseDTO dto = detalleVentaService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(2, dto.getCantidad());
    }

    @Test
    public void testFindById_NoExiste() {
        when(detalleVentaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> detalleVentaService.findById(99L));
    }

    // ================== SAVE ==================

    @Test
    public void testSave_Exito() {
        DetalleVentaRequestDTO request = new DetalleVentaRequestDTO();
        request.setProductoId(10L);
        request.setCantidad(3);
        request.setPrecioUnitario(1500f);

        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(createProducto(10L, "Producto test", 1000.0)));

        when(detalleVentaRepository.save(any(DetalleVentaModel.class)))
                .thenAnswer(invocation -> {
                    DetalleVentaModel d = invocation.getArgument(0);
                    d.setId(1L);
                    return d;
                });

        DetalleVentaResponseDTO dto = detalleVentaService.save(request);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getProductoId());
        assertEquals(3, dto.getCantidad());
        assertEquals(1500f, dto.getPrecioUnitario());
    }

    @Test
    public void testSave_ProductoNull_LanzaIllegalArgument() {
        DetalleVentaRequestDTO request = new DetalleVentaRequestDTO();
        request.setProductoId(null);
        request.setCantidad(1);
        request.setPrecioUnitario(1000f);

        assertThrows(IllegalArgumentException.class,
                () -> detalleVentaService.save(request));

        verify(productoRepository, never()).findById(any(Long.class));
        verify(detalleVentaRepository, never()).save(any(DetalleVentaModel.class));
    }

    @Test
    public void testSave_ProductoNoExiste_Lanza404() {
        DetalleVentaRequestDTO request = new DetalleVentaRequestDTO();
        request.setProductoId(99L);
        request.setCantidad(1);
        request.setPrecioUnitario(1000f);

        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> detalleVentaService.save(request));
    }

    @Test
    public void testSave_CantidadInvalida_LanzaIllegalArgument() {
        DetalleVentaRequestDTO request = new DetalleVentaRequestDTO();
        request.setProductoId(10L);
        request.setCantidad(0);
        request.setPrecioUnitario(1000f);

        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(createProducto(10L, "Producto test", 1000.0)));

        assertThrows(IllegalArgumentException.class,
                () -> detalleVentaService.save(request));

        verify(detalleVentaRepository, never()).save(any(DetalleVentaModel.class));
    }

    @Test
    public void testSave_PrecioInvalido_LanzaIllegalArgument() {
        DetalleVentaRequestDTO request = new DetalleVentaRequestDTO();
        request.setProductoId(10L);
        request.setCantidad(1);
        request.setPrecioUnitario(-5f);

        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(createProducto(10L, "Producto test", 1000.0)));

        assertThrows(IllegalArgumentException.class,
                () -> detalleVentaService.save(request));

        verify(detalleVentaRepository, never()).save(any(DetalleVentaModel.class));
    }

    // ================== UPDATE ==================

    @Test
    public void testUpdate_Exito() {
        DetalleVentaModel existente = createDetalle();

        DetalleVentaUpdateDTO update = new DetalleVentaUpdateDTO();
        update.setProductoId(20L);
        update.setCantidad(5);
        update.setPrecioUnitario(2000f);
        update.setVentaId(2L);

        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.findById(20L))
                .thenReturn(Optional.of(createProducto(20L, "Producto nuevo", 2000.0)));
        when(ventaRepository.findById(2L))
                .thenReturn(Optional.of(createVenta(2L)));
        when(detalleVentaRepository.save(any(DetalleVentaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DetalleVentaResponseDTO dto = detalleVentaService.update(1L, update);

        assertNotNull(dto);
        assertEquals(5, dto.getCantidad());
        assertEquals(2000f, dto.getPrecioUnitario());
        assertEquals(20L, dto.getProductoId());
    }

    @Test
    public void testUpdate_ProductoNull_LanzaIllegalArgument() {
        DetalleVentaModel existente = createDetalle();

        DetalleVentaUpdateDTO update = new DetalleVentaUpdateDTO();
        update.setProductoId(null);
        update.setCantidad(5);
        update.setPrecioUnitario(2000f);

        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> detalleVentaService.update(1L, update));
    }

    @Test
    public void testUpdate_ProductoNoExiste_Lanza404() {
        DetalleVentaModel existente = createDetalle();

        DetalleVentaUpdateDTO update = new DetalleVentaUpdateDTO();
        update.setProductoId(99L);
        update.setCantidad(5);
        update.setPrecioUnitario(2000f);

        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> detalleVentaService.update(1L, update));
    }

    @Test
    public void testUpdate_CantidadInvalida_LanzaIllegalArgument() {
        DetalleVentaModel existente = createDetalle();

        DetalleVentaUpdateDTO update = new DetalleVentaUpdateDTO();
        update.setProductoId(10L);
        update.setCantidad(0);
        update.setPrecioUnitario(2000f);

        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(createProducto(10L, "Producto test", 1000.0)));

        assertThrows(IllegalArgumentException.class,
                () -> detalleVentaService.update(1L, update));
    }

    @Test
    public void testUpdate_PrecioInvalido_LanzaIllegalArgument() {
        DetalleVentaModel existente = createDetalle();

        DetalleVentaUpdateDTO update = new DetalleVentaUpdateDTO();
        update.setProductoId(10L);
        update.setCantidad(1);
        update.setPrecioUnitario(-10f);

        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(createProducto(10L, "Producto test", 1000.0)));

        assertThrows(IllegalArgumentException.class,
                () -> detalleVentaService.update(1L, update));
    }

    // ================== PATCH ==================

    @Test
    public void testPatch_CambiaVentaYProductoYCantidadYPrecio() {
        DetalleVentaModel existente = createDetalle();

        DetalleVentaUpdateDTO patch = new DetalleVentaUpdateDTO();
        patch.setVentaId(2L);
        patch.setProductoId(20L);
        patch.setCantidad(5);
        patch.setPrecioUnitario(2500f);

        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ventaRepository.findById(2L))
                .thenReturn(Optional.of(createVenta(2L)));
        when(productoRepository.findById(20L))
                .thenReturn(Optional.of(createProducto(20L, "Producto nuevo", 2500.0)));
        when(detalleVentaRepository.save(any(DetalleVentaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DetalleVentaResponseDTO dto = detalleVentaService.patch(1L, patch);

        assertNotNull(dto);
        assertEquals(5, dto.getCantidad());
        assertEquals(2500f, dto.getPrecioUnitario());
        assertEquals(20L, dto.getProductoId());
    }

    @Test
    public void testPatch_IdNoExiste_Lanza404() {
        DetalleVentaUpdateDTO patch = new DetalleVentaUpdateDTO();
        patch.setCantidad(5);

        when(detalleVentaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> detalleVentaService.patch(99L, patch));
    }

    @Test
    public void testPatch_CantidadInvalida_LanzaIllegalArgument() {
        DetalleVentaModel existente = createDetalle();

        DetalleVentaUpdateDTO patch = new DetalleVentaUpdateDTO();
        patch.setCantidad(0);

        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> detalleVentaService.patch(1L, patch));
    }

    @Test
    public void testPatch_PrecioInvalido_LanzaIllegalArgument() {
        DetalleVentaModel existente = createDetalle();

        DetalleVentaUpdateDTO patch = new DetalleVentaUpdateDTO();
        patch.setPrecioUnitario(-5f);

        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> detalleVentaService.patch(1L, patch));
    }

    // ================== DELETE ==================

    @Test
    public void testDeleteById_Existe_Elimina() {
        DetalleVentaModel existente = createDetalle();
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));

        detalleVentaService.deleteById(1L);

        verify(detalleVentaRepository, times(1)).delete(existente);
    }

    @Test
    public void testDeleteById_NoExiste_Lanza404() {
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> detalleVentaService.deleteById(1L));

        verify(detalleVentaRepository, never()).delete(any(DetalleVentaModel.class));
    }

    // ================== FIND BY VENTA ==================

    @Test
    public void testFindByVenta() {
        when(detalleVentaRepository.findByVenta_Id(1L))
                .thenReturn(List.of(createDetalle()));

        List<DetalleVentaResponseDTO> lista = detalleVentaService.findByVenta(1L);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(1L, lista.get(0).getId());
    }
}