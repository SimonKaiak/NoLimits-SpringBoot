package com.example.NoLimits.assemblers.usuario;

import com.example.NoLimits.Multimedia.assemblers.usuario.UsuarioModelAssembler;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioModelAssembler Tests")
class UsuarioModelAssemblerTest {

    private final UsuarioModelAssembler assembler =
            new UsuarioModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(1L);

        EntityModel<UsuarioResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("usuarios"));
        assertTrue(model.hasLink("crear"));
    }
}