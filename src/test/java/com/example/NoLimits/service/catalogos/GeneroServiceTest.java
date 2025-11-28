package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.repository.catalogos.GeneroRepository;
import com.example.NoLimits.Multimedia.service.catalogos.GeneroService;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class GeneroServiceTest {

    @Autowired
    private GeneroService generoService;

    @MockBean
    private GeneroRepository generoRepository;

    private GeneroModel genero() {
        GeneroModel g = new GeneroModel();
        g.setId(10L);
        g.setNombre("Acción");
        return g;
    }

    @Test
    public void testFindAll() {
        when(generoRepository.findAll()).thenReturn(List.of(genero()));

        List<GeneroModel> lista = generoService.findAll();

        assertEquals(1, lista.size());
        assertEquals("Acción", lista.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        when(generoRepository.findById(10L)).thenReturn(Optional.of(genero()));

        GeneroModel g = generoService.findById(10L);

        assertNotNull(g);
        assertEquals("Acción", g.getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(generoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generoService.findById(99L)
        );
    }

    @Test
    public void testSave() {
        when(generoRepository.save(any(GeneroModel.class))).thenReturn(genero());

        GeneroModel g = generoService.save(genero());

        assertEquals("Acción", g.getNombre());
    }

    @Test
    public void testUpdate() {
        GeneroModel original = genero();
        GeneroModel cambios = new GeneroModel();
        cambios.setNombre("Aventura");

        when(generoRepository.findById(10L)).thenReturn(Optional.of(original));
        when(generoRepository.save(any(GeneroModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        GeneroModel actualizado = generoService.update(10L, cambios);

        assertEquals("Aventura", actualizado.getNombre());
    }

    @Test
    public void testDelete() {
        when(generoRepository.findById(10L)).thenReturn(Optional.of(genero()));

        generoService.deleteById(10L);

        verify(generoRepository, times(1)).deleteById(10L);
    }
}