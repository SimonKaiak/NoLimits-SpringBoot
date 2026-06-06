package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.GeneroModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GeneroResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GeneroModelAssembler Tests")
class GeneroModelAssemblerTest {

    private final GeneroModelAssembler assembler =
            new GeneroModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        GeneroResponseDTO dto = new GeneroResponseDTO();
        dto.setId(1L);

        EntityModel<GeneroResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("generos"));
        assertTrue(model.hasLink("crear"));
    }
}