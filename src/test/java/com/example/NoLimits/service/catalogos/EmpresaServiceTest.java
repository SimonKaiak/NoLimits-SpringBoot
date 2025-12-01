package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.EmpresaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresaService;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class EmpresaServiceTest {

    @Autowired
    private EmpresaService empresaService;

    @MockBean
    private EmpresaRepository empresaRepository;

    // ================== HELPERS ==================

    private EmpresaModel createEmpresa() {
        EmpresaModel e = new EmpresaModel();
        e.setId(1L);
        e.setNombre("Sony Pictures");
        e.setActivo(true);
        return e;
    }

    private EmpresaRequestDTO createRequestDTO() {
        EmpresaRequestDTO dto = new EmpresaRequestDTO();
        dto.setNombre("Sony Pictures");
        dto.setActivo(true);
        return dto;
    }

    private EmpresaUpdateDTO createUpdateDTO() {
        EmpresaUpdateDTO dto = new EmpresaUpdateDTO();
        dto.setNombre("Warner Bros");
        dto.setActivo(false);
        return dto;
    }

    // ================== TESTS LECTURAS (GET) ==================

    @Test
    public void testFindAll() {
        when(empresaRepository.findAll()).thenReturn(List.of(createEmpresa()));

        List<EmpresaResponseDTO> resultado = empresaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sony Pictures", resultado.get(0).getNombre());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    public void testFindById_Existe() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(createEmpresa()));

        EmpresaResponseDTO resultado = empresaService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Sony Pictures", resultado.getNombre());
    }

    @Test
    public void testFindById_NoExiste_LanzaRecursoNoEncontrado() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresaService.findById(1L));
    }

    // ================== TESTS CREATE (POST) ==================

    @Test
    public void testCreate_Valido() {
        EmpresaRequestDTO request = new EmpresaRequestDTO();
        request.setNombre("  Sony Pictures  ");
        // activo null → se usa el valor por defecto de la entidad

        when(empresaRepository.existsByNombreIgnoreCase("Sony Pictures")).thenReturn(false);
        when(empresaRepository.save(any(EmpresaModel.class)))
                .thenAnswer(invocation -> {
                    EmpresaModel entidad = invocation.getArgument(0);
                    entidad.setId(1L);
                    return entidad;
                });

        EmpresaResponseDTO resultado = empresaService.create(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Sony Pictures", resultado.getNombre()); // nombre normalizado
        verify(empresaRepository, times(1)).save(any(EmpresaModel.class));
    }

    @Test
    public void testCreate_NombreNull_LanzaIllegalArgument() {
        EmpresaRequestDTO request = new EmpresaRequestDTO();
        request.setNombre(null);

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.create(request));

        verify(empresaRepository, never()).save(any(EmpresaModel.class));
    }

    @Test
    public void testCreate_NombreVacio_LanzaIllegalArgument() {
        EmpresaRequestDTO request = new EmpresaRequestDTO();
        request.setNombre("   ");

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.create(request));

        verify(empresaRepository, never()).save(any(EmpresaModel.class));
    }

    @Test
    public void testCreate_NombreDuplicado_LanzaIllegalArgument() {
        EmpresaRequestDTO request = new EmpresaRequestDTO();
        request.setNombre("Sony Pictures");

        when(empresaRepository.existsByNombreIgnoreCase("Sony Pictures")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.create(request));

        verify(empresaRepository, never()).save(any(EmpresaModel.class));
    }

    // ================== TESTS UPDATE (PUT) ==================

    @Test
    public void testUpdate_CambiaNombreYActivo_Valido() {
        EmpresaModel existente = createEmpresa();

        EmpresaRequestDTO entrada = new EmpresaRequestDTO();
        entrada.setNombre("Warner Bros");
        entrada.setActivo(false);

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Warner Bros")).thenReturn(false);
        when(empresaRepository.save(any(EmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmpresaResponseDTO resultado = empresaService.update(1L, entrada);

        assertNotNull(resultado);
        assertEquals("Warner Bros", resultado.getNombre());
        assertEquals(false, resultado.getActivo());
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        EmpresaModel existente = createEmpresa();

        EmpresaRequestDTO entrada = new EmpresaRequestDTO();
        entrada.setNombre("   ");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.update(1L, entrada));
    }

    @Test
    public void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        EmpresaModel existente = createEmpresa();

        EmpresaRequestDTO entrada = new EmpresaRequestDTO();
        entrada.setNombre("Warner Bros");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Warner Bros")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.update(1L, entrada));
    }

    // ================== TESTS PATCH ==================

    @Test
    public void testPatch_CambiaNombre_Valido() {
        EmpresaModel existente = createEmpresa();

        EmpresaUpdateDTO entrada = new EmpresaUpdateDTO();
        entrada.setNombre("Warner Bros");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Warner Bros")).thenReturn(false);
        when(empresaRepository.save(any(EmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmpresaResponseDTO resultado = empresaService.patch(1L, entrada);

        assertNotNull(resultado);
        assertEquals("Warner Bros", resultado.getNombre());
    }

    @Test
    public void testPatch_CambiaActivo_Valido() {
        EmpresaModel existente = createEmpresa();

        EmpresaUpdateDTO entrada = new EmpresaUpdateDTO();
        entrada.setActivo(false);

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.save(any(EmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmpresaResponseDTO resultado = empresaService.patch(1L, entrada);

        assertNotNull(resultado);
        assertEquals(false, resultado.getActivo());
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        EmpresaModel existente = createEmpresa();

        EmpresaUpdateDTO entrada = new EmpresaUpdateDTO();
        entrada.setNombre("   ");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.patch(1L, entrada));
    }

    @Test
    public void testPatch_NombreDuplicado_LanzaIllegalArgument() {
        EmpresaModel existente = createEmpresa();

        EmpresaUpdateDTO entrada = new EmpresaUpdateDTO();
        entrada.setNombre("Warner Bros");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Warner Bros")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.patch(1L, entrada));
    }

    @Test
    public void testPatch_IdNoExiste_LanzaRecursoNoEncontrado() {
        EmpresaUpdateDTO entrada = new EmpresaUpdateDTO();
        entrada.setNombre("Warner Bros");

        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresaService.patch(99L, entrada));
    }

    // ================== TESTS DELETE ==================

    @Test
    public void testDeleteById_Existe_Elimina() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(createEmpresa()));

        empresaService.deleteById(1L);

        verify(empresaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresaService.deleteById(1L));

        verify(empresaRepository, never()).deleteById(1L);
    }
}