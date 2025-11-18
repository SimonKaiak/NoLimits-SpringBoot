package com.example.NoLimits.Multimedia.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.TipoEmpresaControllerV2;
import com.example.NoLimits.Multimedia.model.TipoEmpresaModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TipoEmpresaModelAssembler implements RepresentationModelAssembler<TipoEmpresaModel, EntityModel<TipoEmpresaModel>> {

    @Override
    public EntityModel<TipoEmpresaModel> toModel(TipoEmpresaModel tipo) {
        EntityModel<TipoEmpresaModel> model = EntityModel.of(tipo);

        Long id = tipo.getId();

        if (id != null) {
            // self: /api/v2/tipos-empresa/{id}
            model.add(
                    linkTo(methodOn(TipoEmpresaControllerV2.class).findById(id))
                            .withSelfRel()
            );

            // colección: /api/v2/tipos-empresa
            model.add(
                    linkTo(methodOn(TipoEmpresaControllerV2.class).findAll())
                            .withRel("tipos-empresa")
            );
        }

        return model;
    }
}