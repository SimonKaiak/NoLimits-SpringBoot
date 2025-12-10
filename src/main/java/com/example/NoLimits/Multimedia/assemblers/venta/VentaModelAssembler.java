package com.example.NoLimits.Multimedia.assemblers.venta;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.producto.DetalleVentaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.venta.VentaControllerV2;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<VentaResponseDTO, EntityModel<VentaResponseDTO>> {

    @Override
    public EntityModel<VentaResponseDTO> toModel(VentaResponseDTO venta) {

        EntityModel<VentaResponseDTO> entityModel = EntityModel.of(
            venta,
            // self
            linkTo(methodOn(VentaControllerV2.class).getById(venta.getId())).withSelfRel(),
            // colección
            linkTo(methodOn(VentaControllerV2.class).getAll()).withRel("ventas"),
            // crear
            linkTo(methodOn(VentaControllerV2.class).create(null)).withRel("crear"),
            // actualizar (PUT)
            linkTo(methodOn(VentaControllerV2.class).update(venta.getId(), null)).withRel("actualizar"),
            // actualizar parcial (PATCH)
            linkTo(methodOn(VentaControllerV2.class).patch(venta.getId(), null)).withRel("actualizar_parcial"),
            // eliminar
            linkTo(methodOn(VentaControllerV2.class).delete(venta.getId())).withRel("eliminar")
        );

        // Link a ventas filtradas por el mismo método de pago (si existe)
        if (venta.getMetodoPagoId() != null) {
            entityModel.add(
                linkTo(methodOn(VentaControllerV2.class)
                        .byMetodoPago(venta.getMetodoPagoId()))
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