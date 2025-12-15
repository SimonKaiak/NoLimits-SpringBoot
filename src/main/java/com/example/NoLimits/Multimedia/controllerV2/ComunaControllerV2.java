package com.example.NoLimits.Multimedia.controllerV2;

import java.util.List;
import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.ComunaModelAssembler;
import com.example.NoLimits.Multimedia.model.ComunaModel;
import com.example.NoLimits.Multimedia.service.ComunaService;

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
@RequestMapping(value = "/api/v2/comunas", produces = MediaTypes.HAL_JSON_VALUE)
@Validated
@Tag(name = "Comunas", description = "Gestión de comunas (V2 HATEOAS)")
public class ComunaControllerV2 {

    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaModelAssembler comunaAssembler;

    @GetMapping
    @Operation(summary = "Listar todas las comunas")
    public ResponseEntity<CollectionModel<EntityModel<ComunaModel>>> getAll() {
        List<EntityModel<ComunaModel>> comunas = comunaService.findAll()
                .stream()
                .map(comunaAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ComunaModel>> body = CollectionModel.of(
                comunas,
                linkTo(methodOn(ComunaControllerV2.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una comuna por ID")
    public EntityModel<ComunaModel> getById(@PathVariable Long id) throws RecursoNoEncontradoException {
        ComunaModel comuna = comunaService.findById(id);
        return comunaAssembler.toModel(comuna);
    }

    @PostMapping(consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una nueva comuna")
    public ResponseEntity<EntityModel<ComunaModel>> create(@Valid @RequestBody ComunaModel comuna) {
        ComunaModel creada = comunaService.save(comuna);
        EntityModel<ComunaModel> entityModel = comunaAssembler.toModel(creada);

        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping(value = "/{id}", consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar completamente una comuna")
    public ResponseEntity<EntityModel<ComunaModel>> update(
            @PathVariable Long id,
            @Valid @RequestBody ComunaModel detalles) {

        ComunaModel actualizada = comunaService.update(id, detalles);
        EntityModel<ComunaModel> entityModel = comunaAssembler.toModel(actualizada);

        return ResponseEntity.ok(entityModel);
    }

    @PatchMapping(value = "/{id}", consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente una comuna (PATCH)")
    public ResponseEntity<EntityModel<ComunaModel>> patch(
            @PathVariable Long id,
            @RequestBody ComunaModel parciales) {

        ComunaModel actualizada = comunaService.patch(id, parciales);
        EntityModel<ComunaModel> entityModel = comunaAssembler.toModel(actualizada);

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una comuna por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comunaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}