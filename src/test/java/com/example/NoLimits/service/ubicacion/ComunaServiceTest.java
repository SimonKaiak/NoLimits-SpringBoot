package com.example.NoLimits.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.ComunaRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.ComunaUpdateDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.RegionRepository;
import com.example.NoLimits.Multimedia.service.ubicacion.ComunaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    // ================== HELPERS ==================

    private RegionModel crearRegion(Long id, String nombre) {
        RegionModel region = new RegionModel();
        region.setId(id);
        region.setNombre(nombre);
        return region;
    }

    private ComunaModel crearComuna() {
        RegionModel region = crearRegion(1L, "Región Metropolitana");

        ComunaModel comuna = new ComunaModel();
        comuna.setId(10L);
        comuna.setNombre("Santiago");
        comuna.setRegion(region);
        return comuna;
    }

    // ================== FIND ==================

    @Test
    public void testFindAll() {
        ComunaModel c1 = crearComuna();
        when(comunaRepository.findAll()).thenReturn(List.of(c1));

        List<ComunaResponseDTO> result = comunaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        ComunaResponseDTO dto = result.get(0);
        assertEquals(10L, dto.getId());
        assertEquals("Santiago", dto.getNombre());
        assertEquals(1L, dto.getRegionId());
        assertEquals("Región Metropolitana", dto.getRegionNombre());

        verify(comunaRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Existe() {
        ComunaModel c1 = crearComuna();
        when(comunaRepository.findById(10L)).thenReturn(Optional.of(c1));

        ComunaResponseDTO result = comunaService.findById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Santiago", result.getNombre());
        assertEquals(1L, result.getRegionId());
    }

    @Test
    public void testFindById_NoExiste() {
        when(comunaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> comunaService.findById(999L));
    }

    // ================== CREATE ==================

    @Test
    public void testCreate_NombreNulo_LanzaIllegalArgumentException() {
        ComunaRequestDTO request = new ComunaRequestDTO();
        request.setNombre(null);
        request.setRegionId(1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> comunaService.create(request));

        assertTrue(ex.getMessage().contains("nombre de la comuna es obligatorio"));
        verify(comunaRepository, never()).save(any(ComunaModel.class));
    }

    @Test
    public void testCreate_NombreVacio_LanzaIllegalArgumentException() {
        ComunaRequestDTO request = new ComunaRequestDTO();
        request.setNombre("   ");
        request.setRegionId(1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> comunaService.create(request));

        assertTrue(ex.getMessage().contains("nombre de la comuna es obligatorio"));
        verify(comunaRepository, never()).save(any(ComunaModel.class));
    }

    @Test
    public void testCreate_RegionNoEspecificada_LanzaIllegalArgumentException() {
        ComunaRequestDTO request = new ComunaRequestDTO();
        request.setNombre("Santiago");
        request.setRegionId(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> comunaService.create(request));

        assertTrue(ex.getMessage().contains("Debe especificar una región válida"));
        verify(comunaRepository, never()).save(any(ComunaModel.class));
    }

    @Test
    public void testCreate_RegionNoExiste_LanzaRecursoNoEncontrado() {
        ComunaRequestDTO request = new ComunaRequestDTO();
        request.setNombre("Santiago");
        request.setRegionId(1L);

        when(regionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> comunaService.create(request));

        verify(comunaRepository, never()).save(any(ComunaModel.class));
    }

    @Test
    public void testCreate_Valida_OK() {
        ComunaRequestDTO request = new ComunaRequestDTO();
        request.setNombre("  Santiago  ");
        request.setRegionId(1L);

        RegionModel region = crearRegion(1L, "Región Metropolitana");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));
        when(comunaRepository.save(any(ComunaModel.class))).thenAnswer(inv -> {
            ComunaModel c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });

        ComunaResponseDTO result = comunaService.create(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Santiago", result.getNombre()); // normalizado
        assertEquals(1L, result.getRegionId());
        assertEquals("Región Metropolitana", result.getRegionNombre());

        verify(regionRepository, times(1)).findById(1L);
        verify(comunaRepository, times(1)).save(any(ComunaModel.class));
    }

    // ================== UPDATE ==================

    @Test
    public void testUpdate_CambiaNombreYRegion() {
        ComunaModel existente = crearComuna(); // id 10, nombre Santiago, región 1
        RegionModel nuevaRegion = crearRegion(2L, "Otra Región");

        ComunaUpdateDTO update = new ComunaUpdateDTO();
        update.setNombre("  Ñuñoa  ");
        update.setRegionId(2L);

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(2L)).thenReturn(Optional.of(nuevaRegion));
        when(comunaRepository.save(any(ComunaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ComunaResponseDTO result = comunaService.update(10L, update);

        assertNotNull(result);
        assertEquals("Ñuñoa", result.getNombre());
        assertEquals(2L, result.getRegionId());
        assertEquals("Otra Región", result.getRegionNombre());
    }

    @Test
    public void testUpdate_NombreNulo_LanzaIllegalArgument() {
        ComunaModel existente = crearComuna();

        ComunaUpdateDTO update = new ComunaUpdateDTO();
        update.setNombre(null);
        update.setRegionId(1L);

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> comunaService.update(10L, update));

        assertTrue(ex.getMessage().contains("nombre de la comuna es obligatorio"));
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        ComunaModel existente = crearComuna();

        ComunaUpdateDTO update = new ComunaUpdateDTO();
        update.setNombre("   ");
        update.setRegionId(1L);

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> comunaService.update(10L, update));

        assertTrue(ex.getMessage().contains("nombre no puede estar vacío"));
    }

    @Test
    public void testUpdate_RegionNull_LanzaIllegalArgument() {
        ComunaModel existente = crearComuna();

        ComunaUpdateDTO update = new ComunaUpdateDTO();
        update.setNombre("Santiago");
        update.setRegionId(null);

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> comunaService.update(10L, update));

        assertTrue(ex.getMessage().contains("Debe especificar una región válida"));
    }

    @Test
    public void testUpdate_RegionNoExiste_LanzaRecursoNoEncontrado() {
        ComunaModel existente = crearComuna();

        ComunaUpdateDTO update = new ComunaUpdateDTO();
        update.setNombre("Santiago");
        update.setRegionId(99L);

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> comunaService.update(10L, update));
    }

    // ================== PATCH ==================

    @Test
    public void testPatch_CambiaSoloNombre() {
        ComunaModel existente = crearComuna();

        ComunaUpdateDTO patch = new ComunaUpdateDTO();
        patch.setNombre("Providencia");

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(comunaRepository.save(any(ComunaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ComunaResponseDTO result = comunaService.patch(10L, patch);

        assertNotNull(result);
        assertEquals("Providencia", result.getNombre());
        assertEquals(1L, result.getRegionId()); // región se mantiene
    }

    @Test
    public void testPatch_CambiaSoloRegion() {
        ComunaModel existente = crearComuna();
        RegionModel nuevaRegion = crearRegion(3L, "Nueva Región");

        ComunaUpdateDTO patch = new ComunaUpdateDTO();
        patch.setRegionId(3L);

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(3L)).thenReturn(Optional.of(nuevaRegion));
        when(comunaRepository.save(any(ComunaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ComunaResponseDTO result = comunaService.patch(10L, patch);

        assertNotNull(result);
        assertEquals("Santiago", result.getNombre()); // nombre se mantiene
        assertEquals(3L, result.getRegionId());
        assertEquals("Nueva Región", result.getRegionNombre());
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        ComunaModel existente = crearComuna();

        ComunaUpdateDTO patch = new ComunaUpdateDTO();
        patch.setNombre("   ");

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> comunaService.patch(10L, patch));

        assertTrue(ex.getMessage().contains("nombre no puede estar vacío"));
    }

    @Test
    public void testPatch_RegionNoExiste_LanzaRecursoNoEncontrado() {
        ComunaModel existente = crearComuna();

        ComunaUpdateDTO patch = new ComunaUpdateDTO();
        patch.setRegionId(99L);

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> comunaService.patch(10L, patch));
    }

    @Test
    public void testPatch_IdNoExiste_LanzaRecursoNoEncontrado() {
        ComunaUpdateDTO patch = new ComunaUpdateDTO();
        patch.setNombre("Otra");

        when(comunaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> comunaService.patch(999L, patch));
    }

    // ================== DELETE ==================

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

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(comunaRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> comunaService.deleteById(10L));

        verify(comunaRepository, never()).deleteById(anyLong());
    }
}