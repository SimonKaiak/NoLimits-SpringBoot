package com.example.NoLimits.Multimedia.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.EmpresaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.TiposEmpresaControllerV2;
import com.example.NoLimits.Multimedia.model.EmpresaModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmpresaModelAssembler implements RepresentationModelAssembler<EmpresaModel, EntityModel<EmpresaModel>> {

    @Override
    public EntityModel<EmpresaModel> toModel(EmpresaModel empresa) {
        EntityModel<EmpresaModel> model = EntityModel.of(empresa);

        Long id = empresa.getId();

        // self: /api/v2/empresas/{id}
        if (id != null) {
            model.add(
                    linkTo(methodOn(EmpresaControllerV2.class).findById(id))
                            .withSelfRel()
            );

            // colección: /api/v2/empresas
            model.add(
                    linkTo(methodOn(EmpresaControllerV2.class).findAll())
                            .withRel("empresas")
            );

            // tipos de empresa asociados: /api/v2/empresas/{id}/tipos-empresa
            model.add(
                    linkTo(methodOn(TiposEmpresaControllerV2.class).listar(id))
                            .withRel("tipos-empresa")
            );
        }

        return model;
    }
}