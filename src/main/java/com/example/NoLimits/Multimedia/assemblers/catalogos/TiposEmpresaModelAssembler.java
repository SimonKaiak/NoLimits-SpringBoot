package com.example.NoLimits.Multimedia.assemblers.catalogos;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.EmpresaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TipoEmpresaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.catalogos.TiposEmpresaControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposEmpresaResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TiposEmpresaModelAssembler implements RepresentationModelAssembler<TiposEmpresaResponseDTO, EntityModel<TiposEmpresaResponseDTO>> {

    @Override
    public EntityModel<TiposEmpresaResponseDTO> toModel(TiposEmpresaResponseDTO rel) {

        EntityModel<TiposEmpresaResponseDTO> model = EntityModel.of(rel);

        Long empresaId = rel.getEmpresaId();
        Long tipoId = rel.getTipoEmpresaId();

        if (empresaId != null) {
            model.add(
                linkTo(methodOn(TiposEmpresaControllerV2.class).listar(empresaId))
                    .withSelfRel()
            );
            model.add(
                linkTo(methodOn(EmpresaControllerV2.class).findById(empresaId))
                    .withRel("empresa")
            );
        }

        if (tipoId != null) {
            model.add(
                linkTo(methodOn(TipoEmpresaControllerV2.class).findById(tipoId))
                    .withRel("tipo-empresa")
            );
        }

        if (empresaId != null && tipoId != null) {
            model.add(
                linkTo(methodOn(TiposEmpresaControllerV2.class).unlink(empresaId, tipoId))
                    .withRel("desvincular")
            );
        }

        return model;
    }
}