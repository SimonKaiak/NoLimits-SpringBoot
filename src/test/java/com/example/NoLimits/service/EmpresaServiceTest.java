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
import com.example.NoLimits.Multimedia.model.EmpresaModel;
import com.example.NoLimits.Multimedia.repository.EmpresaRepository;
import com.example.NoLimits.Multimedia.service.EmpresaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EmpresaServiceTest {

    @Autowired
    private EmpresaService empresaService;

    @MockBean
    private EmpresaRepository empresaRepository;

    private EmpresaModel empresa() {
        EmpresaModel e = new EmpresaModel();
        e.setId(1L);
        e.setNombre("Sony Pictures");
        return e;
    }

    @Test
    public void testFindAll() {
        List<EmpresaModel> lista = Arrays.asList(empresa());
        when(empresaRepository.findAll()).thenReturn(lista);

        List<EmpresaModel> resultado = empresaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sony Pictures", resultado.get(0).getNombre());
    }

    @Test
    public void testFindById_Existe() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa()));

        EmpresaModel resultado = empresaService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void testFindById_NoExiste_LanzaRecursoNoEncontrado() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresaService.findById(1L));
    }

    @Test
    public void testSave_Valido() {
        EmpresaModel entrada = new EmpresaModel();
        entrada.setNombre("  Sony Pictures  ");

        when(empresaRepository.existsByNombreIgnoreCase("Sony Pictures")).thenReturn(false);
        when(empresaRepository.save(any(EmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmpresaModel resultado = empresaService.save(entrada);

        assertNotNull(resultado);
        assertEquals("Sony Pictures", resultado.getNombre());
        verify(empresaRepository, times(1)).save(any(EmpresaModel.class));
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgument() {
        EmpresaModel entrada = new EmpresaModel();
        entrada.setNombre("   ");

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.save(entrada));

        verify(empresaRepository, never()).save(any(EmpresaModel.class));
    }

    @Test
    public void testSave_NombreDuplicado_LanzaIllegalArgument() {
        EmpresaModel entrada = new EmpresaModel();
        entrada.setNombre("Sony Pictures");

        when(empresaRepository.existsByNombreIgnoreCase("Sony Pictures")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.save(entrada));

        verify(empresaRepository, never()).save(any(EmpresaModel.class));
    }

    @Test
    public void testUpdate_CambiaNombreValido() {
        EmpresaModel existente = empresa();
        EmpresaModel entrada = new EmpresaModel();
        entrada.setNombre("Warner Bros");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Warner Bros")).thenReturn(false);
        when(empresaRepository.save(existente)).thenReturn(existente);

        EmpresaModel resultado = empresaService.update(1L, entrada);

        assertEquals("Warner Bros", resultado.getNombre());
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        EmpresaModel existente = empresa();
        EmpresaModel entrada = new EmpresaModel();
        entrada.setNombre("   ");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.update(1L, entrada));
    }

    @Test
    public void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        EmpresaModel existente = empresa();
        EmpresaModel entrada = new EmpresaModel();
        entrada.setNombre("Warner Bros");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.existsByNombreIgnoreCase("Warner Bros")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> empresaService.update(1L, entrada));
    }

    @Test
    public void testDeleteById_Existe_Elimina() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa()));

        empresaService.deleteById(1L);

        verify(empresaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> empresaService.deleteById(1L));

        verify(empresaRepository, never()).deleteById(1L);
    }
}