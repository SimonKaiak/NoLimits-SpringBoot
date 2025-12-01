package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GenerosResponseDTO;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.model.catalogos.GenerosModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.GeneroRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.GenerosRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.catalogos.GenerosService;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class GenerosServiceTest {

    @Autowired
    private GenerosService generosService;

    @MockBean
    private GenerosRepository generosRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private GeneroRepository generoRepository;

    // ================== HELPERS ==================

    private ProductoModel producto() {
        ProductoModel p = new ProductoModel();
        p.setId(1L);
        p.setNombre("Producto Test");
        return p;
    }

    private ProductoModel producto2() {
        ProductoModel p = new ProductoModel();
        p.setId(2L);
        p.setNombre("Producto Alternativo");
        return p;
    }

    private GeneroModel genero() {
        GeneroModel g = new GeneroModel();
        g.setId(10L);
        g.setNombre("Acción");
        return g;
    }

    private GeneroModel genero2() {
        GeneroModel g = new GeneroModel();
        g.setId(20L);
        g.setNombre("Aventura");
        return g;
    }

    private GenerosModel relacion() {
        GenerosModel rel = new GenerosModel();
        rel.setId(100L);
        rel.setProducto(producto());
        rel.setGenero(genero());
        return rel;
    }

    // ================== FIND ==================

    @Test
    void testFindByProducto() {
        when(generosRepository.findByProducto_Id(1L)).thenReturn(List.of(relacion()));

        List<GenerosResponseDTO> lista = generosService.findByProducto(1L);

        assertNotNull(lista);
        assertEquals(1, lista.size());

        GenerosResponseDTO dto = lista.get(0);
        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getProductoId());
        assertEquals(10L, dto.getGeneroId());
        assertEquals("Acción", dto.getGeneroNombre());
    }

    @Test
    void testFindByGenero() {
        when(generosRepository.findByGenero_Id(10L)).thenReturn(List.of(relacion()));

        List<GenerosResponseDTO> lista = generosService.findByGenero(10L);

        assertNotNull(lista);
        assertEquals(1, lista.size());

        GenerosResponseDTO dto = lista.get(0);
        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getProductoId());
        assertEquals(10L, dto.getGeneroId());
        assertEquals("Acción", dto.getGeneroNombre());
    }

    // ================== LINK ==================

    @Test
    void testLink_CreaNuevaRelacion() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(generoRepository.findById(10L)).thenReturn(Optional.of(genero()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(1L, 10L)).thenReturn(false);
        when(generosRepository.save(any(GenerosModel.class)))
                .thenAnswer(invocation -> {
                    GenerosModel rel = invocation.getArgument(0);
                    rel.setId(100L);
                    return rel;
                });

        GenerosResponseDTO dto = generosService.link(1L, 10L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getProductoId());
        assertEquals(10L, dto.getGeneroId());
        assertEquals("Acción", dto.getGeneroNombre());
    }

    @Test
    void testLink_YaExisteDevuelveExistente() {
        GenerosModel existente = relacion();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(generoRepository.findById(10L)).thenReturn(Optional.of(genero()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(1L, 10L)).thenReturn(true);
        when(generosRepository.findByProducto_Id(1L)).thenReturn(List.of(existente));

        GenerosResponseDTO dto = generosService.link(1L, 10L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getProductoId());
        assertEquals(10L, dto.getGeneroId());
        assertEquals("Acción", dto.getGeneroNombre());
        verify(generosRepository, never()).save(any(GenerosModel.class));
    }

    @Test
    void testLink_ProductoNoExiste_Lanza404() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generosService.link(1L, 10L));

        verify(generoRepository, never()).findById(anyLong());
        verify(generosRepository, never()).save(any(GenerosModel.class));
    }

    @Test
    void testLink_GeneroNoExiste_Lanza404() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(generoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generosService.link(1L, 10L));

        verify(generosRepository, never()).save(any(GenerosModel.class));
    }

    // ================== UNLINK ==================

    @Test
    void testUnlink_ExisteRelacion_Elimina() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(generoRepository.findById(10L)).thenReturn(Optional.of(genero()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(1L, 10L)).thenReturn(true);

        generosService.unlink(1L, 10L);

        verify(generosRepository, times(1))
                .deleteByProducto_IdAndGenero_Id(1L, 10L);
    }

    @Test
    void testUnlink_NoExisteRelacion_NoElimina() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(generoRepository.findById(10L)).thenReturn(Optional.of(genero()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(1L, 10L)).thenReturn(false);

        generosService.unlink(1L, 10L);

        verify(generosRepository, never())
                .deleteByProducto_IdAndGenero_Id(anyLong(), anyLong());
    }

    @Test
    void testUnlink_ProductoNoExiste_Lanza404() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generosService.unlink(1L, 10L));

        verify(generosRepository, never())
                .deleteByProducto_IdAndGenero_Id(anyLong(), anyLong());
    }

    @Test
    void testUnlink_GeneroNoExiste_Lanza404() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(generoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generosService.unlink(1L, 10L));

        verify(generosRepository, never())
                .deleteByProducto_IdAndGenero_Id(anyLong(), anyLong());
    }

    // ================== PATCH ==================

    @Test
    void testPatch_CambiaSoloProducto_OK() {
        GenerosModel rel = relacion();

        when(generosRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto2()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(2L, 10L)).thenReturn(false);
        when(generosRepository.save(any(GenerosModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        GenerosResponseDTO dto = generosService.patch(100L, 2L, null);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(2L, dto.getProductoId());
        assertEquals(10L, dto.getGeneroId());
        assertEquals("Acción", dto.getGeneroNombre());
    }

    @Test
    void testPatch_CambiaSoloGenero_OK() {
        GenerosModel rel = relacion();

        when(generosRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(generoRepository.findById(20L)).thenReturn(Optional.of(genero2()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(1L, 20L)).thenReturn(false);
        when(generosRepository.save(any(GenerosModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        GenerosResponseDTO dto = generosService.patch(100L, null, 20L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getProductoId());
        assertEquals(20L, dto.getGeneroId());
        assertEquals("Aventura", dto.getGeneroNombre());
    }

    @Test
    void testPatch_CambiaProductoYGenero_OK() {
        GenerosModel rel = relacion();

        when(generosRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto2()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(2L, 10L)).thenReturn(false);
        when(generoRepository.findById(20L)).thenReturn(Optional.of(genero2()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(2L, 20L)).thenReturn(false);
        when(generosRepository.save(any(GenerosModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        GenerosResponseDTO dto = generosService.patch(100L, 2L, 20L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(2L, dto.getProductoId());
        assertEquals(20L, dto.getGeneroId());
        assertEquals("Aventura", dto.getGeneroNombre());
    }

    @Test
    void testPatch_RelacionNoExiste_Lanza404() {
        when(generosRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generosService.patch(999L, 2L, null));
    }

    @Test
    void testPatch_NuevoProductoNoExiste_Lanza404() {
        GenerosModel rel = relacion();

        when(generosRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generosService.patch(100L, 2L, null));
    }

    @Test
    void testPatch_NuevoGeneroNoExiste_Lanza404() {
        GenerosModel rel = relacion();

        when(generosRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(generoRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generosService.patch(100L, null, 20L));
    }

    @Test
    void testPatch_NuevoProductoGeneraDuplicado_LanzaIllegalArgument() {
        GenerosModel rel = relacion();

        when(generosRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto2()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(2L, 10L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> generosService.patch(100L, 2L, null));
    }

    @Test
    void testPatch_NuevoGeneroGeneraDuplicado_LanzaIllegalArgument() {
        GenerosModel rel = relacion();

        when(generosRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(generoRepository.findById(20L)).thenReturn(Optional.of(genero2()));
        when(generosRepository.existsByProducto_IdAndGenero_Id(1L, 20L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> generosService.patch(100L, null, 20L));
    }

    // ================== RESUMEN ==================

    @Test
    void testObtenerResumen() {
        Object[] fila = new Object[] {100L, 1L, "Producto Test", 10L, "Acción"};
        List<Object[]> mock = List.<Object[]>of(fila);

        when(generosRepository.obtenerResumen(1L, 10L)).thenReturn(mock);

        List<Object[]> resultado = generosService.obtenerResumen(1L, 10L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        Object[] row = resultado.get(0);
        assertEquals(100L, row[0]);
        assertEquals(1L, row[1]);
        assertEquals("Producto Test", row[2]);
        assertEquals(10L, row[3]);
        assertEquals("Acción", row[4]);
    }
}