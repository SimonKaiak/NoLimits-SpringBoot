package com.example.NoLimits.Multimedia.controllerV2.ubicacion;

import java.util.List;
import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.ubicacion.RegionModelAssembler;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.service.ubicacion.RegionService;

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
@RequestMapping(value = "/api/v2/regiones", produces = MediaTypes.HAL_JSON_VALUE)
@Validated
@Tag(name = "Regiones", description = "Gestión de regiones (V2 HATEOAS)")
public class RegionControllerV2 {

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionModelAssembler regionAssembler;

    @GetMapping
    @Operation(summary = "Listar todas las regiones")
    public ResponseEntity<CollectionModel<EntityModel<RegionModel>>> getAll() {
        List<EntityModel<RegionModel>> regiones = regionService.findAll()
                .stream()
                .map(regionAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<RegionModel>> body = CollectionModel.of(
                regiones,
                linkTo(methodOn(RegionControllerV2.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una región por ID")
    public EntityModel<RegionModel> getById(@PathVariable Long id) throws RecursoNoEncontradoException {
        RegionModel region = regionService.findById(id);
        return regionAssembler.toModel(region);
    }

    @PostMapping(consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una nueva región")
    public ResponseEntity<EntityModel<RegionModel>> create(@Valid @RequestBody RegionModel region) {
        RegionModel creada = regionService.save(region);
        EntityModel<RegionModel> entityModel = regionAssembler.toModel(creada);

        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping(value = "/{id}", consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar completamente una región")
    public ResponseEntity<EntityModel<RegionModel>> update(
            @PathVariable Long id,
            @Valid @RequestBody RegionModel in) {

        RegionModel actualizada = regionService.update(id, in);
        EntityModel<RegionModel> entityModel = regionAssembler.toModel(actualizada);

        return ResponseEntity.ok(entityModel);
    }

    @PatchMapping(value = "/{id}", consumes = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente una región (PATCH)")
    public ResponseEntity<EntityModel<RegionModel>> patch(
            @PathVariable Long id,
            @RequestBody RegionModel in) {

        RegionModel actualizada = regionService.patch(id, in);
        EntityModel<RegionModel> entityModel = regionAssembler.toModel(actualizada);

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una región por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}