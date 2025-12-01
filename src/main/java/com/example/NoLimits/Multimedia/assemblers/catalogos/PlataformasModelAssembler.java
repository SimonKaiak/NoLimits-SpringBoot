package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.PlataformasControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformasResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlataformasModelAssembler
        implements RepresentationModelAssembler<PlataformasResponseDTO, EntityModel<PlataformasResponseDTO>> {

    @Override
    public EntityModel<PlataformasResponseDTO> toModel(PlataformasResponseDTO modelo) {

        Long productoId = modelo.getProductoId();

        return EntityModel.of(
                modelo,

                // self → usamos la colección del producto como referencia
                linkTo(methodOn(PlataformasControllerV2.class)
                        .listar(productoId))
                        .withSelfRel(),

                // colección
                linkTo(methodOn(PlataformasControllerV2.class)
                        .listar(productoId))
                        .withRel("producto-plataformas")
        );
    }
}