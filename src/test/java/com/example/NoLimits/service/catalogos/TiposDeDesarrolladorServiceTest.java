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
public class TiposDeDesarrolladorServiceTest {

    @Autowired
    private TiposDeDesarrolladorService service;

    @MockBean
    private TiposDeDesarrolladorRepository tiposRepo;

    @MockBean
    private DesarrolladorRepository desarrolladorRepository;

    @MockBean
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;

    // ================= HELPERS ==================

    private DesarrolladorModel dev(Long id) {
        DesarrolladorModel d = new DesarrolladorModel();
        d.setId(id);
        d.setNombre("Dev-" + id);
        return d;
    }

    private TipoDeDesarrolladorModel tipo(Long id) {
        TipoDeDesarrolladorModel t = new TipoDeDesarrolladorModel();
        t.setId(id);
        t.setNombre("Tipo-" + id);
        return t;
    }

    private TiposDeDesarrolladorModel link(Long idRel, Long devId, Long tipoId) {
        TiposDeDesarrolladorModel rel = new TiposDeDesarrolladorModel();
        rel.setId(idRel);
        rel.setDesarrollador(dev(devId));
        rel.setTipoDeDesarrollador(tipo(tipoId));
        return rel;
    }

    // ================== FIND BY DESARROLLADOR ==================

    @Test
    void testFindByDesarrollador() {
        when(tiposRepo.findByDesarrollador_Id(1L))
                .thenReturn(List.of(link(100L, 1L, 10L)));

        List<TiposDeDesarrolladorResponseDTO> lista =
                service.findByDesarrollador(1L);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(1L, lista.get(0).getDesarrolladorId());
        assertEquals(10L, lista.get(0).getTipoDeDesarrolladorId());
    }

    // ================== FIND BY TIPO ==================

    @Test
    void testFindByTipo() {
        when(tiposRepo.findByTipoDeDesarrollador_Id(10L))
                .thenReturn(List.of(link(200L, 2L, 10L)));

        List<TiposDeDesarrolladorResponseDTO> lista =
                service.findByTipo(10L);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(2L, lista.get(0).getDesarrolladorId());
        assertEquals(10L, lista.get(0).getTipoDeDesarrolladorId());
    }

    // ================== LINK ==================

    @Test
    void testLink_Ok() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev(1L)));
        when(tipoDeDesarrolladorRepository.findById(10L)).thenReturn(Optional.of(tipo(10L)));
        when(tiposRepo.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L)).thenReturn(Optional.empty());

        when(tiposRepo.save(any(TiposDeDesarrolladorModel.class)))
                .thenAnswer(invocation -> {
                    TiposDeDesarrolladorModel r = invocation.getArgument(0);
                    r.setId(100L);
                    return r;
                });

        TiposDeDesarrolladorResponseDTO dto = service.link(1L, 10L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getDesarrolladorId());
        assertEquals(10L, dto.getTipoDeDesarrolladorId());
    }

    @Test
    void testLink_Duplicado_LanzaIllegalState() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev(1L)));
        when(tipoDeDesarrolladorRepository.findById(10L)).thenReturn(Optional.of(tipo(10L)));

        when(tiposRepo.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(Optional.of(link(100L, 1L, 10L)));

        assertThrows(IllegalStateException.class,
                () -> service.link(1L, 10L));

        verify(tiposRepo, never()).save(any(TiposDeDesarrolladorModel.class));
    }

    @Test
    void testLink_DevNoExiste() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.link(1L, 10L));
    }

    @Test
    void testLink_TipoNoExiste() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev(1L)));
        when(tipoDeDesarrolladorRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.link(1L, 10L));
    }

    // ================== UNLINK ==================

    @Test
    void testUnlink_OK() {
        TiposDeDesarrolladorModel existente = link(100L, 1L, 10L);

        when(tiposRepo.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(Optional.of(existente));

        service.unlink(1L, 10L);

        verify(tiposRepo, times(1)).delete(existente);
    }

    @Test
    void testUnlink_NoExiste() {
        when(tiposRepo.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.unlink(1L, 10L));
    }

    // ================== PATCH ==================

    @Test
    void testPatch_CambiaDesarrollador() {
        TiposDeDesarrolladorModel existente = link(100L, 1L, 10L);

        when(tiposRepo.findById(100L)).thenReturn(Optional.of(existente));

        when(desarrolladorRepository.findById(2L))
                .thenReturn(Optional.of(dev(2L)));

        when(tiposRepo.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(2L, 10L))
                .thenReturn(false);

        when(tiposRepo.save(any(TiposDeDesarrolladorModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();
        dto.setDesarrolladorId(2L);

        TiposDeDesarrolladorResponseDTO out = service.patch(100L, dto);

        assertEquals(2L, out.getDesarrolladorId());
        assertEquals(10L, out.getTipoDeDesarrolladorId());
    }

    @Test
    void testPatch_CambiaTipo() {
        TiposDeDesarrolladorModel existente = link(100L, 1L, 10L);

        when(tiposRepo.findById(100L)).thenReturn(Optional.of(existente));
        when(tipoDeDesarrolladorRepository.findById(20L))
                .thenReturn(Optional.of(tipo(20L)));

        when(tiposRepo.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 20L))
                .thenReturn(false);

        when(tiposRepo.save(any(TiposDeDesarrolladorModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();
        dto.setTipoDeDesarrolladorId(20L);

        TiposDeDesarrolladorResponseDTO out = service.patch(100L, dto);

        assertEquals(1L, out.getDesarrolladorId());
        assertEquals(20L, out.getTipoDeDesarrolladorId());
    }

    @Test
    void testPatch_RelacionNoExiste() {
        when(tiposRepo.findById(100L)).thenReturn(Optional.empty());

        TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();
        dto.setDesarrolladorId(2L);

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.patch(100L, dto));
    }

    @Test
    void testPatch_CambiarDev_Duplicado() {
        TiposDeDesarrolladorModel existente = link(100L, 1L, 10L);

        when(tiposRepo.findById(100L)).thenReturn(Optional.of(existente));

        when(desarrolladorRepository.findById(2L))
                .thenReturn(Optional.of(dev(2L)));

        when(tiposRepo.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(2L, 10L))
                .thenReturn(true);

        TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();
        dto.setDesarrolladorId(2L);

        assertThrows(IllegalArgumentException.class,
                () -> service.patch(100L, dto));
    }

    @Test
    void testPatch_CambiarTipo_Duplicado() {
        TiposDeDesarrolladorModel existente = link(100L, 1L, 10L);

        when(tiposRepo.findById(100L)).thenReturn(Optional.of(existente));

        when(tipoDeDesarrolladorRepository.findById(20L))
                .thenReturn(Optional.of(tipo(20L)));

        when(tiposRepo.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 20L))
                .thenReturn(true);

        TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();
        dto.setTipoDeDesarrolladorId(20L);

        assertThrows(IllegalArgumentException.class,
                () -> service.patch(100L, dto));
    }
}