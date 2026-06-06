package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.TipoDeDesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TipoDeDesarrolladorControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TipoDeDesarrolladorService;

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

@WebMvcTest(TipoDeDesarrolladorControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class TipoDeDesarrolladorControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TipoDeDesarrolladorService tipoDeDesarrolladorService;

    @MockBean
    private TipoDeDesarrolladorModelAssembler tipoDeDesarrolladorAssembler;

    private TipoDeDesarrolladorResponseDTO dto() {
        TipoDeDesarrolladorResponseDTO dto = new TipoDeDesarrolladorResponseDTO();
        dto.setId(1L);
        dto.setNombre("Test");
        return dto;
    }

    private EntityModel<TipoDeDesarrolladorResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/tipos-desarrollador/1").withSelfRel());
    }

    @Test
    void getAll_ConDatos_Retorna200() throws Exception {
        when(tipoDeDesarrolladorService.findAll()).thenReturn(List.of(dto()));
        when(tipoDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/tipos-desarrollador")).andExpect(status().isOk());
    }

    @Test
    void getAll_SinDatos_Retorna204() throws Exception {
        when(tipoDeDesarrolladorService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/tipos-desarrollador")).andExpect(status().isNoContent());
    }

    @Test
    void getById_Existe_Retorna200() throws Exception {
        when(tipoDeDesarrolladorService.findById(1L)).thenReturn(dto());
        when(tipoDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/tipos-desarrollador/1")).andExpect(status().isOk());
    }

    @Test
    void getById_NoExiste_Retorna404() throws Exception {
        when(tipoDeDesarrolladorService.findById(99L)).thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(get("/api/v2/tipos-desarrollador/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_DatosValidos_Retorna201() throws Exception {
        when(tipoDeDesarrolladorService.save(any())).thenReturn(dto());
        when(tipoDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/tipos-desarrollador")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Test\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void update_Existe_Retorna200() throws Exception {
        when(tipoDeDesarrolladorService.update(eq(1L), any())).thenReturn(dto());
        when(tipoDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(put("/api/v2/tipos-desarrollador/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Nuevo\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(tipoDeDesarrolladorService.patch(eq(1L), any())).thenReturn(dto());
        when(tipoDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/tipos-desarrollador/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Parcial\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(tipoDeDesarrolladorService).deleteById(1L);
        mockMvc.perform(delete("/api/v2/tipos-desarrollador/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(tipoDeDesarrolladorService).deleteById(99L);
        mockMvc.perform(delete("/api/v2/tipos-desarrollador/99")).andExpect(status().isNotFound());
    }
}