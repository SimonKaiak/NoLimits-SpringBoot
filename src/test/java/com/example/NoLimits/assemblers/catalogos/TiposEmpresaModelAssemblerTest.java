package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.TiposEmpresaModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposEmpresaResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TiposEmpresaModelAssembler Tests")
class TiposEmpresaModelAssemblerTest {

    private final TiposEmpresaModelAssembler assembler =
            new TiposEmpresaModelAssembler();

    @Test
    void noDebeAgregarLinksSinIds() {

        TiposEmpresaResponseDTO dto =
                new TiposEmpresaResponseDTO();

        EntityModel<TiposEmpresaResponseDTO> model =
                assembler.toModel(dto);

        assertFalse(model.hasLink("self"));
        assertFalse(model.hasLink("empresa"));
        assertFalse(model.hasLink("tipo-empresa"));
        assertFalse(model.hasLink("desvincular"));
    }

    @Test
    void debeAgregarTodosLosLinks() {

        TiposEmpresaResponseDTO dto =
                new TiposEmpresaResponseDTO();

        dto.setEmpresaId(1L);
        dto.setTipoEmpresaId(2L);

        EntityModel<TiposEmpresaResponseDTO> model =
                assembler.toModel(dto);

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("empresa"));
        assertTrue(model.hasLink("tipo-empresa"));
        assertTrue(model.hasLink("desvincular"));
    }

    @Test
    @DisplayName("Agrega enlaces de empresa cuando tipoEmpresaId es null")
    void agregaLinksEmpresaSinTipoEmpresa() {

        // Arrange
        TiposEmpresaResponseDTO dto =
                new TiposEmpresaResponseDTO();

        dto.setEmpresaId(1L);

        // Act
        EntityModel<TiposEmpresaResponseDTO> model =
                assembler.toModel(dto);

        // Assert
        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("empresa"));

        assertFalse(model.hasLink("tipo-empresa"));
        assertFalse(model.hasLink("desvincular"));
    }

    @Test
    @DisplayName("Agrega enlace tipoEmpresa cuando empresaId es null")
    void agregaLinkTipoEmpresaSinEmpresa() {

        // Arrange
        TiposEmpresaResponseDTO dto =
                new TiposEmpresaResponseDTO();

        dto.setTipoEmpresaId(2L);

        // Act
        EntityModel<TiposEmpresaResponseDTO> model =
                assembler.toModel(dto);

        // Assert
        assertFalse(model.hasLink("self"));
        assertFalse(model.hasLink("empresa"));

        assertTrue(model.hasLink("tipo-empresa"));

        assertFalse(model.hasLink("desvincular"));
    }
}