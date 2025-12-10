package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.DesarrolladoresModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladoresRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladoresResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.DesarrolladoresUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladoresService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/api/v2/productos/{productoId}/desarrolladores",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "Desarrolladores-Controller-V2", description = "Relación Producto ↔ Desarrollador con HATEOAS.")
public class DesarrolladoresControllerV2 {

    @Autowired
    private DesarrolladoresService service;

    @Autowired
    private DesarrolladoresModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar desarrolladores asociados a un producto (TP - HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<DesarrolladoresResponseDTO>>> listar(
            @PathVariable Long productoId
    ) {
        var lista = service.findByProducto(productoId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var collection = CollectionModel.of(lista);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/{desarrolladorId}")
    @Operation(summary = "Vincular Producto ↔ Desarrollador (HATEOAS)")
    public ResponseEntity<EntityModel<DesarrolladoresResponseDTO>> link(
            @PathVariable Long productoId,
            @PathVariable Long desarrolladorId,
            @Valid @RequestBody(required = false) DesarrolladoresRequestDTO requestDTO
    ) {
        try {
            DesarrolladoresRequestDTO dto = (requestDTO != null)
                    ? requestDTO
                    : new DesarrolladoresRequestDTO();

            // Forzamos los IDs desde la ruta
            dto.setProductoId(productoId);
            dto.setDesarrolladorId(desarrolladorId);

            var rel = service.link(dto);
            return ResponseEntity.ok(assembler.toModel(rel));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/relaciones/{relacionId}")
    @Operation(summary = "Actualizar parcialmente la relación Producto ↔ Desarrollador (HATEOAS)")
    public ResponseEntity<EntityModel<DesarrolladoresResponseDTO>> patch(
            @PathVariable Long productoId,
            @PathVariable Long relacionId,
            @RequestBody DesarrolladoresUpdateDTO parciales
    ) {
        try {
            var actualizado = service.patch(relacionId, parciales);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            // p.ej. intento de duplicar una relación existente
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{desarrolladorId}")
    @Operation(summary = "Desvincular Producto ↔ Desarrollador (HATEOAS)")
    public ResponseEntity<Void> unlink(
            @PathVariable Long productoId,
            @PathVariable Long desarrolladorId
    ) {
        try {
            service.unlink(productoId, desarrolladorId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}