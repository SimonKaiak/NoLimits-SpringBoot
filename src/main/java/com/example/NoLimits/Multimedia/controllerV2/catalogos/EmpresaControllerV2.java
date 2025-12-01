package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.NoLimits.Multimedia.assemblers.catalogos.EmpresaModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.request.EmpresaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

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

    // ================== LISTAR ==================
    @GetMapping
    @Operation(summary = "Listar empresas (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<EmpresaResponseDTO>>> findAll() {

        var lista = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    // ================== OBTENER POR ID ==================
    @GetMapping("/{id}")
    @Operation(summary = "Obtener empresa por ID (HATEOAS)")
    public EntityModel<EmpresaResponseDTO> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }

    // ================== CREAR ==================
    @PostMapping
    @Operation(summary = "Crear empresa (HATEOAS)")
    public ResponseEntity<EntityModel<EmpresaResponseDTO>> save(
            @Valid @RequestBody EmpresaRequestDTO dto
    ) {
        var creada = service.create(dto); // ⬅️ antes: save(dto)
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(creada));
    }

    // ================== ACTUALIZAR (PUT) ==================
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empresa (HATEOAS)")
    public EntityModel<EmpresaResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaRequestDTO dto // ⬅️ antes: EmpresaUpdateDTO
    ) {
        return assembler.toModel(service.update(id, dto));
    }

    // ================== PATCH ==================
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente empresa (HATEOAS)")
    public ResponseEntity<EntityModel<EmpresaResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody EmpresaUpdateDTO dto
    ) {
        var actualizada = service.patch(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    // ================== ELIMINAR ==================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empresa (HATEOAS)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}