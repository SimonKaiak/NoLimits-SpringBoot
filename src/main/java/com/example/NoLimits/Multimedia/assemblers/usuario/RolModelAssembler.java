package com.example.NoLimits.Multimedia.assemblers.usuario;

import com.example.NoLimits.Multimedia.controllerV2.usuario.RolControllerV2;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<RolModel, EntityModel<RolModel>> {

    @Override
    public EntityModel<RolModel> toModel(RolModel rol) {

        return EntityModel.of(
                rol,
                linkTo(methodOn(RolControllerV2.class).getById(rol.getId())).withSelfRel(),
                linkTo(methodOn(RolControllerV2.class).getAll()).withRel("roles")
        );
    }
}