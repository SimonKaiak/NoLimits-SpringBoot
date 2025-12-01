package com.example.NoLimits.Multimedia.assemblers.catalogos;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.EstadoControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EstadoModelAssembler implements RepresentationModelAssembler<EstadoResponseDTO, EntityModel<EstadoResponseDTO>> {

    @Override
    public EntityModel<EstadoResponseDTO> toModel(EstadoResponseDTO estado) {

        return EntityModel.of(
                estado,

                // 🔹 self
                linkTo(methodOn(EstadoControllerV2.class)
                        .getById(estado.getId()))
                        .withSelfRel(),

                // 🔹 colección
                linkTo(methodOn(EstadoControllerV2.class)
                        .getAll())
                        .withRel("estados"),

                // 🔹 acciones CRUD
                linkTo(methodOn(EstadoControllerV2.class)
                        .update(estado.getId(), null))
                        .withRel("actualizar"),

                linkTo(methodOn(EstadoControllerV2.class)
                        .patch(estado.getId(), null))
                        .withRel("actualizar_parcial"),

                linkTo(methodOn(EstadoControllerV2.class)
                        .delete(estado.getId()))
                        .withRel("eliminar")
        );
    }
}