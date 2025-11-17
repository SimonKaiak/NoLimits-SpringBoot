package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.GeneroModel;
import com.example.NoLimits.Multimedia.model.GenerosModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.GeneroRepository;
import com.example.NoLimits.Multimedia.repository.GenerosRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.service.GenerosService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class GenerosServiceTest {

    @Autowired
    private GenerosService generosService;

    @MockBean
    private GenerosRepository generosRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private GeneroRepository generoRepository;

    private ProductoModel producto() {
        ProductoModel p = new ProductoModel();
        p.setId(1L);
        p.setNombre("Producto Test");
        return p;
    }

    private GeneroModel genero() {
        GeneroModel g = new GeneroModel();
        g.setId(10L);
        g.setNombre("Acción");
        return g;
    }

    private GenerosModel relacion() {
        GenerosModel rel = new GenerosModel();
        rel.setId(100L);
        rel.setProducto(producto());
        rel.setGenero(genero());
        return rel;
    }

    @Test
    public void testFindByProducto() {
        when(generosRepository.findByProducto_Id(1L)).thenReturn(List.of(relacion()));

        List<GenerosModel> lista = generosService.findByProducto(1L);

        assertEquals(1, lista.size());
        assertEquals(1L, lista.get(0).getProducto().getId());
    }

    @Test
    public void testLink_CreaNuevaRelacion() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(generoRepository.findById(10L)).thenReturn(Optional.of(genero()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(1L, 10L)).thenReturn(false);
        when(generosRepository.save(any(GenerosModel.class))).thenAnswer(inv -> inv.getArgument(0));

        GenerosModel rel = generosService.link(1L, 10L);

        assertNotNull(rel);
        assertEquals(1L, rel.getProducto().getId());
        assertEquals(10L, rel.getGenero().getId());
    }

    @Test
    public void testLink_ProductoNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
            () -> generosService.link(1L, 10L)
        );
    }

    @Test
    public void testUnlink_ExisteRelacion() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(generoRepository.findById(10L)).thenReturn(Optional.of(genero()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(1L, 10L)).thenReturn(true);

        generosService.unlink(1L, 10L);

        verify(generosRepository, times(1))
                .deleteByProducto_IdAndGenero_Id(1L, 10L);
    }
}