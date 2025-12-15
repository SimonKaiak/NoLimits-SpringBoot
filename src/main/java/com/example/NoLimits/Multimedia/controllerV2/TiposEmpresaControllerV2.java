package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.TiposEmpresaModelAssembler;
import com.example.NoLimits.Multimedia.model.TiposEmpresaModel;
import com.example.NoLimits.Multimedia.service.TiposEmpresaService;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

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
    public ResponseEntity<CollectionModel<EntityModel<TiposEmpresaModel>>> listar(
            @PathVariable Long empresaId
    ) {
        var lista = service.findAll().stream()
                .filter(rel -> rel.getEmpresa() != null
                        && rel.getEmpresa().getId().equals(empresaId))
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @PostMapping("/{tipoId}")
    @Operation(summary = "Vincular Empresa ↔ TipoEmpresa (HATEOAS)")
    public ResponseEntity<EntityModel<TiposEmpresaModel>> link(
            @PathVariable Long empresaId,
            @PathVariable Long tipoId
    ) {
        try {
            var rel = service.link(empresaId, tipoId);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(assembler.toModel(rel));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            // relación duplicada
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{relacionId}")
    @Operation(
        summary = "Actualizar parcialmente la relación Empresa ↔ TipoEmpresa (PATCH - HATEOAS)",
        description = "Permite cambiar la empresa y/o el tipo asociados a la relación."
    )
    public ResponseEntity<EntityModel<TiposEmpresaModel>> patch(
            @PathVariable Long empresaId,
            @PathVariable Long relacionId,
            @RequestBody TiposEmpresaModel body
    ) {
        try {
            Long nuevaEmpresaId = (body.getEmpresa() != null)
                    ? body.getEmpresa().getId()
                    : null;

            Long nuevoTipoId = (body.getTipoEmpresa() != null)
                    ? body.getTipoEmpresa().getId()
                    : null;

            TiposEmpresaModel relActualizada =
                    service.patch(relacionId, nuevaEmpresaId, nuevoTipoId);

            return ResponseEntity.ok(assembler.toModel(relActualizada));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{tipoId}")
    @Operation(summary = "Desvincular Empresa ↔ TipoEmpresa (HATEOAS)")
    public ResponseEntity<Void> unlink(
            @PathVariable Long empresaId,
            @PathVariable Long tipoId
    ) {
        try {
            service.unlink(empresaId, tipoId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}