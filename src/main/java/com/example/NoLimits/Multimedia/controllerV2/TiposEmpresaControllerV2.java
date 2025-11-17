// Ruta: src/main/java/com/example/NoLimits/Multimedia/controllerV2/TiposEmpresaControllerV2.java
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
import org.springframework.web.bind.annotation.*;

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