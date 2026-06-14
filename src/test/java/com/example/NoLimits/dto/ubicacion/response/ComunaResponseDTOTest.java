package com.example.NoLimits.dto.ubicacion.response;

import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de ComunaResponseDTO")
class ComunaResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Asigna y obtiene valores correctamente")
        void testGettersAndSetters() {

            // Arrange
            ComunaResponseDTO dto = new ComunaResponseDTO();

            // Act
            dto.setId(13101L);
            dto.setNombre("Santiago");
            dto.setRegionId(13L);
            dto.setRegionNombre("Región Metropolitana de Santiago");

            // Assert
            assertEquals(13101L, dto.getId());
            assertEquals("Santiago", dto.getNombre());
            assertEquals(13L, dto.getRegionId());
            assertEquals(
                    "Región Metropolitana de Santiago",
                    dto.getRegionNombre()
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
            ComunaResponseDTO dto1 = crearDTO();
            ComunaResponseDTO dto2 = crearDTO();

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
            ComunaResponseDTO dto1 = crearDTO();

            ComunaResponseDTO dto2 = crearDTO();
            dto2.setNombre("Providencia");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Comparación con null")
        void testNotEqualsNull() {

            // Arrange
            ComunaResponseDTO dto = crearDTO();

            // Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Comparación consigo mismo")
        void testEqualsSameInstance() {

            // Arrange
            ComunaResponseDTO dto = crearDTO();

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
            ComunaResponseDTO dto = crearDTO();

            // Act
            String result = dto.toString();

            // Assert
            assertNotNull(result);
            assertTrue(result.contains("Santiago"));
            assertTrue(result.contains("13101"));
            assertTrue(result.contains("13"));
        }
    }

    private ComunaResponseDTO crearDTO() {

        ComunaResponseDTO dto = new ComunaResponseDTO();
        dto.setId(13101L);
        dto.setNombre("Santiago");
        dto.setRegionId(13L);
        dto.setRegionNombre("Región Metropolitana de Santiago");

        return dto;
    }
}
