package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformasResponseDTO;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformasModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformasRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformasService;

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
class PlataformasServiceTest {

    @Autowired
    private PlataformasService service;

    @MockBean
    private PlataformasRepository plataformasRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private PlataformaRepository plataformaRepository;

    // ================== HELPERS ==================

    private ProductoModel producto() {
        ProductoModel p = new ProductoModel();
        p.setId(10L);
        p.setNombre("Producto Test");
        return p;
    }

    private ProductoModel producto2() {
        ProductoModel p = new ProductoModel();
        p.setId(99L);
        p.setNombre("Producto Nuevo");
        return p;
    }

    private PlataformaModel plataforma() {
        PlataformaModel p = new PlataformaModel();
        p.setId(20L);
        p.setNombre("PlayStation 5");
        return p;
    }

    private PlataformaModel plataforma2() {
        PlataformaModel p = new PlataformaModel();
        p.setId(77L);
        p.setNombre("PC");
        return p;
    }

    private PlataformasModel relacion() {
        PlataformasModel r = new PlataformasModel();
        r.setId(100L);
        r.setProducto(producto());
        r.setPlataforma(plataforma());
        return r;
    }

    // ================== FIND ==================

    @Test
    void testFindByProducto() {
        when(plataformasRepository.findByProducto_Id(10L))
                .thenReturn(List.of(relacion()));

        List<PlataformasResponseDTO> lista = service.findByProducto(10L);

        assertNotNull(lista);
        assertEquals(1, lista.size());

        PlataformasResponseDTO dto = lista.get(0);
        assertEquals(100L, dto.getId());
        assertEquals(10L, dto.getProductoId());
        assertEquals(20L, dto.getPlataformaId());
        assertEquals("PlayStation 5", dto.getPlataformaNombre());
    }

    @Test
    void testFindByPlataforma() {
        when(plataformasRepository.findByPlataforma_Id(20L))
                .thenReturn(List.of(relacion()));

        List<PlataformasResponseDTO> lista = service.findByPlataforma(20L);

        assertNotNull(lista);
        assertEquals(1, lista.size());

        PlataformasResponseDTO dto = lista.get(0);
        assertEquals(100L, dto.getId());
        assertEquals(10L, dto.getProductoId());
        assertEquals(20L, dto.getPlataformaId());
        assertEquals("PlayStation 5", dto.getPlataformaNombre());
    }

    // ================== LINK ==================

    @Test
    void testLink_CreaRelacionSiNoExiste() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto()));
        when(plataformaRepository.findById(20L)).thenReturn(Optional.of(plataforma()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(10L, 20L))
                .thenReturn(false);
        when(plataformasRepository.save(any(PlataformasModel.class)))
                .thenAnswer(inv -> {
                    PlataformasModel r = inv.getArgument(0);
                    r.setId(100L);
                    return r;
                });

        PlataformasResponseDTO dto = service.link(10L, 20L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(10L, dto.getProductoId());
        assertEquals(20L, dto.getPlataformaId());
        assertEquals("PlayStation 5", dto.getPlataformaNombre());
        verify(plataformasRepository, times(1)).save(any(PlataformasModel.class));
    }

    @Test
    void testLink_ProductoNoExiste_Lanza404() {
        when(productoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.link(10L, 20L));

        verify(plataformaRepository, never()).findById(anyLong());
        verify(plataformasRepository, never()).save(any(PlataformasModel.class));
    }

    @Test
    void testLink_PlataformaNoExiste_Lanza404() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto()));
        when(plataformaRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.link(10L, 20L));

        verify(plataformasRepository, never()).save(any(PlataformasModel.class));
    }

    @Test
    void testLink_YaExisteDevuelveExistenteSinGuardar() {
        PlataformasModel existente = relacion();

        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto()));
        when(plataformaRepository.findById(20L)).thenReturn(Optional.of(plataforma()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(10L, 20L))
                .thenReturn(true);
        when(plataformasRepository.findByProducto_Id(10L))
                .thenReturn(List.of(existente));

        PlataformasResponseDTO dto = service.link(10L, 20L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(10L, dto.getProductoId());
        assertEquals(20L, dto.getPlataformaId());
        assertEquals("PlayStation 5", dto.getPlataformaNombre());
        verify(plataformasRepository, never()).save(any(PlataformasModel.class));
    }

    // ================== UNLINK ==================

    @Test
    void testUnlink_ExisteRelacion_Elimina() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto()));
        when(plataformaRepository.findById(20L)).thenReturn(Optional.of(plataforma()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(10L, 20L))
                .thenReturn(true);

        service.unlink(10L, 20L);

        verify(plataformasRepository, times(1))
                .deleteByProducto_IdAndPlataforma_Id(10L, 20L);
    }

    @Test
    void testUnlink_NoExisteRelacion_NoElimina() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto()));
        when(plataformaRepository.findById(20L)).thenReturn(Optional.of(plataforma()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(10L, 20L))
                .thenReturn(false);

        service.unlink(10L, 20L);

        verify(plataformasRepository, never())
                .deleteByProducto_IdAndPlataforma_Id(anyLong(), anyLong());
    }

    @Test
    void testUnlink_ProductoNoExiste_Lanza404() {
        when(productoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.unlink(10L, 20L));

        verify(plataformasRepository, never())
                .deleteByProducto_IdAndPlataforma_Id(anyLong(), anyLong());
    }

    @Test
    void testUnlink_PlataformaNoExiste_Lanza404() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto()));
        when(plataformaRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.unlink(10L, 20L));

        verify(plataformasRepository, never())
                .deleteByProducto_IdAndPlataforma_Id(anyLong(), anyLong());
    }

    // ================== PATCH ==================

    @Test
    void testPatch_CambiaSoloProducto_OK() {
        PlataformasModel rel = relacion();

        when(plataformasRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(99L)).thenReturn(Optional.of(producto2()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(99L, 20L))
                .thenReturn(false);
        when(plataformasRepository.save(any(PlataformasModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        PlataformasResponseDTO dto = service.patch(100L, 99L, null);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(99L, dto.getProductoId());
        assertEquals(20L, dto.getPlataformaId());
        assertEquals("PlayStation 5", dto.getPlataformaNombre());
    }

    @Test
    void testPatch_CambiaSoloPlataforma_OK() {
        PlataformasModel rel = relacion();

        when(plataformasRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(plataformaRepository.findById(77L)).thenReturn(Optional.of(plataforma2()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(10L, 77L))
                .thenReturn(false);
        when(plataformasRepository.save(any(PlataformasModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        PlataformasResponseDTO dto = service.patch(100L, null, 77L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(10L, dto.getProductoId());
        assertEquals(77L, dto.getPlataformaId());
        assertEquals("PC", dto.getPlataformaNombre());
    }

    @Test
    void testPatch_CambiaProductoYPlataforma_OK() {
        PlataformasModel rel = relacion();

        when(plataformasRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(99L)).thenReturn(Optional.of(producto2()));
        // check duplicado al cambiar producto (con plataforma vieja 20L)
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(99L, 20L))
                .thenReturn(false);
        when(plataformaRepository.findById(77L)).thenReturn(Optional.of(plataforma2()));
        // check duplicado al cambiar plataforma (ahora producto ya es 99L)
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(99L, 77L))
                .thenReturn(false);
        when(plataformasRepository.save(any(PlataformasModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        PlataformasResponseDTO dto = service.patch(100L, 99L, 77L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(99L, dto.getProductoId());
        assertEquals(77L, dto.getPlataformaId());
        assertEquals("PC", dto.getPlataformaNombre());
    }

    @Test
    void testPatch_RelacionNoExiste_Lanza404() {
        when(plataformasRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.patch(123L, 99L, null));
    }

    @Test
    void testPatch_NuevoProductoNoExiste_Lanza404() {
        PlataformasModel rel = relacion();

        when(plataformasRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.patch(100L, 99L, null));
    }

    @Test
    void testPatch_NuevaPlataformaNoExiste_Lanza404() {
        PlataformasModel rel = relacion();

        when(plataformasRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(plataformaRepository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.patch(100L, null, 77L));
    }

    @Test
    void testPatch_NuevoProductoGeneraDuplicado_LanzaIllegalArgument() {
        PlataformasModel rel = relacion();

        when(plataformasRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(99L)).thenReturn(Optional.of(producto2()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(99L, 20L))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.patch(100L, 99L, null));
    }

    @Test
    void testPatch_NuevaPlataformaGeneraDuplicado_LanzaIllegalArgument() {
        PlataformasModel rel = relacion();

        when(plataformasRepository.findById(100L)).thenReturn(Optional.of(rel));
        when(plataformaRepository.findById(77L)).thenReturn(Optional.of(plataforma2()));
        when(plataformasRepository.existsByProducto_IdAndPlataforma_Id(10L, 77L))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.patch(100L, null, 77L));
    }
}