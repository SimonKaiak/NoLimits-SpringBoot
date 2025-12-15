package com.example.NoLimits.Multimedia.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia.model.TiposEmpresaModel;
import com.example.NoLimits.Multimedia.service.TiposEmpresaService;

import org.springframework.beans.factory.annotation.Autowired;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/empresas/{empresaId}/tipos-empresa")
@Tag(name = "TiposEmpresa-Controller", description = "Relación Empresa ↔ TipoEmpresa (TP).")
public class TiposEmpresaController {

    @Autowired
    private TiposEmpresaService tiposEmpresaService;

    @GetMapping
    @Operation(summary = "Listar tipos de empresa asociados a una empresa")
    public List<TiposEmpresaModel> listarPorEmpresa(@PathVariable Long empresaId) {
        return tiposEmpresaService.findAll().stream()
                .filter(rel -> rel.getEmpresa() != null
                        && rel.getEmpresa().getId().equals(empresaId))
                .collect(Collectors.toList());
    }

    @PostMapping("/{tipoId}")
    @Operation(summary = "Vincular Empresa ↔ TipoEmpresa")
    public ResponseEntity<TiposEmpresaModel> link(
            @PathVariable Long empresaId,
            @PathVariable Long tipoId
    ) {
        TiposEmpresaModel rel = tiposEmpresaService.link(empresaId, tipoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(rel);
    }

    @PatchMapping("/{relacionId}")
    @Operation(
        summary = "Actualizar parcialmente la relación Empresa ↔ TipoEmpresa (PATCH)",
        description = "Permite cambiar la empresa o el tipo de empresa asociados."
    )
    public TiposEmpresaModel patch(
            @PathVariable Long empresaId,
            @PathVariable Long relacionId,
            @RequestBody TiposEmpresaModel body
    ) {

        Long nuevaEmpresaId = (body.getEmpresa() != null)
                ? body.getEmpresa().getId()
                : null;

        Long nuevoTipoId = (body.getTipoEmpresa() != null)
                ? body.getTipoEmpresa().getId()
                : null;

        return tiposEmpresaService.patch(relacionId, nuevaEmpresaId, nuevoTipoId);
    }

    @DeleteMapping("/{tipoId}")
    @Operation(summary = "Desvincular Empresa ↔ TipoEmpresa")
    public ResponseEntity<Void> unlink(
            @PathVariable Long empresaId,
            @PathVariable Long tipoId
    ) {
        tiposEmpresaService.unlink(empresaId, tipoId);
        return ResponseEntity.noContent().build();
    }
}