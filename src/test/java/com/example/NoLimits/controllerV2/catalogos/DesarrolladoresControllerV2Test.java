package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.DesarrolladoresModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.DesarrolladoresControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladoresResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladoresService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DesarrolladoresControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class DesarrolladoresControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DesarrolladoresService desarrolladoresService;

    @MockBean
    private DesarrolladoresModelAssembler desarrolladoresAssembler;

    private DesarrolladoresResponseDTO dto() {
        DesarrolladoresResponseDTO dto = new DesarrolladoresResponseDTO();
        dto.setId(1L);
        dto.setProductoId(10L);
        dto.setDesarrolladorId(1L);
        dto.setDesarrolladorNombre("Insomniac Games");
        return dto;
    }

    private EntityModel<DesarrolladoresResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/productos/10/desarrolladores/1").withSelfRel());
    }

    @Test
    void listar_ConDatos_Retorna200() throws Exception {
        when(desarrolladoresService.findByProducto(10L)).thenReturn(List.of(dto()));
        when(desarrolladoresAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/productos/10/desarrolladores"))
                .andExpect(status().isOk());
    }

    @Test
    void listar_SinDatos_Retorna204() throws Exception {
        when(desarrolladoresService.findByProducto(10L)).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/productos/10/desarrolladores"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_DatosValidos_Retorna201() throws Exception {
        when(desarrolladoresService.link(any())).thenReturn(dto());
        when(desarrolladoresAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/productos/10/desarrolladores/1"))
                .andExpect(status().isOk());
    }

    @Test
    void unlink_Existe_Retorna204() throws Exception {
        doNothing().when(desarrolladoresService).unlink(10L, 1L);
        mockMvc.perform(delete("/api/v2/productos/10/desarrolladores/1"))
                .andExpect(status().isNoContent());
    }
}