// Ruta: src/main/java/com/example/NoLimits/Multimedia/assemblers/ubicacion/ComunaModelAssembler.java
package com.example.NoLimits.Multimedia.assemblers.ubicacion;

import com.example.NoLimits.Multimedia.controllerV2.ubicacion.ComunaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.ubicacion.RegionControllerV2;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler HATEOAS para envolver {@link ComunaResponseDTO} en un {@link EntityModel}.
 *
 * La idea de este assembler es:
 * - Devolver siempre la comuna como DTO (sin exponer la entidad JPA).
 * - Agregar los links típicos: self, colección y link a la región si existe.
 *
 * De esta forma, la API v2 queda más limpia y desacoplada del modelo interno.
 */
@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<ComunaResponseDTO, EntityModel<ComunaResponseDTO>> {

    @Override
    public EntityModel<ComunaResponseDTO> toModel(ComunaResponseDTO comuna) {

        // Modelo base con el DTO y los links principales de comunas
        EntityModel<ComunaResponseDTO> model = EntityModel.of(
                comuna,
                // Link al recurso actual (self)
                linkTo(methodOn(ComunaControllerV2.class).getById(comuna.getId())).withSelfRel(),
                // Link a la colección completa de comunas
                linkTo(methodOn(ComunaControllerV2.class).getAll()).withRel("comunas")
        );

        // Link a la región asociada (si el DTO trae regionId)
        if (comuna.getRegionId() != null) {
            model.add(
                    linkTo(methodOn(RegionControllerV2.class).getById(comuna.getRegionId()))
                            .withRel("region")
            );
        }

        return model;
    }
}