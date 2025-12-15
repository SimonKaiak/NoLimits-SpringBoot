package com.example.NoLimits.service;

import java.util.List;
import java.util.Optional;

import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.VentaRepository;
import com.example.NoLimits.Multimedia.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private VentaRepository ventaRepository;

    private UsuarioModel createUsuario() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("correo@test.com");
        usuario.setTelefono(123456789L);
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
        assertThrows(ResponseStatusException.class, () -> usuarioService.findById(99L));
    }

    @Test
    public void testSave() {
        UsuarioModel usuario = createUsuario();
        when(usuarioRepository.existsByCorreo("correo@test.com")).thenReturn(false);
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
        cambios.setTelefono(987654321L);
        cambios.setPassword("nueva_pass");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(original));
        when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(false);
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
        assertEquals(original.getCorreo(), patched.getCorreo());
    }

    @Test
    public void testDeleteById_Existe_SinVentas() {
        UsuarioModel usuario = createUsuario();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(ventaRepository.findByUsuarioModel_Id(1L)).thenReturn(List.of());
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deleteById(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> usuarioService.deleteById(99L));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}