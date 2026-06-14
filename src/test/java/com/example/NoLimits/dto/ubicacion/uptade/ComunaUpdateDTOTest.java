package com.example.NoLimits.dto.ubicacion.uptade;

import com.example.NoLimits.Multimedia.dto.ubicacion.update.ComunaUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de ComunaUpdateDTO")
class ComunaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Asigna y obtiene valores correctamente")
        void testGettersAndSetters() {

            // Arrange
            ComunaUpdateDTO dto = new ComunaUpdateDTO();

            // Act
            dto.setNombre("Santiago Centro");
            dto.setRegionId(13L);

            // Assert
            assertEquals("Santiago Centro", dto.getNombre());
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
            ComunaUpdateDTO dto1 = crearDTO();
            ComunaUpdateDTO dto2 = crearDTO();

            // Assert
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Objetos diferentes")
        void testNotEquals() {

            // Arrange
            ComunaUpdateDTO dto1 = crearDTO();

            ComunaUpdateDTO dto2 = crearDTO();
            dto2.setNombre("Providencia");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Comparación con null")
        void testNotEqualsNull() {

            // Arrange
            ComunaUpdateDTO dto = crearDTO();

            // Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Comparación consigo mismo")
        void testEqualsSameInstance() {

            // Arrange
            ComunaUpdateDTO dto = crearDTO();

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
            ComunaUpdateDTO dto = crearDTO();

            // Act
            String result = dto.toString();

            // Assert
            assertNotNull(result);
            assertTrue(result.contains("Santiago Centro"));
            assertTrue(result.contains("13"));
        }
    }

    private ComunaUpdateDTO crearDTO() {

        ComunaUpdateDTO dto = new ComunaUpdateDTO();
        dto.setNombre("Santiago Centro");
        dto.setRegionId(13L);

        return dto;
    }
}