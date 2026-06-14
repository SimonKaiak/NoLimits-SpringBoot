package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposDeDesarrolladorUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposDeDesarrolladorUpdateDTO")
class TiposDeDesarrolladorUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();

            dto.setDesarrolladorId(3L);
            dto.setTipoDeDesarrolladorId(2L);

            assertEquals(3L, dto.getDesarrolladorId());
            assertEquals(2L, dto.getTipoDeDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();

            assertNull(dto.getDesarrolladorId());
            assertNull(dto.getTipoDeDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TiposDeDesarrolladorUpdateDTO dto1 = new TiposDeDesarrolladorUpdateDTO();
            dto1.setDesarrolladorId(3L);
            dto1.setTipoDeDesarrolladorId(2L);

            TiposDeDesarrolladorUpdateDTO dto2 = new TiposDeDesarrolladorUpdateDTO();
            dto2.setDesarrolladorId(3L);
            dto2.setTipoDeDesarrolladorId(2L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia desarrolladorId")
        void testEqualsDesarrolladorIdDistinto() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto1 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto1.setDesarrolladorId(3L);

            TiposDeDesarrolladorUpdateDTO dto2 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto2.setDesarrolladorId(4L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia tipoDeDesarrolladorId")
        void testEqualsTipoDeDesarrolladorIdDistinto() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto1 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto1.setTipoDeDesarrolladorId(2L);

            TiposDeDesarrolladorUpdateDTO dto2 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto2.setTipoDeDesarrolladorId(5L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto =
                    new TiposDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto =
                    new TiposDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto =
                    new TiposDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene desarrolladorId")
        void testEqualsDesarrolladorIdNuloVsNoNulo() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto1 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto1.setDesarrolladorId(3L);

            TiposDeDesarrolladorUpdateDTO dto2 =
                    new TiposDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene tipoDeDesarrolladorId")
        void testEqualsTipoDeDesarrolladorIdNuloVsNoNulo() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto1 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto1.setTipoDeDesarrolladorId(2L);

            TiposDeDesarrolladorUpdateDTO dto2 =
                    new TiposDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto1 =
                    new TiposDeDesarrolladorUpdateDTO();

            TiposDeDesarrolladorUpdateDTO dto2 =
                    new TiposDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia desarrolladorId")
        void testHashCodeDesarrolladorIdDistinto() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto1 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto1.setDesarrolladorId(3L);

            TiposDeDesarrolladorUpdateDTO dto2 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto2.setDesarrolladorId(4L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia tipoDeDesarrolladorId")
        void testHashCodeTipoDeDesarrolladorIdDistinto() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto1 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto1.setTipoDeDesarrolladorId(2L);

            TiposDeDesarrolladorUpdateDTO dto2 =
                    new TiposDeDesarrolladorUpdateDTO();
            dto2.setTipoDeDesarrolladorId(5L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente")
        void testHashCodeConsistente() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto =
                    new TiposDeDesarrolladorUpdateDTO();
            dto.setDesarrolladorId(3L);
            dto.setTipoDeDesarrolladorId(2L);

            // Act
            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            TiposDeDesarrolladorUpdateDTO dto =
                    new TiposDeDesarrolladorUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();
            dto.setDesarrolladorId(3L);
            dto.setTipoDeDesarrolladorId(2L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("desarrolladorId=3"));
            assertTrue(resultado.contains("tipoDeDesarrolladorId=2"));
        }
    }
}