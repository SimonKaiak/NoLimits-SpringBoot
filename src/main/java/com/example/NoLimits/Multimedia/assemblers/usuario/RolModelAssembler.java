package com.example.NoLimits.Multimedia.assemblers.usuario;

import com.example.NoLimits.Multimedia.controllerV2.usuario.RolControllerV2;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler HATEOAS para envolver RolResponseDTO en un EntityModel.
 *
 * La idea es no exponer directamente la entidad JPA (RolModel) en la capa REST
 * y trabajar siempre con el DTO de salida.
 */
@Component
public class RolModelAssembler implements RepresentationModelAssembler<RolResponseDTO, EntityModel<RolResponseDTO>> {

    @Override
    public EntityModel<RolResponseDTO> toModel(RolResponseDTO rol) {

        return EntityModel.of(
                rol,
                // Link al propio recurso (self)
                linkTo(methodOn(RolControllerV2.class).getById(rol.getId())).withSelfRel(),
                // Link a la colección de roles
                linkTo(methodOn(RolControllerV2.class).getAll()).withRel("roles")
        );
    }
}