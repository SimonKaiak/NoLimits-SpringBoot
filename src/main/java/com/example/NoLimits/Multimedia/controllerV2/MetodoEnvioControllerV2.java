// Ruta: src/main/java/com/example/NoLimits/Multimedia/controllerV2/MetodoEnvioControllerV2.java
package com.example.NoLimits.Multimedia.controllerV2;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.MetodoEnvioModelAssembler;
import com.example.NoLimits.Multimedia.model.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.service.MetodoEnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/metodos-envio", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "MetodoEnvio-Controller-V2", description = "Operaciones relacionadas con los métodos de envío (HATEOAS).")
public class MetodoEnvioControllerV2 {

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @Autowired
    private MetodoEnvioModelAssembler metodoEnvioAssembler;

    @GetMapping
    @Operation(summary = "Listar métodos de envío (HATEOAS)")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(mediaType = "application/hal+json",
            schema = @Schema(implementation = MetodoEnvioModel.class)))
    @ApiResponse(responseCode = "204", description = "Sin contenido")
    public ResponseEntity<CollectionModel<EntityModel<MetodoEnvioModel>>> getAll() {
        List<EntityModel<MetodoEnvioModel>> metodos = metodoEnvioService.findAll().stream()
            .map(metodoEnvioAssembler::toModel)
            .collect(Collectors.toList());

        if (metodos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
            CollectionModel.of(metodos,
                linkTo(methodOn(MetodoEnvioControllerV2.class).getAll()).withSelfRel())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener método de envío por ID (HATEOAS)")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(mediaType = "application/hal+json",
            schema = @Schema(implementation = MetodoEnvioModel.class)))
    @ApiResponse(responseCode = "404", description = "No encontrado")
    public ResponseEntity<EntityModel<MetodoEnvioModel>> getById(@PathVariable Long id) {
        try {
            MetodoEnvioModel metodo = metodoEnvioService.findById(id);
            return ResponseEntity.ok(metodoEnvioAssembler.toModel(metodo));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Crear método de envío (HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "POST mínimo",
                    value = """
                    {
                      "nombre": "Retiro en tienda",
                      "descripcion": "Retiro presencial en sucursal Plaza Oeste"
                    }
                    """
                )
            )
        )
    )
    @ApiResponse(responseCode = "201", description = "Creado")
    public ResponseEntity<EntityModel<MetodoEnvioModel>> create(@RequestBody MetodoEnvioModel body) {
        MetodoEnvioModel nuevo = metodoEnvioService.save(body);
        EntityModel<MetodoEnvioModel> entityModel = metodoEnvioAssembler.toModel(nuevo);
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar método de envío (PUT - HATEOAS)")
    public ResponseEntity<EntityModel<MetodoEnvioModel>> update(
            @PathVariable Long id,
            @RequestBody MetodoEnvioModel body) {
        try {
            MetodoEnvioModel actualizado = metodoEnvioService.update(id, body);
            return ResponseEntity.ok(metodoEnvioAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Actualizar parcialmente método de envío (PATCH - HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "PATCH ejemplo",
                    value = """
                    {
                      "descripcion": "Actualización de descripción"
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<EntityModel<MetodoEnvioModel>> patch(
            @PathVariable Long id,
            @RequestBody MetodoEnvioModel parcial) {
        try {
            MetodoEnvioModel actualizado = metodoEnvioService.patch(id, parcial);
            return ResponseEntity.ok(metodoEnvioAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar método de envío (HATEOAS)")
    @ApiResponse(responseCode = "204", description = "Eliminado")
    @ApiResponse(responseCode = "404", description = "No encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            metodoEnvioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}