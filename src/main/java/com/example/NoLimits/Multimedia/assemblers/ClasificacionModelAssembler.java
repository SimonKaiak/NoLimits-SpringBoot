package com.example.NoLimits.Multimedia.assemblers;

import com.example.NoLimits.Multimedia.controllerV2.ClasificacionControllerV2;
import com.example.NoLimits.Multimedia.model.ClasificacionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClasificacionModelAssembler implements RepresentationModelAssembler<ClasificacionModel, EntityModel<ClasificacionModel>> {

    @Override
    public EntityModel<ClasificacionModel> toModel(ClasificacionModel clasificacion) {

        EntityModel<ClasificacionModel> model = EntityModel.of(
                clasificacion,

                // SELF
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .getById(clasificacion.getId()))
                        .withSelfRel(),

                // CRUD
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .update(clasificacion.getId(), null))
                        .withRel("actualizar"),

                linkTo(methodOn(ClasificacionControllerV2.class)
                        .patch(clasificacion.getId(), null))
                        .withRel("actualizar_parcial"),

                linkTo(methodOn(ClasificacionControllerV2.class)
                        .delete(clasificacion.getId()))
                        .withRel("eliminar"),

                // Colección
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .getAll())
                        .withRel("clasificaciones"),

                // Crear nueva
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .create(null))
                        .withRel("crear"),

                // Listas por estado
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .getAll())
                        .withRel("listar_todas"),

                linkTo(methodOn(ClasificacionControllerV2.class)
                        .getAll())
                        .withRel("self_collection") // alias de colección, por si quieres consumirlo así
        );

        // Enlaces condicionales según datos

        // Si tiene nombre, agregamos link para búsqueda por nombre exacto
        if (clasificacion.getNombre() != null && !clasificacion.getNombre().isBlank()) {
            model.add(
                    linkTo(methodOn(ClasificacionControllerV2.class)
                            .getAll()) // en V2 no hicimos endpoint directo por nombre, eso está en v1
                            .withRel("relacionada_por_nombre")
            );
        }

        return model;
    }
}