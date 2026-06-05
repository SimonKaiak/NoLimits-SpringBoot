package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.TiposEmpresaController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TiposEmpresaService;

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

@WebMvcTest(TiposEmpresaController.class)
@AutoConfigureMockMvc(addFilters = false)
class TiposEmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TiposEmpresaService tiposEmpresaService;

    private TiposEmpresaResponseDTO dto() {
        TiposEmpresaResponseDTO dto = new TiposEmpresaResponseDTO();
        dto.setId(10L);
        dto.setEmpresaId(5L);
        dto.setTipoEmpresaId(2L);
        dto.setTipoEmpresaNombre("Publisher");
        return dto;
    }

    @Test
    void listarPorEmpresa_Retorna200() throws Exception {
        when(tiposEmpresaService.findAllByEmpresa(5L))
                .thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/empresas/5/tipos-empresa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].empresaId").value(5L))
                .andExpect(jsonPath("$[0].tipoEmpresaId").value(2L))
                .andExpect(jsonPath("$[0].tipoEmpresaNombre").value("Publisher"));
    }

    @Test
    void listarPorEmpresa_SinDatos_Retorna200ConListaVacia() throws Exception {
        when(tiposEmpresaService.findAllByEmpresa(5L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/empresas/5/tipos-empresa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void link_Retorna201() throws Exception {
        when(tiposEmpresaService.link(5L, 2L)).thenReturn(dto());

        mockMvc.perform(post("/api/v1/empresas/5/tipos-empresa/2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.empresaId").value(5L))
                .andExpect(jsonPath("$.tipoEmpresaId").value(2L))
                .andExpect(jsonPath("$.tipoEmpresaNombre").value("Publisher"));
    }

    @Test
    void patch_Retorna200() throws Exception {
        when(tiposEmpresaService.patch(eq(10L), any())).thenReturn(dto());

        String body = """
                {
                  "empresaId": 5,
                  "tipoEmpresaId": 2
                }
                """;

        mockMvc.perform(patch("/api/v1/empresas/5/tipos-empresa/10")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empresaId").value(5L))
                .andExpect(jsonPath("$.tipoEmpresaId").value(2L))
                .andExpect(jsonPath("$.tipoEmpresaNombre").value("Publisher"));
    }

    @Test
    void unlink_Retorna204() throws Exception {
        doNothing().when(tiposEmpresaService).unlink(5L, 2L);

        mockMvc.perform(delete("/api/v1/empresas/5/tipos-empresa/2"))
                .andExpect(status().isNoContent());

        verify(tiposEmpresaService).unlink(5L, 2L);
    }
}