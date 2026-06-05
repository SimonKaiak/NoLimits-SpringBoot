package com.example.NoLimits.service.usuario;

import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRegistroDTO;
import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.UsuarioUpdateDTO;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.usuario.FavoritoRepository;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.repository.review.ReviewRepository;
import com.example.NoLimits.Multimedia.repository.review.ReviewReactionRepository;
import com.example.NoLimits.Multimedia.service.usuario.UsuarioService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UsuarioServiceTest — Pruebas unitarias del servicio de usuarios.
 *
 * Cubre: findAll, findById, save (registro público), saveDesdeAdmin,
 * deleteById, findByNombre, findByCorreo, update, patch, login.
 */
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private VentaRepository ventaRepository;

    @MockBean
    private DireccionRepository direccionRepository;

    @MockBean
    private ComunaRepository comunaRepository;

    @MockBean
    private RolRepository rolRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private FavoritoRepository favoritoRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private ReviewReactionRepository reviewReactionRepository;

    // ===================== HELPERS =====================

    private RolModel rolEntity() {
        RolModel rol = new RolModel();
        rol.setId(2L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);
        return rol;
    }

    private UsuarioModel usuarioEntity() {
        UsuarioModel u = new UsuarioModel();
        u.setId(1L);
        u.setNombre("Juan");
        u.setApellidos("Pérez");
        u.setCorreo("juan@test.com");
        u.setTelefono(123456789L);
        u.setPassword("$2a$10$hash");
        u.setRol(rolEntity());
        return u;
    }

    private UsuarioRequestDTO requestDTO() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Juan");
        dto.setApellidos("Pérez");
        dto.setCorreo("juan@test.com");
        dto.setTelefono(123456789L);
        dto.setPassword("Password123!");
        dto.setRolId(2L);
        return dto;
    }

    private UsuarioRegistroDTO registroDTO() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setNombre("Juan");
        dto.setApellidos("Pérez");
        dto.setCorreo("juan@test.com");
        dto.setTelefono(123456789L);
        dto.setPassword("Password123!");
        return dto;
    }

    private UsuarioUpdateDTO updateCompletoDTO() {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setNombre("Carlos");
        dto.setApellidos("Gómez");
        dto.setCorreo("carlos@test.com");
        dto.setTelefono(987654321L);
        dto.setPassword("NuevaClave123!");
        dto.setRolId(2L);
        return dto;
    }

    // ===================== FIND ALL =====================

    @Test
    void testFindAll_DevuelveLista() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity()));

        List<UsuarioResponseDTO> result = usuarioService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("juan@test.com", result.get(0).getCorreo());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ListaVacia_DevuelveVacio() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<UsuarioResponseDTO> result = usuarioService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ===================== FIND BY ID =====================

    @Test
    void testFindById_Existe_DevuelveDTO() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));

        UsuarioResponseDTO result = usuarioService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getNombre());
        assertEquals("juan@test.com", result.getCorreo());
    }

    @Test
    void testFindById_NoExiste_Lanza404() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.findById(99L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ===================== SAVE (registro público) =====================

    @Test
    void testSave_OK_RegistraNuevoUsuario() {
        when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
        when(rolRepository.findByNombreIgnoreCase("ROLE_USER")).thenReturn(Optional.of(rolEntity()));
        when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hash");
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> {
                    UsuarioModel u = inv.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        UsuarioResponseDTO result = usuarioService.save(registroDTO());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("juan@test.com", result.getCorreo());
        verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
    }

    @Test
    void testSave_PasswordVacia_Lanza400() {
        UsuarioRegistroDTO dto = registroDTO();
        dto.setPassword("");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.save(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testSave_PasswordCorta_Lanza400() {
        UsuarioRegistroDTO dto = registroDTO();
        dto.setPassword("corta");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.save(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testSave_PasswordDemasiadoLarga_Lanza400() {
        UsuarioRegistroDTO dto = registroDTO();
        dto.setPassword("A".repeat(256));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.save(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testSave_SinCorreo_Lanza400() {
        UsuarioRegistroDTO dto = registroDTO();
        dto.setCorreo("   ");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.save(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testSave_CorreoDuplicado_Lanza409() {
        when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.save(registroDTO()));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testSave_CorreoSeNormalizaEnMinusculas() {
        UsuarioRegistroDTO dto = registroDTO();
        dto.setCorreo("JUAN@TEST.COM");

        when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
        when(rolRepository.findByNombreIgnoreCase("ROLE_USER")).thenReturn(Optional.of(rolEntity()));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hash");
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> {
                    UsuarioModel u = inv.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        UsuarioResponseDTO result = usuarioService.save(dto);

        assertNotNull(result);
        // El correo guardado debe estar en minúsculas
        verify(usuarioRepository).existsByCorreo("juan@test.com");
    }

    // ===================== SAVE DESDE ADMIN =====================

    @Test
    void testSaveDesdeAdmin_OK() {
        when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
        when(rolRepository.findById(2L)).thenReturn(Optional.of(rolEntity()));
        when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hash");
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> {
                    UsuarioModel u = inv.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        UsuarioResponseDTO result = usuarioService.saveDesdeAdmin(requestDTO());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
    }

    @Test
    void testSaveDesdeAdmin_SinRolId_Lanza400() {
        UsuarioRequestDTO dto = requestDTO();
        dto.setRolId(null);

        when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.saveDesdeAdmin(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testSaveDesdeAdmin_RolNoEncontrado_Lanza404() {
        when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
        when(rolRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.saveDesdeAdmin(requestDTO()));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ===================== DELETE =====================

    @Test
    void testDeleteById_ExisteSinVentas_EliminaOK() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(ventaRepository.findByUsuarioModel_Id(1L)).thenReturn(List.of());

        assertDoesNotThrow(() -> usuarioService.deleteById(1L));

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NoExiste_Lanza404() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.deleteById(99L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteById_ConVentas_Lanza409() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(ventaRepository.findByUsuarioModel_Id(1L)).thenReturn(List.of(new VentaModel()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.deleteById(1L));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    // ===================== FIND BY NOMBRE =====================

    @Test
    void testFindByNombre_Null_Lanza400() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.findByNombre(null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testFindByNombre_Vacio_DevuelveTodos() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity()));

        List<UsuarioResponseDTO> result = usuarioService.findByNombre("");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByNombre_Filtrado_DevuelveCoincidencias() {
        UsuarioModel u1 = usuarioEntity();
        UsuarioModel u2 = new UsuarioModel();
        u2.setId(2L);
        u2.setNombre("Pedro");
        u2.setApellidos("López");
        u2.setCorreo("pedro@test.com");
        u2.setTelefono(111222333L);
        u2.setPassword("$2a$10$hash2");
        u2.setRol(rolEntity());

        when(usuarioRepository.findByNombreContainingIgnoreCase("Juan"))
                .thenReturn(List.of(u1));

        List<UsuarioResponseDTO> result = usuarioService.findByNombre("Juan");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
    }

    // ===================== FIND BY CORREO =====================

    @Test
    void testFindByCorreo_Existe_DevuelveDTO() {
        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuarioEntity()));

        UsuarioResponseDTO result = usuarioService.findByCorreo("juan@test.com");

        assertNotNull(result);
        assertEquals("juan@test.com", result.getCorreo());
    }

    @Test
    void testFindByCorreo_NoExiste_Lanza404() {
        when(usuarioRepository.findByCorreoIgnoreCase("noexiste@test.com"))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.findByCorreo("noexiste@test.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ===================== UPDATE (PUT) =====================

    @Test
    void testUpdate_OK_CambiaTodasPropiedades() {
        UsuarioModel existente = usuarioEntity();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        // correo cambia de "juan@test.com" a "carlos@test.com" → no está duplicado
        when(usuarioRepository.existsByCorreo("carlos@test.com")).thenReturn(false);
        when(rolRepository.findById(2L)).thenReturn(Optional.of(rolEntity()));
        when(passwordEncoder.encode("NuevaClave123!")).thenReturn("$2a$10$newhash");
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponseDTO result = usuarioService.update(1L, updateCompletoDTO());

        assertNotNull(result);
        assertEquals("Carlos", result.getNombre());
        assertEquals("Gómez", result.getApellidos());
    }

    @Test
    void testUpdate_FaltanCampos_Lanza400() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));

        UsuarioUpdateDTO incompleto = new UsuarioUpdateDTO(); // sin campos

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.update(1L, incompleto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testUpdate_CorreoDuplicado_Lanza409() {
        UsuarioModel existente = usuarioEntity();
        // existente.correo = "juan@test.com"; updateDTO tiene "carlos@test.com"
        // → el correo cambia → se llama existsByCorreo("carlos@test.com") → true → CONFLICT

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByCorreo("carlos@test.com")).thenReturn(true);

        UsuarioUpdateDTO dto = updateCompletoDTO();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.update(1L, dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void testUpdate_PasswordCorta_Lanza400() {
        UsuarioModel existente = usuarioEntity();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        // el correo del DTO ("carlos@test.com") difiere del existente ("juan@test.com")
        // pero como la password es inválida, la excepción se lanza antes de guardar
        when(usuarioRepository.existsByCorreo("carlos@test.com")).thenReturn(false);

        UsuarioUpdateDTO dto = updateCompletoDTO();
        dto.setPassword("corta");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.update(1L, dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    // ===================== PATCH =====================

    @Test
    void testPatch_CambiaSoloNombre() {
        UsuarioModel existente = usuarioEntity();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setNombre("NuevoNombre");

        UsuarioResponseDTO result = usuarioService.patch(1L, dto);

        assertNotNull(result);
        assertEquals("NuevoNombre", result.getNombre());
        // El apellido debe permanecer igual
        assertEquals("Pérez", result.getApellidos());
    }

    @Test
    void testPatch_CorreoDuplicado_Lanza409() {
        UsuarioModel existente = usuarioEntity();
        // existente.correo = "juan@test.com"; patch llega con "otro@test.com"
        // → el correo cambia → existsByCorreo("otro@test.com") → true → CONFLICT
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByCorreo("otro@test.com")).thenReturn(true);

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setCorreo("otro@test.com");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.patch(1L, dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    // ===================== LOGIN =====================

    @Test
    void testLogin_CredencialesValidas_DevuelveUsuario() {
        UsuarioModel usuario = usuarioEntity();
        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);

        UsuarioResponseDTO result = usuarioService.login("juan@test.com", "Password123!");

        assertNotNull(result);
        assertEquals("juan@test.com", result.getCorreo());
    }

    @Test
    void testLogin_PasswordIncorrecta_Lanza401() {
        UsuarioModel usuario = usuarioEntity();
        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("WrongPass", "$2a$10$hash")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.login("juan@test.com", "WrongPass"));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    void testLogin_CorreoNulo_Lanza400() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.login(null, "Password123!"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    // ===================== OBTENER DETALLE USUARIO =====================

    @Test
    void testObtenerDetalleUsuario_DevuelveMapa() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(ventaRepository.findByUsuarioModel_Id(1L)).thenReturn(List.of());
        when(ventaRepository.countByUsuarioModel_Id(1L)).thenReturn(0L);

        Map<String, Object> result = usuarioService.obtenerDetalleUsuario(1L);

        assertNotNull(result);
        assertTrue(result.containsKey("usuario"));
        assertTrue(result.containsKey("compras"));
        assertTrue(result.containsKey("totalCompras"));
        assertEquals(0L, result.get("totalCompras"));
    }

    @Test
    void testObtenerDetalleUsuario_NoExiste_Lanza404() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.obtenerDetalleUsuario(99L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void testFindAllPaged_OK() {
        var page = new org.springframework.data.domain.PageImpl<>(List.of(usuarioEntity()));

        when(usuarioRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        var result = usuarioService.findAllPaged(1, 10);

        assertNotNull(result);
        assertEquals(1, result.getContenido().size());
        assertEquals("Juan", result.getContenido().get(0).getNombre());
    }

    @Test
    void testObtenerUsuariosConDatos_OK() {
        Object[] fila = new Object[] {
                1L,
                "Juan",
                "Pérez",
                "juan@test.com",
                123456789L
        };

        when(usuarioRepository.obtenerUsuariosResumen())
                .thenReturn(List.<Object[]>of(fila));

        var result = usuarioService.obtenerUsuariosConDatos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).get("ID"));
        assertEquals("Juan", result.get(0).get("Nombre"));
        assertEquals("Pérez", result.get(0).get("Apellidos"));
        assertEquals("juan@test.com", result.get(0).get("Correo"));
    }

    @Test
    void testObtenerFavoritosPorUsuario_OK() {
        var favorito = new com.example.NoLimits.Multimedia.model.usuario.FavoritoModel();
        favorito.setId(1L);
        favorito.setUsuario(usuarioEntity());
        favorito.setObraId("movie-1");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(favoritoRepository.findByUsuario_Id(1L)).thenReturn(List.of(favorito));

        var result = usuarioService.obtenerFavoritosPorUsuario(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("movie-1", result.get(0).getObraId());
    }

    @Test
    void testAgregarFavorito_OK() {
        var dto = new com.example.NoLimits.Multimedia.dto.usuario.request.FavoritoRequestDTO();
        dto.setObraId("movie-1");
        dto.setTitulo("Spiderman");
        dto.setTipo("movie");
        dto.setPoster("poster.jpg");
        dto.setSource("tmdb");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(favoritoRepository.existsByUsuario_IdAndObraId(1L, "movie-1")).thenReturn(false);
        when(favoritoRepository.save(any()))
                .thenAnswer(inv -> {
                    var fav = (com.example.NoLimits.Multimedia.model.usuario.FavoritoModel) inv.getArgument(0);
                    fav.setId(1L);
                    return fav;
                });

        var result = usuarioService.agregarFavorito(1L, dto);

        assertNotNull(result);
        assertEquals("movie-1", result.getObraId());
        assertEquals("Spiderman", result.getTitulo());
    }

    @Test
    void testAgregarFavorito_ObraIdVacio_Lanza400() {
        var dto = new com.example.NoLimits.Multimedia.dto.usuario.request.FavoritoRequestDTO();
        dto.setObraId(" ");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.agregarFavorito(1L, dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(favoritoRepository, never()).save(any());
    }

    @Test
    void testAgregarFavorito_Duplicado_Lanza409() {
        var dto = new com.example.NoLimits.Multimedia.dto.usuario.request.FavoritoRequestDTO();
        dto.setObraId("movie-1");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(favoritoRepository.existsByUsuario_IdAndObraId(1L, "movie-1")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.agregarFavorito(1L, dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void testEliminarFavorito_OK() {
        var favorito = new com.example.NoLimits.Multimedia.model.usuario.FavoritoModel();
        favorito.setId(1L);
        favorito.setObraId("movie-1");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(favoritoRepository.findByUsuario_IdAndObraId(1L, "movie-1"))
                .thenReturn(Optional.of(favorito));

        usuarioService.eliminarFavorito(1L, "movie-1");

        verify(favoritoRepository).delete(favorito);
    }

    @Test
    void testEliminarFavorito_NoExiste_Lanza404() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
        when(favoritoRepository.findByUsuario_IdAndObraId(1L, "movie-1"))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.eliminarFavorito(1L, "movie-1"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void testCambiarPassword_OK() {
        UsuarioModel usuario = usuarioEntity();

        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);
        when(passwordEncoder.encode("NuevaPassword123!")).thenReturn("$2a$10$newhash");

        usuarioService.cambiarPassword("juan@test.com", "Password123!", "NuevaPassword123!");

        verify(usuarioRepository).save(usuario);
        assertEquals("$2a$10$newhash", usuario.getPassword());
    }

    @Test
    void testCambiarPassword_ActualIncorrecta_Lanza400() {
        UsuarioModel usuario = usuarioEntity();

        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("MalaClave", "$2a$10$hash")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.cambiarPassword("juan@test.com", "MalaClave", "NuevaPassword123!"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testCambiarPassword_NuevaPasswordCorta_Lanza400() {
        UsuarioModel usuario = usuarioEntity();

        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.cambiarPassword("juan@test.com", "Password123!", "corta"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testEliminarCuenta_OK() {
        UsuarioModel usuario = usuarioEntity();

        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(reviewRepository.findByUsuario_Id(1L)).thenReturn(List.of());

        usuarioService.eliminarCuenta("juan@test.com");

        verify(reviewReactionRepository).deleteByUsuario_Id(1L);
        verify(favoritoRepository).deleteByUsuario_Id(1L);
        verify(reviewRepository).deleteAll(List.of());
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void testCambiarCorreo_OK() {
        UsuarioModel usuario = usuarioEntity();

        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);
        when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(false);

        usuarioService.cambiarCorreo("juan@test.com", "NUEVO@TEST.COM", "Password123!");

        assertEquals("nuevo@test.com", usuario.getCorreo());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testCambiarCorreo_PasswordIncorrecta_Lanza400() {
        UsuarioModel usuario = usuarioEntity();

        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("mala", "$2a$10$hash")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.cambiarCorreo("juan@test.com", "nuevo@test.com", "mala"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testCambiarCorreo_Duplicado_Lanza409() {
        UsuarioModel usuario = usuarioEntity();

        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);
        when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.cambiarCorreo("juan@test.com", "nuevo@test.com", "Password123!"));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }
}