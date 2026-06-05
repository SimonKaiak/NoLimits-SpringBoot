package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.controller.catalogos.TiposDeDesarrolladorController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TiposDeDesarrolladorService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TiposDeDesarrolladorController.class)
@AutoConfigureMockMvc(addFilters = false)
class TiposDeDesarrolladorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TiposDeDesarrolladorService service;

    private TiposDeDesarrolladorResponseDTO dto() {
        TiposDeDesarrolladorResponseDTO dto = new TiposDeDesarrolladorResponseDTO();
        dto.setId(1L);
        dto.setDesarrolladorId(10L);
        dto.setTipoDeDesarrolladorId(5L);
        dto.setTipoDeDesarrolladorNombre("Estudio");
        return dto;
    }

    @Test
    void listar_ConDatos_Retorna200() throws Exception {
        when(service.findByDesarrollador(10L)).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/desarrolladores/10/tipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].desarrolladorId").value(10L))
                .andExpect(jsonPath("$[0].tipoDeDesarrolladorId").value(5L))
                .andExpect(jsonPath("$[0].tipoDeDesarrolladorNombre").value("Estudio"));
    }

    @Test
    void listar_SinDatos_Retorna204() throws Exception {
        when(service.findByDesarrollador(10L)).thenReturn(List.of());

        mockMvc.perform(get("/api/desarrolladores/10/tipos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_Existe_Retorna201() throws Exception {
        when(service.link(10L, 5L)).thenReturn(dto());

        mockMvc.perform(post("/api/desarrolladores/10/tipos/5"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.desarrolladorId").value(10L))
                .andExpect(jsonPath("$.tipoDeDesarrolladorId").value(5L))
                .andExpect(jsonPath("$.tipoDeDesarrolladorNombre").value("Estudio"));
    }

    @Test
    void link_NoExiste_Retorna404() throws Exception {
        when(service.link(99L, 5L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(post("/api/desarrolladores/99/tipos/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void link_YaExiste_Retorna400() throws Exception {
        when(service.link(10L, 5L))
                .thenThrow(new IllegalStateException("Relación ya existe"));

        mockMvc.perform(post("/api/desarrolladores/10/tipos/5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unlink_Existe_Retorna204() throws Exception {
        doNothing().when(service).unlink(10L, 5L);

        mockMvc.perform(delete("/api/desarrolladores/10/tipos/5"))
                .andExpect(status().isNoContent());

        verify(service).unlink(10L, 5L);
    }

    @Test
    void unlink_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado"))
                .when(service).unlink(99L, 5L);

        mockMvc.perform(delete("/api/desarrolladores/99/tipos/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(service.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "tipoDeDesarrolladorId": 5
                }
                """;

        mockMvc.perform(patch("/api/desarrolladores/10/tipos/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.desarrolladorId").value(10L))
                .andExpect(jsonPath("$.tipoDeDesarrolladorId").value(5L))
                .andExpect(jsonPath("$.tipoDeDesarrolladorNombre").value("Estudio"));
    }

    @Test
    void patch_NoExiste_Retorna404() throws Exception {
        when(service.patch(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "tipoDeDesarrolladorId": 5
                }
                """;

        mockMvc.perform(patch("/api/desarrolladores/10/tipos/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_DatosInvalidos_Retorna400() throws Exception {
        when(service.patch(eq(1L), any()))
                .thenThrow(new IllegalArgumentException("Datos inválidos"));

        String body = """
                {
                  "tipoDeDesarrolladorId": null
                }
                """;

        mockMvc.perform(patch("/api/desarrolladores/10/tipos/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}