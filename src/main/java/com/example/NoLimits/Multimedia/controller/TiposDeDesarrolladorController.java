// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/TiposDeDesarrolladorController.java
package com.example.NoLimits.Multimedia.controller;

import java.util.List;

import com.example.NoLimits.Multimedia.model.TiposDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.service.TiposDeDesarrolladorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/desarrolladores/{desarrolladorId}/tipos")
public class TiposDeDesarrolladorController {

    @Autowired
    private TiposDeDesarrolladorService service;

    @GetMapping
    public List<TiposDeDesarrolladorModel> listar(@PathVariable Long desarrolladorId) {
        return service.findAll().stream()
                .filter(t -> t.getDesarrollador().getId().equals(desarrolladorId))
                .toList();
    }

    @PostMapping("/{tipoId}")
    public TiposDeDesarrolladorModel link(@PathVariable Long desarrolladorId,
                                          @PathVariable Long tipoId) {
        return service.link(desarrolladorId, tipoId);
    }

    @DeleteMapping("/{tipoId}")
    public void unlink(@PathVariable Long desarrolladorId,
                       @PathVariable Long tipoId) {
        service.unlink(desarrolladorId, tipoId);
    }

    // PATCH - Actualizar relación Desarrollador–Tipo
    @PatchMapping("/{relacionId}")
    public TiposDeDesarrolladorModel patch(
            @PathVariable Long relacionId,
            @RequestParam(required = false) Long nuevoDesarrolladorId,
            @RequestParam(required = false) Long nuevoTipoId
    ) {
        return service.patch(relacionId, nuevoDesarrolladorId, nuevoTipoId);
    }
}