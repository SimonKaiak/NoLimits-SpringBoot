package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;

import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladoresRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladoresResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.DesarrolladoresUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladoresService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos/{productoId}/desarrolladores")
@Tag(name = "Desarrolladores-Controller", description = "Relación Producto ↔ Desarrollador (TP).")
public class DesarrolladoresController {

    @Autowired
    private DesarrolladoresService service;

    // ================== LISTAR ==================
    @GetMapping
    @Operation(summary = "Listar desarrolladores asociados a un producto")
    public List<DesarrolladoresResponseDTO> listar(@PathVariable Long productoId) {
        return service.findByProducto(productoId);
    }

    // ================== VINCULAR DESARROLLADOR ==================
    @PostMapping("/{desarrolladorId}")
    @Operation(summary = "Vincular Producto ↔ Desarrollador")
    public DesarrolladoresResponseDTO link(
            @PathVariable Long productoId,
            @PathVariable Long desarrolladorId,
            // El body es opcional por si en el futuro agregas más datos a la relación
            @Valid @RequestBody(required = false) DesarrolladoresRequestDTO requestDTO
    ) {
        // El RequestDTO permite enviar datos adicionales de la relación
        // como rol, prioridad, observaciones u otros campos de la tabla puente.

        DesarrolladoresRequestDTO dto = (requestDTO != null)
                ? requestDTO
                : new DesarrolladoresRequestDTO();

        // Forzamos que los IDs vengan desde la ruta
        dto.setProductoId(productoId);
        dto.setDesarrolladorId(desarrolladorId);

        return service.link(dto);
    }

    // ================== PATCH RELACIÓN ==================
    // Usa el ID de la relación (tabla puente) para actualizar producto y/o desarrollador.
    @PatchMapping("/relaciones/{relacionId}")
    @Operation(summary = "Actualizar parcialmente la relación Producto ↔ Desarrollador")
    public DesarrolladoresResponseDTO patch(
            @PathVariable Long productoId,
            @PathVariable Long relacionId,
            @RequestBody DesarrolladoresUpdateDTO updateDTO
    ) {
        // productoId queda en la ruta por consistencia REST,
        // pero la lógica de patch se basa en el ID de la relación.
        return service.patch(relacionId, updateDTO);
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