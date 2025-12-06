package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.GeneroRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GeneroResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.GeneroUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.GeneroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/generos")
@Tag(name = "Genero-Controller", description = "Catálogo de géneros (Acción, Terror, etc.).")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @GetMapping
    @Operation(summary = "Listar todos los géneros.", description = "Obtiene una lista de todos los géneros registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de géneros obtenida exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GeneroResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay géneros disponibles.")
    })
    public ResponseEntity<List<GeneroResponseDTO>> listarGeneros() {
        List<GeneroResponseDTO> generos = generoService.findAll();
        if (generos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(generos);
    }

    // ================== LISTAR PAGINADO ==================
    @GetMapping("/paginado")
    @Operation(summary = "Listar géneros con paginación y búsqueda")
    public ResponseEntity<PagedResponse<GeneroResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        PagedResponse<GeneroResponseDTO> resultado =
                generoService.listarPaginado(page, size, search);

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar género por ID.", description = "Obtiene un género específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Género encontrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GeneroResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Género no encontrado.")
    })
    public ResponseEntity<GeneroResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(generoService.findById(id));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar géneros por nombre.", description = "Busca géneros que contengan el texto indicado (ignorando mayúsculas).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Géneros encontrados.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GeneroResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron géneros con ese nombre.")
    })
    public ResponseEntity<List<GeneroResponseDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<GeneroResponseDTO> generos = generoService.findByNombreContaining(nombre);
        if (generos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(generos);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo género.", description = "Registra un nuevo género en el catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Género creado exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GeneroResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos.")
    })
    public ResponseEntity<GeneroResponseDTO> crearGenero(@Valid @RequestBody GeneroRequestDTO genero) {
        GeneroResponseDTO nuevo = generoService.save(genero);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un género (PUT).", description = "Reemplaza o actualiza los datos de un género.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Género actualizado exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GeneroResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Género no encontrado.")
    })
    public ResponseEntity<GeneroResponseDTO> actualizarGenero(
            @PathVariable Long id,
            @RequestBody GeneroUpdateDTO detalles) {
        try {
            return ResponseEntity.ok(generoService.update(id, detalles));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Editar parcialmente un género (PATCH).", description = "Modifica algunos campos del género.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Género actualizado parcialmente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GeneroResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Género no encontrado.")
    })
    public ResponseEntity<GeneroResponseDTO> editarGenero(
            @PathVariable Long id,
            @RequestBody GeneroUpdateDTO detalles) {
        try {
            return ResponseEntity.ok(generoService.patch(id, detalles));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un género.", description = "Elimina un género del catálogo por ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Género eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "Género no encontrado.")
    })
    public ResponseEntity<Void> eliminarGenero(@PathVariable Long id) {
        try {
            generoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}