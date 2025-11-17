// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/TiposEmpresaController.java
package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.TiposEmpresaModel;
import com.example.NoLimits.Multimedia.service.TiposEmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/empresas/{empresaId}/tipos-empresa")
@Tag(name = "TiposEmpresa-Controller", description = "Relación Empresa ↔ TipoEmpresa (TP).")
public class TiposEmpresaController {

    @Autowired
    private TiposEmpresaService tiposEmpresaService;

    @GetMapping
    @Operation(summary = "Listar tipos de empresa asociados a una empresa")
    public List<TiposEmpresaModel> listarPorEmpresa(@PathVariable Long empresaId) {
        // El servicio tiene findAll(), filtramos por empresaId
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