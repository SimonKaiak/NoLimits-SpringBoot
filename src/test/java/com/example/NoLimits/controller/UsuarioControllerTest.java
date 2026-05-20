package com.example.NoLimits.controller;

import com.example.NoLimits.Multimedia.config.AdminInitializer;
import com.example.NoLimits.Multimedia.controller.usuario.UsuarioController;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;
import com.example.NoLimits.Multimedia.model.usuario.FavoritoModel;
import com.example.NoLimits.Multimedia.service.usuario.UsuarioService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private AdminInitializer adminInitializer;

    @AfterEach
    void limpiarSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Debe listar usuarios correctamente")
    void debeListarUsuariosCorrectamente() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setNombre("James");
        usuario.setApellidos("Videla");
        usuario.setCorreo("james@test.com");

        when(usuarioService.findAll())
                .thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("James"))
                .andExpect(jsonPath("$[0].correo").value("james@test.com"));
    }

    @Test
    @DisplayName("Debe retornar 204 si no hay usuarios")
    void debeRetornarNoContentSiNoHayUsuarios() throws Exception {
        when(usuarioService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar usuario por ID")
    void debeBuscarUsuarioPorId() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setNombre("James");
        usuario.setCorreo("james@test.com");

        when(usuarioService.findById(1L))
                .thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("James"));
    }

    @Test
    @DisplayName("Debe obtener favoritos por usuario")
    void debeObtenerFavoritosPorUsuario() throws Exception {
        when(usuarioService.obtenerFavoritosPorUsuario(1L))
                .thenReturn(List.of(new FavoritoModel()));

        mockMvc.perform(get("/api/v1/usuarios/1/favoritos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe eliminar favorito del usuario")
    void debeEliminarFavoritoDelUsuario() throws Exception {
        doNothing().when(usuarioService)
                .eliminarFavorito(1L, "tmdb:movie:1893");

        mockMvc.perform(delete("/api/v1/usuarios/1/favoritos/tmdb:movie:1893"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe obtener detalle de compras del usuario")
    void debeObtenerDetalleComprasUsuario() throws Exception {
        when(usuarioService.obtenerDetalleUsuario(1L))
                .thenReturn(Map.of("totalCompras", 2));

        mockMvc.perform(get("/api/v1/usuarios/1/compras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCompras").value(2));
    }

    @Test
    @DisplayName("Debe registrar usuario público")
    void debeRegistrarUsuarioPublico() throws Exception {
        UsuarioResponseDTO creado = new UsuarioResponseDTO();
        creado.setId(10L);
        creado.setNombre("James");
        creado.setCorreo("james@test.com");

        when(usuarioService.save(any()))
                .thenReturn(creado);

        mockMvc.perform(post("/api/v1/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre": "James",
                          "apellidos": "Videla",
                          "correo": "james@test.com",
                          "telefono": 912345678,
                          "password": "clave123"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("James"));
    }

    @Test
    @DisplayName("Debe eliminar usuario por ID")
    void debeEliminarUsuarioPorId() throws Exception {
        doNothing().when(usuarioService)
                .deleteById(1L);

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar usuarios por nombre")
    void debeBuscarUsuariosPorNombre() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setNombre("James");

        when(usuarioService.findByNombre("James"))
                .thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios/nombre/James"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("James"));
    }

    @Test
    @DisplayName("Debe buscar usuario por correo")
    void debeBuscarUsuarioPorCorreo() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setCorreo("james@test.com");

        when(usuarioService.findByCorreo("james@test.com"))
                .thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/correo/james@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("james@test.com"));
    }

    @Test
    @DisplayName("Debe obtener mi perfil si está autenticado")
    void debeObtenerMiPerfilAutenticado() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setCorreo("james@test.com");

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("james@test.com", null, "ROLE_USER")
        );

        when(usuarioService.findByCorreo("james@test.com"))
                .thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("james@test.com"));
    }

    @Test
    @DisplayName("Debe retornar 401 si no está autenticado al pedir /me")
    void debeRetornarUnauthorizedSiNoEstaAutenticado() throws Exception {
        SecurityContextHolder.clearContext();

        mockMvc.perform(get("/api/v1/usuarios/me"))
                .andExpect(status().isUnauthorized());
    }
}