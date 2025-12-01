package com.example.NoLimits.service.usuario;

import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.UsuarioUpdateDTO;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.service.usuario.UsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private VentaRepository ventaRepository;

    // ============ HELPERS ============

    private UsuarioModel usuarioEntity() {
        UsuarioModel u = new UsuarioModel();
        u.setId(1L);
        u.setNombre("Juan");
        u.setApellidos("Pérez");
        u.setCorreo("correo@test.com");
        u.setTelefono(123456789L);
        u.setPassword("password");
        RolModel rol = new RolModel();
        rol.setId(2L);
        rol.setNombre("CLIENTE");
        u.setRol(rol);
        return u;
    }

    private UsuarioRequestDTO usuarioRequest() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Juan");
        dto.setApellidos("Pérez");
        dto.setCorreo("correo@test.com");
        dto.setTelefono(123456789L);
        dto.setPassword("password");
        dto.setRolId(2L);
        return dto;
    }

    private UsuarioUpdateDTO usuarioUpdateCompleto() {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setNombre("Carlos");
        dto.setApellidos("Gómez");
        dto.setCorreo("nuevo@test.com");
        dto.setTelefono(987654321L);
        dto.setPassword("newpass");
        dto.setRolId(3L);
        return dto;
    }

    // ============ FIND ============

    @Test
    void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity()));

        List<UsuarioResponseDTO> usuarios = usuarioService.findAll();

        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());

        UsuarioResponseDTO dto = usuarios.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getNombre());
        assertEquals("Pérez", dto.getApellidos());
        assertEquals("correo@test.com", dto.getCorreo());
        assertEquals(123456789L, dto.getTelefono());
        assertEquals(2L, dto.getRolId());
        assertEquals("CLIENTE", dto.getRolNombre());
    }

    @Test
    void testFindById_Existe() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));

        UsuarioResponseDTO dto = usuarioService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getNombre());
        assertEquals("Pérez", dto.getApellidos());
        assertEquals("correo@test.com", dto.getCorreo());
    }

    @Test
    void testFindById_NoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> usuarioService.findById(99L));
    }

    // ============ SAVE ============

    @Test
    void testSave_OK() {
        UsuarioRequestDTO dto = usuarioRequest();

        when(usuarioRepository.existsByCorreo("correo@test.com")).thenReturn(false);
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> {
                    UsuarioModel u = inv.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        UsuarioResponseDTO guardado = usuarioService.save(dto);

        assertNotNull(guardado);
        assertEquals(1L, guardado.getId());
        assertEquals("Juan", guardado.getNombre());
        assertEquals("Pérez", guardado.getApellidos());
        assertEquals("correo@test.com", guardado.getCorreo());
        assertEquals(2L, guardado.getRolId());
    }

    @Test
    void testSave_PasswordLarga_Lanza400() {
        UsuarioRequestDTO dto = usuarioRequest();
        dto.setPassword("01234567890"); // 11 caracteres

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.save(dto));

        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void testSave_SinCorreo_Lanza400() {
        UsuarioRequestDTO dto = usuarioRequest();
        dto.setCorreo("   ");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.save(dto));

        assertEquals(400, ex.getStatusCode().value());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testSave_CorreoDuplicado_Lanza409() {
        UsuarioRequestDTO dto = usuarioRequest();

        when(usuarioRepository.existsByCorreo("correo@test.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.save(dto));

        assertEquals(409, ex.getStatusCode().value());
        verify(usuarioRepository, never()).save(any());
    }

    // ============ DELETE ============

    @Test
    void testDeleteById_ExisteSinVentas_EliminaOK() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(ventaRepository.findByUsuarioModel_Id(1L)).thenReturn(List.of());
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deleteById(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NoExiste_Lanza404() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> usuarioService.deleteById(99L));

        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteById_ConVentas_Lanza409() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(ventaRepository.findByUsuarioModel_Id(1L))
                .thenReturn(List.of(new VentaModel()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.deleteById(1L));

        assertEquals(409, ex.getStatusCode().value());
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    // ============ FIND BY NOMBRE / CORREO ============

    @Test
    void testFindByNombre_Null_Lanza400() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.findByNombre(null));

        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void testFindByNombre_Vacio_DevuelveTodos() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity()));

        var resultado = usuarioService.findByNombre("   ");

        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
        verify(usuarioRepository, never()).findByNombreContainingIgnoreCase(anyString());
    }

    @Test
    void testFindByNombre_Filtrado() {
        when(usuarioRepository.findByNombreContainingIgnoreCase("ju"))
                .thenReturn(List.of(usuarioEntity()));

        var resultado = usuarioService.findByNombre("ju");

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void testFindByCorreo_Existe() {
        when(usuarioRepository.findByCorreo("correo@test.com"))
                .thenReturn(Optional.of(usuarioEntity()));

        UsuarioResponseDTO dto = usuarioService.findByCorreo("  CORREO@test.com  ");

        assertNotNull(dto);
        assertEquals("correo@test.com", dto.getCorreo());
    }

    @Test
    void testFindByCorreo_NoExiste() {
        when(usuarioRepository.findByCorreo("correo@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> usuarioService.findByCorreo("correo@test.com"));
    }

    // ============ UPDATE ============

    @Test
    void testUpdate_OK() {
        UsuarioModel existente = usuarioEntity();
        UsuarioUpdateDTO datos = usuarioUpdateCompleto();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(false);
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponseDTO actualizado = usuarioService.update(1L, datos);

        assertNotNull(actualizado);
        assertEquals("Carlos", actualizado.getNombre());
        assertEquals("Gómez", actualizado.getApellidos());
        assertEquals("nuevo@test.com", actualizado.getCorreo());
        assertEquals(3L, actualizado.getRolId());
    }

    @Test
    void testUpdate_FaltanCampos_Lanza400() {
        UsuarioModel existente = usuarioEntity();
        UsuarioUpdateDTO incompleto = new UsuarioUpdateDTO(); // todo null

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.update(1L, incompleto));

        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void testUpdate_CorreoDuplicado_Lanza409() {
        UsuarioModel existente = usuarioEntity();
        UsuarioUpdateDTO datos = usuarioUpdateCompleto();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.update(1L, datos));

        assertEquals(409, ex.getStatusCode().value());
    }

    @Test
    void testUpdate_PasswordLarga_Lanza400() {
        UsuarioModel existente = usuarioEntity();
        UsuarioUpdateDTO datos = usuarioUpdateCompleto();
        datos.setPassword("12345678901"); // 11 chars

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.update(1L, datos));

        assertEquals(400, ex.getStatusCode().value());
    }

    // ============ PATCH ============

    @Test
    void testPatch_CambiaNombreYCorreo() {
        UsuarioModel existente = usuarioEntity();

        UsuarioUpdateDTO patch = new UsuarioUpdateDTO();
        patch.setNombre("Carlos");
        patch.setCorreo("otro@test.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByCorreo("otro@test.com")).thenReturn(false);
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponseDTO dto = usuarioService.patch(1L, patch);

        assertEquals("Carlos", dto.getNombre());
        assertEquals("otro@test.com", dto.getCorreo());
    }

    @Test
    void testPatch_CorreoDuplicado_Lanza409() {
        UsuarioModel existente = usuarioEntity();

        UsuarioUpdateDTO patch = new UsuarioUpdateDTO();
        patch.setCorreo("otro@test.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByCorreo("otro@test.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.patch(1L, patch));

        assertEquals(409, ex.getStatusCode().value());
    }

    @Test
    void testPatch_PasswordLarga_Lanza400() {
        UsuarioModel existente = usuarioEntity();

        UsuarioUpdateDTO patch = new UsuarioUpdateDTO();
        patch.setPassword("12345678901"); // 11 chars

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.patch(1L, patch));

        assertEquals(400, ex.getStatusCode().value());
    }

    // ============ RESUMEN USUARIOS ============

    @Test
    void testObtenerUsuariosConDatos() {
        Object[] fila = new Object[] {
                1L, "Juan", "Pérez", "correo@test.com", 123456789L
        };

        // lista con UN solo Object[]
        List<Object[]> filas = new java.util.ArrayList<>();
        filas.add(fila);
        // O bien:
        // List<Object[]> filas = java.util.Collections.singletonList(fila);

        when(usuarioRepository.obtenerUsuariosResumen()).thenReturn(filas);

        List<Map<String, Object>> resumen = usuarioService.obtenerUsuariosConDatos();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());

        Map<String, Object> row = resumen.get(0);
        assertEquals(1L, row.get("ID"));
        assertEquals("Juan", row.get("Nombre"));
        assertEquals("Pérez", row.get("Apellidos"));
        assertEquals("correo@test.com", row.get("Correo"));
        assertEquals(123456789L, row.get("Teléfono"));
    }


    // ============ DETALLE USUARIO / LOGIN ============

    @Test
    void testObtenerDetalleUsuario() {
        UsuarioModel u = usuarioEntity();
        VentaModel v1 = new VentaModel();
        v1.setId(10L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(ventaRepository.findByUsuarioModel_Id(1L)).thenReturn(List.of(v1));
        when(ventaRepository.countByUsuarioModel_Id(1L)).thenReturn(1L);

        Map<String, Object> detalle = usuarioService.obtenerDetalleUsuario(1L);

        assertNotNull(detalle);
        assertTrue(detalle.containsKey("usuario"));
        assertTrue(detalle.containsKey("compras"));
        assertTrue(detalle.containsKey("totalCompras"));

        UsuarioResponseDTO dto = (UsuarioResponseDTO) detalle.get("usuario");
        assertEquals(1L, dto.getId());

        @SuppressWarnings("unchecked")
        List<VentaModel> compras = (List<VentaModel>) detalle.get("compras");
        assertEquals(1, compras.size());
        assertEquals(1L, detalle.get("totalCompras"));
    }

    @Test
    void testLogin_OK() {
        when(usuarioRepository.findByCorreo("correo@test.com"))
                .thenReturn(Optional.of(usuarioEntity()));

        UsuarioResponseDTO dto = usuarioService.login("correo@test.com", "password");

        assertNotNull(dto);
        assertEquals("correo@test.com", dto.getCorreo());
    }

    @Test
    void testLogin_PasswordIncorrecta_Lanza401() {
        when(usuarioRepository.findByCorreo("correo@test.com"))
                .thenReturn(Optional.of(usuarioEntity()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.login("correo@test.com", "otra"));

        assertEquals(401, ex.getStatusCode().value());
    }

    @Test
    void testLogin_CorreoNoExiste_Lanza404() {
        when(usuarioRepository.findByCorreo("correo@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> usuarioService.login("correo@test.com", "password"));
    }
}