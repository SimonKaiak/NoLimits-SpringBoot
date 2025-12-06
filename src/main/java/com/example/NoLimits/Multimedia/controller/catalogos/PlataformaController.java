package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.PlataformaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.PlataformaUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/plataformas")
@Tag(name = "Plataforma-Controller", description = "CRUD básico de plataformas (TNP).")
public class PlataformaController {

    @Autowired
    private PlataformaService plataformaService;

    @GetMapping
    @Operation(summary = "Listar todas las plataformas")
    public ResponseEntity<List<PlataformaResponseDTO>> findAll() {
        List<PlataformaResponseDTO> lista = plataformaService.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    // ================== LISTAR PAGINADO ==================
    @GetMapping("/paginado")
    @Operation(summary = "Listar plataformas con paginación y búsqueda")
    public ResponseEntity<PagedResponse<PlataformaResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        PagedResponse<PlataformaResponseDTO> resultado =
                plataformaService.listarPaginado(page, size, search);

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una plataforma por ID")
    public ResponseEntity<PlataformaResponseDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(plataformaService.findById(id));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva plataforma")
    public ResponseEntity<PlataformaResponseDTO> save(@Valid @RequestBody PlataformaRequestDTO plataforma) {
        PlataformaResponseDTO creada = plataformaService.save(plataforma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una plataforma existente (PUT)")
    public ResponseEntity<PlataformaResponseDTO> update(
            @PathVariable Long id,
            @RequestBody PlataformaUpdateDTO plataforma
    ) {
        try {
            return ResponseEntity.ok(plataformaService.update(id, plataforma));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una plataforma (PATCH)")
    public ResponseEntity<PlataformaResponseDTO> patch(
            @PathVariable Long id,
            @RequestBody PlataformaUpdateDTO plataforma
    ) {
        try {
            return ResponseEntity.ok(plataformaService.patch(id, plataforma));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una plataforma por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            plataformaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}