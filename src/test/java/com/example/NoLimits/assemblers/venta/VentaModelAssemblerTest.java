package com.example.NoLimits.assemblers.venta;

import com.example.NoLimits.Multimedia.assemblers.venta.VentaModelAssembler;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VentaModelAssembler Tests")
class VentaModelAssemblerTest {

    private final VentaModelAssembler assembler =
            new VentaModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel sin metodo de pago")
    void debeCrearEntityModelSinMetodoPago() {

        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(1L);

        EntityModel<VentaResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("ventas"));
        assertTrue(model.hasLink("crear"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("detalles-venta"));

        assertFalse(model.hasLink("por-metodo-pago"));
    }

    @Test
    @DisplayName("Debe agregar link por metodo de pago")
    void debeAgregarLinkMetodoPago() {

        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(1L);
        dto.setMetodoPagoId(2L);

        EntityModel<VentaResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("ventas"));
        assertTrue(model.hasLink("crear"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("detalles-venta"));

        assertTrue(model.hasLink("por-metodo-pago"));
    }
}