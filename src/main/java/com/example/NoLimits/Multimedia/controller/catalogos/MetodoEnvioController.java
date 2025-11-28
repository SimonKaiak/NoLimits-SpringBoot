package com.example.NoLimits.Multimedia.controller.catalogos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoEnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/v1/metodos-envio")
@Validated
public class MetodoEnvioController {

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @GetMapping
    @Operation(summary = "Listar métodos de envío")
    public ResponseEntity<List<MetodoEnvioModel>> listar() {
        List<MetodoEnvioModel> metodos = metodoEnvioService.findAll();
        return metodos.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(metodos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar método de envío por ID")
    public ResponseEntity<MetodoEnvioModel> buscarPorId(@PathVariable Long id) {
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
    public ResponseEntity<MetodoEnvioModel> crear(@RequestBody MetodoEnvioModel m) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoEnvioService.save(m));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar método de envío")
    public ResponseEntity<MetodoEnvioModel> actualizar(
            @PathVariable Long id,
            @RequestBody MetodoEnvioModel m) {
        return ResponseEntity.ok(metodoEnvioService.update(id, m));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Editar parcialmente método de envío")
    public ResponseEntity<MetodoEnvioModel> patch(
            @PathVariable Long id,
            @RequestBody MetodoEnvioModel m) {
        return ResponseEntity.ok(metodoEnvioService.patch(id, m));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar método de envío")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        metodoEnvioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}