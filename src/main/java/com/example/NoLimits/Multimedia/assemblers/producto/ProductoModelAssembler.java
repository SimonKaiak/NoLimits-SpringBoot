package com.example.NoLimits.Multimedia.assemblers.producto;

import com.example.NoLimits.Multimedia.controllerV2.producto.ProductoControllerV2;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoModel, EntityModel<ProductoModel>> {

    @Override
    public EntityModel<ProductoModel> toModel(ProductoModel producto) {

        return EntityModel.of(
                producto,

                // ====================== SELF ======================
                linkTo(methodOn(ProductoControllerV2.class)
                        .getById(producto.getId()))
                        .withSelfRel(),

                // ====================== ACCIONES CRUD ======================
                linkTo(methodOn(ProductoControllerV2.class)
                        .update(producto.getId(), null))
                        .withRel("actualizar"),

                linkTo(methodOn(ProductoControllerV2.class)
                        .patch(producto.getId(), null))
                        .withRel("actualizar_parcial"),

                linkTo(methodOn(ProductoControllerV2.class)
                        .delete(producto.getId()))
                        .withRel("eliminar"),

                // ====================== NUEVO RECURSO ======================
                linkTo(methodOn(ProductoControllerV2.class)
                        .create(null))
                        .withRel("crear"),

                // ====================== LISTAS ======================
                linkTo(methodOn(ProductoControllerV2.class)
                        .getAll())
                        .withRel("productos"),

                linkTo(methodOn(ProductoControllerV2.class)
                        .obtenerResumenProductos())
                        .withRel("productos_resumen"),

                // ====================== FILTROS INTELIGENTES ======================

                // Por tipo (si existe)
                producto.getTipoProducto() != null ?
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorTipo(producto.getTipoProducto().getId()))
                                .withRel("productos_mismo_tipo")
                        : null,

                // Por clasificación (si existe)
                producto.getClasificacion() != null ?
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorClasificacion(producto.getClasificacion().getId()))
                                .withRel("productos_misma_clasificacion")
                        : null,

                // Por estado (si existe)
                producto.getEstado() != null ?
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorEstado(producto.getEstado().getId()))
                                .withRel("productos_mismo_estado")
                        : null,

                // Por tipo + estado (si ambos existen)
                (producto.getTipoProducto() != null && producto.getEstado() != null) ?
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorTipoYEstado(
                                        producto.getTipoProducto().getId(),
                                        producto.getEstado().getId()
                                ))
                                .withRel("productos_mismo_tipo_y_estado")
                        : null
        );
    }
}