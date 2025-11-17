// Ruta: src/main/java/com/example/NoLimits/Multimedia/controllerV2/TipoEmpresaControllerV2.java
package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia.assemblers.TipoEmpresaModelAssembler;
import com.example.NoLimits.Multimedia.model.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.service.TipoEmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping(
        value = "/api/v2/tipos-empresa",
        produces = MediaTypes.HAL_JSON_VALUE
)
@Tag(name = "TipoEmpresa-Controller-V2", description = "CRUD de tipos de empresa con HATEOAS.")
public class TipoEmpresaControllerV2 {

    @Autowired
    private TipoEmpresaService service;

    @Autowired
    private TipoEmpresaModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar tipos de empresa (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<TipoEmpresaModel>>> findAll() {
        var lista = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo de empresa por ID (HATEOAS)")
    public EntityModel<TipoEmpresaModel> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear tipo de empresa (HATEOAS)")
    public ResponseEntity<EntityModel<TipoEmpresaModel>> save(@RequestBody TipoEmpresaModel tipo) {
        var creado = service.save(tipo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(creado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de empresa (HATEOAS)")
    public EntityModel<TipoEmpresaModel> update(
            @PathVariable Long id,
            @RequestBody TipoEmpresaModel tipo
    ) {
        return assembler.toModel(service.update(id, tipo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de empresa (HATEOAS)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}