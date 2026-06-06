package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.TipoProductoModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoProductoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoProductoModelAssembler Tests")
class TipoProductoModelAssemblerTest {

    private final TipoProductoModelAssembler assembler =
            new TipoProductoModelAssembler();

    @Test
    void debeCrearLinksBasicos() {

        TipoProductoResponseDTO dto =
                new TipoProductoResponseDTO();

        EntityModel<TipoProductoResponseDTO> model =
                assembler.toModel(dto);

        assertTrue(model.hasLink("tipos_producto"));
        assertTrue(model.hasLink("crear"));

        assertFalse(model.hasLink("self"));
        assertFalse(model.hasLink("actualizar"));
        assertFalse(model.hasLink("actualizar_parcial"));
        assertFalse(model.hasLink("eliminar"));
    }

    @Test
    void debeAgregarLinksConId() {

        TipoProductoResponseDTO dto =
                new TipoProductoResponseDTO();

        dto.setId(1L);

        EntityModel<TipoProductoResponseDTO> model =
                assembler.toModel(dto);

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
    }
}