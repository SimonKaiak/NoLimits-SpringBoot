package com.example.NoLimits.integration.auth;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.security.JwtUtil;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:nolimits_auth_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.id.new_generator_mappings=false"
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("T-Integración · Auth")
class AuthIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;

    @MockBean private JwtUtil jwtUtil;
    @MockBean private PasswordEncoder passwordEncoder;

    private RolModel rolUser;

    @BeforeEach
    void prepararDatos() {
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();

        rolUser = new RolModel();
        rolUser.setNombre("ROLE_USER");
        rolUser.setActivo(true);
        rolUser = rolRepository.save(rolUser);
    }

    private UsuarioModel crearUsuario(String correo, String passwordHash) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo(correo);
        usuario.setTelefono(912345678L);
        usuario.setPassword(passwordHash);
        usuario.setRol(rolUser);
        return usuarioRepository.save(usuario);
    }

    @Nested
    @DisplayName("describe: integración Controller → Repository → BD")
    class IntegracionAuth {

        @Test
        @DisplayName("it: login exitoso retorna token y datos del usuario")
        void loginExitoso_retornaTokenYUsuario() throws Exception {
            crearUsuario("juan@test.com", "$2a$10$hash");

            when(passwordEncoder.matches("Password123!", "$2a$10$hash"))
                    .thenReturn(true);

            when(jwtUtil.generateToken("juan@test.com", "ROLE_USER"))
                    .thenReturn("jwt-token-test");

            String body = """
                    {
                      "correo": "juan@test.com",
                      "password": "Password123!"
                    }
                    """;

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt-token-test"))
                    .andExpect(jsonPath("$.correo").value("juan@test.com"))
                    .andExpect(jsonPath("$.rolNombre").value("ROLE_USER"));
        }

        @Test
        @DisplayName("it: reset de contraseña actualiza password en la base de datos")
        void resetPassword_actualizaPasswordEnBD() throws Exception {
            crearUsuario("reset@test.com", "$2a$10$oldhash");

            when(passwordEncoder.encode("NuevaPassword123!"))
                    .thenReturn("$2a$10$newhash");

            String body = """
                    {
                      "correo": "reset@test.com",
                      "password": "NuevaPassword123!"
                    }
                    """;

            mockMvc.perform(post("/api/v1/auth/reset-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Contraseña actualizada correctamente"));

            UsuarioModel actualizado = usuarioRepository
                    .findByCorreoIgnoreCase("reset@test.com")
                    .orElseThrow();

            assertTrue(actualizado.getPassword().equals("$2a$10$newhash"));
        }

        @Test
        @DisplayName("it: google sync crea usuario nuevo y retorna token")
        void googleSync_usuarioNuevo_creaUsuarioYToken() throws Exception {
            when(passwordEncoder.encode(anyString()))
                    .thenReturn("$2a$10$googlehash");

            when(jwtUtil.generateToken("nuevo@gmail.com", "ROLE_USER"))
                    .thenReturn("google-jwt-token");

            String body = """
                    {
                      "correo": "nuevo@gmail.com",
                      "nombre": "Carlos"
                    }
                    """;

            mockMvc.perform(post("/api/v1/auth/google/sync")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("google-jwt-token"))
                    .andExpect(jsonPath("$.correo").value("nuevo@gmail.com"))
                    .andExpect(jsonPath("$.nombre").value("Carlos"))
                    .andExpect(jsonPath("$.rolNombre").value("ROLE_USER"));

            assertTrue(usuarioRepository.existsByCorreoIgnoreCase("nuevo@gmail.com"));
        }
    }
}