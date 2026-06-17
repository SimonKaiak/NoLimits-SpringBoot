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

        @Test
        @DisplayName("Comparación con otra clase")
        void testNotEqualsDifferentClass() {

            // Arrange
            DireccionRequestDTO dto = crearDTO();

            // Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("Null vs valor calle")
        void nullVsValorCalle() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();

            DireccionRequestDTO dto2 = new DireccionRequestDTO();
            dto2.setCalle("Av. Siempre Viva");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Valor vs null calle")
        void valorVsNullCalle() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();
            dto1.setCalle("Av. Siempre Viva");

            DireccionRequestDTO dto2 = new DireccionRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor complemento")
        void nullVsValorComplemento() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();

            DireccionRequestDTO dto2 = new DireccionRequestDTO();
            dto2.setComplemento("Depto 101");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Valor vs null complemento")
        void valorVsNullComplemento() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();
            dto1.setComplemento("Depto 101");

            DireccionRequestDTO dto2 = new DireccionRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor codigo postal")
        void nullVsValorCodigoPostal() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();

            DireccionRequestDTO dto2 = new DireccionRequestDTO();
            dto2.setCodigoPostal("8320000");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Valor vs null codigo postal")
        void valorVsNullCodigoPostal() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();
            dto1.setCodigoPostal("8320000");

            DireccionRequestDTO dto2 = new DireccionRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor comunaId")
        void nullVsValorComunaId() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();

            DireccionRequestDTO dto2 = new DireccionRequestDTO();
            dto2.setComunaId(13101L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Valor vs null comunaId")
        void valorVsNullComunaId() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();
            dto1.setComunaId(13101L);

            DireccionRequestDTO dto2 = new DireccionRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor usuarioId")
        void nullVsValorUsuarioId() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();

            DireccionRequestDTO dto2 = new DireccionRequestDTO();
            dto2.setUsuarioId(5L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Valor vs null usuarioId")
        void valorVsNullUsuarioId() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();
            dto1.setUsuarioId(5L);

            DireccionRequestDTO dto2 = new DireccionRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor activo")
        void nullVsValorActivo() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();

            DireccionRequestDTO dto2 = new DireccionRequestDTO();
            dto2.setActivo(true);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Valor vs null activo")
        void valorVsNullActivo() {

            DireccionRequestDTO dto1 = new DireccionRequestDTO();
            dto1.setActivo(true);

            DireccionRequestDTO dto2 = new DireccionRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente calle")
        void diferenteCalle() {

            DireccionRequestDTO dto1 = crearDTO();

            DireccionRequestDTO dto2 = crearDTO();
            dto2.setCalle("Otra calle");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente complemento")
        void diferenteComplemento() {

            DireccionRequestDTO dto1 = crearDTO();

            DireccionRequestDTO dto2 = crearDTO();
            dto2.setComplemento("Otro depto");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente codigo postal")
        void diferenteCodigoPostal() {

            DireccionRequestDTO dto1 = crearDTO();

            DireccionRequestDTO dto2 = crearDTO();
            dto2.setCodigoPostal("9999999");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente comunaId")
        void diferenteComunaId() {

            DireccionRequestDTO dto1 = crearDTO();

            DireccionRequestDTO dto2 = crearDTO();
            dto2.setComunaId(999L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente usuarioId")
        void diferenteUsuarioId() {

            DireccionRequestDTO dto1 = crearDTO();

            DireccionRequestDTO dto2 = crearDTO();
            dto2.setUsuarioId(999L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente activo")
        void diferenteActivo() {

            DireccionRequestDTO dto1 = crearDTO();

            DireccionRequestDTO dto2 = crearDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1, dto2);
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