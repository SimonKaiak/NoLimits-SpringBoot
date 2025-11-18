package com.example.NoLimits.Multimedia.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.DetalleVentaControllerV2;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DetalleVentaModelAssembler implements RepresentationModelAssembler<DetalleVentaModel, EntityModel<DetalleVentaModel>> {

    @Override
    public EntityModel<DetalleVentaModel> toModel(DetalleVentaModel detalle) {

        EntityModel<DetalleVentaModel> entityModel = EntityModel.of(
            detalle,
            // self
            linkTo(methodOn(DetalleVentaControllerV2.class).getById(detalle.getId())).withSelfRel(),
            // colección
            linkTo(methodOn(DetalleVentaControllerV2.class).getAll()).withRel("detalles-venta"),
            // crear
            linkTo(methodOn(DetalleVentaControllerV2.class).create(null)).withRel("crear"),
            // actualizar parcial
            linkTo(methodOn(DetalleVentaControllerV2.class).patch(detalle.getId(), null)).withRel("actualizar_parcial"),
            // eliminar
            linkTo(methodOn(DetalleVentaControllerV2.class).delete(detalle.getId())).withRel("eliminar")
        );

        // link a los detalles de la venta a la que pertenece
        if (detalle.getVenta() != null && detalle.getVenta().getId() != null) {
            entityModel.add(
                linkTo(methodOn(DetalleVentaControllerV2.class)
                        .getByVenta(detalle.getVenta().getId()))
                    .withRel("detalles-de-esta-venta")
            );
        }

        return entityModel;
    }
}