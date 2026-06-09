package com.example.NoLimits.controller.usuario;

import com.example.NoLimits.Multimedia.config.AdminInitializer;
import com.example.NoLimits.Multimedia.controller.usuario.UsuarioController;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;
import com.example.NoLimits.Multimedia.model.usuario.FavoritoModel;
import com.example.NoLimits.Multimedia.service.usuario.UsuarioService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;      // <-- agregar
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

    @Nested
    @DisplayName("Listar Usuarios")
    class ListarUsuariosTests {

        @Test
        @DisplayName("Debe listar usuarios correctamente")
        void debeListarUsuariosCorrectamente() throws Exception {

            // Arrange
            UsuarioResponseDTO usuario = new UsuarioResponseDTO();
            usuario.setId(1L);
            usuario.setNombre("Eduardo");
            usuario.setApellidos("Hernandez");
            usuario.setCorreo("eduardo@test.com");

            when(usuarioService.findAll())
                    .thenReturn(List.of(usuario));

            // Act & Assert
            mockMvc.perform(get("/api/v1/usuarios"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nombre").value("Eduardo"))
                    .andExpect(jsonPath("$[0].correo").value("eduardo@test.com"));
        }

        @Test
        @DisplayName("Debe retornar 204 si no hay usuarios")
        void debeRetornarNoContentSiNoHayUsuarios() throws Exception {

            // Arrange
            when(usuarioService.findAll())
                    .thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get("/api/v1/usuarios"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Buscar Usuarios")
    class BuscarUsuariosTests {

        @Test
        @DisplayName("Debe buscar usuario por ID")
        void debeBuscarUsuarioPorId() throws Exception {

                // Arrange
                UsuarioResponseDTO usuario = new UsuarioResponseDTO();
                usuario.setId(1L);
                usuario.setNombre("Eduardo");
                usuario.setCorreo("eduardo@test.com");

                when(usuarioService.findById(1L))
                        .thenReturn(usuario);

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.nombre").value("Eduardo"));
        }

        @Test
        @DisplayName("Debe buscar usuarios por nombre")
        void debeBuscarUsuariosPorNombre() throws Exception {

                // Arrange
                UsuarioResponseDTO usuario = new UsuarioResponseDTO();
                usuario.setId(1L);
                usuario.setNombre("Eduardo");

                when(usuarioService.findByNombre("Eduardo"))
                        .thenReturn(List.of(usuario));

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/nombre/Eduardo"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].nombre").value("Eduardo"));
        }

        @Test
        @DisplayName("Debe buscar usuario por correo")
        void debeBuscarUsuarioPorCorreo() throws Exception {

                // Arrange
                UsuarioResponseDTO usuario = new UsuarioResponseDTO();
                usuario.setId(1L);
                usuario.setCorreo("eduardo@test.com");

                when(usuarioService.findByCorreo("eduardo@test.com"))
                .thenReturn(usuario);

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/correo/eduardo@test.com"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.correo").value("eduardo@test.com"));
        }

        @Test
        @DisplayName("Debe retornar 204 cuando no existen usuarios por nombre")
        void debeRetornarNoContentNombre() throws Exception {

                // Arrange
                when(usuarioService.findByNombre("Pedro"))
                        .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/nombre/Pedro"))
                        .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Favoritos")
    class FavoritosTests {

        @Test
        @DisplayName("Debe agregar favorito al usuario")
        void debeAgregarFavorito() throws Exception {

                // Arrange
                FavoritoModel favorito = new FavoritoModel();

                when(usuarioService.agregarFavorito(eq(1L), any()))
                        .thenReturn(favorito);

                // Act & Assert
                mockMvc.perform(post("/api/v1/usuarios/1/favoritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "obraId":"tmdb:movie:550"
                                }
                                """))
                .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Debe obtener favoritos por usuario")
        void debeObtenerFavoritosPorUsuario() throws Exception {

                // Arrange
                when(usuarioService.obtenerFavoritosPorUsuario(1L))
                        .thenReturn(List.of(new FavoritoModel()));

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/1/favoritos"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe eliminar favorito del usuario")
        void debeEliminarFavoritoDelUsuario() throws Exception {

                // Arrange
                doNothing().when(usuarioService)
                        .eliminarFavorito(1L, "tmdb:movie:1893");

                // Act & Assert
                mockMvc.perform(delete("/api/v1/usuarios/1/favoritos/tmdb:movie:1893"))
                        .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe eliminar favorito")
        void debeEliminarFavorito() throws Exception {

                // Arrange
                doNothing().when(usuarioService)
                        .eliminarFavorito(1L, "tmdb:123");

                // Act & Assert
                mockMvc.perform(delete("/api/v1/usuarios/1/favoritos/tmdb:123"))
                .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Compras Usuario")
    class ComprasUsuarioTests {

        @Test
        @DisplayName("Debe obtener detalle de compras del usuario")
        void debeObtenerDetalleComprasUsuario() throws Exception {

                // Arrange
                when(usuarioService.obtenerDetalleUsuario(1L))
                        .thenReturn(Map.of("totalCompras", 2));

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/1/compras"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.totalCompras").value(2));
        }
    }

    @Nested
    @DisplayName("Registro Usuario")
    class RegistroUsuarioTests {

        @Test
        @DisplayName("Debe registrar usuario público")
        void debeRegistrarUsuarioPublico() throws Exception {

                // Arrange
                UsuarioResponseDTO creado = new UsuarioResponseDTO();
                creado.setId(10L);
                creado.setNombre("Eduardo");
                creado.setCorreo("eduardo@test.com");

                when(usuarioService.save(any()))
                        .thenReturn(creado);

                // Act & Assert
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
    }

    @Nested
    @DisplayName("Perfil Usuario")
    class PerfilUsuarioTests {

        @Test
        @DisplayName("Debe obtener mi perfil si está autenticado")
        void debeObtenerMiPerfilAutenticado() throws Exception {

                // Arrange
                UsuarioResponseDTO usuario = new UsuarioResponseDTO();
                usuario.setId(1L);
                usuario.setCorreo("eduardo@test.com");

                SecurityContextHolder.getContext().setAuthentication(
                        new TestingAuthenticationToken(
                                "eduardo@test.com",
                                null,
                                "ROLE_USER")
                );

                when(usuarioService.findByCorreo("eduardo@test.com"))
                        .thenReturn(usuario);

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/me"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.correo").value("eduardo@test.com"));
        }

        @Test
        @DisplayName("Debe retornar 401 si no está autenticado al pedir /me")
        void debeRetornarUnauthorizedSiNoEstaAutenticado() throws Exception {

                // Arrange
                SecurityContextHolder.clearContext();

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/me"))
                        .andExpect(status().isUnauthorized());
        }

         @Test
        @DisplayName("Debe actualizar mi perfil si está autenticado")
        void debeActualizarMiPerfilAutenticado() throws Exception {

                // Arrange
                UsuarioResponseDTO usuario = new UsuarioResponseDTO();
                usuario.setId(1L);
                usuario.setCorreo("eduardo@test.com");

                UsuarioResponseDTO actualizado = new UsuarioResponseDTO();
                actualizado.setId(1L);
                actualizado.setNombre("Eduardo Actualizado");
                actualizado.setCorreo("eduardo@test.com");

                SecurityContextHolder.getContext().setAuthentication(
                        new TestingAuthenticationToken(
                                "eduardo@test.com",
                                null,
                                "ROLE_USER")
                );

                when(usuarioService.findByCorreo("eduardo@test.com"))
                        .thenReturn(usuario);

                when(usuarioService.patch(eq(1L), any()))
                        .thenReturn(actualizado);

                // Act & Assert
                mockMvc.perform(patch("/api/v1/usuarios/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "nombre": "Eduardo Actualizado"
                                        }
                                        """))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.nombre")
                                .value("Eduardo Actualizado"));
        }

        @Test
        @DisplayName("Debe retornar 401 al actualizar mi perfil sin autenticación")
        void debeRetornarUnauthorizedActualizarMiPerfil() throws Exception {

                // Arrange
                SecurityContextHolder.clearContext();

                // Act & Assert
                mockMvc.perform(patch("/api/v1/usuarios/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "nombre": "Nuevo Nombre"
                                        }
                                        """))
                        .andExpect(status().isUnauthorized());
        }
        @Test
        @DisplayName("Debe retornar 401 al actualizar perfil cuando principal es anonymousUser autenticado")
        void debeRetornarUnauthorizedActualizarPerfilAnonymousAutenticado() throws Exception {

                // Arrange
                TestingAuthenticationToken auth =
                        new TestingAuthenticationToken(
                                "anonymousUser",
                                null,
                                 "ROLE_USER");

                SecurityContextHolder.getContext()
                        .setAuthentication(auth);

                // Act & Assert
                mockMvc.perform(patch("/api/v1/usuarios/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "nombre":"Nuevo"
                                        }
                                        """))
                        .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe cambiar correo correctamente")
        void debeCambiarCorreo() throws Exception {

                // Arrange  
                TestingAuthenticationToken authentication =
                        new TestingAuthenticationToken(
                                "eduardo@test.com",
                               null,
                                "ROLE_USER");

                doNothing().when(usuarioService)
                        .cambiarCorreo(
                                anyString(),
                                anyString(),
                                anyString());

                // Act & Assert
                mockMvc.perform(patch("/api/v1/usuarios/cambiar-correo")
                                .principal(authentication)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "nuevoCorreo":"nuevo@test.com",
                                        "passwordActual":"123456"
                                        }
                                        """))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe eliminar cuenta correctamente")
        void debeEliminarCuenta() throws Exception {

                // Arrange
                TestingAuthenticationToken authentication =
                new TestingAuthenticationToken(
                        "eduardo@test.com",
                        null,
                        "ROLE_USER");

                doNothing().when(usuarioService)
                        .eliminarCuenta("eduardo@test.com");

                // Act & Assert
        mockMvc.perform(delete("/api/v1/usuarios/eliminar-cuenta")
                        .principal(authentication))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 401 cuando principal es anonymousUser")
        void debeRetornarUnauthorizedCuandoPrincipalEsAnonymous() throws Exception {

                // Arrange
                SecurityContextHolder.getContext().setAuthentication(
                        new TestingAuthenticationToken(
                                "anonymousUser",
                                null)
                );

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/me"))
                        .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 401 al actualizar perfil con anonymousUser")
        void debeRetornarUnauthorizedActualizarPerfilAnonymous() throws Exception {

                // Arrange
                SecurityContextHolder.getContext().setAuthentication(
                        new TestingAuthenticationToken(
                                "anonymousUser",
                                null)
                );

                // Act & Assert
                mockMvc.perform(patch("/api/v1/usuarios/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "nombre":"Nuevo"
                                        }
                                        """))
                        .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 401 cuando auth no está autenticado")
        void debeRetornarUnauthorizedAuthNoAutenticado() throws Exception {

                // Arrange
                TestingAuthenticationToken auth =
                        new TestingAuthenticationToken(
                                "eduardo@test.com",
                                null);

                auth.setAuthenticated(false);

                SecurityContextHolder.getContext()
                        .setAuthentication(auth);

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/me"))
                        .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 401 cuando auth existe, está autenticado y principal es anonymousUser")
        void debeRetornarUnauthorizedAnonymousAutenticado() throws Exception {

                // Arrange
                TestingAuthenticationToken auth =
                        new TestingAuthenticationToken(
                                "anonymousUser",
                                null,
                                "ROLE_USER");

                SecurityContextHolder.getContext()
                        .setAuthentication(auth);

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/me"))
                        .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Eliminar Usuario")
    class EliminarUsuarioTests {

        @Test
        @DisplayName("Debe eliminar usuario por ID")
        void debeEliminarUsuarioPorId() throws Exception {

                // Arrange
                doNothing().when(usuarioService)
                        .deleteById(1L);

                // Act & Assert
                mockMvc.perform(delete("/api/v1/usuarios/1"))
                        .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Paginación")
    class PaginacionTests {

        @Test
        @DisplayName("Debe listar usuarios paginados")
        void debeListarUsuariosPaginados() throws Exception {

                // Arrange
                UsuarioResponseDTO usuario = new UsuarioResponseDTO();
                usuario.setId(1L);
                usuario.setNombre("Eduardo");
                usuario.setCorreo("eduardo@test.com");

                var respuesta =
                        new com.example.NoLimits.Multimedia.dto.pagination.PagedResponse<>(
                                List.of(usuario),
                                1,
                                1,
                                1
                        );

                when(usuarioService.findAllPaged(1, 4))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/usuarios/paginado"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.contenido[0].nombre")
                                .value("Eduardo"))
                        .andExpect(jsonPath("$.pagina")
                                .value(1))
                        .andExpect(jsonPath("$.totalPaginas")
                                .value(1))
                        .andExpect(jsonPath("$.totalElementos")
                                .value(1));
        }
    }

    @Nested
    @DisplayName("Crear Usuario")
    class CrearUsuarioTests {

        @Test
        @DisplayName("Debe crear usuario desde admin")
        void debeCrearUsuarioDesdeAdmin() throws Exception {

                // Arrange
                UsuarioResponseDTO creado = new UsuarioResponseDTO();
                creado.setId(20L);
                creado.setNombre("Lucas");
                creado.setCorreo("lucas@example.com");

                when(usuarioService.saveDesdeAdmin(any()))
                        .thenReturn(creado);

                // Act & Assert
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
    }

    @Nested
    @DisplayName("Actualizar Usuario")
    class ActualizarUsuarioTests {

        @Test
        @DisplayName("Debe actualizar usuario con PUT")
        void debeActualizarUsuarioConPut() throws Exception {

                // Arrange
                UsuarioResponseDTO actualizado = new UsuarioResponseDTO();
                actualizado.setId(1L);
                actualizado.setNombre("Franco");
                actualizado.setCorreo("franco@example.com");

                when(usuarioService.update(eq(1L), any()))
                        .thenReturn(actualizado);

                // Act & Assert
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
                        .andExpect(jsonPath("$.nombre")
                                .value("Franco"))
                        .andExpect(jsonPath("$.correo")
                                .value("franco@example.com"));
        }

        @Test
        @DisplayName("Debe editar usuario parcialmente con PATCH")
        void debeEditarUsuarioParcialmente() throws Exception {

                // Arrange
                UsuarioResponseDTO actualizado = new UsuarioResponseDTO();
                actualizado.setId(1L);
                actualizado.setNombre("Eduardo");
                actualizado.setCorreo("nuevo@test.com");

                when(usuarioService.patch(eq(1L), any()))
                        .thenReturn(actualizado);

                // Act & Assert
                mockMvc.perform(patch("/api/v1/usuarios/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "correo": "nuevo@test.com"
                                        }
                                        """))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.correo")
                                .value("nuevo@test.com"));
        }

    }

    @Nested
    @DisplayName("Cambiar Contraseña")
    class CambiarPasswordTests {

        @Test
        @DisplayName("Debe cambiar contraseña")
        void debecambiarPassword() throws Exception {

                // Arrange
                SecurityContextHolder.getContext().setAuthentication(
                        new TestingAuthenticationToken(
                                "eduardo@test.com",
                                null,
                                "ROLE_USER")
                );

                doNothing().when(usuarioService)
                        .cambiarPassword(anyString(), anyString(), anyString());

                // Act & Assert
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

                // Arrange
                SecurityContextHolder.clearContext();

                // Act & Assert
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
        @DisplayName("Debe retornar 401 cuando principal es anonymousUser al cambiar password")
        void debeRetornarUnauthorizedCambiarPasswordAnonymous() throws Exception {

                // Arrange
                SecurityContextHolder.getContext().setAuthentication(
                        new TestingAuthenticationToken(
                                "anonymousUser",
                                null)
                );

                // Act & Assert
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
        @DisplayName("Debe retornar 401 al cambiar password cuando principal es anonymousUser autenticado")
        void debeRetornarUnauthorizedPasswordAnonymousAutenticado() throws Exception {

                // Arrange
                TestingAuthenticationToken auth =
                        new TestingAuthenticationToken(
                                "anonymousUser",
                                null,
                                "ROLE_USER");

                SecurityContextHolder.getContext()
                        .setAuthentication(auth);

                // Act & Assert
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
    }
    
}




    

   

    
     
