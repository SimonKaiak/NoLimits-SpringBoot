package com.example.NoLimits.controller;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.security.JwtUtil;
import com.example.NoLimits.config.AbstractContainerBaseTest;

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

/**
 * AuthControllerTest — Pruebas funcionales y validación de API
 *
 * Cubre los tres endpoints de /api/v1/auth:
 *   - POST /login
 *   - POST /reset-password
 *   - POST /google/sync
 *   - POST /logout
 *
 * Tipo: Funcional (se prueba el controlador completo con MockMvc,
 * mockeando sólo la capa de persistencia y seguridad).
 */
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
        UsuarioModel usuarioExistente = usuarioConRol();
        usuarioExistente.setCorreo("existente@gmail.com");

        when(usuarioRepository.findByCorreoIgnoreCase("existente@gmail.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("existing-jwt-token");

        mockMvc.perform(post("/api/v1/auth/google/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"existente@gmail.com\",\"nombre\":\"Carlos\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("existing-jwt-token"));

        // Verifica que NO se intentó guardar un usuario nuevo
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