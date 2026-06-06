package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.MetodoEnvioModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.MetodoEnvioControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoEnvioService;

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

@WebMvcTest(MetodoEnvioControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class MetodoEnvioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetodoEnvioService metodoEnvioService;

    @MockBean
    private MetodoEnvioModelAssembler metodoEnvioAssembler;

    private MetodoEnvioResponseDTO dto() {
        MetodoEnvioResponseDTO dto = new MetodoEnvioResponseDTO();
        dto.setId(1L);
        dto.setNombre("Test");
        return dto;
    }

    private EntityModel<MetodoEnvioResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/metodos-envio/1").withSelfRel());
    }

    @Test
    void getAll_ConDatos_Retorna200() throws Exception {
        when(metodoEnvioService.findAll()).thenReturn(List.of(dto()));
        when(metodoEnvioAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/metodos-envio")).andExpect(status().isOk());
    }

    @Test
    void getAll_SinDatos_Retorna204() throws Exception {
        when(metodoEnvioService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/metodos-envio")).andExpect(status().isNoContent());
    }

    @Test
    void getById_Existe_Retorna200() throws Exception {
        when(metodoEnvioService.findById(1L)).thenReturn(dto());
        when(metodoEnvioAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/metodos-envio/1")).andExpect(status().isOk());
    }

    @Test
    void getById_NoExiste_Retorna404() throws Exception {
        when(metodoEnvioService.findById(99L)).thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(get("/api/v2/metodos-envio/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_DatosValidos_Retorna201() throws Exception {
        when(metodoEnvioService.create(any())).thenReturn(dto());
        when(metodoEnvioAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/metodos-envio")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Test\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void update_Existe_Retorna200() throws Exception {
        when(metodoEnvioService.update(eq(1L), any())).thenReturn(dto());
        when(metodoEnvioAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(put("/api/v2/metodos-envio/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Nuevo\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(metodoEnvioService.patch(eq(1L), any())).thenReturn(dto());
        when(metodoEnvioAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/metodos-envio/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Parcial\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(metodoEnvioService).deleteById(1L);
        mockMvc.perform(delete("/api/v2/metodos-envio/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(metodoEnvioService).deleteById(99L);
        mockMvc.perform(delete("/api/v2/metodos-envio/99")).andExpect(status().isNotFound());
    }
}