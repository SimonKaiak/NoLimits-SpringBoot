package com.example.NoLimits.Multimedia.assemblers.catalogos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.TipoProductoControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoProductoResponseDTO;

@Component
public class TipoProductoModelAssembler implements RepresentationModelAssembler<TipoProductoResponseDTO, EntityModel<TipoProductoResponseDTO>> {

    @Override
    public EntityModel<TipoProductoResponseDTO> toModel(TipoProductoResponseDTO tipoProducto) {

        EntityModel<TipoProductoResponseDTO> model = EntityModel.of(
                tipoProducto,
                // Enlace a la colección completa
                linkTo(methodOn(TipoProductoControllerV2.class).getAll()).withRel("tipos_producto"),
                // Enlace para crear un nuevo tipo de producto
                linkTo(methodOn(TipoProductoControllerV2.class).create(null)).withRel("crear")
        );

        // Solo agregamos enlaces que dependen del ID si el ID no es null
        if (tipoProducto.getId() != null) {
            model.add(
                    // Enlace al recurso específico (self)
                    linkTo(methodOn(TipoProductoControllerV2.class).getById(tipoProducto.getId()))
                            .withSelfRel(),

                    // Enlace para actualizar completamente (PUT)
                    linkTo(methodOn(TipoProductoControllerV2.class).update(tipoProducto.getId(), null))
                            .withRel("actualizar"),

                    // Enlace para actualización parcial (PATCH)
                    linkTo(methodOn(TipoProductoControllerV2.class).patch(tipoProducto.getId(), null))
                            .withRel("actualizar_parcial"),

                    // Enlace para eliminar (DELETE)
                    linkTo(methodOn(TipoProductoControllerV2.class).delete(tipoProducto.getId()))
                            .withRel("eliminar")
            );
        }

        return model;
    }
}