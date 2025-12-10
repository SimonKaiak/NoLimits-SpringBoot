package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.DesarrolladorControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladorResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DesarrolladorModelAssembler implements RepresentationModelAssembler<DesarrolladorResponseDTO, EntityModel<DesarrolladorResponseDTO>> {

    @Override
    public EntityModel<DesarrolladorResponseDTO> toModel(DesarrolladorResponseDTO dto) {
        return EntityModel.of(
                dto,
                linkTo(methodOn(DesarrolladorControllerV2.class).getById(dto.getId())).withSelfRel(),
                linkTo(methodOn(DesarrolladorControllerV2.class).getAll()).withRel("desarrolladores")
        );
    }
}