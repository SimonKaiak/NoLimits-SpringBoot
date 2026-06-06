package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.MetodoEnvioModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MetodoEnvioModelAssembler Tests")
class MetodoEnvioModelAssemblerTest {

    private final MetodoEnvioModelAssembler assembler =
            new MetodoEnvioModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        MetodoEnvioResponseDTO dto =
                new MetodoEnvioResponseDTO();

        dto.setId(1L);

        EntityModel<MetodoEnvioResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("metodos-envio"));
        assertTrue(model.hasLink("crear"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
    }
}