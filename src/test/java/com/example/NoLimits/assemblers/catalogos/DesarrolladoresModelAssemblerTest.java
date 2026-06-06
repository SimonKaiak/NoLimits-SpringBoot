package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.DesarrolladoresModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladoresResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DesarrolladoresModelAssembler Tests")
class DesarrolladoresModelAssemblerTest {

    private final DesarrolladoresModelAssembler assembler =
            new DesarrolladoresModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        DesarrolladoresResponseDTO dto =
                new DesarrolladoresResponseDTO();

        dto.setId(1L);
        dto.setProductoId(10L);
        dto.setDesarrolladorId(5L);

        EntityModel<DesarrolladoresResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("lista"));
        assertTrue(model.hasLink("vincular"));
        assertTrue(model.hasLink("desvincular"));
    }
}