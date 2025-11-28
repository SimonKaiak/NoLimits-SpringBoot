package com.example.NoLimits.Multimedia.controller.usuario;

import java.util.List;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.service.usuario.RolService;

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
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public List<RolModel> getAll() {
        return rolService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolModel> getById(@PathVariable Long id) {
        RolModel rol = rolService.findById(id);
        return ResponseEntity.ok(rol);
    }

    @PostMapping
    public ResponseEntity<RolModel> create(@Valid @RequestBody RolModel rol) {
        RolModel creado = rolService.save(rol);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolModel> update(
            @PathVariable Long id,
            @Valid @RequestBody RolModel in) {

        RolModel actualizado = rolService.update(id, in);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RolModel> patch(
            @PathVariable Long id,
            @RequestBody RolModel in) {

        RolModel actualizado = rolService.patch(id, in);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}