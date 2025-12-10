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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.assemblers.catalogos.TiposEmpresaModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposEmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TiposEmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(
        value = "/api/v2/empresas/{empresaId}/tipos-empresa",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "TiposEmpresa-Controller-V2", description = "Relación Empresa ↔ TipoEmpresa con HATEOAS.")
public class TiposEmpresaControllerV2 {

    @Autowired
    private TiposEmpresaService service;

    @Autowired
    private TiposEmpresaModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar tipos asociados a una empresa (TP - HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<TiposEmpresaResponseDTO>>> listar(
            @PathVariable Long empresaId) {

        var lista = service.findAllByEmpresa(empresaId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @PostMapping("/{tipoId}")
    @Operation(summary = "Vincular Empresa ↔ TipoEmpresa (HATEOAS)")
    public ResponseEntity<EntityModel<TiposEmpresaResponseDTO>> link(
            @PathVariable Long empresaId,
            @PathVariable Long tipoId) {

        TiposEmpresaResponseDTO rel = service.link(empresaId, tipoId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(rel));
    }

    @PatchMapping("/{relacionId}")
    @Operation(
        summary = "Actualizar parcialmente la relación Empresa ↔ TipoEmpresa (PATCH - HATEOAS)",
        description = "Permite cambiar la empresa y/o el tipo asociados a la relación."
    )
    public ResponseEntity<EntityModel<TiposEmpresaResponseDTO>> patch(
            @PathVariable Long relacionId,
            @RequestBody TiposEmpresaUpdateDTO body) {

        TiposEmpresaResponseDTO relActualizada = service.patch(relacionId, body);
        return ResponseEntity.ok(assembler.toModel(relActualizada));
    }

    @DeleteMapping("/{tipoId}")
    @Operation(summary = "Desvincular Empresa ↔ TipoEmpresa (HATEOAS)")
    public ResponseEntity<Void> unlink(
            @PathVariable Long empresaId,
            @PathVariable Long tipoId) {

        service.unlink(empresaId, tipoId);
        return ResponseEntity.noContent().build();
    }
}