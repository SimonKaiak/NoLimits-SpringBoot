package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.EmpresaModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresaResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmpresaModelAssembler Tests")
class EmpresaModelAssemblerTest {

    private final EmpresaModelAssembler assembler =
            new EmpresaModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel sin links cuando id es null")
    void debeCrearEntityModelSinLinks() {

        EmpresaResponseDTO dto = new EmpresaResponseDTO();

        EntityModel<EmpresaResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertFalse(model.hasLink("self"));
        assertFalse(model.hasLink("empresas"));
        assertFalse(model.hasLink("tipos-empresa"));
    }

    @Test
    @DisplayName("Debe agregar links cuando id existe")
    void debeAgregarLinks() {

        EmpresaResponseDTO dto = new EmpresaResponseDTO();
        dto.setId(1L);

        EntityModel<EmpresaResponseDTO> model =
                assembler.toModel(dto);

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("empresas"));
        assertTrue(model.hasLink("tipos-empresa"));
    }
}