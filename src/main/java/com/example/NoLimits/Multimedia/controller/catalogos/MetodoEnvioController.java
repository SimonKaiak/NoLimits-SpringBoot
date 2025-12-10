package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoEnvioRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoEnvioUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoEnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/metodos-envio")
@Validated
public class MetodoEnvioController {

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @GetMapping
    @Operation(summary = "Listar métodos de envío")
    public ResponseEntity<List<MetodoEnvioResponseDTO>> listar() {
        List<MetodoEnvioResponseDTO> metodos = metodoEnvioService.findAll();
        return metodos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(metodos);
    }

    @GetMapping("/paginado")
    @Operation(summary = "Listar métodos de envío con paginación real")
    public ResponseEntity<PagedResponse<MetodoEnvioResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        return ResponseEntity.ok(
            metodoEnvioService.findAllPaged(page, size, search)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar método de envío por ID")
    public ResponseEntity<MetodoEnvioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(metodoEnvioService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear método de envío",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "nombre": "Retiro en tienda",
                      "descripcion": "Sucursal Plaza Oeste"
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<MetodoEnvioResponseDTO> crear(
            @Valid @RequestBody MetodoEnvioRequestDTO body) {
        MetodoEnvioResponseDTO creado = metodoEnvioService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar método de envío")
    public ResponseEntity<MetodoEnvioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MetodoEnvioRequestDTO body) {
        return ResponseEntity.ok(metodoEnvioService.update(id, body));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Editar parcialmente método de envío")
    public ResponseEntity<MetodoEnvioResponseDTO> patch(
            @PathVariable Long id,
            @Valid @RequestBody MetodoEnvioUpdateDTO body) {
        return ResponseEntity.ok(metodoEnvioService.patch(id, body));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar método de envío")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        metodoEnvioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}