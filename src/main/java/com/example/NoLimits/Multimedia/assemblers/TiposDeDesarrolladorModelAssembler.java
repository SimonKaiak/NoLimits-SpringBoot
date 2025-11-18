package com.example.NoLimits.Multimedia.assemblers;

import com.example.NoLimits.Multimedia.controllerV2.TiposDeDesarrolladorControllerV2;
import com.example.NoLimits.Multimedia.model.TiposDeDesarrolladorModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TiposDeDesarrolladorModelAssembler
        implements RepresentationModelAssembler<TiposDeDesarrolladorModel, EntityModel<TiposDeDesarrolladorModel>> {

    @Override
    public EntityModel<TiposDeDesarrolladorModel> toModel(TiposDeDesarrolladorModel entity) {

        Long desarrolladorId = entity.getDesarrollador() != null
                ? entity.getDesarrollador().getId()
                : null;

        Long tipoId = entity.getTipoDeDesarrollador() != null
                ? entity.getTipoDeDesarrollador().getId()
                : null;

        EntityModel<TiposDeDesarrolladorModel> model = EntityModel.of(entity);

        if (desarrolladorId != null) {
            model.add(
                    linkTo(methodOn(TiposDeDesarrolladorControllerV2.class)
                            .listar(desarrolladorId))
                            .withRel("lista")
            );

            if (tipoId != null) {
                model.add(
                        linkTo(methodOn(TiposDeDesarrolladorControllerV2.class)
                                .link(desarrolladorId, tipoId))
                                .withRel("vincular")
                );
                model.add(
                        linkTo(methodOn(TiposDeDesarrolladorControllerV2.class)
                                .unlink(desarrolladorId, tipoId))
                                .withRel("desvincular")
                );
            }
        }

        return model;
    }
}