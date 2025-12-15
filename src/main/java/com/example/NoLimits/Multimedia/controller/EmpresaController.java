package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.EmpresaModel;
import com.example.NoLimits.Multimedia.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresas")
@Tag(name = "Empresa-Controller", description = "CRUD básico de empresas (TNP).")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    @Operation(summary = "Listar todas las empresas")
    public List<EmpresaModel> findAll() {
        return empresaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una empresa por ID")
    public EmpresaModel findById(@PathVariable Long id) {
        return empresaService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva empresa")
    public ResponseEntity<EmpresaModel> save(@RequestBody EmpresaModel empresa) {
        EmpresaModel creada = empresaService.save(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una empresa existente (PUT)")
    public EmpresaModel update(
            @PathVariable Long id,
            @RequestBody EmpresaModel empresa
    ) {
        return empresaService.update(id, empresa);
    }

    // PATCH – actualización parcial
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una empresa (PATCH)")
    public EmpresaModel patch(
            @PathVariable Long id,
            @RequestBody EmpresaModel empresa
    ) {
        return empresaService.patch(id, empresa);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una empresa por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}