package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;
import com.example.NoLimits.Multimedia.service.UsuarioService;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    // Método auxiliar para crear un usuario de ejemplo
    private UsuarioModel createUsuario() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("correo@test.com");
        usuario.setTelefono(123456789);
        usuario.setPassword("password");
        return usuario;
    }

    @Test
    public void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(createUsuario()));
        List<UsuarioModel> usuarios = usuarioService.findAll();
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }

    @Test
    public void testFindById_Existe() {
        UsuarioModel mockUsuario = createUsuario();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(mockUsuario));

        UsuarioModel resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Pérez", resultado.getApellidos());
        assertEquals("correo@test.com", resultado.getCorreo());
        assertEquals(123456789, resultado.getTelefono());
    }

    @Test
    public void testFindById_NoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        // El service lanza ResponseStatusException (404), no RecursoNoEncontradoException
        assertThrows(ResponseStatusException.class, () -> usuarioService.findById(99L));
    }

    @Test
    public void testSave() {
        UsuarioModel usuario = createUsuario();
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        UsuarioModel savedUsuario = usuarioService.save(usuario);

        assertNotNull(savedUsuario);
        assertEquals("Juan", savedUsuario.getNombre());
        assertEquals("Pérez", savedUsuario.getApellidos());
        assertEquals("correo@test.com", savedUsuario.getCorreo());
    }

    @Test
    public void testUpdateUsuario() {
        UsuarioModel original = createUsuario();
        UsuarioModel cambios = new UsuarioModel();
        cambios.setNombre("Carlos");
        cambios.setApellidos("Gómez");
        cambios.setCorreo("nuevo@test.com");
        cambios.setTelefono(987654321);
        cambios.setPassword("nueva_pass");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(original));
        when(usuarioRepository.save(any(UsuarioModel.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioModel actualizado = usuarioService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("Carlos", actualizado.getNombre());
        assertEquals("Gómez", actualizado.getApellidos());
        assertEquals("nuevo@test.com", actualizado.getCorreo());
    }

    @Test
    public void testPatchUsuario() {
        UsuarioModel original = createUsuario();
        UsuarioModel patch = new UsuarioModel();
        patch.setNombre("Carlos");
        patch.setApellidos("Gómez");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(original));
        when(usuarioRepository.save(any(UsuarioModel.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioModel patched = usuarioService.patch(1L, patch);

        assertNotNull(patched);
        assertEquals("Carlos", patched.getNombre());
        assertEquals("Gómez", patched.getApellidos());
        assertEquals(original.getCorreo(), patched.getCorreo()); // no cambia
    }

    @Test
    public void testDeleteById_Existe() {
        UsuarioModel usuario = createUsuario();

        // El service usa findById(...).orElseThrow(...)
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deleteById(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste() {
        // El service verifica con findById(...).orElseThrow(...)
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> usuarioService.deleteById(99L));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}