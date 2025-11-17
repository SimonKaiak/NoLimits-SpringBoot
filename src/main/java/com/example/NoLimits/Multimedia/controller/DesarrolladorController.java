package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.DesarrolladorModel;
import com.example.NoLimits.Multimedia.service.DesarrolladorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/desarrolladores")
public class DesarrolladorController {

    @Autowired
    private DesarrolladorService desarrolladorService;

    @GetMapping
    public List<DesarrolladorModel> findAll() {
        return desarrolladorService.findAll();
    }

    @GetMapping("/{id}")
    public DesarrolladorModel findById(@PathVariable Long id) {
        return desarrolladorService.findById(id);
    }

    @GetMapping("/search")
    public List<DesarrolladorModel> findByNombre(@RequestParam String nombre) {
        return desarrolladorService.findByNombre(nombre);
    }

    @PostMapping
    public DesarrolladorModel save(@Valid @RequestBody DesarrolladorModel dev) {
        return desarrolladorService.save(dev);
    }

    @PutMapping("/{id}")
    public DesarrolladorModel update(@PathVariable Long id,
                                     @Valid @RequestBody DesarrolladorModel body) {
        return desarrolladorService.update(id, body);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        desarrolladorService.deleteById(id);
    }
}