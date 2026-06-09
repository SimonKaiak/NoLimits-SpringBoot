package com.example.NoLimits.security;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("T-17 · Seguridad — Vulnerabilidades y entradas peligrosas")
class SeguridadTest extends AbstractContainerBaseTest {

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

    @Nested
    @DisplayName("Entradas peligrosas en login")
    class EntradasPeligrosasEnLogin {

        @Test
        @DisplayName("SEC-01 — SQL injection en correo no causa error 500")
        void sec01_sqlInjection_enCorreo_noProvoca500() throws Exception {
            when(usuarioRepository.findByCorreoIgnoreCase(anyString()))
                    .thenReturn(Optional.empty());

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"correo\":\"' OR '1'='1' --\",\"password\":\"cualquier\"}"))
                    .andExpect(result -> {
                        int status = result.getResponse().getStatus();
                        assertNotEquals(500, status, "SEC-01: SQL injection provocó error 500");
                        assertTrue(status == 401 || status == 400);
                    });
        }

        @Test
        @DisplayName("SEC-02 — SQL injection en password no causa error 500")
        void sec02_sqlInjection_enPassword_noProvoca500() throws Exception {
            when(usuarioRepository.findByCorreoIgnoreCase(anyString()))
                    .thenReturn(Optional.empty());

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"correo\":\"test@test.com\",\"password\":\"' OR 1=1 --\"}"))
                    .andExpect(result -> assertNotEquals(
                            500,
                            result.getResponse().getStatus(),
                            "SEC-02: SQL injection en password provocó 500"
                    ));
        }

        @Test
        @DisplayName("SEC-03 — Payload XSS en correo no causa error 500")
        void sec03_xss_enCorreo_noProvoca500() throws Exception {
            when(usuarioRepository.findByCorreoIgnoreCase(anyString()))
                    .thenReturn(Optional.empty());

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"correo\":\"<script>alert('xss')</script>\",\"password\":\"pass\"}"))
                    .andExpect(result -> assertNotEquals(
                            500,
                            result.getResponse().getStatus(),
                            "SEC-03: XSS en correo provocó 500"
                    ));
        }

        @Test
        @DisplayName("SEC-05 — Correo extremadamente largo no causa 500")
        void sec05_correoExtremamdamenteLargo_noProvoca500() throws Exception {
            String correoGigante = "a".repeat(9990) + "@test.com";

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"correo\":\"" + correoGigante + "\",\"password\":\"pass\"}"))
                    .andExpect(result -> assertNotEquals(
                            500,
                            result.getResponse().getStatus(),
                            "SEC-05: correo gigante provocó 500"
                    ));
        }

        @Test
        @DisplayName("SEC-10 — Caracteres especiales en password no causan error 500")
        void sec10_caracteresEspeciales_enPassword_noProvoca500() throws Exception {
            when(usuarioRepository.findByCorreoIgnoreCase(anyString()))
                    .thenReturn(Optional.empty());

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"correo\":\"test@test.com\",\"password\":\"{}[]<>|;:\"}"))
                    .andExpect(result -> assertNotEquals(
                            500,
                            result.getResponse().getStatus(),
                            "SEC-10: caracteres especiales provocaron 500"
                    ));
        }
    }

    @Nested
    @DisplayName("Entradas peligrosas en Google Sync")
    class EntradasPeligrosasEnGoogleSync {

        @Test
        @DisplayName("SEC-04 — Payload XSS en nombre no causa error 500")
        void sec04_xss_enNombre_googleSync_noProvoca500() throws Exception {
            when(usuarioRepository.findByCorreoIgnoreCase(anyString()))
                    .thenReturn(Optional.empty());

            RolModel rol = new RolModel();
            rol.setId(1L);
            rol.setNombre("ROLE_USER");

            when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                    .thenReturn(Optional.of(rol));

            when(passwordEncoder.encode(anyString()))
                    .thenReturn("password-encriptada");

            when(jwtUtil.generateToken(anyString(), anyString()))
                    .thenReturn("fake-token");

            when(usuarioRepository.save(any(UsuarioModel.class)))
                    .thenAnswer(invocation -> {
                        UsuarioModel usuario = invocation.getArgument(0);
                        usuario.setId(1L);
                        return usuario;
                    });

            mockMvc.perform(post("/api/v1/auth/google/sync")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"correo\":\"test@test.com\",\"nombre\":\"<img src=x onerror=alert(1)>\"}"))
                    .andExpect(result -> assertNotEquals(
                            500,
                            result.getResponse().getStatus(),
                            "SEC-04: XSS en nombre provocó 500"
                    ));
        }
    }

    @Nested
    @DisplayName("Endpoints protegidos sin token")
    class EndpointsProtegidosSinToken {

        @Test
        @DisplayName("SEC-06 — GET /api/v1/usuarios sin token retorna 401 o 403")
        void sec06_endpointProtegido_sinToken_retorna401o403() throws Exception {
            mockMvc.perform(get("/api/v1/usuarios")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(result -> {
                        int status = result.getResponse().getStatus();
                        assertNotEquals(200, status, "SEC-06: endpoint protegido respondió 200 sin token");
                        assertTrue(status == 401 || status == 403);
                    });
        }

        @Test
        @DisplayName("SEC-07 — GET /api/v1/ventas sin token retorna 401 o 403")
        void sec07_endpointVentas_sinToken_retorna401o403() throws Exception {
            mockMvc.perform(get("/api/v1/ventas")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(result -> {
                        int status = result.getResponse().getStatus();
                        assertNotEquals(200, status, "SEC-07: /api/v1/ventas respondió 200 sin token");
                        assertTrue(status == 401 || status == 403);
                    });
        }
    }

    @Nested
    @DisplayName("Endpoints públicos")
    class EndpointsPublicos {

        @Test
        @DisplayName("SEC-08 — GET /health es público: responde 200 sin token")
        void sec08_healthEsPublico_responde200SinToken() throws Exception {
            mockMvc.perform(get("/health"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("SEC-09 — POST /api/v1/auth/login es público: no bloquea la petición")
        void sec09_loginEsPublico_noBloqueaLaPeticion() throws Exception {
            when(usuarioRepository.findByCorreoIgnoreCase(anyString()))
                    .thenReturn(Optional.empty());

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"correo\":\"test@test.com\",\"password\":\"pass\"}"))
                    .andExpect(result -> assertNotEquals(
                            403,
                            result.getResponse().getStatus(),
                            "SEC-09: /login está bloqueado por seguridad (403)"
                    ));
        }
    }

    @Nested
    @DisplayName("SecurityConfig — EntryPoint")
    class SecurityConfigEntryPoint {

        @Test
        @DisplayName("SEC-11 — GET / redirige a Swagger")
        void sec11_getRoot_redirigeASwagger() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/doc/swagger-ui.html"));
        }
    }
}