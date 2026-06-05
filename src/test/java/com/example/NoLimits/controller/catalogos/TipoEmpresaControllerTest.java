package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.TipoEmpresaController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.TipoEmpresaService;

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

@WebMvcTest(TipoEmpresaController.class)
@AutoConfigureMockMvc(addFilters = false)
class TipoEmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TipoEmpresaService tipoEmpresaService;

    private TipoEmpresaResponseDTO dto() {
        TipoEmpresaResponseDTO dto = new TipoEmpresaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Desarrolladora");
        return dto;
    }

    @Test
    void findAllPaged_ConDatos_Retorna200() throws Exception {

        PagedResponse<TipoEmpresaResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(tipoEmpresaService.findAllPaged(1, 20))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/tipos-empresa/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre")
                        .value("Desarrolladora"));
    }

    @Test
    void findAllPaged_SinDatos_Retorna204() throws Exception {

        PagedResponse<TipoEmpresaResponseDTO> response =
                new PagedResponse<>(List.of(), 0, 1, 0);

        when(tipoEmpresaService.findAllPaged(1, 20))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/tipos-empresa/paginado"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findById_Retorna200() throws Exception {

        when(tipoEmpresaService.findById(1L))
                .thenReturn(dto());

        mockMvc.perform(get("/api/v1/tipos-empresa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre")
                        .value("Desarrolladora"));
    }

    @Test
    void save_Retorna201() throws Exception {

        when(tipoEmpresaService.save(any()))
                .thenReturn(dto());

        String body = """
                {
                  "nombre":"Desarrolladora"
                }
                """;

        mockMvc.perform(post("/api/v1/tipos-empresa")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre")
                        .value("Desarrolladora"));
    }

    @Test
    void update_Retorna200() throws Exception {

        when(tipoEmpresaService.update(eq(1L), any()))
                .thenReturn(dto());

        String body = """
                {
                  "nombre":"Desarrolladora"
                }
                """;

        mockMvc.perform(put("/api/v1/tipos-empresa/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre")
                        .value("Desarrolladora"));
    }

    @Test
    void patch_Retorna200() throws Exception {

        when(tipoEmpresaService.patch(eq(1L), any()))
                .thenReturn(dto());

        String body = """
                {
                  "nombre":"Desarrolladora"
                }
                """;

        mockMvc.perform(patch("/api/v1/tipos-empresa/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre")
                        .value("Desarrolladora"));
    }

    @Test
    void delete_Retorna204() throws Exception {

        doNothing().when(tipoEmpresaService)
                .deleteById(1L);

        mockMvc.perform(delete("/api/v1/tipos-empresa/1"))
                .andExpect(status().isNoContent());

        verify(tipoEmpresaService).deleteById(1L);
    }
}