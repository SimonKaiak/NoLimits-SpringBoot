package com.example.NoLimits.controller.auth;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.security.JwtUtil;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private RolRepository rolRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    // ================== HELPERS ==================

    private RolModel crearRolUser() {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);
        return rol;
    }

    private UsuarioModel usuarioConRol() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("juan@test.com");
        usuario.setTelefono(123456789L);
        usuario.setPassword("$2a$10$hashedpassword");
        usuario.setRol(crearRolUser());
        return usuario;
    }

    private UsuarioModel usuarioEduardo() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(10L);
        usuario.setNombre("Eduardo");
        usuario.setApellidos("Hernandez");
        usuario.setCorreo("eduardo@test.com");
        usuario.setPassword("password-encriptada");
        usuario.setRol(crearRolUser());
        return usuario;
    }

        // ================== LOGIN ==================

    @Nested
    @DisplayName("POST /login")
    class LoginTests {

        @Test
        @DisplayName("Login retorna 401 cuando usuario tiene password null")
        void login_passwordNull_retorna401() throws Exception {

                UsuarioModel usuario = usuarioConRol();
                usuario.setPassword(null);

                when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                        .thenReturn(Optional.of(usuario));

                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"juan@test.com\",\"password\":\"Password123!\"}"))
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().string("Correo o contraseña incorrectos"));

                verify(passwordEncoder, never()).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("Retorna 200 con token cuando las credenciales son válidas")
        void login_credencialesValidas_retorna200ConToken() throws Exception {
                UsuarioModel usuario = usuarioConRol();

                when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                        .thenReturn(Optional.of(usuario));
                when(passwordEncoder.matches("Password123!", usuario.getPassword()))
                        .thenReturn(true);
                when(jwtUtil.generateToken("juan@test.com", "ROLE_USER"))
                        .thenReturn("jwt-token-mock");

                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"juan@test.com\",\"password\":\"Password123!\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").value("jwt-token-mock"))
                        .andExpect(jsonPath("$.correo").value("juan@test.com"))
                        .andExpect(jsonPath("$.rolNombre").value("ROLE_USER"));
        }

        @Test
        @DisplayName("Retorna 200 con token para usuario Eduardo")
        void login_eduardo_credencialesValidas_retorna200() throws Exception {
                UsuarioModel usuario = usuarioEduardo();

                when(usuarioRepository.findByCorreoIgnoreCase("eduardo@test.com"))
                        .thenReturn(Optional.of(usuario));
                when(passwordEncoder.matches("123456", usuario.getPassword()))
                        .thenReturn(true);
                when(jwtUtil.generateToken("eduardo@test.com", "ROLE_USER"))
                        .thenReturn("jwt-token-test");

                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"eduardo@test.com\",\"password\":\"123456\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").value("jwt-token-test"))
                        .andExpect(jsonPath("$.id").value(10))
                        .andExpect(jsonPath("$.nombre").value("Eduardo"))
                        .andExpect(jsonPath("$.correo").value("eduardo@test.com"))
                        .andExpect(jsonPath("$.rolNombre").value("ROLE_USER"));
        }

        @Test
        @DisplayName("Retorna 400 cuando falta el correo")
        void login_sinCorreo_retorna400() throws Exception {
                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"password\":\"Password123!\"}"))
                        .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Retorna 400 cuando falta la contraseña")
        void login_sinPassword_retorna400() throws Exception {
                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"juan@test.com\"}"))
                        .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Retorna 401 cuando el usuario no existe")
        void login_usuarioNoEncontrado_retorna401() throws Exception {
                when(usuarioRepository.findByCorreoIgnoreCase("noexiste@test.com"))
                        .thenReturn(Optional.empty());

                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"noexiste@test.com\",\"password\":\"Password123!\"}"))
                        .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Retorna 401 cuando las credenciales son incorrectas")
        void debeRetornarUnauthorizedSiCredencialesSonIncorrectas() throws Exception {
                when(usuarioRepository.findByCorreoIgnoreCase("eduardo@test.com"))
                        .thenReturn(Optional.empty());

                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"eduardo@test.com\",\"password\":\"incorrecta\"}"))
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().string("Correo o contraseña incorrectos"));
        }

        @Test
        @DisplayName("Retorna 401 cuando la contraseña es incorrecta")
        void login_passwordIncorrecta_retorna401() throws Exception {
                UsuarioModel usuario = usuarioConRol();

                when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                        .thenReturn(Optional.of(usuario));
                when(passwordEncoder.matches("WrongPassword", usuario.getPassword()))
                        .thenReturn(false);

                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"juan@test.com\",\"password\":\"WrongPassword\"}"))
                        .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Normaliza espacios en correo y contraseña")
        void login_correoConEspacios_seNormaliza() throws Exception {
                UsuarioModel usuario = usuarioConRol();

                when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                        .thenReturn(Optional.of(usuario));
                when(passwordEncoder.matches("Password123!", usuario.getPassword()))
                        .thenReturn(true);
                when(jwtUtil.generateToken(anyString(), anyString()))
                        .thenReturn("jwt-token-mock");

                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"  juan@test.com  \",\"password\":\"  Password123!  \"}"))
                        .andExpect(status().isOk());
        }

    }

        // ================== RESET PASSWORD ==================

    @Nested
    @DisplayName("POST /reset-password")
    class ResetPasswordTests {
        @Test
        @DisplayName("Retorna 200 cuando la contraseña se actualiza correctamente")
        void resetPassword_usuarioExiste_retorna200() throws Exception {
                UsuarioModel usuario = usuarioConRol();

                when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                        .thenReturn(Optional.of(usuario));
                when(passwordEncoder.encode("NuevaPassword123!"))
                        .thenReturn("$2a$10$newhash");
                when(usuarioRepository.save(any(UsuarioModel.class)))
                        .thenReturn(usuario);

                mockMvc.perform(post("/api/v1/auth/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"juan@test.com\",\"password\":\"NuevaPassword123!\"}"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Actualiza correctamente la contraseña del usuario")
        void debeActualizarPasswordCorrectamente() throws Exception {
                UsuarioModel usuario = usuarioEduardo();

                when(usuarioRepository.findByCorreoIgnoreCase("eduardo@test.com"))
                        .thenReturn(Optional.of(usuario));
                when(passwordEncoder.encode("nueva123"))
                        .thenReturn("$2a$10$newhash");
                when(usuarioRepository.save(any(UsuarioModel.class)))
                        .thenReturn(usuario);

                mockMvc.perform(post("/api/v1/auth/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"eduardo@test.com\",\"password\":\"nueva123\"}"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Contraseña actualizada correctamente"));
        }

        @Test
        @DisplayName("Retorna 400 cuando falta el correo")
        void resetPassword_sinCorreo_retorna400() throws Exception {
                mockMvc.perform(post("/api/v1/auth/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"password\":\"NuevaPassword123!\"}"))
                        .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Retorna 400 cuando falta la contraseña")
        void resetPassword_sinPassword_retorna400() throws Exception {
                mockMvc.perform(post("/api/v1/auth/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"juan@test.com\"}"))
                        .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Retorna 404 cuando el usuario no existe")
        void resetPassword_usuarioNoEncontrado_retorna404() throws Exception {
                when(usuarioRepository.findByCorreoIgnoreCase("noexiste@test.com"))
                        .thenReturn(Optional.empty());

                mockMvc.perform(post("/api/v1/auth/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"noexiste@test.com\",\"password\":\"NuevaPassword123!\"}"))
                        .andExpect(status().isNotFound());
        }
    }


        // ================== GOOGLE SYNC ==================

    @Nested
    @DisplayName("POST /google/sync")
    class GoogleSyncTests {

        @Test
        @DisplayName("Crea un nuevo usuario y retorna token")
        void googleSync_usuarioNuevo_creaYRetornaToken() throws Exception {

                when(usuarioRepository.findByCorreoIgnoreCase("nuevo@gmail.com"))
                        .thenReturn(Optional.empty());
                when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                        .thenReturn(Optional.of(crearRolUser()));
                when(usuarioRepository.save(any(UsuarioModel.class)))
                        .thenAnswer(invocation -> {
                        UsuarioModel usuario = invocation.getArgument(0);
                        usuario.setId(99L);
                        return usuario;
                        });
                when(jwtUtil.generateToken(anyString(), anyString()))
                        .thenReturn("google-jwt-token");

                mockMvc.perform(post("/api/v1/auth/google/sync")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"nuevo@gmail.com\",\"nombre\":\"Carlos\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").value("google-jwt-token"));
        }

        @Test
        @DisplayName("Retorna token para usuario existente sin crear registro")
        void googleSync_usuarioExistente_retornaTokenSinCrear() throws Exception {

                UsuarioModel usuario = new UsuarioModel();
                usuario.setId(20L);
                usuario.setNombre("Eduardo");
                usuario.setApellidos("Google");
                usuario.setCorreo("eduardo@gmail.com");
                usuario.setRol(crearRolUser());

                when(usuarioRepository.findByCorreoIgnoreCase("eduardo@gmail.com"))
                        .thenReturn(Optional.of(usuario));
                when(jwtUtil.generateToken("eduardo@gmail.com", "ROLE_USER"))
                        .thenReturn("google-jwt-token");

                mockMvc.perform(post("/api/v1/auth/google/sync")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"eduardo@gmail.com\",\"nombre\":\"Eduardo\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").value("google-jwt-token"))
                        .andExpect(jsonPath("$.correo").value("eduardo@gmail.com"))
                        .andExpect(jsonPath("$.rolNombre").value("ROLE_USER"));

                verify(usuarioRepository, never()).save(any(UsuarioModel.class));
        }

        @Test
        @DisplayName("Retorna 400 cuando falta el correo")
        void googleSync_sinCorreo_retorna400() throws Exception {
                mockMvc.perform(post("/api/v1/auth/google/sync")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nombre\":\"Carlos\"}"))
                        .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Retorna 400 cuando el correo está vacío")
        void googleSync_correoVacio_retorna400() throws Exception {
                mockMvc.perform(post("/api/v1/auth/google/sync")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"   \",\"nombre\":\"Carlos\"}"))
                        .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Google Sync falla cuando ROLE_USER no existe")
        void googleSync_sinRoleUser_lanzaExcepcion() throws Exception {

                when(usuarioRepository.findByCorreoIgnoreCase("nuevo@gmail.com"))
                        .thenReturn(Optional.empty());

                when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                        .thenReturn(Optional.empty());

                mockMvc.perform(post("/api/v1/auth/google/sync")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"correo\":\"nuevo@gmail.com\",\"nombre\":\"Carlos\"}"))
                        .andExpect(status().is5xxServerError());
        }

        @Test
        @DisplayName("Google Sync crea usuario con nombre vacío cuando nombre es null")
        void googleSync_nombreNull_creaUsuarioConNombreVacio() throws Exception {

                when(usuarioRepository.findByCorreoIgnoreCase("nuevo@gmail.com"))
                        .thenReturn(Optional.empty());

                when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                        .thenReturn(Optional.of(crearRolUser()));

                when(usuarioRepository.save(any()))
                        .thenAnswer(invocation -> {
                                UsuarioModel u = invocation.getArgument(0);
                                u.setId(99L);
                                return u;
                        });

                when(jwtUtil.generateToken(anyString(), anyString()))
                        .thenReturn("token-google");

                mockMvc.perform(post("/api/v1/auth/google/sync")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "correo":"nuevo@gmail.com"
                                        }
                                """))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.nombre").value(""));
        }

    }

        // ================== LOGOUT ==================

    @Nested
    @DisplayName("POST /logout")
    class LogoutTests {

        @Test
        @DisplayName("Retorna 200 al cerrar sesión")
        void logout_retorna200() throws Exception {
                mockMvc.perform(post("/api/v1/auth/logout"))
                        .andExpect(status().isOk());
        }
    }

}






   

    
