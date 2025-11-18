package com.example.NoLimits.Multimedia.controller;

import java.util.List;

import com.example.NoLimits.Multimedia.model.PlataformaModel;
import com.example.NoLimits.Multimedia.service.PlataformaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plataformas")
@Tag(name = "Plataforma-Controller", description = "CRUD básico de plataformas (TNP).")
public class PlataformaController {

    @Autowired
    private PlataformaService plataformaService;

    @GetMapping
    @Operation(summary = "Listar todas las plataformas")
    public List<PlataformaModel> findAll() {
        return plataformaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una plataforma por ID")
    public PlataformaModel findById(@PathVariable Long id) {
        return plataformaService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva plataforma")
    public ResponseEntity<PlataformaModel> save(@RequestBody PlataformaModel plataforma) {
        PlataformaModel creada = plataformaService.save(plataforma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una plataforma existente")
    public PlataformaModel update(
            @PathVariable Long id,
            @RequestBody PlataformaModel plataforma
    ) {
        return plataformaService.update(id, plataforma);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una plataforma (PATCH)")
    public PlataformaModel patch(
            @PathVariable Long id,
            @RequestBody PlataformaModel plataforma
    ) {
        return plataformaService.patch(id, plataforma);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una plataforma por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        plataformaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}