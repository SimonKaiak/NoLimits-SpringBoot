package com.example.NoLimits.Multimedia.controller;

import java.util.List;

import com.example.NoLimits.Multimedia.model.ComunaModel;
import com.example.NoLimits.Multimedia.service.ComunaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comunas")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public List<ComunaModel> getAll() {
        return comunaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaModel> getById(@PathVariable Long id) {
        ComunaModel comuna = comunaService.findById(id);
        return ResponseEntity.ok(comuna);
    }

    @PostMapping
    public ResponseEntity<ComunaModel> create(@Valid @RequestBody ComunaModel comuna) {
        ComunaModel creada = comunaService.save(comuna);
        return ResponseEntity.status(201).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComunaModel> update(
            @PathVariable Long id,
            @Valid @RequestBody ComunaModel comuna) {

        ComunaModel actualizada = comunaService.update(id, comuna);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ComunaModel> patch(
            @PathVariable Long id,
            @RequestBody ComunaModel parciales) {

        ComunaModel actualizada = comunaService.patch(id, parciales);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comunaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}