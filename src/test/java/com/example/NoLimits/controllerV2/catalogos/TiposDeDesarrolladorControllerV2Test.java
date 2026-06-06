package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.TiposDeDesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TiposDeDesarrolladorControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TiposDeDesarrolladorService;

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

@WebMvcTest(TiposDeDesarrolladorControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class TiposDeDesarrolladorControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TiposDeDesarrolladorService tiposDeDesarrolladorService;

    @MockBean
    private TiposDeDesarrolladorModelAssembler tiposDeDesarrolladorAssembler;

    private TiposDeDesarrolladorResponseDTO dto() {
        TiposDeDesarrolladorResponseDTO dto = new TiposDeDesarrolladorResponseDTO();
        dto.setId(1L);
        dto.setDesarrolladorId(10L);
        dto.setTipoDeDesarrolladorId(1L);
        dto.setTipoDeDesarrolladorNombre("Estudio");
        return dto;
    }

    private EntityModel<TiposDeDesarrolladorResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/desarrolladores/10/tipos/1").withSelfRel());
    }

    @Test
    void listar_ConDatos_Retorna200() throws Exception {
        when(tiposDeDesarrolladorService.findByDesarrollador(10L)).thenReturn(List.of(dto()));
        when(tiposDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/desarrolladores/10/tipos"))
                .andExpect(status().isOk());
    }

    @Test
    void listar_SinDatos_Retorna204() throws Exception {
        when(tiposDeDesarrolladorService.findByDesarrollador(10L)).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/desarrolladores/10/tipos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_DatosValidos_Retorna201() throws Exception {
        when(tiposDeDesarrolladorService.link(10L, 1L)).thenReturn(dto());
        when(tiposDeDesarrolladorAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/desarrolladores/10/tipos/1"))
                .andExpect(status().isCreated());
    }

    @Test
    void unlink_Existe_Retorna204() throws Exception {
        doNothing().when(tiposDeDesarrolladorService).unlink(10L, 1L);
        mockMvc.perform(delete("/api/v2/desarrolladores/10/tipos/1"))
                .andExpect(status().isNoContent());
    }
}