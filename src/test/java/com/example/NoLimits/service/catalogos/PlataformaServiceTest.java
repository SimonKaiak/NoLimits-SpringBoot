package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.PlataformaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.PlataformaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformaRepository;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformaService;
import com.example.NoLimits.config.AbstractContainerBaseTest;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PlataformaServiceTest extends AbstractContainerBaseTest{

    @Autowired
    private PlataformaService plataformaService;

    @MockBean
    private PlataformaRepository plataformaRepository;

    // ================== HELPERS ==================

    private PlataformaModel createPlataforma() {
        PlataformaModel p = new PlataformaModel();
        p.setId(1L);
        p.setNombre("PlayStation");
        return p;
    }

    private PlataformaRequestDTO createRequestDTO() {
        PlataformaRequestDTO dto = new PlataformaRequestDTO();
        dto.setNombre("PlayStation");
        return dto;
    }

    private PlataformaUpdateDTO createUpdateDTO() {
        PlataformaUpdateDTO dto = new PlataformaUpdateDTO();
        dto.setNombre("Xbox");
        return dto;
    }

    // ================== TESTS FIND ==================

    @Test
    void testFindAll() {
        when(plataformaRepository.findAll()).thenReturn(List.of(createPlataforma()));

        List<PlataformaResponseDTO> lista = plataformaService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(1L, lista.get(0).getId());
        assertEquals("PlayStation", lista.get(0).getNombre());
        verify(plataformaRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Exists() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(createPlataforma()));

        PlataformaResponseDTO p = plataformaService.findById(1L);

        assertNotNull(p);
        assertEquals(1L, p.getId());
        assertEquals("PlayStation", p.getNombre());
    }

    @Test
    void testFindById_NotFound() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> plataformaService.findById(1L));
    }

    // ================== TESTS SAVE ==================

    @Test
    void testSave_Ok() {
        PlataformaRequestDTO request = new PlataformaRequestDTO();
        request.setNombre("  PlayStation  ");

        when(plataformaRepository.save(any(PlataformaModel.class)))
                .thenAnswer(invocation -> {
                    PlataformaModel m = invocation.getArgument(0);
                    m.setId(1L);
                    return m;
                });

        PlataformaResponseDTO creado = plataformaService.save(request);

        assertNotNull(creado);
        assertEquals(1L, creado.getId());
        assertEquals("PlayStation", creado.getNombre()); // normalizado
    }

    @Test
    void testSave_NombreNull() {
        PlataformaRequestDTO request = new PlataformaRequestDTO();
        request.setNombre(null);

        assertThrows(IllegalArgumentException.class,
                () -> plataformaService.save(request));
    }

    @Test
    void testSave_NombreVacio() {
        PlataformaRequestDTO request = new PlataformaRequestDTO();
        request.setNombre("   ");

        assertThrows(IllegalArgumentException.class,
                () -> plataformaService.save(request));
    }

    // ================== TESTS UPDATE ==================

    @Test
    void testUpdate_CambiaNombreValido() {
        PlataformaModel existente = createPlataforma();

        PlataformaUpdateDTO entrada = new PlataformaUpdateDTO();
        entrada.setNombre("Xbox");

        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(plataformaRepository.save(any(PlataformaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlataformaResponseDTO actualizado = plataformaService.update(1L, entrada);

        assertNotNull(actualizado);
        assertEquals("Xbox", actualizado.getNombre());
    }

    @Test
    void testUpdate_NombreVacio_LanzaIllegalArgument() {
        PlataformaModel existente = createPlataforma();

        PlataformaUpdateDTO entrada = new PlataformaUpdateDTO();
        entrada.setNombre("   ");

        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> plataformaService.update(1L, entrada));
    }

    @Test
    void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {
        PlataformaUpdateDTO entrada = createUpdateDTO();

        when(plataformaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> plataformaService.update(99L, entrada));
    }

    // ================== TESTS PATCH (delegado a update) ==================

    @Test
    void testPatch_CambiaNombreValido() {
        PlataformaModel existente = createPlataforma();

        PlataformaUpdateDTO entrada = new PlataformaUpdateDTO();
        entrada.setNombre("Steam");

        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(plataformaRepository.save(any(PlataformaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlataformaResponseDTO actualizado = plataformaService.patch(1L, entrada);

        assertNotNull(actualizado);
        assertEquals("Steam", actualizado.getNombre());
    }

    // ================== TESTS DELETE ==================

    @Test
    void testDeleteById_Existe_Elimina() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(createPlataforma()));

        plataformaService.deleteById(1L);

        verify(plataformaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound_LanzaRecursoNoEncontrado() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> plataformaService.deleteById(1L));
    }

    // ================== TESTS PAGINACIÓN ==================

    @Test
    void testListarPaginado_SinBusqueda() {
        PlataformaModel plataforma = createPlataforma();

        Page<PlataformaModel> pageResult =
                new PageImpl<>(List.of(plataforma), PageRequest.of(0, 10), 1);

        when(plataformaRepository.findAll(any(Pageable.class)))
                .thenReturn(pageResult);

        PagedResponse<PlataformaResponseDTO> resultado =
                plataformaService.listarPaginado(1, 10, null);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        assertEquals(1, resultado.getPagina());
        assertEquals(1, resultado.getTotalPaginas());
        assertEquals(1, resultado.getTotalElementos());
        assertEquals("PlayStation", resultado.getContenido().get(0).getNombre());
    }

    @Test
    void testListarPaginado_ConBusqueda() {
        PlataformaModel plataforma = createPlataforma();

        Page<PlataformaModel> pageResult =
                new PageImpl<>(List.of(plataforma), PageRequest.of(0, 10), 1);

        when(plataformaRepository.findByNombreContainingIgnoreCase(
                any(String.class),
                any(Pageable.class)
        )).thenReturn(pageResult);

        PagedResponse<PlataformaResponseDTO> resultado =
                plataformaService.listarPaginado(1, 10, " play ");

        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        assertEquals(1, resultado.getPagina());
        assertEquals(1, resultado.getTotalPaginas());
        assertEquals(1, resultado.getTotalElementos());
        assertEquals("PlayStation", resultado.getContenido().get(0).getNombre());
    }
}