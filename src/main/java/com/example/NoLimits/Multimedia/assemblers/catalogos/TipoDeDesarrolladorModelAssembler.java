package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.TipoDeDesarrolladorControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TipoDeDesarrolladorModelAssembler implements RepresentationModelAssembler<TipoDeDesarrolladorResponseDTO, EntityModel<TipoDeDesarrolladorResponseDTO>> {

    @Override
    public EntityModel<TipoDeDesarrolladorResponseDTO> toModel(TipoDeDesarrolladorResponseDTO entity) {
        return EntityModel.of(
                entity,
                // self
                linkTo(methodOn(TipoDeDesarrolladorControllerV2.class)
                        .getById(entity.getId()))
                        .withSelfRel(),
                // colección
                linkTo(methodOn(TipoDeDesarrolladorControllerV2.class)
                        .getAll())
                        .withRel("tipos-desarrollador"),
                // actualizar
                linkTo(methodOn(TipoDeDesarrolladorControllerV2.class)
                        .update(entity.getId(), null))
                        .withRel("actualizar"),
                // patch
                linkTo(methodOn(TipoDeDesarrolladorControllerV2.class)
                        .patch(entity.getId(), null))
                        .withRel("actualizar_parcial"),
                // eliminar
                linkTo(methodOn(TipoDeDesarrolladorControllerV2.class)
                        .delete(entity.getId()))
                        .withRel("eliminar"),
                // crear
                linkTo(methodOn(TipoDeDesarrolladorControllerV2.class)
                        .create(null))
                        .withRel("crear")
        );
    }
}