package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.PlataformaUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PlataformaUpdateDTO")
class PlataformaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();

            dto.setNombre("PlayStation 5");

            assertEquals("PlayStation 5", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            PlataformaUpdateDTO dto1 = new PlataformaUpdateDTO();
            dto1.setNombre("PlayStation 5");

            PlataformaUpdateDTO dto2 = new PlataformaUpdateDTO();
            dto2.setNombre("PlayStation 5");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            PlataformaUpdateDTO dto1 = new PlataformaUpdateDTO();
            dto1.setNombre("PlayStation 5");

            PlataformaUpdateDTO dto2 = new PlataformaUpdateDTO();
            dto2.setNombre("Xbox Series X");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            PlataformaUpdateDTO dto1 = new PlataformaUpdateDTO();
            dto1.setNombre("PlayStation 5");

            PlataformaUpdateDTO dto2 = new PlataformaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen nombre null")
        void testEqualsAmbosNulos() {

            // Arrange
            PlataformaUpdateDTO dto1 = new PlataformaUpdateDTO();
            PlataformaUpdateDTO dto2 = new PlataformaUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            PlataformaUpdateDTO dto1 = new PlataformaUpdateDTO();
            dto1.setNombre("PlayStation 5");

            PlataformaUpdateDTO dto2 = new PlataformaUpdateDTO();
            dto2.setNombre("Xbox Series X");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual para objetos equivalentes")
        void testHashCodeIgual() {

            // Arrange
            PlataformaUpdateDTO dto1 = new PlataformaUpdateDTO();
            dto1.setNombre("PlayStation 5");

            PlataformaUpdateDTO dto2 = new PlataformaUpdateDTO();
            dto2.setNombre("PlayStation 5");

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();
            dto.setNombre("PlayStation 5");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("toString contiene nombre cuando está informado")
        void testToStringContieneNombre() {

            // Arrange
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();
            dto.setNombre("PlayStation 5");

            // Act
            String resultado = dto.toString();

            // Assert
            assertTrue(resultado.contains("PlayStation 5"));
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();
            dto.setNombre("PlayStation 5");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=PlayStation 5"));
        }
    }
}