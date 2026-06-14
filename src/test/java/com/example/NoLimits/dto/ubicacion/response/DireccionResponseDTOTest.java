package com.example.NoLimits.dto.ubicacion.response;

import com.example.NoLimits.Multimedia.dto.ubicacion.response.DireccionResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de DireccionResponseDTO")
class DireccionResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Asigna y obtiene valores correctamente")
        void testGettersAndSetters() {

            // Arrange
            DireccionResponseDTO dto = new DireccionResponseDTO();

            // Act
            dto.setId(1L);
            dto.setCalle("Av. Siempre Viva");
            dto.setNumero("742");
            dto.setComplemento("Depto 1204-B");
            dto.setCodigoPostal("8320000");
            dto.setComuna("Santiago Centro");
            dto.setRegion("Metropolitana");
            dto.setActivo(true);

            // Assert
            assertEquals(1L, dto.getId());
            assertEquals("Av. Siempre Viva", dto.getCalle());
            assertEquals("742", dto.getNumero());
            assertEquals("Depto 1204-B", dto.getComplemento());
            assertEquals("8320000", dto.getCodigoPostal());
            assertEquals("Santiago Centro", dto.getComuna());
            assertEquals("Metropolitana", dto.getRegion());
            assertTrue(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Objetos iguales")
        void testEqualsAndHashCode() {

            // Arrange
            DireccionResponseDTO dto1 = crearDTO();
            DireccionResponseDTO dto2 = crearDTO();

            // Assert
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Objetos diferentes")
        void testNotEquals() {

            // Arrange
            DireccionResponseDTO dto1 = crearDTO();

            DireccionResponseDTO dto2 = crearDTO();
            dto2.setNumero("999");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Comparación con null")
        void testNotEqualsNull() {

            // Arrange
            DireccionResponseDTO dto = crearDTO();

            // Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Comparación consigo mismo")
        void testEqualsSameInstance() {

            // Arrange
            DireccionResponseDTO dto = crearDTO();

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
            DireccionResponseDTO dto = crearDTO();

            // Act
            String result = dto.toString();

            // Assert
            assertNotNull(result);
            assertTrue(result.contains("Av. Siempre Viva"));
            assertTrue(result.contains("742"));
            assertTrue(result.contains("Santiago Centro"));
        }
    }

    private DireccionResponseDTO crearDTO() {

        DireccionResponseDTO dto = new DireccionResponseDTO();
        dto.setId(1L);
        dto.setCalle("Av. Siempre Viva");
        dto.setNumero("742");
        dto.setComplemento("Depto 1204-B");
        dto.setCodigoPostal("8320000");
        dto.setComuna("Santiago Centro");
        dto.setRegion("Metropolitana");
        dto.setActivo(true);

        return dto;
    }
}