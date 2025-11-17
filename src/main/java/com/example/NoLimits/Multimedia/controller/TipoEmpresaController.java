// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/TipoEmpresaController.java
package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.service.TipoEmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-empresa")
@Tag(name = "TipoEmpresa-Controller", description = "CRUD de tipos de empresa (TNP).")
public class TipoEmpresaController {

    @Autowired
    private TipoEmpresaService tipoEmpresaService;

    @GetMapping
    @Operation(summary = "Listar todos los tipos de empresa")
    public List<TipoEmpresaModel> findAll() {
        return tipoEmpresaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un tipo de empresa por ID")
    public TipoEmpresaModel findById(@PathVariable Long id) {
        return tipoEmpresaService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo tipo de empresa")
    public ResponseEntity<TipoEmpresaModel> save(@RequestBody TipoEmpresaModel tipo) {
        TipoEmpresaModel creado = tipoEmpresaService.save(tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un tipo de empresa existente")
    public TipoEmpresaModel update(
            @PathVariable Long id,
            @RequestBody TipoEmpresaModel tipo
    ) {
        return tipoEmpresaService.update(id, tipo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un tipo de empresa por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoEmpresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}