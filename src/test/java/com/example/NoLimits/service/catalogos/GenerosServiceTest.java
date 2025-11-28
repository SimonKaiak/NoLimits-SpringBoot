package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.model.catalogos.GenerosModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.GeneroRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.GenerosRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.catalogos.GenerosService;

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