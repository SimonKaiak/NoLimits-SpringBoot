package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;
import java.util.Map;

import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformasResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos/{productoId}/plataformas")
@Tag(name = "Plataformas-Controller", description = "Relación Producto ↔ Plataforma (TP).")
public class PlataformasController {

    @Autowired
    private PlataformasService plataformasService;

    @GetMapping
    @Operation(summary = "Listar plataformas asociadas a un producto")
    public ResponseEntity<List<PlataformasResponseDTO>> listarPorProducto(@PathVariable Long productoId) {
        List<PlataformasResponseDTO> lista = plataformasService.findByProducto(productoId);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{plataformaId}")
    @Operation(summary = "Vincular Producto ↔ Plataforma")
    public ResponseEntity<PlataformasResponseDTO> link(
            @PathVariable Long productoId,
            @PathVariable Long plataformaId
    ) {
        PlataformasResponseDTO rel = plataformasService.link(productoId, plataformaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(rel);
    }

    @PatchMapping("/{relacionId}")
    @Operation(
        summary = "Actualizar parcialmente la relación Producto ↔ Plataforma (PATCH)",
        description = "Permite cambiar el producto o la plataforma asociados a la relación."
    )
    public ResponseEntity<PlataformasResponseDTO> patch(
            @PathVariable Long productoId,
            @PathVariable Long relacionId,
            @RequestBody Map<String, Long> body
    ) {
        Long nuevoProductoId   = body.get("nuevoProductoId");
        Long nuevaPlataformaId = body.get("nuevaPlataformaId");

        PlataformasResponseDTO actualizada =
                plataformasService.patch(relacionId, nuevoProductoId, nuevaPlataformaId);

        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{plataformaId}")
    @Operation(summary = "Desvincular Producto ↔ Plataforma")
    public ResponseEntity<Void> unlink(
            @PathVariable Long productoId,
            @PathVariable Long plataformaId
    ) {
        plataformasService.unlink(productoId, plataformaId);
        return ResponseEntity.noContent().build();
    }
}