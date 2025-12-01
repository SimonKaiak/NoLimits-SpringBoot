package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresasResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresasUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresasModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresasRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresasService;

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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class EmpresasServiceTest {

    @Autowired
    private EmpresasService empresasService;

    @MockBean
    private EmpresasRepository empresasRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private EmpresaRepository empresaRepository;

    // ================== HELPERS ==================

    private ProductoModel producto() {
        ProductoModel p = new ProductoModel();
        p.setId(1L);
        p.setNombre("Spider-Man (2002)");
        p.setPrecio(12990.0);
        return p;
    }

    private EmpresaModel empresa() {
        EmpresaModel e = new EmpresaModel();
        e.setId(2L);
        e.setNombre("Sony Pictures");
        return e;
    }

    private EmpresasModel relacion() {
        EmpresasModel rel = new EmpresasModel();
        rel.setId(10L);
        rel.setProducto(producto());
        rel.setEmpresa(empresa());
        return rel;
    }

    private EmpresasUpdateDTO updateDTO(Long productoId, Long empresaId) {
        EmpresasUpdateDTO dto = new EmpresasUpdateDTO();
        dto.setProductoId(productoId);
        dto.setEmpresaId(empresaId);
        return dto;
    }

    // ================== FIND ==================

    @Test
    void testFindByProducto() {
        when(empresasRepository.findByProducto_Id(1L))
                .thenReturn(List.of(relacion()));

        List<EmpresasResponseDTO> resultado = empresasService.findByProducto(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        EmpresasResponseDTO dto = resultado.get(0);
        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getProductoId());
        assertEquals(2L, dto.getEmpresaId());
        assertEquals("Sony Pictures", dto.getEmpresaNombre());
    }

    // ================== LINK ==================

    @Test
    void testLink_CreaRelacionSiNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(empresaRepository.findById(2L)).thenReturn(Optional.of(empresa()));
        when(empresasRepository.existsByProducto_IdAndEmpresa_Id(1L, 2L)).thenReturn(false);
        when(empresasRepository.save(any(EmpresasModel.class)))
                .thenAnswer(invocation -> {
                    EmpresasModel rel = invocation.getArgument(0);
                    rel.setId(10L);
                    return rel;
                });

        EmpresasResponseDTO resultado = empresasService.link(1L, 2L);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(1L, resultado.getProductoId());
        assertEquals(2L, resultado.getEmpresaId());
        assertEquals("Sony Pictures", resultado.getEmpresaNombre());
        verify(empresasRepository, times(1)).save(any(EmpresasModel.class));
    }

    @Test
    void testLink_LanzaRecursoNoEncontradoSiProductoNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresasService.link(1L, 2L));

        verify(empresaRepository, never()).findById(2L);
        verify(empresasRepository, never()).save(any(EmpresasModel.class));
    }

    @Test
    void testLink_LanzaRecursoNoEncontradoSiEmpresaNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(empresaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresasService.link(1L, 2L));

        verify(empresasRepository, never()).save(any(EmpresasModel.class));
    }

    @Test
    void testLink_SiRelacionExisteDevuelveExistenteSinGuardar() {
        EmpresasModel existente = relacion();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(empresaRepository.findById(2L)).thenReturn(Optional.of(empresa()));
        when(empresasRepository.existsByProducto_IdAndEmpresa_Id(1L, 2L)).thenReturn(true);
        when(empresasRepository.findByProducto_Id(1L)).thenReturn(List.of(existente));

        EmpresasResponseDTO resultado = empresasService.link(1L, 2L);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(1L, resultado.getProductoId());
        assertEquals(2L, resultado.getEmpresaId());
        assertEquals("Sony Pictures", resultado.getEmpresaNombre());
        verify(empresasRepository, never()).save(any(EmpresasModel.class));
    }

    // ================== PATCH ==================

    @Test
    void testPatch_CambiaSoloProducto_OK() {
        EmpresasModel rel = relacion();
        EmpresasUpdateDTO dto = updateDTO(3L, null);

        ProductoModel nuevoProducto = new ProductoModel();
        nuevoProducto.setId(3L);
        nuevoProducto.setNombre("Producto Nuevo");

        when(empresasRepository.findById(10L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(3L)).thenReturn(Optional.of(nuevoProducto));
        when(empresasRepository.save(any(EmpresasModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        EmpresasResponseDTO resultado = empresasService.patch(10L, dto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(3L, resultado.getProductoId());
        assertEquals(2L, resultado.getEmpresaId());
        assertEquals("Sony Pictures", resultado.getEmpresaNombre());
    }

    @Test
    void testPatch_CambiaSoloEmpresa_OK() {
        EmpresasModel rel = relacion();
        EmpresasUpdateDTO dto = updateDTO(null, 4L);

        EmpresaModel nuevaEmpresa = new EmpresaModel();
        nuevaEmpresa.setId(4L);
        nuevaEmpresa.setNombre("Marvel Studios");

        when(empresasRepository.findById(10L)).thenReturn(Optional.of(rel));
        when(empresaRepository.findById(4L)).thenReturn(Optional.of(nuevaEmpresa));
        when(empresasRepository.save(any(EmpresasModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        EmpresasResponseDTO resultado = empresasService.patch(10L, dto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(1L, resultado.getProductoId());
        assertEquals(4L, resultado.getEmpresaId());
        assertEquals("Marvel Studios", resultado.getEmpresaNombre());
    }

    @Test
    void testPatch_CambiaProductoYEempresa_OK() {
        EmpresasModel rel = relacion();
        EmpresasUpdateDTO dto = updateDTO(3L, 4L);

        ProductoModel nuevoProducto = new ProductoModel();
        nuevoProducto.setId(3L);
        nuevoProducto.setNombre("Producto Nuevo");

        EmpresaModel nuevaEmpresa = new EmpresaModel();
        nuevaEmpresa.setId(4L);
        nuevaEmpresa.setNombre("Marvel Studios");

        when(empresasRepository.findById(10L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(3L)).thenReturn(Optional.of(nuevoProducto));
        when(empresaRepository.findById(4L)).thenReturn(Optional.of(nuevaEmpresa));
        when(empresasRepository.save(any(EmpresasModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        EmpresasResponseDTO resultado = empresasService.patch(10L, dto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(3L, resultado.getProductoId());
        assertEquals(4L, resultado.getEmpresaId());
        assertEquals("Marvel Studios", resultado.getEmpresaNombre());
    }

    @Test
    void testPatch_Lanza404_SiRelacionNoExiste() {
        EmpresasUpdateDTO dto = updateDTO(null, null);
        when(empresasRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresasService.patch(99L, dto));
    }

    @Test
    void testPatch_Lanza404_SiNuevoProductoNoExiste() {
        EmpresasModel rel = relacion();
        EmpresasUpdateDTO dto = updateDTO(3L, null);

        when(empresasRepository.findById(10L)).thenReturn(Optional.of(rel));
        when(productoRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresasService.patch(10L, dto));
    }

    @Test
    void testPatch_Lanza404_SiNuevaEmpresaNoExiste() {
        EmpresasModel rel = relacion();
        EmpresasUpdateDTO dto = updateDTO(null, 4L);

        when(empresasRepository.findById(10L)).thenReturn(Optional.of(rel));
        when(empresaRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresasService.patch(10L, dto));
    }

    // ================== UNLINK ==================

    @Test
    void testUnlink_InvocaDeleteConIds() {
        empresasService.unlink(1L, 2L);

        verify(empresasRepository, times(1))
                .deleteByProducto_IdAndEmpresa_Id(1L, 2L);
    }
}