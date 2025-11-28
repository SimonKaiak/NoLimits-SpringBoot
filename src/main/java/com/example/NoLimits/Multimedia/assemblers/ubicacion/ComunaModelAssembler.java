package com.example.NoLimits.Multimedia.assemblers.ubicacion;

import com.example.NoLimits.Multimedia.controllerV2.ubicacion.ComunaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.ubicacion.RegionControllerV2;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<ComunaModel, EntityModel<ComunaModel>> {

    @Override
    public EntityModel<ComunaModel> toModel(ComunaModel comuna) {

        EntityModel<ComunaModel> model = EntityModel.of(
                comuna,
                linkTo(methodOn(ComunaControllerV2.class).getById(comuna.getId())).withSelfRel(),
                linkTo(methodOn(ComunaControllerV2.class).getAll()).withRel("comunas")
        );

        // Link a la región de la comuna (si existe)
        if (comuna.getRegion() != null && comuna.getRegion().getId() != null) {
            model.add(
                    linkTo(methodOn(RegionControllerV2.class).getById(comuna.getRegion().getId()))
                            .withRel("region")
            );
        }

        return model;
    }
}