package com.example.NoLimits.Multimedia.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.GeneroControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.GenerosControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.ProductoControllerV2;
import com.example.NoLimits.Multimedia.model.GenerosModel;

@Component
public class GenerosModelAssembler implements RepresentationModelAssembler<GenerosModel, EntityModel<GenerosModel>> {

    @Override
    public EntityModel<GenerosModel> toModel(GenerosModel rel) {

        EntityModel<GenerosModel> model = EntityModel.of(rel,
                // No tenemos endpoint getById(relId) en GenerosControllerV2, así que el self será el vínculo lógico
                // producto-genero (POST/DELETE path):
                linkTo(methodOn(GenerosControllerV2.class)
                        .vincular(rel.getProducto().getId(), rel.getGenero().getId()))
                        .withSelfRel(),

                // Desvincular
                linkTo(methodOn(GenerosControllerV2.class)
                        .desvincular(rel.getProducto().getId(), rel.getGenero().getId()))
                        .withRel("desvincular"),

                // Ver todas las relaciones de ese producto
                linkTo(methodOn(GenerosControllerV2.class)
                        .obtenerPorProducto(rel.getProducto().getId()))
                        .withRel("generos_del_producto"),

                // Ver todas las relaciones de ese género
                linkTo(methodOn(GenerosControllerV2.class)
                        .obtenerPorGenero(rel.getGenero().getId()))
                        .withRel("productos_del_genero"),

                // Resumen filtrable
                linkTo(methodOn(GenerosControllerV2.class)
                        .resumen(rel.getProducto().getId(), rel.getGenero().getId()))
                        .withRel("resumen")
        );

        // Enlaces directos al recurso Producto y Género (si existen IDs)
        if (rel.getProducto() != null && rel.getProducto().getId() != null) {
            model.add(
                linkTo(methodOn(ProductoControllerV2.class)
                        .getById(rel.getProducto().getId()))
                        .withRel("producto")
            );
        }

        if (rel.getGenero() != null && rel.getGenero().getId() != null) {
            model.add(
                linkTo(methodOn(GeneroControllerV2.class)
                        .getById(rel.getGenero().getId()))
                        .withRel("genero")
            );
        }

        return model;
    }
}