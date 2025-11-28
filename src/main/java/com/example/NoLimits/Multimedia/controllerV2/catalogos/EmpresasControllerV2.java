package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.EmpresasModelAssembler;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresasModel;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(
        value = "/api/v2/productos/{productoId}/empresas",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "Empresas-Controller-V2", description = "Relación Producto ↔ Empresa con HATEOAS.")
public class EmpresasControllerV2 {

    @Autowired
    private EmpresasService service;

    @Autowired
    private EmpresasModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar empresas asociadas a un producto (TP - HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<EmpresasModel>>> listar(
            @PathVariable Long productoId
    ) {
        var lista = service.findByProducto(productoId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @PostMapping("/{empresaId}")
    @Operation(summary = "Vincular Producto ↔ Empresa (HATEOAS)")
    public ResponseEntity<EntityModel<EmpresasModel>> link(
            @PathVariable Long productoId,
            @PathVariable Long empresaId
    ) {
        try {
            var rel = service.link(productoId, empresaId);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(assembler.toModel(rel));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/{relacionId}")
    @Operation(
            summary = "Actualizar parcialmente la relación Producto ↔ Empresa (PATCH - HATEOAS)",
            description = "Permite cambiar el producto o la empresa asociados a la relación."
    )
    public ResponseEntity<EntityModel<EmpresasModel>> patch(
            @PathVariable Long productoId,
            @PathVariable Long relacionId,
            @RequestBody EmpresasModel body
    ) {
        try {
            EmpresasModel relActualizada = service.patch(relacionId, body);

            return ResponseEntity.ok(assembler.toModel(relActualizada));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{empresaId}")
    @Operation(summary = "Desvincular Producto ↔ Empresa (HATEOAS)")
    public ResponseEntity<Void> unlink(
            @PathVariable Long productoId,
            @PathVariable Long empresaId
    ) {
        try {
            service.unlink(productoId, empresaId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}