package com.example.NoLimits.Multimedia.assemblers.catalogos;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.MetodoEnvioControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MetodoEnvioModelAssembler implements RepresentationModelAssembler<MetodoEnvioResponseDTO, EntityModel<MetodoEnvioResponseDTO>> {

    @Override
    public EntityModel<MetodoEnvioResponseDTO> toModel(MetodoEnvioResponseDTO metodoEnvio) {

        Long id = metodoEnvio.getId();

        return EntityModel.of(
            metodoEnvio,
            linkTo(methodOn(MetodoEnvioControllerV2.class).getById(id)).withSelfRel(),
            linkTo(methodOn(MetodoEnvioControllerV2.class).getAll()).withRel("metodos-envio"),
            linkTo(methodOn(MetodoEnvioControllerV2.class).create(null)).withRel("crear"),
            linkTo(methodOn(MetodoEnvioControllerV2.class).update(id, null)).withRel("actualizar"),
            linkTo(methodOn(MetodoEnvioControllerV2.class).patch(id, null)).withRel("actualizar_parcial"),
            linkTo(methodOn(MetodoEnvioControllerV2.class).delete(id)).withRel("eliminar")
        );
    }
}