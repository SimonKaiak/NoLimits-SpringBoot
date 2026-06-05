package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.EmpresasController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresasResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresasService;

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

@WebMvcTest(EmpresasController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmpresasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpresasService service;

    private EmpresasResponseDTO dto() {
        EmpresasResponseDTO dto = new EmpresasResponseDTO();
        dto.setId(1L);
        dto.setProductoId(10L);
        dto.setEmpresaId(5L);
        dto.setEmpresaNombre("Sony Pictures");
        return dto;
    }

    @Test
    void listar_Retorna200() throws Exception {
        when(service.findByProducto(10L)).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/productos/10/empresas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(10L))
                .andExpect(jsonPath("$[0].empresaId").value(5L))
                .andExpect(jsonPath("$[0].empresaNombre").value("Sony Pictures"));
    }

    @Test
    void listar_SinDatos_Retorna200ConListaVacia() throws Exception {
        when(service.findByProducto(10L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos/10/empresas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void link_Retorna201() throws Exception {
        when(service.link(10L, 5L)).thenReturn(dto());

        mockMvc.perform(post("/api/v1/productos/10/empresas/5"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productoId").value(10L))
                .andExpect(jsonPath("$.empresaId").value(5L))
                .andExpect(jsonPath("$.empresaNombre").value("Sony Pictures"));
    }

    @Test
    void patch_Retorna200() throws Exception {
        when(service.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "productoId": 10,
                  "empresaId": 5
                }
                """;

        mockMvc.perform(patch("/api/v1/productos/10/empresas/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(10L))
                .andExpect(jsonPath("$.empresaId").value(5L))
                .andExpect(jsonPath("$.empresaNombre").value("Sony Pictures"));
    }

    @Test
    void unlink_Retorna204() throws Exception {
        doNothing().when(service).unlink(10L, 5L);

        mockMvc.perform(delete("/api/v1/productos/10/empresas/5"))
                .andExpect(status().isNoContent());

        verify(service).unlink(10L, 5L);
    }
}