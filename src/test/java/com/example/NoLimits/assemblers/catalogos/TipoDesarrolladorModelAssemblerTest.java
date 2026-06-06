package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.TipoDeDesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoDeDesarrolladorModelAssembler Tests")
class TipoDeDesarrolladorModelAssemblerTest {

    private final TipoDeDesarrolladorModelAssembler assembler =
            new TipoDeDesarrolladorModelAssembler();

    @Test
    void debeCrearEntityModelConLinks() {

        TipoDeDesarrolladorResponseDTO dto =
                new TipoDeDesarrolladorResponseDTO();

        dto.setId(1L);

        EntityModel<TipoDeDesarrolladorResponseDTO> model =
                assembler.toModel(dto);

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("tipos-desarrollador"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("crear"));
    }
}