// Ruta: src/test/java/com/example/NoLimits/service/RolServiceTest.java
package com.example.NoLimits.service.usuario;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.service.usuario.RolService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@SpringBootTest
@ActiveProfiles("test")
public class RolServiceTest {

    @Autowired
    private RolService rolService;

    @MockBean
    private RolRepository rolRepository;

    private RolModel crearRol() {
        RolModel r = new RolModel();
        r.setId(1L);
        r.setNombre("ADMIN");
        r.setActivo(true);
        return r;
    }

    @Test
    public void testFindAll() {
        when(rolRepository.findAll()).thenReturn(Arrays.asList(crearRol()));

        List<RolModel> result = rolService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Existe() {
        RolModel r = crearRol();
        when(rolRepository.findById(1L)).thenReturn(Optional.of(r));

        RolModel result = rolService.findById(1L);

        assertNotNull(result);
        assertEquals("ADMIN", result.getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> rolService.findById(99L));
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgumentException() {
        RolModel r = new RolModel();
        r.setNombre("  ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> rolService.save(r));

        assertTrue(ex.getMessage().contains("nombre del rol es obligatorio"));
        verify(rolRepository, never()).save(any(RolModel.class));
    }

    @Test
    public void testSave_OK() {
        RolModel entrada = new RolModel();
        entrada.setNombre("  CLIENTE  ");

        when(rolRepository.save(any(RolModel.class))).thenAnswer(inv -> inv.getArgument(0));

        RolModel result = rolService.save(entrada);

        assertEquals("CLIENTE", result.getNombre());
    }

    @Test
    public void testUpdate_CambiaNombre() {
        RolModel existente = crearRol();

        RolModel in = new RolModel();
        in.setNombre("  VENDEDOR  ");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(RolModel.class))).thenAnswer(inv -> inv.getArgument(0));

        RolModel result = rolService.update(1L, in);

        assertEquals("VENDEDOR", result.getNombre());
    }

    @Test
    public void testDeleteById_ConUsuarios_LanzaIllegalStateException() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(crearRol()));
        when(rolRepository.existeUsuarioConRol(1L)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> rolService.deleteById(1L));

        assertTrue(ex.getMessage().contains("hay usuarios con este rol"));
        verify(rolRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteById_SinUsuarios_EliminaOK() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(crearRol()));
        when(rolRepository.existeUsuarioConRol(1L)).thenReturn(false);

        rolService.deleteById(1L);

        verify(rolRepository, times(1)).deleteById(1L);
    }
}