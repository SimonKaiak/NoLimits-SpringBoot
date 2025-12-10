package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GenerosResponseDTO;
import com.example.NoLimits.Multimedia.service.catalogos.GenerosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST V1 (sin HATEOAS) para la relación Producto–Género.
 *
 * Expone la información usando DTO (GenerosResponseDTO) y endpoints simples.
 */
@RestController
@RequestMapping("/api/v1/productos-generos")
@Tag(name = "Generos-Controller", description = "Relaciones puente entre Producto y Género.")
public class GenerosController {

    @Autowired
    private GenerosService generosService;

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar géneros de un producto.",
            description = "Devuelve las relaciones Producto–Género para un producto dado.")
    @ApiResponse(responseCode = "200", description = "Relaciones encontradas.",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = GenerosResponseDTO.class))))
    public ResponseEntity<List<GenerosResponseDTO>> obtenerPorProducto(@PathVariable Long productoId) {
        List<GenerosResponseDTO> relaciones = generosService.findByProducto(productoId);
        if (relaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(relaciones);
    }

    @GetMapping("/genero/{generoId}")
    @Operation(summary = "Listar productos de un género.",
            description = "Devuelve las relaciones Producto–Género asociadas a un género dado.")
    @ApiResponse(responseCode = "200", description = "Relaciones encontradas.",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = GenerosResponseDTO.class))))
    public ResponseEntity<List<GenerosResponseDTO>> obtenerPorGenero(@PathVariable Long generoId) {
        List<GenerosResponseDTO> relaciones = generosService.findByGenero(generoId);
        if (relaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(relaciones);
    }

    @PostMapping("/producto/{productoId}/genero/{generoId}")
    @Operation(summary = "Vincular producto con género.",
            description = "Crea (si no existe) la relación entre un producto y un género.")
    @ApiResponse(responseCode = "200", description = "Relación creada o ya existente.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GenerosResponseDTO.class)))
    public ResponseEntity<GenerosResponseDTO> vincular(
            @PathVariable Long productoId,
            @PathVariable Long generoId
    ) {
        try {
            GenerosResponseDTO rel = generosService.link(productoId, generoId);
            return ResponseEntity.ok(rel);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/producto/{productoId}/genero/{generoId}")
    @Operation(summary = "Desvincular producto de género.",
            description = "Elimina la relación entre un producto y un género si existe.")
    @ApiResponse(responseCode = "204", description = "Relación eliminada (o no existía).")
    public ResponseEntity<Void> desvincular(
            @PathVariable Long productoId,
            @PathVariable Long generoId
    ) {
        try {
            generosService.unlink(productoId, generoId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --------------------------------------------------------
    // PATCH - Actualizar relación Producto–Género
    // --------------------------------------------------------
    @PatchMapping("/{relacionId}")
    @Operation(
            summary = "Actualizar parcialmente la relación Producto–Género.",
            description = "Permite cambiar el producto y/o el género asociados a la relación puente. "
                    + "Puedes enviar solo nuevoProductoId, solo nuevoGeneroId, o ambos."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Relación actualizada.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GenerosResponseDTO.class)
            )
    )
    public ResponseEntity<GenerosResponseDTO> patch(
            @PathVariable Long relacionId,
            @RequestParam(required = false) Long nuevoProductoId,
            @RequestParam(required = false) Long nuevoGeneroId
    ) {
        try {
            GenerosResponseDTO actualizado = generosService.patch(relacionId, nuevoProductoId, nuevoGeneroId);
            return ResponseEntity.ok(actualizado);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resumen")
    @Operation(summary = "Resumen de relaciones Producto–Género.",
            description = "Devuelve un resumen opcionalmente filtrado por productoId y/o generoId.")
    @ApiResponse(responseCode = "200", description = "Resumen obtenido.",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Map<String, Object>>> resumen(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Long generoId
    ) {
        List<Object[]> filas = generosService.obtenerResumen(productoId, generoId);
        if (filas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Map<String, Object>> resultado = filas.stream().map(fila -> {
            Map<String, Object> map = new HashMap<>();
            // [relId, productoId, productoNombre, generoId, generoNombre]
            map.put("relacionId", fila[0]);
            map.put("productoId", fila[1]);
            map.put("productoNombre", fila[2]);
            map.put("generoId", fila[3]);
            map.put("generoNombre", fila[4]);
            return map;
        }).toList();

        return ResponseEntity.ok(resultado);
    }
}