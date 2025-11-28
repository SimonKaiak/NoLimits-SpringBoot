// Ruta: src/test/java/com/example/NoLimits/service/TiposEmpresaServiceTest.java
package com.example.NoLimits.service.catalogos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TiposEmpresaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposEmpresaRepository;
import com.example.NoLimits.Multimedia.service.catalogos.TiposEmpresaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TiposEmpresaServiceTest {

    @Autowired
    private TiposEmpresaService tiposEmpresaService;

    @MockBean
    private TiposEmpresaRepository tiposEmpresaRepository;

    @MockBean
    private EmpresaRepository empresaRepository;

    @MockBean
    private TipoEmpresaRepository tipoEmpresaRepository;

    private EmpresaModel empresa() {
        EmpresaModel e = new EmpresaModel();
        e.setId(1L);
        e.setNombre("Sony");
        return e;
    }

    private TipoEmpresaModel tipoEmpresa() {
        TipoEmpresaModel t = new TipoEmpresaModel();
        t.setId(2L);
        t.setNombre("Publisher");
        return t;
    }

    private TiposEmpresaModel relacion() {
        TiposEmpresaModel rel = new TiposEmpresaModel();
        rel.setId(10L);
        rel.setEmpresa(empresa());
        rel.setTipoEmpresa(tipoEmpresa());
        return rel;
    }

    @Test
    public void testFindAll() {
        List<TiposEmpresaModel> lista = Arrays.asList(relacion());
        when(tiposEmpresaRepository.findAll()).thenReturn(lista);

        List<TiposEmpresaModel> resultado = tiposEmpresaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(empresa().getId(), resultado.get(0).getEmpresa().getId());
    }

    @Test
    public void testLink_CreaRelacionCuandoNoExiste() {
        EmpresaModel emp = empresa();
        TipoEmpresaModel tipo = tipoEmpresa();

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(tipoEmpresaRepository.findById(2L)).thenReturn(Optional.of(tipo));
        when(tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(1L, 2L)).thenReturn(false);
        when(tiposEmpresaRepository.save(any(TiposEmpresaModel.class)))
                .thenAnswer(invocation -> {
                    TiposEmpresaModel rel = invocation.getArgument(0);
                    rel.setId(10L);
                    return rel;
                });

        TiposEmpresaModel resultado = tiposEmpresaService.link(1L, 2L);

        assertNotNull(resultado);
        assertEquals(emp, resultado.getEmpresa());
        assertEquals(tipo, resultado.getTipoEmpresa());
        verify(tiposEmpresaRepository).save(any(TiposEmpresaModel.class));
    }

    @Test
    public void testLink_LanzaExcepcionSiEmpresaNoExiste() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tiposEmpresaService.link(1L, 2L));
    }

    @Test
    public void testLink_LanzaExcepcionSiTipoEmpresaNoExiste() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa()));
        when(tipoEmpresaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tiposEmpresaService.link(1L, 2L));
    }

    @Test
    public void testLink_LanzaExcepcionSiRelacionYaExiste() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa()));
        when(tipoEmpresaRepository.findById(2L)).thenReturn(Optional.of(tipoEmpresa()));
        when(tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(1L, 2L)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> tiposEmpresaService.link(1L, 2L));

        verify(tiposEmpresaRepository, never()).save(any(TiposEmpresaModel.class));
    }

    @Test
    public void testUnlink_EliminaCuandoExiste() {
        TiposEmpresaModel rel = relacion();
        when(tiposEmpresaRepository.findByEmpresa_IdAndTipoEmpresa_Id(1L, 2L))
                .thenReturn(Optional.of(rel));

        tiposEmpresaService.unlink(1L, 2L);

        verify(tiposEmpresaRepository).delete(rel);
    }

    @Test
    public void testUnlink_LanzaExcepcionSiNoExiste() {
        when(tiposEmpresaRepository.findByEmpresa_IdAndTipoEmpresa_Id(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tiposEmpresaService.unlink(1L, 2L));
    }
}