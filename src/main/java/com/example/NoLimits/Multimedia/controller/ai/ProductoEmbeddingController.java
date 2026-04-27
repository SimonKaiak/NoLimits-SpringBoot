package com.example.NoLimits.Multimedia.controller.ai;

import com.example.NoLimits.Multimedia.service.ai.ProductoEmbeddingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/embeddings")
public class ProductoEmbeddingController {

    private final ProductoEmbeddingService productoEmbeddingService;

    public ProductoEmbeddingController(ProductoEmbeddingService productoEmbeddingService) {
        this.productoEmbeddingService = productoEmbeddingService;
    }

    @PostMapping("/producto/{id}")
    public String crearEmbeddingProducto(
            @PathVariable Long id,
            @RequestBody String contenido
    ) {
        productoEmbeddingService.guardarEmbeddingProducto(id, contenido);
        return "Embedding guardado correctamente";
    }

    @GetMapping("/buscar")
    public List<String> buscar(@RequestParam String q) {
        return productoEmbeddingService.buscarSimilares(q);
    }

    @PostMapping("/indexar")
    public String indexar() {
        int total = productoEmbeddingService.indexarTodosLosProductos();
        return "Productos indexados: " + total;
    }
}