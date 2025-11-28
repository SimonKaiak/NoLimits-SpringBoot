package com.example.NoLimits.Multimedia.assemblers.producto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.producto.ImagenesControllerV2;
import com.example.NoLimits.Multimedia.model.producto.ImagenesModel;

@Component
public class ImagenesModelAssembler implements RepresentationModelAssembler<ImagenesModel, EntityModel<ImagenesModel>> {

    @Override
    public EntityModel<ImagenesModel> toModel(ImagenesModel imagen) {

        EntityModel<ImagenesModel> model = EntityModel.of(
                imagen,
                // self
                linkTo(methodOn(ImagenesControllerV2.class).getById(imagen.getId())).withSelfRel(),
                // actualizar completa (PUT)
                linkTo(methodOn(ImagenesControllerV2.class).update(imagen.getId(), null)).withRel("actualizar"),
                // actualizar parcial (PATCH)
                linkTo(methodOn(ImagenesControllerV2.class).patch(imagen.getId(), null)).withRel("actualizar_parcial"),
                // eliminar (DELETE)
                linkTo(methodOn(ImagenesControllerV2.class).delete(imagen.getId())).withRel("eliminar"),
                // colección
                linkTo(methodOn(ImagenesControllerV2.class).getAll()).withRel("imagenes"),
                // crear
                linkTo(methodOn(ImagenesControllerV2.class).create(null)).withRel("crear")
        );

        // Si la imagen tiene producto, agregamos link al recurso por producto
        if (imagen.getProducto() != null && imagen.getProducto().getId() != null) {
            model.add(
                linkTo(methodOn(ImagenesControllerV2.class)
                        .getByProducto(imagen.getProducto().getId()))
                        .withRel("imagenes_del_producto")
            );
            model.add(
                linkTo(methodOn(ImagenesControllerV2.class)
                        .deleteByProducto(imagen.getProducto().getId()))
                        .withRel("eliminar_todas_del_producto")
            );
        }

        return model;
    }
}