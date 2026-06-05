package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.EmpresaController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresaService;

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

@WebMvcTest(EmpresaController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpresaService empresaService;

    private EmpresaResponseDTO dto() {
        EmpresaResponseDTO dto = new EmpresaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Nintendo");
        return dto;
    }

    @Test
    void findAllPaged_Retorna200() throws Exception {
        PagedResponse<EmpresaResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(empresaService.findAllPaged(1, 4)).thenReturn(response);

        mockMvc.perform(get("/api/v1/empresas/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Nintendo"));
    }

    @Test
    void findById_Retorna200() throws Exception {
        when(empresaService.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/empresas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nintendo"));
    }

    @Test
    void save_BodyInvalido_Retorna400() throws Exception {
        String body = """
                {
                  "nombre": "Nintendo"
                }
                """;

        mockMvc.perform(post("/api/v1/empresas")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        verify(empresaService, never()).create(any());
    }

    @Test
    void update_BodyInvalido_Retorna400() throws Exception {
        String body = """
                {
                  "nombre": "Nintendo"
                }
                """;

        mockMvc.perform(put("/api/v1/empresas/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        verify(empresaService, never()).update(anyLong(), any());
    }

    @Test
    void patch_Retorna200() throws Exception {
        when(empresaService.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Nintendo"
                }
                """;

        mockMvc.perform(patch("/api/v1/empresas/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nintendo"));
    }

    @Test
    void delete_Retorna204() throws Exception {
        doNothing().when(empresaService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/empresas/1"))
                .andExpect(status().isNoContent());

        verify(empresaService).deleteById(1L);
    }
}