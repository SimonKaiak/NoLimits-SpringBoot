package com.example.NoLimits.dto.ubicacion.uptade;

import com.example.NoLimits.Multimedia.dto.ubicacion.update.RegionUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de RegionUpdateDTO")
class RegionUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Asigna y obtiene nombre correctamente")
        void testGettersAndSetters() {

            // Arrange
            RegionUpdateDTO dto = new RegionUpdateDTO();

            // Act
            dto.setNombre("Región de Valparaíso");

            // Assert
            assertEquals(
                    "Región de Valparaíso",
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
            RegionUpdateDTO dto1 = crearDTO();
            RegionUpdateDTO dto2 = crearDTO();

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
            RegionUpdateDTO dto1 = crearDTO();

            RegionUpdateDTO dto2 = crearDTO();
            dto2.setNombre("Región Metropolitana");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Comparación con null")
        void testNotEqualsNull() {

            // Arrange
            RegionUpdateDTO dto = crearDTO();

            // Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Comparación consigo mismo")
        void testEqualsSameInstance() {

            // Arrange
            RegionUpdateDTO dto = crearDTO();

            // Assert
            assertEquals(dto, dto);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTests {

        @Test
        @DisplayName("Genera representación textual")
        void testToString() {

            // Arrange
            RegionUpdateDTO dto = crearDTO();

            // Act
            String result = dto.toString();

            // Assert
            assertNotNull(result);
            assertTrue(
                    result.contains("Región de Valparaíso")
            );
        }
    }

    private RegionUpdateDTO crearDTO() {

        RegionUpdateDTO dto = new RegionUpdateDTO();
        dto.setNombre("Región de Valparaíso");

        return dto;
    }
}