package com.example.NoLimits.controllerV2.usuario;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.usuario.RolModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.usuario.RolControllerV2;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;
import com.example.NoLimits.Multimedia.service.usuario.RolService;

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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class RolControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @MockBean
    private RolModelAssembler rolAssembler;

    private RolResponseDTO dto() {
        RolResponseDTO dto = new RolResponseDTO();
        dto.setId(1L);
        dto.setNombre("CLIENTE");
        dto.setDescripcion("Rol por defecto");
        dto.setActivo(true);
        return dto;
    }

    private EntityModel<RolResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/roles/1").withSelfRel());
    }

    @Test
    void getAll_ConDatos_Retorna200() throws Exception {
        when(rolService.findAll()).thenReturn(List.of(dto()));
        when(rolAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/roles")).andExpect(status().isOk());
    }

    @Test
    void getById_Existe_Retorna200() throws Exception {
        when(rolService.findById(1L)).thenReturn(dto());
        when(rolAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/roles/1")).andExpect(status().isOk());
    }

    @Test
    void getById_NoExiste_Retorna404() throws Exception {
        when(rolService.findById(99L)).thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(get("/api/v2/roles/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_DatosValidos_Retorna201() throws Exception {
        when(rolService.save(any())).thenReturn(dto());
        when(rolAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"CLIENTE\",\"descripcion\":\"Rol por defecto\",\"activo\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    void update_Existe_Retorna200() throws Exception {
        when(rolService.update(eq(1L), any())).thenReturn(dto());
        when(rolAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(put("/api/v2/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"ADMIN\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(rolService.patch(eq(1L), any())).thenReturn(dto());
        when(rolAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"PARCIAL\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(rolService).deleteById(1L);
        mockMvc.perform(delete("/api/v2/roles/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(rolService).deleteById(99L);
        mockMvc.perform(delete("/api/v2/roles/99")).andExpect(status().isNotFound());
    }
}