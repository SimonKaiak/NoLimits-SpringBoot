package com.example.NoLimits.Multimedia.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.UsuarioControllerV2;
import com.example.NoLimits.Multimedia.model.UsuarioModel;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioModel, EntityModel<UsuarioModel>> {

    @Override
    public EntityModel<UsuarioModel> toModel(UsuarioModel usuario) {
        // Ocultar contraseña en las respuestas
        usuario.setPassword("********");

        return EntityModel.of(usuario,
                // Enlace al recurso específico (self)
                linkTo(methodOn(UsuarioControllerV2.class).getById(usuario.getId())).withSelfRel(),
                // Enlace para actualizar (PUT)
                linkTo(methodOn(UsuarioControllerV2.class).update(usuario.getId(), null)).withRel("actualizar"),
                // Enlace para actualizar parcialmente (PATCH)
                linkTo(methodOn(UsuarioControllerV2.class).patch(usuario.getId(), null)).withRel("actualizar_parcial"),
                // Enlace para eliminar (DELETE)
                linkTo(methodOn(UsuarioControllerV2.class).delete(usuario.getId())).withRel("eliminar"),
                // Enlace a la colección de usuarios
                linkTo(methodOn(UsuarioControllerV2.class).getAll()).withRel("usuarios"),
                // Enlace para crear (apunta a la raíz de usuarios, POST)
                linkTo(UsuarioControllerV2.class).withRel("crear")
        );
    }
}
