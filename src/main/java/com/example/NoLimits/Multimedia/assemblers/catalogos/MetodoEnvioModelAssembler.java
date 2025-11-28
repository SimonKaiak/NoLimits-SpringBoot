package com.example.NoLimits.Multimedia.assemblers.catalogos;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.MetodoEnvioControllerV2;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MetodoEnvioModelAssembler implements RepresentationModelAssembler<MetodoEnvioModel, EntityModel<MetodoEnvioModel>> {

    @Override
    public EntityModel<MetodoEnvioModel> toModel(MetodoEnvioModel metodoEnvio) {

        return EntityModel.of(
            metodoEnvio,
            // self
            linkTo(methodOn(MetodoEnvioControllerV2.class).getById(metodoEnvio.getId())).withSelfRel(),
            // colección
            linkTo(methodOn(MetodoEnvioControllerV2.class).getAll()).withRel("metodos-envio"),
            // crear
            linkTo(methodOn(MetodoEnvioControllerV2.class).create(null)).withRel("crear"),
            // actualizar (PUT)
            linkTo(methodOn(MetodoEnvioControllerV2.class).update(metodoEnvio.getId(), null)).withRel("actualizar"),
            // actualizar parcial (PATCH)
            linkTo(methodOn(MetodoEnvioControllerV2.class).patch(metodoEnvio.getId(), null)).withRel("actualizar_parcial"),
            // eliminar
            linkTo(methodOn(MetodoEnvioControllerV2.class).delete(metodoEnvio.getId())).withRel("eliminar")
        );
    }
}