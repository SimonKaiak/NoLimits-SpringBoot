package com.example.NoLimits.Multimedia.controller.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoDeDesarrolladorRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoDeDesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TipoDeDesarrolladorService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TipoDeDesarrolladorResponseDTO>> findAll() {
        List<TipoDeDesarrolladorResponseDTO> lista = service.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDeDesarrolladorResponseDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TipoDeDesarrolladorResponseDTO> save(
            @Valid @RequestBody TipoDeDesarrolladorRequestDTO in) {
        TipoDeDesarrolladorResponseDTO creado = service.save(in);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDeDesarrolladorResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TipoDeDesarrolladorUpdateDTO in
    ) {
        try {
            return ResponseEntity.ok(service.update(id, in));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TipoDeDesarrolladorResponseDTO> patch(
            @PathVariable Long id,
            @RequestBody TipoDeDesarrolladorUpdateDTO in
    ) {
        try {
            return ResponseEntity.ok(service.patch(id, in));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            // Hay relaciones en tipos_de_desarrollador
            return ResponseEntity.badRequest().build();
        }
    }
}