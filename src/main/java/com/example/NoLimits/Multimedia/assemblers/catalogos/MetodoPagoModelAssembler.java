package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.MetodoPagoControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoPagoResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MetodoPagoModelAssembler implements RepresentationModelAssembler<MetodoPagoResponseDTO, EntityModel<MetodoPagoResponseDTO>> {

    @Override
    public EntityModel<MetodoPagoResponseDTO> toModel(MetodoPagoResponseDTO metodoPago) {
        return EntityModel.of(
                metodoPago,
                // Self
                linkTo(methodOn(MetodoPagoControllerV2.class)
                        .getById(metodoPago.getId()))
                        .withSelfRel(),

                // Update completo
                linkTo(methodOn(MetodoPagoControllerV2.class)
                        .update(metodoPago.getId(), null))
                        .withRel("actualizar"),

                // Patch
                linkTo(methodOn(MetodoPagoControllerV2.class)
                        .patch(metodoPago.getId(), null))
                        .withRel("actualizar_parcial"),

                // Delete
                linkTo(methodOn(MetodoPagoControllerV2.class)
                        .delete(metodoPago.getId()))
                        .withRel("eliminar"),

                // Colección
                linkTo(methodOn(MetodoPagoControllerV2.class)
                        .getAll())
                        .withRel("metodos_pago"),

                // Crear
                linkTo(methodOn(MetodoPagoControllerV2.class)
                        .create(null))
                        .withRel("crear")
        );
    }
}