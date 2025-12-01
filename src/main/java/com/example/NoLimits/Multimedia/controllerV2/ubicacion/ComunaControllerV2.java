package com.example.NoLimits.Multimedia.controllerV2.ubicacion;

import java.util.List;
import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.ubicacion.ComunaModelAssembler;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.ComunaRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.ComunaUpdateDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.ComunaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controlador V2 de comunas con soporte HATEOAS.
 *
 * - Expone los datos usando DTOs (ComunaRequest/Response/Update).
 * - Responde en formato HAL+JSON (EntityModel / CollectionModel).
 * - La lógica de negocio y mapeos vive en el service y en el assembler.
 *
 * La idea es tener esta versión V2 más "restful" y desacoplada de las entidades JPA.
 */
@RestController
@RequestMapping(value = "/api/v2/comunas", produces = MediaTypes.HAL_JSON_VALUE)
@Validated
@Tag(name = "Comunas", description = "Gestión de comunas (V2 HATEOAS)")
public class ComunaControllerV2 {

    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaModelAssembler comunaAssembler;

    /**
     * GET /api/v2/comunas
     *
     * Lista todas las comunas, devolviendo una colección HAL con:
     * - Cada comuna envuelta en EntityModel<ComunaResponseDTO>.
     * - Link self de la colección.
     */
    @GetMapping
    @Operation(summary = "Listar todas las comunas")
    public ResponseEntity<CollectionModel<EntityModel<ComunaResponseDTO>>> getAll() {
        List<EntityModel<ComunaResponseDTO>> comunas = comunaService.findAll()
                .stream()
                .map(comunaAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ComunaResponseDTO>> body = CollectionModel.of(
                comunas,
                linkTo(methodOn(ComunaControllerV2.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(body);
    }

    /**
     * GET /api/v2/comunas/{id}
     *
     * Devuelve una comuna puntual, envuelta en EntityModel con sus links.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una comuna por ID")
    public ResponseEntity<EntityModel<ComunaResponseDTO>> getById(@PathVariable Long id) {
        try {
            ComunaResponseDTO comuna = comunaService.findById(id);
            return ResponseEntity.ok(comunaAssembler.toModel(comuna));
        } catch (RecursoNoEncontradoException ex) {
            // Podría manejarse a nivel global, pero por ahora se responde 404 directo
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/v2/comunas
     *
     * Crea una nueva comuna a partir de un ComunaRequestDTO.
     * El body de respuesta devuelve la comuna creada envuelta en EntityModel,
     * con Location apuntando al self de la nueva comuna.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear una nueva comuna")
    public ResponseEntity<EntityModel<ComunaResponseDTO>> create(
            @Valid @RequestBody ComunaRequestDTO comunaRequest) {

        ComunaResponseDTO creada = comunaService.create(comunaRequest);
        EntityModel<ComunaResponseDTO> entityModel = comunaAssembler.toModel(creada);

        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * PUT /api/v2/comunas/{id}
     *
     * Actualización completa de una comuna usando ComunaUpdateDTO.
     * Aquí se espera que vengan todos los datos necesarios (nombre + regionId).
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar completamente una comuna")
    public ResponseEntity<EntityModel<ComunaResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ComunaUpdateDTO detalles) {

        ComunaResponseDTO actualizada = comunaService.update(id, detalles);
        EntityModel<ComunaResponseDTO> entityModel = comunaAssembler.toModel(actualizada);

        return ResponseEntity.ok(entityModel);
    }

    /**
     * PATCH /api/v2/comunas/{id}
     *
     * Actualización parcial de una comuna.
     * Solo se modifican los campos no nulos que vengan en el ComunaUpdateDTO.
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente una comuna (PATCH)")
    public ResponseEntity<EntityModel<ComunaResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody ComunaUpdateDTO parciales) {

        ComunaResponseDTO actualizada = comunaService.patch(id, parciales);
        EntityModel<ComunaResponseDTO> entityModel = comunaAssembler.toModel(actualizada);

        return ResponseEntity.ok(entityModel);
    }

    /**
     * DELETE /api/v2/comunas/{id}
     *
     * Elimina una comuna por ID.
     * Si tiene direcciones asociadas, el service lanza una excepción de estado
     * para impedir la eliminación.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una comuna por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comunaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}