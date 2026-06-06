package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.TiposDeDesarrolladorModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TiposDeDesarrolladorModelAssembler Tests")
class TiposDeDesarrolladorModelAssemblerTest {

    private final TiposDeDesarrolladorModelAssembler assembler =
            new TiposDeDesarrolladorModelAssembler();

    @Test
    void noDebeAgregarLinksSinIds() {

        TiposDeDesarrolladorResponseDTO dto =
                new TiposDeDesarrolladorResponseDTO();

        EntityModel<TiposDeDesarrolladorResponseDTO> model =
                assembler.toModel(dto);

        assertFalse(model.hasLink("lista"));
        assertFalse(model.hasLink("vincular"));
        assertFalse(model.hasLink("desvincular"));
        assertFalse(model.hasLink("actualizar_relacion"));
    }

    @Test
    void debeAgregarTodosLosLinks() {

        TiposDeDesarrolladorResponseDTO dto =
                new TiposDeDesarrolladorResponseDTO();

        dto.setId(1L);
        dto.setDesarrolladorId(10L);
        dto.setTipoDeDesarrolladorId(20L);

        EntityModel<TiposDeDesarrolladorResponseDTO> model =
                assembler.toModel(dto);

        assertTrue(model.hasLink("lista"));
        assertTrue(model.hasLink("vincular"));
        assertTrue(model.hasLink("desvincular"));
        assertTrue(model.hasLink("actualizar_relacion"));
    }
}