package com.example.NoLimits.Multimedia.controller.ubicacion;

import java.util.List;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.DireccionResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.DireccionUpdateDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.DireccionService;

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
@RequestMapping("/api/direcciones")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @GetMapping
    public ResponseEntity<List<DireccionResponseDTO>> getAll() {
        List<DireccionResponseDTO> direcciones = direccionService.findAll();
        return direcciones.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(direcciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> getById(@PathVariable Long id) {
        DireccionResponseDTO direccion = direccionService.findById(id);
        return ResponseEntity.ok(direccion);
    }

    @PostMapping
    public ResponseEntity<DireccionResponseDTO> create(
            @Valid @RequestBody DireccionRequestDTO direccionRequest) {

        DireccionResponseDTO creada = direccionService.save(direccionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DireccionUpdateDTO direccionUpdate) {

        DireccionResponseDTO actualizada = direccionService.update(id, direccionUpdate);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> patch(
            @PathVariable Long id,
            @RequestBody DireccionUpdateDTO entrada) {

        DireccionResponseDTO actualizada = direccionService.patch(id, entrada);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        direccionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}