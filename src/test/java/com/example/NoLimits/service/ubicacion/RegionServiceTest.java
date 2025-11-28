// Ruta: src/test/java/com/example/NoLimits/service/RegionServiceTest.java
package com.example.NoLimits.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.RegionRepository;
import com.example.NoLimits.Multimedia.service.ubicacion.RegionService;

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
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@SpringBootTest
@ActiveProfiles("test")
public class RegionServiceTest {

    @Autowired
    private RegionService regionService;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private ComunaRepository comunaRepository;

    private RegionModel crearRegion() {
        RegionModel r = new RegionModel();
        r.setId(1L);
        r.setNombre("Región Metropolitana");
        return r;
    }

    @Test
    public void testFindAll() {
        when(regionRepository.findAll()).thenReturn(Arrays.asList(crearRegion()));

        List<RegionModel> result = regionService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(regionRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Existe() {
        RegionModel r = crearRegion();
        when(regionRepository.findById(1L)).thenReturn(Optional.of(r));

        RegionModel result = regionService.findById(1L);

        assertNotNull(result);
        assertEquals("Región Metropolitana", result.getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> regionService.findById(99L));
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgumentException() {
        RegionModel r = new RegionModel();
        r.setNombre("   ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> regionService.save(r));

        assertTrue(ex.getMessage().contains("nombre de la región es obligatorio"));
        verify(regionRepository, never()).save(any(RegionModel.class));
    }

    @Test
    public void testSave_OK() {
        RegionModel entrada = new RegionModel();
        entrada.setNombre("  Región X  ");

        when(regionRepository.save(any(RegionModel.class))).thenAnswer(inv -> inv.getArgument(0));

        RegionModel result = regionService.save(entrada);

        assertEquals("Región X", result.getNombre());
    }

    @Test
    public void testUpdate_CambiaNombre() {
        RegionModel existente = crearRegion();

        RegionModel in = new RegionModel();
        in.setNombre("  Nueva Región  ");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(regionRepository.save(any(RegionModel.class))).thenAnswer(inv -> inv.getArgument(0));

        RegionModel result = regionService.update(1L, in);

        assertEquals("Nueva Región", result.getNombre());
    }

    @Test
    public void testDeleteById_ConComunas_LanzaIllegalStateException() {
        when(regionRepository.findById(1L)).thenReturn(Optional.of(crearRegion()));
        when(comunaRepository.existsByRegion_Id(1L)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> regionService.deleteById(1L));

        assertTrue(ex.getMessage().contains("tiene comunas asociadas"));
        verify(regionRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteById_SinComunas_EliminaOK() {
        when(regionRepository.findById(1L)).thenReturn(Optional.of(crearRegion()));
        when(comunaRepository.existsByRegion_Id(1L)).thenReturn(false);

        regionService.deleteById(1L);

        verify(regionRepository, times(1)).deleteById(1L);
    }
}