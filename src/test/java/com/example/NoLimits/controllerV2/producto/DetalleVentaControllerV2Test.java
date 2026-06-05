package com.example.NoLimits.controllerV2.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.producto.DetalleVentaModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.producto.DetalleVentaControllerV2;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;
import com.example.NoLimits.Multimedia.service.producto.DetalleVentaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DetalleVentaControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class DetalleVentaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DetalleVentaService detalleVentaService;

    @MockBean
    private DetalleVentaModelAssembler detalleVentaAssembler;

    @Test
    @DisplayName("Debe listar detalles de venta")
    void debeListarDetallesVenta() throws Exception {

        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();
        dto.setId(1L);

        when(detalleVentaService.findAll())
                .thenReturn(List.of(dto));

        when(detalleVentaAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/detalles-venta"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existen detalles")
    void debeRetornarNoContent() throws Exception {

        when(detalleVentaService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/detalles-venta"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar detalle por ID")
    void debeBuscarDetallePorId() throws Exception {

        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();
        dto.setId(1L);

        when(detalleVentaService.findById(1L))
                .thenReturn(dto);

        when(detalleVentaAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/detalles-venta/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 cuando detalle no existe")
    void debeRetornar404DetalleNoExiste() throws Exception {

        when(detalleVentaService.findById(999L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(get("/api/v2/detalles-venta/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe listar detalles por venta")
    void debeListarDetallesPorVenta() throws Exception {

        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();
        dto.setId(1L);

        when(detalleVentaService.findByVenta(1L))
                .thenReturn(List.of(dto));

        when(detalleVentaAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/detalles-venta/venta/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando venta no tiene detalles")
    void debeRetornarNoContentPorVenta() throws Exception {

        when(detalleVentaService.findByVenta(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/detalles-venta/venta/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe crear detalle venta")
    void debeCrearDetalleVenta() throws Exception {

        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();
        dto.setId(1L);

        when(detalleVentaService.save(any()))
                .thenReturn(dto);

        when(detalleVentaAssembler.toModel(any()))
                .thenReturn(
                        EntityModel.of(dto)
                                .add(Link.of(
                                        "http://localhost/api/v2/detalles-venta/1",
                                        "self"))
                );

        mockMvc.perform(post("/api/v2/detalles-venta")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "productoId":1,
                            "cantidad":2,
                            "precioUnitario":12990
                        }
                        """))
                .andExpect(status().isCreated());
    }
    @Test
    @DisplayName("Debe actualizar detalle con PATCH")
    void debeActualizarDetalleConPatch() throws Exception {

        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();
        dto.setId(1L);

        when(detalleVentaService.patch(eq(1L), any()))
                .thenReturn(dto);

        when(detalleVentaAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(patch("/api/v2/detalles-venta/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "cantidad":3,
                            "precioUnitario":14990
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar detalle")
    void debeRetornar404Patch() throws Exception {

        when(detalleVentaService.patch(eq(999L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(patch("/api/v2/detalles-venta/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "cantidad":3
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe eliminar detalle")
    void debeEliminarDetalle() throws Exception {

        doNothing().when(detalleVentaService)
                .deleteById(1L);

        mockMvc.perform(delete("/api/v2/detalles-venta/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe retornar 404 al eliminar detalle")
    void debeRetornar404Delete() throws Exception {

        doThrow(new RecursoNoEncontradoException("No encontrado"))
                .when(detalleVentaService)
                .deleteById(999L);

        mockMvc.perform(delete("/api/v2/detalles-venta/999"))
                .andExpect(status().isNotFound());
    }

    
}