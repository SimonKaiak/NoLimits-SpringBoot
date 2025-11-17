package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import com.example.NoLimits.Multimedia.service.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metodos-pago")
@Tag(name = "MetodoPagoController", description = "Operaciones CRUD para los métodos de pago.")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    // Obtener todos los métodos de pago
    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los métodos de pago", description = "Lista completa de métodos de pago disponibles.")
    public ResponseEntity<List<MetodoPagoModel>> getAllMetodosPago() {
        List<MetodoPagoModel> metodos = metodoPagoService.findAll();
        return metodos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(metodos);
    }

    // Obtener un método de pago por ID
    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Buscar método de pago por ID", description = "Devuelve un método de pago específico por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Método de pago encontrado",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = MetodoPagoModel.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado")
    })
    public ResponseEntity<MetodoPagoModel> getMetodoPagoById(@PathVariable Long id) {
        return ResponseEntity.ok(metodoPagoService.findById(id));
    }

    // Crear un nuevo método de pago
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Crear método de pago",
        description = "Crea un nuevo método de pago en el sistema.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "POST mínimo",
                    value = """
                    {
                      "nombre": "Tarjeta de Débito"
                    }
                    """
                )
            )
        )
    )
    @ApiResponse(responseCode = "201", description = "Método de pago creado correctamente",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MetodoPagoModel.class)))
    public ResponseEntity<MetodoPagoModel> createMetodoPago(@Valid @RequestBody MetodoPagoModel metodoPago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoPagoService.save(metodoPago));
    }

    // Actualizar un método de pago completo (PUT)
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Actualizar método de pago",
        description = "Reemplaza completamente un método de pago por ID.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "PUT seguro (sin id en body)",
                        value = """
                        {
                          "nombre": "Tarjeta de Crédito"
                        }
                        """
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "PUT con id (coincide con path)",
                        value = """
                        {
                          "id": 1,
                          "nombre": "Tarjeta de Crédito"
                        }
                        """
                    )
                }
            )
        )
    )
    public ResponseEntity<MetodoPagoModel> updateMetodoPago(@PathVariable Long id,
                                                            @Valid @RequestBody MetodoPagoModel metodoPagoDetails) {
        return ResponseEntity.ok(metodoPagoService.update(id, metodoPagoDetails));
    }

    // Actualizar parcialmente un método de pago (PATCH)
    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Editar parcialmente un método de pago",
        description = "Actualiza campos específicos de un método de pago por ID.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "PATCH ejemplo",
                    value = """
                    { "nombre": "Transferencia" }
                    """
                )
            )
        )
    )
    public ResponseEntity<MetodoPagoModel> patchMetodoPago(@PathVariable Long id,
                                                           @RequestBody MetodoPagoModel metodoPagoDetails) {
        return ResponseEntity.ok(metodoPagoService.patch(id, metodoPagoDetails));
    }

    // Eliminar un método de pago por ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar método de pago", description = "Elimina un método de pago existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Método de pago eliminado"),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado")
    })
    public ResponseEntity<Void> deleteMetodoPago(@PathVariable Long id) {
        metodoPagoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar un método de pago por nombre
    @GetMapping(value = "/buscar/{nombre}", produces = "application/json")
    @Operation(summary = "Buscar método de pago por nombre", description = "Devuelve un método de pago según el nombre.")
    public ResponseEntity<MetodoPagoModel> getMetodoPagoByNombre(@PathVariable String nombre) {
        return metodoPagoService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}