package com.example.NoLimits.Multimedia.controller;

import java.util.List;

import com.example.NoLimits.Multimedia.model.RegionModel;
import com.example.NoLimits.Multimedia.service.RegionService;

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
@RequestMapping("/api/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public List<RegionModel> getAll() {
        return regionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionModel> getById(@PathVariable Long id) {
        RegionModel region = regionService.findById(id);
        return ResponseEntity.ok(region);
    }

    @PostMapping
    public ResponseEntity<RegionModel> create(@Valid @RequestBody RegionModel region) {
        RegionModel creada = regionService.save(region);
        return ResponseEntity.status(201).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionModel> update(
            @PathVariable Long id,
            @Valid @RequestBody RegionModel in) {

        RegionModel actualizada = regionService.update(id, in);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RegionModel> patch(
            @PathVariable Long id,
            @RequestBody RegionModel in) {

        RegionModel actualizada = regionService.patch(id, in);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}