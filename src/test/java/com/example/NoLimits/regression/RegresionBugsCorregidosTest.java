package com.example.NoLimits.regression;

import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRegistroDTO;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.usuario.FavoritoRepository;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.repository.review.ReviewRepository;
import com.example.NoLimits.Multimedia.repository.review.ReviewReactionRepository;
import com.example.NoLimits.Multimedia.security.JwtUtil;
import com.example.NoLimits.Multimedia.service.usuario.UsuarioService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("T-16 · Regresión — Bugs ya corregidos no deben reaparecer")
public class RegresionBugsCorregidosTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

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

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("REG-01 — Correo se normaliza a minúsculas al registrar usuario")
    void reg01_correoSeNormalizaEnMinusculas_noRegresan() {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);

        when(usuarioRepository.findByCorreoIgnoreCase(anyString()))
                .thenReturn(Optional.empty());
        when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                .thenReturn(Optional.of(rol));
        when(passwordEncoder.encode(anyString()))
                .thenReturn("$2a$hash");
        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> {
                    UsuarioModel u = inv.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setNombre("Juan");
        dto.setApellidos("Pérez");
        dto.setCorreo("JUAN@TEST.COM");
        dto.setPassword("Password123!");
        dto.setTelefono(912345678L);

        assertDoesNotThrow(() -> usuarioService.save(dto),
                "REG-01 REGRESIÓN: el registro con correo en mayúsculas lanzó excepción");

        verify(usuarioRepository).save(argThat(u ->
                u.getCorreo() != null && u.getCorreo().equals(u.getCorreo().toLowerCase())
        ));
    }

    @Test
    @DisplayName("REG-02 — Registro con correo duplicado lanza 409, no crea usuario duplicado")
    void reg02_correoDuplicado_lanza409_noGuardaUsuario() {

        when(usuarioRepository.existsByCorreo("duplicado@test.com"))
                .thenReturn(true);

        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setNombre("Otro");
        dto.setApellidos("Usuario");
        dto.setCorreo("duplicado@test.com");
        dto.setPassword("Password123!");
        dto.setTelefono(900000000L);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.save(dto),
        "REG-02 REGRESIÓN: no lanzó excepción con correo duplicado"
        );

        assertEquals(
            409,
                ex.getStatusCode().value(),
                "REG-02 REGRESIÓN: debería lanzar 409, lanzó "
                        + ex.getStatusCode().value()
        );

        verify(usuarioRepository, never()).save(any(UsuarioModel.class));
}

    @Test
    @DisplayName("REG-03 — Login con correo con espacios es aceptado (trim aplicado)")
    void reg03_loginCorreoConEspacios_seNormaliza() throws Exception {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("juan@test.com");
        usuario.setPassword("$2a$10$hash");
        usuario.setRol(rol);

        when(usuarioRepository.findByCorreoIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Pass123!", usuario.getPassword()))
                .thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("jwt-token");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"  juan@test.com  \",\"password\":\"  Pass123!  \"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    @DisplayName("REG-04 — Reset-password con usuario inexistente retorna 404, no 500")
    void reg04_resetPassword_usuarioInexistente_retorna404() throws Exception {
        when(usuarioRepository.findByCorreoIgnoreCase("fantasma@test.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"fantasma@test.com\",\"password\":\"NuevaClave123!\"}"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertNotEquals(500,
                        result.getResponse().getStatus()));
    }

    @Test
    @DisplayName("REG-05 — Login sin campo password retorna 400, no 500")
    void reg05_loginSinPassword_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"juan@test.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertNotEquals(500,
                        result.getResponse().getStatus()));
    }

    @Test
    @DisplayName("REG-06 — Google sync con correo vacío retorna 400, no 500")
    void reg06_googleSync_correoVacio_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/google/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"   \",\"nombre\":\"Carlos\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertNotEquals(500,
                        result.getResponse().getStatus()));
    }
}