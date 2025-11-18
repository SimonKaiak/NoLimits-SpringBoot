package com.example.NoLimits.Multimedia.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.controllerV2.EmpresaControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.EmpresasControllerV2;
import com.example.NoLimits.Multimedia.controllerV2.TiposEmpresaControllerV2;
import com.example.NoLimits.Multimedia.model.EmpresasModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmpresasModelAssembler implements RepresentationModelAssembler<EmpresasModel, EntityModel<EmpresasModel>> {

    @Override
    public EntityModel<EmpresasModel> toModel(EmpresasModel rel) {
        EntityModel<EmpresasModel> model = EntityModel.of(rel);

        Long productoId = rel.getProducto() != null ? rel.getProducto().getId() : null;
        Long empresaId = rel.getEmpresa() != null ? rel.getEmpresa().getId() : null;

        // /api/v2/productos/{productoId}/empresas  (colección)
        if (productoId != null) {
            model.add(
                    linkTo(methodOn(EmpresasControllerV2.class).listar(productoId))
                            .withRel("producto-empresas")
            );
        }

        // /api/v2/empresas/{empresaId}
        if (empresaId != null) {
            model.add(
                    linkTo(methodOn(EmpresaControllerV2.class).findById(empresaId))
                            .withRel("empresa")
            );
            // /api/v2/empresas/{empresaId}/tipos-empresa
            model.add(
                    linkTo(methodOn(TiposEmpresaControllerV2.class).listar(empresaId))
                            .withRel("tipos-empresa")
            );
        }

        // Self: usamos la colección como referencia del recurso relación
        if (productoId != null) {
            model.add(
                    linkTo(methodOn(EmpresasControllerV2.class).listar(productoId))
                            .withSelfRel()
            );
        }

        // Link para desvincular Producto ↔ Empresa
        if (productoId != null && empresaId != null) {
            model.add(
                    linkTo(methodOn(EmpresasControllerV2.class).unlink(productoId, empresaId))
                            .withRel("desvincular")
            );
        }

        return model;
    }
}