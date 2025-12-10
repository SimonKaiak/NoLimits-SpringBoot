package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import java.util.List;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.EstadoModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.request.EstadoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EstadoUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.EstadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value = "/api/v2/estados", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Estado-Controller-V2", description = "Gestión de estados con HATEOAS y DTOs.")
public class EstadoControllerV2 {

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private EstadoModelAssembler assembler;

    // ========= GET ALL =========
    @GetMapping
    @Operation(summary = "Listar estados (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<EstadoResponseDTO>>> getAll() {

        List<EntityModel<EstadoResponseDTO>> lista = estadoService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        lista,
                        linkTo(methodOn(EstadoControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    // ========= GET BY ID =========
    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado por ID (HATEOAS)")
    public ResponseEntity<EntityModel<EstadoResponseDTO>> getById(@PathVariable Long id) {
        try {
            EstadoResponseDTO dto = estadoService.findById(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========= CREATE =========
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear estado (HATEOAS)")
    public ResponseEntity<EntityModel<EstadoResponseDTO>> create(
            @Valid @RequestBody EstadoRequestDTO body) {

        EstadoResponseDTO creado = estadoService.save(body);
        EntityModel<EstadoResponseDTO> entity = assembler.toModel(creado);

        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    // ========= PUT =========
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar estado completo (PUT - HATEOAS)")
    public ResponseEntity<EntityModel<EstadoResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO body) {

        try {
            EstadoResponseDTO actualizado = estadoService.update(id, body);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========= PATCH =========
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente estado (PATCH - HATEOAS)")
    public ResponseEntity<EntityModel<EstadoResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody EstadoUpdateDTO body) {

        try {
            EstadoResponseDTO actualizado = estadoService.patch(id, body);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========= DELETE =========
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estado (DELETE - HATEOAS)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            estadoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}