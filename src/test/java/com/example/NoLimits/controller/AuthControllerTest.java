package com.example.NoLimits.controller;

import com.example.NoLimits.Multimedia.controller.auth.AuthController;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.security.JwtUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

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

    @Test
    @DisplayName("Debe iniciar sesión correctamente")
    void debeIniciarSesionCorrectamente() throws Exception {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(10L);
        usuario.setNombre("James");
        usuario.setApellidos("Videla");
        usuario.setCorreo("james@test.com");
        usuario.setPassword("password-encriptada");
        usuario.setRol(rol);

        when(usuarioRepository.findByCorreoIgnoreCase("james@test.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("123456", "password-encriptada"))
                .thenReturn(true);

        when(jwtUtil.generateToken("james@test.com", "ROLE_USER"))
                .thenReturn("jwt-token-test");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "correo": "james@test.com",
                          "password": "123456"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-test"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("James"))
                .andExpect(jsonPath("$.correo").value("james@test.com"))
                .andExpect(jsonPath("$.rolNombre").value("ROLE_USER"));
    }

    @Test
    @DisplayName("Debe retornar 400 si faltan credenciales")
    void debeRetornarBadRequestSiFaltanCredenciales() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "correo": "james@test.com"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Correo y contraseña obligatorios"));
    }

    @Test
    @DisplayName("Debe retornar 401 si las credenciales son incorrectas")
    void debeRetornarUnauthorizedSiCredencialesSonIncorrectas() throws Exception {
        when(usuarioRepository.findByCorreoIgnoreCase("james@test.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "correo": "james@test.com",
                          "password": "incorrecta"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Correo o contraseña incorrectos"));
    }

    @Test
    @DisplayName("Debe actualizar contraseña correctamente")
    void debeActualizarPasswordCorrectamente() throws Exception {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(10L);
        usuario.setCorreo("james@test.com");
        usuario.setPassword("old-password");

        when(usuarioRepository.findByCorreoIgnoreCase("james@test.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.encode("nueva123"))
                .thenReturn("password-encriptada-nueva");

        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenReturn(usuario);

        mockMvc.perform(post("/api/v1/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "correo": "james@test.com",
                          "password": "nueva123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña actualizada correctamente"));
    }

    @Test
    @DisplayName("Debe sincronizar usuario Google existente")
    void debeSincronizarUsuarioGoogleExistente() throws Exception {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(20L);
        usuario.setNombre("James");
        usuario.setApellidos("Google");
        usuario.setCorreo("james@gmail.com");
        usuario.setRol(rol);

        when(usuarioRepository.findByCorreoIgnoreCase("james@gmail.com"))
                .thenReturn(Optional.of(usuario));

        when(jwtUtil.generateToken("james@gmail.com", "ROLE_USER"))
                .thenReturn("google-jwt-token");

        mockMvc.perform(post("/api/v1/auth/google/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "correo": "james@gmail.com",
                          "nombre": "James"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("google-jwt-token"))
                .andExpect(jsonPath("$.correo").value("james@gmail.com"))
                .andExpect(jsonPath("$.rolNombre").value("ROLE_USER"));
    }

    @Test
    @DisplayName("Debe cerrar sesión correctamente")
    void debeCerrarSesionCorrectamente() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout OK"));
    }
}