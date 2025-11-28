package com.example.NoLimits.Multimedia.assemblers.catalogos;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.EmpresaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TipoEmpresaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TiposEmpresaControllerV2;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TiposEmpresaModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TiposEmpresaModelAssembler implements RepresentationModelAssembler<TiposEmpresaModel, EntityModel<TiposEmpresaModel>> {

    @Override
    public EntityModel<TiposEmpresaModel> toModel(TiposEmpresaModel rel) {
        EntityModel<TiposEmpresaModel> model = EntityModel.of(rel);

        EmpresaModel empresa = rel.getEmpresa();
        TipoEmpresaModel tipoEmpresa = rel.getTipoEmpresa();

        Long empresaId = empresa != null ? empresa.getId() : null;
        Long tipoId = tipoEmpresa != null ? tipoEmpresa.getId() : null;

        // self: usamos la lista de tipos de una empresa como referencia
        if (empresaId != null) {
            model.add(
                    linkTo(methodOn(TiposEmpresaControllerV2.class).listar(empresaId))
                            .withSelfRel()
            );
        }

        // Link a la Empresa
        if (empresaId != null) {
            model.add(
                    linkTo(methodOn(EmpresaControllerV2.class).findById(empresaId))
                            .withRel("empresa")
            );
        }

        // Link al TipoEmpresa
        if (tipoId != null) {
            model.add(
                    linkTo(methodOn(TipoEmpresaControllerV2.class).findById(tipoId))
                            .withRel("tipo-empresa")
            );
        }

        // Link para desvincular Empresa ↔ TipoEmpresa
        if (empresaId != null && tipoId != null) {
            model.add(
                    linkTo(methodOn(TiposEmpresaControllerV2.class).unlink(empresaId, tipoId))
                            .withRel("desvincular")
            );
        }

        return model;
    }
}