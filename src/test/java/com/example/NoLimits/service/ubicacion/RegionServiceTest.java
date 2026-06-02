package com.example.NoLimits.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.RegionRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.RegionResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.RegionUpdateDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.RegionRepository;
import com.example.NoLimits.Multimedia.service.ubicacion.RegionService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * RegionServiceTest — Pruebas unitarias del servicio de regiones.
 *
 * Cubre: findAll, findById, save, update, patch, deleteById.
 */
@SpringBootTest
@ActiveProfiles("test")
public class RegionServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private RegionService regionService;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private ComunaRepository comunaRepository;

    // ===================== HELPERS =====================

    private RegionModel crearRegion() {
        RegionModel r = new RegionModel();
        r.setId(1L);
        r.setNombre("Región Metropolitana");
        return r;
    }

    // ===================== FIND ALL =====================

    @Test
    public void testFindAll_DevuelveLista() {
        when(regionRepository.findAll()).thenReturn(List.of(crearRegion()));

        List<RegionResponseDTO> result = regionService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        RegionResponseDTO dto = result.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Región Metropolitana", dto.getNombre());

        verify(regionRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_ListaVacia_DevuelveVacio() {
        when(regionRepository.findAll()).thenReturn(List.of());

        List<RegionResponseDTO> result = regionService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAll_MultipleRegiones_DevuelveTodas() {
        RegionModel r2 = new RegionModel();
        r2.setId(2L);
        r2.setNombre("Región de Valparaíso");

        when(regionRepository.findAll()).thenReturn(List.of(crearRegion(), r2));

        List<RegionResponseDTO> result = regionService.findAll();

        assertEquals(2, result.size());
        assertEquals("Región Metropolitana", result.get(0).getNombre());
        assertEquals("Región de Valparaíso", result.get(1).getNombre());
    }

    // ===================== FIND BY ID =====================

    @Test
    public void testFindById_Existe_DevuelveDTO() {
        when(regionRepository.findById(1L)).thenReturn(Optional.of(crearRegion()));

        RegionResponseDTO result = regionService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Región Metropolitana", result.getNombre());
    }

    @Test
    public void testFindById_NoExiste_LanzaRecursoNoEncontrado() {
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> regionService.findById(99L));
    }

    // ===================== SAVE =====================

    @Test
    public void testSave_OK_CreaRegion() {
        RegionRequestDTO dto = new RegionRequestDTO();
        dto.setNombre("  Región X  ");

        when(regionRepository.save(any(RegionModel.class)))
                .thenAnswer(inv -> {
                    RegionModel r = inv.getArgument(0);
                    r.setId(5L);
                    return r;
                });

        RegionResponseDTO result = regionService.save(dto);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("Región X", result.getNombre()); // nombre normalizado sin espacios extra
        verify(regionRepository, times(1)).save(any(RegionModel.class));
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgumentException() {
        RegionRequestDTO dto = new RegionRequestDTO();
        dto.setNombre("   "); // solo espacios

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> regionService.save(dto));

        assertTrue(ex.getMessage().contains("nombre de la región es obligatorio"));
        verify(regionRepository, never()).save(any(RegionModel.class));
    }

    @Test
    public void testSave_NombreNull_LanzaIllegalArgumentException() {
        RegionRequestDTO dto = new RegionRequestDTO();
        dto.setNombre(null);

        assertThrows(IllegalArgumentException.class,
                () -> regionService.save(dto));

        verify(regionRepository, never()).save(any(RegionModel.class));
    }

    @Test
    public void testSave_NombreConEspacios_SeNormaliza() {
        RegionRequestDTO dto = new RegionRequestDTO();
        dto.setNombre("  Región del Maule  ");

        when(regionRepository.save(any(RegionModel.class)))
                .thenAnswer(inv -> {
                    RegionModel r = inv.getArgument(0);
                    r.setId(7L);
                    return r;
                });

        RegionResponseDTO result = regionService.save(dto);

        assertEquals("Región del Maule", result.getNombre());
    }

    // ===================== UPDATE (PUT) =====================

    @Test
    public void testUpdate_CambiaNombre() {
        RegionModel existente = crearRegion();

        RegionUpdateDTO in = new RegionUpdateDTO();
        in.setNombre("  Nueva Región  ");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(regionRepository.save(any(RegionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RegionResponseDTO result = regionService.update(1L, in);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nueva Región", result.getNombre());
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        RegionModel existente = crearRegion();

        RegionUpdateDTO in = new RegionUpdateDTO();
        in.setNombre("   ");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> regionService.update(1L, in));

        verify(regionRepository, never()).save(any(RegionModel.class));
    }

    @Test
    public void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {
        RegionUpdateDTO in = new RegionUpdateDTO();
        in.setNombre("Región Nueva");

        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> regionService.update(99L, in));

        verify(regionRepository, never()).save(any(RegionModel.class));
    }

    @Test
    public void testUpdate_SinNombre_MantieneValorActual() {
        RegionModel existente = crearRegion();

        RegionUpdateDTO in = new RegionUpdateDTO();
        in.setNombre(null); // no viene nombre en el DTO

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(regionRepository.save(any(RegionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RegionResponseDTO result = regionService.update(1L, in);

        assertNotNull(result);
        assertEquals("Región Metropolitana", result.getNombre()); // sin cambios
    }

    // ===================== PATCH =====================

    @Test
    public void testPatch_CambiaNombre() {
        RegionModel existente = crearRegion();

        RegionUpdateDTO in = new RegionUpdateDTO();
        in.setNombre("  Región Patch  ");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(regionRepository.save(any(RegionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RegionResponseDTO result = regionService.patch(1L, in);

        assertNotNull(result);
        assertEquals("Región Patch", result.getNombre());
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgumentException() {
        RegionModel existente = crearRegion();

        RegionUpdateDTO in = new RegionUpdateDTO();
        in.setNombre("   ");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> regionService.patch(1L, in));

        verify(regionRepository, never()).save(any(RegionModel.class));
    }

    @Test
    public void testPatch_IdNoExiste_LanzaRecursoNoEncontrado() {
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> regionService.patch(99L, new RegionUpdateDTO()));
    }

    // ===================== DELETE =====================

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

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> regionService.deleteById(99L));

        verify(regionRepository, never()).deleteById(anyLong());
    }
}