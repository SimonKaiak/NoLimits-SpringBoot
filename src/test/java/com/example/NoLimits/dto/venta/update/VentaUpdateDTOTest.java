package com.example.NoLimits.dto.venta.update;

import com.example.NoLimits.Multimedia.dto.venta.update.VentaUpdateDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class VentaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            VentaUpdateDTO dto = new VentaUpdateDTO();

            LocalDate fecha = LocalDate.of(2025, 7, 6);
            LocalTime hora = LocalTime.of(14, 30);

            dto.setFechaCompra(fecha);
            dto.setHoraCompra(hora);
            dto.setMetodoPagoId(1L);
            dto.setMetodoEnvioId(2L);
            dto.setEstadoId(3L);

            assertEquals(fecha, dto.getFechaCompra());
            assertEquals(hora, dto.getHoraCompra());
            assertEquals(1L, dto.getMetodoPagoId());
            assertEquals(2L, dto.getMetodoEnvioId());
            assertEquals(3L, dto.getEstadoId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            VentaUpdateDTO dto = new VentaUpdateDTO();

            assertNull(dto.getFechaCompra());
            assertNull(dto.getHoraCompra());
            assertNull(dto.getMetodoPagoId());
            assertNull(dto.getMetodoEnvioId());
            assertNull(dto.getEstadoId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            VentaUpdateDTO dto1 = new VentaUpdateDTO();
            dto1.setMetodoPagoId(1L);

            VentaUpdateDTO dto2 = new VentaUpdateDTO();
            dto2.setMetodoPagoId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals mismo objeto retorna true")
        void equalsMismoObjeto() {

            VentaUpdateDTO dto = new VentaUpdateDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals con null retorna false")
        void equalsConNull() {

            VentaUpdateDTO dto = new VentaUpdateDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals con clase distinta retorna false")
        void equalsConClaseDistinta() {

            VentaUpdateDTO dto = new VentaUpdateDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals distinta fecha retorna false")
        void equalsDistintaFecha() {

            VentaUpdateDTO dto1 = new VentaUpdateDTO();
            dto1.setFechaCompra(LocalDate.of(2025, 7, 6));

            VentaUpdateDTO dto2 = new VentaUpdateDTO();
            dto2.setFechaCompra(LocalDate.of(2025, 7, 7));

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinta hora retorna false")
        void equalsDistintaHora() {

            VentaUpdateDTO dto1 = new VentaUpdateDTO();
            dto1.setHoraCompra(LocalTime.of(14, 30));

            VentaUpdateDTO dto2 = new VentaUpdateDTO();
            dto2.setHoraCompra(LocalTime.of(15, 30));

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto metodo pago retorna false")
        void equalsDistintoMetodoPago() {

            VentaUpdateDTO dto1 = new VentaUpdateDTO();
            dto1.setMetodoPagoId(1L);

            VentaUpdateDTO dto2 = new VentaUpdateDTO();
            dto2.setMetodoPagoId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto metodo envio retorna false")
        void equalsDistintoMetodoEnvio() {

            VentaUpdateDTO dto1 = new VentaUpdateDTO();
            dto1.setMetodoEnvioId(1L);

            VentaUpdateDTO dto2 = new VentaUpdateDTO();
            dto2.setMetodoEnvioId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals distinto estado retorna false")
        void equalsDistintoEstado() {

            VentaUpdateDTO dto1 = new VentaUpdateDTO();
            dto1.setEstadoId(1L);

            VentaUpdateDTO dto2 = new VentaUpdateDTO();
            dto2.setEstadoId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando todos los campos son iguales")
        void equalsTodosLosCamposIguales() {

            VentaUpdateDTO dto1 = new VentaUpdateDTO();
            dto1.setFechaCompra(LocalDate.of(2025, 7, 6));
            dto1.setHoraCompra(LocalTime.of(14, 30));
            dto1.setMetodoPagoId(1L);
            dto1.setMetodoEnvioId(2L);
            dto1.setEstadoId(3L);

            VentaUpdateDTO dto2 = new VentaUpdateDTO();
            dto2.setFechaCompra(LocalDate.of(2025, 7, 6));
            dto2.setHoraCompra(LocalTime.of(14, 30));
            dto2.setMetodoPagoId(1L);
            dto2.setMetodoEnvioId(2L);
            dto2.setEstadoId(3L);

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode igual para objetos equivalentes")
        void hashCodeIgual() {

            VentaUpdateDTO dto1 = new VentaUpdateDTO();
            dto1.setMetodoPagoId(1L);
            dto1.setEstadoId(3L);

            VentaUpdateDTO dto2 = new VentaUpdateDTO();
            dto2.setMetodoPagoId(1L);
            dto2.setEstadoId(3L);

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode distinto cuando cambia metodo pago")
        void hashCodeDistinto() {

            VentaUpdateDTO dto1 = new VentaUpdateDTO();
            dto1.setMetodoPagoId(1L);

            VentaUpdateDTO dto2 = new VentaUpdateDTO();
            dto2.setMetodoPagoId(2L);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            VentaUpdateDTO dto = new VentaUpdateDTO();
            dto.setEstadoId(3L);

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("3"));
        }

        @Test
        @DisplayName("toString contiene estado y metodo pago")
        void toStringContieneCampos() {

            VentaUpdateDTO dto = new VentaUpdateDTO();
            dto.setMetodoPagoId(1L);
            dto.setEstadoId(3L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("1"));
            assertTrue(resultado.contains("3"));
        }

    }
}