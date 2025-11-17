package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.DesarrolladoresModelAssembler;
import com.example.NoLimits.Multimedia.model.DesarrolladoresModel;
import com.example.NoLimits.Multimedia.service.DesarrolladoresService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping(
        value = "/api/v2/productos/{productoId}/desarrolladores",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "Desarrolladores-Controller-V2", description = "Relación Producto ↔ Desarrollador con HATEOAS.")
public class DesarrolladoresControllerV2 {

    @Autowired
    private DesarrolladoresService service;

    @Autowired
    private DesarrolladoresModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar desarrolladores asociados a un producto (TP - HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<DesarrolladoresModel>>> listar(
            @PathVariable Long productoId
    ) {
        var lista = service.findByProducto(productoId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var collection = CollectionModel.of(lista);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/{desarrolladorId}")
    @Operation(summary = "Vincular Producto ↔ Desarrollador (HATEOAS)")
    public ResponseEntity<EntityModel<DesarrolladoresModel>> link(
            @PathVariable Long productoId,
            @PathVariable Long desarrolladorId
    ) {
        try {
            var rel = service.link(productoId, desarrolladorId);
            return ResponseEntity.ok(assembler.toModel(rel));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{desarrolladorId}")
    @Operation(summary = "Desvincular Producto ↔ Desarrollador (HATEOAS)")
    public ResponseEntity<Void> unlink(
            @PathVariable Long productoId,
            @PathVariable Long desarrolladorId
    ) {
        try {
            service.unlink(productoId, desarrolladorId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}