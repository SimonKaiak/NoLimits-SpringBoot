package com.example.NoLimits.Multimedia.assemblers.usuario;

import com.example.NoLimits.Multimedia.controllerV2.usuario.UsuarioControllerV2;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioResponseDTO, EntityModel<UsuarioResponseDTO>> {

    @Override
    public EntityModel<UsuarioResponseDTO> toModel(UsuarioResponseDTO dto) {

        return EntityModel.of(
            dto,
            linkTo(methodOn(UsuarioControllerV2.class).getById(dto.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioControllerV2.class).update(dto.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(UsuarioControllerV2.class).patch(dto.getId(), null)).withRel("actualizar_parcial"),
            linkTo(methodOn(UsuarioControllerV2.class).delete(dto.getId())).withRel("eliminar"),
            linkTo(methodOn(UsuarioControllerV2.class).getAll()).withRel("usuarios"),
            linkTo(UsuarioControllerV2.class).withRel("crear")
        );
    }
}