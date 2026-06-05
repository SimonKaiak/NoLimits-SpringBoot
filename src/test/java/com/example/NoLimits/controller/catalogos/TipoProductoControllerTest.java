package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.controller.catalogos.TipoProductoController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.TipoProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(TipoProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class TipoProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TipoProductoService tipoProductoService;

    private TipoProductoResponseDTO dto() {
        TipoProductoResponseDTO dto = new TipoProductoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Videojuego");
        dto.setDescripcion("Productos de tipo videojuego");
        dto.setActivo(true);
        return dto;
    }

    @Test
    void getAllTiposProductos_ConDatos_Retorna200() throws Exception {
        when(tipoProductoService.findAll()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/tipo-productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Videojuego"));
    }

    @Test
    void getAllTiposProductos_SinDatos_Retorna204() throws Exception {
        when(tipoProductoService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tipo-productos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarTiposProductoPaginado_Retorna200() throws Exception {
        PagedResponse<TipoProductoResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(tipoProductoService.listarPaginado(1, 4, ""))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/tipo-productos/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Videojuego"));
    }

    @Test
    void getTipoProductoById_Existe_Retorna200() throws Exception {
        when(tipoProductoService.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/tipo-productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Videojuego"));
    }

    @Test
    void getTipoProductoById_NoExiste_Retorna404() throws Exception {
        when(tipoProductoService.findById(99L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(get("/api/v1/tipo-productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTipoProductoByNombreLike_ConDatos_Retorna200() throws Exception {
        when(tipoProductoService.findByNombre("video"))
                .thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/tipo-productos/buscar")
                        .param("nombre", "video"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Videojuego"));
    }

    @Test
    void getTipoProductoByNombreLike_SinDatos_Retorna204() throws Exception {
        when(tipoProductoService.findByNombre("nada"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tipo-productos/buscar")
                        .param("nombre", "nada"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTipoProductoByNombreExacto_Existe_Retorna200() throws Exception {
        when(tipoProductoService.findByNombreExactIgnoreCase("Videojuego"))
                .thenReturn(dto());

        mockMvc.perform(get("/api/v1/tipo-productos/nombre-exacto")
                        .param("nombre", "Videojuego"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Videojuego"));
    }

    @Test
    void getTipoProductoByNombreExacto_NoExiste_Retorna404() throws Exception {
        when(tipoProductoService.findByNombreExactIgnoreCase("Nada"))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(get("/api/v1/tipo-productos/nombre-exacto")
                        .param("nombre", "Nada"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTipoProducto_Retorna201() throws Exception {
        when(tipoProductoService.save(any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Videojuego",
                  "descripcion": "Productos de tipo videojuego",
                  "activo": true
                }
                """;

        mockMvc.perform(post("/api/v1/tipo-productos")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Videojuego"));
    }

    @Test
    void updateTipoProducto_Existe_Retorna200() throws Exception {
        when(tipoProductoService.update(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Videojuego",
                  "descripcion": "Actualizado",
                  "activo": true
                }
                """;

        mockMvc.perform(put("/api/v1/tipo-productos/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Videojuego"));
    }

    @Test
    void updateTipoProducto_NoExiste_Retorna404() throws Exception {
        when(tipoProductoService.update(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "nombre": "Videojuego",
                  "descripcion": "Actualizado",
                  "activo": true
                }
                """;

        mockMvc.perform(put("/api/v1/tipo-productos/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchTipoProducto_Existe_Retorna200() throws Exception {
        when(tipoProductoService.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "descripcion": "Parcial",
                  "activo": false
                }
                """;

        mockMvc.perform(patch("/api/v1/tipo-productos/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Videojuego"));
    }

    @Test
    void patchTipoProducto_NoExiste_Retorna404() throws Exception {
        when(tipoProductoService.patch(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        String body = """
                {
                  "descripcion": "Parcial"
                }
                """;

        mockMvc.perform(patch("/api/v1/tipo-productos/99")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTipoProducto_Retorna204() throws Exception {
        doNothing().when(tipoProductoService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/tipo-productos/1"))
                .andExpect(status().isNoContent());

        verify(tipoProductoService).deleteById(1L);
    }

    @Test
    void listarActivos_ConDatos_Retorna200() throws Exception {
        when(tipoProductoService.findActivos()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/tipo-productos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    void listarActivos_SinDatos_Retorna204() throws Exception {
        when(tipoProductoService.findActivos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tipo-productos/activos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarInactivos_ConDatos_Retorna200() throws Exception {
        TipoProductoResponseDTO inactivo = dto();
        inactivo.setActivo(false);

        when(tipoProductoService.findInactivos()).thenReturn(List.of(inactivo));

        mockMvc.perform(get("/api/v1/tipo-productos/inactivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(false));
    }

    @Test
    void listarInactivos_SinDatos_Retorna204() throws Exception {
        when(tipoProductoService.findInactivos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tipo-productos/inactivos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerResumen_ConDatos_Retorna200() throws Exception {
        when(tipoProductoService.obtenerTipoProductoConNombres())
                .thenReturn(List.of(Map.of(
                        "ID", 1L,
                        "Nombre", "Videojuego",
                        "Descripcion", "Productos",
                        "Activo", true
                )));

        mockMvc.perform(get("/api/v1/tipo-productos/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].Nombre").value("Videojuego"));
    }

    @Test
    void obtenerResumen_SinDatos_Retorna204() throws Exception {
        when(tipoProductoService.obtenerTipoProductoConNombres())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tipo-productos/resumen"))
                .andExpect(status().isNoContent());
    }
}