package com.example.NoLimits.config;

import com.example.NoLimits.Multimedia.config.AdminInitializer;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("AdminInitializer")
class AdminInitializerTest {

    private AdminInitializer adminInitializer;
    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        rolRepository = mock(RolRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        adminInitializer = new AdminInitializer();

        ReflectionTestUtils.setField(adminInitializer, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(adminInitializer, "rolRepository", rolRepository);
        ReflectionTestUtils.setField(adminInitializer, "passwordEncoder", passwordEncoder);
    }

    @Nested
    @DisplayName("describe: credenciales vacías o nulas")
    class CredencialesVacias {

        @Test
        @DisplayName("it: debería retornar sin tocar repositorios cuando correo está vacío")
        void deberiaRetornarSinTocarRepositoriosCuandoCorreoEstaVacio() throws Exception {
            ReflectionTestUtils.setField(adminInitializer, "correo", "  ");
            ReflectionTestUtils.setField(adminInitializer, "password", "admin123");

            adminInitializer.run();

            verifyNoInteractions(usuarioRepository);
            verifyNoInteractions(rolRepository);
        }

        @Test
        @DisplayName("it: debería retornar sin tocar repositorios cuando password está vacío")
        void deberiaRetornarSinTocarRepositoriosCuandoPasswordEstaVacio() throws Exception {
            ReflectionTestUtils.setField(adminInitializer, "correo", "admin@test.com");
            ReflectionTestUtils.setField(adminInitializer, "password", "");

            adminInitializer.run();

            verifyNoInteractions(usuarioRepository);
            verifyNoInteractions(rolRepository);
        }

        @Test
        @DisplayName("it: debería retornar sin tocar repositorios cuando correo es null")
        void deberiaRetornarSinTocarRepositoriosCuandoCorreoEsNull() throws Exception {

            ReflectionTestUtils.setField(adminInitializer, "correo", null);
            ReflectionTestUtils.setField(adminInitializer, "password", "admin123");

            adminInitializer.run();

            verifyNoInteractions(usuarioRepository);
            verifyNoInteractions(rolRepository);
        }

        @Test
        @DisplayName("it: debería retornar sin tocar repositorios cuando password es null")
        void deberiaRetornarSinTocarRepositoriosCuandoPasswordEsNull() throws Exception {

            ReflectionTestUtils.setField(adminInitializer, "correo", "admin@test.com");
            ReflectionTestUtils.setField(adminInitializer, "password", null);

            adminInitializer.run();

            verifyNoInteractions(usuarioRepository);
            verifyNoInteractions(rolRepository);
        }
    }

    @Nested
    @DisplayName("describe: rol admin")
    class RolAdmin {

        @Test
        @DisplayName("it: no debería crear admin cuando no existe ROLE_ADMIN ni ADMIN")
        void noDeberiaCrearAdminCuandoNoExisteRoleAdminNiAdmin() throws Exception {
            ReflectionTestUtils.setField(adminInitializer, "correo", "admin@test.com");
            ReflectionTestUtils.setField(adminInitializer, "password", "admin123");

            when(rolRepository.findByNombreIgnoreCase("ROLE_ADMIN"))
                    .thenReturn(Optional.empty());
            when(rolRepository.findByNombreIgnoreCase("ADMIN"))
                    .thenReturn(Optional.empty());

            adminInitializer.run();

            verify(rolRepository).findByNombreIgnoreCase("ROLE_ADMIN");
            verify(rolRepository).findByNombreIgnoreCase("ADMIN");
            verify(usuarioRepository, never()).save(any(UsuarioModel.class));
        }

        @Test
        @DisplayName("it: debería usar ADMIN como respaldo cuando no existe ROLE_ADMIN")
        void deberiaUsarAdminComoRespaldoCuandoNoExisteRoleAdmin() throws Exception {
            ReflectionTestUtils.setField(adminInitializer, "correo", "admin@test.com");
            ReflectionTestUtils.setField(adminInitializer, "password", "admin123");

            RolModel adminRol = new RolModel();
            adminRol.setNombre("ADMIN");

            when(rolRepository.findByNombreIgnoreCase("ROLE_ADMIN"))
                    .thenReturn(Optional.empty());
            when(rolRepository.findByNombreIgnoreCase("ADMIN"))
                    .thenReturn(Optional.of(adminRol));
            when(usuarioRepository.findByCorreoIgnoreCase("admin@test.com"))
                    .thenReturn(Optional.empty());
            when(passwordEncoder.encode("admin123"))
                    .thenReturn("password-encriptada");

            adminInitializer.run();

            ArgumentCaptor<UsuarioModel> usuarioCaptor = ArgumentCaptor.forClass(UsuarioModel.class);
            verify(usuarioRepository).save(usuarioCaptor.capture());

            UsuarioModel adminGuardado = usuarioCaptor.getValue();

            assertEquals("admin@test.com", adminGuardado.getCorreo());
            assertEquals(adminRol, adminGuardado.getRol());
        }

        @Test
        @DisplayName("it: no debería consultar ADMIN cuando ROLE_ADMIN existe")
        void noDeberiaConsultarAdminCuandoRoleAdminExiste() throws Exception {

            ReflectionTestUtils.setField(adminInitializer, "correo", "admin@test.com");
            ReflectionTestUtils.setField(adminInitializer, "password", "admin123");

            RolModel adminRol = new RolModel();
            adminRol.setNombre("ROLE_ADMIN");

            when(rolRepository.findByNombreIgnoreCase("ROLE_ADMIN"))
                    .thenReturn(Optional.of(adminRol));

            when(usuarioRepository.findByCorreoIgnoreCase("admin@test.com"))
                    .thenReturn(Optional.empty());

            when(passwordEncoder.encode("admin123"))
                    .thenReturn("password-encriptada");

            adminInitializer.run();

            verify(rolRepository).findByNombreIgnoreCase("ROLE_ADMIN");
            verify(rolRepository, never()).findByNombreIgnoreCase("ADMIN");
        }
    }

    @Nested
    @DisplayName("describe: admin nuevo")
    class AdminNuevo {

        @Test
        @DisplayName("it: debería crear admin nuevo cuando no existe usuario y existe ROLE_ADMIN")
        void deberiaCrearAdminNuevoCuandoNoExisteUsuarioYExisteRoleAdmin() throws Exception {
            ReflectionTestUtils.setField(adminInitializer, "correo", " Admin@Test.com ");
            ReflectionTestUtils.setField(adminInitializer, "password", "admin123");

            RolModel adminRol = new RolModel();
            adminRol.setNombre("ROLE_ADMIN");

            when(rolRepository.findByNombreIgnoreCase("ROLE_ADMIN"))
                    .thenReturn(Optional.of(adminRol));
            when(usuarioRepository.findByCorreoIgnoreCase("admin@test.com"))
                    .thenReturn(Optional.empty());
            when(passwordEncoder.encode("admin123"))
                    .thenReturn("password-encriptada");

            adminInitializer.run();

            ArgumentCaptor<UsuarioModel> usuarioCaptor = ArgumentCaptor.forClass(UsuarioModel.class);
            verify(usuarioRepository).save(usuarioCaptor.capture());

            UsuarioModel adminGuardado = usuarioCaptor.getValue();

            assertEquals("Admin", adminGuardado.getNombre());
            assertEquals("NoLimits", adminGuardado.getApellidos());
            assertEquals("admin@test.com", adminGuardado.getCorreo());
            assertEquals(911111111L, adminGuardado.getTelefono());
            assertEquals("password-encriptada", adminGuardado.getPassword());
            assertEquals(adminRol, adminGuardado.getRol());
        }
    }

    @Nested
    @DisplayName("describe: admin existente")
    class AdminExistente {

        @Test
        @DisplayName("it: debería actualizar admin existente cuando ya existe usuario")
        void deberiaActualizarAdminExistenteCuandoYaExisteUsuario() throws Exception {
            ReflectionTestUtils.setField(adminInitializer, "correo", "admin@test.com");
            ReflectionTestUtils.setField(adminInitializer, "password", "admin123");

            RolModel adminRol = new RolModel();
            adminRol.setNombre("ROLE_ADMIN");

            UsuarioModel adminExistente = new UsuarioModel();
            adminExistente.setNombre("Nombre antiguo");
            adminExistente.setApellidos("Apellido antiguo");
            adminExistente.setCorreo("admin@test.com");

            when(rolRepository.findByNombreIgnoreCase("ROLE_ADMIN"))
                    .thenReturn(Optional.of(adminRol));
            when(usuarioRepository.findByCorreoIgnoreCase("admin@test.com"))
                    .thenReturn(Optional.of(adminExistente));
            when(passwordEncoder.encode("admin123"))
                    .thenReturn("password-actualizada");

            adminInitializer.run();

            verify(usuarioRepository).save(adminExistente);

            assertEquals("Admin", adminExistente.getNombre());
            assertEquals("NoLimits", adminExistente.getApellidos());
            assertEquals("admin@test.com", adminExistente.getCorreo());
            assertEquals(911111111L, adminExistente.getTelefono());
            assertEquals("password-actualizada", adminExistente.getPassword());
            assertEquals(adminRol, adminExistente.getRol());
        }
    }
}