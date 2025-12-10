package com.example.NoLimits.Multimedia.controller.producto;

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

import com.example.NoLimits.Multimedia.dto.producto.request.DetalleVentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.DetalleVentaUpdateDTO;
import com.example.NoLimits.Multimedia.service.producto.DetalleVentaService;

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
    public ResponseEntity<List<DetalleVentaResponseDTO>> listar() {
        List<DetalleVentaResponseDTO> detalles = detalleVentaService.findAll();
        return detalles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detalle de venta por ID")
    public ResponseEntity<DetalleVentaResponseDTO> buscarPorId(@PathVariable Long id) {
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
                      "productoId": 10,
                      "cantidad": 2,
                      "precioUnitario": 12990
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<DetalleVentaResponseDTO> crear(
            @Validated @RequestBody DetalleVentaRequestDTO detalleRequest) {

        DetalleVentaResponseDTO creado = detalleVentaService.save(detalleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar detalle de venta (PUT)")
    public ResponseEntity<DetalleVentaResponseDTO> actualizar(
            @PathVariable Long id,
            @Validated @RequestBody DetalleVentaUpdateDTO detalleUpdate) {

        DetalleVentaResponseDTO actualizado = detalleVentaService.update(id, detalleUpdate);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Editar parcialmente un detalle de venta (PATCH)")
    public ResponseEntity<DetalleVentaResponseDTO> patch(
            @PathVariable Long id,
            @RequestBody DetalleVentaUpdateDTO cambios) {

        DetalleVentaResponseDTO actualizado = detalleVentaService.patch(id, cambios);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar detalle de venta")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        detalleVentaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Listar detalles por ID de venta")
    public ResponseEntity<List<DetalleVentaResponseDTO>> buscarPorVenta(@PathVariable Long ventaId) {
        List<DetalleVentaResponseDTO> detalles = detalleVentaService.findByVenta(ventaId);
        return detalles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(detalles);
    }
}