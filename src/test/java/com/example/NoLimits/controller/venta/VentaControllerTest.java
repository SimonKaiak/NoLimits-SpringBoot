package com.example.NoLimits.controller.venta;

import com.example.NoLimits.Multimedia.controller.venta.VentaController;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;
import com.example.NoLimits.Multimedia.service.venta.VentaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
@AutoConfigureMockMvc(addFilters = false)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    @Test
    @DisplayName("Debe listar ventas")
    void debeListarVentas() throws Exception {

        when(ventaService.findAll())
                .thenReturn(List.of(new VentaResponseDTO()));

        mockMvc.perform(get("/api/v1/ventas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existen ventas")
    void debeRetornarNoContentCuandoNoExistenVentas() throws Exception {

        when(ventaService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/ventas"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe listar ventas paginadas")
    void debeListarVentasPaginadas() throws Exception {

        PagedResponse<VentaResponseDTO> respuesta =
                new PagedResponse<>(List.of(new VentaResponseDTO()), 1, 1, 1);

        when(ventaService.findAllPaged(1, 4))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v1/ventas/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagina").value(1));
    }

    @Test
    @DisplayName("Debe buscar venta por ID")
    void debeBuscarVentaPorId() throws Exception {

        when(ventaService.findById(1L))
                .thenReturn(new VentaResponseDTO());

        mockMvc.perform(get("/api/v1/ventas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe eliminar venta")
    void debeEliminarVenta() throws Exception {

        doNothing().when(ventaService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/ventas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar ventas por método de pago")
    void debeBuscarVentasPorMetodoPago() throws Exception {

        when(ventaService.findByMetodoPago(1L))
                .thenReturn(List.of(new VentaResponseDTO()));

        mockMvc.perform(get("/api/v1/ventas/metodopago/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existen ventas por método de pago")
    void debeRetornarNoContentMetodoPago() throws Exception {

        when(ventaService.findByMetodoPago(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/ventas/metodopago/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe obtener resumen de ventas")
    void debeObtenerResumenVentas() throws Exception {

        when(ventaService.obtenerVentasConDatos())
                .thenReturn(List.of(Map.of("id", 1)));

        mockMvc.perform(get("/api/v1/ventas/resumen"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existe resumen")
    void debeRetornarNoContentResumen() throws Exception {

        when(ventaService.obtenerVentasConDatos())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/ventas/resumen"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe actualizar venta con PUT")
    void debeActualizarVentaConPut() throws Exception {

        when(ventaService.update(eq(1L), any()))
                .thenReturn(new VentaResponseDTO());

        mockMvc.perform(put("/api/v1/ventas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "metodoPagoId":1,
                            "metodoEnvioId":1,
                            "estadoId":1
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe actualizar venta con PATCH")
    void debeActualizarVentaConPatch() throws Exception {

        when(ventaService.patch(eq(1L), any()))
                .thenReturn(new VentaResponseDTO());

        mockMvc.perform(patch("/api/v1/ventas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "estadoId":2
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe actualizar venta con PUT usando datos mínimos")
    void debeActualizarVentaConPutMinimo() throws Exception {

        when(ventaService.update(eq(2L), any()))
                .thenReturn(new VentaResponseDTO());

        mockMvc.perform(put("/api/v1/ventas/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "estadoId":3
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe actualizar venta con PATCH usando estado")
    void debeActualizarVentaConPatchEstado() throws Exception {

        when(ventaService.patch(eq(2L), any()))
                .thenReturn(new VentaResponseDTO());

        mockMvc.perform(patch("/api/v1/ventas/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "estadoId":4
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe consultar paginado con parámetros")
    void debeConsultarPaginadoConParametros() throws Exception {

        PagedResponse<VentaResponseDTO> respuesta =
                new PagedResponse<>(List.of(new VentaResponseDTO()), 2, 5, 10);

        when(ventaService.findAllPaged(2, 5))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v1/ventas/paginado")
                .param("page", "2")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagina").value(2));
    }
}