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
        usuario.setNombre("Eduardo");
        usuario.setApellidos("Hernandez");
        usuario.setCorreo("eduardo@test.com");

        when(usuarioService.findAll())
                .thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Eduardo"))
                .andExpect(jsonPath("$[0].correo").value("eduardo@test.com"));
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
        usuario.setNombre("Eduardo");
        usuario.setCorreo("eduardo@test.com");

        when(usuarioService.findById(1L))
                .thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Eduardo"));
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
        creado.setNombre("Eduardo");
        creado.setCorreo("eduardo@test.com");

        when(usuarioService.save(any()))
                .thenReturn(creado);

        mockMvc.perform(post("/api/v1/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre": "Eduardo",
                          "apellidos": "Hernandez",
                          "correo": "eduardo@test.com",
                          "telefono": 912345678,
                          "password": "clave123"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Eduardo"));
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
        usuario.setNombre("Eduardo");

        when(usuarioService.findByNombre("Eduardo"))
                .thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios/nombre/Eduardo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Eduardo"));
    }

    @Test
    @DisplayName("Debe buscar usuario por correo")
    void debeBuscarUsuarioPorCorreo() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setCorreo("eduardo@test.com");

        when(usuarioService.findByCorreo("eduardo@test.com"))
                .thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/correo/eduardo@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("eduardo@test.com"));
    }

    @Test
    @DisplayName("Debe obtener mi perfil si está autenticado")
    void debeObtenerMiPerfilAutenticado() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setCorreo("eduardo@test.com");

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("eduardo@test.com", null, "ROLE_USER")
        );

        when(usuarioService.findByCorreo("eduardo@test.com"))
                .thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("eduardo@test.com"));
    }

    @Test
    @DisplayName("Debe retornar 401 si no está autenticado al pedir /me")
    void debeRetornarUnauthorizedSiNoEstaAutenticado() throws Exception {
        SecurityContextHolder.clearContext();

        mockMvc.perform(get("/api/v1/usuarios/me"))
                .andExpect(status().isUnauthorized());
    }

    // ================== PAGINADO ==================

    @Test
    @DisplayName("Debe listar usuarios paginados")
    void debeListarUsuariosPaginados() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setNombre("Eduardo");
        usuario.setCorreo("eduardo@test.com");

        var respuesta = new com.example.NoLimits.Multimedia.dto.pagination.PagedResponse<>(
                List.of(usuario),
                1,
                1,
                1
        );

        when(usuarioService.findAllPaged(1, 4)).thenReturn(respuesta);

        mockMvc.perform(get("/api/v1/usuarios/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Eduardo"))
                .andExpect(jsonPath("$.pagina").value(1))
                .andExpect(jsonPath("$.totalPaginas").value(1))
                .andExpect(jsonPath("$.totalElementos").value(1));
    } 

    // ================== CREATE ==================

    @Test
    @DisplayName("Debe crear usuario desde admin")
    void debeCrearUsuarioDesdeAdmin() throws Exception {
        UsuarioResponseDTO creado = new UsuarioResponseDTO();
        creado.setId(20L);
        creado.setNombre("Lucas");
        creado.setCorreo("lucas@example.com");

        when(usuarioService.saveDesdeAdmin(any())).thenReturn(creado);

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "nombre": "Lucas",
                        "apellidos": "Fernández",
                        "correo": "lucas@example.com",
                        "telefono": 912345678,
                        "password": "clave12345",
                        "rolId": 1
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.nombre").value("Lucas"))
                .andExpect(jsonPath("$.correo").value("lucas@example.com"));
    }

    // ================== PUT ==================

    @Test
    @DisplayName("Debe actualizar usuario con PUT")
    void debeActualizarUsuarioConPut() throws Exception {
        UsuarioResponseDTO actualizado = new UsuarioResponseDTO();
        actualizado.setId(1L);
        actualizado.setNombre("Franco");
        actualizado.setCorreo("franco@example.com");

        when(usuarioService.update(eq(1L), any())).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "nombre": "Franco",
                        "apellidos": "Medhurst",
                        "correo": "franco@example.com",
                        "telefono": 912345678,
                        "password": "clave123",
                        "rolId": 1
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Franco"))
                .andExpect(jsonPath("$.correo").value("franco@example.com"));
    }

    // ================== PATCH ==================

    @Test
    @DisplayName("Debe editar usuario parcialmente con PATCH")
    void debeEditarUsuarioParcialmente() throws Exception {
        UsuarioResponseDTO actualizado = new UsuarioResponseDTO();
        actualizado.setId(1L);
        actualizado.setNombre("Eduardo");
        actualizado.setCorreo("nuevo@test.com");

        when(usuarioService.patch(eq(1L), any())).thenReturn(actualizado);

        mockMvc.perform(patch("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "correo": "nuevo@test.com"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("nuevo@test.com"));
    }

    @Test
    @DisplayName("Debe actualizar mi perfil si está autenticado")
    void debeActualizarMiPerfilAutenticado() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(1L);
        usuario.setCorreo("eduardo@test.com");

        UsuarioResponseDTO actualizado = new UsuarioResponseDTO();
        actualizado.setId(1L);
        actualizado.setNombre("Eduardo Actualizado");
        actualizado.setCorreo("eduardo@test.com");

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("eduardo@test.com", null, "ROLE_USER")
        );

        when(usuarioService.findByCorreo("eduardo@test.com")).thenReturn(usuario);
        when(usuarioService.patch(eq(1L), any())).thenReturn(actualizado);

        mockMvc.perform(patch("/api/v1/usuarios/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "nombre": "Eduardo Actualizado"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Eduardo Actualizado"));
    }
    // ================== Casos de error ==================
    @Test
    @DisplayName("Debe retornar 204 cuando no existen usuarios por nombre")
    void debeRetornarNoContentNombre() throws Exception {

        when(usuarioService.findByNombre("Pedro"))
                .thenReturn(List.of());
        
        mockMvc.perform(get("/api/v1/usuarios/nombre/Pedro"))
                .andExpect(status().isNoContent());
    }
    // ================== Actualizar perfil sin autenticación ==================
    @Test
    @DisplayName("Debe reornar 401 al actualizar mi perfil sin autenticación")
    void debeRetornarUnauthorizedActualizarMiPerfil() throws Exception {

        SecurityContextHolder.clearContext();

        mockMvc.perform(patch("/api/v1/usuarios/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "nombre": "Nuevo Nombre"
                        }
                                
                        """))
                .andExpect(status().isUnauthorized());
    }
    //=================== Cambiar contraseña ==================
    @Test
    @DisplayName("Debe cambiar contraseña")
    void debecambiarPassword() throws Exception {

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(
                    "eduardo@test.com",
                    null,
                    "ROLE_USER")
        );

        doNothing().when(usuarioService)
                .cambiarPassword(anyString(), anyString(), anyString());

        mockMvc.perform(patch("/api/v1/usuarios/me/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "passwordActual":"123",
                          "nuevaPassword":"456"
                        }
                        """))
                .andExpect(status().isOk());
     }

     @Test
     @DisplayName("Debe retornar 401 al cambiar contraseña sin autenticación")
     void debeRetornarUnauthorizedCambiarPassword() throws Exception {
        SecurityContextHolder.clearContext();

        mockMvc.perform(patch("/api/v1/usuarios/me/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "passwordActual":"123",
                          "nuevaPassword":"456"
                        }
                        """))
                .andExpect(status().isUnauthorized());  
     }

     @Test
     @DisplayName("Debe eliminar favorito")
     void debeEliminarFavorito() throws Exception {

        doNothing().when(usuarioService)
                .eliminarFavorito(1L, "tmdb:123");

        mockMvc.perform(delete("/api/v1/usuarios/1/favoritos/tmdb:123"))
                .andExpect(status().isNoContent());
     }

}