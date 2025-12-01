package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposEmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TiposEmpresaService;

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
    public List<TiposEmpresaResponseDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        return tiposEmpresaService.findAllByEmpresa(empresaId);
    }

    @PostMapping("/{tipoId}")
    @Operation(summary = "Vincular Empresa ↔ TipoEmpresa")
    public ResponseEntity<TiposEmpresaResponseDTO> link(
            @PathVariable Long empresaId,
            @PathVariable Long tipoId) {

        TiposEmpresaResponseDTO rel = tiposEmpresaService.link(empresaId, tipoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(rel);
    }

    @PatchMapping("/{relacionId}")
    @Operation(
        summary = "Actualizar parcialmente la relación Empresa ↔ TipoEmpresa (PATCH)",
        description = "Permite cambiar la empresa o el tipo de empresa asociados."
    )
    public TiposEmpresaResponseDTO patch(
            @PathVariable Long relacionId,
            @RequestBody TiposEmpresaUpdateDTO body) {

        return tiposEmpresaService.patch(relacionId, body);
    }

    @DeleteMapping("/{tipoId}")
    @Operation(summary = "Desvincular Empresa ↔ TipoEmpresa")
    public ResponseEntity<Void> unlink(
            @PathVariable Long empresaId,
            @PathVariable Long tipoId) {

        tiposEmpresaService.unlink(empresaId, tipoId);
        return ResponseEntity.noContent().build();
    }
}