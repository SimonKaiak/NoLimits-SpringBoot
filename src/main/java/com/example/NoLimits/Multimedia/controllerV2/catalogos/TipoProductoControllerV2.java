// Ruta: src/main/java/com/example/NoLimits/Multimedia/controllerV2/TipoProductoControllerV2.java
package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.TipoProductoModelAssembler;
import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import com.example.NoLimits.Multimedia.service.catalogos.TipoProductoService;

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
import org.springframework.http.MediaType;
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

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/tipos-producto", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(
        name = "TipoProducto-Controller-V2",
        description = "Operaciones relacionadas con los tipos de productos (HATEOAS)."
)
public class TipoProductoControllerV2 {

    @Autowired
    private TipoProductoService tipoProductoService;

    @Autowired
    private TipoProductoModelAssembler tipoProductoAssembler;

    // ================== GET ALL ==================

    @GetMapping
    @Operation(
            summary = "Obtener todos los tipos de producto (HATEOAS)",
            description = "Devuelve una lista de todos los tipos de producto con enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipos de producto obtenidos exitosamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay tipos de producto registrados.")
    })
    public ResponseEntity<CollectionModel<EntityModel<TipoProductoModel>>> getAll() {
        var tipos = tipoProductoService.findAll().stream()
                .map(tipoProductoAssembler::toModel)
                .collect(Collectors.toList());

        if (tipos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        tipos,
                        linkTo(methodOn(TipoProductoControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    // ================== GET BY ID ==================

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un tipo de producto por ID (HATEOAS)",
            description = "Devuelve un tipo de producto específico con enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de producto encontrado.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado.")
    })
    public ResponseEntity<EntityModel<TipoProductoModel>> getById(@PathVariable Long id) {
        try {
            var tipo = tipoProductoService.findById(id);
            return ResponseEntity.ok(tipoProductoAssembler.toModel(tipo));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== CREATE ==================

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Crear un tipo de producto (HATEOAS)",
            description = "Crea un nuevo tipo de producto y devuelve el recurso con enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tipo de producto creado correctamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud.")
    })
    public ResponseEntity<EntityModel<TipoProductoModel>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoProductoModel.class),
                            examples = {
                                    @ExampleObject(
                                            name = "POST completo",
                                            description = "Ejemplo de creación de tipo de producto.",
                                            value = """
                                                    {
                                                      "nombre": "Accesorio",
                                                      "descripcion": "Categoría general para accesorios",
                                                      "activo": true
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @RequestBody TipoProductoModel body
    ) {
        body.setId(null); // ignorar id si lo envían
        var nuevo = tipoProductoService.save(body);
        var entity = tipoProductoAssembler.toModel(nuevo);

        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    // ================== UPDATE (PUT) ==================

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Actualizar un tipo de producto (PUT - HATEOAS)",
            description = "Reemplaza completamente un tipo de producto existente por los datos enviados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de producto actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos incompletos para un PUT."),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado.")
    })
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "PUT completo (sin id en body)",
                                            value = """
                                                    {
                                                      "nombre": "Periféricos",
                                                      "descripcion": "Categoría general para clasificar productos",
                                                      "activo": true
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "PUT completo (con id, debe coincidir con path)",
                                            value = """
                                                    {
                                                      "id": 1,
                                                      "nombre": "Periféricos",
                                                      "descripcion": "Categoría general para clasificar productos",
                                                      "activo": true
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @RequestBody TipoProductoModel detalles
    ) {
        // Guardia mínima para que PUT sea “reemplazo completo”
        if (detalles.getNombre() == null || detalles.getDescripcion() == null || detalles.getActivo() == null) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", "PUT requiere nombre, descripcion y activo.")
            );
        }

        try {
            var actualizado = tipoProductoService.update(id, detalles);
            return ResponseEntity.ok(tipoProductoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== PATCH (PARCIAL) ==================

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Actualizar parcialmente un tipo de producto (PATCH - HATEOAS)",
            description = "Modifica uno o más campos de un tipo de producto existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de producto actualizado parcialmente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = TipoProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado.")
    })
    public ResponseEntity<EntityModel<TipoProductoModel>> patch(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "PATCH ejemplo",
                                    value = """
                                            { "nombre": "Audio" }
                                            """
                            )
                    )
            )
            @RequestBody TipoProductoModel detalles
    ) {
        try {
            var actualizado = tipoProductoService.patch(id, detalles);
            return ResponseEntity.ok(tipoProductoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== DELETE ==================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un tipo de producto (HATEOAS)",
            description = "Elimina un tipo de producto por su ID si existe y no tiene productos asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de producto eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado."),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar: existen productos asociados.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            tipoProductoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}