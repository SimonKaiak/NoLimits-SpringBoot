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
        @DisplayName("equals mismo objeto retorna true")
        void equalsMismoObjeto() {

            VentaRequestDTO dto = new VentaRequestDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals con null retorna false")
        void equalsConNull() {

            VentaRequestDTO dto = new VentaRequestDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals con clase distinta retorna false")
        void equalsConClaseDistinta() {

            VentaRequestDTO dto = new VentaRequestDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals distinto usuario retorna false")
        void equalsDistintoUsuario() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setUsuarioId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setUsuarioId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto metodoPago retorna false")
        void equalsDistintoMetodoPago() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setMetodoPagoId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setMetodoPagoId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto metodoEnvio retorna false")
        void equalsDistintoMetodoEnvio() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setMetodoEnvioId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setMetodoEnvioId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto estado retorna false")
        void equalsDistintoEstado() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setEstadoId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setEstadoId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distintos detalles retorna false")
        void equalsDistintosDetalles() {

            DetalleVentaRequestDTO detalle1 = new DetalleVentaRequestDTO();
            detalle1.setProductoId(1L);

            DetalleVentaRequestDTO detalle2 = new DetalleVentaRequestDTO();
            detalle2.setProductoId(2L);

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setDetalles(List.of(detalle1));

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setDetalles(List.of(detalle2));

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia usuario")
        void hashCodeDistintoUsuario() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setUsuarioId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setUsuarioId(2L);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode igual para objetos equivalentes")
        void hashCodeIgual() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setUsuarioId(1L);
            dto1.setMetodoPagoId(2L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setUsuarioId(1L);
            dto2.setMetodoPagoId(2L);

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

        @Test
        @DisplayName("equals usuario null vs valor")
        void equalsUsuarioNullVsValor() {

            VentaRequestDTO dto1 = new VentaRequestDTO();

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setUsuarioId(1L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals metodoPago null vs valor")
        void equalsMetodoPagoNullVsValor() {

            VentaRequestDTO dto1 = new VentaRequestDTO();

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setMetodoPagoId(1L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals metodoEnvio null vs valor")
        void equalsMetodoEnvioNullVsValor() {

            VentaRequestDTO dto1 = new VentaRequestDTO();

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setMetodoEnvioId(1L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals estado null vs valor")
        void equalsEstadoNullVsValor() {

            VentaRequestDTO dto1 = new VentaRequestDTO();

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setEstadoId(1L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detalles null vs valor")
        void equalsDetallesNullVsValor() {

            DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
            detalle.setProductoId(1L);

            VentaRequestDTO dto1 = new VentaRequestDTO();

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setDetalles(List.of(detalle));

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals cuando ambos objetos tienen campos nulos")
        void equalsTodosNulos() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            VentaRequestDTO dto2 = new VentaRequestDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode con todos los campos nulos")
        void hashCodeTodosNulos() {

            VentaRequestDTO dto = new VentaRequestDTO();

            assertDoesNotThrow(dto::hashCode);
        }

        @Test
        @DisplayName("hashCode distinto cuando cambia metodoPago")
        void hashCodeDistintoMetodoPago() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setMetodoPagoId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setMetodoPagoId(2L);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode distinto cuando cambia metodoEnvio")
        void hashCodeDistintoMetodoEnvio() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setMetodoEnvioId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setMetodoEnvioId(2L);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode distinto cuando cambia estado")
        void hashCodeDistintoEstado() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setEstadoId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setEstadoId(2L);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode distinto cuando cambian los detalles")
        void hashCodeDistintosDetalles() {

            DetalleVentaRequestDTO detalle1 = new DetalleVentaRequestDTO();
            detalle1.setProductoId(1L);

            DetalleVentaRequestDTO detalle2 = new DetalleVentaRequestDTO();
            detalle2.setProductoId(2L);

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setDetalles(List.of(detalle1));

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setDetalles(List.of(detalle2));

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals cuando usuario es igual pero metodoPago es distinto")
        void equalsUsuarioIgualMetodoPagoDistinto() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setUsuarioId(1L);
            dto1.setMetodoPagoId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setUsuarioId(1L);
            dto2.setMetodoPagoId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals cuando usuario y metodoPago son iguales pero metodoEnvio es distinto")
        void equalsMetodoEnvioDistinto() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setUsuarioId(1L);
            dto1.setMetodoPagoId(1L);
            dto1.setMetodoEnvioId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setUsuarioId(1L);
            dto2.setMetodoPagoId(1L);
            dto2.setMetodoEnvioId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals cuando todos los ids son iguales pero estado es distinto")
        void equalsEstadoDistintoConCamposPreviosIguales() {

            VentaRequestDTO dto1 = new VentaRequestDTO();
            dto1.setUsuarioId(1L);
            dto1.setMetodoPagoId(1L);
            dto1.setMetodoEnvioId(1L);
            dto1.setEstadoId(1L);

            VentaRequestDTO dto2 = new VentaRequestDTO();
            dto2.setUsuarioId(1L);
            dto2.setMetodoPagoId(1L);
            dto2.setMetodoEnvioId(1L);
            dto2.setEstadoId(2L);

            assertNotEquals(dto1, dto2);
        }

    }
}