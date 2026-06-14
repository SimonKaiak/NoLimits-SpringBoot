package com.example.NoLimits.dto.ubicacion.request;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de DireccionRequestDTO")
class DireccionRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Asigna y obtiene valores correctamente")
        void testGettersAndSetters() {

            // Arrange
            DireccionRequestDTO dto = new DireccionRequestDTO();

            // Act
            dto.setCalle("Av. Siempre Viva");
            dto.setNumero("742");
            dto.setComplemento("Depto 101");
            dto.setCodigoPostal("8320000");
            dto.setComunaId(13101L);
            dto.setUsuarioId(5L);
            dto.setActivo(true);

            // Assert
            assertEquals("Av. Siempre Viva", dto.getCalle());
            assertEquals("742", dto.getNumero());
            assertEquals("Depto 101", dto.getComplemento());
            assertEquals("8320000", dto.getCodigoPostal());
            assertEquals(13101L, dto.getComunaId());
            assertEquals(5L, dto.getUsuarioId());
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
            DireccionRequestDTO dto1 = crearDTO();
            DireccionRequestDTO dto2 = crearDTO();

            // Assert
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Objetos diferentes")
        void testNotEquals() {

            // Arrange
            DireccionRequestDTO dto1 = crearDTO();

            DireccionRequestDTO dto2 = crearDTO();
            dto2.setNumero("999");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Comparación con null")
        void testNotEqualsNull() {

            // Arrange
            DireccionRequestDTO dto = crearDTO();

            // Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Comparación consigo mismo")
        void testEqualsSameInstance() {

            // Arrange
            DireccionRequestDTO dto = crearDTO();

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
            DireccionRequestDTO dto = crearDTO();

            // Act
            String result = dto.toString();

            // Assert
            assertNotNull(result);
            assertTrue(result.contains("Av. Siempre Viva"));
            assertTrue(result.contains("742"));
            assertTrue(result.contains("8320000"));
        }
    }

    private DireccionRequestDTO crearDTO() {

        DireccionRequestDTO dto = new DireccionRequestDTO();
        dto.setCalle("Av. Siempre Viva");
        dto.setNumero("742");
        dto.setComplemento("Depto 101");
        dto.setCodigoPostal("8320000");
        dto.setComunaId(13101L);
        dto.setUsuarioId(5L);
        dto.setActivo(true);

        return dto;
    }
}