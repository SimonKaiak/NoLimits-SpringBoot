package com.example.NoLimits.Multimedia.assemblers.ubicacion;

import com.example.NoLimits.Multimedia.controllerV2.ubicacion.RegionControllerV2;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.RegionResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler<RegionResponseDTO, EntityModel<RegionResponseDTO>> {

    @Override
    public EntityModel<RegionResponseDTO> toModel(RegionResponseDTO region) {

        return EntityModel.of(
                region,
                linkTo(methodOn(RegionControllerV2.class).getById(region.getId())).withSelfRel(),
                linkTo(methodOn(RegionControllerV2.class).getAll()).withRel("regiones")
        );
    }
}