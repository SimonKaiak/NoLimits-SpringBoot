package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.controller.catalogos.EstadoController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.EstadoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadoService estadoService;

    private EstadoResponseDTO dto() {
        EstadoResponseDTO dto = new EstadoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Activo");
        dto.setDescripcion("Estado activo");
        dto.setActivo(true);
        return dto;
    }

    @Test
    void listarEstados_ConDatos_Retorna200() throws Exception {
        when(estadoService.findAll()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/estados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Activo"));
    }

    @Test
    void listarEstados_SinDatos_Retorna204() throws Exception {
        when(estadoService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/estados"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarPaginado_Retorna200() throws Exception {
        PagedResponse<EstadoResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(estadoService.listarPaginado(1, 4, ""))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/estados/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Activo"));
    }

    @Test
    void obtenerPorId_Existe_Retorna200() throws Exception {
        when(estadoService.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/estados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Activo"));
    }

    @Test
    void obtenerPorId_NoExiste_Retorna404() throws Exception {
        when(estadoService.findById(99L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(get("/api/v1/estados/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorNombre_ConDatos_Retorna200() throws Exception {
        when(estadoService.findByNombreLike("Activo"))
                .thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/estados/nombre/Activo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Activo"));
    }

    @Test
    void buscarPorNombre_SinDatos_Retorna204() throws Exception {
        when(estadoService.findByNombreLike("Nada"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/estados/nombre/Nada"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarActivos_ConDatos_Retorna200() throws Exception {
        when(estadoService.findActivos()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/estados/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    void listarActivos_SinDatos_Retorna204() throws Exception {
        when(estadoService.findActivos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/estados/activos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarInactivos_ConDatos_Retorna200() throws Exception {
        EstadoResponseDTO inactivo = dto();
        inactivo.setActivo(false);

        when(estadoService.findInactivos()).thenReturn(List.of(inactivo));

        mockMvc.perform(get("/api/v1/estados/inactivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(false));
    }

    @Test
    void listarInactivos_SinDatos_Retorna204() throws Exception {
        when(estadoService.findInactivos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/estados/inactivos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerResumen_ConDatos_Retorna200() throws Exception {
        when(estadoService.obtenerEstadosResumen())
                .thenReturn(List.of(Map.of(
                        "ID", 1L,
                        "Nombre", "Activo",
                        "Descripcion", "Estado activo",
                        "Activo", true
                )));

        mockMvc.perform(get("/api/v1/estados/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].Nombre").value("Activo"));
    }

    @Test
    void obtenerResumen_SinDatos_Retorna204() throws Exception {
        when(estadoService.obtenerEstadosResumen())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/estados/resumen"))
                .andExpect(status().isNoContent());
    }

    @Test
    void crearEstado_Retorna201() throws Exception {
        when(estadoService.save(any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Activo",
                  "descripcion": "Estado activo",
                  "activo": true
                }
                """;

        mockMvc.perform(post("/api/v1/estados")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Activo"));
    }

    @Test
    void actualizarEstado_Existe_Retorna200() throws Exception {
        when(estadoService.update(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Activo",
                  "descripcion": "Actualizado",
                  "activo": true
                }
                """;

        mockMvc.perform(put("/api/v1/estados/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Activo"));
    }

    @Test
    void actualizarEstado_NoExiste_Retorna404() throws Exception {
        when(estadoService.update(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "nombre": "Activo",
                  "descripcion": "Actualizado",
                  "activo": true
                }
                """;

        mockMvc.perform(put("/api/v1/estados/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchEstado_Existe_Retorna200() throws Exception {
        when(estadoService.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "descripcion": "Parcial",
                  "activo": false
                }
                """;

        mockMvc.perform(patch("/api/v1/estados/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Activo"));
    }

    @Test
    void patchEstado_NoExiste_Retorna404() throws Exception {
        when(estadoService.patch(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "descripcion": "Parcial"
                }
                """;

        mockMvc.perform(patch("/api/v1/estados/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarEstado_Existe_Retorna204() throws Exception {
        doNothing().when(estadoService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/estados/1"))
                .andExpect(status().isNoContent());

        verify(estadoService).deleteById(1L);
    }

    @Test
    void eliminarEstado_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado"))
                .when(estadoService).deleteById(99L);

        mockMvc.perform(delete("/api/v1/estados/99"))
                .andExpect(status().isNotFound());
    }
}