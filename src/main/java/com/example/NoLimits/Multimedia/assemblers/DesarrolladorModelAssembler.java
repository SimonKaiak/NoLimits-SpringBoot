package com.example.NoLimits.Multimedia.assemblers;

import com.example.NoLimits.Multimedia.controllerV2.DesarrolladorControllerV2;
import com.example.NoLimits.Multimedia.model.DesarrolladorModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DesarrolladorModelAssembler implements RepresentationModelAssembler<DesarrolladorModel, EntityModel<DesarrolladorModel>> {

    @Override
    public EntityModel<DesarrolladorModel> toModel(DesarrolladorModel entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(DesarrolladorControllerV2.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(DesarrolladorControllerV2.class).getAll()).withRel("desarrolladores")
        );
    }
}