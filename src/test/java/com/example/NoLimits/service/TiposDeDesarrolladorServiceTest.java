package com.example.NoLimits.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.TiposDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.model.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.TiposDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.service.TiposDeDesarrolladorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    private DesarrolladorModel dev() {
        DesarrolladorModel d = new DesarrolladorModel();
        d.setId(1L);
        d.setNombre("FromSoftware");
        return d;
    }

    private TipoDeDesarrolladorModel tipo() {
        TipoDeDesarrolladorModel t = new TipoDeDesarrolladorModel();
        t.setId(10L);
        t.setNombre("Estudio");
        return t;
    }

    private TiposDeDesarrolladorModel link() {
        TiposDeDesarrolladorModel rel = new TiposDeDesarrolladorModel();
        rel.setId(100L);
        rel.setDesarrollador(dev());
        rel.setTipoDeDesarrollador(tipo());
        return rel;
    }

    @Test
    void testFindAll() {
        when(tiposRepo.findAll()).thenReturn(List.of(link()));

        List<TiposDeDesarrolladorModel> lista = service.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
    }

    @Test
    void testLink_Ok() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));
        when(tipoDeDesarrolladorRepository.findById(10L)).thenReturn(Optional.of(tipo()));
        when(tiposRepo.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(false);
        when(tiposRepo.save(any(TiposDeDesarrolladorModel.class)))
                .thenAnswer(invocation -> {
                    TiposDeDesarrolladorModel rel = invocation.getArgument(0);
                    rel.setId(100L);
                    return rel;
                });

        TiposDeDesarrolladorModel result = service.link(1L, 10L);

        assertNotNull(result);
        assertEquals(1L, result.getDesarrollador().getId());
        assertEquals(10L, result.getTipoDeDesarrollador().getId());
        verify(tiposRepo, times(1)).save(any(TiposDeDesarrolladorModel.class));
    }

    @Test
    void testLink_Duplicado_LanzaIllegalState() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));
        when(tipoDeDesarrolladorRepository.findById(10L)).thenReturn(Optional.of(tipo()));
        when(tiposRepo.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> service.link(1L, 10L));

        verify(tiposRepo, never()).save(any(TiposDeDesarrolladorModel.class));
    }

    @Test
    void testLink_DevNoExiste_Lanza404() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.link(1L, 10L));
    }

    @Test
    void testLink_TipoNoExiste_Lanza404() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));
        when(tipoDeDesarrolladorRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.link(1L, 10L));
    }

    @Test
    void testUnlink_OK() {
        TiposDeDesarrolladorModel existente = link();

        when(tiposRepo.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(Optional.of(existente));

        service.unlink(1L, 10L);

        verify(tiposRepo, times(1)).delete(existente);
    }

    @Test
    void testUnlink_NoExiste_Lanza404() {
        when(tiposRepo.findByDesarrollador_IdAndTipoDeDesarrollador_Id(1L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.unlink(1L, 10L));
    }
}