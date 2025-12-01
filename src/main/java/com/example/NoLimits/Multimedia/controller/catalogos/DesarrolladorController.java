// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/catalogos/DesarrolladorController.java
package com.example.NoLimits.Multimedia.controller.catalogos;

import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladorRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.DesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladorService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/desarrolladores")
public class DesarrolladorController {

    @Autowired
    private DesarrolladorService desarrolladorService;

    // ================== LISTAR ==================

    @GetMapping
    public List<DesarrolladorResponseDTO> findAll(
            @RequestParam(required = false) String nombre
    ) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return desarrolladorService.findAll();
        }

        return desarrolladorService.findByNombre(nombre);
    }

    // ================== OBTENER POR ID ==================

    @GetMapping("/{id}")
    public DesarrolladorResponseDTO findById(@PathVariable Long id) {
        return desarrolladorService.findById(id);
    }

    // ================== BUSCAR POR NOMBRE ==================

    @GetMapping("/search")
    public List<DesarrolladorResponseDTO> findByNombre(@RequestParam String nombre) {
        return desarrolladorService.findByNombre(nombre);
    }

    // ================== CREAR ==================

    @PostMapping
    public ResponseEntity<DesarrolladorResponseDTO> save(@Valid @RequestBody DesarrolladorRequestDTO dev) {
        DesarrolladorResponseDTO creado = desarrolladorService.save(dev);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ================== ACTUALIZAR COMPLETO (PUT) ==================

    @PutMapping("/{id}")
    public DesarrolladorResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody DesarrolladorUpdateDTO body
    ) {
        return desarrolladorService.update(id, body);
    }

    // ================== ACTUALIZACIÓN PARCIAL (PATCH) ==================

    @PatchMapping("/{id}")
    public DesarrolladorResponseDTO patch(
            @PathVariable Long id,
            @RequestBody DesarrolladorUpdateDTO body
    ) {
        return desarrolladorService.patch(id, body);
    }

    // ================== ELIMINAR ==================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        desarrolladorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}