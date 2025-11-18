package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.model.VentaModel;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.VentaRepository;
import com.example.NoLimits.Multimedia.service.DetalleVentaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

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

    private VentaModel createVenta() {
        VentaModel v = new VentaModel();
        v.setId(1L);
        v.setFechaCompra(LocalDate.now());
        v.setHoraCompra(LocalTime.now());
        return v;
    }

    private ProductoModel createProducto() {
        ProductoModel p = new ProductoModel();
        p.setId(10L);
        p.setNombre("Producto test");
        p.setPrecio(1000.0);
        return p;
    }

    private DetalleVentaModel createDetalle() {
        DetalleVentaModel d = new DetalleVentaModel();
        d.setId(1L);
        d.setVenta(createVenta());
        d.setProducto(createProducto());
        d.setCantidad(2);
        d.setPrecioUnitario(1000f);
        return d;
    }

    @Test
    public void testFindAll() {
        when(detalleVentaRepository.findAll()).thenReturn(List.of(createDetalle()));
        var lista = detalleVentaService.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
    }

    @Test
    public void testFindById_Existe() {
        DetalleVentaModel d = createDetalle();
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(d));
        DetalleVentaModel res = detalleVentaService.findById(1L);
        assertNotNull(res);
        assertEquals(2, res.getCantidad());
    }

    @Test
    public void testFindById_NoExiste() {
        when(detalleVentaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> detalleVentaService.findById(99L));
    }

    @Test
    public void testSave() {
        DetalleVentaModel entrada = new DetalleVentaModel();
        VentaModel ventaRef = new VentaModel();
        ventaRef.setId(1L);
        ProductoModel prodRef = new ProductoModel();
        prodRef.setId(10L);

        entrada.setVenta(ventaRef);
        entrada.setProducto(prodRef);
        entrada.setCantidad(3);
        entrada.setPrecioUnitario(1500f);

        VentaModel ventaReal = createVenta();
        ProductoModel prodReal = createProducto();

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaReal));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(prodReal));
        when(detalleVentaRepository.save(any(DetalleVentaModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        DetalleVentaModel res = detalleVentaService.save(entrada);

        assertNotNull(res);
        assertEquals(3, res.getCantidad());
        assertEquals(1500f, res.getPrecioUnitario());
        assertEquals(ventaReal, res.getVenta());
        assertEquals(prodReal, res.getProducto());
    }

    @Test
    public void testPatch() {
        DetalleVentaModel original = createDetalle();
        DetalleVentaModel patch = new DetalleVentaModel();
        patch.setCantidad(5);
        patch.setPrecioUnitario(2000f);

        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(original));
        when(detalleVentaRepository.save(any(DetalleVentaModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        DetalleVentaModel res = detalleVentaService.patch(1L, patch);

        assertEquals(5, res.getCantidad());
        assertEquals(2000f, res.getPrecioUnitario());
    }

    @Test
    public void testDeleteById() {
        DetalleVentaModel existente = createDetalle();
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(existente));
        doNothing().when(detalleVentaRepository).delete(existente);

        detalleVentaService.deleteById(1L);

        verify(detalleVentaRepository, times(1)).delete(existente);
    }

    @Test
    public void testFindByVenta() {
        when(detalleVentaRepository.findByVenta_Id(1L)).thenReturn(List.of(createDetalle()));
        var lista = detalleVentaService.findByVenta(1L);
        assertNotNull(lista);
        assertEquals(1, lista.size());
    }
}