// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/EmpresasController.java
package com.example.NoLimits.Multimedia.controller.catalogos;

import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresasResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresasUpdateDTO;
import com.example.NoLimits.Multimedia.service.catalogos.EmpresasService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos/{productoId}/empresas")
@Tag(name = "Empresas-Controller")
public class EmpresasController {

    @Autowired
    private EmpresasService service;

    @GetMapping
    public List<EmpresasResponseDTO> listar(@PathVariable Long productoId) {
        return service.findByProducto(productoId);
    }

    @PostMapping("/{empresaId}")
    public ResponseEntity<EmpresasResponseDTO> link(
            @PathVariable Long productoId,
            @PathVariable Long empresaId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.link(productoId, empresaId));
    }

    @PatchMapping("/{relacionId}")
    public ResponseEntity<EmpresasResponseDTO> patch(
            @PathVariable Long relacionId,
            @RequestBody EmpresasUpdateDTO dto) {

        return ResponseEntity.ok(service.patch(relacionId, dto));
    }

    @DeleteMapping("/{empresaId}")
    public ResponseEntity<Void> unlink(
            @PathVariable Long productoId,
            @PathVariable Long empresaId) {

        service.unlink(productoId, empresaId);
        return ResponseEntity.noContent().build();
    }
}