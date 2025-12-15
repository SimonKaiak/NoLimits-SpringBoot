package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

import java.util.List;
import java.util.Optional;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.repository.MetodoEnvioRepository;
import com.example.NoLimits.Multimedia.service.MetodoEnvioService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class MetodoEnvioServiceTest {

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @MockBean
    private MetodoEnvioRepository metodoEnvioRepository;

    private MetodoEnvioModel createMetodoEnvio() {
        MetodoEnvioModel m = new MetodoEnvioModel();
        m.setId(1L);
        m.setNombre("Despacho a domicilio");
        m.setDescripcion("Entrega en la casa del cliente");
        m.setActivo(true);
        return m;
    }

    @Test
    public void testFindAll() {
        when(metodoEnvioRepository.findAll()).thenReturn(List.of(createMetodoEnvio()));
        var lista = metodoEnvioService.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
    }

    @Test
    public void testFindById_Existe() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(createMetodoEnvio()));
        MetodoEnvioModel m = metodoEnvioService.findById(1L);
        assertNotNull(m);
        assertEquals("Despacho a domicilio", m.getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(metodoEnvioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> metodoEnvioService.findById(99L));
    }

    @Test
    public void testSave() {
        MetodoEnvioModel in = new MetodoEnvioModel();
        in.setNombre("Retiro en tienda");

        when(metodoEnvioRepository.save(any(MetodoEnvioModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        MetodoEnvioModel saved = metodoEnvioService.save(in);

        assertNotNull(saved);
        assertEquals("Retiro en tienda", saved.getNombre());
        assertEquals(true, saved.getActivo());
    }

    @Test
    public void testSave_NombreVacio() {
        MetodoEnvioModel in = new MetodoEnvioModel();
        in.setNombre("  ");
        assertThrows(IllegalArgumentException.class, () -> metodoEnvioService.save(in));
    }

    @Test
    public void testUpdate() {
        MetodoEnvioModel existente = createMetodoEnvio();
        MetodoEnvioModel in = new MetodoEnvioModel();
        in.setNombre("Retiro en sucursal");
        in.setDescripcion("Retiro presencial");
        in.setActivo(false);

        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoEnvioRepository.save(any(MetodoEnvioModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        MetodoEnvioModel actualizado = metodoEnvioService.update(1L, in);

        assertEquals("Retiro en sucursal", actualizado.getNombre());
        assertEquals("Retiro presencial", actualizado.getDescripcion());
        assertEquals(false, actualizado.getActivo());
    }

    @Test
    public void testPatch() {
        MetodoEnvioModel existente = createMetodoEnvio();
        MetodoEnvioModel in = new MetodoEnvioModel();
        in.setDescripcion("Nueva descripción");
        in.setActivo(false);

        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoEnvioRepository.save(any(MetodoEnvioModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        MetodoEnvioModel patched = metodoEnvioService.patch(1L, in);

        assertEquals("Despacho a domicilio", patched.getNombre());
        assertEquals("Nueva descripción", patched.getDescripcion());
        assertEquals(false, patched.getActivo());
    }

    @Test
    public void testDeleteById() {
        MetodoEnvioModel existente = createMetodoEnvio();
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        doNothing().when(metodoEnvioRepository).deleteById(1L);

        metodoEnvioService.deleteById(1L);

        verify(metodoEnvioRepository, times(1)).deleteById(1L);
    }
}