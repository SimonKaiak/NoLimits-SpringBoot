// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/EmpresasController.java
package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.EmpresasModel;
import com.example.NoLimits.Multimedia.service.EmpresasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos/{productoId}/empresas")
@Tag(name = "Empresas-Controller", description = "Relación Producto ↔ Empresa (TP).")
public class EmpresasController {

    @Autowired
    private EmpresasService empresasService;

    @GetMapping
    @Operation(summary = "Listar empresas asociadas a un producto")
    public List<EmpresasModel> listarPorProducto(@PathVariable Long productoId) {
        return empresasService.findByProducto(productoId);
    }

    @PostMapping("/{empresaId}")
    @Operation(summary = "Vincular Producto ↔ Empresa")
    public ResponseEntity<EmpresasModel> link(
            @PathVariable Long productoId,
            @PathVariable Long empresaId
    ) {
        EmpresasModel rel = empresasService.link(productoId, empresaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(rel);
    }

    @DeleteMapping("/{empresaId}")
    @Operation(summary = "Desvincular Producto ↔ Empresa")
    public ResponseEntity<Void> unlink(
            @PathVariable Long productoId,
            @PathVariable Long empresaId
    ) {
        empresasService.unlink(productoId, empresaId);
        return ResponseEntity.noContent().build();
    }
}