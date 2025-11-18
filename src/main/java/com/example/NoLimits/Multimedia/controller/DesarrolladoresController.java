package com.example.NoLimits.Multimedia.controller;

import java.util.List;

import com.example.NoLimits.Multimedia.model.DesarrolladoresModel;
import com.example.NoLimits.Multimedia.service.DesarrolladoresService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos/{productoId}/desarrolladores")
@Tag(name = "Desarrolladores-Controller", description = "Relación Producto ↔ Desarrollador (TP).")
public class DesarrolladoresController {

    @Autowired
    private DesarrolladoresService service;

    // ================== LISTAR ==================
    @GetMapping
    @Operation(summary = "Listar desarrolladores asociados a un producto")
    public List<DesarrolladoresModel> listar(@PathVariable Long productoId) {
        return service.findByProducto(productoId);
    }

    // ================== VINCULAR DESARROLLADOR ==================
    @PostMapping("/{desarrolladorId}")
    @Operation(summary = "Vincular Producto ↔ Desarrollador")
    public DesarrolladoresModel link(
            @PathVariable Long productoId,
            @PathVariable Long desarrolladorId
    ) {
        return service.link(productoId, desarrolladorId);
    }

    // ================== PATCH RELACIÓN ==================
    // Usa el ID de la relación (tabla puente) para actualizar producto y/o desarrollador.
    @PatchMapping("/relaciones/{relacionId}")
    @Operation(summary = "Actualizar parcialmente la relación Producto ↔ Desarrollador")
    public DesarrolladoresModel patch(
            @PathVariable Long productoId,
            @PathVariable Long relacionId,
            @RequestBody DesarrolladoresModel parciales
    ) {
        // productoId queda en la ruta por consistencia REST,
        // pero la lógica de patch se basa en el ID de la relación.
        return service.patch(relacionId, parciales);
    }

    // ================== DESVINCULAR DESARROLLADOR ==================
    @DeleteMapping("/{desarrolladorId}")
    @Operation(summary = "Desvincular Producto ↔ Desarrollador")
    public void unlink(
            @PathVariable Long productoId,
            @PathVariable Long desarrolladorId
    ) {
        service.unlink(productoId, desarrolladorId);
    }
}