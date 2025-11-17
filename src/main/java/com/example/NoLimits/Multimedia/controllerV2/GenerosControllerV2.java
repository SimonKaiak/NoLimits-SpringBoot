package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.GenerosModelAssembler;
import com.example.NoLimits.Multimedia.model.GenerosModel;
import com.example.NoLimits.Multimedia.service.GenerosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/productos-generos", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Generos-Controller-V2", description = "Relaciones Producto–Género (HATEOAS).")
public class GenerosControllerV2 {

    @Autowired
    private GenerosService generosService;

    @Autowired
    private GenerosModelAssembler generosAssembler;

    // =========== LISTAR POR PRODUCTO ===========
    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar géneros de un producto (HATEOAS)",
            description = "Devuelve las relaciones Producto–Género para un producto dado.")
    @ApiResponse(responseCode = "200", description = "Relaciones encontradas.",
        content = @Content(mediaType = "application/hal+json",
            array = @ArraySchema(schema = @Schema(implementation = GenerosModel.class))))
    public ResponseEntity<CollectionModel<EntityModel<GenerosModel>>> obtenerPorProducto(
            @PathVariable Long productoId) {

        List<EntityModel<GenerosModel>> relaciones = generosService.findByProducto(productoId)
                .stream()
                .map(generosAssembler::toModel)
                .collect(Collectors.toList());

        if (relaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        relaciones,
                        linkTo(methodOn(GenerosControllerV2.class).obtenerPorProducto(productoId)).withSelfRel()
                )
        );
    }

    // =========== LISTAR POR GÉNERO ===========
    @GetMapping("/genero/{generoId}")
    @Operation(summary = "Listar productos de un género (HATEOAS)",
            description = "Devuelve las relaciones Producto–Género asociadas a un género dado.")
    @ApiResponse(responseCode = "200", description = "Relaciones encontradas.",
        content = @Content(mediaType = "application/hal+json",
            array = @ArraySchema(schema = @Schema(implementation = GenerosModel.class))))
    public ResponseEntity<CollectionModel<EntityModel<GenerosModel>>> obtenerPorGenero(
            @PathVariable Long generoId) {

        List<EntityModel<GenerosModel>> relaciones = generosService.findByGenero(generoId)
                .stream()
                .map(generosAssembler::toModel)
                .collect(Collectors.toList());

        if (relaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        relaciones,
                        linkTo(methodOn(GenerosControllerV2.class).obtenerPorGenero(generoId)).withSelfRel()
                )
        );
    }

    // =========== CREAR VÍNCULO ===========
    @PostMapping("/producto/{productoId}/genero/{generoId}")
    @Operation(summary = "Vincular producto con género (HATEOAS)",
            description = "Crea la relación Producto–Género si no existe.")
    @ApiResponse(responseCode = "200", description = "Relación creada o ya existente.",
        content = @Content(mediaType = "application/hal+json",
            schema = @Schema(implementation = GenerosModel.class)))
    public ResponseEntity<EntityModel<GenerosModel>> vincular(
            @PathVariable Long productoId,
            @PathVariable Long generoId) {
        try {
            GenerosModel rel = generosService.link(productoId, generoId);
            return ResponseEntity.ok(generosAssembler.toModel(rel));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========== ELIMINAR VÍNCULO ===========
    @DeleteMapping("/producto/{productoId}/genero/{generoId}")
    @Operation(summary = "Desvincular producto de género (HATEOAS)",
            description = "Elimina la relación Producto–Género si existe.")
    @ApiResponse(responseCode = "204", description = "Relación eliminada (o no existía).")
    public ResponseEntity<Void> desvincular(
            @PathVariable Long productoId,
            @PathVariable Long generoId) {
        try {
            generosService.unlink(productoId, generoId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========== RESUMEN (NO HATEOAS, PERO ÚTIL) ===========
    @GetMapping("/resumen")
    @Operation(summary = "Resumen de relaciones Producto–Género.",
            description = "Devuelve un resumen opcionalmente filtrado por productoId y/o generoId.")
    @ApiResponse(responseCode = "200", description = "Resumen obtenido.",
        content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Map<String, Object>>> resumen(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Long generoId) {

        List<Object[]> filas = generosService.obtenerResumen(productoId, generoId);
        if (filas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Map<String, Object>> resultado = filas.stream().map(fila -> {
            Map<String, Object> map = new HashMap<>();
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