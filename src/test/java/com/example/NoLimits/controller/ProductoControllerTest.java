package com.example.NoLimits.controller;

import com.example.NoLimits.Multimedia.controller.producto.ProductoController;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResumenDTO;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Test
    @DisplayName("Debe listar productos correctamente")
    void debeListarProductos() throws Exception {

        ProductoResumenDTO producto = new ProductoResumenDTO();

        when(productoService.findAll())
                .thenReturn(List.of(producto));

        mockMvc.perform(get("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 si no hay productos")
    void debeRetornarNoContentSiNoHayProductos() throws Exception {
        when(productoService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe listar productos paginados")
    void debeListarProductosPaginados() throws Exception {
        ProductoResumenDTO producto = new ProductoResumenDTO();

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(producto), 1, 1, 1);

        when(productoService.findAllPaged(1, 20)).thenReturn(respuesta);

        mockMvc.perform(get("/api/v1/productos/paginacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagina").value(1))
                .andExpect(jsonPath("$.totalPaginas").value(1))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    @DisplayName("Debe buscar producto por ID")
    void debeBuscarProductoPorId() throws Exception {
        ProductoResponseDTO producto = new ProductoResponseDTO();

        when(productoService.findById(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe crear producto")
    void debeCrearProducto() throws Exception {
        ProductoResponseDTO producto = new ProductoResponseDTO();

        when(productoService.save(any())).thenReturn(producto);

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Debe actualizar producto con PUT")
    void debeActualizarProductoConPut() throws Exception {
        ProductoResponseDTO producto = new ProductoResponseDTO();

        when(productoService.update(eq(1L), any())).thenReturn(producto);

        mockMvc.perform(put("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe editar producto con PATCH")
    void debeEditarProductoConPatch() throws Exception {
        ProductoResponseDTO producto = new ProductoResponseDTO();

        when(productoService.patch(eq(1L), any())).thenReturn(producto);

        mockMvc.perform(patch("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe eliminar producto")
    void debeEliminarProducto() throws Exception {
        doNothing().when(productoService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNoContent());
    }
 
    @Test
    @DisplayName("Debe listar sagas")
    void debeListarSagas() throws Exception {
        when(productoService.obtenerSagasDistinct())
                .thenReturn(List.of("Spider-Man"));

        mockMvc.perform(get("/api/v1/productos/sagas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Spider-Man"));
    }

    @Test
    @DisplayName("Debe retornar 204 si no hay sagas")
    void debeRetornarNoContentSiNoHaySagas() throws Exception {
        when(productoService.obtenerSagasDistinct())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos/sagas"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe verificar si existe producto por link")
    void debeVerificarSiExisteProductoPorLink() throws Exception {
        when(productoService.existeProductoPorLinkCompra("https://mercadolibre.cl/producto"))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/productos/existe-link")
                .param("url", "https://mercadolibre.cl/producto"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Debe buscar productos por nombre")
    void debeBuscarProductosPorNombre() throws Exception {

        ProductoResumenDTO producto = new ProductoResumenDTO();

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(producto), 1, 1, 1);

        when(productoService.findByNombreContainingIgnoreCase(
                eq("spider"), eq(1), eq(20)))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v1/productos/buscar")
                .param("nombre", "spider"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no encuentra productos por nombre")
    void debeRetornarNoContentCuandoNoEncuentraPorNombre() throws Exception {

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(), 1, 0, 0);

        when(productoService.findByNombreContainingIgnoreCase(
                eq("inexistente"), eq(1), eq(20)))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v1/productos/buscar")
                .param("nombre", "inexistente"))
                .andExpect(status().isNoContent());
    
    }

    @Test
    @DisplayName("Debe buscar productos por tipo")
    void debeBuscarProductosPorTipo() throws Exception {

        ProductoResumenDTO producto = new ProductoResumenDTO();

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(producto), 1, 1, 1);

        when(productoService.findByTipoProducto(1L, 1, 20))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v1/productos/tipo/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar producto por estado")
    void  debeBuscarProductosPorEstado() throws Exception {

        ProductoResumenDTO producto = new ProductoResumenDTO();

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(producto), 1, 1, 1);

        when(productoService.findByEstado(1L, 1, 20))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v1/productos/estado/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar productos por saga")
    void debeBuscarProductosPorSaga() throws Exception {
        ProductoResumenDTO producto = new ProductoResumenDTO();

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(producto), 1, 1, 1);

        when(productoService.findBySagaIgnoreCase("Spider-Man", 1, 20))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v1/productos/sagas/Spider-Man"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Debe actualizar precio desde Steam")
    void debeActualizarPrecioDesdeSteam() throws Exception {
        ProductoResponseDTO producto = new ProductoResponseDTO();

        when(productoService.actualizarPrecioDesdeSteam(1L))
                .thenReturn(producto);

        mockMvc.perform(patch("/api/v1/productos/1/actualizar-precio-steam"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe listar sagas con portada")
    void debeListarSagasConPortada() throws Exception {

        when(productoService.obtenerSagasConPortada())
                .thenReturn(List.of(Map.of("saga", "Spider-Man")));

        mockMvc.perform(get("/api/v1/productos/sagas/resumen"))
                .andExpect(status().isOk());
        
    }

    @Test
    @DisplayName("Debe listar sagas por tipo de producto")
    void debeListarSagasPorTipoProducto() throws Exception {

        when(productoService.obtenerSagasDistinctPorTipoProducto(1L))
                .thenReturn(List.of("Spider-Man"));

        mockMvc.perform(get("/api/v1/productos/sagas/tipo/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe obtener productos completos de una saga")
    void debeObtenerProductosCompletosDeSaga() throws Exception {

        ProductoResponseDTO producto = new ProductoResponseDTO();

        when(productoService.findBySagaCompleto("Spider-Man"))
                .thenReturn(List.of(producto));

        mockMvc.perform(get("/api/v1/productos/sagas/Spider-Man/completo"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe obtener resumen de productos")
    void debeObtenerResumenProductos() throws Exception {

        when(productoService.obtenerProductosConDatos())
                .thenReturn(List.of(Map.of("id", 1)));

        mockMvc.perform(get("/api/v1/productos/resumen"))
                .andExpect(status().isOk());
    }


}