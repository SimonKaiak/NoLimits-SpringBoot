package com.example.NoLimits.controllerV2.usuario;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.usuario.UsuarioModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.usuario.UsuarioControllerV2;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;
import com.example.NoLimits.Multimedia.service.usuario.UsuarioService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UsuarioControllerV2 Tests")
class UsuarioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioModelAssembler usuarioAssembler;

    private UsuarioResponseDTO crearUsuario() {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(1L);
        dto.setNombre("Eduardo");
        dto.setApellidos("Hernandez");
        dto.setCorreo("eduardo@test.com");
        dto.setTelefono(912345678L);
        return dto;
    }

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("Debe listar usuarios")
        void debeListarUsuarios() throws Exception {

            UsuarioResponseDTO dto = crearUsuario();

            when(usuarioService.findAll())
                    .thenReturn(List.of(dto));

            when(usuarioAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(get("/api/v2/usuarios"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando no existen usuarios")
        void debeRetornarNoContent() throws Exception {

            when(usuarioService.findAll())
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v2/usuarios"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe buscar usuario por ID")
        void debeBuscarUsuarioPorId() throws Exception {

            UsuarioResponseDTO dto = crearUsuario();

            when(usuarioService.findById(1L))
                    .thenReturn(dto);

            when(usuarioAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(get("/api/v2/usuarios/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 cuando usuario no existe")
        void debeRetornar404() throws Exception {

            when(usuarioService.findById(999L))
                    .thenThrow(new RecursoNoEncontradoException("No encontrado"));

            mockMvc.perform(get("/api/v2/usuarios/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Registro")
    class Registro {

        @Test
        @DisplayName("Debe registrar usuario público")
        void debeRegistrarUsuario() throws Exception {

            UsuarioResponseDTO dto = crearUsuario();

            when(usuarioService.save(any()))
                    .thenReturn(dto);

            when(usuarioAssembler.toModel(any()))
                    .thenReturn(
                            EntityModel.of(dto)
                                    .add(Link.of(
                                            "http://localhost/api/v2/usuarios/1",
                                            "self"))
                    );

            mockMvc.perform(post("/api/v2/usuarios/registro")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "nombre":"Eduardo",
                              "apellidos":"Hernandez",
                              "correo":"eduardo@test.com",
                              "telefono":912345678,
                              "password":"clave12345"
                            }
                            """))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Creación")
    class Creacion {

        @Test
        @DisplayName("Debe crear usuario desde admin")
        void debeCrearUsuarioDesdeAdmin() throws Exception {

            UsuarioResponseDTO dto = crearUsuario();

            when(usuarioService.saveDesdeAdmin(any()))
                    .thenReturn(dto);

            when(usuarioAssembler.toModel(any()))
                    .thenReturn(
                            EntityModel.of(dto)
                                    .add(Link.of(
                                            "http://localhost/api/v2/usuarios/1",
                                            "self"))
                    );

            mockMvc.perform(post("/api/v2/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "nombre":"Eduardo",
                              "apellidos":"Hernandez",
                              "correo":"eduardo@test.com",
                              "telefono":912345678,
                              "password":"clave12345",
                              "rolId":1
                            }
                            """))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Actualización")
    class Actualizacion {

        @Test
        @DisplayName("Debe actualizar usuario con PUT")
        void debeActualizarPut() throws Exception {

            UsuarioResponseDTO dto = crearUsuario();

            when(usuarioService.update(eq(1L), any()))
                    .thenReturn(dto);

            when(usuarioAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(put("/api/v2/usuarios/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "nombre":"Eduardo Actualizado"
                            }
                            """))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe actualizar usuario con PATCH")
        void debeActualizarPatch() throws Exception {

            UsuarioResponseDTO dto = crearUsuario();

            when(usuarioService.patch(eq(1L), any()))
                    .thenReturn(dto);

            when(usuarioAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(patch("/api/v2/usuarios/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "correo":"nuevo@test.com"
                            }
                            """))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 en PUT")
        void debeRetornar404Put() throws Exception {

            when(usuarioService.update(eq(999L), any()))
                    .thenThrow(new RecursoNoEncontradoException("No encontrado"));

            mockMvc.perform(put("/api/v2/usuarios/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "nombre":"X"
                            }
                            """))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe retornar 404 en PATCH")
        void debeRetornar404Patch() throws Exception {

            when(usuarioService.patch(eq(999L), any()))
                    .thenThrow(new RecursoNoEncontradoException("No encontrado"));

            mockMvc.perform(patch("/api/v2/usuarios/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "nombre":"X"
                            }
                            """))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Eliminación")
    class Eliminacion {

        @Test
        @DisplayName("Debe eliminar usuario")
        void debeEliminarUsuario() throws Exception {

            doNothing().when(usuarioService)
                    .deleteById(1L);

            mockMvc.perform(delete("/api/v2/usuarios/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe retornar 404 al eliminar")
        void debeRetornar404Delete() throws Exception {

            doThrow(new RecursoNoEncontradoException("No encontrado"))
                    .when(usuarioService)
                    .deleteById(999L);

            mockMvc.perform(delete("/api/v2/usuarios/999"))
                    .andExpect(status().isNotFound());
        }
    }
}