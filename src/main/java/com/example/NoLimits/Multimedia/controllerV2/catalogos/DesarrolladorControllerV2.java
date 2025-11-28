package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.DesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladorService;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(
        value = "/api/v2/desarrolladores",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "Desarrollador-Controller-V2", description = "CRUD de desarrolladores con HATEOAS.")
public class DesarrolladorControllerV2 {

    @Autowired
    private DesarrolladorService desarrolladorService;

    @Autowired
    private DesarrolladorModelAssembler desarrolladorAssembler;

    @GetMapping
    @Operation(summary = "Listar todos los desarrolladores (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<DesarrolladorModel>>> getAll() {
        var lista = desarrolladorService.findAll().stream()
                .map(desarrolladorAssembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var collection = CollectionModel.of(
                lista,
                linkTo(methodOn(DesarrolladorControllerV2.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener desarrollador por ID (HATEOAS)")
    public ResponseEntity<EntityModel<DesarrolladorModel>> getById(@PathVariable Long id) {
        try {
            var dev = desarrolladorService.findById(id);
            return ResponseEntity.ok(desarrolladorAssembler.toModel(dev));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/search", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar desarrolladores por nombre (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<DesarrolladorModel>>> searchByNombre(
            @RequestParam String nombre
    ) {
        var lista = desarrolladorService.findByNombre(nombre).stream()
                .map(desarrolladorAssembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var collection = CollectionModel.of(
                lista,
                linkTo(methodOn(DesarrolladorControllerV2.class).searchByNombre(nombre)).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear desarrollador (HATEOAS)")
    public ResponseEntity<EntityModel<DesarrolladorModel>> create(
            @Valid @RequestBody DesarrolladorModel body
    ) {
        body.setId(null);
        var creado = desarrolladorService.save(body);
        var entity = desarrolladorAssembler.toModel(creado);

        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar desarrollador por ID (PUT - HATEOAS)")
    public ResponseEntity<EntityModel<DesarrolladorModel>> update(
            @PathVariable Long id,
            @Valid @RequestBody DesarrolladorModel body
    ) {
        try {
            var actualizado = desarrolladorService.update(id, body);
            return ResponseEntity.ok(desarrolladorAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente desarrollador por ID (PATCH - HATEOAS)")
    public ResponseEntity<EntityModel<DesarrolladorModel>> patch(
            @PathVariable Long id,
            @RequestBody DesarrolladorModel body
    ) {
        try {
            var actualizado = desarrolladorService.patch(id, body);
            return ResponseEntity.ok(desarrolladorAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            // nombre vacío o duplicado, etc.
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar desarrollador por ID (HATEOAS)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            desarrolladorService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}