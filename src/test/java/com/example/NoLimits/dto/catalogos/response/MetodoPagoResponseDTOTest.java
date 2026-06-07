package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoPagoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoPagoResponseDTO")
class MetodoPagoResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();

            dto.setId(1L);
            dto.setNombre("Tarjeta de Crédito");
            dto.setActivo(true);

            assertEquals(1L, dto.getId());
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
            MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();

            assertNull(dto.getId());
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
            MetodoPagoResponseDTO dto1 = new MetodoPagoResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Tarjeta de Crédito");
            dto1.setActivo(true);

            MetodoPagoResponseDTO dto2 = new MetodoPagoResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Tarjeta de Crédito");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();
            dto.setId(1L);
            dto.setNombre("Tarjeta de Crédito");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Tarjeta de Crédito"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}