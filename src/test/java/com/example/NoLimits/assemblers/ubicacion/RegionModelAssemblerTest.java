package com.example.NoLimits.assemblers.ubicacion;

import com.example.NoLimits.Multimedia.assemblers.ubicacion.RegionModelAssembler;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.RegionResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RegionModelAssembler Tests")
class RegionModelAssemblerTest {

    private final RegionModelAssembler assembler =
            new RegionModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(1L);

        EntityModel<RegionResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("regiones"));
    }
}
