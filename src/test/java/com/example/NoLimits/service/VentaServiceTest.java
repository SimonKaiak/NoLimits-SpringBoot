package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.*;
import com.example.NoLimits.Multimedia.repository.*;
import com.example.NoLimits.Multimedia.service.VentaService;

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

    private VentaModel createVenta() {
        var venta = new VentaModel();
        venta.setId(1L);
        venta.setFechaCompra(LocalDate.of(2025, 7, 27));
        venta.setHoraCompra(LocalTime.of(14, 30));
        venta.setUsuarioModel(new UsuarioModel(1L, "Juan", "Pérez", "correo@test.com", 123456789, "password", null, null));
        venta.setMetodoPagoModel(new MetodoPagoModel(1L, "Tarjeta de Crédito", null));
        venta.setMetodoEnvioModel(new MetodoEnvioModel(1L, "Despacho a domicilio", true, null));
        venta.setEstado(new EstadoModel(1L, "Pagada", true, null));
        return venta;
    }

    @Test
    public void testFindAll() {
        when(ventaRepository.findAll()).thenReturn(List.of(createVenta()));
        var ventas = ventaService.findAll();
        assertNotNull(ventas);
        assertEquals(1, ventas.size());
    }

    @Test
    public void testFindById() {
        var venta = createVenta();
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        var resultado = ventaService.findById(1L);
        assertNotNull(resultado);
        assertEquals("Tarjeta de Crédito", resultado.getMetodoPagoModel().getNombre());
        assertEquals("Juan", resultado.getUsuarioModel().getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> ventaService.findById(99L));
    }

    @Test
    public void testSave() {
        var venta = createVenta();

        // Stubs para resolutores de FK
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(venta.getUsuarioModel()));
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(venta.getMetodoPagoModel()));
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(venta.getMetodoEnvioModel()));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(venta.getEstado()));

        when(ventaRepository.save(any(VentaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        var saved = ventaService.save(venta);
        assertNotNull(saved);
        assertEquals("Pagada", saved.getEstado().getNombre());
    }

    @Test
    public void testPatchVenta() {
        var original = createVenta();
        var patch = new VentaModel();
        patch.setEstado(new EstadoModel(2L, "Enviada", true, null));

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(original));
        when(estadoRepository.findById(2L)).thenReturn(Optional.of(new EstadoModel(2L, "Enviada", true, null)));
        when(ventaRepository.save(any(VentaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        var patched = ventaService.patchVentaModel(1L, patch);
        assertEquals("Enviada", patched.getEstado().getNombre());
    }

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
        assertThrows(RecursoNoEncontradoException.class, () -> ventaService.deleteById(99L));
        verify(ventaRepository, never()).deleteById(anyLong());
    }
}