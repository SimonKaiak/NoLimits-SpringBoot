package com.example.NoLimits.assemblers.usuario;

import com.example.NoLimits.Multimedia.assemblers.usuario.RolModelAssembler;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RolModelAssembler Tests")
class RolModelAssemblerTest {

    private final RolModelAssembler assembler =
            new RolModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        RolResponseDTO dto = new RolResponseDTO();
        dto.setId(1L);

        EntityModel<RolResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("roles"));
    }
}