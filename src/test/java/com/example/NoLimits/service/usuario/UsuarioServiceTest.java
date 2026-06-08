package com.example.NoLimits.service.usuario;

import com.example.NoLimits.Multimedia.dto.usuario.request.FavoritoRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRegistroDTO;
import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.UsuarioUpdateDTO;
import com.example.NoLimits.Multimedia.model.usuario.FavoritoModel;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.review.ReviewReactionRepository;
import com.example.NoLimits.Multimedia.repository.review.ReviewRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.usuario.FavoritoRepository;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.service.usuario.UsuarioService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UsuarioServiceTest")
class UsuarioServiceTest extends AbstractContainerBaseTest {

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

    private RolModel rolEntity() {
        RolModel rol = new RolModel();
        rol.setId(2L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);
        return rol;
    }

    private UsuarioModel usuarioEntity() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("juan@test.com");
        usuario.setTelefono(123456789L);
        usuario.setPassword("$2a$10$hash");
        usuario.setRol(rolEntity());
        return usuario;
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

    private ComunaModel comunaEntity() {
        ComunaModel comuna = new ComunaModel();
        comuna.setId(1L);
        comuna.setNombre("Santiago");
        return comuna;
    }

    private RegionModel regionEntity() {
        RegionModel region = new RegionModel();
        region.setId(13L);
        region.setNombre("Región Metropolitana");
        return region;
    }

    private DireccionRequestDTO direccionRequestDTO() {
        DireccionRequestDTO dto = new DireccionRequestDTO();
        dto.setComunaId(1L);
        dto.setCalle("Av. Siempre Viva");
        dto.setNumero("742");
        dto.setComplemento("Depto 10");
        dto.setCodigoPostal("8320000");
        dto.setActivo(true);
        return dto;
    }

    @Nested
    @DisplayName("describe findAll()")
    class DescribeFindAll {

        @Test
        @DisplayName("test: debe devolver lista de usuarios")
        void testFindAll_devuelveLista() {
            when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity()));

            List<UsuarioResponseDTO> result = usuarioService.findAll();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Juan", result.get(0).getNombre());
            assertEquals("Pérez", result.get(0).getApellidos());
            assertEquals("Juan Pérez", result.get(0).getNombreCompleto());
            assertEquals("juan@test.com", result.get(0).getCorreo());

            verify(usuarioRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("it: debe devolver lista vacía si no existen usuarios")
        void itFindAll_listaVacia() {
            when(usuarioRepository.findAll()).thenReturn(List.of());

            List<UsuarioResponseDTO> result = usuarioService.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(usuarioRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("describe findById()")
    class DescribeFindById {

        @Test
        @DisplayName("test: debe devolver usuario cuando existe")
        void testFindById_existe() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));

            UsuarioResponseDTO result = usuarioService.findById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Juan", result.getNombre());
            assertEquals("juan@test.com", result.getCorreo());

            verify(usuarioRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("it: debe lanzar 404 cuando el usuario no existe")
        void itFindById_noExiste() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.findById(99L)
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            assertTrue(ex.getReason().contains("Usuario no encontrado con ID: 99"));

            verify(usuarioRepository, times(1)).findById(99L);
        }

        @Test
        @DisplayName("it: debe mapear dirección, comuna y región en el response")
        void itFindById_mapeaDireccionComunaRegion() {
            RegionModel region = regionEntity();

            ComunaModel comuna = comunaEntity();
            comuna.setRegion(region);

            DireccionModel direccion = new DireccionModel();
            direccion.setId(10L);
            direccion.setComuna(comuna);

            UsuarioModel usuario = usuarioEntity();
            usuario.setDireccion(direccion);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            UsuarioResponseDTO result = usuarioService.findById(1L);

            assertNotNull(result);
            assertEquals(10L, result.getDireccionId());
            assertEquals(1L, result.getComunaId());
            assertEquals("Santiago", result.getComunaNombre());
            assertEquals(13L, result.getRegionId());
            assertEquals("Región Metropolitana", result.getRegionNombre());
        }
    }

    @Nested
    @DisplayName("describe save()")
    class DescribeSave {

        @Test
        @DisplayName("test: debe registrar un usuario correctamente")
        void testSave_ok() {
            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findByNombreIgnoreCase("ROLE_USER")).thenReturn(Optional.of(rolEntity()));
            when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hash");
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> {
                        UsuarioModel usuario = inv.getArgument(0);
                        usuario.setId(1L);
                        return usuario;
                    });

            UsuarioResponseDTO result = usuarioService.save(registroDTO());

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Juan", result.getNombre());
            assertEquals("juan@test.com", result.getCorreo());

            verify(usuarioRepository, times(1)).existsByCorreo("juan@test.com");
            verify(rolRepository, times(1)).findByNombreIgnoreCase("ROLE_USER");
            verify(passwordEncoder, times(1)).encode("Password123!");
            verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
        }

        @Test
        @DisplayName("it: debe normalizar correo en minúsculas")
        void itSave_normalizaCorreo() {
            UsuarioRegistroDTO dto = registroDTO();
            dto.setCorreo("  JUAN@TEST.COM  ");

            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findByNombreIgnoreCase("ROLE_USER")).thenReturn(Optional.of(rolEntity()));
            when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hash");
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> {
                        UsuarioModel usuario = inv.getArgument(0);
                        usuario.setId(1L);
                        return usuario;
                    });

            UsuarioResponseDTO result = usuarioService.save(dto);

            assertNotNull(result);
            assertEquals("juan@test.com", result.getCorreo());

            verify(usuarioRepository, times(1)).existsByCorreo("juan@test.com");
        }

        @Test
        @DisplayName("it: debe lanzar 400 si password está vacía")
        void itSave_passwordVacia() {
            UsuarioRegistroDTO dto = registroDTO();
            dto.setPassword(" ");

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.save(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertTrue(ex.getReason().contains("La contraseña es obligatoria"));

            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si password es corta")
        void itSave_passwordCorta() {
            UsuarioRegistroDTO dto = registroDTO();
            dto.setPassword("corta");

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.save(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertTrue(ex.getReason().contains("La contraseña debe tener entre 8 y 255 caracteres"));

            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si password supera 255 caracteres")
        void itSave_passwordMuyLarga() {
            UsuarioRegistroDTO dto = registroDTO();
            dto.setPassword("A".repeat(256));

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.save(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si correo está vacío")
        void itSave_correoVacio() {
            UsuarioRegistroDTO dto = registroDTO();
            dto.setCorreo(" ");

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.save(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertTrue(ex.getReason().contains("El correo es obligatorio"));

            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 409 si correo está duplicado")
        void itSave_correoDuplicado() {
            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(true);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.save(registroDTO())
            );

            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            assertTrue(ex.getReason().contains("Correo ya registrado"));

            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 500 si no existe ROLE_USER")
        void itSave_rolDefaultNoExiste() {
            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findByNombreIgnoreCase("ROLE_USER")).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.save(registroDTO())
            );

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
            assertTrue(ex.getReason().contains("Rol por defecto ROLE_USER no existe"));

            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si dirección viene sin comunaId")
        void itSave_direccionSinComunaId() {
            UsuarioRegistroDTO dto = registroDTO();
            dto.setDireccion(new DireccionRequestDTO());

            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findByNombreIgnoreCase("ROLE_USER")).thenReturn(Optional.of(rolEntity()));
            when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hash");
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> {
                        UsuarioModel usuario = inv.getArgument(0);
                        usuario.setId(1L);
                        return usuario;
                    });

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.save(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(direccionRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe crear usuario con dirección")
        void itSave_conDireccion() {
            UsuarioRegistroDTO dto = registroDTO();
            dto.setDireccion(direccionRequestDTO());

            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findByNombreIgnoreCase("ROLE_USER")).thenReturn(Optional.of(rolEntity()));
            when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hash");
            when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaEntity()));
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> {
                        UsuarioModel usuario = inv.getArgument(0);
                        usuario.setId(1L);
                        return usuario;
                    });
            when(direccionRepository.save(any(DireccionModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioResponseDTO result = usuarioService.save(dto);

            assertNotNull(result);
            assertEquals("juan@test.com", result.getCorreo());

            verify(direccionRepository, times(1)).save(any(DireccionModel.class));
            verify(usuarioRepository, times(2)).save(any(UsuarioModel.class));
        }
    }

    @Nested
    @DisplayName("describe saveDesdeAdmin()")
    class DescribeSaveDesdeAdmin {

        @Test
        @DisplayName("test: debe crear usuario desde admin correctamente")
        void testSaveDesdeAdmin_ok() {
            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findById(2L)).thenReturn(Optional.of(rolEntity()));
            when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hash");
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> {
                        UsuarioModel usuario = inv.getArgument(0);
                        usuario.setId(1L);
                        return usuario;
                    });

            UsuarioResponseDTO result = usuarioService.saveDesdeAdmin(requestDTO());

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("juan@test.com", result.getCorreo());

            verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
        }

        @Test
        @DisplayName("it: debe lanzar 400 si password es null")
        void itSaveDesdeAdmin_passwordNull() {
            UsuarioRequestDTO dto = requestDTO();
            dto.setPassword(null);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.saveDesdeAdmin(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si password es corta")
        void itSaveDesdeAdmin_passwordCorta() {
            UsuarioRequestDTO dto = requestDTO();
            dto.setPassword("corta");

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.saveDesdeAdmin(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si correo está vacío")
        void itSaveDesdeAdmin_correoVacio() {
            UsuarioRequestDTO dto = requestDTO();
            dto.setCorreo(" ");

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.saveDesdeAdmin(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 409 si correo está duplicado")
        void itSaveDesdeAdmin_correoDuplicado() {
            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(true);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.saveDesdeAdmin(requestDTO())
            );

            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si rolId es null")
        void itSaveDesdeAdmin_rolIdNull() {
            UsuarioRequestDTO dto = requestDTO();
            dto.setRolId(null);

            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.saveDesdeAdmin(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 404 si rol no existe")
        void itSaveDesdeAdmin_rolNoEncontrado() {
            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findById(2L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.saveDesdeAdmin(requestDTO())
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si dirección viene sin comunaId")
        void itSaveDesdeAdmin_direccionSinComunaId() {
            UsuarioRequestDTO dto = requestDTO();
            dto.setDireccion(new DireccionRequestDTO());

            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findById(2L)).thenReturn(Optional.of(rolEntity()));
            when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hash");
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> {
                        UsuarioModel usuario = inv.getArgument(0);
                        usuario.setId(1L);
                        return usuario;
                    });

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.saveDesdeAdmin(dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(direccionRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 404 si comuna de dirección no existe")
        void itSaveDesdeAdmin_comunaNoExiste() {
            UsuarioRequestDTO dto = requestDTO();
            dto.setDireccion(direccionRequestDTO());

            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findById(2L)).thenReturn(Optional.of(rolEntity()));
            when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hash");
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> {
                        UsuarioModel usuario = inv.getArgument(0);
                        usuario.setId(1L);
                        return usuario;
                    });
            when(comunaRepository.findById(1L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.saveDesdeAdmin(dto)
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(direccionRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe crear usuario con dirección desde admin")
        void itSaveDesdeAdmin_conDireccion() {
            UsuarioRequestDTO dto = requestDTO();
            dto.setDireccion(direccionRequestDTO());

            when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(false);
            when(rolRepository.findById(2L)).thenReturn(Optional.of(rolEntity()));
            when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hash");
            when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaEntity()));
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> {
                        UsuarioModel usuario = inv.getArgument(0);
                        usuario.setId(1L);
                        return usuario;
                    });
            when(direccionRepository.save(any(DireccionModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioResponseDTO result = usuarioService.saveDesdeAdmin(dto);

            assertNotNull(result);
            assertEquals("juan@test.com", result.getCorreo());

            verify(direccionRepository, times(1)).save(any(DireccionModel.class));
            verify(usuarioRepository, times(2)).save(any(UsuarioModel.class));
        }
    }

    @Nested
    @DisplayName("describe deleteById()")
    class DescribeDeleteById {

        @Test
        @DisplayName("test: debe eliminar usuario si existe y no tiene ventas")
        void testDeleteById_ok() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(ventaRepository.findByUsuarioModel_Id(1L)).thenReturn(List.of());

            usuarioService.deleteById(1L);

            verify(usuarioRepository, times(1)).findById(1L);
            verify(ventaRepository, times(1)).findByUsuarioModel_Id(1L);
            verify(usuarioRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("it: debe lanzar 404 si usuario no existe")
        void itDeleteById_noExiste() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.deleteById(99L)
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(usuarioRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("it: debe lanzar 409 si usuario tiene ventas")
        void itDeleteById_conVentas() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(ventaRepository.findByUsuarioModel_Id(1L)).thenReturn(List.of(new VentaModel()));

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.deleteById(1L)
            );

            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            verify(usuarioRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("describe findByNombre()")
    class DescribeFindByNombre {

        @Test
        @DisplayName("test: debe lanzar 400 si nombre es null")
        void testFindByNombre_null() {
            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.findByNombre(null)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        }

        @Test
        @DisplayName("it: debe devolver todos si nombre viene vacío")
        void itFindByNombre_vacio() {
            when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity()));

            List<UsuarioResponseDTO> result = usuarioService.findByNombre("");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Juan", result.get(0).getNombre());

            verify(usuarioRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("it: debe devolver coincidencias por nombre")
        void itFindByNombre_filtrado() {
            when(usuarioRepository.findByNombreContainingIgnoreCase("Juan"))
                    .thenReturn(List.of(usuarioEntity()));

            List<UsuarioResponseDTO> result = usuarioService.findByNombre(" Juan ");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Juan", result.get(0).getNombre());

            verify(usuarioRepository, times(1)).findByNombreContainingIgnoreCase("Juan");
        }
    }

    @Nested
    @DisplayName("describe findByCorreo()")
    class DescribeFindByCorreo {

        @Test
        @DisplayName("test: debe devolver usuario por correo")
        void testFindByCorreo_existe() {
            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuarioEntity()));

            UsuarioResponseDTO result = usuarioService.findByCorreo("juan@test.com");

            assertNotNull(result);
            assertEquals("juan@test.com", result.getCorreo());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si correo es null")
        void itFindByCorreo_null() {
            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.findByCorreo(null)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        }

        @Test
        @DisplayName("it: debe lanzar 404 si correo no existe")
        void itFindByCorreo_noExiste() {
            when(usuarioRepository.findByCorreoIgnoreCase("noexiste@test.com"))
                    .thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.findByCorreo("noexiste@test.com")
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }
    }

    @Nested
    @DisplayName("describe update()")
    class DescribeUpdate {

        @Test
        @DisplayName("test: debe actualizar todas las propiedades")
        void testUpdate_ok() {
            UsuarioModel existente = usuarioEntity();

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(usuarioRepository.existsByCorreo("carlos@test.com")).thenReturn(false);
            when(rolRepository.findById(2L)).thenReturn(Optional.of(rolEntity()));
            when(passwordEncoder.encode("NuevaClave123!")).thenReturn("$2a$10$newhash");
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioResponseDTO result = usuarioService.update(1L, updateCompletoDTO());

            assertNotNull(result);
            assertEquals("Carlos", result.getNombre());
            assertEquals("Gómez", result.getApellidos());
            assertEquals("carlos@test.com", result.getCorreo());
            assertEquals(987654321L, result.getTelefono());

            verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
        }

        @Test
        @DisplayName("it: debe lanzar 404 si usuario no existe")
        void itUpdate_usuarioNoExiste() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.update(99L, updateCompletoDTO())
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si faltan campos")
        void itUpdate_faltanCampos() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.update(1L, new UsuarioUpdateDTO())
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 409 si correo está duplicado")
        void itUpdate_correoDuplicado() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(usuarioRepository.existsByCorreo("carlos@test.com")).thenReturn(true);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.update(1L, updateCompletoDTO())
            );

            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si password es corta")
        void itUpdate_passwordCorta() {
            UsuarioUpdateDTO dto = updateCompletoDTO();
            dto.setPassword("corta");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(usuarioRepository.existsByCorreo("carlos@test.com")).thenReturn(false);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.update(1L, dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 404 si rol no existe")
        void itUpdate_rolNoExiste() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(usuarioRepository.existsByCorreo("carlos@test.com")).thenReturn(false);
            when(rolRepository.findById(2L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.update(1L, updateCompletoDTO())
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("describe patch()")
    class DescribePatch {

        @Test
        @DisplayName("test: debe cambiar solo nombre")
        void testPatch_cambiaSoloNombre() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setNombre(" NuevoNombre ");

            UsuarioResponseDTO result = usuarioService.patch(1L, dto);

            assertNotNull(result);
            assertEquals("NuevoNombre", result.getNombre());
            assertEquals("Pérez", result.getApellidos());

            verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
        }

        @Test
        @DisplayName("it: debe cambiar apellidos, teléfono y foto")
        void itPatch_cambiaCamposSimples() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setApellidos(" González ");
            dto.setTelefono(999888777L);
            dto.setFotoPerfil(" foto.png ");

            UsuarioResponseDTO result = usuarioService.patch(1L, dto);

            assertNotNull(result);
            assertEquals("González", result.getApellidos());
            assertEquals(999888777L, result.getTelefono());
            assertEquals("foto.png", result.getFotoPerfil());
        }

        @Test
        @DisplayName("it: debe cambiar correo si no está duplicado")
        void itPatch_cambiaCorreo() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(false);
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setCorreo(" NUEVO@TEST.COM ");

            UsuarioResponseDTO result = usuarioService.patch(1L, dto);

            assertNotNull(result);
            assertEquals("nuevo@test.com", result.getCorreo());

            verify(usuarioRepository, times(1)).existsByCorreo("nuevo@test.com");
        }

        @Test
        @DisplayName("it: debe lanzar 409 si correo está duplicado")
        void itPatch_correoDuplicado() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(usuarioRepository.existsByCorreo("otro@test.com")).thenReturn(true);

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setCorreo("otro@test.com");

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.patch(1L, dto)
            );

            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe cambiar password correctamente")
        void itPatch_cambiaPassword() {
            UsuarioModel usuario = usuarioEntity();

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(passwordEncoder.encode("NuevaPassword123!")).thenReturn("$2a$10$nueva");
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setPassword("NuevaPassword123!");

            UsuarioResponseDTO result = usuarioService.patch(1L, dto);

            assertNotNull(result);
            assertEquals("$2a$10$nueva", usuario.getPassword());

            verify(passwordEncoder, times(1)).encode("NuevaPassword123!");
            verify(usuarioRepository, times(1)).save(usuario);
        }

        @Test
        @DisplayName("it: debe lanzar 400 si password es corta")
        void itPatch_passwordCorta() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setPassword("corta");

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.patch(1L, dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe cambiar rol")
        void itPatch_cambiaRol() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(rolRepository.findById(2L)).thenReturn(Optional.of(rolEntity()));
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setRolId(2L);

            UsuarioResponseDTO result = usuarioService.patch(1L, dto);

            assertNotNull(result);
            assertEquals(2L, result.getRolId());
            assertEquals("ROLE_USER", result.getRolNombre());
        }

        @Test
        @DisplayName("it: debe lanzar 404 si rol no existe")
        void itPatch_rolNoExiste() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(rolRepository.findById(99L)).thenReturn(Optional.empty());

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setRolId(99L);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.patch(1L, dto)
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe guardar sin cambios si DTO viene vacío")
        void itPatch_dtoVacio() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioResponseDTO result = usuarioService.patch(1L, new UsuarioUpdateDTO());

            assertNotNull(result);
            assertEquals("Juan", result.getNombre());
            assertEquals("Pérez", result.getApellidos());

            verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
        }

        @Test
        @DisplayName("it: debe actualizar dirección completa")
        void itPatch_actualizaDireccionCompleta() {
            UsuarioModel usuario = usuarioEntity();

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setDireccion(direccionRequestDTO());

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaEntity()));
            when(direccionRepository.save(any(DireccionModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioResponseDTO result = usuarioService.patch(1L, dto);

            assertNotNull(result);
            assertNotNull(usuario.getDireccion());
            assertEquals("Av. Siempre Viva", usuario.getDireccion().getCalle());
            assertEquals("742", usuario.getDireccion().getNumero());
            assertEquals("Depto 10", usuario.getDireccion().getComplemento());
            assertEquals("8320000", usuario.getDireccion().getCodigoPostal());
            assertTrue(usuario.getDireccion().getActivo());

            verify(direccionRepository, times(1)).save(any(DireccionModel.class));
            verify(usuarioRepository, times(1)).save(usuario);
        }

        @Test
        @DisplayName("it: debe lanzar 404 si comuna de dirección no existe en patch")
        void itPatch_direccionComunaNoExiste() {
            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setDireccion(direccionRequestDTO());

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(comunaRepository.findById(1L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.patch(1L, dto)
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(direccionRepository, never()).save(any());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: no debe tocar dirección si viene vacía")
        void itPatch_direccionVacia_noTocaDireccion() {
            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setDireccion(new DireccionRequestDTO());

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            UsuarioResponseDTO result = usuarioService.patch(1L, dto);

            assertNotNull(result);

            verify(direccionRepository, never()).save(any());
            verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
        }
    }

    @Nested
    @DisplayName("describe login()")
    class DescribeLogin {

        @Test
        @DisplayName("test: debe devolver usuario con credenciales válidas")
        void testLogin_ok() {
            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuarioEntity()));
            when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);

            UsuarioResponseDTO result = usuarioService.login("juan@test.com", "Password123!");

            assertNotNull(result);
            assertEquals("juan@test.com", result.getCorreo());
        }

        @Test
        @DisplayName("it: debe lanzar 401 si password es incorrecta")
        void itLogin_passwordIncorrecta() {
            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuarioEntity()));
            when(passwordEncoder.matches("WrongPass", "$2a$10$hash")).thenReturn(false);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.login("juan@test.com", "WrongPass")
            );

            assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si correo es null")
        void itLogin_correoNull() {
            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.login(null, "Password123!")
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        }

        @Test
        @DisplayName("it: debe lanzar 404 si correo no existe")
        void itLogin_correoNoExiste() {
            when(usuarioRepository.findByCorreoIgnoreCase("no@test.com"))
                    .thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.login("no@test.com", "Password123!")
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }
    }

    @Nested
    @DisplayName("describe obtenerDetalleUsuario()")
    class DescribeObtenerDetalleUsuario {

        @Test
        @DisplayName("test: debe devolver mapa con usuario, compras y totalCompras")
        void testObtenerDetalleUsuario_ok() {
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
        @DisplayName("it: debe lanzar 404 si usuario no existe")
        void itObtenerDetalleUsuario_noExiste() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.obtenerDetalleUsuario(99L)
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }
    }

    @Nested
    @DisplayName("describe findAllPaged()")
    class DescribeFindAllPaged {

        @Test
        @DisplayName("test: debe devolver usuarios paginados")
        void testFindAllPaged_ok() {
            var page = new PageImpl<>(List.of(usuarioEntity()));

            when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(page);

            var result = usuarioService.findAllPaged(1, 10);

            assertNotNull(result);
            assertEquals(1, result.getContenido().size());
            assertEquals("Juan", result.getContenido().get(0).getNombre());

            verify(usuarioRepository, times(1)).findAll(any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("describe obtenerUsuariosConDatos()")
    class DescribeObtenerUsuariosConDatos {

        @Test
        @DisplayName("test: debe devolver resumen de usuarios")
        void testObtenerUsuariosConDatos_ok() {
            Object[] fila = new Object[] {
                    1L,
                    "Juan",
                    "Pérez",
                    "juan@test.com",
                    123456789L
            };

            when(usuarioRepository.obtenerUsuariosResumen())
                    .thenReturn(List.<Object[]>of(fila));

            List<Map<String, Object>> result = usuarioService.obtenerUsuariosConDatos();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).get("ID"));
            assertEquals("Juan", result.get(0).get("Nombre"));
            assertEquals("Pérez", result.get(0).get("Apellidos"));
            assertEquals("juan@test.com", result.get(0).get("Correo"));
            assertEquals(123456789L, result.get(0).get("Teléfono"));
        }
    }

    @Nested
    @DisplayName("describe favoritos")
    class DescribeFavoritos {

        @Test
        @DisplayName("test: debe obtener favoritos por usuario")
        void testObtenerFavoritosPorUsuario_ok() {
            FavoritoModel favorito = new FavoritoModel();
            favorito.setId(1L);
            favorito.setUsuario(usuarioEntity());
            favorito.setObraId("movie-1");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(favoritoRepository.findByUsuario_Id(1L)).thenReturn(List.of(favorito));

            List<FavoritoModel> result = usuarioService.obtenerFavoritosPorUsuario(1L);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("movie-1", result.get(0).getObraId());
        }

        @Test
        @DisplayName("it: debe agregar favorito correctamente")
        void itAgregarFavorito_ok() {
            FavoritoRequestDTO dto = new FavoritoRequestDTO();
            dto.setObraId("movie-1");
            dto.setTitulo("Spiderman");
            dto.setTipo("movie");
            dto.setPoster("poster.jpg");
            dto.setSource("tmdb");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(favoritoRepository.existsByUsuario_IdAndObraId(1L, "movie-1")).thenReturn(false);
            when(favoritoRepository.save(any(FavoritoModel.class)))
                    .thenAnswer(inv -> {
                        FavoritoModel favorito = inv.getArgument(0);
                        favorito.setId(1L);
                        return favorito;
                    });

            FavoritoModel result = usuarioService.agregarFavorito(1L, dto);

            assertNotNull(result);
            assertEquals("movie-1", result.getObraId());
            assertEquals("Spiderman", result.getTitulo());

            verify(favoritoRepository, times(1)).save(any(FavoritoModel.class));
        }

        @Test
        @DisplayName("it: debe lanzar 400 si obraId está vacío")
        void itAgregarFavorito_obraIdVacio() {
            FavoritoRequestDTO dto = new FavoritoRequestDTO();
            dto.setObraId(" ");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.agregarFavorito(1L, dto)
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(favoritoRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 409 si favorito ya existe")
        void itAgregarFavorito_duplicado() {
            FavoritoRequestDTO dto = new FavoritoRequestDTO();
            dto.setObraId("movie-1");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(favoritoRepository.existsByUsuario_IdAndObraId(1L, "movie-1")).thenReturn(true);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.agregarFavorito(1L, dto)
            );

            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        }

        @Test
        @DisplayName("it: debe eliminar favorito correctamente")
        void itEliminarFavorito_ok() {
            FavoritoModel favorito = new FavoritoModel();
            favorito.setId(1L);
            favorito.setObraId("movie-1");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(favoritoRepository.findByUsuario_IdAndObraId(1L, "movie-1"))
                    .thenReturn(Optional.of(favorito));

            usuarioService.eliminarFavorito(1L, "movie-1");

            verify(favoritoRepository, times(1)).delete(favorito);
        }

        @Test
        @DisplayName("it: debe lanzar 404 si favorito no existe")
        void itEliminarFavorito_noExiste() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity()));
            when(favoritoRepository.findByUsuario_IdAndObraId(1L, "movie-1"))
                    .thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.eliminarFavorito(1L, "movie-1")
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }
    }

    @Nested
    @DisplayName("describe cambiarPassword()")
    class DescribeCambiarPassword {

        @Test
        @DisplayName("test: debe cambiar password correctamente")
        void testCambiarPassword_ok() {
            UsuarioModel usuario = usuarioEntity();

            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);
            when(passwordEncoder.encode("NuevaPassword123!")).thenReturn("$2a$10$newhash");

            usuarioService.cambiarPassword("juan@test.com", "Password123!", "NuevaPassword123!");

            assertEquals("$2a$10$newhash", usuario.getPassword());
            verify(usuarioRepository, times(1)).save(usuario);
        }

        @Test
        @DisplayName("it: debe lanzar 404 si usuario no existe")
        void itCambiarPassword_usuarioNoExiste() {
            when(usuarioRepository.findByCorreoIgnoreCase("no@test.com"))
                    .thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.cambiarPassword("no@test.com", "Password123!", "NuevaPassword123!")
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si password actual es incorrecta")
        void itCambiarPassword_actualIncorrecta() {
            UsuarioModel usuario = usuarioEntity();

            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("MalaClave", "$2a$10$hash")).thenReturn(false);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.cambiarPassword("juan@test.com", "MalaClave", "NuevaPassword123!")
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si nueva password es corta")
        void itCambiarPassword_nuevaPasswordCorta() {
            UsuarioModel usuario = usuarioEntity();

            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.cambiarPassword("juan@test.com", "Password123!", "corta")
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        }
    }

    @Nested
    @DisplayName("describe eliminarCuenta()")
    class DescribeEliminarCuenta {

        @Test
        @DisplayName("test: debe eliminar cuenta correctamente")
        void testEliminarCuenta_ok() {
            UsuarioModel usuario = usuarioEntity();

            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuario));
            when(reviewRepository.findByUsuario_Id(1L)).thenReturn(List.of());

            usuarioService.eliminarCuenta("juan@test.com");

            verify(reviewReactionRepository, times(1)).deleteByUsuario_Id(1L);
            verify(favoritoRepository, times(1)).deleteByUsuario_Id(1L);
            verify(reviewRepository, times(1)).deleteAll(List.of());
            verify(usuarioRepository, times(1)).delete(usuario);
        }

        @Test
        @DisplayName("it: debe lanzar 404 si usuario no existe")
        void itEliminarCuenta_usuarioNoExiste() {
            when(usuarioRepository.findByCorreoIgnoreCase("no@test.com"))
                    .thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.eliminarCuenta("no@test.com")
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(usuarioRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("describe cambiarCorreo()")
    class DescribeCambiarCorreo {

        @Test
        @DisplayName("test: debe cambiar correo correctamente")
        void testCambiarCorreo_ok() {
            UsuarioModel usuario = usuarioEntity();

            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);
            when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(false);

            usuarioService.cambiarCorreo("juan@test.com", "NUEVO@TEST.COM", "Password123!");

            assertEquals("nuevo@test.com", usuario.getCorreo());
            verify(usuarioRepository, times(1)).save(usuario);
        }

        @Test
        @DisplayName("it: debe lanzar 404 si usuario no existe")
        void itCambiarCorreo_usuarioNoExiste() {
            when(usuarioRepository.findByCorreoIgnoreCase("no@test.com"))
                    .thenReturn(Optional.empty());

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.cambiarCorreo("no@test.com", "nuevo@test.com", "Password123!")
            );

            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("it: debe lanzar 400 si password actual es incorrecta")
        void itCambiarCorreo_passwordIncorrecta() {
            UsuarioModel usuario = usuarioEntity();

            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("mala", "$2a$10$hash")).thenReturn(false);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.cambiarCorreo("juan@test.com", "nuevo@test.com", "mala")
            );

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        }

        @Test
        @DisplayName("it: debe lanzar 409 si nuevo correo ya existe")
        void itCambiarCorreo_duplicado() {
            UsuarioModel usuario = usuarioEntity();

            when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                    .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("Password123!", "$2a$10$hash")).thenReturn(true);
            when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(true);

            ResponseStatusException ex = assertThrows(
                    ResponseStatusException.class,
                    () -> usuarioService.cambiarCorreo("juan@test.com", "nuevo@test.com", "Password123!")
            );

            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            verify(usuarioRepository, never()).save(any());
        }
    }
}