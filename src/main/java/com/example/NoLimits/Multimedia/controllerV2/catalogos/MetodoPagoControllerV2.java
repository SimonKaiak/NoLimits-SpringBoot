package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.MetodoPagoModelAssembler;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/metodos-pago", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "MetodoPago-Controller-V2", description = "Operaciones relacionadas con los métodos de pago (HATEOAS).")
public class MetodoPagoControllerV2 {

    @Autowired private MetodoPagoService metodoPagoService;
    @Autowired private MetodoPagoModelAssembler metodoPagoAssembler;

    // Obtener todos los métodos de pago
    @Operation(summary = "Obtener todos los métodos de pago (HATEOAS)",
               description = "Devuelve una lista de todos los métodos de pago con enlaces HATEOAS.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente.",
                 content = @Content(mediaType = "application/hal+json",
                 schema = @Schema(implementation = MetodoPagoModel.class)))
    @ApiResponse(responseCode = "204", description = "No hay métodos de pago registrados.")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<MetodoPagoModel>>> getAll() {
        List<EntityModel<MetodoPagoModel>> metodosPago = metodoPagoService.findAll().stream()
                .map(metodoPagoAssembler::toModel)
                .collect(Collectors.toList());

        if (metodosPago.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(metodosPago,
                        linkTo(methodOn(MetodoPagoControllerV2.class).getAll()).withSelfRel())
        );
    }

    // Obtener un método de pago por ID
    @Operation(summary = "Obtener un método de pago por ID (HATEOAS)",
               description = "Devuelve un método de pago específico con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Método de pago encontrado.",
                     content = @Content(mediaType = "application/hal+json",
                     schema = @Schema(implementation = MetodoPagoModel.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado.")
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoModel>> getById(@PathVariable Long id) {
        try {
            MetodoPagoModel metodo = metodoPagoService.findById(id);
            return ResponseEntity.ok(metodoPagoAssembler.toModel(metodo));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un nuevo método de pago
    @Operation(
        summary = "Crear un nuevo método de pago (HATEOAS)",
        description = "Crea un nuevo método de pago y devuelve el recurso con enlaces HATEOAS.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "POST mínimo",
                    value = """
                    { "nombre": "Tarjeta de Débito" }
                    """
                )
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Método de pago creado correctamente.",
                     content = @Content(mediaType = "application/hal+json",
                     schema = @Schema(implementation = MetodoPagoModel.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud.")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoModel>> create(
            @org.springframework.web.bind.annotation.RequestBody MetodoPagoModel metodoPago) {
        MetodoPagoModel nuevo = metodoPagoService.save(metodoPago);
        EntityModel<MetodoPagoModel> entityModel = metodoPagoAssembler.toModel(nuevo);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Actualizar un método de pago (PUT)
    @Operation(
        summary = "Actualizar un método de pago (PUT - HATEOAS)",
        description = "Reemplaza completamente un método de pago existente por uno nuevo.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "PUT seguro (sin id en body)",
                        value = """
                        { "nombre": "Tarjeta de Crédito" }
                        """
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "PUT con id (coincide con path)",
                        value = """
                        { "id": 1, "nombre": "Tarjeta de Crédito" }
                        """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Método de pago actualizado correctamente.",
                     content = @Content(mediaType = "application/hal+json",
                     schema = @Schema(implementation = MetodoPagoModel.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado.")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoModel>> update(@PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody MetodoPagoModel metodoPagoDetails) {
        try {
            MetodoPagoModel actualizado = metodoPagoService.update(id, metodoPagoDetails);
            return ResponseEntity.ok(metodoPagoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar parcialmente un método de pago (PATCH)
    @Operation(
        summary = "Actualizar parcialmente un método de pago (PATCH - HATEOAS)",
        description = "Modifica campos específicos de un método de pago existente.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "PATCH ejemplo",
                    value = """
                    { "nombre": "Transferencia" }
                    """
                )
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Método de pago actualizado correctamente.",
                     content = @Content(mediaType = "application/hal+json",
                     schema = @Schema(implementation = MetodoPagoModel.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado.")
    })
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoModel>> patch(@PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody MetodoPagoModel metodoPagoDetails) {
        try {
            MetodoPagoModel actualizado = metodoPagoService.patch(id, metodoPagoDetails);
            return ResponseEntity.ok(metodoPagoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un método de pago
    @Operation(summary = "Eliminar un método de pago",
               description = "Elimina un método de pago por su ID si existe.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Método de pago eliminado correctamente."),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            metodoPagoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}