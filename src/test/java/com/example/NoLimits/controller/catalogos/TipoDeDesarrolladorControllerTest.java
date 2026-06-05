package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.controller.catalogos.TipoDeDesarrolladorController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.TipoDeDesarrolladorService;

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

@WebMvcTest(TipoDeDesarrolladorController.class)
@AutoConfigureMockMvc(addFilters = false)
class TipoDeDesarrolladorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TipoDeDesarrolladorService service;

    private TipoDeDesarrolladorResponseDTO dto() {
        TipoDeDesarrolladorResponseDTO dto = new TipoDeDesarrolladorResponseDTO();
        dto.setId(1L);
        dto.setNombre("Estudio");
        return dto;
    }

    @Test
    void findAll_Retorna200() throws Exception {
        PagedResponse<TipoDeDesarrolladorResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(service.findAllPaged(1, 4)).thenReturn(response);

        mockMvc.perform(get("/api/v1/tipos-desarrollador/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Estudio"));
    }

    @Test
    void findById_Existe_Retorna200() throws Exception {
        when(service.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/tipos-desarrollador/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Estudio"));
    }

    @Test
    void findById_NoExiste_Retorna404() throws Exception {
        when(service.findById(99L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(get("/api/v1/tipos-desarrollador/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void save_Retorna201() throws Exception {
        when(service.save(any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Estudio"
                }
                """;

        mockMvc.perform(post("/api/v1/tipos-desarrollador")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Estudio"));
    }

    @Test
    void update_Existe_Retorna200() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Estudio"
                }
                """;

        mockMvc.perform(put("/api/v1/tipos-desarrollador/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Estudio"));
    }

    @Test
    void update_NoExiste_Retorna404() throws Exception {
        when(service.update(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "nombre": "Estudio"
                }
                """;

        mockMvc.perform(put("/api/v1/tipos-desarrollador/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(service.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Estudio"
                }
                """;

        mockMvc.perform(patch("/api/v1/tipos-desarrollador/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Estudio"));
    }

    @Test
    void patch_NoExiste_Retorna404() throws Exception {
        when(service.patch(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "nombre": "Estudio"
                }
                """;

        mockMvc.perform(patch("/api/v1/tipos-desarrollador/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/api/v1/tipos-desarrollador/1"))
                .andExpect(status().isNoContent());

        verify(service).deleteById(1L);
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado"))
                .when(service).deleteById(99L);

        mockMvc.perform(delete("/api/v1/tipos-desarrollador/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ConRelaciones_Retorna400() throws Exception {
        doThrow(new IllegalStateException("Tiene relaciones"))
                .when(service).deleteById(2L);

        mockMvc.perform(delete("/api/v1/tipos-desarrollador/2"))
                .andExpect(status().isBadRequest());
    }
}