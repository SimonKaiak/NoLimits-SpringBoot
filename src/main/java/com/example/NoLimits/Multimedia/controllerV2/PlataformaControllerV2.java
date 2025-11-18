package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia.assemblers.PlataformaModelAssembler;
import com.example.NoLimits.Multimedia.model.PlataformaModel;
import com.example.NoLimits.Multimedia.service.PlataformaService;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(
        value = "/api/v2/plataformas",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "Plataforma-Controller-V2", description = "CRUD de plataformas con HATEOAS.")
public class PlataformaControllerV2 {

    @Autowired
    private PlataformaService service;

    @Autowired
    private PlataformaModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar plataformas (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<PlataformaModel>>> findAll() {
        var lista = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener plataforma por ID (HATEOAS)")
    public EntityModel<PlataformaModel> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear plataforma (HATEOAS)")
    public ResponseEntity<EntityModel<PlataformaModel>> save(
            @RequestBody PlataformaModel body
    ) {
        var creada = service.save(body);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(creada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar plataforma (HATEOAS)")
    public EntityModel<PlataformaModel> update(
            @PathVariable Long id,
            @RequestBody PlataformaModel body
    ) {
        return assembler.toModel(service.update(id, body));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Patch parcial de plataforma (HATEOAS)")
    public EntityModel<PlataformaModel> patch(
            @PathVariable Long id,
            @RequestBody PlataformaModel body
    ) {
        return assembler.toModel(service.patch(id, body));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar plataforma (HATEOAS)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}