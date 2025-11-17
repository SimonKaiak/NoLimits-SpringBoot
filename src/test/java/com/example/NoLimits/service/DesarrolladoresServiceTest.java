package com.example.NoLimits.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.DesarrolladoresModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.DesarrolladoresRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.service.DesarrolladoresService;
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
public class DesarrolladoresServiceTest {

    @Autowired
    private DesarrolladoresService desarrolladoresService;

    @MockBean
    private DesarrolladoresRepository desarrolladoresRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private DesarrolladorRepository desarrolladorRepository;

    private ProductoModel producto() {
        ProductoModel p = new ProductoModel();
        p.setId(1L);
        p.setNombre("Producto Test");
        return p;
    }

    private DesarrolladorModel desarrollador() {
        DesarrolladorModel d = new DesarrolladorModel();
        d.setId(10L);
        d.setNombre("Insomniac Games");
        return d;
    }

    private DesarrolladoresModel relacion() {
        DesarrolladoresModel rel = new DesarrolladoresModel();
        rel.setId(100L);
        rel.setProducto(producto());
        rel.setDesarrollador(desarrollador());
        return rel;
    }

    @Test
    void testFindByProducto() {
        when(desarrolladoresRepository.findByProducto_Id(1L))
                .thenReturn(List.of(relacion()));

        List<DesarrolladoresModel> lista = desarrolladoresService.findByProducto(1L);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(1L, lista.get(0).getProducto().getId());
    }

    @Test
    void testFindByDesarrollador() {
        when(desarrolladoresRepository.findByDesarrollador_Id(10L))
                .thenReturn(List.of(relacion()));

        List<DesarrolladoresModel> lista = desarrolladoresService.findByDesarrollador(10L);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getDesarrollador().getId());
    }

    @Test
    void testLink_CreaNuevaRelacion() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.of(desarrollador()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 10L))
                .thenReturn(false);

        when(desarrolladoresRepository.save(any(DesarrolladoresModel.class)))
                .thenAnswer(invocation -> {
                    DesarrolladoresModel rel = invocation.getArgument(0);
                    rel.setId(100L);
                    return rel;
                });

        DesarrolladoresModel result = desarrolladoresService.link(1L, 10L);

        assertNotNull(result);
        assertEquals(1L, result.getProducto().getId());
        assertEquals(10L, result.getDesarrollador().getId());
        verify(desarrolladoresRepository, times(1)).save(any(DesarrolladoresModel.class));
    }

    @Test
    void testLink_YaExiste_NoDuplica() {
        DesarrolladoresModel existente = relacion();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.of(desarrollador()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 10L))
                .thenReturn(true);
        when(desarrolladoresRepository.findByProducto_Id(1L))
                .thenReturn(List.of(existente));

        DesarrolladoresModel result = desarrolladoresService.link(1L, 10L);

        assertNotNull(result);
        assertEquals(existente.getId(), result.getId());
        verify(desarrolladoresRepository, never()).save(any(DesarrolladoresModel.class));
    }

    @Test
    void testUnlink_ExisteRelacion() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.of(desarrollador()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 10L))
                .thenReturn(true);

        desarrolladoresService.unlink(1L, 10L);

        verify(desarrolladoresRepository, times(1))
                .deleteByProducto_IdAndDesarrollador_Id(1L, 10L);
    }

    @Test
    void testUnlink_NoExisteRelacion_NoRevienta() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.of(desarrollador()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 10L))
                .thenReturn(false);

        assertDoesNotThrow(() -> desarrolladoresService.unlink(1L, 10L));

        verify(desarrolladoresRepository, never())
                .deleteByProducto_IdAndDesarrollador_Id(anyLong(), anyLong());
    }

    @Test
    void testLink_ProductoNoExiste_Lanza404() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladoresService.link(1L, 10L));
    }

    @Test
    void testLink_DesarrolladorNoExiste_Lanza404() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladoresService.link(1L, 10L));
    }
}