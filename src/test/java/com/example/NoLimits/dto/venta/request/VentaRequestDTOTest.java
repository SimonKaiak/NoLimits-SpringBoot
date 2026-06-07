package com.example.NoLimits.dto.venta.request;

import com.example.NoLimits.Multimedia.dto.producto.request.DetalleVentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.venta.request.VentaRequestDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VentaRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            VentaRequestDTO dto = new VentaRequestDTO();

            DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
            detalle.setProductoId(10L);
            detalle.setCantidad(2);
            detalle.setPrecioUnitario(12990F);

            dto.setUsuarioId(1L);
            dto.setMetodoPagoId(2L);
            dto.setMetodoEnvioId(3L);
            dto.setEstadoId(4L);
            dto.setDetalles(List.of(detalle));

            assertEquals(1L, dto.getUsuarioId());
            assertEquals(2L, dto.getMetodoPagoId());
            assertEquals(3L, dto.getMetodoEnvioId());
            assertEquals(4L, dto.getEstadoId());

            assertNotNull(dto.getDetalles());
            assertEquals(1, dto.getDetalles().size());

            assertEquals(10L, dto.getDetalles().get(0).getProductoId());
            assertEquals(2, dto.getDetalles().get(0).getCantidad());
            assertEquals(12990F, dto.getDetalles().get(0).getPrecioUnitario());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            VentaRequestDTO dto = new VentaRequestDTO();

            assertNull(dto.getUsuarioId());
            assertNull(dto.getMetodoPagoId());
            assertNull(dto.getMetodoEnvioId());
            assertNull(dto.getEstadoId());
            assertNull(dto.getDetalles());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setUsuarioId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setUsuarioId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            VentaRequestDTO dto = new VentaRequestDTO();
            dto.setUsuarioId(1L);

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("1"));
        }
    }
}