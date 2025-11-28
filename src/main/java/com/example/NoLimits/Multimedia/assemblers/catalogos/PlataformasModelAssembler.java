package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.PlataformasControllerV2;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformasModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlataformasModelAssembler
        implements RepresentationModelAssembler<PlataformasModel, EntityModel<PlataformasModel>> {

    @Override
    public EntityModel<PlataformasModel> toModel(PlataformasModel modelo) {

        Long productoId = modelo.getProducto() != null ? modelo.getProducto().getId() : null;

        return EntityModel.of(
                modelo,

                // self
                linkTo(methodOn(PlataformasControllerV2.class)
                        .listar(productoId))
                        .withSelfRel(),

                // colección
                linkTo(methodOn(PlataformasControllerV2.class)
                        .listar(productoId))
                        .withRel("producto-plataformas")
        );
    }
}