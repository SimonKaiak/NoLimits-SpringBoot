package com.example.NoLimits.Multimedia.assemblers.producto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.producto.DetalleVentaControllerV2;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DetalleVentaModelAssembler implements RepresentationModelAssembler<DetalleVentaResponseDTO, EntityModel<DetalleVentaResponseDTO>> {

    @Override
    public EntityModel<DetalleVentaResponseDTO> toModel(DetalleVentaResponseDTO detalle) {

        EntityModel<DetalleVentaResponseDTO> entityModel = EntityModel.of(
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

        return entityModel;
    }
}