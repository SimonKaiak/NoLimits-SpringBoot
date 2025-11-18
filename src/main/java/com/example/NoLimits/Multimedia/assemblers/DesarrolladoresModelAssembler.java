package com.example.NoLimits.Multimedia.assemblers;

import com.example.NoLimits.Multimedia.controllerV2.DesarrolladoresControllerV2;
import com.example.NoLimits.Multimedia.model.DesarrolladoresModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DesarrolladoresModelAssembler implements RepresentationModelAssembler<DesarrolladoresModel, EntityModel<DesarrolladoresModel>> {

    @Override
    public EntityModel<DesarrolladoresModel> toModel(DesarrolladoresModel entity) {

        Long productoId = entity.getProducto().getId();
        Long desarrolladorId = entity.getDesarrollador().getId();

        return EntityModel.of(
                entity,
                linkTo(methodOn(DesarrolladoresControllerV2.class).listar(productoId)).withRel("lista"),
                linkTo(methodOn(DesarrolladoresControllerV2.class).link(productoId, desarrolladorId)).withRel("vincular"),
                linkTo(methodOn(DesarrolladoresControllerV2.class).unlink(productoId, desarrolladorId)).withRel("desvincular")
        );
    }
}