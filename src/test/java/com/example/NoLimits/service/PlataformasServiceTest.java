package com.example.NoLimits.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.PlataformaModel;
import com.example.NoLimits.Multimedia.model.PlataformasModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.PlataformaRepository;
import com.example.NoLimits.Multimedia.repository.PlataformasRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.service.PlataformasService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
public class PlataformasServiceTest {

    @Autowired
    private PlataformasService service;

    @MockBean
    private PlataformasRepository plataformasRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private PlataformaRepository plataformaRepository;

    private ProductoModel producto() {
        ProductoModel p = new ProductoModel();
        p.setId(10L);
        return p;
    }

    private PlataformaModel plataforma() {
        PlataformaModel p = new PlataformaModel();
        p.setId(20L);
        return p;
    }

    private PlataformasModel relacion() {
        PlataformasModel r = new PlataformasModel();
        r.setId(100L);
        r.setProducto(producto());
        r.setPlataforma(plataforma());
        return r;
    }

    @Test
    void testFindByProducto() {
        when(plataformasRepository.findByProducto_Id(10L)).thenReturn(List.of(relacion()));

        List<PlataformasModel> lista = service.findByProducto(10L);

        assertEquals(1, lista.size());
    }

    @Test
    void testLink_Ok() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto()));
        when(plataformaRepository.findById(20L)).thenReturn(Optional.of(plataforma()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(10L, 20L)).thenReturn(false);
        when(plataformasRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PlataformasModel rel = service.link(10L, 20L);

        assertEquals(10L, rel.getProducto().getId());
        assertEquals(20L, rel.getPlataforma().getId());
    }

    @Test
    void testLink_NotFoundProducto() {
        when(productoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.link(10L, 20L)
        );
    }

    @Test
    void testLink_NotFoundPlataforma() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto()));
        when(plataformaRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.link(10L, 20L)
        );
    }

    @Test
    void testUnlink_Ok() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto()));
        when(plataformaRepository.findById(20L)).thenReturn(Optional.of(plataforma()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(10L, 20L)).thenReturn(true);

        service.unlink(10L, 20L);

        verify(plataformasRepository, times(1))
                .deleteByProducto_IdAndPlataforma_Id(10L, 20L);
    }

    @Test
    void testUnlink_NotFoundProducto() {
        when(productoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.unlink(10L, 20L)
        );
    }

    @Test
    void testPatch_CambiarProducto() {
        PlataformasModel r = relacion();

        // Mock de la relación existente
        when(plataformasRepository.findById(100L)).thenReturn(Optional.of(r));

        // Mock de un producto con ID = 999
        ProductoModel p999 = new ProductoModel();
        p999.setId(999L);
        when(productoRepository.findById(999L)).thenReturn(Optional.of(p999));

        // No existe relación duplicada
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(anyLong(), anyLong()))
                .thenReturn(false);

        // Guardar devuelve el mismo objeto
        when(plataformasRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Ejecutar
        PlataformasModel actualizado = service.patch(100L, 999L, null);

        // Afirmación correcta
        assertEquals(999L, actualizado.getProducto().getId());
    }

    @Test
    void testPatch_CambiarPlataforma() {
        PlataformasModel r = relacion();

        when(plataformasRepository.findById(100L)).thenReturn(Optional.of(r));
        PlataformaModel nueva = new PlataformaModel();
        nueva.setId(777L);

        when(plataformaRepository.findById(777L)).thenReturn(Optional.of(nueva));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(anyLong(), anyLong())).thenReturn(false);
        when(plataformasRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PlataformasModel actualizado = service.patch(100L, null, 777L);

        assertEquals(777L, actualizado.getPlataforma().getId());
    }

    @Test
    void testPatch_NoExisteRelacion() {
        when(plataformasRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.patch(100L, null, null)
        );
    }
}