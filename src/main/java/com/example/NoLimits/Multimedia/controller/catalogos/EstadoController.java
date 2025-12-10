package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.EstadoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EstadoUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.EstadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/estados")
@Tag(name = "Estado-Controller", description = "Gestión del catálogo de estados.")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    // ================== LISTAR TODOS ==================
    @GetMapping
    @Operation(summary = "Listar todos los estados")
    public ResponseEntity<List<EstadoResponseDTO>> listarEstados() {
        List<EstadoResponseDTO> estados = estadoService.findAll();
        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estados);
    }

    // ================== LISTAR PAGINADO ==================
    @GetMapping("/paginado")
    @Operation(summary = "Listar estados con paginación y búsqueda")
    public ResponseEntity<PagedResponse<EstadoResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        PagedResponse<EstadoResponseDTO> resultado =
                estadoService.listarPaginado(page, size, search);

        return ResponseEntity.ok(resultado);
    }

    // ================== BUSCAR POR ID ==================
    @GetMapping("/{id}")
    @Operation(summary = "Buscar estado por ID")
    public ResponseEntity<EstadoResponseDTO> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(estadoService.findById(id));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== BUSCAR POR NOMBRE ==================
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar estados por nombre")
    public ResponseEntity<List<EstadoResponseDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<EstadoResponseDTO> estados = estadoService.findByNombreLike(nombre);
        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estados);
    }

    // ================== LISTAR ACTIVOS ==================
    @GetMapping("/activos")
    @Operation(summary = "Listar estados activos")
    public ResponseEntity<List<EstadoResponseDTO>> listarActivos() {
        List<EstadoResponseDTO> activos = estadoService.findActivos();
        if (activos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activos);
    }

    // ================== LISTAR INACTIVOS ==================
    @GetMapping("/inactivos")
    @Operation(summary = "Listar estados inactivos")
    public ResponseEntity<List<EstadoResponseDTO>> listarInactivos() {
        List<EstadoResponseDTO> inactivos = estadoService.findInactivos();
        if (inactivos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inactivos);
    }

    // ================== RESUMEN ==================
    @GetMapping("/resumen")
    @Operation(summary = "Obtener resumen de estados")
    public ResponseEntity<List<Map<String, Object>>> obtenerResumen() {
        List<Map<String, Object>> resumen = estadoService.obtenerEstadosResumen();
        if (resumen.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumen);
    }

    // ================== CREAR ==================
    @PostMapping
    @Operation(summary = "Crear un nuevo estado")
    public ResponseEntity<EstadoResponseDTO> crearEstado(
            @Valid @RequestBody EstadoRequestDTO body) {

        EstadoResponseDTO creado = estadoService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ================== ACTUALIZAR (PUT) ==================
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un estado (PUT)")
    public ResponseEntity<EstadoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO body) {

        try {
            return ResponseEntity.ok(estadoService.update(id, body));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== ACTUALIZAR PARCIAL (PATCH) ==================
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un estado (PATCH)")
    public ResponseEntity<EstadoResponseDTO> patchEstado(
            @PathVariable Long id,
            @RequestBody EstadoUpdateDTO body) {

        try {
            return ResponseEntity.ok(estadoService.patch(id, body));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== ELIMINAR ==================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un estado")
    public ResponseEntity<Void> eliminarEstado(@PathVariable Long id) {
        try {
            estadoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}