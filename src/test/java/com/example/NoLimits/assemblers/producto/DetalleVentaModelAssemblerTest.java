package com.example.NoLimits.assemblers.producto;

import com.example.NoLimits.Multimedia.assemblers.producto.DetalleVentaModelAssembler;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DetalleVentaModelAssembler Tests")
class DetalleVentaModelAssemblerTest {

    private final DetalleVentaModelAssembler assembler =
            new DetalleVentaModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con todos los links HATEOAS")
    void debeCrearEntityModelConLinks() {

        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();
        dto.setId(1L);
        dto.setProductoId(10L);
        dto.setProductoNombre("Control Xbox");
        dto.setCantidad(2);
        dto.setPrecioUnitario(12990f);
        dto.setSubtotal(25980f);

        EntityModel<DetalleVentaResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("detalles-venta"));
        assertTrue(model.hasLink("crear"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
    }
}