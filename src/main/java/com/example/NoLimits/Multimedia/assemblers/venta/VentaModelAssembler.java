package com.example.NoLimits.Multimedia.assemblers.venta;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.producto.DetalleVentaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.venta.VentaControllerV2;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<VentaModel, EntityModel<VentaModel>> {

    @Override
    public EntityModel<VentaModel> toModel(VentaModel venta) {

        EntityModel<VentaModel> entityModel = EntityModel.of(
            venta,
            // self
            linkTo(methodOn(VentaControllerV2.class).getById(venta.getId())).withSelfRel(),
            // colección
            linkTo(methodOn(VentaControllerV2.class).getAll()).withRel("ventas"),
            // crear
            linkTo(methodOn(VentaControllerV2.class).create(null)).withRel("crear"),
            // actualizar (PUT)
            linkTo(methodOn(VentaControllerV2.class).update(venta.getId(), null)).withRel("actualizar"),
            // actualizar parcial (PATCH) – ojo: usa patchVentaModel en el controller
            linkTo(methodOn(VentaControllerV2.class).patch(venta.getId(), null)).withRel("actualizar_parcial"),
            // eliminar
            linkTo(methodOn(VentaControllerV2.class).delete(venta.getId())).withRel("eliminar")
        );

        // Link a ventas filtradas por el mismo método de pago (si existe)
        if (venta.getMetodoPagoModel() != null && venta.getMetodoPagoModel().getId() != null) {
            entityModel.add(
                linkTo(methodOn(VentaControllerV2.class)
                        .byMetodoPago(venta.getMetodoPagoModel().getId()))
                    .withRel("por-metodo-pago")
            );
        }

        // Link a los detalles de esta venta
        entityModel.add(
            linkTo(methodOn(DetalleVentaControllerV2.class)
                    .getByVenta(venta.getId()))
                .withRel("detalles-venta")
        );

        return entityModel;
    }
}