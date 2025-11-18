// Ruta: src/test/java/com/example/NoLimits/service/TipoEmpresaServiceTest.java
package com.example.NoLimits.service;

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
import com.example.NoLimits.Multimedia.model.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.repository.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.TiposEmpresaRepository;
import com.example.NoLimits.Multimedia.service.TipoEmpresaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TipoEmpresaServiceTest {

    @Autowired
    private TipoEmpresaService tipoEmpresaService;

    @MockBean
    private TipoEmpresaRepository tipoEmpresaRepository;

    @MockBean
    private TiposEmpresaRepository tiposEmpresaRepository;

    private TipoEmpresaModel tipoEmpresa() {
        TipoEmpresaModel t = new TipoEmpresaModel();
        t.setId(1L);
        t.setNombre("Publisher");
        return t;
    }

    @Test
    public void testFindAll() {
        List<TipoEmpresaModel> lista = Arrays.asList(tipoEmpresa());
        when(tipoEmpresaRepository.findAll()).thenReturn(lista);

        List<TipoEmpresaModel> resultado = tipoEmpresaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Publisher", resultado.get(0).getNombre());
    }

    @Test
    public void testFindById_Existe() {
        when(tipoEmpresaRepository.findById(1L)).thenReturn(Optional.of(tipoEmpresa()));

        TipoEmpresaModel resultado = tipoEmpresaService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void testFindById_NoExiste_LanzaRecursoNoEncontrado() {
        when(tipoEmpresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tipoEmpresaService.findById(1L));
    }

    @Test
    public void testSave_Valido() {
        TipoEmpresaModel entrada = new TipoEmpresaModel();
        entrada.setNombre("  Publisher  ");

        when(tipoEmpresaRepository.existsByNombreIgnoreCase("Publisher")).thenReturn(false);
        when(tipoEmpresaRepository.save(any(TipoEmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TipoEmpresaModel resultado = tipoEmpresaService.save(entrada);

        assertNotNull(resultado);
        assertEquals("Publisher", resultado.getNombre());
        verify(tipoEmpresaRepository).save(any(TipoEmpresaModel.class));
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgument() {
        TipoEmpresaModel entrada = new TipoEmpresaModel();
        entrada.setNombre("   ");

        assertThrows(IllegalArgumentException.class,
                () -> tipoEmpresaService.save(entrada));

        verify(tipoEmpresaRepository, never()).save(any(TipoEmpresaModel.class));
    }

    @Test
    public void testSave_NombreDuplicado_LanzaIllegalArgument() {
        TipoEmpresaModel entrada = new TipoEmpresaModel();
        entrada.setNombre("Publisher");

        when(tipoEmpresaRepository.existsByNombreIgnoreCase("Publisher")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> tipoEmpresaService.save(entrada));

        verify(tipoEmpresaRepository, never()).save(any(TipoEmpresaModel.class));
    }

    @Test
    public void testUpdate_CambiaNombreValido() {
        TipoEmpresaModel existente = tipoEmpresa();
        TipoEmpresaModel entrada = new TipoEmpresaModel();
        entrada.setNombre("Distribuidora");

        when(tipoEmpresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoEmpresaRepository.existsByNombreIgnoreCase("Distribuidora")).thenReturn(false);
        when(tipoEmpresaRepository.save(existente)).thenReturn(existente);

        TipoEmpresaModel resultado = tipoEmpresaService.update(1L, entrada);

        assertEquals("Distribuidora", resultado.getNombre());
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        TipoEmpresaModel existente = tipoEmpresa();
        TipoEmpresaModel entrada = new TipoEmpresaModel();
        entrada.setNombre("   ");

        when(tipoEmpresaRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> tipoEmpresaService.update(1L, entrada));
    }

    @Test
    public void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        TipoEmpresaModel existente = tipoEmpresa();
        TipoEmpresaModel entrada = new TipoEmpresaModel();
        entrada.setNombre("Distribuidora");

        when(tipoEmpresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoEmpresaRepository.existsByNombreIgnoreCase("Distribuidora")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> tipoEmpresaService.update(1L, entrada));
    }

    @Test
    public void testDeleteById_EliminaCuandoNoTieneRelaciones() {
        TipoEmpresaModel existente = tipoEmpresa();

        when(tipoEmpresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tiposEmpresaRepository.existsByTipoEmpresa_Id(1L)).thenReturn(false);

        tipoEmpresaService.deleteById(1L);

        verify(tipoEmpresaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_ConRelaciones_LanzaIllegalState() {
        TipoEmpresaModel existente = tipoEmpresa();

        when(tipoEmpresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tiposEmpresaRepository.existsByTipoEmpresa_Id(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> tipoEmpresaService.deleteById(1L));

        verify(tipoEmpresaRepository, never()).deleteById(1L);
    }
}