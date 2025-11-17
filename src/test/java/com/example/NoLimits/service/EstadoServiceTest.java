package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.EstadoModel;
import com.example.NoLimits.Multimedia.repository.EstadoRepository;
import com.example.NoLimits.Multimedia.service.EstadoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EstadoServiceTest {

    @Autowired
    private EstadoService estadoService;

    @MockBean
    private EstadoRepository estadoRepository;

    private EstadoModel createEstado() {
        EstadoModel e = new EstadoModel();
        e.setId(1L);
        e.setNombre("PAGADA");
        e.setDescripcion("Pago confirmado por la pasarela");
        e.setActivo(true);
        return e;
    }

    @Test
    public void testFindAll() {
        when(estadoRepository.findAll()).thenReturn(List.of(createEstado()));

        List<EstadoModel> estados = estadoService.findAll();

        assertNotNull(estados);
        assertEquals(1, estados.size());
        assertEquals("PAGADA", estados.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(createEstado()));

        EstadoModel estado = estadoService.findById(1L);

        assertNotNull(estado);
        assertEquals(1L, estado.getId());
        assertEquals("PAGADA", estado.getNombre());
        assertTrue(Boolean.TRUE.equals(estado.getActivo()));
    }

    @Test
    public void testFindById_NoExiste() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> estadoService.findById(99L));

        verify(estadoRepository, times(1)).findById(99L);
    }

    @Test
    public void testSave_OK() {
        EstadoModel entrada = new EstadoModel();
        entrada.setNombre("  ENVIADA  ");
        entrada.setDescripcion("Pedido enviado al cliente");
        entrada.setActivo(true);

        when(estadoRepository.save(any(EstadoModel.class)))
                .thenAnswer(invocation -> {
                    EstadoModel e = invocation.getArgument(0);
                    // simular que la BD asigna ID
                    e.setId(1L);
                    return e;
                });

        EstadoModel guardado = estadoService.save(entrada);

        assertNotNull(guardado);
        assertEquals(1L, guardado.getId());
        // Debe venir trimmeado
        assertEquals("ENVIADA", guardado.getNombre());
        assertEquals("Pedido enviado al cliente", guardado.getDescripcion());
        assertTrue(Boolean.TRUE.equals(guardado.getActivo()));
    }

    @Test
    public void testSave_NombreVacio() {
        EstadoModel entrada = new EstadoModel();
        entrada.setNombre("   "); // solo espacios

        assertThrows(IllegalArgumentException.class, () -> estadoService.save(entrada));

        verify(estadoRepository, never()).save(any(EstadoModel.class));
    }

    @Test
    public void testUpdate() {
        EstadoModel original = createEstado(); // PAGADA
        EstadoModel cambios = new EstadoModel();
        cambios.setNombre("  ENVIADA  ");
        cambios.setDescripcion("Pedido enviado");

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));
        when(estadoRepository.save(any(EstadoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EstadoModel actualizado = estadoService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals(1L, actualizado.getId());
        // nombre trimmeado
        assertEquals("ENVIADA", actualizado.getNombre());
        assertEquals("Pedido enviado", actualizado.getDescripcion());
        // activo no se toca en update
        assertTrue(Boolean.TRUE.equals(actualizado.getActivo()));
    }

    @Test
    public void testUpdate_NombreVacioLanzaExcepcion() {
        EstadoModel original = createEstado();
        EstadoModel cambios = new EstadoModel();
        cambios.setNombre("   "); // vacío después de trim

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));

        assertThrows(IllegalArgumentException.class, () -> estadoService.update(1L, cambios));

        verify(estadoRepository, never()).save(any(EstadoModel.class));
    }

    @Test
    public void testDeleteById_Existe() {
        EstadoModel existente = createEstado();

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        doNothing().when(estadoRepository).deleteById(1L);

        estadoService.deleteById(1L);

        verify(estadoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> estadoService.deleteById(99L));

        verify(estadoRepository, never()).deleteById(anyLong());
    }
}