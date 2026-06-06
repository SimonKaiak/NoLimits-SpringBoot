package com.example.NoLimits.assemblers.ubicacion;

import com.example.NoLimits.Multimedia.assemblers.ubicacion.DireccionModelAssembler;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.DireccionResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DireccionModelAssembler Tests")
class DireccionModelAssemblerTest {

    private final DireccionModelAssembler assembler =
            new DireccionModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        DireccionResponseDTO dto = new DireccionResponseDTO();
        dto.setId(1L);

        EntityModel<DireccionResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("direcciones"));
    }
}