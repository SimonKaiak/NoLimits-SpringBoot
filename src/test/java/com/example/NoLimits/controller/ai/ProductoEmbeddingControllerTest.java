package com.example.NoLimits.controller.ai;

import com.example.NoLimits.Multimedia.controller.ai.ProductoEmbeddingController;
import com.example.NoLimits.Multimedia.service.ai.ProductoEmbeddingService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoEmbeddingController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ProductoEmbeddingControllerTest — Endpoints de embeddings de productos")
class ProductoEmbeddingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoEmbeddingService productoEmbeddingService;

    @Test
    @DisplayName("POST /producto/{id} → 200 OK con mensaje de confirmación")
    void crearEmbeddingProducto_retorna200() throws Exception {
        doNothing().when(productoEmbeddingService)
                .guardarEmbeddingProducto(anyLong(), anyString());

        mockMvc.perform(post("/api/embeddings/producto/1")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Descripción del producto"))
                .andExpect(status().isOk())
                .andExpect(content().string("Embedding guardado correctamente"));
    }

    @Test
    @DisplayName("GET /buscar?q=zelda → 200 OK con lista de resultados")
    void buscar_retorna200ConLista() throws Exception {
        when(productoEmbeddingService.buscarSimilares("zelda"))
                .thenReturn(List.of("Zelda Breath of the Wild", "Zelda Ocarina of Time"));

        mockMvc.perform(get("/api/embeddings/buscar").param("q", "zelda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("Zelda Breath of the Wild"));
    }

    @Test
    @DisplayName("GET /buscar?q=xyz → 200 OK con lista vacía")
    void buscar_sinResultados_retornaListaVacia() throws Exception {
        when(productoEmbeddingService.buscarSimilares("xyz"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/embeddings/buscar").param("q", "xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("POST /indexar → 200 OK con mensaje de total indexado")
    void indexar_retorna200() throws Exception {
        when(productoEmbeddingService.indexarTodosLosProductos()).thenReturn(75);

        mockMvc.perform(post("/api/embeddings/indexar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Productos indexados: 75"));
    }

    @Test
    @DisplayName("POST /indexar → 200 OK cuando no hay productos")
    void indexar_sinProductos_retorna200() throws Exception {
        when(productoEmbeddingService.indexarTodosLosProductos()).thenReturn(0);

        mockMvc.perform(post("/api/embeddings/indexar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Productos indexados: 0"));
    }
}