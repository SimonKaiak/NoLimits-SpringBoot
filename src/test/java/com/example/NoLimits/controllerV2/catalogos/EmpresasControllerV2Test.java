package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.EmpresasModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.EmpresasControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresasResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresasService;

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

@WebMvcTest(EmpresasControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class EmpresasControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpresasService empresasService;

    @MockBean
    private EmpresasModelAssembler empresasAssembler;

    private EmpresasResponseDTO dto() {
        EmpresasResponseDTO dto = new EmpresasResponseDTO();
        dto.setId(1L);
        dto.setProductoId(10L);
        dto.setEmpresaId(1L);
        return dto;
    }

    private EntityModel<EmpresasResponseDTO> entityModel() {
        return EntityModel.of(dto(),
                Link.of("http://localhost/api/v2/productos/10/empresas/1").withSelfRel());
    }

    @Test
    void listar_ConDatos_Retorna200() throws Exception {
        when(empresasService.findByProducto(10L)).thenReturn(List.of(dto()));
        when(empresasAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/productos/10/empresas"))
                .andExpect(status().isOk());
    }

    @Test
    void listar_SinDatos_Retorna204() throws Exception {
        when(empresasService.findByProducto(10L)).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/productos/10/empresas"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_DatosValidos_Retorna201() throws Exception {
        when(empresasService.link(10L, 1L)).thenReturn(dto());
        when(empresasAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/productos/10/empresas/1"))
                .andExpect(status().isCreated());
    }

    @Test
    void patch_DatosValidos_Retorna200() throws Exception {
        when(empresasService.patch(eq(1L), any())).thenReturn(dto());
        when(empresasAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/productos/10/empresas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":20}"))
                .andExpect(status().isOk());
    }

    @Test
    void unlink_Existe_Retorna204() throws Exception {
        doNothing().when(empresasService).unlink(10L, 1L);
        mockMvc.perform(delete("/api/v2/productos/10/empresas/1"))
                .andExpect(status().isNoContent());
    }
}