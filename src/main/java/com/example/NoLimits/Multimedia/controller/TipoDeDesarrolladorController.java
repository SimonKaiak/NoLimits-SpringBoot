package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.service.TipoDeDesarrolladorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-desarrollador")
public class TipoDeDesarrolladorController {

    @Autowired
    private TipoDeDesarrolladorService service;

    @GetMapping
    public List<TipoDeDesarrolladorModel> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TipoDeDesarrolladorModel findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public TipoDeDesarrolladorModel save(@Valid @RequestBody TipoDeDesarrolladorModel in) {
        return service.save(in);
    }

    @PutMapping("/{id}")
    public TipoDeDesarrolladorModel update(@PathVariable Long id,
                                           @Valid @RequestBody TipoDeDesarrolladorModel in) {
        return service.update(id, in);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}