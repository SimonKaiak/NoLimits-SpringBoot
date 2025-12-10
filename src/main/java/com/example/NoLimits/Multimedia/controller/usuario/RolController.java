package com.example.NoLimits.Multimedia.controller.usuario;

import java.util.List;

import com.example.NoLimits.Multimedia.dto.usuario.request.RolRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.RolUpdateDTO;
import com.example.NoLimits.Multimedia.service.usuario.RolService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    /**
     * GET — Listar todos los roles
     */
    @GetMapping
    public ResponseEntity<List<RolResponseDTO>> getAll() {
        List<RolResponseDTO> roles = rolService.findAll();
        return roles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(roles);
    }

    /**
     * GET — Buscar rol por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RolResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.findById(id));
    }

    /**
     * POST — Crear un rol
     */
    @PostMapping
    public ResponseEntity<RolResponseDTO> create(
            @Valid @RequestBody RolRequestDTO dto) {

        RolResponseDTO creado = rolService.save(dto);
        return ResponseEntity.status(201).body(creado);
    }

    /**
     * PUT — Actualizar completamente un rol
     */
    @PutMapping("/{id}")
    public ResponseEntity<RolResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RolUpdateDTO dto) {

        RolResponseDTO actualizado = rolService.update(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * PATCH — Actualización parcial
     */
    @PatchMapping("/{id}")
    public ResponseEntity<RolResponseDTO> patch(
            @PathVariable Long id,
            @RequestBody RolUpdateDTO dto) {

        RolResponseDTO actualizado = rolService.patch(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * DELETE — Eliminar un rol por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}