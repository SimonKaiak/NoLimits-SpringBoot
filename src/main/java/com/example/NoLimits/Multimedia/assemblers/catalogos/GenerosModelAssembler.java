package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.GeneroControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.GenerosControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.producto.ProductoControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GenerosResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler HATEOAS para envolver GenerosResponseDTO en EntityModel,
 * agregando enlaces relacionados (self, desvincular, etc.).
 */
@Component
public class GenerosModelAssembler implements RepresentationModelAssembler<GenerosResponseDTO, EntityModel<GenerosResponseDTO>> {

    @Override
    public EntityModel<GenerosResponseDTO> toModel(GenerosResponseDTO dto) {

        // Modelo base con los datos del DTO
        EntityModel<GenerosResponseDTO> model = EntityModel.of(dto);

        // Solo generamos enlaces si tenemos productoId y generoId
        if (dto.getProductoId() != null && dto.getGeneroId() != null) {

            // Self: vínculo lógico producto–género (misma operación de vincular)
            model.add(
                    linkTo(methodOn(GenerosControllerV2.class)
                            .vincular(dto.getProductoId(), dto.getGeneroId()))
                            .withSelfRel()
            );

            // Desvincular producto–género
            model.add(
                    linkTo(methodOn(GenerosControllerV2.class)
                            .desvincular(dto.getProductoId(), dto.getGeneroId()))
                            .withRel("desvincular")
            );

            // Todas las relaciones de ese producto
            model.add(
                    linkTo(methodOn(GenerosControllerV2.class)
                            .obtenerPorProducto(dto.getProductoId()))
                            .withRel("generos_del_producto")
            );

            // Todas las relaciones de ese género
            model.add(
                    linkTo(methodOn(GenerosControllerV2.class)
                            .obtenerPorGenero(dto.getGeneroId()))
                            .withRel("productos_del_genero")
            );

            // Enlace directo al recurso Producto
            model.add(
                    linkTo(methodOn(ProductoControllerV2.class)
                            .getById(dto.getProductoId()))
                            .withRel("producto")
            );

            // Enlace directo al recurso Género
            model.add(
                    linkTo(methodOn(GeneroControllerV2.class)
                            .getById(dto.getGeneroId()))
                            .withRel("genero")
            );
        }

        return model;
    }
}