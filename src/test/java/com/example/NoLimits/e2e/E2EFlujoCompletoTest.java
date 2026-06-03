package com.example.NoLimits.e2e;

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
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("T-15 · E2E / Funcional — Flujos completos de negocio de punta a punta")
public class E2EFlujoCompletoTest extends AbstractContainerBaseTest {

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

    private UsuarioModel usuarioConRol(Long id, String correo, String nombre) {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);

        UsuarioModel u = new UsuarioModel();
        u.setId(id);
        u.setNombre(nombre);
        u.setApellidos("Test");
        u.setCorreo(correo);
        u.setTelefono(912345678L);
        u.setPassword("$2a$10$hashedpassword");
        u.setRol(rol);
        return u;
    }

    @Test
    @DisplayName("E2E Flujo-1 Paso 1/4 — Login exitoso retorna token JWT")
    void e2e_flujo1_paso1_loginExitoso_retornaToken() throws Exception {
        UsuarioModel usuario = usuarioConRol(1L, "usuario@nolimits.cl", "Valentina");

        when(usuarioRepository.findByCorreoIgnoreCase("usuario@nolimits.cl"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Pass123!", usuario.getPassword()))
                .thenReturn(true);
        when(jwtUtil.generateToken("usuario@nolimits.cl", "ROLE_USER"))
                .thenReturn("jwt-e2e-token");

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"usuario@nolimits.cl\",\"password\":\"Pass123!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-e2e-token"))
                .andExpect(jsonPath("$.correo").value("usuario@nolimits.cl"))
                .andExpect(jsonPath("$.rolNombre").value("ROLE_USER"))
                .andReturn();

        String token = result.getResponse().getContentAsString();
        assertTrue(token.contains("jwt-e2e-token"));
    }

    @Test
    @DisplayName("E2E Flujo-1 Paso 2/4 — Reset de contraseña retorna 200")
    void e2e_flujo1_paso2_resetPassword_retorna200() throws Exception {
        UsuarioModel usuario = usuarioConRol(1L, "usuario@nolimits.cl", "Valentina");

        when(usuarioRepository.findByCorreoIgnoreCase("usuario@nolimits.cl"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("NuevaClave456!"))
                .thenReturn("$2a$10$newhash");
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenReturn(usuario);

        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"usuario@nolimits.cl\",\"password\":\"NuevaClave456!\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña actualizada correctamente"));
    }

    @Test
    @DisplayName("E2E Flujo-1 Paso 3/4 — Login con nueva contraseña es exitoso")
    void e2e_flujo1_paso3_loginConNuevaPassword_exitoso() throws Exception {
        UsuarioModel usuario = usuarioConRol(1L, "usuario@nolimits.cl", "Valentina");
        usuario.setPassword("$2a$10$newhash");

        when(usuarioRepository.findByCorreoIgnoreCase("usuario@nolimits.cl"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("NuevaClave456!", "$2a$10$newhash"))
                .thenReturn(true);
        when(jwtUtil.generateToken("usuario@nolimits.cl", "ROLE_USER"))
                .thenReturn("jwt-nuevo-token");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"usuario@nolimits.cl\",\"password\":\"NuevaClave456!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-nuevo-token"));
    }

    @Test
    @DisplayName("E2E Flujo-1 Paso 4/4 — Logout retorna 200")
    void e2e_flujo1_paso4_logout_retorna200() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("E2E Flujo-2 Paso 1/2 — Google sync usuario nuevo crea cuenta y retorna token")
    void e2e_flujo2_paso1_googleSync_usuarioNuevo_creaToken() throws Exception {
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
                .thenReturn("google-jwt-nuevo");

        mockMvc.perform(post("/api/v1/auth/google/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"nuevo@gmail.com\",\"nombre\":\"Carlos\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("google-jwt-nuevo"))
                .andExpect(jsonPath("$.correo").value("nuevo@gmail.com"));
    }

    @Test
    @DisplayName("E2E Flujo-2 Paso 2/2 — Google sync usuario existente reutiliza cuenta")
    void e2e_flujo2_paso2_googleSync_usuarioExistente_reutilizaCuenta() throws Exception {
        UsuarioModel usuarioExistente = usuarioConRol(50L, "existente@gmail.com", "Ana");

        when(usuarioRepository.findByCorreoIgnoreCase("existente@gmail.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(jwtUtil.generateToken("existente@gmail.com", "ROLE_USER"))
                .thenReturn("google-jwt-existente");

        mockMvc.perform(post("/api/v1/auth/google/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"existente@gmail.com\",\"nombre\":\"Ana\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("google-jwt-existente"))
                .andExpect(jsonPath("$.correo").value("existente@gmail.com"))
                .andExpect(jsonPath("$.rolNombre").value("ROLE_USER"));
    }

    @Test
    @DisplayName("E2E Flujo-3 — Login con password incorrecta bloquea acceso (401)")
    void e2e_flujo3_loginConPasswordIncorrecta_bloqueaAcceso() throws Exception {
        UsuarioModel usuario = usuarioConRol(1L, "usuario@nolimits.cl", "Valentina");

        when(usuarioRepository.findByCorreoIgnoreCase("usuario@nolimits.cl"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("ClaveEquivocada", usuario.getPassword()))
                .thenReturn(false);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"usuario@nolimits.cl\",\"password\":\"ClaveEquivocada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Correo o contraseña incorrectos"));
    }
}