package com.example.NoLimits.Multimedia.controller.ubicacion;

import java.util.List;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.RegionRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.RegionResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.RegionUpdateDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.RegionService;

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
    public List<RegionResponseDTO> getAll() {
        return regionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponseDTO> getById(@PathVariable Long id) {
        RegionResponseDTO region = regionService.findById(id);
        return ResponseEntity.ok(region);
    }

    @PostMapping
    public ResponseEntity<RegionResponseDTO> create(
            @Valid @RequestBody RegionRequestDTO region) {

        RegionResponseDTO creada = regionService.save(region);
        return ResponseEntity.status(201).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RegionUpdateDTO in) {

        RegionResponseDTO actualizada = regionService.update(id, in);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RegionResponseDTO> patch(
            @PathVariable Long id,
            @RequestBody RegionUpdateDTO in) {

        RegionResponseDTO actualizada = regionService.patch(id, in);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}