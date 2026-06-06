package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.DesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladorResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DesarrolladorModelAssembler Tests")
class DesarrolladorModelAssemblerTest {

    private final DesarrolladorModelAssembler assembler =
            new DesarrolladorModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        DesarrolladorResponseDTO dto =
                new DesarrolladorResponseDTO();

        dto.setId(1L);

        EntityModel<DesarrolladorResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("desarrolladores"));
    }
}