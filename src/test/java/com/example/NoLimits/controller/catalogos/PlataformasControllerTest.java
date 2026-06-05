package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.PlataformasController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformasResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformasService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlataformasController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlataformasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlataformasService plataformasService;

    private PlataformasResponseDTO dto() {
        PlataformasResponseDTO dto = new PlataformasResponseDTO();
        dto.setId(5L);
        dto.setProductoId(10L);
        dto.setPlataformaId(1L);
        dto.setPlataformaNombre("PC");
        return dto;
    }

    @Test
    void listarPorProducto_ConDatos_Retorna200() throws Exception {
        when(plataformasService.findByProducto(10L))
                .thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/productos/10/plataformas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(10L))
                .andExpect(jsonPath("$[0].plataformaId").value(1L))
                .andExpect(jsonPath("$[0].plataformaNombre").value("PC"));
    }

    @Test
    void listarPorProducto_SinDatos_Retorna204() throws Exception {
        when(plataformasService.findByProducto(10L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos/10/plataformas"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_Retorna201() throws Exception {
        when(plataformasService.link(10L, 1L))
                .thenReturn(dto());

        mockMvc.perform(post("/api/v1/productos/10/plataformas/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productoId").value(10L))
                .andExpect(jsonPath("$.plataformaId").value(1L))
                .andExpect(jsonPath("$.plataformaNombre").value("PC"));
    }

    @Test
    void patch_Retorna200() throws Exception {
        when(plataformasService.patch(5L, 10L, 1L))
                .thenReturn(dto());

        String body = """
                {
                  "nuevoProductoId": 10,
                  "nuevaPlataformaId": 1
                }
                """;

        mockMvc.perform(patch("/api/v1/productos/10/plataformas/5")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(10L))
                .andExpect(jsonPath("$.plataformaId").value(1L))
                .andExpect(jsonPath("$.plataformaNombre").value("PC"));
    }

    @Test
    void unlink_Retorna204() throws Exception {
        doNothing().when(plataformasService).unlink(10L, 1L);

        mockMvc.perform(delete("/api/v1/productos/10/plataformas/1"))
                .andExpect(status().isNoContent());

        verify(plataformasService).unlink(10L, 1L);
    }
}