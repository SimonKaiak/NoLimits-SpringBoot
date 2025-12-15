package com.example.NoLimits.Multimedia.controllerV2;

import java.util.stream.Collectors;

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

import com.example.NoLimits.Multimedia.assemblers.EmpresaModelAssembler;
import com.example.NoLimits.Multimedia.model.EmpresaModel;
import com.example.NoLimits.Multimedia.service.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(
        value = "/api/v2/empresas",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "Empresa-Controller-V2", description = "CRUD de empresas con HATEOAS.")
public class EmpresaControllerV2 {

    @Autowired
    private EmpresaService service;

    @Autowired
    private EmpresaModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar empresas (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<EmpresaModel>>> findAll() {
        var lista = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empresa por ID (HATEOAS)")
    public EntityModel<EmpresaModel> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear empresa (HATEOAS)")
    public ResponseEntity<EntityModel<EmpresaModel>> save(@RequestBody EmpresaModel empresa) {
        var creada = service.save(empresa);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(creada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empresa (HATEOAS)")
    public EntityModel<EmpresaModel> update(
            @PathVariable Long id,
            @RequestBody EmpresaModel empresa
    ) {
        return assembler.toModel(service.update(id, empresa));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente empresa (HATEOAS)")
    public ResponseEntity<EntityModel<EmpresaModel>> patch(
            @PathVariable Long id,
            @RequestBody EmpresaModel cambios
    ) {
        var actualizada = service.patch(id, cambios);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empresa (HATEOAS)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}