package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.EstadoModelAssembler;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.service.catalogos.EstadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/estados", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Estado-Controller-V2", description = "Gestión de estados con HATEOAS.")
public class EstadoControllerV2 {

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private EstadoModelAssembler estadoAssembler;

    // ========= GET ALL =========
    @GetMapping
    @Operation(summary = "Listar todos los estados (HATEOAS)")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estados encontrados.",
            content = @Content(
                mediaType = "application/hal+json",
                array = @ArraySchema(schema = @Schema(implementation = EstadoModel.class))
            )
        ),
        @ApiResponse(responseCode = "204", description = "No hay estados registrados.")
    })
    public ResponseEntity<CollectionModel<EntityModel<EstadoModel>>> getAll() {
        var modelos = estadoService.findAll().stream()
                .map(estadoAssembler::toModel)
                .collect(Collectors.toList());

        if (modelos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        modelos,
                        linkTo(methodOn(EstadoControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    // ========= GET BY ID =========
    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado por ID (HATEOAS)")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estado encontrado.",
            content = @Content(
                mediaType = "application/hal+json",
                schema = @Schema(implementation = EstadoModel.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado.")
    })
    public ResponseEntity<EntityModel<EstadoModel>> getById(@PathVariable Long id) {
        try {
            var estado = estadoService.findById(id);
            return ResponseEntity.ok(estadoAssembler.toModel(estado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========= BUSCAR POR NOMBRE (LIKE) =========
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar estados por nombre (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<EstadoModel>>> buscarPorNombre(@PathVariable String nombre) {
        var estados = estadoService.findByNombreLike(nombre).stream()
                .map(estadoAssembler::toModel)
                .collect(Collectors.toList());

        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        estados,
                        linkTo(methodOn(EstadoControllerV2.class).buscarPorNombre(nombre)).withSelfRel()
                )
        );
    }

    // ========= ACTIVOS =========
    @GetMapping("/activos")
    @Operation(summary = "Listar estados activos (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<EstadoModel>>> listarActivos() {
        var activos = estadoService.findActivos().stream()
                .map(estadoAssembler::toModel)
                .collect(Collectors.toList());

        if (activos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        activos,
                        linkTo(methodOn(EstadoControllerV2.class).listarActivos()).withSelfRel()
                )
        );
    }

    // ========= INACTIVOS =========
    @GetMapping("/inactivos")
    @Operation(summary = "Listar estados inactivos (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<EstadoModel>>> listarInactivos() {
        var inactivos = estadoService.findInactivos().stream()
                .map(estadoAssembler::toModel)
                .collect(Collectors.toList());

        if (inactivos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        inactivos,
                        linkTo(methodOn(EstadoControllerV2.class).listarInactivos()).withSelfRel()
                )
        );
    }

    // ========= RESUMEN =========
    @GetMapping(value = "/resumen", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Resumen de estados (tabla simple, sin HATEOAS)")
    public ResponseEntity<List<Map<String, Object>>> obtenerResumen() {
        var resumen = estadoService.obtenerEstadosResumen();
        if (resumen.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumen);
    }

    // ========= CREATE =========
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Crear un estado (HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EstadoModel.class),
                examples = @ExampleObject(
                    name = "Estado básico",
                    value = """
                    {
                      "nombre": "Activo",
                      "descripcion": "Producto disponible para su compra",
                      "activo": true
                    }
                    """
                )
            )
        )
    )
    @ApiResponse(responseCode = "201", description = "Estado creado correctamente.")
    public ResponseEntity<EntityModel<EstadoModel>> create(@Valid @RequestBody EstadoModel body) {
        body.setId(null); // ignorar id si lo mandan
        var creado = estadoService.save(body);
        var entity = estadoAssembler.toModel(creado);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    // ========= PUT =========
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Actualizar un estado (PUT - HATEOAS)",
        description = "Reemplaza los datos de un estado existente."
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody EstadoModel body) {

        // Sugerencia mínima: que al menos venga nombre
        if (body.getNombre() == null || body.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "PUT requiere al menos un nombre de estado no vacío.")
            );
        }

        try {
            var actualizado = estadoService.update(id, body);
            return ResponseEntity.ok(estadoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========= PATCH =========
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Actualizar parcialmente un estado (PATCH - HATEOAS)",
        description = "Modifica solo los campos enviados."
    )
    public ResponseEntity<EntityModel<EstadoModel>> patch(
            @PathVariable Long id,
            @RequestBody EstadoModel body) {
        try {
            var actualizado = estadoService.patch(id, body);
            return ResponseEntity.ok(estadoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========= DELETE =========
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un estado (HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Estado eliminado correctamente."),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            estadoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}