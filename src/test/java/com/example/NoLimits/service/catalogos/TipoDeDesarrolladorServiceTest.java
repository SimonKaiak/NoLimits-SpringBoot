package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoDeDesarrolladorRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoDeDesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.service.catalogos.TipoDeDesarrolladorService;

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
public class TipoDeDesarrolladorServiceTest {

    @Autowired
    private TipoDeDesarrolladorService service;

    @MockBean
    private TipoDeDesarrolladorRepository tipoRepo;

    @MockBean
    private TiposDeDesarrolladorRepository tpRepo;

    // ================== HELPERS ==================

    private TipoDeDesarrolladorModel tipo() {
        TipoDeDesarrolladorModel t = new TipoDeDesarrolladorModel();
        t.setId(1L);
        t.setNombre("Estudio");
        return t;
    }

    private TipoDeDesarrolladorRequestDTO createRequestDTO() {
        TipoDeDesarrolladorRequestDTO dto = new TipoDeDesarrolladorRequestDTO();
        dto.setNombre("Publisher");
        return dto;
    }

    private TipoDeDesarrolladorUpdateDTO createUpdateDTO() {
        TipoDeDesarrolladorUpdateDTO dto = new TipoDeDesarrolladorUpdateDTO();
        dto.setNombre("Co-desarrollador");
        return dto;
    }

    // ================== TESTS FIND ==================

    @Test
    void testFindAll() {
        when(tipoRepo.findAll()).thenReturn(List.of(tipo()));

        List<TipoDeDesarrolladorResponseDTO> lista = service.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(1L, lista.get(0).getId());
        assertEquals("Estudio", lista.get(0).getNombre());
    }

    @Test
    void testFindById_Existe() {
        when(tipoRepo.findById(1L)).thenReturn(Optional.of(tipo()));

        TipoDeDesarrolladorResponseDTO t = service.findById(1L);

        assertNotNull(t);
        assertEquals(1L, t.getId());
        assertEquals("Estudio", t.getNombre());
    }

    @Test
    void testFindById_NoExiste_Lanza404() {
        when(tipoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.findById(99L));
    }

    // ================== TESTS SAVE ==================

    @Test
    void testSave_Ok() {
        TipoDeDesarrolladorRequestDTO input = new TipoDeDesarrolladorRequestDTO();
        input.setNombre("  Publisher  ");

        when(tipoRepo.existsByNombreIgnoreCase("Publisher")).thenReturn(false);
        when(tipoRepo.save(any(TipoDeDesarrolladorModel.class)))
                .thenAnswer(invocation -> {
                    TipoDeDesarrolladorModel t = invocation.getArgument(0);
                    t.setId(5L);
                    return t;
                });

        TipoDeDesarrolladorResponseDTO saved = service.save(input);

        assertNotNull(saved);
        assertEquals(5L, saved.getId());
        assertEquals("Publisher", saved.getNombre());
    }

    @Test
    void testSave_NombreNull_LanzaIllegalArgument() {
        TipoDeDesarrolladorRequestDTO input = new TipoDeDesarrolladorRequestDTO();
        input.setNombre(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.save(input));

        verify(tipoRepo, never()).save(any(TipoDeDesarrolladorModel.class));
    }

    @Test
    void testSave_NombreVacio_LanzaIllegalArgument() {
        TipoDeDesarrolladorRequestDTO input = new TipoDeDesarrolladorRequestDTO();
        input.setNombre("   ");

        assertThrows(IllegalArgumentException.class,
                () -> service.save(input));

        verify(tipoRepo, never()).save(any(TipoDeDesarrolladorModel.class));
    }

    @Test
    void testSave_NombreDuplicado_LanzaIllegalArgument() {
        TipoDeDesarrolladorRequestDTO input = new TipoDeDesarrolladorRequestDTO();
        input.setNombre("Publisher");

        when(tipoRepo.existsByNombreIgnoreCase("Publisher")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.save(input));

        verify(tipoRepo, never()).save(any(TipoDeDesarrolladorModel.class));
    }

    // ================== TESTS UPDATE ==================

    @Test
    void testUpdate_CambiaNombre() {
        TipoDeDesarrolladorModel original = tipo();
        TipoDeDesarrolladorUpdateDTO cambios = new TipoDeDesarrolladorUpdateDTO();
        cambios.setNombre("Co-desarrollador");

        when(tipoRepo.findById(1L)).thenReturn(Optional.of(original));
        when(tipoRepo.existsByNombreIgnoreCase("Co-desarrollador")).thenReturn(false);
        when(tipoRepo.save(any(TipoDeDesarrolladorModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TipoDeDesarrolladorResponseDTO actualizado = service.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("Co-desarrollador", actualizado.getNombre());
    }

    @Test
    void testUpdate_NombreVacio_LanzaIllegalArgument() {
        TipoDeDesarrolladorModel original = tipo();
        TipoDeDesarrolladorUpdateDTO cambios = new TipoDeDesarrolladorUpdateDTO();
        cambios.setNombre("   ");

        when(tipoRepo.findById(1L)).thenReturn(Optional.of(original));

        assertThrows(IllegalArgumentException.class,
                () -> service.update(1L, cambios));
    }

    @Test
    void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        TipoDeDesarrolladorModel original = tipo();
        TipoDeDesarrolladorUpdateDTO cambios = new TipoDeDesarrolladorUpdateDTO();
        cambios.setNombre("Publisher");

        when(tipoRepo.findById(1L)).thenReturn(Optional.of(original));
        when(tipoRepo.existsByNombreIgnoreCase("Publisher")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.update(1L, cambios));
    }

    @Test
    void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {
        TipoDeDesarrolladorUpdateDTO cambios = createUpdateDTO();

        when(tipoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.update(99L, cambios));
    }

    // ================== TESTS PATCH (delegado a update) ==================

    @Test
    void testPatch_CambiaNombre() {
        TipoDeDesarrolladorModel original = tipo();
        TipoDeDesarrolladorUpdateDTO cambios = new TipoDeDesarrolladorUpdateDTO();
        cambios.setNombre("Co-desarrollador");

        when(tipoRepo.findById(1L)).thenReturn(Optional.of(original));
        when(tipoRepo.existsByNombreIgnoreCase("Co-desarrollador")).thenReturn(false);
        when(tipoRepo.save(any(TipoDeDesarrolladorModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TipoDeDesarrolladorResponseDTO actualizado = service.patch(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("Co-desarrollador", actualizado.getNombre());
    }

    // ================== TESTS DELETE ==================

    @Test
    void testDeleteById_SinRelaciones() {
        when(tipoRepo.findById(1L)).thenReturn(Optional.of(tipo()));
        when(tpRepo.existsByTipoDeDesarrollador_Id(1L)).thenReturn(false);

        service.deleteById(1L);

        verify(tipoRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_ConRelaciones_LanzaIllegalState() {
        when(tipoRepo.findById(1L)).thenReturn(Optional.of(tipo()));
        when(tpRepo.existsByTipoDeDesarrollador_Id(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> service.deleteById(1L));

        verify(tipoRepo, never()).deleteById(any(Long.class));
    }

    @Test
    void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(tipoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.deleteById(99L));

        verify(tipoRepo, never()).deleteById(any(Long.class));
    }
}