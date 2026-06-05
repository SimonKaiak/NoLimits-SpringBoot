package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.DesarrolladoresController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladoresResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladoresService;

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

@WebMvcTest(DesarrolladoresController.class)
@AutoConfigureMockMvc(addFilters = false)
class DesarrolladoresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DesarrolladoresService service;

    private DesarrolladoresResponseDTO dto() {
        DesarrolladoresResponseDTO dto = new DesarrolladoresResponseDTO();
        dto.setId(1L);
        dto.setProductoId(10L);
        dto.setDesarrolladorId(5L);
        return dto;
    }

    @Test
    void listar_Retorna200() throws Exception {
        when(service.findByProducto(10L)).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/productos/10/desarrolladores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(10L))
                .andExpect(jsonPath("$[0].desarrolladorId").value(5L));
    }

    @Test
    void listar_SinDatos_Retorna200ConListaVacia() throws Exception {
        when(service.findByProducto(10L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos/10/desarrolladores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void link_ConBody_Retorna200() throws Exception {
        when(service.link(any())).thenReturn(dto());

        String body = """
                {
                  "productoId": 999,
                  "desarrolladorId": 888
                }
                """;

        mockMvc.perform(post("/api/v1/productos/10/desarrolladores/5")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(10L))
                .andExpect(jsonPath("$.desarrolladorId").value(5L));

        verify(service).link(argThat(dto ->
                dto.getProductoId().equals(10L) &&
                dto.getDesarrolladorId().equals(5L)
        ));
    }

    @Test
    void link_SinBody_Retorna200() throws Exception {
        when(service.link(any())).thenReturn(dto());

        mockMvc.perform(post("/api/v1/productos/10/desarrolladores/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(10L))
                .andExpect(jsonPath("$.desarrolladorId").value(5L));

        verify(service).link(argThat(dto ->
                dto.getProductoId().equals(10L) &&
                dto.getDesarrolladorId().equals(5L)
        ));
    }

    @Test
    void patch_Retorna200() throws Exception {
        when(service.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "productoId": 10,
                  "desarrolladorId": 5
                }
                """;

        mockMvc.perform(patch("/api/v1/productos/10/desarrolladores/relaciones/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(10L))
                .andExpect(jsonPath("$.desarrolladorId").value(5L));
    }

    @Test
    void unlink_Retorna200() throws Exception {
        doNothing().when(service).unlink(10L, 5L);

        mockMvc.perform(delete("/api/v1/productos/10/desarrolladores/5"))
                .andExpect(status().isOk());

        verify(service).unlink(10L, 5L);
    }
}