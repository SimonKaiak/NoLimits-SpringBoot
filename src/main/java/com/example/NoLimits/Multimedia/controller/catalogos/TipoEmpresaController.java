package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoEmpresaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoEmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TipoEmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/tipos-empresa")
@Tag(name = "TipoEmpresa-Controller", description = "CRUD de tipos de empresa con DTO.")
public class TipoEmpresaController {

    @Autowired
    private TipoEmpresaService tipoEmpresaService;

    @GetMapping
    @Operation(summary = "Listar todos los tipos de empresa")
    public List<TipoEmpresaResponseDTO> findAll() {
        return tipoEmpresaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un tipo de empresa por ID")
    public TipoEmpresaResponseDTO findById(@PathVariable Long id) {
        return tipoEmpresaService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo tipo de empresa")
    public ResponseEntity<TipoEmpresaResponseDTO> save(@RequestBody TipoEmpresaRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tipoEmpresaService.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un tipo de empresa")
    public TipoEmpresaResponseDTO update(
            @PathVariable Long id,
            @RequestBody TipoEmpresaRequestDTO dto) {
        return tipoEmpresaService.update(id, dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un tipo de empresa")
    public TipoEmpresaResponseDTO patch(
            @PathVariable Long id,
            @RequestBody TipoEmpresaUpdateDTO dto) {
        return tipoEmpresaService.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un tipo de empresa")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoEmpresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}