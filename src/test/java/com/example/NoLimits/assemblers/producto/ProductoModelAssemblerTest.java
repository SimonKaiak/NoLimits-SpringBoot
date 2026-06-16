package com.example.NoLimits.assemblers.producto;

import com.example.NoLimits.Multimedia.assemblers.producto.ProductoModelAssembler;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoModelAssembler Tests")
class ProductoModelAssemblerTest {

    private final ProductoModelAssembler assembler =
            new ProductoModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con todos los links")
    void debeCrearEntityModelConLinks() {

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);
        dto.setTipoProductoId(1L);
        dto.setClasificacionId(1L);
        dto.setEstadoId(1L);
        dto.setSaga("Halo");

        EntityModel<ProductoResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("actualizar"));
        assertTrue(model.hasLink("actualizar_parcial"));
        assertTrue(model.hasLink("actualizar_precio_steam"));
        assertTrue(model.hasLink("eliminar"));
        assertTrue(model.hasLink("crear"));
        assertTrue(model.hasLink("productos"));
        assertTrue(model.hasLink("productos_resumen"));

        assertTrue(model.hasLink("productos_mismo_tipo"));
        assertTrue(model.hasLink("productos_misma_clasificacion"));
        assertTrue(model.hasLink("productos_mismo_estado"));
        assertTrue(model.hasLink("productos_mismo_tipo_y_estado"));
        assertTrue(model.hasLink("productos_misma_saga"));
    }

    @Test
    @DisplayName("Debe agregar links opcionales cuando existen datos")
    void debeAgregarLinksOpcionales() {

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);
        dto.setTipoProductoId(2L);
        dto.setClasificacionId(3L);
        dto.setEstadoId(4L);
        dto.setSaga("Halo");

        EntityModel<ProductoResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);

        assertTrue(model.hasLink("productos_mismo_tipo"));
        assertTrue(model.hasLink("productos_misma_clasificacion"));
        assertTrue(model.hasLink("productos_mismo_estado"));
        assertTrue(model.hasLink("productos_mismo_tipo_y_estado"));
        assertTrue(model.hasLink("productos_misma_saga"));
    }

}