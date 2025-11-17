package com.example.NoLimits.Multimedia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.EstadoModel;
import com.example.NoLimits.Multimedia.service.EstadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/estados")
@Tag(name = "Estado-Controller", description = "Gestión del catálogo de estados (Activo, Agotado, etc.).")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    // ================== LISTAR TODOS ==================
    @GetMapping
    @Operation(
        summary = "Listar todos los estados",
        description = "Obtiene la lista completa de estados registrados."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estados encontrados.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = EstadoModel.class))
            )
        ),
        @ApiResponse(responseCode = "204", description = "No hay estados registrados.")
    })
    public ResponseEntity<List<EstadoModel>> listarEstados() {
        List<EstadoModel> estados = estadoService.findAll();
        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estados);
    }

    // ================== BUSCAR POR ID ==================
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar estado por ID",
        description = "Obtiene un estado específico según su ID."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estado encontrado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EstadoModel.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado.")
    })
    public ResponseEntity<EstadoModel> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(estadoService.findById(id));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== BUSCAR POR NOMBRE (LIKE) ==================
    @GetMapping("/nombre/{nombre}")
    @Operation(
        summary = "Buscar estados por nombre",
        description = "Busca estados cuyo nombre contenga el texto indicado (ignorando mayúsculas/minúsculas)."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estados encontrados.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = EstadoModel.class))
            )
        ),
        @ApiResponse(responseCode = "204", description = "No se encontraron estados que coincidan.")
    })
    public ResponseEntity<List<EstadoModel>> buscarPorNombre(@PathVariable String nombre) {
        List<EstadoModel> estados = estadoService.findByNombreLike(nombre);
        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estados);
    }

    // ================== LISTAR ACTIVOS ==================
    @GetMapping("/activos")
    @Operation(
        summary = "Listar estados activos",
        description = "Devuelve todos los estados con el campo 'activo' = true."
    )
    public ResponseEntity<List<EstadoModel>> listarActivos() {
        List<EstadoModel> activos = estadoService.findActivos();
        if (activos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activos);
    }

    // ================== LISTAR INACTIVOS ==================
    @GetMapping("/inactivos")
    @Operation(
        summary = "Listar estados inactivos",
        description = "Devuelve todos los estados con el campo 'activo' = false."
    )
    public ResponseEntity<List<EstadoModel>> listarInactivos() {
        List<EstadoModel> inactivos = estadoService.findInactivos();
        if (inactivos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inactivos);
    }

    // ================== RESUMEN ==================
    @GetMapping("/resumen")
    @Operation(
        summary = "Obtener resumen de estados",
        description = "Devuelve un resumen tabular (ID, Nombre, Descripción, Activo) de los estados."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Resumen generado correctamente.",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                description = "Lista de filas con ID, Nombre, Descripcion y Activo.",
                implementation = Object.class
            )
        )
    )
    public ResponseEntity<List<Map<String, Object>>> obtenerResumen() {
        List<Map<String, Object>> resumen = estadoService.obtenerEstadosResumen();
        if (resumen.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumen);
    }

    // ================== CREAR ==================
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Crear un nuevo estado",
        description = "Registra un nuevo estado en el sistema.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EstadoModel.class),
                examples = {
                    @ExampleObject(
                        name = "Estado básico",
                        description = "Ejemplo mínimo para crear un estado.",
                        value = """
                        {
                          "nombre": "Activo",
                          "descripcion": "Producto disponible para su compra",
                          "activo": true
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Estado creado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EstadoModel.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado.")
    })
    public ResponseEntity<EstadoModel> crearEstado(@Valid @RequestBody EstadoModel body) {
        EstadoModel creado = estadoService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ================== ACTUALIZAR (PUT) ==================
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Actualizar un estado (PUT)",
        description = "Reemplaza los datos de un estado existente."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estado actualizado correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EstadoModel.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado.")
    })
    public ResponseEntity<EstadoModel> actualizarEstado(
            @PathVariable Long id,
            @RequestBody EstadoModel body) {
        try {
            EstadoModel actualizado = estadoService.update(id, body);
            return ResponseEntity.ok(actualizado);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== ACTUALIZAR PARCIAL (PATCH) ==================
    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Actualizar parcialmente un estado (PATCH)",
        description = "Modifica solo los campos enviados de un estado existente."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estado actualizado parcialmente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EstadoModel.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado.")
    })
    public ResponseEntity<EstadoModel> patchEstado(
            @PathVariable Long id,
            @RequestBody EstadoModel body) {
        try {
            EstadoModel actualizado = estadoService.patch(id, body);
            return ResponseEntity.ok(actualizado);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== ELIMINAR ==================
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar un estado",
        description = "Elimina un estado por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Estado eliminado correctamente."),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado.")
    })
    public ResponseEntity<Void> eliminarEstado(@PathVariable Long id) {
        try {
            estadoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}