package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.service.TipoEmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-empresa")
@Tag(name = "TipoEmpresa-Controller", description = "CRUD de tipos de empresa (TNP).")
public class TipoEmpresaController {

    @Autowired
    private TipoEmpresaService tipoEmpresaService;

    // --------------------------------------------------------
    // GET - Listar
    // --------------------------------------------------------
    @GetMapping
    @Operation(summary = "Listar todos los tipos de empresa")
    public List<TipoEmpresaModel> findAll() {
        return tipoEmpresaService.findAll();
    }

    // --------------------------------------------------------
    // GET - Obtener por ID
    // --------------------------------------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un tipo de empresa por ID")
    public TipoEmpresaModel findById(@PathVariable Long id) {
        return tipoEmpresaService.findById(id);
    }

    // --------------------------------------------------------
    // POST - Crear
    // --------------------------------------------------------
    @PostMapping
    @Operation(summary = "Crear un nuevo tipo de empresa")
    public ResponseEntity<TipoEmpresaModel> save(@RequestBody TipoEmpresaModel tipo) {
        TipoEmpresaModel creado = tipoEmpresaService.save(tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // --------------------------------------------------------
    // PUT - Actualizar completo
    // --------------------------------------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un tipo de empresa existente")
    public TipoEmpresaModel update(
            @PathVariable Long id,
            @RequestBody TipoEmpresaModel tipo
    ) {
        return tipoEmpresaService.update(id, tipo);
    }

    // --------------------------------------------------------
    // PATCH - Actualización parcial
    // --------------------------------------------------------
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un tipo de empresa existente")
    public ResponseEntity<TipoEmpresaModel> patch(
            @PathVariable Long id,
            @RequestBody TipoEmpresaModel cambios
    ) {
        TipoEmpresaModel actualizado = tipoEmpresaService.patch(id, cambios);
        return ResponseEntity.ok(actualizado);
    }

    // --------------------------------------------------------
    // DELETE - Eliminar
    // --------------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un tipo de empresa por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoEmpresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}