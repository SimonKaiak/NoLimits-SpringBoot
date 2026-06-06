package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.PlataformasModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.PlataformasControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformasResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformasService;

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

@WebMvcTest(PlataformasControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class PlataformasControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlataformasService plataformasService;

    @MockBean
    private PlataformasModelAssembler plataformasAssembler;

    private PlataformasResponseDTO dto() {
        PlataformasResponseDTO dto = new PlataformasResponseDTO();
        dto.setId(1L);
        dto.setProductoId(10L);
        dto.setPlataformaId(1L);
        dto.setPlataformaNombre("PC");
        return dto;
    }

    private EntityModel<PlataformasResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/productos/10/plataformas/1").withSelfRel());
    }

    @Test
    void listar_ConDatos_Retorna200() throws Exception {
        when(plataformasService.findByProducto(10L)).thenReturn(List.of(dto()));
        when(plataformasAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/productos/10/plataformas"))
                .andExpect(status().isOk());
    }

    @Test
    void listar_SinDatos_Retorna204() throws Exception {
        when(plataformasService.findByProducto(10L)).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/productos/10/plataformas"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_DatosValidos_Retorna201() throws Exception {
        when(plataformasService.link(10L, 1L)).thenReturn(dto());
        when(plataformasAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/productos/10/plataformas/1"))
                .andExpect(status().isCreated());
    }

    @Test
    void unlink_Existe_Retorna204() throws Exception {
        doNothing().when(plataformasService).unlink(10L, 1L);
        mockMvc.perform(delete("/api/v2/productos/10/plataformas/1"))
                .andExpect(status().isNoContent());
    }
}