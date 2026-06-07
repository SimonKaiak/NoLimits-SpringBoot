package com.example.NoLimits.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
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
import static org.mockito.ArgumentMatchers.eq;
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
    void findByGenero_ConDatos_Retorna200() throws Exception {
        when(generosService.findByGenero(1L)).thenReturn(List.of(dto()));
        when(generosAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(get("/api/v2/productos-generos/genero/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findByGenero_SinDatos_Retorna204() throws Exception {
        when(generosService.findByGenero(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/productos-generos/genero/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void link_DatosValidos_Retorna200() throws Exception {
        when(generosService.link(10L, 1L)).thenReturn(dto());
        when(generosAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(post("/api/v2/productos-generos/producto/10/genero/1"))
                .andExpect(status().isOk());
    }

    @Test
    void link_NoExiste_Retorna404() throws Exception {
        when(generosService.link(10L, 99L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(post("/api/v2/productos-generos/producto/10/genero/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void unlink_Existe_Retorna204() throws Exception {
        doNothing().when(generosService).unlink(10L, 1L);
        mockMvc.perform(delete("/api/v2/productos-generos/producto/10/genero/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void unlink_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado"))
                .when(generosService).unlink(10L, 99L);
        mockMvc.perform(delete("/api/v2/productos-generos/producto/10/genero/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_DatosValidos_Retorna200() throws Exception {
        when(generosService.patch(eq(1L), any(), any())).thenReturn(dto());
        when(generosAssembler.toModel(any())).thenReturn(entityModel());
        mockMvc.perform(patch("/api/v2/productos-generos/1")
                .param("nuevoProductoId", "20")
                .param("nuevoGeneroId", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void patch_NoExiste_Retorna404() throws Exception {
        when(generosService.patch(eq(99L), any(), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));
        mockMvc.perform(patch("/api/v2/productos-generos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void resumen_ConDatos_Retorna200() throws Exception {
        List<Object[]> filas = new java.util.ArrayList<>();
        filas.add(new Object[]{1L, 10L, "Halo", 1L, "Acción"});
        when(generosService.obtenerResumen(null, null)).thenReturn(filas);
        mockMvc.perform(get("/api/v2/productos-generos/resumen"))
                .andExpect(status().isOk());
    }

    @Test
    void resumen_SinDatos_Retorna204() throws Exception {
        when(generosService.obtenerResumen(null, null)).thenReturn(List.of());
        mockMvc.perform(get("/api/v2/productos-generos/resumen"))
                .andExpect(status().isNoContent());
    }
}