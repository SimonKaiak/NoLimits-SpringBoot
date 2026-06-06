package com.example.NoLimits.controllerV2.venta;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.venta.VentaModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.venta.VentaControllerV2;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;
import com.example.NoLimits.Multimedia.service.venta.VentaService;

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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class VentaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private VentaModelAssembler ventaAssembler;

    private VentaResponseDTO dto() {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(1L);
        dto.setUsuarioId(1L);
        return dto;
    }

    private EntityModel<VentaResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/ventas/1").withSelfRel());
    }

    @Test
    void getAll_ConDatos_Retorna200() throws Exception {
        when(ventaService.findAll()).thenReturn(List.of(dto()));
        when(ventaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/ventas")).andExpect(status().isOk());
    }

    @Test
    void getAll_SinDatos_Retorna204() throws Exception {
        when(ventaService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/ventas")).andExpect(status().isNoContent());
    }

    @Test
    void getById_Existe_Retorna200() throws Exception {
        when(ventaService.findById(1L)).thenReturn(dto());
        when(ventaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/ventas/1")).andExpect(status().isOk());
    }

    @Test
    void getById_NoExiste_Retorna404() throws Exception {
        when(ventaService.findById(99L)).thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(get("/api/v2/ventas/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_DatosValidos_Retorna201() throws Exception {
        when(ventaService.crearVentaDesdeRequest(any(), any())).thenReturn(dto());
        when(ventaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuarioId\":1,\"metodoPagoId\":1,\"metodoEnvioId\":1,\"detalles\":[]}"))
                .andExpect(status().isCreated());
    }

    @Test
    void update_Existe_Retorna200() throws Exception {
        when(ventaService.update(eq(1L), any())).thenReturn(dto());
        when(ventaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(put("/api/v2/ventas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuarioId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(ventaService.patch(eq(1L), any())).thenReturn(dto());
        when(ventaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/ventas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuarioId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_Existe_Retorna204() throws Exception {
        doNothing().when(ventaService).deleteById(1L);
        mockMvc.perform(delete("/api/v2/ventas/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(ventaService).deleteById(99L);
        mockMvc.perform(delete("/api/v2/ventas/99")).andExpect(status().isNotFound());
    }
}