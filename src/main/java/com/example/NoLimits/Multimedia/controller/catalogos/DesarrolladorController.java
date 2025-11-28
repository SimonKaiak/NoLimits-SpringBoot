package com.example.NoLimits.Multimedia.controller.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladorService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/desarrolladores")
public class DesarrolladorController {

    @Autowired
    private DesarrolladorService desarrolladorService;

    // ================== LISTAR ==================

    @GetMapping
    public List<DesarrolladorModel> findAll(
            @RequestParam(required = false) String nombre
    ) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return desarrolladorService.findAll();
        }

        return desarrolladorService.findByNombre(nombre);
    }

    // ================== OBTENER POR ID ==================

    @GetMapping("/{id}")
    public DesarrolladorModel findById(@PathVariable Long id) {
        return desarrolladorService.findById(id);
    }

    // ================== BUSCAR POR NOMBRE ==================

    @GetMapping("/search")
    public List<DesarrolladorModel> findByNombre(@RequestParam String nombre) {
        return desarrolladorService.findByNombre(nombre);
    }

    // ================== CREAR ==================

    @PostMapping
    public DesarrolladorModel save(@Valid @RequestBody DesarrolladorModel dev) {
        return desarrolladorService.save(dev);
    }

    // ================== ACTUALIZAR COMPLETO (PUT) ==================

    @PutMapping("/{id}")
    public DesarrolladorModel update(
            @PathVariable Long id,
            @Valid @RequestBody DesarrolladorModel body
    ) {
        return desarrolladorService.update(id, body);
    }

    // ================== ACTUALIZACIÓN PARCIAL (PATCH) ==================

    @PatchMapping("/{id}")
    public DesarrolladorModel patch(
            @PathVariable Long id,
            @RequestBody DesarrolladorModel body
    ) {
        return desarrolladorService.patch(id, body);
    }

    // ================== ELIMINAR ==================

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        desarrolladorService.deleteById(id);
    }
}