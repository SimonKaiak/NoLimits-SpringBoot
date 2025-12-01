package com.example.NoLimits.Multimedia.controller.catalogos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.dto.catalogos.request.EmpresaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresas")
@Tag(name = "Empresa-Controller", description = "CRUD básico de empresas (TNP).")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    @Operation(summary = "Listar todas las empresas")
    public ResponseEntity<List<EmpresaResponseDTO>> findAll() {
        List<EmpresaResponseDTO> empresas = empresaService.findAll();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una empresa por ID")
    public ResponseEntity<EmpresaResponseDTO> findById(@PathVariable Long id) {
        EmpresaResponseDTO empresa = empresaService.findById(id);
        return ResponseEntity.ok(empresa);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva empresa")
    public ResponseEntity<EmpresaResponseDTO> save(@Valid @RequestBody EmpresaRequestDTO requestDTO) {
        EmpresaResponseDTO creada = empresaService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una empresa existente (PUT)")
    public ResponseEntity<EmpresaResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaRequestDTO requestDTO
    ) {
        EmpresaResponseDTO actualizada = empresaService.update(id, requestDTO);
        return ResponseEntity.ok(actualizada);
    }

    // PATCH – actualización parcial
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una empresa (PATCH)")
    public ResponseEntity<EmpresaResponseDTO> patch(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaUpdateDTO updateDTO
    ) {
        EmpresaResponseDTO actualizada = empresaService.patch(id, updateDTO);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una empresa por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}