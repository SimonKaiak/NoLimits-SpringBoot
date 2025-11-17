package com.example.NoLimits.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.service.DesarrolladorService;
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
public class DesarrolladorServiceTest {

    @Autowired
    private DesarrolladorService desarrolladorService;

    @MockBean
    private DesarrolladorRepository desarrolladorRepository;

    private DesarrolladorModel dev() {
        DesarrolladorModel d = new DesarrolladorModel();
        d.setId(1L);
        d.setNombre("Insomniac Games");
        return d;
    }

    @Test
    void testFindAll() {
        when(desarrolladorRepository.findAll()).thenReturn(List.of(dev()));

        List<DesarrolladorModel> lista = desarrolladorService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Insomniac Games", lista.get(0).getNombre());
    }

    @Test
    void testFindById_Existe() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));

        DesarrolladorModel d = desarrolladorService.findById(1L);

        assertNotNull(d);
        assertEquals(1L, d.getId());
    }

    @Test
    void testFindById_NoExiste_Lanza404() {
        when(desarrolladorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladorService.findById(99L));
    }

    @Test
    void testSave_Ok() {
        DesarrolladorModel input = new DesarrolladorModel();
        input.setNombre("Naughty Dog");

        when(desarrolladorRepository.existsByNombreIgnoreCase("Naughty Dog"))
                .thenReturn(false);
        when(desarrolladorRepository.save(any(DesarrolladorModel.class)))
                .thenAnswer(invocation -> {
                    DesarrolladorModel d = invocation.getArgument(0);
                    d.setId(10L);
                    return d;
                });

        DesarrolladorModel saved = desarrolladorService.save(input);

        assertNotNull(saved);
        assertEquals(10L, saved.getId());
        assertEquals("Naughty Dog", saved.getNombre());
        verify(desarrolladorRepository, times(1)).save(any(DesarrolladorModel.class));
    }

    @Test
    void testSave_NombreVacio_LanzaIllegalArgument() {
        DesarrolladorModel input = new DesarrolladorModel();
        input.setNombre("  ");

        assertThrows(IllegalArgumentException.class,
                () -> desarrolladorService.save(input));
    }

    @Test
    void testSave_NombreDuplicado_LanzaIllegalArgument() {
        DesarrolladorModel input = new DesarrolladorModel();
        input.setNombre("Insomniac Games");

        when(desarrolladorRepository.existsByNombreIgnoreCase("Insomniac Games"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> desarrolladorService.save(input));
    }

    @Test
    void testUpdate_CambiaNombre() {
        DesarrolladorModel original = dev();
        DesarrolladorModel cambios = new DesarrolladorModel();
        cambios.setNombre("Insomniac Studio");

        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(original));
        when(desarrolladorRepository.existsByNombreIgnoreCase("Insomniac Studio"))
                .thenReturn(false);
        when(desarrolladorRepository.save(any(DesarrolladorModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DesarrolladorModel actualizado = desarrolladorService.update(1L, cambios);

        assertEquals("Insomniac Studio", actualizado.getNombre());
    }

    @Test
    void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        DesarrolladorModel original = dev();
        DesarrolladorModel cambios = new DesarrolladorModel();
        cambios.setNombre("Naughty Dog");

        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(original));
        when(desarrolladorRepository.existsByNombreIgnoreCase("Naughty Dog"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> desarrolladorService.update(1L, cambios));
    }

    @Test
    void testDeleteById() {
        when(desarrolladorRepository.findById(1L)).thenReturn(Optional.of(dev()));

        desarrolladorService.deleteById(1L);

        verify(desarrolladorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByNombre_DelegadoAlRepo() {
        when(desarrolladorRepository.findByNombreContainingIgnoreCase("games"))
                .thenReturn(List.of(dev()));

        List<DesarrolladorModel> lista = desarrolladorService.findByNombre("games");

        assertEquals(1, lista.size());
        verify(desarrolladorRepository, times(1))
                .findByNombreContainingIgnoreCase("games");
    }
}