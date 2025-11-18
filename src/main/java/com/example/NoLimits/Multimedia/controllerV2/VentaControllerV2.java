package com.example.NoLimits.Multimedia.controllerV2;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.VentaModelAssembler;
import com.example.NoLimits.Multimedia.model.VentaModel;
import com.example.NoLimits.Multimedia.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/v2/ventas", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Venta-Controller-V2", description = "Operaciones relacionadas con las ventas (HATEOAS).")
@Validated
public class VentaControllerV2 {

    @Autowired private VentaService ventaService;
    @Autowired private VentaModelAssembler ventaAssembler;

    @Operation(summary = "Listar ventas (HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/hal+json")),
        @ApiResponse(responseCode = "204", description = "Sin contenido")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<VentaModel>>> getAll() {
        var ventas = ventaService.findAll().stream().map(ventaAssembler::toModel).collect(Collectors.toList());
        return ventas.isEmpty()? ResponseEntity.noContent().build()
                               : ResponseEntity.ok(CollectionModel.of(ventas, linkTo(methodOn(VentaControllerV2.class).getAll()).withSelfRel()));
    }

    @Operation(summary = "Obtener venta por ID (HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/hal+json")),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<VentaModel>> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ventaAssembler.toModel(ventaService.findById(id)));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "application/json")
    @Operation(
        summary = "Crear venta (HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Venta mínima",
                    value = """
                    {
                      "usuarioModel": { "id": 1 },
                      "metodoPagoModel": { "id": 2 },
                      "metodoEnvioModel": { "id": 1 },
                      "estado": { "id": 1 }
                    }
                    """
                )
            )
        )
    )
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Creada"))
    public ResponseEntity<EntityModel<VentaModel>> create(@Valid @RequestBody VentaModel venta) {
        var nueva = ventaService.save(venta);
        var entityModel = ventaAssembler.toModel(nueva);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    @Operation(summary = "Actualizar venta (PUT - HATEOAS)")
    public ResponseEntity<EntityModel<VentaModel>> update(@PathVariable Long id, @Valid @RequestBody VentaModel detalles) {
        try {
            var actualizada = ventaService.update(id, detalles);
            return ResponseEntity.ok(ventaAssembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    @Operation(summary = "Actualizar parcialmente venta (PATCH - HATEOAS)")
    public ResponseEntity<EntityModel<VentaModel>> patch(@PathVariable Long id, @RequestBody VentaModel detalles) {
        try {
            var actualizada = ventaService.patch(id, detalles);
            return ResponseEntity.ok(ventaAssembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta (HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminada"),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            ventaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/metodopago/{metodoPagoId}")
    @Operation(summary = "Ventas por método de pago (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<VentaModel>>> byMetodoPago(@PathVariable Long metodoPagoId) {
        var ventas = ventaService.findByMetodoPago(metodoPagoId).stream().map(ventaAssembler::toModel).collect(Collectors.toList());
        return ventas.isEmpty()? ResponseEntity.noContent().build()
                               : ResponseEntity.ok(CollectionModel.of(ventas, linkTo(methodOn(VentaControllerV2.class).byMetodoPago(metodoPagoId)).withSelfRel()));
    }
}