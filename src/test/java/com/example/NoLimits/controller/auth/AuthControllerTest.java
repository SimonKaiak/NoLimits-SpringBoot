package com.example.NoLimits.controller.auth;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.security.JwtUtil;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.junit.jupiter.api.DisplayName;
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
public class AuthControllerTest extends AbstractContainerBaseTest {

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

    // ===================== HELPERS =====================

    private UsuarioModel usuarioConRol() {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);

        UsuarioModel u = new UsuarioModel();
        u.setId(1L);
        u.setNombre("Juan");
        u.setApellidos("Pérez");
        u.setCorreo("juan@test.com");
        u.setTelefono(123456789L);
        u.setPassword("$2a$10$hashedpassword");
        u.setRol(rol);
        return u;
    }

    private UsuarioModel usuarioEduardo() {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);

        UsuarioModel u = new UsuarioModel();
        u.setId(10L);
        u.setNombre("Eduardo");
        u.setApellidos("Hernandez");
        u.setCorreo("eduardo@test.com");
        u.setPassword("password-encriptada");
        u.setRol(rol);
        return u;
    }

    // ===================== POST /login =====================

    @Test
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
    @DisplayName("Login con credenciales de Eduardo retorna 200 con token")
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
    void login_sinCorreo_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"Password123!\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_sinPassword_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"juan@test.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_usuarioNoEncontrado_retorna401() throws Exception {
        when(usuarioRepository.findByCorreoIgnoreCase("noexiste@test.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"noexiste@test.com\",\"password\":\"Password123!\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debe retornar 401 si las credenciales son incorrectas")
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

    // ===================== POST /reset-password =====================

    @Test
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
    @DisplayName("Debe actualizar contraseña correctamente")
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
    void resetPassword_sinCorreo_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"NuevaPassword123!\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void resetPassword_sinPassword_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"juan@test.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void resetPassword_usuarioNoEncontrado_retorna404() throws Exception {
        when(usuarioRepository.findByCorreoIgnoreCase("noexiste@test.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"noexiste@test.com\",\"password\":\"NuevaPassword123!\"}"))
                .andExpect(status().isNotFound());
    }

    // ===================== POST /google/sync =====================

    @Test
    void googleSync_usuarioNuevo_creaYRetornaToken() throws Exception {
        RolModel rolUser = new RolModel();
        rolUser.setId(1L);
        rolUser.setNombre("ROLE_USER");
        rolUser.setActivo(true);

        when(usuarioRepository.findByCorreoIgnoreCase("nuevo@gmail.com"))
                .thenReturn(Optional.empty());
        when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                .thenReturn(Optional.of(rolUser));
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> {
                    UsuarioModel u = inv.getArgument(0);
                    u.setId(99L);
                    return u;
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
    void googleSync_usuarioExistente_retornaTokenSinCrear() throws Exception {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(20L);
        usuario.setNombre("Eduardo");
        usuario.setApellidos("Google");
        usuario.setCorreo("eduardo@gmail.com");
        usuario.setRol(rol);

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
    void googleSync_sinCorreo_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/google/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Carlos\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void googleSync_correoVacio_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/google/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"   \",\"nombre\":\"Carlos\"}"))
                .andExpect(status().isBadRequest());
    }

    // ===================== POST /logout =====================

    @Test
    void logout_retorna200() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk());
    }
}