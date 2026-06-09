package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.PlataformaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.PlataformaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformaRepository;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformaService;
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
public class PlataformaServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private PlataformaService plataformaService;

    @MockBean
    private PlataformaRepository plataformaRepository;

    // ================== HELPERS ==================

    private PlataformaModel entity() {
        PlataformaModel p = new PlataformaModel();
        p.setId(1L);
        p.setNombre("PlayStation");
        return p;
    }

    private PlataformaRequestDTO req(String nombre) {
        PlataformaRequestDTO dto = new PlataformaRequestDTO();
        dto.setNombre(nombre);
        return dto;
    }

    private PlataformaUpdateDTO upd(String nombre) {
        PlataformaUpdateDTO dto = new PlataformaUpdateDTO();
        dto.setNombre(nombre);
        return dto;
    }

    // ================== TESTS FIND ==================

    @Test
    void testFindAll() {
        when(plataformaRepository.findAll()).thenReturn(List.of(entity()));
        List<PlataformaResponseDTO> lista = plataformaService.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("PlayStation", lista.get(0).getNombre());
    }

    @Test
    void testFindById_Exists() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        PlataformaResponseDTO dto = plataformaService.findById(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("PlayStation", dto.getNombre());
    }

    @Test
    void testFindById_NotFound() {
        when(plataformaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> plataformaService.findById(99L));
    }

    // ================== TESTS SAVE ==================

    @Test
    void testSave_Ok() {
        when(plataformaRepository.save(any())).thenAnswer(inv -> {
            PlataformaModel p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });
        PlataformaResponseDTO dto = plataformaService.save(req("PC"));
        assertNotNull(dto);
        assertEquals("PC", dto.getNombre());
    }

    @Test
    void testSave_NombreNull() {
        assertThrows(IllegalArgumentException.class, () -> plataformaService.save(req(null)));
        verify(plataformaRepository, never()).save(any());
    }

    @Test
    void testSave_NombreVacio() {
        assertThrows(IllegalArgumentException.class, () -> plataformaService.save(req("   ")));
        verify(plataformaRepository, never()).save(any());
    }

    // ================== TESTS UPDATE ==================

    @Test
    void testUpdate_CambiaNombreValido() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(plataformaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PlataformaResponseDTO dto = plataformaService.update(1L, upd("Xbox"));
        assertEquals("Xbox", dto.getNombre());
    }

    @Test
    void testUpdate_NombreVacio_LanzaIllegalArgument() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class, () -> plataformaService.update(1L, upd("   ")));
        verify(plataformaRepository, never()).save(any());
    }

    @Test
    void testUpdate_NombreNull_NoModificaNombre() {
        // Si nombre es null en el update, no cambia
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(plataformaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PlataformaResponseDTO dto = plataformaService.update(1L, upd(null));
        assertEquals("PlayStation", dto.getNombre()); // nombre no cambió
    }

    @Test
    void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {
        when(plataformaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> plataformaService.update(99L, upd("Xbox")));
    }

    // ================== TESTS PATCH (delega a update) ==================

    @Test
    void testPatch_CambiaNombreValido() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(plataformaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PlataformaResponseDTO dto = plataformaService.patch(1L, upd("Nintendo Switch"));
        assertEquals("Nintendo Switch", dto.getNombre());
    }

    @Test
    void testPatch_NombreNull_NoModificaNombre() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(plataformaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PlataformaResponseDTO dto = plataformaService.patch(1L, upd(null));
        assertEquals("PlayStation", dto.getNombre()); // nombre no cambió
    }

    // ================== TESTS DELETE ==================

    @Test
    void testDeleteById_Existe_Elimina() {
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(entity()));
        plataformaService.deleteById(1L);
        verify(plataformaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound_LanzaRecursoNoEncontrado() {
        when(plataformaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> plataformaService.deleteById(99L));
    }

    // ================== PAGINACIÓN ==================

    @Test
    void testListarPaginado_SinBusqueda() {
        Page<PlataformaModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(plataformaRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = plataformaService.listarPaginado(1, 10, null);
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        assertEquals("PlayStation", resultado.getContenido().get(0).getNombre());
    }

    @Test
    void testListarPaginado_ConBusqueda() {
        Page<PlataformaModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(plataformaRepository.findByNombreContainingIgnoreCase(any(), any(Pageable.class)))
                .thenReturn(page);

        var resultado = plataformaService.listarPaginado(1, 10, "play");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }

    @Test
    void testListarPaginado_BusquedaBlanco_UsaTodos() {
        Page<PlataformaModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(plataformaRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = plataformaService.listarPaginado(1, 10, "   ");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }
}