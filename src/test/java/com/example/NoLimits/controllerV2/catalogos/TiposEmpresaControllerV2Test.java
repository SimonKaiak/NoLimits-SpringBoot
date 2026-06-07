package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.TiposEmpresaModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TiposEmpresaControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TiposEmpresaService;

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

@WebMvcTest(TiposEmpresaControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class TiposEmpresaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TiposEmpresaService tiposEmpresaService;

    @MockBean
    private TiposEmpresaModelAssembler tiposEmpresaAssembler;

    private TiposEmpresaResponseDTO dto() {
        TiposEmpresaResponseDTO dto = new TiposEmpresaResponseDTO();
        dto.setId(1L);
        dto.setEmpresaId(3L);
        dto.setTipoEmpresaId(2L);
        return dto;
    }

    private EntityModel<TiposEmpresaResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/empresas/3/tipos-empresa/1").withSelfRel());
    }

    @Test
    void listar_ConDatos_Retorna200() throws Exception {
        when(tiposEmpresaService.findAllByEmpresa(3L)).thenReturn(List.of(dto()));
        when(tiposEmpresaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/empresas/3/tipos-empresa"))
                .andExpect(status().isOk());
    }

    @Test
    void listar_SinDatos_Retorna204() throws Exception {
        when(tiposEmpresaService.findAllByEmpresa(3L)).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/empresas/3/tipos-empresa"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_DatosValidos_Retorna201() throws Exception {
        when(tiposEmpresaService.link(3L, 2L)).thenReturn(dto());
        when(tiposEmpresaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/empresas/3/tipos-empresa/2"))
                .andExpect(status().isCreated());
    }

    @Test
    void patch_DatosValidos_Retorna200() throws Exception {
        when(tiposEmpresaService.patch(eq(1L), any())).thenReturn(dto());
        when(tiposEmpresaAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/empresas/3/tipos-empresa/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipoEmpresaId\":5}"))
                .andExpect(status().isOk());
    }

    @Test
    void unlink_Existe_Retorna204() throws Exception {
        doNothing().when(tiposEmpresaService).unlink(3L, 2L);
        mockMvc.perform(delete("/api/v2/empresas/3/tipos-empresa/2"))
                .andExpect(status().isNoContent());
    }
}