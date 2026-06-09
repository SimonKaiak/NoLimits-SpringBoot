package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.EmpresaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresaService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class EmpresaServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private EmpresaService empresaService;

    @MockBean
    private EmpresaRepository empresaRepository;

    // ================== HELPERS ==================

    private EmpresaModel entity() {
        EmpresaModel e = new EmpresaModel();
        e.setId(1L);
        e.setNombre("Sony");
        e.setActivo(true);
        return e;
    }

    private EmpresaRequestDTO req(String nombre, Boolean activo) {
        EmpresaRequestDTO dto = new EmpresaRequestDTO();
        dto.setNombre(nombre);
        dto.setActivo(activo);
        return dto;
    }

    private EmpresaUpdateDTO upd(String nombre, Boolean activo) {
        EmpresaUpdateDTO dto = new EmpresaUpdateDTO();
        dto.setNombre(nombre);
        dto.setActivo(activo);
        return dto;
    }

    // ================== FIND ==================

    @Test
    public void testFindAll() {
        when(empresaRepository.findAll()).thenReturn(List.of(entity()));
        List<EmpresaResponseDTO> lista = empresaService.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Sony", lista.get(0).getNombre());
    }

    @Test
    public void testFindById_Existe() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        EmpresaResponseDTO dto = empresaService.findById(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Sony", dto.getNombre());
    }

    @Test
    public void testFindById_NoExiste_LanzaRecursoNoEncontrado() {
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> empresaService.findById(99L));
    }

    // ================== CREATE ==================

    @Test
    public void testCreate_Valido() {
        when(empresaRepository.existsByNombreIgnoreCase("Microsoft")).thenReturn(false);
        when(empresaRepository.save(any())).thenAnswer(inv -> {
            EmpresaModel e = inv.getArgument(0);
            e.setId(2L);
            return e;
        });
        EmpresaResponseDTO dto = empresaService.create(req("Microsoft", true));
        assertNotNull(dto);
        assertEquals("Microsoft", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testCreate_ActivoNull_UsaDefaultDelModelo() {
        // activo null → no se llama setActivo → queda el default del modelo
        when(empresaRepository.existsByNombreIgnoreCase("Microsoft")).thenReturn(false);
        when(empresaRepository.save(any())).thenAnswer(inv -> {
            EmpresaModel e = inv.getArgument(0);
            e.setId(2L);
            return e;
        });
        EmpresaResponseDTO dto = empresaService.create(req("Microsoft", null));
        assertNotNull(dto);
        // solo verificamos que no lanza excepción y guarda
        verify(empresaRepository, times(1)).save(any());
    }

    @Test
    public void testCreate_ActivoFalse_RespetaValor() {
        when(empresaRepository.existsByNombreIgnoreCase("Microsoft")).thenReturn(false);
        when(empresaRepository.save(any())).thenAnswer(inv -> {
            EmpresaModel e = inv.getArgument(0);
            e.setId(2L);
            return e;
        });
        EmpresaResponseDTO dto = empresaService.create(req("Microsoft", false));
        assertNotNull(dto);
        assertFalse(dto.getActivo());
    }

    @Test
    public void testCreate_NombreNull_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> empresaService.create(req(null, true)));
        verify(empresaRepository, never()).save(any());
    }

    @Test
    public void testCreate_NombreVacio_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> empresaService.create(req("  ", true)));
        verify(empresaRepository, never()).save(any());
    }

    @Test
    public void testCreate_NombreDuplicado_LanzaIllegalArgument() {
        when(empresaRepository.existsByNombreIgnoreCase("Sony")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> empresaService.create(req("Sony", true)));
    }

    // ================== UPDATE ==================

    @Test
    public void testUpdate_CambiaNombreYActivo_Valido() {
        EmpresaModel existente = entity();
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Microsoft")).thenReturn(false);
        when(empresaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EmpresaResponseDTO dto = empresaService.update(1L, req("Microsoft", false));
        assertEquals("Microsoft", dto.getNombre());
        assertFalse(dto.getActivo());
    }

    @Test
    public void testUpdate_MismoNombre_NoVerificaDuplicado() {
        EmpresaModel existente = entity(); // nombre = "Sony"
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EmpresaResponseDTO dto = empresaService.update(1L, req("Sony", false));
        assertEquals("Sony", dto.getNombre());
        verify(empresaRepository, never()).existsByNombreIgnoreCase(any());
    }

    @Test
    public void testUpdate_ActivoNull_MantieneSinCambio() {
        EmpresaModel existente = entity(); // activo = true
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Microsoft")).thenReturn(false);
        when(empresaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EmpresaResponseDTO dto = empresaService.update(1L, req("Microsoft", null));
        assertEquals("Microsoft", dto.getNombre());
        assertTrue(dto.getActivo()); // activo no cambió
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class, () -> empresaService.update(1L, req("  ", null)));
        verify(empresaRepository, never()).save(any());
    }

    @Test
    public void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        EmpresaModel existente = entity();
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Microsoft")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> empresaService.update(1L, req("Microsoft", null)));
    }

    @Test
    public void testUpdate_NoExiste_Lanza404() {
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> empresaService.update(99L, req("X", null)));
    }

    // ================== PATCH ==================

    @Test
    public void testPatch_CambiaNombre_Valido() {
        EmpresaModel existente = entity();
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Microsoft")).thenReturn(false);
        when(empresaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EmpresaResponseDTO dto = empresaService.patch(1L, upd("Microsoft", null));
        assertEquals("Microsoft", dto.getNombre());
        assertTrue(dto.getActivo()); // no cambió
    }

    @Test
    public void testPatch_NombreNull_NoModifica() {
        // Si nombre es null, no entra al if → nombre no cambia
        EmpresaModel existente = entity();
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EmpresaResponseDTO dto = empresaService.patch(1L, upd(null, null));
        assertEquals("Sony", dto.getNombre()); // no cambió
    }

    @Test
    public void testPatch_CambiaActivo_Valido() {
        EmpresaModel existente = entity();
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EmpresaResponseDTO dto = empresaService.patch(1L, upd(null, false));
        assertFalse(dto.getActivo());
        assertEquals("Sony", dto.getNombre()); // no cambió
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class, () -> empresaService.patch(1L, upd("  ", null)));
    }

    @Test
    public void testPatch_NombreDuplicado_LanzaIllegalArgument() {
        EmpresaModel existente = entity();
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Microsoft")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> empresaService.patch(1L, upd("Microsoft", null)));
    }

    @Test
    public void testPatch_IdNoExiste_LanzaRecursoNoEncontrado() {
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> empresaService.patch(99L, upd("X", null)));
    }

    // ================== DELETE ==================

    @Test
    public void testDeleteById_Existe_Elimina() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        empresaService.deleteById(1L);
        verify(empresaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> empresaService.deleteById(99L));
    }

    // ================== PAGINACIÓN ==================

    @Test
    public void testFindAllPaged() {
        Page<EmpresaModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(empresaRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = empresaService.findAllPaged(1, 10);
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        assertEquals("Sony", resultado.getContenido().get(0).getNombre());
    }
}