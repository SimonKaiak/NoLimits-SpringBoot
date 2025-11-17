package com.example.NoLimits.Multimedia.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.example.NoLimits.Multimedia.controllerV2.VentaControllerV2;
import com.example.NoLimits.Multimedia.model.VentaModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<VentaModel, EntityModel<VentaModel>> {

    @Override
    public EntityModel<VentaModel> toModel(VentaModel venta) {
        return EntityModel.of(venta,
                linkTo(methodOn(VentaControllerV2.class).getById(venta.getId())).withSelfRel(),
                linkTo(methodOn(VentaControllerV2.class).getAll()).withRel("ventas"),
                linkTo(methodOn(VentaControllerV2.class).create(null)).withRel("crear"),
                linkTo(methodOn(VentaControllerV2.class).update(venta.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(VentaControllerV2.class).patch(venta.getId(), null)).withRel("actualizar_parcial"),
                linkTo(methodOn(VentaControllerV2.class).delete(venta.getId())).withRel("eliminar"),
                // opcional: atajo por método de pago del propio recurso
                (venta.getMetodoPagoModel()!=null && venta.getMetodoPagoModel().getId()!=null)
                    ? linkTo(methodOn(VentaControllerV2.class).byMetodoPago(venta.getMetodoPagoModel().getId())).withRel("por-metodo-pago")
                    : null
        );
    }
}