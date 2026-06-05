package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.controller.catalogos.GeneroController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GeneroResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.GeneroService;

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

@WebMvcTest(GeneroController.class)
@AutoConfigureMockMvc(addFilters = false)
class GeneroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeneroService generoService;

    private GeneroResponseDTO dto() {
        GeneroResponseDTO dto = new GeneroResponseDTO();
        dto.setId(1L);
        dto.setNombre("Acción");
        return dto;
    }

    @Test
    void listarGeneros_ConDatos_Retorna200() throws Exception {
        when(generoService.findAll()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/generos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Acción"));
    }

    @Test
    void listarGeneros_SinDatos_Retorna204() throws Exception {
        when(generoService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/generos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarPaginado_Retorna200() throws Exception {
        PagedResponse<GeneroResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(generoService.listarPaginado(1, 4, ""))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/generos/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Acción"));
    }

    @Test
    void buscarPorId_Existe_Retorna200() throws Exception {
        when(generoService.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/generos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Acción"));
    }

    @Test
    void buscarPorId_NoExiste_Retorna404() throws Exception {
        when(generoService.findById(99L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(get("/api/v1/generos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorNombre_ConDatos_Retorna200() throws Exception {
        when(generoService.findByNombreContaining("Acción"))
                .thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/generos/nombre/Acción"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Acción"));
    }

    @Test
    void buscarPorNombre_SinDatos_Retorna204() throws Exception {
        when(generoService.findByNombreContaining("Nada"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/generos/nombre/Nada"))
                .andExpect(status().isNoContent());
    }

    @Test
    void crearGenero_Retorna201() throws Exception {
        when(generoService.save(any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Acción",
                  "descripcion": "Género de acción",
                  "activo": true
                }
                """;

        mockMvc.perform(post("/api/v1/generos")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Acción"));
    }

    @Test
    void actualizarGenero_Existe_Retorna200() throws Exception {
        when(generoService.update(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Acción",
                  "descripcion": "Actualizado",
                  "activo": true
                }
                """;

        mockMvc.perform(put("/api/v1/generos/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Acción"));
    }

    @Test
    void actualizarGenero_NoExiste_Retorna404() throws Exception {
        when(generoService.update(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "nombre": "Acción",
                  "descripcion": "Actualizado",
                  "activo": true
                }
                """;

        mockMvc.perform(put("/api/v1/generos/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void editarGenero_Existe_Retorna200() throws Exception {
        when(generoService.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "descripcion": "Parcial",
                  "activo": false
                }
                """;

        mockMvc.perform(patch("/api/v1/generos/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Acción"));
    }

    @Test
    void editarGenero_NoExiste_Retorna404() throws Exception {
        when(generoService.patch(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "descripcion": "Parcial"
                }
                """;

        mockMvc.perform(patch("/api/v1/generos/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarGenero_Existe_Retorna204() throws Exception {
        doNothing().when(generoService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/generos/1"))
                .andExpect(status().isNoContent());

        verify(generoService).deleteById(1L);
    }

    @Test
    void eliminarGenero_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado"))
                .when(generoService).deleteById(99L);

        mockMvc.perform(delete("/api/v1/generos/99"))
                .andExpect(status().isNotFound());
    }
}