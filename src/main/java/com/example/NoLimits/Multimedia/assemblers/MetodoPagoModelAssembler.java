package com.example.NoLimits.Multimedia.assemblers;

import com.example.NoLimits.Multimedia.controllerV2.MetodoPagoControllerV2;
import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MetodoPagoModelAssembler implements RepresentationModelAssembler<MetodoPagoModel, EntityModel<MetodoPagoModel>> {

    @Override
    public EntityModel<MetodoPagoModel> toModel(MetodoPagoModel metodoPago) {
        return EntityModel.of(metodoPago,
                // Enlace al recurso específico (self)
                linkTo(methodOn(MetodoPagoControllerV2.class).getById(metodoPago.getId())).withSelfRel(),
                // Enlace para actualizar completamente (PUT)
                linkTo(methodOn(MetodoPagoControllerV2.class).update(metodoPago.getId(), null)).withRel("actualizar"),
                // Enlace para actualización parcial (PATCH)
                linkTo(methodOn(MetodoPagoControllerV2.class).patch(metodoPago.getId(), null)).withRel("actualizar_parcial"),
                // Enlace para eliminar (DELETE)
                linkTo(methodOn(MetodoPagoControllerV2.class).delete(metodoPago.getId())).withRel("eliminar"),
                // Enlace a la colección de métodos de pago
                linkTo(methodOn(MetodoPagoControllerV2.class).getAll()).withRel("metodos_pago"),
                // Enlace para crear un nuevo método de pago
                linkTo(methodOn(MetodoPagoControllerV2.class).create(null)).withRel("crear")
        );
    }
}