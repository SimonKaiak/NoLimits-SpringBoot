package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia.controller.catalogos.MetodoPagoController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoPagoResponseDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoPagoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MetodoPagoController.class)
@AutoConfigureMockMvc(addFilters = false)
class MetodoPagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetodoPagoService metodoPagoService;

    private MetodoPagoResponseDTO dto() {
        MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Tarjeta de Débito");
        return dto;
    }

    @Test
    void getAllMetodosPago_ConDatos_Retorna200() throws Exception {
        when(metodoPagoService.findAll()).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/metodos-pago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Tarjeta de Débito"));
    }

    @Test
    void getAllMetodosPago_SinDatos_Retorna204() throws Exception {
        when(metodoPagoService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/metodos-pago"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAllPaged_Retorna200() throws Exception {
        PagedResponse<MetodoPagoResponseDTO> response =
                new PagedResponse<>(List.of(dto()), 1, 1, 1);

        when(metodoPagoService.findAllPaged(1, 4))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/metodos-pago/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre").value("Tarjeta de Débito"));
    }

    @Test
    void getMetodoPagoById_Retorna200() throws Exception {
        when(metodoPagoService.findById(1L)).thenReturn(dto());

        mockMvc.perform(get("/api/v1/metodos-pago/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tarjeta de Débito"));
    }

    @Test
    void createMetodoPago_BodyInvalido_Retorna400() throws Exception {
        String body = """
                {
                "nombre": "Tarjeta de Débito"
                }
                """;

        mockMvc.perform(post("/api/v1/metodos-pago")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        verify(metodoPagoService, never()).save(any());
    }

    @Test
    void updateMetodoPago_BodyInvalido_Retorna400() throws Exception {
        String body = """
                {
                "nombre": "Tarjeta de Débito"
                }
                """;

        mockMvc.perform(put("/api/v1/metodos-pago/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        verify(metodoPagoService, never()).update(anyLong(), any());
    }

    @Test
    void patchMetodoPago_Retorna200() throws Exception {
        when(metodoPagoService.patch(eq(1L), any())).thenReturn(dto());

        String body = """
                {
                  "nombre": "Tarjeta de Débito"
                }
                """;

        mockMvc.perform(patch("/api/v1/metodos-pago/1")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tarjeta de Débito"));
    }

    @Test
    void deleteMetodoPago_Retorna204() throws Exception {
        doNothing().when(metodoPagoService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/metodos-pago/1"))
                .andExpect(status().isNoContent());

        verify(metodoPagoService).deleteById(1L);
    }

    @Test
    void getMetodoPagoByNombre_Existe_Retorna200() throws Exception {
        when(metodoPagoService.findByNombre("Tarjeta"))
                .thenReturn(Optional.of(dto()));

        mockMvc.perform(get("/api/v1/metodos-pago/buscar/Tarjeta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tarjeta de Débito"));
    }

    @Test
    void getMetodoPagoByNombre_NoExiste_Retorna404() throws Exception {
        when(metodoPagoService.findByNombre("Nada"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/metodos-pago/buscar/Nada"))
                .andExpect(status().isNotFound());
    }
}