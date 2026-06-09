package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoDeDesarrolladorRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoDeDesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.service.catalogos.TipoDeDesarrolladorService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TipoDeDesarrolladorServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private TipoDeDesarrolladorService service;

    @MockBean
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;

    @MockBean
    private TiposDeDesarrolladorRepository tiposDeDesarrolladorRepository;

    // ==========================
    // HELPERS
    // ==========================

    private TipoDeDesarrolladorModel entity() {
        TipoDeDesarrolladorModel m = new TipoDeDesarrolladorModel();
        m.setId(1L);
        m.setNombre("Indie");
        return m;
    }

    private TipoDeDesarrolladorRequestDTO req(String nombre) {
        TipoDeDesarrolladorRequestDTO dto = new TipoDeDesarrolladorRequestDTO();
        dto.setNombre(nombre);
        return dto;
    }

    private TipoDeDesarrolladorUpdateDTO upd(String nombre) {
        TipoDeDesarrolladorUpdateDTO dto = new TipoDeDesarrolladorUpdateDTO();
        dto.setNombre(nombre);
        return dto;
    }

    // ==========================
    // findAll / findById
    // ==========================

    @Test
    void testFindAll() {
        when(tipoDeDesarrolladorRepository.findAll()).thenReturn(List.of(entity()));
        List<TipoDeDesarrolladorResponseDTO> lista = service.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Indie", lista.get(0).getNombre());
    }

    @Test
    void testFindById_Existe() {
        when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(entity()));
        TipoDeDesarrolladorResponseDTO dto = service.findById(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
    }

    @Test
    void testFindById_NoExiste_Lanza404() {
        when(tipoDeDesarrolladorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.findById(99L));
    }

    // ==========================
    // save
    // ==========================

    @Test
    void testSave_Ok() {
        when(tipoDeDesarrolladorRepository.existsByNombreIgnoreCase("AAA")).thenReturn(false);
        when(tipoDeDesarrolladorRepository.save(any())).thenAnswer(inv -> {
            TipoDeDesarrolladorModel m = inv.getArgument(0);
            m.setId(2L);
            return m;
        });
        TipoDeDesarrolladorResponseDTO dto = service.save(req("AAA"));
        assertNotNull(dto);
        assertEquals("AAA", dto.getNombre());
    }

    @Test
    void testSave_NombreNull_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> service.save(req(null)));
        verify(tipoDeDesarrolladorRepository, never()).save(any());
    }

    @Test
    void testSave_NombreVacio_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> service.save(req("  ")));
        verify(tipoDeDesarrolladorRepository, never()).save(any());
    }

    @Test
    void testSave_NombreDuplicado_LanzaIllegalArgument() {
        when(tipoDeDesarrolladorRepository.existsByNombreIgnoreCase("Indie")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.save(req("Indie")));
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    void testUpdate_CambiaNombre() {
        TipoDeDesarrolladorModel existente = entity();
        when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoDeDesarrolladorRepository.existsByNombreIgnoreCase("AAA")).thenReturn(false);
        when(tipoDeDesarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoDeDesarrolladorResponseDTO dto = service.update(1L, upd("AAA"));
        assertEquals("AAA", dto.getNombre());
    }

    @Test
    void testUpdate_MismoNombre_NoVerificaDuplicado() {
        TipoDeDesarrolladorModel existente = entity(); // nombre = "Indie"
        when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoDeDesarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoDeDesarrolladorResponseDTO dto = service.update(1L, upd("Indie"));
        assertEquals("Indie", dto.getNombre());
        // mismo nombre → no debe verificar duplicado
        verify(tipoDeDesarrolladorRepository, never()).existsByNombreIgnoreCase(anyString());
    }

    @Test
    void testUpdate_NombreVacio_LanzaIllegalArgument() {
        when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class, () -> service.update(1L, upd("   ")));
    }

    @Test
    void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        TipoDeDesarrolladorModel existente = entity();
        when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoDeDesarrolladorRepository.existsByNombreIgnoreCase("AAA")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.update(1L, upd("AAA")));
    }

    @Test
    void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {
        when(tipoDeDesarrolladorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.update(99L, upd("X")));
    }

    @Test
    void testUpdate_NombreNull_NoCambia() {
        // Si nombre es null, la condición `in.getNombre() != null` es false → no modifica nombre
        TipoDeDesarrolladorModel existente = entity();
        when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoDeDesarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoDeDesarrolladorResponseDTO dto = service.update(1L, upd(null));
        assertEquals("Indie", dto.getNombre()); // no cambió
    }

    // ==========================
    // patch (delega a update)
    // ==========================

    @Test
    void testPatch_CambiaNombre() {
        TipoDeDesarrolladorModel existente = entity();
        when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoDeDesarrolladorRepository.existsByNombreIgnoreCase("AAA")).thenReturn(false);
        when(tipoDeDesarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoDeDesarrolladorResponseDTO dto = service.patch(1L, upd("AAA"));
        assertEquals("AAA", dto.getNombre());
    }

    // ==========================
    // deleteById
    // ==========================

    @Test
    void testDeleteById_SinRelaciones() {
        when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(tiposDeDesarrolladorRepository.existsByTipoDeDesarrollador_Id(1L)).thenReturn(false);

        service.deleteById(1L);
        verify(tipoDeDesarrolladorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_ConRelaciones_LanzaIllegalState() {
        when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(tiposDeDesarrolladorRepository.existsByTipoDeDesarrollador_Id(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.deleteById(1L));
        verify(tipoDeDesarrolladorRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(tipoDeDesarrolladorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.deleteById(99L));
    }

    // ==========================
    // findAllPaged / findByNombrePaged
    // ==========================

    @Test
    void testFindAllPaged() {
        Page<TipoDeDesarrolladorModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(tipoDeDesarrolladorRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = service.findAllPaged(1, 10);
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        assertEquals("Indie", resultado.getContenido().get(0).getNombre());
    }

    @Test
    void testFindByNombrePaged() {
        Page<TipoDeDesarrolladorModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(tipoDeDesarrolladorRepository.findByNombreContainingIgnoreCase(any(), any(Pageable.class)))
                .thenReturn(page);

        var resultado = service.findByNombrePaged("indie", 1, 10);
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }
}