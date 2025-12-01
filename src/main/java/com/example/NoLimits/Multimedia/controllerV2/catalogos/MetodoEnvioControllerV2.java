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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.MetodoEnvioModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoEnvioRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoEnvioUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoEnvioService;

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
            schema = @Schema(implementation = MetodoEnvioResponseDTO.class)))
    @ApiResponse(responseCode = "204", description = "Sin contenido")
    public ResponseEntity<CollectionModel<EntityModel<MetodoEnvioResponseDTO>>> getAll() {
        List<EntityModel<MetodoEnvioResponseDTO>> metodos = metodoEnvioService.findAll().stream()
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
            schema = @Schema(implementation = MetodoEnvioResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "No encontrado")
    public ResponseEntity<EntityModel<MetodoEnvioResponseDTO>> getById(@PathVariable Long id) {
        try {
            MetodoEnvioResponseDTO metodo = metodoEnvioService.findById(id);
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
    public ResponseEntity<EntityModel<MetodoEnvioResponseDTO>> create(
            @RequestBody MetodoEnvioRequestDTO body) {
        MetodoEnvioResponseDTO nuevo = metodoEnvioService.create(body);
        EntityModel<MetodoEnvioResponseDTO> entityModel = metodoEnvioAssembler.toModel(nuevo);
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar método de envío (PUT - HATEOAS)")
    public ResponseEntity<EntityModel<MetodoEnvioResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody MetodoEnvioRequestDTO body) {
        try {
            MetodoEnvioResponseDTO actualizado = metodoEnvioService.update(id, body);
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
    public ResponseEntity<EntityModel<MetodoEnvioResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody MetodoEnvioUpdateDTO parcial) {
        try {
            MetodoEnvioResponseDTO actualizado = metodoEnvioService.patch(id, parcial);
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