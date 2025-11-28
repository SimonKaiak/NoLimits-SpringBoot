package com.example.NoLimits.Multimedia.assemblers.catalogos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.EstadoControllerV2;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;

@Component
public class EstadoModelAssembler implements RepresentationModelAssembler<EstadoModel, EntityModel<EstadoModel>> {

    @Override
    public EntityModel<EstadoModel> toModel(EstadoModel estado) {
        return EntityModel.of(
                estado,
                // Self
                linkTo(methodOn(EstadoControllerV2.class).getById(estado.getId())).withSelfRel(),

                // Operaciones sobre este recurso
                linkTo(methodOn(EstadoControllerV2.class).update(estado.getId(), null))
                        .withRel("actualizar"),
                linkTo(methodOn(EstadoControllerV2.class).patch(estado.getId(), null))
                        .withRel("actualizar_parcial"),
                linkTo(methodOn(EstadoControllerV2.class).delete(estado.getId()))
                        .withRel("eliminar"),

                // Colección
                linkTo(methodOn(EstadoControllerV2.class).getAll())
                        .withRel("estados"),

                // Crear nuevo
                linkTo(methodOn(EstadoControllerV2.class).create(null))
                        .withRel("crear"),

                // Extras útiles
                linkTo(methodOn(EstadoControllerV2.class).listarActivos())
                        .withRel("activos"),
                linkTo(methodOn(EstadoControllerV2.class).listarInactivos())
                        .withRel("inactivos")
        );
    }
}