package com.example.NoLimits.Multimedia.controller.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.service.catalogos.TipoDeDesarrolladorService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public TipoDeDesarrolladorModel update(
            @PathVariable Long id,
            @Valid @RequestBody TipoDeDesarrolladorModel in
    ) {
        return service.update(id, in);
    }

    @PatchMapping("/{id}")
    public TipoDeDesarrolladorModel patch(
            @PathVariable Long id,
            @RequestBody TipoDeDesarrolladorModel in
    ) {
        return service.patch(id, in);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}