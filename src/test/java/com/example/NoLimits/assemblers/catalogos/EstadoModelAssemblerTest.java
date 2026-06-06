package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.EstadoModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EstadoModelAssembler Tests")
class EstadoModelAssemblerTest {

    private final EstadoModelAssembler assembler =
            new EstadoModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        EstadoResponseDTO dto = new EstadoResponseDTO();
        dto.setId(1L);

        EntityModel<EstadoResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("estados"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
    }
}