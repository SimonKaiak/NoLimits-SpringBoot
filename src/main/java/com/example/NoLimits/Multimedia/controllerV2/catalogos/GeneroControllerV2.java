package com.example.NoLimits.Multimedia.controllerV2.catalogos;

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
import com.example.NoLimits.Multimedia.assemblers.catalogos.GeneroModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.request.GeneroRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GeneroResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.GeneroUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.GeneroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/generos", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Genero-Controller-V2", description = "Catálogo de géneros (HATEOAS).")
public class GeneroControllerV2 {

    @Autowired
    private GeneroService generoService;

    @Autowired
    private GeneroModelAssembler generoAssembler;

    // =========== GET ALL ===========
    @GetMapping
    @Operation(summary = "Obtener todos los géneros (HATEOAS)")
    @ApiResponse(responseCode = "200", description = "Lista de géneros obtenida.",
        content = @Content(mediaType = "application/hal+json",
            schema = @Schema(implementation = GeneroResponseDTO.class)))
    public ResponseEntity<CollectionModel<EntityModel<GeneroResponseDTO>>> getAll() {
        var generos = generoService.findAll().stream()
                .map(generoAssembler::toModel)
                .collect(Collectors.toList());

        if (generos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        generos,
                        linkTo(methodOn(GeneroControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    // =========== GET BY ID ===========
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un género por ID (HATEOAS)")
    @ApiResponse(responseCode = "200", description = "Género encontrado.",
        content = @Content(mediaType = "application/hal+json",
            schema = @Schema(implementation = GeneroResponseDTO.class)))
    public ResponseEntity<EntityModel<GeneroResponseDTO>> getById(@PathVariable Long id) {
        try {
            GeneroResponseDTO genero = generoService.findById(id);
            return ResponseEntity.ok(generoAssembler.toModel(genero));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========== CREATE ===========
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Crear un género (HATEOAS)",
        description = "Crea un nuevo género. Solo requiere el nombre.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GeneroRequestDTO.class),
                examples = @ExampleObject(
                    name = "POST género",
                    value = """
                    {
                      "nombre": "Acción"
                    }
                    """
                )
            )
        )
    )
    @ApiResponse(responseCode = "201", description = "Género creado.")
    public ResponseEntity<EntityModel<GeneroResponseDTO>> create(
            @Valid @RequestBody GeneroRequestDTO body) {

        GeneroResponseDTO nuevo = generoService.save(body);
        EntityModel<GeneroResponseDTO> entityModel = generoAssembler.toModel(nuevo);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // =========== PUT ===========
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Actualizar un género (PUT - HATEOAS)",
        description = "Reemplaza completamente el nombre del género.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "PUT sin id en body",
                        value = """
                        {
                          "nombre": "Aventura"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "PUT con id (debe coincidir con path)",
                        value = """
                        {
                          "id": 1,
                          "nombre": "Aventura"
                        }
                        """
                    )
                }
            )
        )
    )
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody GeneroUpdateDTO detalles) {
        if (detalles.getNombre() == null) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", "PUT requiere al menos el campo 'nombre'.")
            );
        }
        try {
            GeneroResponseDTO actualizado = generoService.update(id, detalles);
            return ResponseEntity.ok(generoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========== PATCH ===========
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Actualizar parcialmente un género (PATCH - HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "PATCH ejemplo",
                    value = """
                    { "nombre": "Terror psicológico" }
                    """
                )
            )
        )
    )
    public ResponseEntity<EntityModel<GeneroResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody GeneroUpdateDTO detalles) {
        try {
            GeneroResponseDTO actualizado = generoService.patch(id, detalles);
            return ResponseEntity.ok(generoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========== DELETE ===========
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un género (HATEOAS)")
    @ApiResponse(responseCode = "204", description = "Género eliminado.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            generoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}