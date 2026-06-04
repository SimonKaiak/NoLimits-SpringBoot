package com.example.NoLimits.controller.producto;

import com.example.NoLimits.Multimedia.controller.producto.DetalleVentaController;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;
import com.example.NoLimits.Multimedia.service.producto.DetalleVentaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DetalleVentaController.class)
@AutoConfigureMockMvc(addFilters = false)
class DetalleVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DetalleVentaService detalleVentaService;

    @Test
    @DisplayName("Debe listar detalles de venta")
    void debeListarDetallesVenta() throws Exception {

        when(detalleVentaService.findAll())
                .thenReturn(List.of(new DetalleVentaResponseDTO()));

        mockMvc.perform(get("/api/v1/detalles-venta"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existen detalles")
    void debeRetornarNoContentCuandoNoExistenDetalles() throws Exception {

        when(detalleVentaService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/detalles-venta"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar detalle por ID")
    void debeBuscarDetallePorId() throws Exception {

        when(detalleVentaService.findById(1L))
                .thenReturn(new DetalleVentaResponseDTO());

        mockMvc.perform(get("/api/v1/detalles-venta/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe crear detalle de venta")
    void debeCrearDetalleVenta() throws Exception {

        when(detalleVentaService.save(any()))
                .thenReturn(new DetalleVentaResponseDTO());

        mockMvc.perform(post("/api/v1/detalles-venta")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Debe actualizar detalle con PUT")
    void debeActualizarDetalleConPut() throws Exception {

        when(detalleVentaService.update(eq(1L), any()))
                .thenReturn(new DetalleVentaResponseDTO());

        mockMvc.perform(put("/api/v1/detalles-venta/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe actualizar detalle con PATCH")
    void debeActualizarDetalleConPatch() throws Exception {

        when(detalleVentaService.patch(eq(1L), any()))
                .thenReturn(new DetalleVentaResponseDTO());

        mockMvc.perform(patch("/api/v1/detalles-venta/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe eliminar detalle de venta")
    void debeEliminarDetalleVenta() throws Exception {

        doNothing().when(detalleVentaService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/detalles-venta/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar detalles por venta")
    void debeBuscarDetallesPorVenta() throws Exception {

        when(detalleVentaService.findByVenta(1L))
                .thenReturn(List.of(new DetalleVentaResponseDTO()));

        mockMvc.perform(get("/api/v1/detalles-venta/venta/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando la venta no tiene detalles")
    void debeRetornarNoContentCuandoVentaNoTieneDetalles() throws Exception {

        when(detalleVentaService.findByVenta(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/detalles-venta/venta/1"))
                .andExpect(status().isNoContent());
    }
}