package com.example.NoLimits.Multimedia.assemblers.ubicacion;

import com.example.NoLimits.Multimedia.controllerV2.ubicacion.ComunaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.ubicacion.DireccionControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.ubicacion.RegionControllerV2;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DireccionModelAssembler implements RepresentationModelAssembler<DireccionModel, EntityModel<DireccionModel>> {

    @Override
    public EntityModel<DireccionModel> toModel(DireccionModel direccion) {

        EntityModel<DireccionModel> model = EntityModel.of(
                direccion,
                linkTo(methodOn(DireccionControllerV2.class).getById(direccion.getId())).withSelfRel(),
                linkTo(methodOn(DireccionControllerV2.class).getAll()).withRel("direcciones")
        );

        // Link a la comuna
        if (direccion.getComuna() != null && direccion.getComuna().getId() != null) {
            model.add(
                    linkTo(methodOn(ComunaControllerV2.class).getById(direccion.getComuna().getId()))
                            .withRel("comuna")
            );

            // Link a la región (a través de la comuna)
            if (direccion.getComuna().getRegion() != null &&
                direccion.getComuna().getRegion().getId() != null) {

                model.add(
                        linkTo(methodOn(RegionControllerV2.class)
                                .getById(direccion.getComuna().getRegion().getId()))
                                .withRel("region")
                );
            }
        }

        return model;
    }
}