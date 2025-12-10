package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.TipoDeDesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoDeDesarrolladorRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoDeDesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.TipoDeDesarrolladorService;

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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Ruta: src/main/java/com/example/NoLimits/Multimedia/controllerV2/catalogos/TipoDeDesarrolladorControllerV2.java
@RestController
@RequestMapping(
        value = "/api/v2/tipos-desarrollador",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "TipoDeDesarrollador-Controller-V2", description = "Catálogo de tipos de desarrollador con HATEOAS.")
public class TipoDeDesarrolladorControllerV2 {

    @Autowired
    private TipoDeDesarrolladorService service;

    @Autowired
    private TipoDeDesarrolladorModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar tipos de desarrollador (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<TipoDeDesarrolladorResponseDTO>>> getAll() {

        List<EntityModel<TipoDeDesarrolladorResponseDTO>> lista = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        CollectionModel<EntityModel<TipoDeDesarrolladorResponseDTO>> collection =
                CollectionModel.of(
                        lista,
                        linkTo(methodOn(TipoDeDesarrolladorControllerV2.class).getAll()).withSelfRel()
                );

        return ResponseEntity.ok(collection);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo de desarrollador por ID (HATEOAS)")
    public ResponseEntity<EntityModel<TipoDeDesarrolladorResponseDTO>> getById(@PathVariable Long id) {
        try {
            TipoDeDesarrolladorResponseDTO tipo = service.findById(id);
            return ResponseEntity.ok(assembler.toModel(tipo));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear tipo de desarrollador (HATEOAS)")
    public ResponseEntity<EntityModel<TipoDeDesarrolladorResponseDTO>> create(
            @Valid @RequestBody TipoDeDesarrolladorRequestDTO body
    ) {
        TipoDeDesarrolladorResponseDTO creado = service.save(body);
        EntityModel<TipoDeDesarrolladorResponseDTO> entity = assembler.toModel(creado);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar tipo de desarrollador (PUT - HATEOAS)")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody TipoDeDesarrolladorUpdateDTO body
    ) {
        try {
            TipoDeDesarrolladorResponseDTO actualizado = service.update(id, body);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", ex.getMessage())
            );
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente tipo de desarrollador (PATCH - HATEOAS)")
    public ResponseEntity<?> patch(
            @PathVariable Long id,
            @RequestBody TipoDeDesarrolladorUpdateDTO body
    ) {
        try {
            TipoDeDesarrolladorResponseDTO actualizado = service.patch(id, body);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", ex.getMessage())
            );
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de desarrollador (HATEOAS)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            // Hay relaciones en tipos_de_desarrollador
            return ResponseEntity.badRequest().build();
        }
    }
}