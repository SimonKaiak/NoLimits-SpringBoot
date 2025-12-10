package com.example.NoLimits.Multimedia.controllerV2.ubicacion;

import java.util.List;
import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.ubicacion.DireccionModelAssembler;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.DireccionResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.DireccionUpdateDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.DireccionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controlador V2 de direcciones con soporte HATEOAS.
 *
 * - Usa DTOs (Request / Response / Update) en lugar de exponer la entidad JPA.
 * - Responde en formato HAL+JSON usando EntityModel y CollectionModel.
 * - Delega la lógica de negocio y mapeo de entidades al DireccionService.
 */
@RestController
@RequestMapping(value = "/api/v2/direcciones", produces = MediaTypes.HAL_JSON_VALUE)
@Validated
@Tag(name = "Direcciones", description = "Gestión de direcciones (V2 HATEOAS)")
public class DireccionControllerV2 {

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private DireccionModelAssembler direccionAssembler;

    /**
     * GET /api/v2/direcciones
     *
     * Lista todas las direcciones como una colección HAL.
     */
    @GetMapping
    @Operation(summary = "Listar todas las direcciones")
    public ResponseEntity<CollectionModel<EntityModel<DireccionResponseDTO>>> getAll() {

        List<EntityModel<DireccionResponseDTO>> direcciones = direccionService.findAll()
                .stream()
                .map(direccionAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DireccionResponseDTO>> body = CollectionModel.of(
                direcciones,
                linkTo(methodOn(DireccionControllerV2.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(body);
    }

    /**
     * GET /api/v2/direcciones/{id}
     *
     * Obtiene una dirección específica por ID, envuelta en EntityModel.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una dirección por ID")
    public ResponseEntity<EntityModel<DireccionResponseDTO>> getById(@PathVariable Long id) {
        try {
            DireccionResponseDTO direccion = direccionService.findById(id);
            return ResponseEntity.ok(direccionAssembler.toModel(direccion));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/v2/direcciones
     *
     * Crea una nueva dirección a partir de un DireccionRequestDTO.
     * Devuelve la dirección creada en formato HAL y con Location apuntando al self.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear una nueva dirección")
    public ResponseEntity<EntityModel<DireccionResponseDTO>> create(
            @Valid @RequestBody DireccionRequestDTO direccionRequest) {

        DireccionResponseDTO creada = direccionService.save(direccionRequest);
        EntityModel<DireccionResponseDTO> entityModel = direccionAssembler.toModel(creada);

        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * PATCH /api/v2/direcciones/{id}
     *
     * Actualización parcial de una dirección. Solo se aplican los campos no nulos
     * que vengan en DireccionUpdateDTO.
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente una dirección (PATCH)")
    public ResponseEntity<EntityModel<DireccionResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody DireccionUpdateDTO entrada) {

        DireccionResponseDTO actualizada = direccionService.patch(id, entrada);
        EntityModel<DireccionResponseDTO> entityModel = direccionAssembler.toModel(actualizada);

        return ResponseEntity.ok(entityModel);
    }

    /**
     * DELETE /api/v2/direcciones/{id}
     *
     * Elimina una dirección por su ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una dirección por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        direccionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}