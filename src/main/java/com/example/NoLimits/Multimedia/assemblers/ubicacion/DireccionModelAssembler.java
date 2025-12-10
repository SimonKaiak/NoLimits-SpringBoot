package com.example.NoLimits.Multimedia.assemblers.ubicacion;

import com.example.NoLimits.Multimedia.controllerV2.ubicacion.DireccionControllerV2;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.DireccionResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler HATEOAS para envolver DireccionResponseDTO en EntityModel.
 *
 * La idea es:
 * - No exponer la entidad JPA DireccionModel directamente.
 * - Usar siempre el DTO de salida que ya armé para la capa REST.
 * - Agregar los links básicos (self, colección).
 *
 * Si más adelante al DTO le agrego IDs de comuna/region, acá se pueden
 * agregar links extra hacia esos recursos (ComunaControllerV2, RegionControllerV2).
 */
@Component
public class DireccionModelAssembler implements RepresentationModelAssembler<DireccionResponseDTO, EntityModel<DireccionResponseDTO>> {

    @Override
    public EntityModel<DireccionResponseDTO> toModel(DireccionResponseDTO direccion) {

        EntityModel<DireccionResponseDTO> model = EntityModel.of(
                direccion,
                // Link al recurso actual
                linkTo(methodOn(DireccionControllerV2.class).getById(direccion.getId())).withSelfRel(),
                // Link a la colección de direcciones
                linkTo(methodOn(DireccionControllerV2.class).getAll()).withRel("direcciones")
        );

        // Nota:
        // Por ahora el DTO solo expone nombres de comuna/region (no sus IDs),
        // así que no puedo construir links directos a esos recursos.
        // Si después agrego comunaId / regionId al DTO, acá puedo sumar:
        // model.add(linkTo(methodOn(ComunaControllerV2.class).getById(...)).withRel("comuna"));

        return model;
    }
}