package com.example.NoLimits.dto.ubicacion.uptade;

import com.example.NoLimits.Multimedia.dto.ubicacion.update.DireccionUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de DireccionUpdateDTO")
class DireccionUpdateDTOTest {

    private DireccionUpdateDTO crearDTO() {

        DireccionUpdateDTO dto = new DireccionUpdateDTO();
        dto.setCalle("Av. Providencia");
        dto.setNumero("1234");
        dto.setComplemento("Depto 402");
        dto.setCodigoPostal("7500000");
        dto.setComunaId(13114L);
        dto.setActivo(true);
        dto.setUsuarioId(5L);

        return dto;
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Asigna y obtiene valores correctamente")
        void testGettersAndSetters() {

            // Arrange
            DireccionUpdateDTO dto = new DireccionUpdateDTO();

            // Act
            dto.setCalle("Av. Providencia");
            dto.setNumero("1234");
            dto.setComplemento("Depto 402");
            dto.setCodigoPostal("7500000");
            dto.setComunaId(13114L);
            dto.setActivo(true);
            dto.setUsuarioId(5L);

            // Assert
            assertEquals("Av. Providencia", dto.getCalle());
            assertEquals("1234", dto.getNumero());
            assertEquals("Depto 402", dto.getComplemento());
            assertEquals("7500000", dto.getCodigoPostal());
            assertEquals(13114L, dto.getComunaId());
            assertTrue(dto.getActivo());
            assertEquals(5L, dto.getUsuarioId());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Objetos iguales")
        void testEqualsAndHashCode() {

            // Arrange
            DireccionUpdateDTO dto1 = crearDTO();
            DireccionUpdateDTO dto2 = crearDTO();

            // Assert
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Objetos diferentes")
        void testNotEquals() {

            // Arrange
            DireccionUpdateDTO dto1 = crearDTO();

            DireccionUpdateDTO dto2 = crearDTO();
            dto2.setNumero("9999");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Comparación con null")
        void testNotEqualsNull() {

            // Arrange
            DireccionUpdateDTO dto = crearDTO();

            // Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Comparación consigo mismo")
        void testEqualsSameInstance() {

            // Arrange
            DireccionUpdateDTO dto = crearDTO();

            // Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("Comparación con otra clase retorna false")
        void testEqualsOtraClase() {

            DireccionUpdateDTO dto = crearDTO();

            assertNotEquals("texto", dto);
        }

        @Test
        @DisplayName("Objetos vacíos son iguales")
        void testObjetosVaciosIguales() {

            DireccionUpdateDTO dto1 = new DireccionUpdateDTO();
            DireccionUpdateDTO dto2 = new DireccionUpdateDTO();

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Diferente calle")
        void testNotEqualsCalle() {

            DireccionUpdateDTO dto1 = crearDTO();
            DireccionUpdateDTO dto2 = crearDTO();

            dto2.setCalle("Otra calle");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente complemento")
        void testNotEqualsComplemento() {

            DireccionUpdateDTO dto1 = crearDTO();
            DireccionUpdateDTO dto2 = crearDTO();

            dto2.setComplemento("Casa");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente codigo postal")
        void testNotEqualsCodigoPostal() {

            DireccionUpdateDTO dto1 = crearDTO();
            DireccionUpdateDTO dto2 = crearDTO();

            dto2.setCodigoPostal("9999999");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente comunaId")
        void testNotEqualsComunaId() {

            DireccionUpdateDTO dto1 = crearDTO();
            DireccionUpdateDTO dto2 = crearDTO();

            dto2.setComunaId(999L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente activo")
        void testNotEqualsActivo() {

            DireccionUpdateDTO dto1 = crearDTO();
            DireccionUpdateDTO dto2 = crearDTO();

            dto2.setActivo(false);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Diferente usuarioId")
        void testNotEqualsUsuarioId() {

            DireccionUpdateDTO dto1 = crearDTO();
            DireccionUpdateDTO dto2 = crearDTO();

            dto2.setUsuarioId(99L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor en calle")
        void testNullVsValorCalle() {

            DireccionUpdateDTO dto1 = new DireccionUpdateDTO();

            DireccionUpdateDTO dto2 = new DireccionUpdateDTO();
            dto2.setCalle("Av. Providencia");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor en numero")
        void testNullVsValorNumero() {

            DireccionUpdateDTO dto1 = new DireccionUpdateDTO();

            DireccionUpdateDTO dto2 = new DireccionUpdateDTO();
            dto2.setNumero("1234");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor en comunaId")
        void testNullVsValorComunaId() {

            DireccionUpdateDTO dto1 = new DireccionUpdateDTO();

            DireccionUpdateDTO dto2 = new DireccionUpdateDTO();
            dto2.setComunaId(13114L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Null vs valor en usuarioId")
        void testNullVsValorUsuarioId() {

            DireccionUpdateDTO dto1 = new DireccionUpdateDTO();

            DireccionUpdateDTO dto2 = new DireccionUpdateDTO();
            dto2.setUsuarioId(5L);

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
            DireccionUpdateDTO dto = crearDTO();

            // Act
            String result = dto.toString();

            // Assert
            assertNotNull(result);
            assertTrue(result.contains("Av. Providencia"));
            assertTrue(result.contains("1234"));
            assertTrue(result.contains("7500000"));
        }

        @Test
        @DisplayName("ToString objeto vacío")
        void testToStringVacio() {

            assertNotNull(new DireccionUpdateDTO().toString());
        }
    }

}