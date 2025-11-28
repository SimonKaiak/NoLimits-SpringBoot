// Ruta: src/test/java/com/example/NoLimits/service/DireccionServiceTest.java
package com.example.NoLimits.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.service.ubicacion.DireccionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@SpringBootTest
@ActiveProfiles("test")
public class DireccionServiceTest {

    @Autowired
    private DireccionService direccionService;

    @MockBean
    private DireccionRepository direccionRepository;

    @MockBean
    private ComunaRepository comunaRepository;

    private DireccionModel crearDireccion() {
        RegionModel region = new RegionModel();
        region.setId(1L);
        region.setNombre("Región Metropolitana");

        ComunaModel comuna = new ComunaModel();
        comuna.setId(10L);
        comuna.setNombre("Santiago");
        comuna.setRegion(region);

        DireccionModel d = new DireccionModel();
        d.setId(100L);
        d.setCalle("Siempre Viva");
        d.setNumero("742");
        d.setComuna(comuna);
        d.setCodigoPostal("8320000");
        return d;
    }

    @Test
    public void testFindAll() {
        when(direccionRepository.findAll()).thenReturn(Arrays.asList(crearDireccion()));

        List<DireccionModel> result = direccionService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(direccionRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Existe() {
        DireccionModel d = crearDireccion();
        when(direccionRepository.findById(100L)).thenReturn(Optional.of(d));

        DireccionModel result = direccionService.findById(100L);

        assertNotNull(result);
        assertEquals("Siempre Viva", result.getCalle());
    }

    @Test
    public void testFindById_NoExiste() {
        when(direccionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> direccionService.findById(999L));
    }

    @Test
    public void testSave_ComunaNoEspecificada_LanzaIllegalArgumentException() {
        DireccionModel d = new DireccionModel();
        d.setCalle("X");
        d.setNumero("1");
        d.setComuna(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> direccionService.save(d));

        assertTrue(ex.getMessage().contains("Debe especificar una comuna válida"));
        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    @Test
    public void testSave_ComunaNoEncontrada_LanzaRecursoNoEncontrado() {
        DireccionModel d = new DireccionModel();
        d.setCalle("X");
        d.setNumero("1");

        ComunaModel comunaRef = new ComunaModel();
        comunaRef.setId(10L);
        d.setComuna(comunaRef);

        when(comunaRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> direccionService.save(d));

        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    @Test
    public void testSave_OK() {
        DireccionModel d = new DireccionModel();
        d.setCalle(" X ");
        d.setNumero("742");

        ComunaModel comunaRef = new ComunaModel();
        comunaRef.setId(10L);
        d.setComuna(comunaRef);

        ComunaModel comunaBD = new ComunaModel();
        comunaBD.setId(10L);
        comunaBD.setNombre("Santiago");

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(comunaBD));
        when(direccionRepository.save(any(DireccionModel.class))).thenAnswer(inv -> inv.getArgument(0));

        DireccionModel result = direccionService.save(d);

        assertNotNull(result);
        assertEquals(" X ", result.getCalle()); // no la estás trimmeando en el service
        assertEquals(10L, result.getComuna().getId());
    }

    @Test
    public void testPatch_CambiaCalleYNumero() {
        DireccionModel existente = crearDireccion();

        DireccionModel entrada = new DireccionModel();
        entrada.setCalle("Nueva Calle");
        entrada.setNumero("123");

        when(direccionRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(direccionRepository.save(any(DireccionModel.class))).thenAnswer(inv -> inv.getArgument(0));

        DireccionModel result = direccionService.patch(100L, entrada);

        assertEquals("Nueva Calle", result.getCalle());
        assertEquals("123", result.getNumero());
    }

    @Test
    public void testDeleteById_OK() {
        DireccionModel existente = crearDireccion();
        when(direccionRepository.findById(100L)).thenReturn(Optional.of(existente));

        direccionService.deleteById(100L);

        verify(direccionRepository, times(1)).delete(existente);
    }
}