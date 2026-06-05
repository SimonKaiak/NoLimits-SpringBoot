package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.DesarrolladorController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladorService;

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

@WebMvcTest(DesarrolladorController.class)
@AutoConfigureMockMvc(addFilters = false)
class DesarrolladorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DesarrolladorService desarrolladorService;

    private DesarrolladorResponseDTO dto() {
        DesarrolladorResponseDTO dto = new DesarrolladorResponseDTO();
        dto.setId(1L);
        dto.setNombre("Nintendo");
        return dto;
    }

    @Test
    void findAll_SinNombre_Retorna200() throws Exception {
        when(desarrolladorService.findAll()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/desarrolladores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Nintendo"));

        verify(desarrolladorService).findAll();
        verify(desarrolladorService, never()).findByNombre(anyString());
    }

    @Test
    void findAll_ConNombre_Retorna200() throws Exception {
        when(desarrolladorService.findByNombre("Nintendo")).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/desarrolladores")
                        .param("nombre", "Nintendo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Nintendo"));

        verify(desarrolladorService).findByNombre("Nintendo");
    }

    @Test
    void findAll_NombreVacio_UsaFindAll() throws Exception {
        when(desarrolladorService.findAll()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/desarrolladores")
                        .param("nombre", " "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Nintendo"));

        verify(desarrolladorService).findAll();
    }

    @Test
    void listarPaginado_Retorna200() throws Exception {
        PagedResponse<DesarrolladorResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(desarrolladorService.listarPaginado(1, 4, ""))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/desarrolladores/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Nintendo"));
    }

    @Test
    void findById_Retorna200() throws Exception {
        when(desarrolladorService.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/desarrolladores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nintendo"));
    }

    @Test
    void findByNombre_Retorna200() throws Exception {
        when(desarrolladorService.findByNombre("Nintendo"))
                .thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/desarrolladores/search")
                        .param("nombre", "Nintendo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Nintendo"));
    }

    @Test
    void save_Retorna201() throws Exception {
        when(desarrolladorService.save(any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Nintendo"
                }
                """;

        mockMvc.perform(post("/api/v1/desarrolladores")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Nintendo"));
    }

    @Test
    void update_Retorna200() throws Exception {
        when(desarrolladorService.update(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Nintendo"
                }
                """;

        mockMvc.perform(put("/api/v1/desarrolladores/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nintendo"));
    }

    @Test
    void patch_Retorna200() throws Exception {
        when(desarrolladorService.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Nintendo"
                }
                """;

        mockMvc.perform(patch("/api/v1/desarrolladores/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nintendo"));
    }

    @Test
    void deleteById_Retorna204() throws Exception {
        doNothing().when(desarrolladorService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/desarrolladores/1"))
                .andExpect(status().isNoContent());

        verify(desarrolladorService).deleteById(1L);
    }
}