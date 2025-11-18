package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.ClasificacionModel;
import com.example.NoLimits.Multimedia.service.ClasificacionService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clasificaciones")
@Tag(
        name = "Clasificacion-Controller",
        description = "Operaciones relacionadas con las clasificaciones de contenido/edad."
)
@Validated
public class ClasificacionController {

    @Autowired
    private ClasificacionService clasificacionService;

    // ================== LISTAR ==================

    @GetMapping
    @Operation(
            summary = "Listar todas las clasificaciones.",
            description = "Obtiene una lista con todas las clasificaciones registradas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de clasificaciones obtenida exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClasificacionModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay clasificaciones registradas.")
    })
    public ResponseEntity<List<ClasificacionModel>> listar() {
        List<ClasificacionModel> lista = clasificacionService.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    // ================== OBTENER POR ID ==================

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener clasificación por ID.",
            description = "Devuelve una clasificación específica según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificación encontrada.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClasificacionModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Clasificación no encontrada.")
    })
    public ResponseEntity<ClasificacionModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clasificacionService.findById(id));
    }

    // ================== CREAR ==================

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Crear una nueva clasificación.",
            description = "Registra una nueva clasificación de contenido/edad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Clasificación creada exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClasificacionModel.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear la clasificación.")
    })
    public ResponseEntity<ClasificacionModel> crear(
            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Clasificación básica",
                                            description = "Ejemplo de creación de clasificación.",
                                            value = """
                                                    {
                                                      "nombre": "T",
                                                      "descripcion": "Contenido apto para adolescentes.",
                                                      "activo": true
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody ClasificacionModel clasificacion
    ) {
        ClasificacionModel nueva = clasificacionService.save(clasificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // ================== ACTUALIZAR (PUT) ==================

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Actualizar una clasificación.",
            description = "Reemplaza completamente una clasificación existente con los datos enviados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificación actualizada exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClasificacionModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Clasificación no encontrada.")
    })
    public ResponseEntity<ClasificacionModel> actualizar(
            @PathVariable Long id,
            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "PUT ejemplo",
                                            description = "Ejemplo de actualización completa.",
                                            value = """
                                                    {
                                                      "nombre": "M",
                                                      "descripcion": "Solo para adultos.",
                                                      "activo": true
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody ClasificacionModel detalles
    ) {
        ClasificacionModel actualizada = clasificacionService.update(id, detalles);
        return ResponseEntity.ok(actualizada);
    }

    // ================== ACTUALIZAR PARCIAL (PATCH) ==================

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Actualizar parcialmente una clasificación.",
            description = "Modifica uno o más campos de la clasificación indicada (PATCH)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificación actualizada parcialmente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClasificacionModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Clasificación no encontrada.")
    })
    public ResponseEntity<ClasificacionModel> actualizarParcial(
            @PathVariable Long id,
            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "PATCH ejemplo",
                                            description = "Ejemplo de actualización parcial.",
                                            value = """
                                                    {
                                                      "descripcion": "Actualizada: contenido solo para adultos.",
                                                      "activo": false
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody Map<String, Object> campos
    ) {
        ClasificacionModel actualizada = clasificacionService.patch(id, campos);
        return ResponseEntity.ok(actualizada);
    }

    // ================== ELIMINAR ==================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una clasificación.",
            description = "Elimina una clasificación por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Clasificación eliminada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Clasificación no encontrada.")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clasificacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ================== BÚSQUEDAS ==================

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar clasificaciones por nombre (contiene).",
            description = "Devuelve clasificaciones cuyo nombre contenga el texto indicado (búsqueda case-insensitive)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificaciones encontradas.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClasificacionModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron clasificaciones para ese criterio.")
    })
    public ResponseEntity<List<ClasificacionModel>> buscarPorNombre(
            @RequestParam("nombre") String nombre) {

        List<ClasificacionModel> lista = clasificacionService.findByNombreContainingIgnoreCase(nombre);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/nombre-exacto")
    @Operation(
            summary = "Buscar clasificación por nombre exacto (ignore case).",
            description = "Devuelve una clasificación cuyo nombre coincida exactamente (sin considerar mayúsculas/minúsculas)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificación encontrada.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClasificacionModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Clasificación no encontrada.")
    })
    public ResponseEntity<ClasificacionModel> buscarPorNombreExacto(
            @RequestParam("nombre") String nombre) {

        ClasificacionModel clasificacion = clasificacionService.findByNombreExactIgnoreCase(nombre);
        return ResponseEntity.ok(clasificacion);
    }

    @GetMapping("/activas")
    @Operation(
            summary = "Listar clasificaciones activas.",
            description = "Devuelve solo las clasificaciones marcadas como activas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificaciones activas encontradas.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClasificacionModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay clasificaciones activas.")
    })
    public ResponseEntity<List<ClasificacionModel>> listarActivas() {
        List<ClasificacionModel> lista = clasificacionService.findActivas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/inactivas")
    @Operation(
            summary = "Listar clasificaciones inactivas.",
            description = "Devuelve solo las clasificaciones marcadas como inactivas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificaciones inactivas encontradas.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClasificacionModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay clasificaciones inactivas.")
    })
    public ResponseEntity<List<ClasificacionModel>> listarInactivas() {
        List<ClasificacionModel> lista = clasificacionService.findInactivas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    // ================== RESUMEN ==================

    @GetMapping("/resumen")
    @Operation(
            summary = "Obtener resumen de clasificaciones.",
            description = "Devuelve un resumen con ID, Nombre, Descripción y estado activo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumen obtenido exitosamente.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "204", description = "No hay clasificaciones para mostrar en el resumen.")
    })
    public ResponseEntity<List<Map<String, Object>>> obtenerResumen() {
        List<Map<String, Object>> resumen = clasificacionService.obtenerClasificacionesConDatos();
        if (resumen.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumen);
    }
}