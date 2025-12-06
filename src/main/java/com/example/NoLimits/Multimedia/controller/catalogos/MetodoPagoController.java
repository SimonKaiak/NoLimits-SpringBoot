package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoPagoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoPagoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoPagoUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/metodos-pago")
@Tag(name = "MetodoPagoController", description = "Operaciones CRUD para los métodos de pago.")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    // Obtener todos los métodos de pago
    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los métodos de pago",
            description = "Lista completa de métodos de pago disponibles.")
    public ResponseEntity<List<MetodoPagoResponseDTO>> getAllMetodosPago() {
        List<MetodoPagoResponseDTO> metodos = metodoPagoService.findAll();
        return metodos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(metodos);
    }

    @GetMapping("/paginado")
    public ResponseEntity<PagedResponse<MetodoPagoResponseDTO>> findAllPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(metodoPagoService.findAllPaged(page, size));
    }

    // Obtener un método de pago por ID
    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Buscar método de pago por ID",
            description = "Devuelve un método de pago específico por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Método de pago encontrado",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = MetodoPagoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado")
    })
    public ResponseEntity<MetodoPagoResponseDTO> getMetodoPagoById(@PathVariable Long id) {
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
                                    schema = @Schema(implementation = MetodoPagoResponseDTO.class)))
    public ResponseEntity<MetodoPagoResponseDTO> createMetodoPago(
            @Valid @RequestBody MetodoPagoRequestDTO metodoPago) {

        MetodoPagoResponseDTO creado = metodoPagoService.save(metodoPago);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Actualizar un método de pago completo (PUT)
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Actualizar método de pago",
        description = "Reemplaza completamente un método de pago por ID."
    )
    public ResponseEntity<MetodoPagoResponseDTO> updateMetodoPago(
            @PathVariable Long id,
            @Valid @RequestBody MetodoPagoRequestDTO metodoPagoDetails) {

        return ResponseEntity.ok(metodoPagoService.update(id, metodoPagoDetails));
    }

    // Actualizar parcialmente un método de pago (PATCH)
    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Editar parcialmente un método de pago",
        description = "Actualiza campos específicos de un método de pago por ID."
    )
    public ResponseEntity<MetodoPagoResponseDTO> patchMetodoPago(
            @PathVariable Long id,
            @RequestBody MetodoPagoUpdateDTO metodoPagoDetails) {

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
    @Operation(summary = "Buscar método de pago por nombre",
            description = "Devuelve un método de pago según el nombre.")
    public ResponseEntity<MetodoPagoResponseDTO> getMetodoPagoByNombre(@PathVariable String nombre) {
        return metodoPagoService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}