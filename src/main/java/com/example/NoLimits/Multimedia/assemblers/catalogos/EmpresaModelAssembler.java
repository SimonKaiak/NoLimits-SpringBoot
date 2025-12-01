package com.example.NoLimits.Multimedia.assemblers.catalogos;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.EmpresaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TiposEmpresaControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresaResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmpresaModelAssembler implements RepresentationModelAssembler<EmpresaResponseDTO, EntityModel<EmpresaResponseDTO>> {

    @Override
    public EntityModel<EmpresaResponseDTO> toModel(EmpresaResponseDTO empresa) {

        EntityModel<EmpresaResponseDTO> model = EntityModel.of(empresa);

        Long id = empresa.getId();

        if (id != null) {

            model.add(
                    linkTo(methodOn(EmpresaControllerV2.class).findById(id))
                            .withSelfRel()
            );

            model.add(
                    linkTo(methodOn(EmpresaControllerV2.class).findAll())
                            .withRel("empresas")
            );

            model.add(
                    linkTo(methodOn(TiposEmpresaControllerV2.class).listar(id))
                            .withRel("tipos-empresa")
            );
        }

        return model;
    }
}