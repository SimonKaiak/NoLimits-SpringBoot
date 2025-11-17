package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.ClasificacionModelAssembler;
import com.example.NoLimits.Multimedia.model.ClasificacionModel;
import com.example.NoLimits.Multimedia.service.ClasificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/clasificaciones", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(
        name = "Clasificacion-Controller-V2",
        description = "Operaciones HATEOAS relacionadas con las clasificaciones de contenido/edad."
)
@Validated
public class ClasificacionControllerV2 {

    @Autowired
    private ClasificacionService clasificacionService;

    @Autowired
    private ClasificacionModelAssembler clasificacionAssembler;

    // ================== GET ALL ==================

    @Operation(
            summary = "Obtener todas las clasificaciones (HATEOAS)",
            description = "Devuelve una lista de todas las clasificaciones con enlaces HATEOAS."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Clasificaciones obtenidas exitosamente.",
            content = @Content(
                    mediaType = "application/hal+json",
                    schema = @Schema(implementation = ClasificacionModel.class)
            )
    )
    @ApiResponse(
            responseCode = "204",
            description = "No hay clasificaciones registradas."
    )
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClasificacionModel>>> getAll() {
        List<EntityModel<ClasificacionModel>> lista = clasificacionService.findAll().stream()
                .map(clasificacionAssembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        lista,
                        linkTo(methodOn(ClasificacionControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    // ================== GET BY ID ==================

    @Operation(
            summary = "Obtener una clasificación por ID (HATEOAS)",
            description = "Devuelve una clasificación específica con enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificación encontrada.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = ClasificacionModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Clasificación no encontrada."
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClasificacionModel>> getById(@PathVariable Long id) {
        try {
            ClasificacionModel clasificacion = clasificacionService.findById(id);
            return ResponseEntity.ok(clasificacionAssembler.toModel(clasificacion));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== CREATE ==================

    @Operation(
            summary = "Crear una nueva clasificación (HATEOAS)",
            description = "Crea una nueva clasificación y devuelve el recurso con enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Clasificación creada correctamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = ClasificacionModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud."
            )
    })
    @PostMapping(consumes = "application/json", produces = "application/hal+json")
    public ResponseEntity<EntityModel<ClasificacionModel>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
            @Valid @RequestBody ClasificacionModel clasificacion
    ) {
        ClasificacionModel nueva = clasificacionService.save(clasificacion);
        EntityModel<ClasificacionModel> entityModel = clasificacionAssembler.toModel(nueva);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // ================== UPDATE (PUT) ==================

    @Operation(
            summary = "Actualizar una clasificación (PUT - HATEOAS)",
            description = "Reemplaza completamente una clasificación existente por los datos enviados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificación actualizada correctamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = ClasificacionModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Clasificación no encontrada."
            )
    })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/hal+json")
    public ResponseEntity<EntityModel<ClasificacionModel>> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
            @Valid @RequestBody ClasificacionModel detalles
    ) {
        try {
            ClasificacionModel actualizada = clasificacionService.update(id, detalles);
            return ResponseEntity.ok(clasificacionAssembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== PATCH (parcial) ==================

    @Operation(
            summary = "Actualizar parcialmente una clasificación (PATCH - HATEOAS)",
            description = "Modifica campos específicos de una clasificación existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clasificación actualizada correctamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = ClasificacionModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Clasificación no encontrada."
            )
    })
    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/hal+json")
    public ResponseEntity<EntityModel<ClasificacionModel>> patch(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
            @RequestBody ClasificacionModel detalles
    ) {
        try {
            // Para simplificar, usamos la misma lógica de update (update ya maneja campos no nulos).
            ClasificacionModel actualizada = clasificacionService.update(id, detalles);
            return ResponseEntity.ok(clasificacionAssembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== DELETE ==================

    @Operation(
            summary = "Eliminar una clasificación (HATEOAS)",
            description = "Elimina una clasificación por su ID si existe."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Clasificación eliminada correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Clasificación no encontrada."
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            clasificacionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}