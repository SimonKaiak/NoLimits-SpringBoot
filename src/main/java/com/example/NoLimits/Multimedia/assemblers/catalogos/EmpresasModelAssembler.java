package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.EmpresasControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresasResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmpresasModelAssembler
        implements RepresentationModelAssembler<EmpresasResponseDTO, EntityModel<EmpresasResponseDTO>> {

    @Override
    public EntityModel<EmpresasResponseDTO> toModel(EmpresasResponseDTO dto) {

        return EntityModel.of(dto,

            linkTo(methodOn(EmpresasControllerV2.class)
                    .listar(dto.getProductoId()))
                    .withRel("producto-empresas"),

            linkTo(methodOn(EmpresasControllerV2.class)
                    .unlink(dto.getProductoId(), dto.getEmpresaId()))
                    .withRel("desvincular")
        );
    }
}