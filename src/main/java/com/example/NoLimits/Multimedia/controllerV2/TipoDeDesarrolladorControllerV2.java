package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.TipoDeDesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.model.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.service.TipoDeDesarrolladorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(
        value = "/api/v2/tipos-desarrollador",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "TipoDeDesarrollador-Controller-V2", description = "Catálogo de tipos de desarrollador con HATEOAS.")
public class TipoDeDesarrolladorControllerV2 {

    @Autowired
    private TipoDeDesarrolladorService service;

    @Autowired
    private TipoDeDesarrolladorModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar tipos de desarrollador (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<TipoDeDesarrolladorModel>>> getAll() {
        var lista = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var collection = CollectionModel.of(
                lista,
                linkTo(methodOn(TipoDeDesarrolladorControllerV2.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo de desarrollador por ID (HATEOAS)")
    public ResponseEntity<EntityModel<TipoDeDesarrolladorModel>> getById(@PathVariable Long id) {
        try {
            var tipo = service.findById(id);
            return ResponseEntity.ok(assembler.toModel(tipo));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear tipo de desarrollador (HATEOAS)")
    public ResponseEntity<EntityModel<TipoDeDesarrolladorModel>> create(
            @Valid @RequestBody TipoDeDesarrolladorModel body
    ) {
        body.setId(null);
        var creado = service.save(body);
        var entity = assembler.toModel(creado);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar tipo de desarrollador (PUT - HATEOAS)")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody TipoDeDesarrolladorModel body
    ) {
        try {
            var actualizado = service.update(id, body);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de desarrollador (HATEOAS)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            // Hay relaciones en tipos_de_desarrollador
            return ResponseEntity.badRequest().build();
        }
    }
}