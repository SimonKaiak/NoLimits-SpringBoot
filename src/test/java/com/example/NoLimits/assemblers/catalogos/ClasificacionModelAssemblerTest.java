package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.ClasificacionModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.ClasificacionResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClasificacionModelAssembler Tests")
class ClasificacionModelAssemblerTest {

    private final ClasificacionModelAssembler assembler =
            new ClasificacionModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel sin link relacionado por nombre")
    void debeCrearEntityModelSinLinkRelacionado() {

        ClasificacionResponseDTO dto = new ClasificacionResponseDTO();
        dto.setId(1L);

        EntityModel<ClasificacionResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("clasificaciones"));
        assertTrue(model.hasLink("crear"));
        assertTrue(model.hasLink("listar_todas"));
        assertTrue(model.hasLink("self_collection"));

        assertFalse(model.hasLink("relacionada_por_nombre"));
    }

    @Test
    @DisplayName("Debe agregar link relacionado por nombre")
    void debeAgregarLinkRelacionadoPorNombre() {

        ClasificacionResponseDTO dto = new ClasificacionResponseDTO();
        dto.setId(1L);
        dto.setNombre("PEGI 18");

        EntityModel<ClasificacionResponseDTO> model =
                assembler.toModel(dto);

        assertTrue(model.hasLink("relacionada_por_nombre"));
    }
}