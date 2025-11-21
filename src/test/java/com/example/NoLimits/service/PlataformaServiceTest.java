package com.example.NoLimits.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.PlataformaModel;
import com.example.NoLimits.Multimedia.repository.PlataformaRepository;
import com.example.NoLimits.Multimedia.service.PlataformaService;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PlataformaServiceTest {

    @Autowired
    private PlataformaService service;

    @MockBean
    private PlataformaRepository repository;

    private PlataformaModel plataforma() {
        PlataformaModel p = new PlataformaModel();
        p.setId(1L);
        p.setNombre("PlayStation");
        return p;
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(List.of(plataforma()));

        List<PlataformaModel> lista = service.findAll();

        assertEquals(1, lista.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindById_Exists() {
        when(repository.findById(1L)).thenReturn(Optional.of(plataforma()));

        PlataformaModel p = service.findById(1L);

        assertNotNull(p);
        assertEquals("PlayStation", p.getNombre());
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.findById(1L)
        );
    }

    @Test
    void testSave_Ok() {
        PlataformaModel p = plataforma();
        when(repository.save(ArgumentMatchers.any())).thenAnswer(i -> i.getArgument(0));

        PlataformaModel creado = service.save(p);

        assertEquals("PlayStation", creado.getNombre());
    }

    @Test
    void testSave_NombreVacio() {
        PlataformaModel p = new PlataformaModel();
        p.setNombre("");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.save(p)
        );
    }

    @Test
    void testUpdate() {
        PlataformaModel existente = plataforma();
        PlataformaModel entrada = new PlataformaModel();
        entrada.setNombre("Xbox");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        PlataformaModel actualizado = service.update(1L, entrada);

        assertEquals("Xbox", actualizado.getNombre());
    }

    @Test
    void testPatch() {
        PlataformaModel existente = plataforma();
        PlataformaModel entrada = new PlataformaModel();
        entrada.setNombre("Steam");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        PlataformaModel actualizado = service.patch(1L, entrada);

        assertEquals("Steam", actualizado.getNombre());
    }

    @Test
    void testDeleteById() {
        when(repository.findById(1L)).thenReturn(Optional.of(plataforma()));

        service.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.deleteById(1L)
        );
    }
}