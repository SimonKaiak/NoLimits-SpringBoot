package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.PlataformaModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.PlataformaControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformaService;

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

@WebMvcTest(PlataformaControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class PlataformaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlataformaService plataformaService;

    @MockBean
    private PlataformaModelAssembler plataformaAssembler;

    private PlataformaResponseDTO dto() {
        PlataformaResponseDTO dto = new PlataformaResponseDTO();
        dto.setId(1L);
        dto.setNombre("PC");
        return dto;
    }

    private EntityModel<PlataformaResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/plataformas/1").withSelfRel());
    }

    @Test
    void getAll_ConDatos_Retorna200() throws Exception {
        when(plataformaService.findAll()).thenReturn(List.of(dto()));
        when(plataformaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/plataformas")).andExpect(status().isOk());
    }

    @Test
    void getAll_SinDatos_Retorna204() throws Exception {
        when(plataformaService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/plataformas")).andExpect(status().isNoContent());
    }

    @Test
    void getById_Existe_Retorna200() throws Exception {
        when(plataformaService.findById(1L)).thenReturn(dto());
        when(plataformaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/plataformas/1")).andExpect(status().isOk());
    }

    @Test
    void getById_NoExiste_Retorna404() throws Exception {
        when(plataformaService.findById(99L)).thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(get("/api/v2/plataformas/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_DatosValidos_Retorna201() throws Exception {
        when(plataformaService.save(any())).thenReturn(dto());
        when(plataformaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/plataformas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"PC\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void update_Existe_Retorna200() throws Exception {
        when(plataformaService.update(eq(1L), any())).thenReturn(dto());
        when(plataformaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(put("/api/v2/plataformas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Xbox\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void update_NoExiste_Retorna404() throws Exception {
        when(plataformaService.update(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(put("/api/v2/plataformas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Xbox\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(plataformaService.patch(eq(1L), any())).thenReturn(dto());
        when(plataformaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/plataformas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"PlayStation\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void patch_NoExiste_Retorna404() throws Exception {
        when(plataformaService.patch(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(patch("/api/v2/plataformas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"PlayStation\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(plataformaService).deleteById(1L);
        mockMvc.perform(delete("/api/v2/plataformas/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(plataformaService).deleteById(99L);
        mockMvc.perform(delete("/api/v2/plataformas/99")).andExpect(status().isNotFound());
    }
}