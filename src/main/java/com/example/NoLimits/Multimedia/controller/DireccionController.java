package com.example.NoLimits.Multimedia.controller;

import java.util.List;

import com.example.NoLimits.Multimedia.model.DireccionModel;
import com.example.NoLimits.Multimedia.service.DireccionService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/direcciones")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @GetMapping
    public List<DireccionModel> getAll() {
        return direccionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionModel> getById(@PathVariable Long id) {
        DireccionModel direccion = direccionService.findById(id);
        return ResponseEntity.ok(direccion);
    }

    @PostMapping
    public ResponseEntity<DireccionModel> create(@Valid @RequestBody DireccionModel direccion) {
        DireccionModel creada = direccionService.save(direccion);
        return ResponseEntity.status(201).body(creada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DireccionModel> patch(
            @PathVariable Long id,
            @RequestBody DireccionModel entrada) {

        DireccionModel actualizada = direccionService.patch(id, entrada);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        direccionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}