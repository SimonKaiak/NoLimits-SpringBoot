package com.example.NoLimits.Multimedia.controllerV2.usuario;

import java.util.List;
import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.usuario.RolModelAssembler;
import com.example.NoLimits.Multimedia.dto.usuario.request.RolRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.RolUpdateDTO;
import com.example.NoLimits.Multimedia.service.usuario.RolService;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controlador V2 de roles con soporte HATEOAS.
 *
 * - Usa DTOs (request / response / update) en vez de exponer RolModel.
 * - Responde en formato HAL+JSON usando EntityModel y CollectionModel.
 */
@RestController
@RequestMapping(value = "/api/v2/roles", produces = MediaTypes.HAL_JSON_VALUE)
@Validated
@Tag(name = "Roles", description = "Gestión de roles (V2 HATEOAS)")
public class RolControllerV2 {

    @Autowired
    private RolService rolService;

    @Autowired
    private RolModelAssembler rolAssembler;

    /**
     * GET /api/v2/roles
     *
     * Lista todos los roles en formato HAL.
     */
    @GetMapping
    @Operation(summary = "Listar todos los roles")
    public ResponseEntity<CollectionModel<EntityModel<RolResponseDTO>>> getAll() {
        List<EntityModel<RolResponseDTO>> roles = rolService.findAll()
                .stream()
                .map(rolAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<RolResponseDTO>> body = CollectionModel.of(
                roles,
                linkTo(methodOn(RolControllerV2.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(body);
    }

    /**
     * GET /api/v2/roles/{id}
     *
     * Obtiene un rol por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un rol por ID")
    public ResponseEntity<EntityModel<RolResponseDTO>> getById(@PathVariable Long id) {
        try {
            RolResponseDTO rol = rolService.findById(id);
            return ResponseEntity.ok(rolAssembler.toModel(rol));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/v2/roles
     *
     * Crea un nuevo rol a partir de RolRequestDTO.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear un nuevo rol")
    public ResponseEntity<EntityModel<RolResponseDTO>> create(
            @Valid @RequestBody RolRequestDTO dto) {

        RolResponseDTO creado = rolService.save(dto);
        EntityModel<RolResponseDTO> entityModel = rolAssembler.toModel(creado);

        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * PUT /api/v2/roles/{id}
     *
     * Actualización completa de un rol usando RolUpdateDTO.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar completamente un rol")
    public ResponseEntity<EntityModel<RolResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody RolUpdateDTO dto) {

        RolResponseDTO actualizado = rolService.update(id, dto);
        EntityModel<RolResponseDTO> entityModel = rolAssembler.toModel(actualizado);

        return ResponseEntity.ok(entityModel);
    }

    /**
     * PATCH /api/v2/roles/{id}
     *
     * Actualización parcial de un rol.
     * Solo se aplican los campos no nulos del RolUpdateDTO.
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente un rol (PATCH)")
    public ResponseEntity<EntityModel<RolResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody RolUpdateDTO dto) {

        RolResponseDTO actualizado = rolService.patch(id, dto);
        EntityModel<RolResponseDTO> entityModel = rolAssembler.toModel(actualizado);

        return ResponseEntity.ok(entityModel);
    }

    /**
     * DELETE /api/v2/roles/{id}
     *
     * Elimina un rol por ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un rol por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}