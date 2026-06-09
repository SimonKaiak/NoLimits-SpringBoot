package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladorRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.DesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladorService;
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
public class DesarrolladorServiceTest extends AbstractContainerBaseTest {

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
    }

    @Test
    void testFindById_Existe() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));
        DesarrolladorResponseDTO dto = desarrolladorService.findById(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Insomniac Games", dto.getNombre());
    }

    @Test
    void testFindById_NoExiste_Lanza404() {
        when(desarrolladorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> desarrolladorService.findById(99L));
    }

    // ==========================
    // save (POST)
    // ==========================

    @Test
    void testSave_Ok() {
        when(desarrolladorRepository.existsByNombreIgnoreCase("Rockstar")).thenReturn(false);
        when(desarrolladorRepository.save(any())).thenAnswer(inv -> {
            DesarrolladorModel d = inv.getArgument(0);
            d.setId(2L);
            return d;
        });
        DesarrolladorResponseDTO dto = desarrolladorService.save(req("Rockstar", true));
        assertNotNull(dto);
        assertEquals("Rockstar", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    void testSave_ActivoNull_DefaultTrue() {
        when(desarrolladorRepository.existsByNombreIgnoreCase("Rockstar")).thenReturn(false);
        when(desarrolladorRepository.save(any())).thenAnswer(inv -> {
            DesarrolladorModel d = inv.getArgument(0);
            d.setId(2L);
            return d;
        });
        DesarrolladorResponseDTO dto = desarrolladorService.save(req("Rockstar", null));
        assertNotNull(dto);
        assertTrue(dto.getActivo());
    }

    @Test
    void testSave_ActivoFalse_RespetaValor() {
        when(desarrolladorRepository.existsByNombreIgnoreCase("Rockstar")).thenReturn(false);
        when(desarrolladorRepository.save(any())).thenAnswer(inv -> {
            DesarrolladorModel d = inv.getArgument(0);
            d.setId(2L);
            return d;
        });
        DesarrolladorResponseDTO dto = desarrolladorService.save(req("Rockstar", false));
        assertNotNull(dto);
        assertFalse(dto.getActivo());
    }

    @Test
    void testSave_NombreVacio_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> desarrolladorService.save(req("   ", true)));
        verify(desarrolladorRepository, never()).save(any());
    }

    @Test
    void testSave_NombreDuplicado_LanzaIllegalArgument() {
        when(desarrolladorRepository.existsByNombreIgnoreCase("Insomniac Games")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> desarrolladorService.save(req("Insomniac Games", true)));
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    void testUpdate_CambiaNombreYActivo() {
        DesarrolladorModel existente = dev();
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.existsByNombreIgnoreCase("Naughty Dog")).thenReturn(false);
        when(desarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DesarrolladorResponseDTO dto = desarrolladorService.update(1L, upd("Naughty Dog", false));
        assertEquals("Naughty Dog", dto.getNombre());
        assertFalse(dto.getActivo());
    }

    @Test
    void testUpdate_MismoNombre_NoVerificaDuplicado() {
        // Si el nombre no cambia, no debe verificar duplicado
        DesarrolladorModel existente = dev(); // nombre = "Insomniac Games"
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DesarrolladorResponseDTO dto = desarrolladorService.update(1L, upd("Insomniac Games", false));
        assertEquals("Insomniac Games", dto.getNombre());
        // No debería haber llamado a existsByNombreIgnoreCase
        verify(desarrolladorRepository, never()).existsByNombreIgnoreCase(any());
    }

    @Test
    void testUpdate_ActivoIgual_NoActualiza() {
        // activo es true y se envía true → la condición `in.getActivo() != d.isActivo()` es false
        DesarrolladorModel existente = dev(); // activo = true
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DesarrolladorResponseDTO dto = desarrolladorService.update(1L, upd(null, true));
        assertTrue(dto.getActivo());
    }

    @Test
    void testUpdate_NombreVacio_LanzaIllegalArgument() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));
        assertThrows(IllegalArgumentException.class, () -> desarrolladorService.update(1L, upd("   ", null)));
    }

    @Test
    void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        DesarrolladorModel existente = dev();
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.existsByNombreIgnoreCase("Ubisoft")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> desarrolladorService.update(1L, upd("Ubisoft", null)));
    }

    @Test
    void testUpdate_NoExiste_Lanza404() {
        when(desarrolladorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> desarrolladorService.update(99L, upd("X", null)));
    }

    // ==========================
    // patch
    // ==========================

    @Test
    void testPatch_CambiaSoloNombre() {
        DesarrolladorModel existente = dev();
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.existsByNombreIgnoreCase("EA Games")).thenReturn(false);
        when(desarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DesarrolladorResponseDTO dto = desarrolladorService.patch(1L, upd("EA Games", null));
        assertEquals("EA Games", dto.getNombre());
        assertTrue(dto.getActivo()); // activo no cambió
    }

    @Test
    void testPatch_CambiaSoloActivo() {
        DesarrolladorModel existente = dev(); // activo = true
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DesarrolladorResponseDTO dto = desarrolladorService.patch(1L, upd(null, false));
        assertFalse(dto.getActivo());
        assertEquals("Insomniac Games", dto.getNombre()); // nombre no cambió
    }

    @Test
    void testPatch_NombreVacio_LanzaIllegalArgument() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));
        assertThrows(IllegalArgumentException.class, () -> desarrolladorService.patch(1L, upd("   ", null)));
    }

    @Test
    void testPatch_NombreDuplicado_LanzaIllegalArgument() {
        DesarrolladorModel existente = dev();
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.existsByNombreIgnoreCase("Ubisoft")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> desarrolladorService.patch(1L, upd("Ubisoft", null)));
    }

    @Test
    void testPatch_NoExiste_Lanza404() {
        when(desarrolladorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> desarrolladorService.patch(99L, upd("X", null)));
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
        assertThrows(RecursoNoEncontradoException.class, () -> desarrolladorService.deleteById(99L));
    }

    // ==========================
    // findByNombre
    // ==========================

    @Test
    void testFindByNombre_Ok() {
        when(desarrolladorRepository.findByNombreContainingIgnoreCase("insomniac"))
                .thenReturn(List.of(dev()));
        List<DesarrolladorResponseDTO> lista = desarrolladorService.findByNombre("insomniac");
        assertEquals(1, lista.size());
    }

    @Test
    void testFindByNombre_Null_UsaFiltroVacio() {
        when(desarrolladorRepository.findByNombreContainingIgnoreCase("")).thenReturn(List.of());
        List<DesarrolladorResponseDTO> lista = desarrolladorService.findByNombre(null);
        assertNotNull(lista);
    }

    // ==========================
    // listarPaginado
    // ==========================

    @Test
    void testListarPaginado_SinFiltro() {
        Page<DesarrolladorModel> page = new PageImpl<>(List.of(dev()), PageRequest.of(0, 10), 1);
        when(desarrolladorRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = desarrolladorService.listarPaginado(1, 10, null);
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }

    @Test
    void testListarPaginado_ConFiltro() {
        Page<DesarrolladorModel> page = new PageImpl<>(List.of(dev()), PageRequest.of(0, 10), 1);
        when(desarrolladorRepository.findByNombreContainingIgnoreCase(any(), any(Pageable.class)))
                .thenReturn(page);

        var resultado = desarrolladorService.listarPaginado(1, 10, "insomniac");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }

    @Test
    void testListarPaginado_FiltroBlanco_UsaTodos() {
        Page<DesarrolladorModel> page = new PageImpl<>(List.of(dev()), PageRequest.of(0, 10), 1);
        when(desarrolladorRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = desarrolladorService.listarPaginado(1, 10, "   ");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }
}