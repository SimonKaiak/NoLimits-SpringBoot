package com.example.NoLimits.Multimedia.controllerV2.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.producto.ImagenesModelAssembler;
import com.example.NoLimits.Multimedia.dto.producto.request.ImagenesRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ImagenesResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ImagenesUpdateDTO;
import com.example.NoLimits.Multimedia.service.producto.ImagenesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping(value = "/api/v2/imagenes", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Imagenes-Controller-V2", description = "Operaciones HATEOAS para las imágenes de los productos.")
public class ImagenesControllerV2 {

    @Autowired
    private ImagenesService imagenesService;

    @Autowired
    private ImagenesModelAssembler imagenesAssembler;

    /* ================== LISTAR ================== */

    @GetMapping
    @Operation(
        summary = "Obtener todas las imágenes (HATEOAS)",
        description = "Devuelve todas las imágenes registradas con enlaces HATEOAS."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de imágenes obtenida correctamente.",
            content = @Content(
                mediaType = "application/hal+json",
                array = @ArraySchema(schema = @Schema(implementation = ImagenesResponseDTO.class))
            )
        ),
        @ApiResponse(responseCode = "204", description = "No hay imágenes registradas.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ImagenesResponseDTO>>> getAll() {
        List<EntityModel<ImagenesResponseDTO>> imagenes = imagenesService.findAll().stream()
                .map(imagenesAssembler::toModel)
                .collect(Collectors.toList());

        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        imagenes,
                        linkTo(methodOn(ImagenesControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener una imagen por ID (HATEOAS)",
        description = "Devuelve una imagen específica con enlaces HATEOAS."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imagen encontrada.",
            content = @Content(
                mediaType = "application/hal+json",
                schema = @Schema(implementation = ImagenesResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada.")
    })
    public ResponseEntity<EntityModel<ImagenesResponseDTO>> getById(@PathVariable Long id) {
        try {
            ImagenesResponseDTO img = imagenesService.findById(id);
            return ResponseEntity.ok(imagenesAssembler.toModel(img));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/producto/{productoId}")
    @Operation(
        summary = "Obtener imágenes por producto (HATEOAS)",
        description = "Devuelve todas las imágenes asociadas a un producto, con enlaces HATEOAS."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imágenes encontradas.",
            content = @Content(
                mediaType = "application/hal+json",
                array = @ArraySchema(schema = @Schema(implementation = ImagenesResponseDTO.class))
            )
        ),
        @ApiResponse(responseCode = "204", description = "El producto no tiene imágenes asociadas.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ImagenesResponseDTO>>> getByProducto(@PathVariable Long productoId) {
        List<EntityModel<ImagenesResponseDTO>> imagenes = imagenesService.findByProducto(productoId).stream()
                .map(imagenesAssembler::toModel)
                .collect(Collectors.toList());

        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        imagenes,
                        linkTo(methodOn(ImagenesControllerV2.class).getByProducto(productoId)).withSelfRel()
                )
        );
    }

    /* ================== CREAR ================== */

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Crear una nueva imagen (HATEOAS)",
        description = "Registra una nueva imagen asociada a un producto y devuelve el recurso con enlaces HATEOAS.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImagenesRequestDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Imagen mínima",
                        description = "Incluye ruta y el ID del producto.",
                        value = """
                        {
                          "ruta": "/assets/img/Peliculas/spiderman.webp",
                          "altText": "Spider-Man posando",
                          "productoId": 10
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "201",
        description = "Imagen creada correctamente.",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = ImagenesResponseDTO.class)
        )
    )
    public ResponseEntity<EntityModel<ImagenesResponseDTO>> create(
            @Valid @RequestBody ImagenesRequestDTO body) {

        ImagenesResponseDTO nueva = imagenesService.save(body);
        EntityModel<ImagenesResponseDTO> entity = imagenesAssembler.toModel(nueva);

        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    /* ================== ACTUALIZAR ================== */

    @PutMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
        summary = "Actualizar una imagen (PUT - HATEOAS)",
        description = "Reemplaza completamente los datos de una imagen existente."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imagen actualizada correctamente.",
            content = @Content(
                mediaType = "application/hal+json",
                schema = @Schema(implementation = ImagenesResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada.")
    })
    public ResponseEntity<EntityModel<ImagenesResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ImagenesUpdateDTO detalles) {

        try {
            ImagenesResponseDTO actualizada = imagenesService.update(id, detalles);
            return ResponseEntity.ok(imagenesAssembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
        summary = "Actualizar parcialmente una imagen (PATCH - HATEOAS)",
        description = "Modifica uno o más campos de una imagen existente.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImagenesUpdateDTO.class),
                examples = @ExampleObject(
                    name = "PATCH ejemplo",
                    description = "Solo modifica la ruta.",
                    value = """
                    {
                      "ruta": "/assets/img/Peliculas/spiderman-remaster.webp"
                    }
                    """
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imagen actualizada parcialmente.",
            content = @Content(
                mediaType = "application/hal+json",
                schema = @Schema(implementation = ImagenesResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada.")
    })
    public ResponseEntity<EntityModel<ImagenesResponseDTO>> patch(
            @PathVariable Long id,
            @Valid @RequestBody ImagenesUpdateDTO detalles) {

        try {
            ImagenesResponseDTO actualizada = imagenesService.patch(id, detalles);
            return ResponseEntity.ok(imagenesAssembler.toModel(actualizada));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /* ================== ELIMINAR ================== */

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar una imagen (HATEOAS)",
        description = "Elimina una imagen por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Imagen eliminada correctamente."),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            imagenesService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/producto/{productoId}")
    @Operation(
        summary = "Eliminar todas las imágenes de un producto (HATEOAS)",
        description = "Elimina todas las imágenes asociadas a un producto existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Imágenes eliminadas (si existían)."),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<Void> deleteByProducto(@PathVariable Long productoId) {
        try {
            imagenesService.deleteByProducto(productoId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}