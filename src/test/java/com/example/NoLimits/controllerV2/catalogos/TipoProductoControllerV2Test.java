package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.TipoProductoModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TipoProductoControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoProductoResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TipoProductoService;

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

@WebMvcTest(TipoProductoControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class TipoProductoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TipoProductoService tipoProductoService;

    @MockBean
    private TipoProductoModelAssembler tipoProductoAssembler;

    private TipoProductoResponseDTO dto() {
        TipoProductoResponseDTO dto = new TipoProductoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Test");
        dto.setDescripcion("Desc");
        dto.setActivo(true);
        return dto;
    }

    private EntityModel<TipoProductoResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/tipos-producto/1").withSelfRel());
    }

    @Test
    void getAll_ConDatos_Retorna200() throws Exception {
        when(tipoProductoService.findAll()).thenReturn(List.of(dto()));
        when(tipoProductoAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/tipos-producto")).andExpect(status().isOk());
    }

    @Test
    void getAll_SinDatos_Retorna204() throws Exception {
        when(tipoProductoService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/tipos-producto")).andExpect(status().isNoContent());
    }

    @Test
    void getById_Existe_Retorna200() throws Exception {
        when(tipoProductoService.findById(1L)).thenReturn(dto());
        when(tipoProductoAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/tipos-producto/1")).andExpect(status().isOk());
    }

    @Test
    void getById_NoExiste_Retorna404() throws Exception {
        when(tipoProductoService.findById(99L)).thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(get("/api/v2/tipos-producto/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_DatosValidos_Retorna201() throws Exception {
        when(tipoProductoService.save(any())).thenReturn(dto());
        when(tipoProductoAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/tipos-producto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Test\",\"descripcion\":\"Desc\",\"activo\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    void update_DatosCompletos_Retorna200() throws Exception {
        when(tipoProductoService.update(eq(1L), any())).thenReturn(dto());
        when(tipoProductoAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(put("/api/v2/tipos-producto/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Nuevo\",\"descripcion\":\"Desc nueva\",\"activo\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    void update_DatosIncompletos_Retorna400() throws Exception {
        // PUT without descripcion and activo should fail the guard
        mockMvc.perform(put("/api/v2/tipos-producto/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Incompleto\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_NoExiste_Retorna404() throws Exception {
        when(tipoProductoService.update(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(put("/api/v2/tipos-producto/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Nuevo\",\"descripcion\":\"Desc\",\"activo\":true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(tipoProductoService.patch(eq(1L), any())).thenReturn(dto());
        when(tipoProductoAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/tipos-producto/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Parcial\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void patch_NoExiste_Retorna404() throws Exception {
        when(tipoProductoService.patch(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(patch("/api/v2/tipos-producto/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Parcial\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(tipoProductoService).deleteById(1L);
        mockMvc.perform(delete("/api/v2/tipos-producto/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(tipoProductoService).deleteById(99L);
        mockMvc.perform(delete("/api/v2/tipos-producto/99")).andExpect(status().isNotFound());
    }
}