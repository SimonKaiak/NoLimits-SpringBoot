package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.ClasificacionModelAssembler;
// Ahora usamos DTOs en lugar de la entidad directamente.
import com.example.NoLimits.Multimedia.dto.catalogos.request.ClasificacionRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.ClasificacionResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.ClasificacionUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.ClasificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*
 Controlador HATEOAS para las clasificaciones (versión V2).

 En esta versión:
 - Las respuestas no devuelven solo el JSON del recurso.
 - También incluyen enlaces (links) para navegar a otras operaciones relacionadas.
 - Cada clasificación viene envuelta en un EntityModel con sus propios enlaces.
 - Los listados se devuelven como CollectionModel, también con enlaces propios.

 La idea es que el cliente pueda descubrir qué más puede hacer
 siguiendo los enlaces que vienen en la respuesta.
*/
@RestController
@RequestMapping(value = "/api/v2/clasificaciones", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(
        name = "Clasificacion-Controller-V2",
        description = "Operaciones HATEOAS relacionadas con las clasificaciones de contenido/edad."
)
@Validated
public class ClasificacionControllerV2 {

    // Servicio que maneja la lógica de negocio de las clasificaciones.
    @Autowired
    private ClasificacionService clasificacionService;

    // Assembler que se encarga de convertir ClasificacionResponseDTO a EntityModel<ClasificacionResponseDTO>
    // y agregar los enlaces HATEOAS correspondientes.
    @Autowired
    private ClasificacionModelAssembler clasificacionAssembler;

    // ================== GET ALL ==================

    /*
     Obtener todas las clasificaciones en formato HATEOAS.

     - Se toma la lista normal desde el servicio.
     - Cada elemento se transforma usando el assembler, para incluir enlaces.
     - Si la lista está vacía, se responde con 204 (sin contenido).
     - Si hay datos, se envuelven en un CollectionModel con un enlace "self".
    */
    @Operation(
            summary = "Obtener todas las clasificaciones (HATEOAS)",
            description = "Devuelve una lista de todas las clasificaciones con enlaces HATEOAS."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Clasificaciones obtenidas exitosamente.",
            content = @Content(
                    mediaType = "application/hal+json",
                    schema = @Schema(implementation = ClasificacionResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "204",
            description = "No hay clasificaciones registradas."
    )
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClasificacionResponseDTO>>> getAll() {
        // Transformar cada ClasificacionResponseDTO a EntityModel con enlaces.
        List<EntityModel<ClasificacionResponseDTO>> lista = clasificacionService.findAll().stream()
                .map(clasificacionAssembler::toModel)
                .collect(Collectors.toList());

        // Si no hay ninguna clasificación, se responde sin contenido.
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Si hay datos, se envuelven en un CollectionModel con un enlace "self".
        return ResponseEntity.ok(
                CollectionModel.of(
                        lista,
                        linkTo(methodOn(ClasificacionControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    // ================== GET BY ID ==================

    /*
     Obtener una clasificación específica por su ID en formato HATEOAS.

     - Busca la clasificación por ID usando el servicio.
     - Si se encuentra, se transforma con el assembler para agregar enlaces.
     - Si el servicio lanza RecursoNoEncontradoException, se responde con 404.
    */
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
                            schema = @Schema(implementation = ClasificacionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Clasificación no encontrada."
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClasificacionResponseDTO>> getById(@PathVariable Long id) {
        try {
            ClasificacionResponseDTO clasificacion = clasificacionService.findById(id);
            return ResponseEntity.ok(clasificacionAssembler.toModel(clasificacion));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== CREATE ==================

    /*
     Crear una nueva clasificación y devolverla con enlaces HATEOAS.

     - Se valida el cuerpo recibido.
     - Se guarda la nueva clasificación con el servicio.
     - Se transforma en EntityModel usando el assembler.
     - Se devuelve con código 201 y cabecera Location apuntando al enlace self.
    */
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
                            schema = @Schema(implementation = ClasificacionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud."
            )
    })
    @PostMapping(consumes = "application/json", produces = "application/hal+json")
    public ResponseEntity<EntityModel<ClasificacionResponseDTO>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClasificacionRequestDTO.class),
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
            @Valid @RequestBody ClasificacionRequestDTO clasificacionRequest
    ) {
        // Guardar la nueva clasificación en la base de datos.
        ClasificacionResponseDTO nueva = clasificacionService.create(clasificacionRequest);

        // Transformarla en EntityModel con enlaces HATEOAS.
        EntityModel<ClasificacionResponseDTO> entityModel = clasificacionAssembler.toModel(nueva);

        // Devolver 201 con Location apuntando al enlace self del recurso creado.
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // ================== UPDATE (PUT) ==================

    /*
     Actualizar completamente una clasificación usando PUT, en versión HATEOAS.

     - Se reemplazan los datos de la clasificación con el ID indicado.
     - Si no existe, se responde con 404.
     - Si se actualiza bien, se devuelve el recurso actualizado con enlaces.
    */
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
                            schema = @Schema(implementation = ClasificacionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Clasificación no encontrada."
            )
    })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/hal+json")
    public ResponseEntity<EntityModel<ClasificacionResponseDTO>> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClasificacionRequestDTO.class),
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
            @Valid @RequestBody ClasificacionRequestDTO detalles
    ) {
        try {
            ClasificacionResponseDTO actualizada = clasificacionService.update(id, detalles);
            return ResponseEntity.ok(clasificacionAssembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== PATCH (parcial) ==================

    /*
     Actualizar parcialmente una clasificación usando PATCH, en versión HATEOAS.

     - Se recibe un DTO con los campos a modificar.
     - El servicio se encarga de aplicar solo esos cambios.
     - Si la clasificación no existe, se responde con 404.
     - Si se actualiza, se devuelve el recurso con sus enlaces.
    */
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
                            schema = @Schema(implementation = ClasificacionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Clasificación no encontrada."
            )
    })
    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/hal+json")
    public ResponseEntity<EntityModel<ClasificacionResponseDTO>> patch(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClasificacionUpdateDTO.class),
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
            @RequestBody ClasificacionUpdateDTO campos
    ) {
        try {
            ClasificacionResponseDTO actualizada = clasificacionService.patch(id, campos);
            return ResponseEntity.ok(clasificacionAssembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== DELETE ==================

    /*
     Eliminar una clasificación por su ID.

     - Si la eliminación se realiza correctamente, responde con 204 (sin contenido).
     - Si la clasificación no existe, responde con 404.
    */
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