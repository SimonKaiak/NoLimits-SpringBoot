package com.example.NoLimits.Multimedia.assemblers.catalogos;

import com.example.NoLimits.Multimedia.controllerV2.catalogos.DesarrolladoresControllerV2;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladoresResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DesarrolladoresModelAssembler implements RepresentationModelAssembler<DesarrolladoresResponseDTO, EntityModel<DesarrolladoresResponseDTO>> {

    @Override
    public EntityModel<DesarrolladoresResponseDTO> toModel(DesarrolladoresResponseDTO dto) {

        Long productoId = dto.getProductoId();
        Long desarrolladorId = dto.getDesarrolladorId();
        Long relacionId = dto.getId();

        return EntityModel.of(
                dto,
                // lista de relaciones para el producto
                linkTo(methodOn(DesarrolladoresControllerV2.class).listar(productoId)).withRel("lista"),
                // crear/vincular (pasamos null como body porque methodOn solo necesita la signatura)
                linkTo(methodOn(DesarrolladoresControllerV2.class).link(productoId, desarrolladorId, null)).withRel("vincular"),
                // desvincular
                linkTo(methodOn(DesarrolladoresControllerV2.class).unlink(productoId, desarrolladorId)).withRel("desvincular"),
                // self/patch sobre esta relación
                linkTo(methodOn(DesarrolladoresControllerV2.class).patch(productoId, relacionId, null)).withSelfRel()
        );
    }
}