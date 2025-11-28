package com.example.NoLimits.service.catalogos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresasModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresasRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresasService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EmpresasServiceTest {

    @Autowired
    private EmpresasService empresasService;

    @MockBean
    private EmpresasRepository empresasRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private EmpresaRepository empresaRepository;

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

    @Test
    public void testFindByProducto() {
        List<EmpresasModel> lista = Arrays.asList(relacion());
        when(empresasRepository.findByProducto_Id(1L)).thenReturn(lista);

        List<EmpresasModel> resultado = empresasService.findByProducto(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(empresa().getId(), resultado.get(0).getEmpresa().getId());
    }

    @Test
    public void testFindByEmpresa() {
        List<EmpresasModel> lista = Arrays.asList(relacion());
        when(empresasRepository.findByEmpresa_Id(2L)).thenReturn(lista);

        List<EmpresasModel> resultado = empresasService.findByEmpresa(2L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(producto().getId(), resultado.get(0).getProducto().getId());
    }

    @Test
    public void testLink_CreaRelacionSiNoExiste() {
        ProductoModel p = producto();
        EmpresaModel e = empresa();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(empresaRepository.findById(2L)).thenReturn(Optional.of(e));
        when(empresasRepository.existsByProducto_IdAndEmpresa_Id(1L, 2L)).thenReturn(false);
        when(empresasRepository.save(any(EmpresasModel.class)))
                .thenAnswer(invocation -> {
                    EmpresasModel rel = invocation.getArgument(0);
                    rel.setId(10L);
                    return rel;
                });

        EmpresasModel resultado = empresasService.link(1L, 2L);

        assertNotNull(resultado);
        assertEquals(p, resultado.getProducto());
        assertEquals(e, resultado.getEmpresa());
        verify(empresasRepository, times(1)).save(any(EmpresasModel.class));
    }

    @Test
    public void testLink_LanzaRecursoNoEncontradoSiProductoNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresasService.link(1L, 2L));
    }

    @Test
    public void testLink_LanzaRecursoNoEncontradoSiEmpresaNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(empresaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresasService.link(1L, 2L));
    }

    @Test
    public void testLink_SiRelacionExisteDevuelveExistenteSinGuardar() {
        EmpresasModel existente = relacion();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(empresaRepository.findById(2L)).thenReturn(Optional.of(empresa()));
        when(empresasRepository.existsByProducto_IdAndEmpresa_Id(1L, 2L)).thenReturn(true);
        when(empresasRepository.findByProducto_Id(1L)).thenReturn(Arrays.asList(existente));

        EmpresasModel resultado = empresasService.link(1L, 2L);

        assertNotNull(resultado);
        assertEquals(existente.getId(), resultado.getId());
        verify(empresasRepository, never()).save(any(EmpresasModel.class));
    }

    @Test
    public void testUnlink_EliminaCuandoRelacionExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(empresaRepository.findById(2L)).thenReturn(Optional.of(empresa()));
        when(empresasRepository.existsByProducto_IdAndEmpresa_Id(1L, 2L)).thenReturn(true);

        empresasService.unlink(1L, 2L);

        verify(empresasRepository, times(1)).deleteByProducto_IdAndEmpresa_Id(1L, 2L);
    }

    @Test
    public void testUnlink_IdempotenteCuandoRelacionNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(empresaRepository.findById(2L)).thenReturn(Optional.of(empresa()));
        when(empresasRepository.existsByProducto_IdAndEmpresa_Id(1L, 2L)).thenReturn(false);

        empresasService.unlink(1L, 2L);

        verify(empresasRepository, never()).deleteByProducto_IdAndEmpresa_Id(1L, 2L);
    }

    @Test
    public void testUnlink_LanzaRecursoNoEncontradoSiProductoNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresasService.unlink(1L, 2L));
    }

    @Test
    public void testUnlink_LanzaRecursoNoEncontradoSiEmpresaNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(empresaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresasService.unlink(1L, 2L));
    }
}