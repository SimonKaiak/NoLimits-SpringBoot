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
        @DisplayName("genera toString correctamente")
        void testToString() {

            VentaUpdateDTO dto = new VentaUpdateDTO();
            dto.setEstadoId(3L);

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("3"));
        }
    }
}