package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.ClasificacionModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.ClasificacionControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.ClasificacionResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.ClasificacionService;

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

@WebMvcTest(ClasificacionControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class ClasificacionControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClasificacionService clasificacionService;

    @MockBean
    private ClasificacionModelAssembler clasificacionAssembler;

    private ClasificacionResponseDTO dto() {
        ClasificacionResponseDTO dto = new ClasificacionResponseDTO();
        dto.setId(1L);
        dto.setNombre("Test");
        return dto;
    }

    private EntityModel<ClasificacionResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/clasificaciones/1").withSelfRel());
    }

    @Test
    void getAll_ConDatos_Retorna200() throws Exception {
        when(clasificacionService.findAll()).thenReturn(List.of(dto()));
        when(clasificacionAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/clasificaciones")).andExpect(status().isOk());
    }

    @Test
    void getAll_SinDatos_Retorna204() throws Exception {
        when(clasificacionService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/clasificaciones")).andExpect(status().isNoContent());
    }

    @Test
    void getById_Existe_Retorna200() throws Exception {
        when(clasificacionService.findById(1L)).thenReturn(dto());
        when(clasificacionAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/clasificaciones/1")).andExpect(status().isOk());
    }

    @Test
    void getById_NoExiste_Retorna404() throws Exception {
        when(clasificacionService.findById(99L)).thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(get("/api/v2/clasificaciones/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_DatosValidos_Retorna201() throws Exception {
        when(clasificacionService.create(any())).thenReturn(dto());
        when(clasificacionAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/clasificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Test\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void update_Existe_Retorna200() throws Exception {
        when(clasificacionService.update(eq(1L), any())).thenReturn(dto());
        when(clasificacionAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(put("/api/v2/clasificaciones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Nuevo\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(clasificacionService.patch(eq(1L), any())).thenReturn(dto());
        when(clasificacionAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/clasificaciones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Parcial\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(clasificacionService).deleteById(1L);
        mockMvc.perform(delete("/api/v2/clasificaciones/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(clasificacionService).deleteById(99L);
        mockMvc.perform(delete("/api/v2/clasificaciones/99")).andExpect(status().isNotFound());
    }
}