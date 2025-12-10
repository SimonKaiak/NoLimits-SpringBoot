package com.example.NoLimits.Multimedia.controllerV2.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.catalogos.MetodoPagoModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoPagoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoPagoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoPagoUpdateDTO;
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

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
                 schema = @Schema(implementation = MetodoPagoResponseDTO.class)))
    @ApiResponse(responseCode = "204", description = "No hay métodos de pago registrados.")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<MetodoPagoResponseDTO>>> getAll() {
        List<EntityModel<MetodoPagoResponseDTO>> metodosPago = metodoPagoService.findAll().stream()
                .map(metodoPagoAssembler::toModel)
                .collect(Collectors.toList());

        if (metodosPago.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        metodosPago,
                        linkTo(methodOn(MetodoPagoControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    // Obtener un método de pago por ID
    @Operation(summary = "Obtener un método de pago por ID (HATEOAS)",
               description = "Devuelve un método de pago específico con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Método de pago encontrado.",
                     content = @Content(mediaType = "application/hal+json",
                     schema = @Schema(implementation = MetodoPagoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MetodoPagoResponseDTO>> getById(@PathVariable Long id) {
        try {
            MetodoPagoResponseDTO metodo = metodoPagoService.findById(id);
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
                     schema = @Schema(implementation = MetodoPagoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud.")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoResponseDTO>> create(
            @Valid @RequestBody MetodoPagoRequestDTO body) {

        MetodoPagoResponseDTO nuevo = metodoPagoService.save(body);
        EntityModel<MetodoPagoResponseDTO> entityModel = metodoPagoAssembler.toModel(nuevo);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Actualizar un método de pago (PUT)
    @Operation(
        summary = "Actualizar un método de pago (PUT - HATEOAS)",
        description = "Reemplaza completamente un método de pago existente por uno nuevo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Método de pago actualizado correctamente.",
                     content = @Content(mediaType = "application/hal+json",
                     schema = @Schema(implementation = MetodoPagoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado.")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody MetodoPagoRequestDTO metodoPagoDetails) {

        try {
            MetodoPagoResponseDTO actualizado = metodoPagoService.update(id, metodoPagoDetails);
            return ResponseEntity.ok(metodoPagoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar parcialmente un método de pago (PATCH)
    @Operation(
        summary = "Actualizar parcialmente un método de pago (PATCH - HATEOAS)",
        description = "Modifica campos específicos de un método de pago existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Método de pago actualizado correctamente.",
                     content = @Content(mediaType = "application/hal+json",
                     schema = @Schema(implementation = MetodoPagoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado.")
    })
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody MetodoPagoUpdateDTO metodoPagoDetails) {

        try {
            MetodoPagoResponseDTO actualizado = metodoPagoService.patch(id, metodoPagoDetails);
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