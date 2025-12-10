package com.example.NoLimits.Multimedia.assemblers.catalogos;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.TipoEmpresaControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoEmpresaResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TipoEmpresaModelAssembler
        implements RepresentationModelAssembler<TipoEmpresaResponseDTO, EntityModel<TipoEmpresaResponseDTO>> {

    @Override
    public EntityModel<TipoEmpresaResponseDTO> toModel(TipoEmpresaResponseDTO tipo) {

        EntityModel<TipoEmpresaResponseDTO> model = EntityModel.of(tipo);

        if (tipo.getId() != null) {
            model.add(
                linkTo(methodOn(TipoEmpresaControllerV2.class).findById(tipo.getId()))
                    .withSelfRel()
            );

            model.add(
                linkTo(methodOn(TipoEmpresaControllerV2.class).findAll())
                    .withRel("tipos-empresa")
            );
        }

        return model;
    }
}