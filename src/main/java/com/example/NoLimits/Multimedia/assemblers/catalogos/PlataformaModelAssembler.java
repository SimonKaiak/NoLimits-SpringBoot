package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.PlataformaControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlataformaModelAssembler implements RepresentationModelAssembler<PlataformaResponseDTO, EntityModel<PlataformaResponseDTO>> {

    @Override
    public EntityModel<PlataformaResponseDTO> toModel(PlataformaResponseDTO modelo) {

        return EntityModel.of(
                modelo,
                // self
                linkTo(methodOn(PlataformaControllerV2.class)
                        .findById(modelo.getId()))
                        .withSelfRel(),

                // colección
                linkTo(methodOn(PlataformaControllerV2.class)
                        .findAll())
                        .withRel("plataformas"),

                // actualizar (PUT)
                linkTo(methodOn(PlataformaControllerV2.class)
                        .update(modelo.getId(), null))
                        .withRel("actualizar"),

                // actualizar parcialmente (PATCH)
                linkTo(methodOn(PlataformaControllerV2.class)
                        .patch(modelo.getId(), null))
                        .withRel("actualizar_parcial"),

                // eliminar
                linkTo(methodOn(PlataformaControllerV2.class)
                        .delete(modelo.getId()))
                        .withRel("eliminar"),

                // crear
                linkTo(methodOn(PlataformaControllerV2.class)
                        .save(null))
                        .withRel("crear")
        );
    }
}