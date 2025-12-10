package com.example.NoLimits.Multimedia.assemblers.producto;

import com.example.NoLimits.Multimedia.controllerV2.producto.ProductoControllerV2;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler HATEOAS para el recurso ProductoResponseDTO.
 *
 * Acá envuelvo el DTO de respuesta del producto dentro de un EntityModel
 * y le agrego todos los enlaces relacionados (self, actualizar, eliminar, filtros, etc.).
 *
 * La idea es centralizar la construcción de enlaces para que el controller
 * solo devuelva modelos ya "enriquecidos" con HATEOAS.
 */
@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoResponseDTO, EntityModel<ProductoResponseDTO>> {

    /**
     * Construye el EntityModel<ProductoResponseDTO> con todos los links HATEOAS
     * asociados a un producto en particular.
     *
     * @param dto DTO de producto que viene del service.
     * @return EntityModel con el DTO y sus enlaces.
     */
    @Override
    public EntityModel<ProductoResponseDTO> toModel(ProductoResponseDTO dto) {

        return EntityModel.of(
                dto,

                // ====================== SELF ======================
                // Enlace principal al propio recurso /api/v2/productos/{id}
                linkTo(methodOn(ProductoControllerV2.class)
                        .getById(dto.getId()))
                        .withSelfRel(),

                // ====================== ACCIONES CRUD ======================
                // Enlace para actualizar completamente el producto (PUT)
                linkTo(methodOn(ProductoControllerV2.class)
                        .update(dto.getId(), null))
                        .withRel("actualizar"),

                // Enlace para actualizar parcialmente el producto (PATCH)
                linkTo(methodOn(ProductoControllerV2.class)
                        .patch(dto.getId(), null))
                        .withRel("actualizar_parcial"),

                // Enlace para eliminar el producto
                linkTo(methodOn(ProductoControllerV2.class)
                        .delete(dto.getId()))
                        .withRel("eliminar"),

                // ====================== NUEVO RECURSO ======================
                // Enlace genérico para crear un nuevo producto
                linkTo(methodOn(ProductoControllerV2.class)
                        .create(null))
                        .withRel("crear"),

                // ====================== LISTAS ======================
                // Enlace a la lista completa de productos
                linkTo(methodOn(ProductoControllerV2.class)
                        .getAll())
                        .withRel("productos"),

                // Enlace al resumen de productos (vista liviana)
                linkTo(methodOn(ProductoControllerV2.class)
                        .obtenerResumenProductos())
                        .withRel("productos_resumen"),

                // ====================== FILTROS INTELIGENTES ======================

                // Por tipo (si tengo tipoProductoId)
                dto.getTipoProductoId() != null ?
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorTipo(dto.getTipoProductoId()))
                                .withRel("productos_mismo_tipo")
                        : null,

                // Por clasificación (si tengo clasificacionId)
                dto.getClasificacionId() != null ?
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorClasificacion(dto.getClasificacionId()))
                                .withRel("productos_misma_clasificacion")
                        : null,

                // Por estado (si tengo estadoId)
                dto.getEstadoId() != null ?
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorEstado(dto.getEstadoId()))
                                .withRel("productos_mismo_estado")
                        : null,

                // Por tipo + estado (si ambos existen)
                (dto.getTipoProductoId() != null && dto.getEstadoId() != null) ?
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorTipoYEstado(
                                        dto.getTipoProductoId(),
                                        dto.getEstadoId()
                                ))
                                .withRel("productos_mismo_tipo_y_estado")
                        : null,

                // ====================== SAGAS ======================
                // Productos de la misma saga (si el producto pertenece a una)
                dto.getSaga() != null ?
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorSaga(dto.getSaga()))
                                .withRel("productos_misma_saga")
                        : null
        );
    }
}