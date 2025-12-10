package com.example.NoLimits.Multimedia.controllerV2.catalogos;

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

import com.example.NoLimits.Multimedia.assemblers.catalogos.EmpresasModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresasResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresasUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresasService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v2/productos/{productoId}/empresas",
                produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Empresas-Controller-V2")
public class EmpresasControllerV2 {

    @Autowired private EmpresasService service;
    @Autowired private EmpresasModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EmpresasResponseDTO>>> listar(
            @PathVariable Long productoId) {

        var lista = service.findByProducto(productoId).stream()
                .map(assembler::toModel)
                .toList();

        return lista.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(CollectionModel.of(lista));
    }

    @PostMapping("/{empresaId}")
    public ResponseEntity<EntityModel<EmpresasResponseDTO>> link(
            @PathVariable Long productoId,
            @PathVariable Long empresaId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(service.link(productoId, empresaId)));
    }

    @PatchMapping("/{relacionId}")
    public ResponseEntity<EntityModel<EmpresasResponseDTO>> patch(
            @PathVariable Long relacionId,
            @RequestBody EmpresasUpdateDTO dto) {

        return ResponseEntity.ok(assembler.toModel(service.patch(relacionId, dto)));
    }

    @DeleteMapping("/{empresaId}")
    public ResponseEntity<Void> unlink(
            @PathVariable Long productoId,
            @PathVariable Long empresaId) {

        service.unlink(productoId, empresaId);
        return ResponseEntity.noContent().build();
    }
}