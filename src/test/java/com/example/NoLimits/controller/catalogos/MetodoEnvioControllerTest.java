package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.MetodoEnvioController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoEnvioService;

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

@WebMvcTest(MetodoEnvioController.class)
@AutoConfigureMockMvc(addFilters = false)
class MetodoEnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetodoEnvioService metodoEnvioService;

    private MetodoEnvioResponseDTO dto() {
        MetodoEnvioResponseDTO dto = new MetodoEnvioResponseDTO();
        dto.setId(1L);
        dto.setNombre("Retiro en tienda");
        dto.setDescripcion("Sucursal Plaza Oeste");
        return dto;
    }

    @Test
    void listar_ConDatos_Retorna200() throws Exception {
        when(metodoEnvioService.findAll()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/metodos-envio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Retiro en tienda"));
    }

    @Test
    void listar_SinDatos_Retorna204() throws Exception {
        when(metodoEnvioService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/metodos-envio"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarPaginado_Retorna200() throws Exception {
        PagedResponse<MetodoEnvioResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(metodoEnvioService.findAllPaged(1, 4, ""))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/metodos-envio/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Retiro en tienda"));
    }

    @Test
    void buscarPorId_Retorna200() throws Exception {
        when(metodoEnvioService.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/metodos-envio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Retiro en tienda"));
    }

    @Test
    void crear_BodyInvalido_Retorna400() throws Exception {
        String body = """
                {
                "nombre": "Retiro en tienda",
                "descripcion": "Sucursal Plaza Oeste"
                }
                """;

        mockMvc.perform(post("/api/v1/metodos-envio")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        verify(metodoEnvioService, never()).create(any());
    }

    @Test
    void actualizar_BodyInvalido_Retorna400() throws Exception {
        String body = """
                {
                "nombre": "Retiro en tienda",
                "descripcion": "Sucursal Plaza Oeste"
                }
                """;

        mockMvc.perform(put("/api/v1/metodos-envio/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        verify(metodoEnvioService, never()).update(anyLong(), any());
    }

    @Test
    void patch_Retorna200() throws Exception {
        when(metodoEnvioService.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "descripcion": "Actualizado"
                }
                """;

        mockMvc.perform(patch("/api/v1/metodos-envio/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Retiro en tienda"));
    }

    @Test
    void eliminar_Retorna204() throws Exception {
        doNothing().when(metodoEnvioService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/metodos-envio/1"))
                .andExpect(status().isNoContent());

        verify(metodoEnvioService).deleteById(1L);
    }
}