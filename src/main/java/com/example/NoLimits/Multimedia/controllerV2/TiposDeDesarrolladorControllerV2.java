package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.TiposDeDesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.model.TiposDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.service.TiposDeDesarrolladorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(
        value = "/api/v2/desarrolladores/{desarrolladorId}/tipos",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(
    name = "TiposDeDesarrollador-Controller-V2",
    description = "Relación Desarrollador ↔ TipoDeDesarrollador con HATEOAS."
)
public class TiposDeDesarrolladorControllerV2 {

    @Autowired
    private TiposDeDesarrolladorService service;

    @Autowired
    private TiposDeDesarrolladorModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar tipos asociados a un desarrollador (TP - HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<TiposDeDesarrolladorModel>>> listar(
            @PathVariable Long desarrolladorId
    ) {
        var lista = service.findAll().stream()
                .filter(rel -> rel.getDesarrollador() != null
                        && rel.getDesarrollador().getId() != null
                        && rel.getDesarrollador().getId().equals(desarrolladorId))
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var collection = CollectionModel.of(
                lista,
                linkTo(methodOn(TiposDeDesarrolladorControllerV2.class).listar(desarrolladorId)).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @PostMapping("/{tipoId}")
    @Operation(summary = "Vincular Desarrollador ↔ TipoDeDesarrollador (HATEOAS)")
    public ResponseEntity<EntityModel<TiposDeDesarrolladorModel>> link(
            @PathVariable Long desarrolladorId,
            @PathVariable Long tipoId
    ) {
        try {
            var rel = service.link(desarrolladorId, tipoId);
            var entity = assembler.toModel(rel);

            return ResponseEntity
                    .created(
                        linkTo(methodOn(TiposDeDesarrolladorControllerV2.class)
                                .listar(desarrolladorId)).toUri()
                    )
                    .body(entity);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PATCH - Actualizar relación Desarrollador–Tipo (HATEOAS)
    @PatchMapping("/{relacionId}")
    @Operation(
            summary = "Actualizar parcialmente la relación Desarrollador–Tipo (HATEOAS)",
            description = "Permite cambiar el desarrollador y/o el tipo asociados a la relación. "
                        + "Puedes enviar solo nuevoDesarrolladorId, solo nuevoTipoId o ambos."
    )
    public ResponseEntity<EntityModel<TiposDeDesarrolladorModel>> patch(
            @PathVariable Long desarrolladorId,
            @PathVariable Long relacionId,
            @RequestParam(required = false) Long nuevoDesarrolladorId,
            @RequestParam(required = false) Long nuevoTipoId
    ) {
        try {
            TiposDeDesarrolladorModel relActualizada =
                    service.patch(relacionId, nuevoDesarrolladorId, nuevoTipoId);

            return ResponseEntity.ok(assembler.toModel(relActualizada));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{tipoId}")
    @Operation(summary = "Desvincular Desarrollador ↔ TipoDeDesarrollador (HATEOAS)")
    public ResponseEntity<Void> unlink(
            @PathVariable Long desarrolladorId,
            @PathVariable Long tipoId
    ) {
        try {
            service.unlink(desarrolladorId, tipoId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}