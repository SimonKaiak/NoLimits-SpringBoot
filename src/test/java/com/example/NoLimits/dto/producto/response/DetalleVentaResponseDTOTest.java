package com.example.NoLimits.dto.producto.response;

import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DetalleVentaResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();

            dto.setId(1L);
            dto.setProductoId(10L);
            dto.setProductoNombre("Control Xbox Series X");
            dto.setCantidad(2);
            dto.setPrecioUnitario(12990F);
            dto.setSubtotal(25980F);

            assertEquals(1L, dto.getId());
            assertEquals(10L, dto.getProductoId());
            assertEquals("Control Xbox Series X", dto.getProductoNombre());
            assertEquals(2, dto.getCantidad());
            assertEquals(12990F, dto.getPrecioUnitario());
            assertEquals(25980F, dto.getSubtotal());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getProductoId());
            assertNull(dto.getProductoNombre());
            assertNull(dto.getCantidad());
            assertNull(dto.getPrecioUnitario());
            assertNull(dto.getSubtotal());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            DetalleVentaResponseDTO dto1 = new DetalleVentaResponseDTO();
            dto1.setId(1L);
            dto1.setProductoId(10L);

            DetalleVentaResponseDTO dto2 = new DetalleVentaResponseDTO();
            dto2.setId(1L);
            dto2.setProductoId(10L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna true para misma instancia")
        void equalsMismaInstancia() {

            DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false con null")
        void equalsConNull() {

            DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false con otro tipo")
        void equalsConOtroTipo() {

            DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando cambia id")
        void equalsDistintoId() {

            DetalleVentaResponseDTO dto1 = new DetalleVentaResponseDTO();
            dto1.setId(1L);

            DetalleVentaResponseDTO dto2 = new DetalleVentaResponseDTO();
            dto2.setId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia productoId")
        void equalsDistintoProductoId() {

            DetalleVentaResponseDTO dto1 = new DetalleVentaResponseDTO();
            dto1.setProductoId(10L);

            DetalleVentaResponseDTO dto2 = new DetalleVentaResponseDTO();
            dto2.setProductoId(20L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia productoNombre")
        void equalsDistintoProductoNombre() {

            DetalleVentaResponseDTO dto1 = new DetalleVentaResponseDTO();
            dto1.setProductoNombre("Xbox");

            DetalleVentaResponseDTO dto2 = new DetalleVentaResponseDTO();
            dto2.setProductoNombre("PlayStation");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia cantidad")
        void equalsDistintaCantidad() {

            DetalleVentaResponseDTO dto1 = new DetalleVentaResponseDTO();
            dto1.setCantidad(1);

            DetalleVentaResponseDTO dto2 = new DetalleVentaResponseDTO();
            dto2.setCantidad(2);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia precioUnitario")
        void equalsDistintoPrecioUnitario() {

            DetalleVentaResponseDTO dto1 = new DetalleVentaResponseDTO();
            dto1.setPrecioUnitario(1000F);

            DetalleVentaResponseDTO dto2 = new DetalleVentaResponseDTO();
            dto2.setPrecioUnitario(2000F);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia subtotal")
        void equalsDistintoSubtotal() {

            DetalleVentaResponseDTO dto1 = new DetalleVentaResponseDTO();
            dto1.setSubtotal(1000F);

            DetalleVentaResponseDTO dto2 = new DetalleVentaResponseDTO();
            dto2.setSubtotal(2000F);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            DetalleVentaResponseDTO dto1 = new DetalleVentaResponseDTO();
            DetalleVentaResponseDTO dto2 = new DetalleVentaResponseDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("objetos vacíos generan mismo hashCode")
        void objetosVaciosMismoHashCode() {

            DetalleVentaResponseDTO dto1 = new DetalleVentaResponseDTO();
            DetalleVentaResponseDTO dto2 = new DetalleVentaResponseDTO();

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode puede ejecutarse con objeto vacío")
        void hashCodeObjetoVacio() {

            DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();

            assertDoesNotThrow(dto::hashCode);
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();
            dto.setProductoNombre("Control Xbox Series X");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("Control Xbox Series X"));
        }
    }
}