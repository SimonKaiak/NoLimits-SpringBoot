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

        @Test
        @DisplayName("Comparación con otra clase")
        void testNotEqualsDifferentClass() {

            ComunaRequestDTO dto = new ComunaRequestDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("Objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            ComunaRequestDTO dto1 = new ComunaRequestDTO();
            ComunaRequestDTO dto2 = new ComunaRequestDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Objetos vacíos tienen mismo hashCode")
        void objetosVaciosHashCode() {

            ComunaRequestDTO dto1 = new ComunaRequestDTO();
            ComunaRequestDTO dto2 = new ComunaRequestDTO();

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Null vs valor nombre")
        void nullVsValorNombre() {

            ComunaRequestDTO dto1 = new ComunaRequestDTO();

            ComunaRequestDTO dto2 = new ComunaRequestDTO();
            dto2.setNombre("Santiago");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Valor vs null nombre")
        void valorVsNullNombre() {

            ComunaRequestDTO dto1 = new ComunaRequestDTO();
            dto1.setNombre("Santiago");

            ComunaRequestDTO dto2 = new ComunaRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor regionId")
        void nullVsValorRegionId() {

            ComunaRequestDTO dto1 = new ComunaRequestDTO();

            ComunaRequestDTO dto2 = new ComunaRequestDTO();
            dto2.setRegionId(13L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Valor vs null regionId")
        void valorVsNullRegionId() {

            ComunaRequestDTO dto1 = new ComunaRequestDTO();
            dto1.setRegionId(13L);

            ComunaRequestDTO dto2 = new ComunaRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente nombre")
        void diferenteNombre() {

            ComunaRequestDTO dto1 = new ComunaRequestDTO();
            dto1.setNombre("Santiago");
            dto1.setRegionId(13L);

            ComunaRequestDTO dto2 = new ComunaRequestDTO();
            dto2.setNombre("Providencia");
            dto2.setRegionId(13L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente regionId")
        void diferenteRegionId() {

            ComunaRequestDTO dto1 = new ComunaRequestDTO();
            dto1.setNombre("Santiago");
            dto1.setRegionId(13L);

            ComunaRequestDTO dto2 = new ComunaRequestDTO();
            dto2.setNombre("Santiago");
            dto2.setRegionId(14L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("HashCode objeto vacío")
        void hashCodeObjetoVacio() {

            ComunaRequestDTO dto = new ComunaRequestDTO();

            assertNotEquals(0, dto.hashCode());
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
