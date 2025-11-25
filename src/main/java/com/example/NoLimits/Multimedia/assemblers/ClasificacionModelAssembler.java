package com.example.NoLimits.Multimedia.assemblers;

import com.example.NoLimits.Multimedia.controllerV2.ClasificacionControllerV2;
import com.example.NoLimits.Multimedia.model.ClasificacionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler encargado de transformar un objeto ClasificacionModel
 * en una representación HATEOAS enriquecida con enlaces dinámicos.
 *
 * Implementa RepresentationModelAssembler para incluir links que
 * guían al cliente a través de las posibles acciones disponibles,
 * siguiendo el principio HATEOAS (Hypermedia As The Engine Of Application State).
 */
@Component
public class ClasificacionModelAssembler implements RepresentationModelAssembler<ClasificacionModel, EntityModel<ClasificacionModel>> {

    /**
     * Convierte un objeto ClasificacionModel en un EntityModel con enlaces HATEOAS.
     *
     * @param clasificacion entidad de clasificación base
     * @return EntityModel con enlaces auto-descriptivos
     */
    @Override
    public EntityModel<ClasificacionModel> toModel(ClasificacionModel clasificacion) {

        // Se crea la representación base con la entidad + links principales
        EntityModel<ClasificacionModel> model = EntityModel.of(
                clasificacion,

                // ================================
                // 🔹 ENLACE SELF (Recurso actual)
                // ================================
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .getById(clasificacion.getId()))
                        .withSelfRel(),

                // ================================
                // 🔹 OPERACIONES CRUD
                // ================================

                // Actualización completa (PUT)
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .update(clasificacion.getId(), null))
                        .withRel("actualizar"),

                // Actualización parcial (PATCH)
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .patch(clasificacion.getId(), null))
                        .withRel("actualizar_parcial"),

                // Eliminación
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .delete(clasificacion.getId()))
                        .withRel("eliminar"),

                // ================================
                // 🔹 COLECCIÓN DE RECURSOS
                // ================================

                // Listado general de clasificaciones
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .getAll())
                        .withRel("clasificaciones"),

                // Crear nueva clasificación
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .create(null))
                        .withRel("crear"),

                // Alias de listado completo
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .getAll())
                        .withRel("listar_todas"),

                // Alias alternativo para la colección
                linkTo(methodOn(ClasificacionControllerV2.class)
                        .getAll())
                        .withRel("self_collection")
        );

        // =====================================================
        // 🔹 Enlaces condicionales según contenido del modelo
        // =====================================================

        // Si la clasificación tiene nombre, se añade un link relacionado
        if (clasificacion.getNombre() != null && !clasificacion.getNombre().isBlank()) {
            model.add(
                    linkTo(methodOn(ClasificacionControllerV2.class)
                            .getAll())
                            .withRel("relacionada_por_nombre")
            );
        }

        return model;
    }
}