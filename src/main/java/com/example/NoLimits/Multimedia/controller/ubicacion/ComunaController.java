package com.example.NoLimits.Multimedia.controller.ubicacion;

import java.util.List;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.ComunaRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.ComunaUpdateDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.ComunaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<ComunaResponseDTO>> getAll() {
        List<ComunaResponseDTO> comunas = comunaService.findAll();
        return comunas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(comunas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaResponseDTO> getById(@PathVariable Long id) {
        ComunaResponseDTO comuna = comunaService.findById(id);
        return ResponseEntity.ok(comuna);
    }

    @PostMapping
    public ResponseEntity<ComunaResponseDTO> create(@Valid @RequestBody ComunaRequestDTO comunaRequest) {
        ComunaResponseDTO creada = comunaService.create(comunaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComunaResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ComunaUpdateDTO comunaUpdate) {

        ComunaResponseDTO actualizada = comunaService.update(id, comunaUpdate);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ComunaResponseDTO> patch(
            @PathVariable Long id,
            @RequestBody ComunaUpdateDTO parciales) {

        ComunaResponseDTO actualizada = comunaService.patch(id, parciales);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comunaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}