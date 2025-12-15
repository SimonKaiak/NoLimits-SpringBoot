// Ruta: src/test/java/com/example/NoLimits/service/ComunaServiceTest.java
package com.example.NoLimits.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ComunaModel;
import com.example.NoLimits.Multimedia.model.RegionModel;
import com.example.NoLimits.Multimedia.repository.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.RegionRepository;
import com.example.NoLimits.Multimedia.service.ComunaService;
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
public class ComunaServiceTest {

    @Autowired
    private ComunaService comunaService;

    @MockBean
    private ComunaRepository comunaRepository;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private DireccionRepository direccionRepository;

    private ComunaModel crearComuna() {
        RegionModel region = new RegionModel();
        region.setId(1L);
        region.setNombre("Región Metropolitana");

        ComunaModel comuna = new ComunaModel();
        comuna.setId(10L);
        comuna.setNombre("Santiago");
        comuna.setRegion(region);
        return comuna;
    }

    @Test
    public void testFindAll() {
        ComunaModel c1 = crearComuna();
        when(comunaRepository.findAll()).thenReturn(Arrays.asList(c1));

        List<ComunaModel> result = comunaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(comunaRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Existe() {
        ComunaModel c1 = crearComuna();
        when(comunaRepository.findById(10L)).thenReturn(Optional.of(c1));

        ComunaModel result = comunaService.findById(10L);

        assertNotNull(result);
        assertEquals("Santiago", result.getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(comunaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> comunaService.findById(999L));
    }

    @Test
    public void testSave_NombreNulo_LanzaIllegalArgumentException() {
        ComunaModel comuna = new ComunaModel();
        comuna.setNombre(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> comunaService.save(comuna));

        assertTrue(ex.getMessage().contains("nombre de la comuna es obligatorio"));
        verify(comunaRepository, never()).save(any(ComunaModel.class));
    }

    @Test
    public void testSave_RegionNoEspecificada_LanzaIllegalArgumentException() {
        ComunaModel comuna = new ComunaModel();
        comuna.setNombre("   Santiago   ");
        comuna.setRegion(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> comunaService.save(comuna));

        assertTrue(ex.getMessage().contains("Debe especificar una región válida"));
        verify(comunaRepository, never()).save(any(ComunaModel.class));
    }

    @Test
    public void testSave_Valida_OK() {
        RegionModel region = new RegionModel();
        region.setId(1L);
        region.setNombre("Región Metropolitana");

        ComunaModel entrada = new ComunaModel();
        entrada.setNombre("  Santiago  ");
        RegionModel regionRef = new RegionModel();
        regionRef.setId(1L);
        entrada.setRegion(regionRef);

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));
        when(comunaRepository.save(any(ComunaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ComunaModel result = comunaService.save(entrada);

        assertNotNull(result);
        assertEquals("Santiago", result.getNombre());
        assertNotNull(result.getRegion());
        assertEquals(1L, result.getRegion().getId());
        verify(regionRepository, times(1)).findById(1L);
        verify(comunaRepository, times(1)).save(any(ComunaModel.class));
    }

    @Test
    public void testUpdate_CambiaNombreYRegion() {
        ComunaModel existente = crearComuna(); // nombre: Santiago, region.id = 1
        RegionModel nuevaRegion = new RegionModel();
        nuevaRegion.setId(2L);
        nuevaRegion.setNombre("Otra Región");

        ComunaModel detalles = new ComunaModel();
        detalles.setNombre("  Ñuñoa  ");
        RegionModel regionRef = new RegionModel();
        regionRef.setId(2L);
        detalles.setRegion(regionRef);

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(2L)).thenReturn(Optional.of(nuevaRegion));
        when(comunaRepository.save(any(ComunaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ComunaModel result = comunaService.update(10L, detalles);

        assertEquals("Ñuñoa", result.getNombre());
        assertEquals(2L, result.getRegion().getId());
    }

    @Test
    public void testDeleteById_ConDirecciones_LanzaIllegalStateException() {
        when(comunaRepository.findById(10L)).thenReturn(Optional.of(crearComuna()));
        when(direccionRepository.existsByComuna_Id(10L)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> comunaService.deleteById(10L));

        assertTrue(ex.getMessage().contains("la comuna tiene direcciones asociadas"));
        verify(comunaRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteById_SinDirecciones_EliminaOK() {
        when(comunaRepository.findById(10L)).thenReturn(Optional.of(crearComuna()));
        when(direccionRepository.existsByComuna_Id(10L)).thenReturn(false);

        comunaService.deleteById(10L);

        verify(comunaRepository, times(1)).deleteById(10L);
    }
}