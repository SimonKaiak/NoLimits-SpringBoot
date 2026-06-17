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
        @DisplayName("equals retorna true para la misma instancia")
        void equalsMismaInstancia() {

            DetalleVentaUpdateDTO dto = new DetalleVentaUpdateDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false con null")
        void equalsConNull() {

            DetalleVentaUpdateDTO dto = new DetalleVentaUpdateDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false con otra clase")
        void equalsConOtraClase() {

            DetalleVentaUpdateDTO dto = new DetalleVentaUpdateDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();
            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("objetos vacíos tienen mismo hashCode")
        void objetosVaciosMismoHashCode() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();
            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals detecta diferencia en ventaId")
        void equalsVentaIdDistinto() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();
            dto1.setVentaId(1L);

            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();
            dto2.setVentaId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detecta diferencia en productoId")
        void equalsProductoIdDistinto() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();
            dto1.setProductoId(10L);

            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();
            dto2.setProductoId(20L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detecta diferencia en cantidad")
        void equalsCantidadDistinta() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();
            dto1.setCantidad(1);

            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();
            dto2.setCantidad(2);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detecta diferencia en precioUnitario")
        void equalsPrecioUnitarioDistinto() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();
            dto1.setPrecioUnitario(1000F);

            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();
            dto2.setPrecioUnitario(2000F);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null versus valor en ventaId")
        void nullVsValorVentaId() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();

            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();
            dto2.setVentaId(1L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null versus valor en productoId")
        void nullVsValorProductoId() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();

            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();
            dto2.setProductoId(10L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null versus valor en cantidad")
        void nullVsValorCantidad() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();

            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();
            dto2.setCantidad(2);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null versus valor en precioUnitario")
        void nullVsValorPrecioUnitario() {

            DetalleVentaUpdateDTO dto1 = new DetalleVentaUpdateDTO();

            DetalleVentaUpdateDTO dto2 = new DetalleVentaUpdateDTO();
            dto2.setPrecioUnitario(12990F);

            assertNotEquals(dto1, dto2);
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