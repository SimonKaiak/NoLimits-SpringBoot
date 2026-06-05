package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.ClasificacionController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.ClasificacionResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.ClasificacionService;

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

@WebMvcTest(ClasificacionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClasificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClasificacionService clasificacionService;

    private ClasificacionResponseDTO dto() {
        ClasificacionResponseDTO dto = new ClasificacionResponseDTO();
        dto.setId(1L);
        dto.setNombre("T");
        dto.setDescripcion("Contenido apto para adolescentes");
        dto.setActivo(true);
        return dto;
    }

    @Test
    void listar_ConDatos_Retorna200() throws Exception {
        when(clasificacionService.findAll()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/clasificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("T"));
    }

    @Test
    void listar_SinDatos_Retorna204() throws Exception {
        when(clasificacionService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/clasificaciones"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarPaginado_Retorna200() throws Exception {
        var page = new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(clasificacionService.listarPaginado(1, 4, ""))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/clasificaciones/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("T"));
    }

    @Test
    void buscarPorId_Retorna200() throws Exception {
        when(clasificacionService.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/clasificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("T"));
    }

    @Test
    void crear_Retorna201() throws Exception {
        when(clasificacionService.create(any())).thenReturn(dto());

        String body = """
                {
                "nombre": "T"
                }
                """;

        mockMvc.perform(post("/api/v1/clasificaciones")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("T"));
    }

    @Test
    void actualizar_Retorna200() throws Exception {
        when(clasificacionService.update(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                "nombre": "M"
                }
                """;

        mockMvc.perform(put("/api/v1/clasificaciones/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("T"));
    }

    @Test
    void actualizarParcial_Retorna200() throws Exception {
        when(clasificacionService.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "descripcion": "Actualizada"
                }
                """;

        mockMvc.perform(patch("/api/v1/clasificaciones/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("T"));
    }

    @Test
    void eliminar_Retorna204() throws Exception {
        doNothing().when(clasificacionService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/clasificaciones/1"))
                .andExpect(status().isNoContent());

        verify(clasificacionService).deleteById(1L);
    }

    @Test
    void buscarPorNombre_ConDatos_Retorna200() throws Exception {
        when(clasificacionService.findByNombreContainingIgnoreCase("T"))
                .thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/clasificaciones/buscar")
                        .param("nombre", "T"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("T"));
    }

    @Test
    void buscarPorNombre_SinDatos_Retorna204() throws Exception {
        when(clasificacionService.findByNombreContainingIgnoreCase("Nada"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/clasificaciones/buscar")
                        .param("nombre", "Nada"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorNombreExacto_Retorna200() throws Exception {
        when(clasificacionService.findByNombreExactIgnoreCase("T"))
                .thenReturn(dto());

        mockMvc.perform(get("/api/v1/clasificaciones/nombre-exacto")
                        .param("nombre", "T"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("T"));
    }

    @Test
    void listarActivas_ConDatos_Retorna200() throws Exception {
        when(clasificacionService.findActivas()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/clasificaciones/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    void listarActivas_SinDatos_Retorna204() throws Exception {
        when(clasificacionService.findActivas()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/clasificaciones/activas"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarInactivas_ConDatos_Retorna200() throws Exception {
        ClasificacionResponseDTO inactiva = dto();
        inactiva.setActivo(false);

        when(clasificacionService.findInactivas()).thenReturn(List.of(inactiva));

        mockMvc.perform(get("/api/v1/clasificaciones/inactivas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(false));
    }

    @Test
    void listarInactivas_SinDatos_Retorna204() throws Exception {
        when(clasificacionService.findInactivas()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/clasificaciones/inactivas"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerResumen_ConDatos_Retorna200() throws Exception {
        when(clasificacionService.obtenerClasificacionesConDatos())
                .thenReturn(List.of(Map.of(
                        "ID", 1L,
                        "Nombre", "T",
                        "Descripcion", "Contenido apto",
                        "Activo", true
                )));

        mockMvc.perform(get("/api/v1/clasificaciones/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].Nombre").value("T"));
    }

    @Test
    void obtenerResumen_SinDatos_Retorna204() throws Exception {
        when(clasificacionService.obtenerClasificacionesConDatos())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/clasificaciones/resumen"))
                .andExpect(status().isNoContent());
    }
}