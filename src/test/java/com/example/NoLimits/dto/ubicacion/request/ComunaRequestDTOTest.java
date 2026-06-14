package com.example.NoLimits.dto.ubicacion.request;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.ComunaRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de ComunaRequestDTO")
class ComunaRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Asigna y obtiene valores correctamente")
        void testGettersAndSetters() {

            // Arrange
            ComunaRequestDTO dto = new ComunaRequestDTO();

            // Act
            dto.setNombre("Santiago");
            dto.setRegionId(13L);

            // Assert
            assertEquals("Santiago", dto.getNombre());
            assertEquals(13L, dto.getRegionId());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Objetos iguales")
        void testEqualsAndHashCode() {

            // Arrange
            ComunaRequestDTO dto1 = new ComunaRequestDTO();
            dto1.setNombre("Santiago");
            dto1.setRegionId(13L);

            ComunaRequestDTO dto2 = new ComunaRequestDTO();
            dto2.setNombre("Santiago");
            dto2.setRegionId(13L);

            // Assert
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Objetos diferentes")
        void testNotEquals() {

            // Arrange
            ComunaRequestDTO dto1 = new ComunaRequestDTO();
            dto1.setNombre("Santiago");
            dto1.setRegionId(13L);

            ComunaRequestDTO dto2 = new ComunaRequestDTO();
            dto2.setNombre("Providencia");
            dto2.setRegionId(14L);

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Comparación con null")
        void testNotEqualsNull() {

            // Arrange
            ComunaRequestDTO dto = new ComunaRequestDTO();

            // Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Comparación consigo mismo")
        void testEqualsSameInstance() {

            // Arrange
            ComunaRequestDTO dto = new ComunaRequestDTO();

            // Assert
            assertEquals(dto, dto);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTests {

        @Test
        @DisplayName("Genera representación de texto")
        void testToString() {

            // Arrange
            ComunaRequestDTO dto = new ComunaRequestDTO();
            dto.setNombre("Santiago");
            dto.setRegionId(13L);

            // Act
            String result = dto.toString();

            // Assert
            assertNotNull(result);
            assertTrue(result.contains("Santiago"));
            assertTrue(result.contains("13"));
        }
    }
}
