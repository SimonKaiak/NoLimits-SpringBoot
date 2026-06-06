package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.PlataformaModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlataformaModelAssembler Tests")
class PlataformaModelAssemblerTest {

    private final PlataformaModelAssembler assembler =
            new PlataformaModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        PlataformaResponseDTO dto = new PlataformaResponseDTO();
        dto.setId(1L);

        EntityModel<PlataformaResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("plataformas"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("crear"));
    }
}