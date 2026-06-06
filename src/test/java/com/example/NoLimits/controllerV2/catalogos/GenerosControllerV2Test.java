package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.GenerosModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.GenerosControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GenerosResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.GenerosService;

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

@WebMvcTest(GenerosControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class GenerosControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenerosService generosService;

    @MockBean
    private GenerosModelAssembler generosAssembler;

    private GenerosResponseDTO dto() {
        GenerosResponseDTO dto = new GenerosResponseDTO();
        dto.setId(1L);
        dto.setProductoId(10L);
        dto.setGeneroId(1L);
        dto.setGeneroNombre("Acción");
        return dto;
    }

    private EntityModel<GenerosResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/productos-generos/1").withSelfRel());
    }

    @Test
    void findByProducto_ConDatos_Retorna200() throws Exception {
        when(generosService.findByProducto(10L)).thenReturn(List.of(dto()));
        when(generosAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/productos-generos/producto/10"))
                .andExpect(status().isOk());
    }

    @Test
    void findByProducto_SinDatos_Retorna204() throws Exception {
        when(generosService.findByProducto(10L)).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/productos-generos/producto/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_DatosValidos_Retorna201() throws Exception {
        when(generosService.link(10L, 1L)).thenReturn(dto());
        when(generosAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/productos-generos/producto/10/genero/1"))
                .andExpect(status().isOk());
    }

    @Test
    void unlink_Existe_Retorna204() throws Exception {
        doNothing().when(generosService).unlink(10L, 1L);
        mockMvc.perform(delete("/api/v2/productos-generos/producto/10/genero/1"))
                .andExpect(status().isNoContent());
    }
}