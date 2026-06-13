package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.GenerosModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GenerosResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GenerosModelAssembler Tests")
class GenerosModelAssemblerTest {

    private final GenerosModelAssembler assembler =
            new GenerosModelAssembler();

    @Test
    @DisplayName("No debe agregar links si faltan ids")
    void noDebeAgregarLinks() {

        GenerosResponseDTO dto = new GenerosResponseDTO();

        EntityModel<GenerosResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);

        assertFalse(model.hasLink("self"));
        assertFalse(model.hasLink("desvincular"));
        assertFalse(model.hasLink("generos_del_producto"));
        assertFalse(model.hasLink("productos_del_genero"));
        assertFalse(model.hasLink("producto"));
        assertFalse(model.hasLink("genero"));
    }

    @Test
    @DisplayName("Debe agregar todos los links")
    void debeAgregarTodosLosLinks() {

        GenerosResponseDTO dto = new GenerosResponseDTO();
        dto.setProductoId(1L);
        dto.setGeneroId(2L);

        EntityModel<GenerosResponseDTO> model =
                assembler.toModel(dto);

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("desvincular"));
        assertTrue(model.hasLink("generos_del_producto"));
        assertTrue(model.hasLink("productos_del_genero"));
        assertTrue(model.hasLink("producto"));
        assertTrue(model.hasLink("genero"));
    }

    @Test
    @DisplayName("No agrega links cuando productoId es null")
    void noAgregaLinksCuandoProductoIdEsNull() {

        // Arrange
        GenerosResponseDTO dto = new GenerosResponseDTO();
        dto.setProductoId(null);
        dto.setGeneroId(2L);

        // Act
        EntityModel<GenerosResponseDTO> model =
            assembler.toModel(dto);

        // Assert
        assertFalse(model.hasLink("self"));
        assertFalse(model.hasLink("desvincular"));
        assertFalse(model.hasLink("generos_del_producto"));
        assertFalse(model.hasLink("productos_del_genero"));
        assertFalse(model.hasLink("producto"));
        assertFalse(model.hasLink("genero"));
    }

    @Test
    @DisplayName("No agrega links cuando generoId es null")
    void noAgregaLinksCuandoGeneroIdEsNull() {

        // Arrange
        GenerosResponseDTO dto = new GenerosResponseDTO();
        dto.setProductoId(1L);
        dto.setGeneroId(null);

        // Act
        EntityModel<GenerosResponseDTO> model =
                assembler.toModel(dto);

        // Assert
        assertFalse(model.hasLink("self"));
        assertFalse(model.hasLink("desvincular"));
        assertFalse(model.hasLink("generos_del_producto"));
        assertFalse(model.hasLink("productos_del_genero"));
        assertFalse(model.hasLink("producto"));
        assertFalse(model.hasLink("genero"));
    }
}