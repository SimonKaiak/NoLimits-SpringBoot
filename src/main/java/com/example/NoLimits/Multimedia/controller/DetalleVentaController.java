package com.example.NoLimits.Multimedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.service.DetalleVentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/v1/detalles-venta")
@Validated
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @GetMapping
    @Operation(summary = "Listar todos los detalles de venta")
    public ResponseEntity<List<DetalleVentaModel>> listar() {
        List<DetalleVentaModel> detalles = detalleVentaService.findAll();
        return detalles.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detalle de venta por ID")
    public ResponseEntity<DetalleVentaModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaService.findById(id));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Crear detalle de venta",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Detalle mínimo",
                    value = """
                    {
                      "venta": { "id": 1 },
                      "producto": { "id": 10 },
                      "cantidad": 2,
                      "precioUnitario": 12990
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<DetalleVentaModel> crear(@Validated @RequestBody DetalleVentaModel detalle) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleVentaService.save(detalle));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar detalle de venta (PUT)")
    public ResponseEntity<DetalleVentaModel> actualizar(
            @PathVariable Long id,
            @Validated @RequestBody DetalleVentaModel detalle) {
        DetalleVentaModel existente = detalleVentaService.findById(id);
        detalle.setId(existente.getId());
        return ResponseEntity.ok(detalleVentaService.save(detalle));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Editar parcialmente un detalle de venta")
    public ResponseEntity<DetalleVentaModel> patch(
            @PathVariable Long id,
            @RequestBody DetalleVentaModel cambios) {
        return ResponseEntity.ok(detalleVentaService.patch(id, cambios));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar detalle de venta")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        detalleVentaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Listar detalles por ID de venta")
    public ResponseEntity<List<DetalleVentaModel>> buscarPorVenta(@PathVariable Long ventaId) {
        List<DetalleVentaModel> detalles = detalleVentaService.findByVenta(ventaId);
        return detalles.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(detalles);
    }
}