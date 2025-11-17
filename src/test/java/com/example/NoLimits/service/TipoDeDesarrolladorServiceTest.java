package com.example.NoLimits.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.TiposDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.service.TipoDeDesarrolladorService;
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
public class TipoDeDesarrolladorServiceTest {

    @Autowired
    private TipoDeDesarrolladorService service;

    @MockBean
    private TipoDeDesarrolladorRepository tipoRepo;

    @MockBean
    private TiposDeDesarrolladorRepository tpRepo;

    private TipoDeDesarrolladorModel tipo() {
        TipoDeDesarrolladorModel t = new TipoDeDesarrolladorModel();
        t.setId(1L);
        t.setNombre("Estudio");
        return t;
    }

    @Test
    void testFindAll() {
        when(tipoRepo.findAll()).thenReturn(List.of(tipo()));

        List<TipoDeDesarrolladorModel> lista = service.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
    }

    @Test
    void testFindById_Existe() {
        when(tipoRepo.findById(1L)).thenReturn(Optional.of(tipo()));

        TipoDeDesarrolladorModel t = service.findById(1L);

        assertNotNull(t);
        assertEquals(1L, t.getId());
    }

    @Test
    void testFindById_NoExiste_Lanza404() {
        when(tipoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.findById(99L));
    }

    @Test
    void testSave_Ok() {
        TipoDeDesarrolladorModel input = new TipoDeDesarrolladorModel();
        input.setNombre("Publisher");

        when(tipoRepo.save(any(TipoDeDesarrolladorModel.class)))
                .thenAnswer(invocation -> {
                    TipoDeDesarrolladorModel t = invocation.getArgument(0);
                    t.setId(5L);
                    return t;
                });

        TipoDeDesarrolladorModel saved = service.save(input);

        assertNotNull(saved);
        assertEquals(5L, saved.getId());
        assertEquals("Publisher", saved.getNombre());
    }

    @Test
    void testSave_NombreVacio_LanzaIllegalArgument() {
        TipoDeDesarrolladorModel input = new TipoDeDesarrolladorModel();
        input.setNombre("   ");

        assertThrows(IllegalArgumentException.class,
                () -> service.save(input));
    }

    @Test
    void testUpdate_CambiaNombre() {
        TipoDeDesarrolladorModel original = tipo();
        TipoDeDesarrolladorModel cambios = new TipoDeDesarrolladorModel();
        cambios.setNombre("Co-desarrollador");

        when(tipoRepo.findById(1L)).thenReturn(Optional.of(original));
        when(tipoRepo.save(any(TipoDeDesarrolladorModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TipoDeDesarrolladorModel actualizado = service.update(1L, cambios);

        assertEquals("Co-desarrollador", actualizado.getNombre());
    }

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

        verify(tipoRepo, never()).deleteById(anyLong());
    }
}