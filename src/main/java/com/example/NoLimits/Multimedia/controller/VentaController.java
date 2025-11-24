package com.example.NoLimits.Multimedia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.dto.VentaRequest;
import com.example.NoLimits.Multimedia.model.VentaModel;
import com.example.NoLimits.Multimedia.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ventas")
@Tag(name = "Venta-Controller", description = "Operaciones relacionadas con las ventas.")
@Validated
@CrossOrigin(
    origins = {
        "http://localhost:5173",
        "https://no-limits-react.vercel.app"
    },
    allowCredentials = "true"
)
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    @Operation(summary = "Listar todas las ventas", description = "Obtiene todas las ventas registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = VentaModel.class)))),
        @ApiResponse(responseCode = "204", description = "Sin contenido")
    })
    public ResponseEntity<List<VentaModel>> listarVentas() {
        List<VentaModel> ventas = ventaService.findAll();
        return ventas.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venta por ID", description = "Obtiene una venta por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = VentaModel.class))),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<VentaModel> buscarVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    @PostMapping("/registrar")
    public ResponseEntity<VentaModel> registrarVenta(
            @RequestBody VentaRequest request,
            jakarta.servlet.http.HttpSession session) {

        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED,
                "Usuario no autenticado"
            );
        }

        return ResponseEntity.ok(
            ventaService.crearVentaDesdeRequest(request, usuarioId)
        );
    }


    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Actualizar venta (PUT)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "PUT con FKs por id",
                    value = """
                    {
                      "fechaCompra": "2025-08-08",
                      "horaCompra": "12:00:00",
                      "usuarioModel": { "id": 1 },
                      "metodoPagoModel": { "id": 2 },
                      "metodoEnvioModel": { "id": 1 },
                      "estado": { "id": 2 }
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<VentaModel> actualizarVenta(@PathVariable Long id, @Valid @RequestBody VentaModel venta) {
        return ResponseEntity.ok(ventaService.update(id, venta));
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Editar parcialmente una venta",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "PATCH ejemplo",
                    value = """
                    {
                      "fechaCompra": "2025-08-09",
                      "horaCompra": "18:30:00",
                      "estado": { "id": 3 }
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<VentaModel> editarVenta(@PathVariable Long id, @RequestBody VentaModel venta) {
        return ResponseEntity.ok(ventaService.patch(id, venta));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta", description = "Elimina una venta por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminada"),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        ventaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metodopago/{metodoPagoId}")
    @Operation(summary = "Buscar ventas por método de pago")
    public ResponseEntity<List<VentaModel>> buscarVentasPorMetodoPago(@PathVariable Long metodoPagoId) {
        List<VentaModel> ventas = ventaService.findByMetodoPago(metodoPagoId);
        return ventas.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(ventas);
    }

    @GetMapping("/resumen")
    @Operation(summary = "Resumen de ventas")
    public ResponseEntity<List<Map<String, Object>>> resumenVentas() {
        var resumen = ventaService.obtenerVentasConDatos();
        return resumen.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(resumen);
    }
}