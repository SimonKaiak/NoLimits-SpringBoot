package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.controller.catalogos.PlataformaController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformaService;

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

@WebMvcTest(PlataformaController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlataformaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlataformaService plataformaService;

    private PlataformaResponseDTO dto() {
        PlataformaResponseDTO dto = new PlataformaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Steam");
        return dto;
    }

    @Test
    void findAll_ConDatos_Retorna200() throws Exception {
        when(plataformaService.findAll()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/plataformas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Steam"));
    }

    @Test
    void findAll_SinDatos_Retorna204() throws Exception {
        when(plataformaService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/plataformas"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarPaginado_Retorna200() throws Exception {
        PagedResponse<PlataformaResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(plataformaService.listarPaginado(1, 4, ""))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/plataformas/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Steam"));
    }

    @Test
    void findById_Existe_Retorna200() throws Exception {
        when(plataformaService.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/plataformas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Steam"));
    }

    @Test
    void findById_NoExiste_Retorna404() throws Exception {
        when(plataformaService.findById(99L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(get("/api/v1/plataformas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void save_Retorna201() throws Exception {
        when(plataformaService.save(any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Steam"
                }
                """;

        mockMvc.perform(post("/api/v1/plataformas")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Steam"));
    }

    @Test
    void update_Existe_Retorna200() throws Exception {
        when(plataformaService.update(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Steam"
                }
                """;

        mockMvc.perform(put("/api/v1/plataformas/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Steam"));
    }

    @Test
    void update_NoExiste_Retorna404() throws Exception {
        when(plataformaService.update(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "nombre": "Steam"
                }
                """;

        mockMvc.perform(put("/api/v1/plataformas/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(plataformaService.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Steam"
                }
                """;

        mockMvc.perform(patch("/api/v1/plataformas/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Steam"));
    }

    @Test
    void patch_NoExiste_Retorna404() throws Exception {
        when(plataformaService.patch(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "nombre": "Steam"
                }
                """;

        mockMvc.perform(patch("/api/v1/plataformas/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(plataformaService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/plataformas/1"))
                .andExpect(status().isNoContent());

        verify(plataformaService).deleteById(1L);
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado"))
                .when(plataformaService).deleteById(99L);

        mockMvc.perform(delete("/api/v1/plataformas/99"))
                .andExpect(status().isNotFound());
    }
}