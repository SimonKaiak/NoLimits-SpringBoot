package com.example.NoLimits.Multimedia.controllerV2;

import java.util.List;
import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.DireccionModelAssembler;
import com.example.NoLimits.Multimedia.model.DireccionModel;
import com.example.NoLimits.Multimedia.service.DireccionService;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/direcciones", produces = MediaTypes.HAL_JSON_VALUE)
@Validated
@Tag(name = "Direcciones", description = "Gestión de direcciones (V2 HATEOAS)")
public class DireccionControllerV2 {

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private DireccionModelAssembler direccionAssembler;

    @GetMapping
    @Operation(summary = "Listar todas las direcciones")
    public ResponseEntity<CollectionModel<EntityModel<DireccionModel>>> getAll() {
        List<EntityModel<DireccionModel>> direcciones = direccionService.findAll()
                .stream()
                .map(direccionAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DireccionModel>> body = CollectionModel.of(
                direcciones,
                linkTo(methodOn(DireccionControllerV2.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una dirección por ID")
    public EntityModel<DireccionModel> getById(@PathVariable Long id) throws RecursoNoEncontradoException {
        DireccionModel direccion = direccionService.findById(id);
        return direccionAssembler.toModel(direccion);
    }

    @PostMapping(consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una nueva dirección")
    public ResponseEntity<EntityModel<DireccionModel>> create(
            @Valid @RequestBody DireccionModel direccion) {

        DireccionModel creada = direccionService.save(direccion);
        EntityModel<DireccionModel> entityModel = direccionAssembler.toModel(creada);

        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PatchMapping(value = "/{id}", consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente una dirección (PATCH)")
    public ResponseEntity<EntityModel<DireccionModel>> patch(
            @PathVariable Long id,
            @RequestBody DireccionModel entrada) {

        DireccionModel actualizada = direccionService.patch(id, entrada);
        EntityModel<DireccionModel> entityModel = direccionAssembler.toModel(actualizada);

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una dirección por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        direccionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}