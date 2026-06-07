package com.example.NoLimits.dto.venta.response;

import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VentaResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            VentaResponseDTO dto = new VentaResponseDTO();

            DetalleVentaResponseDTO detalle = new DetalleVentaResponseDTO();
            detalle.setId(1L);
            detalle.setProductoId(10L);
            detalle.setProductoNombre("Control Xbox Series X");
            detalle.setCantidad(2);
            detalle.setPrecioUnitario(12990F);
            detalle.setSubtotal(25980F);

            LocalDate fecha = LocalDate.of(2025, 7, 6);
            LocalTime hora = LocalTime.of(14, 30);

            dto.setId(10L);
            dto.setFechaCompra(fecha);
            dto.setHoraCompra(hora);
            dto.setUsuarioId(1L);
            dto.setUsuarioNombre("Juan Pérez Soto");
            dto.setMetodoPagoId(2L);
            dto.setMetodoPagoNombre("Tarjeta de Crédito");
            dto.setMetodoEnvioId(3L);
            dto.setMetodoEnvioNombre("Despacho a domicilio");
            dto.setEstadoId(4L);
            dto.setEstadoNombre("PENDIENTE");
            dto.setTotalVenta(45990F);
            dto.setDetalles(List.of(detalle));

            assertEquals(10L, dto.getId());
            assertEquals(fecha, dto.getFechaCompra());
            assertEquals(hora, dto.getHoraCompra());
            assertEquals(1L, dto.getUsuarioId());
            assertEquals("Juan Pérez Soto", dto.getUsuarioNombre());
            assertEquals(2L, dto.getMetodoPagoId());
            assertEquals("Tarjeta de Crédito", dto.getMetodoPagoNombre());
            assertEquals(3L, dto.getMetodoEnvioId());
            assertEquals("Despacho a domicilio", dto.getMetodoEnvioNombre());
            assertEquals(4L, dto.getEstadoId());
            assertEquals("PENDIENTE", dto.getEstadoNombre());
            assertEquals(45990F, dto.getTotalVenta());

            assertNotNull(dto.getDetalles());
            assertEquals(1, dto.getDetalles().size());
            assertEquals("Control Xbox Series X", dto.getDetalles().get(0).getProductoNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            VentaResponseDTO dto = new VentaResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getFechaCompra());
            assertNull(dto.getHoraCompra());
            assertNull(dto.getUsuarioId());
            assertNull(dto.getUsuarioNombre());
            assertNull(dto.getMetodoPagoId());
            assertNull(dto.getMetodoPagoNombre());
            assertNull(dto.getMetodoEnvioId());
            assertNull(dto.getMetodoEnvioNombre());
            assertNull(dto.getEstadoId());
            assertNull(dto.getEstadoNombre());
            assertNull(dto.getTotalVenta());
            assertNull(dto.getDetalles());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setId(10L);
            dto1.setUsuarioNombre("Juan Pérez Soto");

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setId(10L);
            dto2.setUsuarioNombre("Juan Pérez Soto");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            VentaResponseDTO dto = new VentaResponseDTO();
            dto.setEstadoNombre("PENDIENTE");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("PENDIENTE"));
        }
    }
}