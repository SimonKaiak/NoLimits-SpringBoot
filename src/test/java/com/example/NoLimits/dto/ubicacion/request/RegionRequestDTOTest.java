package com.example.NoLimits.dto.ubicacion.request;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.RegionRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de RegionRequestDTO")
class RegionRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Asigna y obtiene nombre correctamente")
        void testGettersAndSetters() {

            // Arrange
            RegionRequestDTO dto = new RegionRequestDTO();

            // Act
            dto.setNombre("Región Metropolitana");

            // Assert
            assertEquals(
                    "Región Metropolitana",
                    dto.getNombre()
            );
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Objetos iguales")
        void testEqualsAndHashCode() {

            // Arrange
            RegionRequestDTO dto1 = new RegionRequestDTO();
            dto1.setNombre("Región Metropolitana");

            RegionRequestDTO dto2 = new RegionRequestDTO();
            dto2.setNombre("Región Metropolitana");

            // Assert
            assertEquals(dto1, dto2);
            assertEquals(
                    dto1.hashCode(),
                    dto2.hashCode()
            );
        }

        @Test
        @DisplayName("Objetos diferentes")
        void testNotEquals() {

            // Arrange
            RegionRequestDTO dto1 = new RegionRequestDTO();
            dto1.setNombre("Región Metropolitana");

            RegionRequestDTO dto2 = new RegionRequestDTO();
            dto2.setNombre("Valparaíso");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Comparación con null")
        void testNotEqualsNull() {

            // Arrange
            RegionRequestDTO dto = new RegionRequestDTO();

            // Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Comparación consigo mismo")
        void testEqualsSameInstance() {

            // Arrange
            RegionRequestDTO dto = new RegionRequestDTO();

            // Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("Comparación con otra clase")
        void testNotEqualsDifferentClass() {

            RegionRequestDTO dto = new RegionRequestDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("Objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            RegionRequestDTO dto1 = new RegionRequestDTO();
            RegionRequestDTO dto2 = new RegionRequestDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Objetos vacíos tienen mismo hashCode")
        void objetosVaciosMismoHashCode() {

            RegionRequestDTO dto1 = new RegionRequestDTO();
            RegionRequestDTO dto2 = new RegionRequestDTO();

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Null vs valor nombre")
        void nullVsValorNombre() {

            RegionRequestDTO dto1 = new RegionRequestDTO();

            RegionRequestDTO dto2 = new RegionRequestDTO();
            dto2.setNombre("Región Metropolitana");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Valor vs null nombre")
        void valorVsNullNombre() {

            RegionRequestDTO dto1 = new RegionRequestDTO();
            dto1.setNombre("Región Metropolitana");

            RegionRequestDTO dto2 = new RegionRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("HashCode objeto vacío")
        void hashCodeObjetoVacio() {

            RegionRequestDTO dto = new RegionRequestDTO();

            assertNotEquals(0, dto.hashCode());
        }

        @Test
        @DisplayName("HashCode cambia cuando cambia nombre")
        void hashCodeDistintoNombre() {

            RegionRequestDTO dto1 = new RegionRequestDTO();
            dto1.setNombre("Región Metropolitana");

            RegionRequestDTO dto2 = new RegionRequestDTO();
            dto2.setNombre("Valparaíso");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTests {

        @Test
        @DisplayName("Genera representación textual")
        void testToString() {

            // Arrange
            RegionRequestDTO dto = new RegionRequestDTO();
            dto.setNombre("Región Metropolitana");

            // Act
            String result = dto.toString();

            // Assert
            assertNotNull(result);
            assertTrue(
                    result.contains("Región Metropolitana")
            );
        }
    }
}