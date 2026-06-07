package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoPagoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoPagoRequestDTO")
class MetodoPagoRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();

            dto.setNombre("Tarjeta de Crédito");
            dto.setActivo(true);

            assertEquals("Tarjeta de Crédito", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();

            assertNull(dto.getNombre());
            assertNull(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setNombre("Tarjeta de Crédito");
            dto1.setActivo(true);

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();
            dto2.setNombre("Tarjeta de Crédito");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();
            dto.setNombre("Tarjeta de Crédito");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Tarjeta de Crédito"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}