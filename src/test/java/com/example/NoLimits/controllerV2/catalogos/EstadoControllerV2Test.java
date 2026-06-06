package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.EstadoModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.EstadoControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.EstadoService;

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

@WebMvcTest(EstadoControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class EstadoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadoService estadoService;

    @MockBean
    private EstadoModelAssembler assembler;

    private EstadoResponseDTO dto() {
        EstadoResponseDTO dto = new EstadoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Test");
        return dto;
    }

    private EntityModel<EstadoResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/estados/1").withSelfRel());
    }

    @Test
    void getAll_ConDatos_Retorna200() throws Exception {
        when(estadoService.findAll()).thenReturn(List.of(dto()));
        when(assembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/estados")).andExpect(status().isOk());
    }

    @Test
    void getAll_SinDatos_Retorna204() throws Exception {
        when(estadoService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/estados")).andExpect(status().isNoContent());
    }

    @Test
    void getById_Existe_Retorna200() throws Exception {
        when(estadoService.findById(1L)).thenReturn(dto());
        when(assembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/estados/1")).andExpect(status().isOk());
    }

    @Test
    void getById_NoExiste_Retorna404() throws Exception {
        when(estadoService.findById(99L)).thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(get("/api/v2/estados/99")).andExpect(status().isNotFound());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(estadoService.patch(eq(1L), any())).thenReturn(dto());
        when(assembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/estados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Parcial\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(estadoService).deleteById(1L);
        mockMvc.perform(delete("/api/v2/estados/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(estadoService).deleteById(99L);
        mockMvc.perform(delete("/api/v2/estados/99")).andExpect(status().isNotFound());
    }
}