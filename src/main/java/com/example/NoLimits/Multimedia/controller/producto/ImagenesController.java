package com.example.NoLimits.Multimedia.controller.producto;

import java.util.List;
import java.util.Map;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/imagenes")
@Tag(name = "Imagenes-Controller", description = "Operaciones relacionadas con las imágenes de los productos.")
@Validated
public class ImagenesController {

    @Autowired
    private ImagenesService imagenesService;

    /* ================== LISTAR ================== */

    @GetMapping
    @Operation(
        summary = "Listar todas las imágenes",
        description = "Obtiene una lista de todas las imágenes registradas."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de imágenes obtenida correctamente.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ImagenesResponseDTO.class))
            )
        ),
        @ApiResponse(responseCode = "204", description = "No hay imágenes registradas.")
    })
    public ResponseEntity<List<ImagenesResponseDTO>> listarImagenes() {
        List<ImagenesResponseDTO> imagenes = imagenesService.findAll();
        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(imagenes);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener una imagen por ID",
        description = "Devuelve una imagen específica según su ID."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imagen encontrada.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImagenesResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada.")
    })
    public ResponseEntity<ImagenesResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(imagenesService.findById(id));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(
        summary = "Listar imágenes por producto",
        description = "Obtiene todas las imágenes asociadas a un producto."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imágenes encontradas.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ImagenesResponseDTO.class))
            )
        ),
        @ApiResponse(responseCode = "204", description = "El producto no tiene imágenes asociadas.")
    })
    public ResponseEntity<List<ImagenesResponseDTO>> obtenerPorProducto(@PathVariable Long productoId) {
        List<ImagenesResponseDTO> imagenes = imagenesService.findByProducto(productoId);
        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(imagenes);
    }

    @GetMapping("/buscar")
    @Operation(
        summary = "Buscar imágenes por ruta parcial",
        description = "Busca imágenes cuya ruta contenga el texto indicado (ignorando mayúsculas/minúsculas)."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imágenes encontradas.",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ImagenesResponseDTO.class))
            )
        ),
        @ApiResponse(responseCode = "204", description = "No se encontraron imágenes que coincidan.")
    })
    public ResponseEntity<List<ImagenesResponseDTO>> buscarPorRuta(@RequestParam("ruta") String ruta) {
        List<ImagenesResponseDTO> imagenes = imagenesService.findByRutaContainingIgnoreCase(ruta);
        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(imagenes);
    }

    @GetMapping("/resumen")
    @Operation(
        summary = "Obtener resumen de imágenes",
        description = "Devuelve un resumen en formato tabla: ID, Ruta, AltText y ProductoId."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Resumen generado correctamente.",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                description = "Lista de filas con claves: ID, Ruta, AltText, ProductoId",
                implementation = Object.class
            )
        )
    )
    public ResponseEntity<List<Map<String, Object>>> obtenerResumen() {
        List<Map<String, Object>> resumen = imagenesService.obtenerImagenesResumen();
        if (resumen.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumen);
    }

    /* ================== CREAR ================== */

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Registrar una nueva imagen",
        description = "Crea una nueva imagen asociada a un producto.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImagenesRequestDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Imagen mínima",
                        description = "Solo requiere ruta y el ID del producto.",
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
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Imagen creada correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImagenesResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos.")
    })
    public ResponseEntity<ImagenesResponseDTO> crearImagen(
            @Valid @RequestBody ImagenesRequestDTO imagen) {

        ImagenesResponseDTO nueva = imagenesService.save(imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    /* ================== ACTUALIZAR ================== */

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Actualizar una imagen (PUT)",
        description = "Actualiza una imagen existente. Solo se modifican los campos enviados."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imagen actualizada correctamente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImagenesResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada.")
    })
    public ResponseEntity<ImagenesResponseDTO> actualizarImagen(
            @PathVariable Long id,
            @Valid @RequestBody ImagenesUpdateDTO detalles) {

        ImagenesResponseDTO actualizada = imagenesService.update(id, detalles);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Editar parcialmente una imagen (PATCH)",
        description = "Modifica uno o más campos de una imagen existente."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imagen actualizada parcialmente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImagenesResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada.")
    })
    public ResponseEntity<ImagenesResponseDTO> editarParcialmente(
            @PathVariable Long id,
            @Valid @RequestBody ImagenesUpdateDTO detalles) {

        ImagenesResponseDTO actualizada = imagenesService.patch(id, detalles);
        return ResponseEntity.ok(actualizada);
    }

    /* ================== ELIMINAR ================== */

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar una imagen",
        description = "Elimina una imagen por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Imagen eliminada correctamente."),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada.")
    })
    public ResponseEntity<Void> eliminarImagen(@PathVariable Long id) {
        imagenesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/producto/{productoId}")
    @Operation(
        summary = "Eliminar todas las imágenes de un producto",
        description = "Elimina todas las imágenes asociadas al producto indicado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Imágenes eliminadas (si existían)."),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<Void> eliminarPorProducto(@PathVariable Long productoId) {
        imagenesService.deleteByProducto(productoId);
        return ResponseEntity.noContent().build();
    }
}