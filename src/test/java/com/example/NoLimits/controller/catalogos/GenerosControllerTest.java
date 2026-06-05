package com.example.NoLimits.controller.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.controller.catalogos.GenerosController;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GenerosResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.GenerosService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenerosController.class)
@AutoConfigureMockMvc(addFilters = false)
class GenerosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenerosService generosService;

    private GenerosResponseDTO dto() {
        GenerosResponseDTO dto = new GenerosResponseDTO();
        dto.setId(5L);
        dto.setProductoId(10L);
        dto.setGeneroId(1L);
        dto.setGeneroNombre("Acción");
        return dto;
    }

    @Test
    void obtenerPorProducto_ConDatos_Retorna200() throws Exception {
        when(generosService.findByProducto(10L)).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/productos-generos/producto/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(10L))
                .andExpect(jsonPath("$[0].generoId").value(1L))
                .andExpect(jsonPath("$[0].generoNombre").value("Acción"));
    }

    @Test
    void obtenerPorProducto_SinDatos_Retorna204() throws Exception {
        when(generosService.findByProducto(10L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos-generos/producto/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerPorGenero_ConDatos_Retorna200() throws Exception {
        when(generosService.findByGenero(1L)).thenReturn(List.of(dto()));

        mockMvc.perform(get("/api/v1/productos-generos/genero/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(10L))
                .andExpect(jsonPath("$[0].generoId").value(1L))
                .andExpect(jsonPath("$[0].generoNombre").value("Acción"));
    }

    @Test
    void obtenerPorGenero_SinDatos_Retorna204() throws Exception {
        when(generosService.findByGenero(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos-generos/genero/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void vincular_Existe_Retorna200() throws Exception {
        when(generosService.link(10L, 1L)).thenReturn(dto());

        mockMvc.perform(post("/api/v1/productos-generos/producto/10/genero/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(10L))
                .andExpect(jsonPath("$.generoId").value(1L))
                .andExpect(jsonPath("$.generoNombre").value("Acción"));
    }

    @Test
    void vincular_NoExiste_Retorna404() throws Exception {
        when(generosService.link(99L, 1L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(post("/api/v1/productos-generos/producto/99/genero/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void desvincular_Existe_Retorna204() throws Exception {
        doNothing().when(generosService).unlink(10L, 1L);

        mockMvc.perform(delete("/api/v1/productos-generos/producto/10/genero/1"))
                .andExpect(status().isNoContent());

        verify(generosService).unlink(10L, 1L);
    }

    @Test
    void desvincular_NoExiste_Retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado"))
                .when(generosService).unlink(99L, 1L);

        mockMvc.perform(delete("/api/v1/productos-generos/producto/99/genero/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_Existe_Retorna200() throws Exception {
        when(generosService.patch(5L, 10L, 1L)).thenReturn(dto());

        mockMvc.perform(patch("/api/v1/productos-generos/5")
                        .param("nuevoProductoId", "10")
                        .param("nuevoGeneroId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(10L))
                .andExpect(jsonPath("$.generoId").value(1L))
                .andExpect(jsonPath("$.generoNombre").value("Acción"));
    }

    @Test
    void patch_NoExiste_Retorna404() throws Exception {
        when(generosService.patch(99L, 10L, 1L))
                .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(patch("/api/v1/productos-generos/99")
                        .param("nuevoProductoId", "10")
                        .param("nuevoGeneroId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void resumen_ConDatos_Retorna200() throws Exception {
        Object[] fila = new Object[] {
                5L,
                10L,
                "Producto Demo",
                1L,
                "Acción"
        };

        List<Object[]> filas = List.<Object[]>of(fila);

        when(generosService.obtenerResumen(null, null))
                .thenReturn(filas);

        mockMvc.perform(get("/api/v1/productos-generos/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].relacionId").value(5L))
                .andExpect(jsonPath("$[0].productoId").value(10L))
                .andExpect(jsonPath("$[0].productoNombre").value("Producto Demo"))
                .andExpect(jsonPath("$[0].generoId").value(1L))
                .andExpect(jsonPath("$[0].generoNombre").value("Acción"));
    }

    @Test
    void resumen_SinDatos_Retorna204() throws Exception {
        when(generosService.obtenerResumen(null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos-generos/resumen"))
                .andExpect(status().isNoContent());
    }
}