package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.PlataformaModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.request.PlataformaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.PlataformaUpdateDTO;
import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.service.catalogos.PlataformaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;

import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping(
        value = "/api/v2/plataformas",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "Plataforma-Controller-V2", description = "CRUD de plataformas con HATEOAS.")
public class PlataformaControllerV2 {

    @Autowired
    private PlataformaService service;

    @Autowired
    private PlataformaModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar plataformas (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<PlataformaResponseDTO>>> findAll() {
        var lista = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener plataforma por ID (HATEOAS)")
    public ResponseEntity<EntityModel<PlataformaResponseDTO>> findById(@PathVariable Long id) {
        try {
            PlataformaResponseDTO dto = service.findById(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear plataforma (HATEOAS)")
    public ResponseEntity<EntityModel<PlataformaResponseDTO>> save(
            @RequestBody PlataformaRequestDTO body
    ) {
        PlataformaResponseDTO creada = service.save(body);
        EntityModel<PlataformaResponseDTO> entityModel = assembler.toModel(creada);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar plataforma (HATEOAS)")
    public ResponseEntity<EntityModel<PlataformaResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody PlataformaUpdateDTO body
    ) {
        try {
            PlataformaResponseDTO actualizada = service.update(id, body);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Patch parcial de plataforma (HATEOAS)")
    public ResponseEntity<EntityModel<PlataformaResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody PlataformaUpdateDTO body
    ) {
        try {
            PlataformaResponseDTO actualizada = service.patch(id, body);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar plataforma (HATEOAS)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}