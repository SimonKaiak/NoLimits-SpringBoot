package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.TipoEmpresaModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoEmpresaResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoEmpresaModelAssembler Tests")
class TipoEmpresaModelAssemblerTest {

    private final TipoEmpresaModelAssembler assembler =
            new TipoEmpresaModelAssembler();

    @Test
    void noDebeAgregarLinksSiIdEsNull() {

        TipoEmpresaResponseDTO dto =
                new TipoEmpresaResponseDTO();

        EntityModel<TipoEmpresaResponseDTO> model =
                assembler.toModel(dto);

        assertFalse(model.hasLink("self"));
        assertFalse(model.hasLink("tipos-empresa"));
    }

    @Test
    void debeAgregarLinksSiIdExiste() {

        TipoEmpresaResponseDTO dto =
                new TipoEmpresaResponseDTO();

        dto.setId(1L);

        EntityModel<TipoEmpresaResponseDTO> model =
                assembler.toModel(dto);

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("tipos-empresa"));
    }
}