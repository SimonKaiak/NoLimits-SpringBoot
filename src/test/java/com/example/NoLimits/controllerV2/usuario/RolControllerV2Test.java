package com.example.NoLimits.controllerV2.usuario;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.usuario.RolModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.usuario.RolControllerV2;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;
import com.example.NoLimits.Multimedia.service.usuario.RolService;

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

@WebMvcTest(RolControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RolControllerV2 Tests")
class RolControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @MockBean
    private RolModelAssembler rolAssembler;

    private RolResponseDTO crearRol() {
        RolResponseDTO dto = new RolResponseDTO();
        dto.setId(1L);
        dto.setNombre("CLIENTE");
        dto.setDescripcion("Rol cliente");
        dto.setActivo(true);
        return dto;
    }

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("Debe listar roles")
        void debeListarRoles() throws Exception {

            RolResponseDTO dto = crearRol();

            when(rolService.findAll())
                    .thenReturn(List.of(dto));

            when(rolAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(get("/api/v2/roles"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe listar vacío")
        void debeListarVacio() throws Exception {

            when(rolService.findAll())
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v2/roles"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe buscar rol por id")
        void debeBuscarPorId() throws Exception {

            RolResponseDTO dto = crearRol();

            when(rolService.findById(1L))
                    .thenReturn(dto);

            when(rolAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(get("/api/v2/roles/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 si no existe")
        void debeRetornar404() throws Exception {

            when(rolService.findById(999L))
                    .thenThrow(new RecursoNoEncontradoException("No encontrado"));

            mockMvc.perform(get("/api/v2/roles/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Creación")
    class Creacion {

        @Test
        @DisplayName("Debe crear rol")
        void debeCrearRol() throws Exception {

            RolResponseDTO dto = crearRol();

            when(rolService.save(any()))
                    .thenReturn(dto);

            when(rolAssembler.toModel(any()))
                    .thenReturn(
                            EntityModel.of(dto)
                                    .add(Link.of(
                                            "http://localhost/api/v2/roles/1",
                                            "self"))
                    );

            mockMvc.perform(post("/api/v2/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "nombre":"CLIENTE",
                                      "descripcion":"Rol cliente",
                                      "activo":true
                                    }
                                    """))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Debe retornar 400 sin nombre")
        void debeRetornar400SinNombre() throws Exception {

            mockMvc.perform(post("/api/v2/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "descripcion":"Rol cliente",
                                      "activo":true
                                    }
                                    """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 sin activo")
        void debeRetornar400SinActivo() throws Exception {

            mockMvc.perform(post("/api/v2/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "nombre":"CLIENTE"
                                    }
                                    """))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Actualización")
    class Actualizacion {

        @Test
        @DisplayName("Debe actualizar rol con PUT")
        void debeActualizarConPut() throws Exception {

            RolResponseDTO dto = crearRol();

            when(rolService.update(eq(1L), any()))
                    .thenReturn(dto);

            when(rolAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(put("/api/v2/roles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "nombre":"ADMIN",
                                      "descripcion":"Administrador",
                                      "activo":true
                                    }
                                    """))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 en PUT")
        void debeRetornar404Put() throws Exception {

            when(rolService.update(eq(999L), any()))
                    .thenThrow(new RecursoNoEncontradoException("No encontrado"));

            mockMvc.perform(put("/api/v2/roles/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "nombre":"ADMIN"
                                    }
                                    """))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe actualizar rol con PATCH")
        void debeActualizarPatch() throws Exception {

            RolResponseDTO dto = crearRol();

            when(rolService.patch(eq(1L), any()))
                    .thenReturn(dto);

            when(rolAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(patch("/api/v2/roles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "nombre":"ADMIN"
                                    }
                                    """))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 en PATCH")
        void debeRetornar404Patch() throws Exception {

            when(rolService.patch(eq(999L), any()))
                    .thenThrow(new RecursoNoEncontradoException("No encontrado"));

            mockMvc.perform(patch("/api/v2/roles/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "nombre":"ADMIN"
                                    }
                                    """))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Eliminación")
    class Eliminacion {

        @Test
        @DisplayName("Debe eliminar rol")
        void debeEliminarRol() throws Exception {

            doNothing()
                    .when(rolService)
                    .deleteById(1L);

            mockMvc.perform(delete("/api/v2/roles/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe retornar 404 al eliminar")
        void debeRetornar404Delete() throws Exception {

            doThrow(new RecursoNoEncontradoException("No encontrado"))
                    .when(rolService)
                    .deleteById(999L);

            mockMvc.perform(delete("/api/v2/roles/999"))
                    .andExpect(status().isNotFound());
        }
    }
}
    

