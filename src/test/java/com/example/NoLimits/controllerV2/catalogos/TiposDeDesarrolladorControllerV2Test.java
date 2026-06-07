package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.TiposDeDesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TiposDeDesarrolladorControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TiposDeDesarrolladorService;

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

@WebMvcTest(TiposDeDesarrolladorControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class TiposDeDesarrolladorControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TiposDeDesarrolladorService tiposDeDesarrolladorService;

    @MockBean
    private TiposDeDesarrolladorModelAssembler tiposDeDesarrolladorAssembler;

    private TiposDeDesarrolladorResponseDTO dto() {
        TiposDeDesarrolladorResponseDTO dto = new TiposDeDesarrolladorResponseDTO();
        dto.setId(1L);
        dto.setDesarrolladorId(5L);
        dto.setTipoDeDesarrolladorId(2L);
        return dto;
    }

    private EntityModel<TiposDeDesarrolladorResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/desarrolladores/5/tipos/1").withSelfRel());
    }

    @Test
    void listar_ConDatos_Retorna200() throws Exception {
        when(tiposDeDesarrolladorService.findByDesarrollador(5L)).thenReturn(List.of(dto()));
        when(tiposDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/desarrolladores/5/tipos"))
                .andExpect(status().isOk());
    }

    @Test
    void listar_SinDatos_Retorna204() throws Exception {
        when(tiposDeDesarrolladorService.findByDesarrollador(5L)).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/desarrolladores/5/tipos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_DatosValidos_Retorna201() throws Exception {
        when(tiposDeDesarrolladorService.link(5L, 2L)).thenReturn(dto());
        when(tiposDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/desarrolladores/5/tipos/2"))
                .andExpect(status().isCreated());
    }

    @Test
    void link_NoExiste_Retorna404() throws Exception {
        when(tiposDeDesarrolladorService.link(5L, 99L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(post("/api/v2/desarrolladores/5/tipos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void link_YaVinculado_Retorna400() throws Exception {
        when(tiposDeDesarrolladorService.link(5L, 2L))
                .thenThrow(new IllegalStateException("Ya vinculado"));
        mockMvc.perform(post("/api/v2/desarrolladores/5/tipos/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patch_DatosValidos_Retorna200() throws Exception {
        when(tiposDeDesarrolladorService.patch(eq(1L), any())).thenReturn(dto());
        when(tiposDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/desarrolladores/5/tipos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipoDeDesarrolladorId\":3}"))
                .andExpect(status().isOk());
    }

    @Test
    void patch_NoExiste_Retorna404() throws Exception {
        when(tiposDeDesarrolladorService.patch(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(patch("/api/v2/desarrolladores/5/tipos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipoDeDesarrolladorId\":3}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_ArgumentoInvalido_Retorna400() throws Exception {
        when(tiposDeDesarrolladorService.patch(eq(1L), any()))
                .thenThrow(new IllegalArgumentException("Argumento inválido"));
        mockMvc.perform(patch("/api/v2/desarrolladores/5/tipos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipoDeDesarrolladorId\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unlink_Existe_Retorna204() throws Exception {
        doNothing().when(tiposDeDesarrolladorService).unlink(5L, 2L);
        mockMvc.perform(delete("/api/v2/desarrolladores/5/tipos/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void unlink_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado"))
                .when(tiposDeDesarrolladorService).unlink(5L, 99L);
        mockMvc.perform(delete("/api/v2/desarrolladores/5/tipos/99"))
                .andExpect(status().isNotFound());
    }
}