package com.example.NoLimits.Multimedia.controller.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposDeDesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TiposDeDesarrolladorService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/desarrolladores/{desarrolladorId}/tipos")
public class TiposDeDesarrolladorController {

    @Autowired
    private TiposDeDesarrolladorService service;

    @GetMapping
    public ResponseEntity<List<TiposDeDesarrolladorResponseDTO>> listar(@PathVariable Long desarrolladorId) {
        List<TiposDeDesarrolladorResponseDTO> lista = service.findByDesarrollador(desarrolladorId);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{tipoId}")
    public ResponseEntity<TiposDeDesarrolladorResponseDTO> link(@PathVariable Long desarrolladorId,
                                                                @PathVariable Long tipoId) {
        try {
            TiposDeDesarrolladorResponseDTO dto = service.link(desarrolladorId, tipoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{tipoId}")
    public ResponseEntity<Void> unlink(@PathVariable Long desarrolladorId,
                                       @PathVariable Long tipoId) {
        try {
            service.unlink(desarrolladorId, tipoId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH - Actualizar relación Desarrollador–Tipo
    @PatchMapping("/{relacionId}")
    public ResponseEntity<TiposDeDesarrolladorResponseDTO> patch(
            @PathVariable Long relacionId,
            @RequestBody TiposDeDesarrolladorUpdateDTO body
    ) {
        try {
            TiposDeDesarrolladorResponseDTO dto = service.patch(relacionId, body);
            return ResponseEntity.ok(dto);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}