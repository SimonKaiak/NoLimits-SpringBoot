package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.PlataformasModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformasResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlataformasModelAssembler Tests")
class PlataformasModelAssemblerTest {

    private final PlataformasModelAssembler assembler =
            new PlataformasModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        PlataformasResponseDTO dto = new PlataformasResponseDTO();
        dto.setProductoId(1L);

        EntityModel<PlataformasResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("producto-plataformas"));
    }
}