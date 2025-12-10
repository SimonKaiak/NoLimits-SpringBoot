package com.example.NoLimits.Multimedia.controllerV2.catalogos;

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

import com.example.NoLimits.Multimedia.assemblers.catalogos.TipoEmpresaModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoEmpresaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoEmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TipoEmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v2/tipos-empresa", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "TipoEmpresa-Controller-V2", description = "CRUD TipoEmpresa con DTO + HATEOAS.")
public class TipoEmpresaControllerV2 {

    @Autowired
    private TipoEmpresaService service;

    @Autowired
    private TipoEmpresaModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar tipos de empresa")
    public ResponseEntity<CollectionModel<EntityModel<TipoEmpresaResponseDTO>>> findAll() {

        var lista = service.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo empresa por ID")
    public EntityModel<TipoEmpresaResponseDTO> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear tipo empresa")
    public ResponseEntity<EntityModel<TipoEmpresaResponseDTO>> save(
            @RequestBody TipoEmpresaRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(service.save(dto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo empresa")
    public EntityModel<TipoEmpresaResponseDTO> update(
            @PathVariable Long id,
            @RequestBody TipoEmpresaRequestDTO dto) {

        return assembler.toModel(service.update(id, dto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente tipo empresa")
    public EntityModel<TipoEmpresaResponseDTO> patch(
            @PathVariable Long id,
            @RequestBody TipoEmpresaUpdateDTO dto) {

        return assembler.toModel(service.patch(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo empresa")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}