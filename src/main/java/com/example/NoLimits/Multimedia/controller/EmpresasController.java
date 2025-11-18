// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/EmpresasController.java
package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.EmpresasModel;
import com.example.NoLimits.Multimedia.service.EmpresasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos/{productoId}/empresas")
@Tag(name = "Empresas-Controller", description = "Relación Producto ↔ Empresa (TP).")
public class EmpresasController {

    @Autowired
    private EmpresasService empresasService;

    // --------------------------------------------------------
    // GET - Listar por producto
    // --------------------------------------------------------
    @GetMapping
    @Operation(summary = "Listar empresas asociadas a un producto")
    public List<EmpresasModel> listarPorProducto(@PathVariable Long productoId) {
        return empresasService.findByProducto(productoId);
    }

    // --------------------------------------------------------
    // POST - Vincular relación
    // --------------------------------------------------------
    @PostMapping("/{empresaId}")
    @Operation(summary = "Vincular Producto ↔ Empresa")
    public ResponseEntity<EmpresasModel> link(
            @PathVariable Long productoId,
            @PathVariable Long empresaId
    ) {
        EmpresasModel rel = empresasService.link(productoId, empresaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(rel);
    }

    // --------------------------------------------------------
    // PATCH - Actualizar relación Producto ↔ Empresa
    // --------------------------------------------------------
    @PatchMapping("/{relacionId}")
    @Operation(summary = "Actualizar parcialmente la relación Producto ↔ Empresa")
    public ResponseEntity<EmpresasModel> patch(
            @PathVariable Long relacionId,
            @RequestBody EmpresasModel cambios
    ) {
        EmpresasModel actualizado = empresasService.patch(relacionId, cambios);
        return ResponseEntity.ok(actualizado);
    }

    // --------------------------------------------------------
    // DELETE - Eliminar relación
    // --------------------------------------------------------
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