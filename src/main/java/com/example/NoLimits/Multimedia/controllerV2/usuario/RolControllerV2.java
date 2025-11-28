package com.example.NoLimits.Multimedia.controllerV2.usuario;

import java.util.List;
import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.usuario.RolModelAssembler;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.service.usuario.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/roles", produces = MediaTypes.HAL_JSON_VALUE)
@Validated
@Tag(name = "Roles", description = "Gestión de roles (V2 HATEOAS)")
public class RolControllerV2 {

    @Autowired
    private RolService rolService;

    @Autowired
    private RolModelAssembler rolAssembler;

    @GetMapping
    @Operation(summary = "Listar todos los roles")
    public ResponseEntity<CollectionModel<EntityModel<RolModel>>> getAll() {
        List<EntityModel<RolModel>> roles = rolService.findAll()
                .stream()
                .map(rolAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<RolModel>> body = CollectionModel.of(
                roles,
                linkTo(methodOn(RolControllerV2.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un rol por ID")
    public EntityModel<RolModel> getById(@PathVariable Long id) throws RecursoNoEncontradoException {
        RolModel rol = rolService.findById(id);
        return rolAssembler.toModel(rol);
    }

    @PostMapping(consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo rol")
    public ResponseEntity<EntityModel<RolModel>> create(@Valid @RequestBody RolModel rol) {
        RolModel creado = rolService.save(rol);
        EntityModel<RolModel> entityModel = rolAssembler.toModel(creado);

        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping(value = "/{id}", consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar completamente un rol")
    public ResponseEntity<EntityModel<RolModel>> update(
            @PathVariable Long id,
            @Valid @RequestBody RolModel in) {

        RolModel actualizado = rolService.update(id, in);
        EntityModel<RolModel> entityModel = rolAssembler.toModel(actualizado);

        return ResponseEntity.ok(entityModel);
    }

    @PatchMapping(value = "/{id}", consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente un rol (PATCH)")
    public ResponseEntity<EntityModel<RolModel>> patch(
            @PathVariable Long id,
            @RequestBody RolModel in) {

        RolModel actualizado = rolService.patch(id, in);
        EntityModel<RolModel> entityModel = rolAssembler.toModel(actualizado);

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un rol por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}