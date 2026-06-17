package com.example.NoLimits.Multimedia.assemblers.producto;

import com.example.NoLimits.Multimedia.controllerV2.producto.ProductoControllerV2;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
     * Los links opcionales (tipo, clasificación, estado, tipo+estado, saga) solo
     * se agregan si el dato existe. Antes se armaban con un operador ternario que,
     * cuando el dato era null, pasaba un Link null directo a EntityModel.of(...) —
     * y Spring HATEOAS lanza IllegalArgumentException si recibe un link null.
     * Por eso ahora se construyen en una lista y solo se agregan condicionalmente.
     *
     * @param dto DTO de producto que viene del service.
     * @return EntityModel con el DTO y sus enlaces.
     */
    @Override
    public EntityModel<ProductoResponseDTO> toModel(ProductoResponseDTO dto) {

        List<Link> links = new ArrayList<>();

        // ====================== SELF ======================
        // Enlace principal al propio recurso /api/v2/productos/{id}
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .getById(dto.getId()))
                .withSelfRel());

        // ====================== ACCIONES CRUD ======================
        // Enlace para actualizar completamente el producto (PUT)
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .update(dto.getId(), null))
                .withRel("actualizar"));

        // Enlace para actualizar parcialmente el producto (PATCH)
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .patch(dto.getId(), null))
                .withRel("actualizar_parcial"));

        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .actualizarPrecioDesdeSteam(dto.getId()))
                .withRel("actualizar_precio_steam"));

        // Enlace para eliminar el producto
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .delete(dto.getId()))
                .withRel("eliminar"));

        // ====================== NUEVO RECURSO ======================
        // Enlace genérico para crear un nuevo producto
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .create(null))
                .withRel("crear"));

        // ====================== LISTAS ======================
        // Enlace a la lista completa de productos
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .getAll())
                .withRel("productos"));

        // Enlace al resumen de productos (vista liviana)
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .obtenerResumenProductos())
                .withRel("productos_resumen"));

        // ====================== FILTROS INTELIGENTES ======================

        // Por tipo (si tengo tipoProductoId)
        if (dto.getTipoProductoId() != null) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .buscarPorTipo(dto.getTipoProductoId()))
                    .withRel("productos_mismo_tipo"));
        }

        // Por clasificación (si tengo clasificacionId)
        if (dto.getClasificacionId() != null) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .buscarPorClasificacion(dto.getClasificacionId()))
                    .withRel("productos_misma_clasificacion"));
        }

        // Por estado (si tengo estadoId)
        if (dto.getEstadoId() != null) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .buscarPorEstado(dto.getEstadoId()))
                    .withRel("productos_mismo_estado"));
        }

        // Por tipo + estado (si ambos existen)
        if (dto.getTipoProductoId() != null && dto.getEstadoId() != null) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .buscarPorTipoYEstado(
                            dto.getTipoProductoId(),
                            dto.getEstadoId()
                    ))
                    .withRel("productos_mismo_tipo_y_estado"));
        }

        // ====================== SAGAS ======================
        // Productos de la misma saga (si el producto pertenece a una)
        if (dto.getSaga() != null) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .buscarPorSaga(dto.getSaga()))
                    .withRel("productos_misma_saga"));
        }

        return EntityModel.of(dto, links);
    }
}