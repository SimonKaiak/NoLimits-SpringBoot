package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.PlataformasModelAssembler;
import com.example.NoLimits.Multimedia.model.PlataformasModel;
import com.example.NoLimits.Multimedia.service.PlataformasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(
        value = "/api/v2/productos/{productoId}/plataformas",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "Plataformas-Controller-V2", description = "Relación Producto ↔ Plataforma con HATEOAS.")
public class PlataformasControllerV2 {

    @Autowired
    private PlataformasService service;

    @Autowired
    private PlataformasModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar plataformas asociadas a un producto (TP - HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<PlataformasModel>>> listar(
            @PathVariable Long productoId
    ) {
        var lista = service.findByProducto(productoId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @PostMapping("/{plataformaId}")
    @Operation(summary = "Vincular Producto ↔ Plataforma (HATEOAS)")
    public ResponseEntity<EntityModel<PlataformasModel>> link(
            @PathVariable Long productoId,
            @PathVariable Long plataformaId
    ) {
        try {
            var rel = service.link(productoId, plataformaId);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(assembler.toModel(rel));

        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{relacionId}")
    @Operation(summary = "Actualizar parcialmente la relación Producto ↔ Plataforma (PATCH - HATEOAS)")
    public ResponseEntity<EntityModel<PlataformasModel>> patch(
            @PathVariable Long productoId,
            @PathVariable Long relacionId,
            @RequestBody PlataformasModel body
    ) {
        try {
            Long nuevoProductoId = (body.getProducto() != null)
                    ? body.getProducto().getId()
                    : null;

            Long nuevaPlataformaId = (body.getPlataforma() != null)
                    ? body.getPlataforma().getId()
                    : null;

            var relActualizada =
                    service.patch(relacionId, nuevoProductoId, nuevaPlataformaId);

            return ResponseEntity.ok(assembler.toModel(relActualizada));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{plataformaId}")
    @Operation(summary = "Desvincular Producto ↔ Plataforma (HATEOAS)")
    public ResponseEntity<Void> unlink(
            @PathVariable Long productoId,
            @PathVariable Long plataformaId
    ) {
        try {
            service.unlink(productoId, plataformaId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}