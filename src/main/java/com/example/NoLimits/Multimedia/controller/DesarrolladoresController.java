package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.DesarrolladoresModel;
import com.example.NoLimits.Multimedia.service.DesarrolladoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos/{productoId}/desarrolladores")
public class DesarrolladoresController {

    @Autowired
    private DesarrolladoresService service;

    @GetMapping
    public List<DesarrolladoresModel> listar(@PathVariable Long productoId) {
        return service.findByProducto(productoId);
    }

    @PostMapping("/{desarrolladorId}")
    public DesarrolladoresModel link(@PathVariable Long productoId,
                                     @PathVariable Long desarrolladorId) {
        return service.link(productoId, desarrolladorId);
    }

    @DeleteMapping("/{desarrolladorId}")
    public void unlink(@PathVariable Long productoId,
                       @PathVariable Long desarrolladorId) {
        service.unlink(productoId, desarrolladorId);
    }
}