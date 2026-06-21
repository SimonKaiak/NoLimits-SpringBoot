package com.example.NoLimits.smoke;

import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResumenDTO;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;
import com.example.NoLimits.config.AbstractContainerBaseTest;
import com.example.NoLimits.config.TestSecurityConfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@DisplayName("T-14 · Smoke Tests — El sistema arranca y responde lo mínimo")
public class SmokeTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ProductoService productoService;

    @Test
    @DisplayName("GET /health → 200 OK: el sistema está vivo")
    void smoke_healthEndpoint_responde200() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login → no retorna 404 ni 500: el endpoint existe")
    void smoke_loginEndpoint_existeYResponde() throws Exception {
        when(usuarioRepository.findByCorreoIgnoreCase("smoke@test.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"smoke@test.com\",\"password\":\"smoke\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/v1/productos → no retorna 404 ni 500: el endpoint existe")
    void smoke_productosEndpoint_existeYResponde() throws Exception {
        when(productoService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/usuarios → no retorna 404 ni 500: el endpoint existe")
    void smoke_usuariosEndpoint_existeYResponde() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status == 404 || status >= 500) {
                        throw new AssertionError(
                                "Smoke FAIL: /api/v1/usuarios retornó " + status
                        );
                    }
                });
    }

    @Test
    @DisplayName("Contexto Spring Boot carga correctamente todos los beans")
    void smoke_springContext_cargaSinErrores() {
    }
}