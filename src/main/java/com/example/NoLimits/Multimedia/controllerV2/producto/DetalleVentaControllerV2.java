// Ruta: src/main/java/com/example/NoLimits/Multimedia/controllerV2/producto/DetalleVentaControllerV2.java
package com.example.NoLimits.Multimedia.controllerV2.producto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.producto.DetalleVentaModelAssembler;
import com.example.NoLimits.Multimedia.dto.producto.request.DetalleVentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.DetalleVentaUpdateDTO;
import com.example.NoLimits.Multimedia.service.producto.DetalleVentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/detalles-venta", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "DetalleVenta-Controller-V2", description = "Operaciones relacionadas con los detalles de venta (HATEOAS).")
@Validated
public class DetalleVentaControllerV2 {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private DetalleVentaModelAssembler detalleVentaAssembler;

    @GetMapping
    @Operation(summary = "Listar detalles de venta (HATEOAS)")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(mediaType = "application/hal+json",
            schema = @Schema(implementation = DetalleVentaResponseDTO.class)))
    @ApiResponse(responseCode = "204", description = "Sin contenido")
    public ResponseEntity<CollectionModel<EntityModel<DetalleVentaResponseDTO>>> getAll() {
        List<EntityModel<DetalleVentaResponseDTO>> detalles = detalleVentaService.findAll().stream()
            .map(detalleVentaAssembler::toModel)
            .collect(Collectors.toList());

        if (detalles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
            CollectionModel.of(detalles,
                linkTo(methodOn(DetalleVentaControllerV2.class).getAll()).withSelfRel())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de venta por ID (HATEOAS)")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(mediaType = "application/hal+json",
            schema = @Schema(implementation = DetalleVentaResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "No encontrado")
    public ResponseEntity<EntityModel<DetalleVentaResponseDTO>> getById(@PathVariable Long id) {
        try {
            DetalleVentaResponseDTO detalle = detalleVentaService.findById(id);
            return ResponseEntity.ok(detalleVentaAssembler.toModel(detalle));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Listar detalles de una venta (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<DetalleVentaResponseDTO>>> getByVenta(@PathVariable Long ventaId) {
        List<EntityModel<DetalleVentaResponseDTO>> detalles = detalleVentaService.findByVenta(ventaId).stream()
            .map(detalleVentaAssembler::toModel)
            .collect(Collectors.toList());

        if (detalles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
            CollectionModel.of(detalles,
                linkTo(methodOn(DetalleVentaControllerV2.class).getByVenta(ventaId)).withSelfRel())
        );
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Crear detalle de venta (HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
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
    @ApiResponse(responseCode = "201", description = "Creado")
    public ResponseEntity<EntityModel<DetalleVentaResponseDTO>> create(
            @Valid @RequestBody DetalleVentaRequestDTO body) {

        DetalleVentaResponseDTO nuevo = detalleVentaService.save(body);
        EntityModel<DetalleVentaResponseDTO> entityModel = detalleVentaAssembler.toModel(nuevo);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Actualizar parcialmente detalle de venta (PATCH - HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "PATCH ejemplo",
                    value = """
                    {
                      "cantidad": 3,
                      "precioUnitario": 14990
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<EntityModel<DetalleVentaResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody DetalleVentaUpdateDTO parcial) {
        try {
            DetalleVentaResponseDTO actualizado = detalleVentaService.patch(id, parcial);
            return ResponseEntity.ok(detalleVentaAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar detalle de venta (HATEOAS)")
    @ApiResponse(responseCode = "204", description = "Eliminado")
    @ApiResponse(responseCode = "404", description = "No encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            detalleVentaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}