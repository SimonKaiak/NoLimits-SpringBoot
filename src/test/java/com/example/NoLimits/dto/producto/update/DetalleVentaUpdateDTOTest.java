package com.example.NoLimits.dto.producto.update;

import com.example.NoLimits.Multimedia.dto.producto.update.DetalleVentaUpdateDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DetalleVentaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            DetalleVentaUpdateDTO dto = new DetalleVentaUpdateDTO();

            dto.setVentaId(3L);
            dto.setProductoId(10L);
            dto.setCantidad(2);
            dto.setPrecioUnitario(12990F);

            assertEquals(3L, dto.getVentaId());
            assertEquals(10L, dto.getProductoId());
            assertEquals(2, dto.getCantidad());
            assertEquals(12990F, dto.getPrecioUnitario());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            DetalleVentaUpdateDTO dto = new DetalleVentaUpdateDTO();

            assertNull(dto.getVentaId());
            assertNull(dto.getProductoId());
            assertNull(dto.getCantidad());
            assertNull(dto.getPrecioUnitario());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();
            dto1.setVentaId(3L);
            dto1.setProductoId(10L);
            dto1.setCantidad(2);
            dto1.setPrecioUnitario(12990F);

            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();
            dto2.setVentaId(3L);
            dto2.setProductoId(10L);
            dto2.setCantidad(2);
            dto2.setPrecioUnitario(12990F);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            DetalleVentaUpdateDTO dto = new DetalleVentaUpdateDTO();
            dto.setVentaId(3L);

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("3"));
        }
    }
}