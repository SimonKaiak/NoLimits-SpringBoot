package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.PlataformaControllerV2;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlataformaModelAssembler
        implements RepresentationModelAssembler<PlataformaModel, EntityModel<PlataformaModel>> {

    @Override
    public EntityModel<PlataformaModel> toModel(PlataformaModel modelo) {

        return EntityModel.of(
                modelo,
                linkTo(methodOn(PlataformaControllerV2.class)
                        .findById(modelo.getId()))
                        .withSelfRel(),

                linkTo(methodOn(PlataformaControllerV2.class)
                        .findAll())
                        .withRel("plataformas")
        );
    }
}