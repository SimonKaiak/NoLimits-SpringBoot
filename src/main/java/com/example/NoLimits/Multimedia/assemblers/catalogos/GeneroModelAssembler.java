package com.example.NoLimits.Multimedia.assemblers.catalogos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.GeneroControllerV2;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;

@Component
public class GeneroModelAssembler implements RepresentationModelAssembler<GeneroModel, EntityModel<GeneroModel>> {

    @Override
    public EntityModel<GeneroModel> toModel(GeneroModel genero) {
        return EntityModel.of(
                genero,
                // self
                linkTo(methodOn(GeneroControllerV2.class).getById(genero.getId())).withSelfRel(),
                // actualizar (PUT)
                linkTo(methodOn(GeneroControllerV2.class).update(genero.getId(), null)).withRel("actualizar"),
                // actualizar parcialmente (PATCH)
                linkTo(methodOn(GeneroControllerV2.class).patch(genero.getId(), null)).withRel("actualizar_parcial"),
                // eliminar
                linkTo(methodOn(GeneroControllerV2.class).delete(genero.getId())).withRel("eliminar"),
                // lista completa
                linkTo(methodOn(GeneroControllerV2.class).getAll()).withRel("generos"),
                // crear
                linkTo(methodOn(GeneroControllerV2.class).create(null)).withRel("crear")
        );
    }
}