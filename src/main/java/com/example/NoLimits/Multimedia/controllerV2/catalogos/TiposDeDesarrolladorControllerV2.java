package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.TiposDeDesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposDeDesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TiposDeDesarrolladorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<CollectionModel<EntityModel<TiposDeDesarrolladorResponseDTO>>> listar(
            @PathVariable Long desarrolladorId
    ) {
        var lista = service.findByDesarrollador(desarrolladorId).stream()
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
    public ResponseEntity<EntityModel<TiposDeDesarrolladorResponseDTO>> link(
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
            description = "Permite cambiar el desarrollador y/o el tipo asociados a la relación."
    )
    public ResponseEntity<EntityModel<TiposDeDesarrolladorResponseDTO>> patch(
            @PathVariable Long desarrolladorId,
            @PathVariable Long relacionId,
            @RequestBody TiposDeDesarrolladorUpdateDTO body
    ) {
        try {
            TiposDeDesarrolladorResponseDTO relActualizada =
                    service.patch(relacionId, body);

            return ResponseEntity.ok(assembler.toModel(relActualizada));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
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