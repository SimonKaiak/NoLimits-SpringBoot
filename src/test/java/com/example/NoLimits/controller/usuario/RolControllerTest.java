package com.example.NoLimits.controller.usuario;

import com.example.NoLimits.Multimedia.controller.usuario.RolController;
import com.example.NoLimits.Multimedia.dto.usuario.request.RolRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.RolUpdateDTO;
import com.example.NoLimits.Multimedia.service.usuario.RolService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RolControllerTest — Endpoints de gestión de roles")
class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @Test
    @DisplayName("GET /roles → 200 OK con lista de roles")
    void getAll_conRoles_retorna200() throws Exception {
        RolResponseDTO rol = new RolResponseDTO();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);

        when(rolService.findAll()).thenReturn(List.of(rol));

        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ROLE_USER"))
                .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    @DisplayName("GET /roles → 204 No Content cuando no hay roles")
    void getAll_sinRoles_retorna204() throws Exception {
        when(rolService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /roles/{id} → 200 OK con el rol encontrado")
    void getById_existente_retorna200() throws Exception {
        RolResponseDTO rol = new RolResponseDTO();
        rol.setId(1L);
        rol.setNombre("ROLE_ADMIN");
        rol.setDescripcion("Administrador del sistema");
        rol.setActivo(true);

        when(rolService.findById(1L)).thenReturn(rol);

        mockMvc.perform(get("/api/v1/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.descripcion").value("Administrador del sistema"));
    }

    @Test
    @DisplayName("GET /roles/{id} → 404 cuando el rol no existe")
    void getById_noExistente_retorna404() throws Exception {
        when(rolService.findById(99L))
                .thenThrow(new ResponseStatusException(NOT_FOUND, "Rol no encontrado"));

        mockMvc.perform(get("/api/v1/roles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /roles → 201 Created con el rol creado")
    void create_valido_retorna201() throws Exception {
        RolResponseDTO creado = new RolResponseDTO();
        creado.setId(2L);
        creado.setNombre("ROLE_VENDEDOR");
        creado.setActivo(true);

        when(rolService.save(any())).thenReturn(creado);

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"ROLE_VENDEDOR\",\"activo\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("ROLE_VENDEDOR"));
    }

    @Test
    @DisplayName("POST /roles → 400 cuando falta el nombre")
    void create_sinNombre_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"activo\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /roles → 400 cuando falta el campo activo")
    void create_sinActivo_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"ROLE_TEST\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /roles/{id} → 200 OK con el rol actualizado")
    void update_valido_retorna200() throws Exception {
        RolResponseDTO actualizado = new RolResponseDTO();
        actualizado.setId(1L);
        actualizado.setNombre("ROLE_USER_V2");
        actualizado.setActivo(false);

        when(rolService.update(eq(1L), any(RolUpdateDTO.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"ROLE_USER_V2\",\"activo\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ROLE_USER_V2"))
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    @DisplayName("PUT /roles/{id} → 404 cuando el rol no existe")
    void update_noExistente_retorna404() throws Exception {
        when(rolService.update(eq(99L), any(RolUpdateDTO.class)))
                .thenThrow(new ResponseStatusException(NOT_FOUND, "Rol no encontrado"));

        mockMvc.perform(put("/api/v1/roles/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"ROLE_X\",\"activo\":true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /roles/{id} → 200 OK actualizando solo el nombre")
    void patch_soloNombre_retorna200() throws Exception {
        RolResponseDTO parcial = new RolResponseDTO();
        parcial.setId(1L);
        parcial.setNombre("ROLE_MODIFICADO");
        parcial.setActivo(true);

        when(rolService.patch(eq(1L), any(RolUpdateDTO.class))).thenReturn(parcial);

        mockMvc.perform(patch("/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"ROLE_MODIFICADO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ROLE_MODIFICADO"));
    }

    @Test
    @DisplayName("PATCH /roles/{id} → 200 OK actualizando solo activo")
    void patch_soloActivo_retorna200() throws Exception {
        RolResponseDTO parcial = new RolResponseDTO();
        parcial.setId(1L);
        parcial.setNombre("ROLE_USER");
        parcial.setActivo(false);

        when(rolService.patch(eq(1L), any(RolUpdateDTO.class))).thenReturn(parcial);

        mockMvc.perform(patch("/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"activo\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    @DisplayName("PATCH /roles/{id} → 404 cuando el rol no existe")
    void patch_noExistente_retorna404() throws Exception {
        when(rolService.patch(eq(99L), any(RolUpdateDTO.class)))
                .thenThrow(new ResponseStatusException(NOT_FOUND, "Rol no encontrado"));

        mockMvc.perform(patch("/api/v1/roles/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"activo\":true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /roles/{id} → 204 No Content al eliminar")
    void delete_existente_retorna204() throws Exception {
        doNothing().when(rolService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/roles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /roles/{id} → 404 cuando el rol no existe")
    void delete_noExistente_retorna404() throws Exception {
        doThrow(new ResponseStatusException(NOT_FOUND, "Rol no encontrado"))
                .when(rolService).deleteById(99L);

        mockMvc.perform(delete("/api/v1/roles/99"))
                .andExpect(status().isNotFound());
    }
}