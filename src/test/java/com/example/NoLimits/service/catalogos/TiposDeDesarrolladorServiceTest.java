package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposDeDesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TiposDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.service.catalogos.TiposDeDesarrolladorService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

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
public class TiposDeDesarrolladorServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private TiposDeDesarrolladorService service;

    @MockBean
    private TiposDeDesarrolladorRepository tiposDeDesarrolladorRepository;

    @MockBean
    private DesarrolladorRepository desarrolladorRepository;

    @MockBean
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;

    // ================== HELPERS ==================

    private DesarrolladorModel dev() {
        DesarrolladorModel d = new DesarrolladorModel();
        d.setId(1L);
        d.setNombre("Insomniac");
        return d;
    }

    private DesarrolladorModel dev2() {
        DesarrolladorModel d = new DesarrolladorModel();
        d.setId(2L);
        d.setNombre("Naughty Dog");
        return d;
    }

    private TipoDeDesarrolladorModel tipo() {
        TipoDeDesarrolladorModel t = new TipoDeDesarrolladorModel();
        t.setId(10L);
        t.setNombre("Indie");
        return t;
    }

    private TipoDeDesarrolladorModel tipo2() {
        TipoDeDesarrolladorModel t = new TipoDeDesarrolladorModel();
        t.setId(20L);
        t.setNombre("AAA");
        return t;
    }

    private TiposDeDesarrolladorModel relacion() {
        TiposDeDesarrolladorModel rel = new TiposDeDesarrolladorModel();
        rel.setId(100L);
        rel.setDesarrollador(dev());
        rel.setTipoDeDesarrollador(tipo());
        return rel;
    }

    private TiposDeDesarrolladorUpdateDTO upd(Long devId, Long tipoId) {
        TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();
        dto.setDesarrolladorId(devId);
        dto.setTipoDeDesarrolladorId(tipoId);
        return dto;
    }

    // ================== FIND ==================

    @Test
    public void testFindByDesarrollador() {
        when(tiposDeDesarrolladorRepository.findByDesarrollador_Id(1L)).thenReturn(List.of(relacion()));
        List<TiposDeDesarrolladorResponseDTO> lista = service.findByDesarrollador(1L);
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(1L, lista.get(0).getDesarrolladorId());
        assertEquals(10L, lista.get(0).getTipoDeDesarrolladorId());
    }

    @Test
    public void testFindByDesarrollador_Vacio() {
        when(tiposDeDesarrolladorRepository.findByDesarrollador_Id(99L)).thenReturn(List.of());
        List<TiposDeDesarrolladorResponseDTO> lista = service.findByDesarrollador(99L);
        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    @Test
    public void testFindByTipo() {
        when(tiposDeDesarrolladorRepository.findByTipoDeDesarrollador_Id(10L)).thenReturn(List.of(relacion()));
        List<TiposDeDesarrolladorResponseDTO> lista = service.findByTipo(10L);
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getTipoDeDesarrolladorId());
    }

    @Test
    public void testFindByTipo_Vacio() {
        when(tiposDeDesarrolladorRepository.findByTipoDeDesarrollador_Id(99L)).thenReturn(List.of());
        List<TiposDeDesarrolladorResponseDTO> lista = service.findByTipo(99L);
        assertTrue(lista.isEmpty());
    }

    // ================== LINK ==================

    @Test
    public void testLink_Ok() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));
        when(tipoDeDesarrolladorRepository.findById(10L)).thenReturn(Optional.of(tipo()));
        when(tiposDeDesarrolladorRepository.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(Optional.empty());
        when(tiposDeDesarrolladorRepository.save(any())).thenAnswer(inv -> {
            TiposDeDesarrolladorModel rel = inv.getArgument(0);
            rel.setId(100L);
            return rel;
        });

        TiposDeDesarrolladorResponseDTO dto = service.link(1L, 10L);
        assertNotNull(dto);
        assertEquals(1L, dto.getDesarrolladorId());
        assertEquals(10L, dto.getTipoDeDesarrolladorId());
    }

    @Test
    public void testLink_Duplicado_LanzaIllegalState() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));
        when(tipoDeDesarrolladorRepository.findById(10L)).thenReturn(Optional.of(tipo()));
        when(tiposDeDesarrolladorRepository.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(Optional.of(relacion()));

        assertThrows(IllegalStateException.class, () -> service.link(1L, 10L));
        verify(tiposDeDesarrolladorRepository, never()).save(any());
    }

    @Test
    public void testLink_DevNoExiste() {
        when(desarrolladorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.link(99L, 10L));
    }

    @Test
    public void testLink_TipoNoExiste() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));
        when(tipoDeDesarrolladorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.link(1L, 99L));
    }

    // ================== UNLINK ==================

    @Test
    public void testUnlink_OK() {
        when(tiposDeDesarrolladorRepository.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(Optional.of(relacion()));
        service.unlink(1L, 10L);
        verify(tiposDeDesarrolladorRepository, times(1)).delete(any());
    }

    @Test
    public void testUnlink_NoExiste() {
        when(tiposDeDesarrolladorRepository.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.unlink(1L, 10L));
        verify(tiposDeDesarrolladorRepository, never()).delete(any());
    }

    // ================== PATCH ==================

    @Test
    public void testPatch_CambiaDesarrollador() {
        TiposDeDesarrolladorModel rel = relacion();
        when(tiposDeDesarrolladorRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(desarrolladorRepository.findById(2L)).thenReturn(Optional.of(dev2()));
        when(tiposDeDesarrolladorRepository.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(2L, 10L))
                .thenReturn(false);
        when(tiposDeDesarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TiposDeDesarrolladorResponseDTO dto = service.patch(100L, upd(2L, null));
        assertEquals(2L, dto.getDesarrolladorId());
        assertEquals(10L, dto.getTipoDeDesarrolladorId());
    }

    @Test
    public void testPatch_CambiaTipo() {
        TiposDeDesarrolladorModel rel = relacion();
        when(tiposDeDesarrolladorRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(tipoDeDesarrolladorRepository.findById(20L)).thenReturn(Optional.of(tipo2()));
        when(tiposDeDesarrolladorRepository.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 20L))
                .thenReturn(false);
        when(tiposDeDesarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TiposDeDesarrolladorResponseDTO dto = service.patch(100L, upd(null, 20L));
        assertEquals(1L, dto.getDesarrolladorId());
        assertEquals(20L, dto.getTipoDeDesarrolladorId());
    }

    @Test
    public void testPatch_AmbosNulos_NoModificaNada() {
        TiposDeDesarrolladorModel rel = relacion();
        when(tiposDeDesarrolladorRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(tiposDeDesarrolladorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TiposDeDesarrolladorResponseDTO dto = service.patch(100L, upd(null, null));
        assertEquals(1L, dto.getDesarrolladorId());
        assertEquals(10L, dto.getTipoDeDesarrolladorId());
    }

    @Test
    public void testPatch_RelacionNoExiste() {
        when(tiposDeDesarrolladorRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.patch(999L, upd(null, null)));
    }

    @Test
    public void testPatch_CambiarDev_Duplicado() {
        TiposDeDesarrolladorModel rel = relacion();
        when(tiposDeDesarrolladorRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(desarrolladorRepository.findById(2L)).thenReturn(Optional.of(dev2()));
        when(tiposDeDesarrolladorRepository.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(2L, 10L))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.patch(100L, upd(2L, null)));
    }

    @Test
    public void testPatch_CambiarTipo_Duplicado() {
        TiposDeDesarrolladorModel rel = relacion();
        when(tiposDeDesarrolladorRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(tipoDeDesarrolladorRepository.findById(20L)).thenReturn(Optional.of(tipo2()));
        when(tiposDeDesarrolladorRepository.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 20L))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.patch(100L, upd(null, 20L)));
    }

    @Test
    public void testPatch_DevNuevoNoExiste_Lanza404() {
        TiposDeDesarrolladorModel rel = relacion();
        when(tiposDeDesarrolladorRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(desarrolladorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.patch(100L, upd(99L, null)));
    }

    @Test
    public void testPatch_TipoNuevoNoExiste_Lanza404() {
        TiposDeDesarrolladorModel rel = relacion();
        when(tiposDeDesarrolladorRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(tipoDeDesarrolladorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.patch(100L, upd(null, 99L)));
    }
}