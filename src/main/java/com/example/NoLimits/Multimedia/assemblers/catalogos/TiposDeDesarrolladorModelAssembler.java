package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.TiposDeDesarrolladorControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TiposDeDesarrolladorModelAssembler implements RepresentationModelAssembler<TiposDeDesarrolladorResponseDTO, EntityModel<TiposDeDesarrolladorResponseDTO>> {

    @Override
    public EntityModel<TiposDeDesarrolladorResponseDTO> toModel(TiposDeDesarrolladorResponseDTO entity) {

        Long desarrolladorId = entity.getDesarrolladorId();
        Long tipoId = entity.getTipoDeDesarrolladorId();
        Long relacionId = entity.getId();

        EntityModel<TiposDeDesarrolladorResponseDTO> model = EntityModel.of(entity);

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

            if (relacionId != null) {
                model.add(
                        linkTo(methodOn(TiposDeDesarrolladorControllerV2.class)
                                .patch(desarrolladorId, relacionId, null))
                                .withRel("actualizar_relacion")
                );
            }
        }

        return model;
    }
}