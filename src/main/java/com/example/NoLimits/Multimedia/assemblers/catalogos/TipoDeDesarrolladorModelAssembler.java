package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.TipoDeDesarrolladorControllerV2;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TipoDeDesarrolladorModelAssembler implements RepresentationModelAssembler<TipoDeDesarrolladorModel, EntityModel<TipoDeDesarrolladorModel>> {

    @Override
    public EntityModel<TipoDeDesarrolladorModel> toModel(TipoDeDesarrolladorModel entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(TipoDeDesarrolladorControllerV2.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(TipoDeDesarrolladorControllerV2.class).getAll()).withRel("tipos-desarrollador")
        );
    }
}