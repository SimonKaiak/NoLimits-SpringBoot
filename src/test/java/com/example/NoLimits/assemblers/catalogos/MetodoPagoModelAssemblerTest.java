package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.MetodoPagoModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoPagoResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MetodoPagoModelAssembler Tests")
class MetodoPagoModelAssemblerTest {

    private final MetodoPagoModelAssembler assembler =
            new MetodoPagoModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();
        dto.setId(1L);

        EntityModel<MetodoPagoResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("metodos_pago"));
        assertTrue(model.hasLink("crear"));
    }
}