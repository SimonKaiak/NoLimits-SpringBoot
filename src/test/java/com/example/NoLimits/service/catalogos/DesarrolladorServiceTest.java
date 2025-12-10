package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladorRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.DesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladorService;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@SpringBootTest
@ActiveProfiles("test")
public class DesarrolladorServiceTest {

    @Autowired
    private DesarrolladorService desarrolladorService;

    @MockBean
    private DesarrolladorRepository desarrolladorRepository;

    // ==========================
    // HELPERS
    // ==========================

    private DesarrolladorModel dev() {
        DesarrolladorModel d = new DesarrolladorModel();
        d.setId(1L);
        d.setNombre("Insomniac Games");
        d.setActivo(true);
        return d;
    }

    private DesarrolladorRequestDTO req(String nombre, Boolean activo) {
        DesarrolladorRequestDTO dto = new DesarrolladorRequestDTO();
        dto.setNombre(nombre);
        dto.setActivo(activo);
        return dto;
    }

    private DesarrolladorUpdateDTO upd(String nombre, Boolean activo) {
        DesarrolladorUpdateDTO dto = new DesarrolladorUpdateDTO();
        dto.setNombre(nombre);
        dto.setActivo(activo);
        return dto;
    }

    // ==========================
    // findAll / findById
    // ==========================

    @Test
    void testFindAll() {
        when(desarrolladorRepository.findAll()).thenReturn(List.of(dev()));

        List<DesarrolladorResponseDTO> lista = desarrolladorService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Insomniac Games", lista.get(0).getNombre());
        assertTrue(lista.get(0).getActivo());
    }

    @Test
    void testFindById_Existe() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));

        DesarrolladorResponseDTO dto = desarrolladorService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Insomniac Games", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    void testFindById_NoExiste_Lanza404() {
        when(desarrolladorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladorService.findById(99L));
    }

    // ==========================
    // save (POST)
    // ==========================

    @Test
    void testSave_Ok() {
        DesarrolladorRequestDTO input = req("Naughty Dog", true);

        when(desarrolladorRepository.existsByNombreIgnoreCase("Naughty Dog"))
                .thenReturn(false);

        when(desarrolladorRepository.save(any(DesarrolladorModel.class)))
                .thenAnswer(invocation -> {
                    DesarrolladorModel d = invocation.getArgument(0);
                    d.setId(10L);
                    return d;
                });

        DesarrolladorResponseDTO saved = desarrolladorService.save(input);

        assertNotNull(saved);
        assertEquals(10L, saved.getId());
        assertEquals("Naughty Dog", saved.getNombre());
        assertTrue(saved.getActivo());
    }

    @Test
    void testSave_ActivoNull_DefaultTrue() {
        DesarrolladorRequestDTO input = req("Studio X", null);

        when(desarrolladorRepository.existsByNombreIgnoreCase("Studio X"))
                .thenReturn(false);

        when(desarrolladorRepository.save(any(DesarrolladorModel.class)))
                .thenAnswer(invocation -> {
                    DesarrolladorModel d = invocation.getArgument(0);
                    d.setId(20L);
                    return d;
                });

        DesarrolladorResponseDTO saved = desarrolladorService.save(input);

        assertNotNull(saved);
        assertTrue(saved.getActivo(), "El campo activo debe quedar TRUE cuando viene null");
    }

    @Test
    void testSave_NombreVacio_LanzaIllegalArgument() {
        DesarrolladorRequestDTO input = req("   ", true);

        assertThrows(IllegalArgumentException.class,
                () -> desarrolladorService.save(input));
    }

    @Test
    void testSave_NombreDuplicado_LanzaIllegalArgument() {
        DesarrolladorRequestDTO input = req("Insomniac Games", true);

        when(desarrolladorRepository.existsByNombreIgnoreCase("Insomniac Games"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> desarrolladorService.save(input));
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    void testUpdate_CambiaNombreYActivo() {
        DesarrolladorModel original = dev(); // activo = true
        DesarrolladorUpdateDTO cambios = upd("Insomniac Studio", false);

        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(original));
        when(desarrolladorRepository.existsByNombreIgnoreCase("Insomniac Studio"))
                .thenReturn(false);
        when(desarrolladorRepository.save(any(DesarrolladorModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DesarrolladorResponseDTO actualizado = desarrolladorService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("Insomniac Studio", actualizado.getNombre());
        assertFalse(actualizado.getActivo());
    }

    @Test
    void testUpdate_NombreVacio_LanzaIllegalArgument() {
        DesarrolladorModel original = dev();
        DesarrolladorUpdateDTO cambios = upd("   ", null);

        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(original));

        assertThrows(IllegalArgumentException.class,
                () -> desarrolladorService.update(1L, cambios));
    }

    @Test
    void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        DesarrolladorModel original = dev();
        DesarrolladorUpdateDTO cambios = upd("Naughty Dog", null);

        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(original));
        when(desarrolladorRepository.existsByNombreIgnoreCase("Naughty Dog"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> desarrolladorService.update(1L, cambios));
    }

    // ==========================
    // patch (PATCH)
    // ==========================

    @Test
    void testPatch_CambiaSoloNombre() {
        DesarrolladorModel existente = dev(); // activo=true
        DesarrolladorUpdateDTO parciales = upd("Insomniac Studio", null);

        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.existsByNombreIgnoreCase("Insomniac Studio"))
                .thenReturn(false);
        when(desarrolladorRepository.save(any(DesarrolladorModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DesarrolladorResponseDTO actualizado = desarrolladorService.patch(1L, parciales);

        assertNotNull(actualizado);
        assertEquals("Insomniac Studio", actualizado.getNombre());
        assertTrue(actualizado.getActivo());
    }

    @Test
    void testPatch_CambiaSoloActivo() {
        DesarrolladorModel existente = dev(); // activo=true
        DesarrolladorUpdateDTO parciales = upd(null, false);

        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.save(any(DesarrolladorModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DesarrolladorResponseDTO actualizado = desarrolladorService.patch(1L, parciales);

        assertNotNull(actualizado);
        assertEquals("Insomniac Games", actualizado.getNombre());
        assertFalse(actualizado.getActivo());
    }

    @Test
    void testPatch_NombreVacio_LanzaIllegalArgument() {
        DesarrolladorModel existente = dev();
        DesarrolladorUpdateDTO parciales = upd("   ", null);

        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> desarrolladorService.patch(1L, parciales));
    }

    @Test
    void testPatch_NombreDuplicado_LanzaIllegalArgument() {
        DesarrolladorModel existente = dev();
        DesarrolladorUpdateDTO parciales = upd("Naughty Dog", null);

        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.existsByNombreIgnoreCase("Naughty Dog"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> desarrolladorService.patch(1L, parciales));
    }

    // ==========================
    // deleteById
    // ==========================

    @Test
    void testDeleteById() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));

        desarrolladorService.deleteById(1L);

        verify(desarrolladorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NoExiste_Lanza404() {
        when(desarrolladorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladorService.deleteById(99L));

        verify(desarrolladorRepository, never()).deleteById(any());
    }

    // ==========================
    // findByNombre
    // ==========================

    @Test
    void testFindByNombre_Ok() {
        when(desarrolladorRepository.findByNombreContainingIgnoreCase("games"))
                .thenReturn(List.of(dev()));

        List<DesarrolladorResponseDTO> lista = desarrolladorService.findByNombre("games");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Insomniac Games", lista.get(0).getNombre());
    }

    @Test
    void testFindByNombre_Null_UsaFiltroVacio() {
        when(desarrolladorRepository.findByNombreContainingIgnoreCase(""))
                .thenReturn(List.of(dev()));

        List<DesarrolladorResponseDTO> lista = desarrolladorService.findByNombre(null);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(desarrolladorRepository, times(1))
                .findByNombreContainingIgnoreCase("");
    }
}