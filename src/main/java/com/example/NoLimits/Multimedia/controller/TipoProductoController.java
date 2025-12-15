package com.example.NoLimits.Multimedia.controller;

import java.util.List;
import java.util.Map;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.TipoProductoModel;
import com.example.NoLimits.Multimedia.service.TipoProductoService;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tipo-productos")
@Tag(name = "TipoProducto-Controller", description = "Operaciones relacionadas con los tipos de productos.")
public class TipoProductoController {

    @Autowired
    private TipoProductoService tipoProductoService;

    // ================== LISTAR TODO ==================

    @GetMapping
    @Operation(
            summary = "Obtener todos los tipos de productos",
            description = "Obtiene una lista completa de los tipos de productos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de tipos de producto obtenida exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TipoProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay tipos de productos registrados.")
    })
    public ResponseEntity<List<TipoProductoModel>> getAllTiposProductos() {
        List<TipoProductoModel> tipos = tipoProductoService.findAll();
        return tipos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(tipos);
    }

    // ================== OBTENER POR ID ==================

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar tipo de producto por ID",
            description = "Obtiene un tipo de producto específico por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de producto encontrado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado.")
    })
    public ResponseEntity<TipoProductoModel> getTipoProductoById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tipoProductoService.findById(id));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== BÚSQUEDA POR NOMBRE (CONTIENE, IGNORE CASE) ==================

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar tipos de producto por nombre (contiene)",
            description = "Obtiene los tipos de producto cuyo nombre contenga el texto indicado (ignorando mayúsculas/minúsculas)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipos de producto encontrados.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TipoProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron tipos de producto para ese criterio.")
    })
    public ResponseEntity<List<TipoProductoModel>> getTipoProductoByNombreLike(
            @RequestParam("nombre") String nombre) {

        List<TipoProductoModel> tipos = tipoProductoService.findByNombre(nombre);
        return tipos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(tipos);
    }

    // ================== BÚSQUEDA POR NOMBRE EXACTO (IGNORE CASE) ==================

    @GetMapping("/nombre-exacto")
    @Operation(
            summary = "Buscar tipo de producto por nombre exacto",
            description = "Obtiene un tipo de producto cuyo nombre coincida exactamente (ignorando mayúsculas/minúsculas)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de producto encontrado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado.")
    })
    public ResponseEntity<TipoProductoModel> getTipoProductoByNombreExacto(
            @RequestParam("nombre") String nombre) {

        try {
            TipoProductoModel tipo = tipoProductoService.findByNombreExactIgnoreCase(nombre);
            return ResponseEntity.ok(tipo);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== CREAR ==================

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Crear un tipo de producto",
            description = "Crea un nuevo tipo de producto en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tipo de producto creado exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el tipo de producto.")
    })
    public ResponseEntity<TipoProductoModel> createTipoProducto(
            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Tipo básico",
                                            description = "Ejemplo de creación de tipo de producto.",
                                            value = """
                                                    {
                                                      "nombre": "Película",
                                                      "descripcion": "Productos de tipo película (Blu-ray, digital, etc.)",
                                                      "activo": true
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody TipoProductoModel tipoProducto
    ) {
        TipoProductoModel nuevoTipo = tipoProductoService.save(tipoProducto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo);
    }

    // ================== ACTUALIZAR (PUT) ==================

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Actualizar un tipo de producto",
            description = "Actualiza completamente un tipo de producto por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de producto actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado.")
    })
    public ResponseEntity<TipoProductoModel> updateTipoProducto(
            @PathVariable Long id,
            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "PUT ejemplo",
                                            description = "Actualización completa de tipo de producto.",
                                            value = """
                                                    {
                                                      "nombre": "Videojuego",
                                                      "descripcion": "Productos de tipo videojuego (consolas, PC, etc.)",
                                                      "activo": true
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody TipoProductoModel detalles
    ) {
        try {
            TipoProductoModel actualizado = tipoProductoService.update(id, detalles);
            return ResponseEntity.ok(actualizado);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== PATCH (PARCIAL) ==================

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Editar parcialmente un tipo de producto",
            description = "Actualiza algunos campos de un tipo de producto existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de producto actualizado parcialmente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado.")
    })
    public ResponseEntity<TipoProductoModel> patchTipoProducto(
            @PathVariable Long id,
            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "PATCH ejemplo",
                                            description = "Actualiza solo algunos campos.",
                                            value = """
                                                    {
                                                      "descripcion": "Actualizado: productos de entretenimiento.",
                                                      "activo": false
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody TipoProductoModel detalles
    ) {
        try {
            TipoProductoModel actualizado = tipoProductoService.patch(id, detalles);
            return ResponseEntity.ok(actualizado);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== ELIMINAR ==================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un tipo de producto",
            description = "Elimina un tipo de producto por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de producto eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado."),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar: existen productos asociados.")
    })
    public ResponseEntity<Void> deleteTipoProducto(@PathVariable Long id) {
        tipoProductoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================== ACTIVO / INACTIVO ==================

    @GetMapping("/activos")
    @Operation(
            summary = "Listar tipos de producto activos",
            description = "Devuelve todos los tipos de producto marcados como activos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipos de producto activos encontrados.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TipoProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay tipos de producto activos.")
    })
    public ResponseEntity<List<TipoProductoModel>> listarActivos() {
        List<TipoProductoModel> tipos = tipoProductoService.findActivos();
        return tipos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(tipos);
    }

    @GetMapping("/inactivos")
    @Operation(
            summary = "Listar tipos de producto inactivos",
            description = "Devuelve todos los tipos de producto marcados como inactivos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipos de producto inactivos encontrados.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TipoProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay tipos de producto inactivos.")
    })
    public ResponseEntity<List<TipoProductoModel>> listarInactivos() {
        List<TipoProductoModel> tipos = tipoProductoService.findInactivos();
        return tipos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(tipos);
    }

    // ================== RESUMEN ==================

    @GetMapping("/resumen")
    @Operation(
            summary = "Obtener resumen de tipos de producto",
            description = "Devuelve un resumen con ID, Nombre, Descripción y estado activo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumen obtenido exitosamente.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "204", description = "No hay tipos de producto para mostrar en el resumen.")
    })
    public ResponseEntity<List<Map<String, Object>>> obtenerResumen() {
        List<Map<String, Object>> resumen = tipoProductoService.obtenerTipoProductoConNombres();
        return resumen.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(resumen);
    }
}