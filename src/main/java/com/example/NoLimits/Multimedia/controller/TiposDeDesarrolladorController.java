package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.TiposDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.service.TiposDeDesarrolladorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}