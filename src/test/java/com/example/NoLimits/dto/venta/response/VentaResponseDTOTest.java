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

    private VentaResponseDTO crearDTOCompleto() {

        DetalleVentaResponseDTO detalle = new DetalleVentaResponseDTO();
        detalle.setId(1L);
        detalle.setProductoId(10L);
        detalle.setProductoNombre("Control Xbox Series X");
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(12990F);
        detalle.setSubtotal(25980F);

        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(10L);
        dto.setFechaCompra(LocalDate.of(2025, 7, 6));
        dto.setHoraCompra(LocalTime.of(14, 30));
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

        return dto;
    }

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

            VentaResponseDTO dto1 = crearDTOCompleto();
            VentaResponseDTO dto2 = crearDTOCompleto();

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals mismo objeto retorna true")
        void equalsMismoObjeto() {

            VentaResponseDTO dto = new VentaResponseDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals con null retorna false")
        void equalsConNull() {

            VentaResponseDTO dto = new VentaResponseDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals con clase distinta retorna false")
        void equalsConClaseDistinta() {

            VentaResponseDTO dto = new VentaResponseDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals distinto id retorna false")
        void equalsDistintoId() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setId(1L);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto usuario retorna false")
        void equalsDistintoUsuario() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setUsuarioNombre("Juan");

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setUsuarioNombre("Pedro");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto metodo pago retorna false")
        void equalsDistintoMetodoPago() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setMetodoPagoNombre("Tarjeta");

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setMetodoPagoNombre("OnePay");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto metodo envio retorna false")
        void equalsDistintoMetodoEnvio() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setMetodoEnvioNombre("Despacho");

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setMetodoEnvioNombre("Retiro");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto estado retorna false")
        void equalsDistintoEstado() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setEstadoNombre("PENDIENTE");

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setEstadoNombre("PAGADA");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto total retorna false")
        void equalsDistintoTotal() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setTotalVenta(1000F);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setTotalVenta(2000F);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinta fecha retorna false")
        void equalsDistintaFecha() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setFechaCompra(LocalDate.of(2025, 1, 1));

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setFechaCompra(LocalDate.of(2025, 1, 2));

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinta hora retorna false")
        void equalsDistintaHora() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setHoraCompra(LocalTime.of(10, 0));

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setHoraCompra(LocalTime.of(11, 0));

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distintos detalles retorna false")
        void equalsDistintosDetalles() {

            DetalleVentaResponseDTO detalle1 = new DetalleVentaResponseDTO();
            detalle1.setProductoId(1L);

            DetalleVentaResponseDTO detalle2 = new DetalleVentaResponseDTO();
            detalle2.setProductoId(2L);

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setDetalles(List.of(detalle1));

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setDetalles(List.of(detalle2));

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto usuarioId retorna false")
        void equalsDistintoUsuarioId() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setUsuarioId(1L);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setUsuarioId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto metodoPagoId retorna false")
        void equalsDistintoMetodoPagoId() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setMetodoPagoId(1L);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setMetodoPagoId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto metodoEnvioId retorna false")
        void equalsDistintoMetodoEnvioId() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setMetodoEnvioId(1L);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setMetodoEnvioId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto estadoId retorna false")
        void equalsDistintoEstadoId() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setEstadoId(1L);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setEstadoId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode igual para objetos equivalentes")
        void hashCodeIgual() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setId(1L);
            dto1.setUsuarioNombre("Juan");

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setId(1L);
            dto2.setUsuarioNombre("Juan");

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode distinto cuando cambia id")
        void hashCodeDistinto() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setId(1L);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setId(2L);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
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

        @Test
        @DisplayName("toString contiene id y estado")
        void toStringContieneCampos() {

            VentaResponseDTO dto = new VentaResponseDTO();
            dto.setId(10L);
            dto.setEstadoNombre("PENDIENTE");

            String resultado = dto.toString();

            assertTrue(resultado.contains("10"));
            assertTrue(resultado.contains("PENDIENTE"));
        }

        @Test
        @DisplayName("equals id null vs valor")
        void equalsIdNullVsValor() {

            VentaResponseDTO dto1 = new VentaResponseDTO();

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setId(1L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals usuarioNombre null vs valor")
        void equalsUsuarioNombreNullVsValor() {

            VentaResponseDTO dto1 = new VentaResponseDTO();

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setUsuarioNombre("Juan");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals totalVenta null vs valor")
        void equalsTotalVentaNullVsValor() {

            VentaResponseDTO dto1 = new VentaResponseDTO();

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setTotalVenta(1000F);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals cuando ambos objetos tienen todos los campos nulos")
        void equalsTodosNulos() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            VentaResponseDTO dto2 = new VentaResponseDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode con todos los campos nulos")
        void hashCodeTodosNulos() {

            VentaResponseDTO dto = new VentaResponseDTO();

            assertDoesNotThrow(dto::hashCode);
        }

        @Test
        @DisplayName("equals mismo id pero distinto usuarioId")
        void equalsMismoIdDistintoUsuarioId() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setId(1L);
            dto1.setUsuarioId(1L);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setId(1L);
            dto2.setUsuarioId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals mismos ids pero distinto metodoPagoId")
        void equalsMismosCamposPreviosDistintoMetodoPagoId() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setId(1L);
            dto1.setUsuarioId(1L);
            dto1.setMetodoPagoId(1L);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setId(1L);
            dto2.setUsuarioId(1L);
            dto2.setMetodoPagoId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals mismos campos previos pero distinto estadoId")
        void equalsCamposPreviosIgualesEstadoIdDistinto() {

            VentaResponseDTO dto1 = new VentaResponseDTO();
            dto1.setId(1L);
            dto1.setUsuarioId(1L);
            dto1.setMetodoPagoId(1L);
            dto1.setMetodoEnvioId(1L);
            dto1.setEstadoId(1L);

            VentaResponseDTO dto2 = new VentaResponseDTO();
            dto2.setId(1L);
            dto2.setUsuarioId(1L);
            dto2.setMetodoPagoId(1L);
            dto2.setMetodoEnvioId(1L);
            dto2.setEstadoId(2L);

            assertNotEquals(dto1, dto2);
        }
    }
}