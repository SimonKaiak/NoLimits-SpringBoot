package com.example.NoLimits.assemblers.ubicacion;

import com.example.NoLimits.Multimedia.assemblers.ubicacion.ComunaModelAssembler;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ComunaModelAssembler Tests")
class ComunaModelAssemblerTest {

    private final ComunaModelAssembler assembler =
            new ComunaModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel sin region")
    void debeCrearEntityModelSinRegion() {

        ComunaResponseDTO dto = new ComunaResponseDTO();
        dto.setId(1L);

        EntityModel<ComunaResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("comunas"));
        assertFalse(model.hasLink("region"));
    }

    @Test
    @DisplayName("Debe agregar link de region")
    void debeAgregarLinkRegion() {

        ComunaResponseDTO dto = new ComunaResponseDTO();
        dto.setId(1L);
        dto.setRegionId(10L);

        EntityModel<ComunaResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("comunas"));
        assertTrue(model.hasLink("region"));
    }
}