package com.example.NoLimits.assemblers.producto;

import com.example.NoLimits.Multimedia.assemblers.producto.ImagenesModelAssembler;
import com.example.NoLimits.Multimedia.dto.producto.response.ImagenesResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ImagenesModelAssembler Tests")
class ImagenesModelAssemblerTest {

    private final ImagenesModelAssembler assembler =
            new ImagenesModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links básicos")
    void debeCrearEntityModelConLinksBasicos() {

        ImagenesResponseDTO dto = new ImagenesResponseDTO();
        dto.setId(1L);

        EntityModel<ImagenesResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("imagenes"));
        assertTrue(model.hasLink("crear"));

        assertFalse(model.hasLink("imagenes_del_producto"));
        assertFalse(model.hasLink("eliminar_todas_del_producto"));
    }

    @Test
    @DisplayName("Debe agregar links relacionados al producto")
    void debeAgregarLinksDelProducto() {

        ImagenesResponseDTO dto = new ImagenesResponseDTO();
        dto.setId(1L);
        dto.setProductoId(10L);

        EntityModel<ImagenesResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("imagenes_del_producto"));
        assertTrue(model.hasLink("eliminar_todas_del_producto"));
    }
}