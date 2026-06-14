package com.example.NoLimits.dto.ubicacion.response;

import com.example.NoLimits.Multimedia.dto.ubicacion.response.RegionResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de RegionResponseDTO")
class RegionResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Asigna y obtiene valores correctamente")
        void testGettersAndSetters() {

            // Arrange
            RegionResponseDTO dto = new RegionResponseDTO();

            // Act
            dto.setId(13L);
            dto.setNombre("Región Metropolitana de Santiago");

            // Assert
            assertEquals(13L, dto.getId());
            assertEquals(
                    "Región Metropolitana de Santiago",
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
            RegionResponseDTO dto1 = crearDTO();
            RegionResponseDTO dto2 = crearDTO();

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
            RegionResponseDTO dto1 = crearDTO();

            RegionResponseDTO dto2 = crearDTO();
            dto2.setNombre("Valparaíso");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Comparación con null")
        void testNotEqualsNull() {

            // Arrange
            RegionResponseDTO dto = crearDTO();

            // Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Comparación consigo mismo")
        void testEqualsSameInstance() {

            // Arrange
            RegionResponseDTO dto = crearDTO();

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
            RegionResponseDTO dto = crearDTO();

            // Act
            String result = dto.toString();

            // Assert
            assertNotNull(result);
            assertTrue(
                    result.contains(
                            "Región Metropolitana de Santiago"
                    )
            );
            assertTrue(result.contains("13"));
        }
    }

    private RegionResponseDTO crearDTO() {

        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(13L);
        dto.setNombre("Región Metropolitana de Santiago");

        return dto;
    }
}