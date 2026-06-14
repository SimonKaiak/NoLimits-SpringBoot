package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.GeneroUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GeneroUpdateDTO")
class GeneroUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            GeneroUpdateDTO dto = new GeneroUpdateDTO();

            dto.setNombre("Aventura");

            assertEquals("Aventura", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            GeneroUpdateDTO dto = new GeneroUpdateDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            GeneroUpdateDTO dto1 = new GeneroUpdateDTO();
            dto1.setNombre("Aventura");

            GeneroUpdateDTO dto2 = new GeneroUpdateDTO();
            dto2.setNombre("Aventura");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            GeneroUpdateDTO dto1 = new GeneroUpdateDTO();
            dto1.setNombre("Aventura");

            GeneroUpdateDTO dto2 = new GeneroUpdateDTO();
            dto2.setNombre("Acción");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            GeneroUpdateDTO dto = new GeneroUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            GeneroUpdateDTO dto = new GeneroUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            GeneroUpdateDTO dto = new GeneroUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            GeneroUpdateDTO dto1 = new GeneroUpdateDTO();
            dto1.setNombre("Aventura");

            GeneroUpdateDTO dto2 = new GeneroUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen nombre null")
        void testEqualsAmbosNulos() {

            // Arrange
            GeneroUpdateDTO dto1 = new GeneroUpdateDTO();

            GeneroUpdateDTO dto2 = new GeneroUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            GeneroUpdateDTO dto1 = new GeneroUpdateDTO();
            dto1.setNombre("Aventura");

            GeneroUpdateDTO dto2 = new GeneroUpdateDTO();
            dto2.setNombre("Acción");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual cuando los objetos son iguales")
        void testHashCodeIgual() {

            // Arrange
            GeneroUpdateDTO dto1 = new GeneroUpdateDTO();
            dto1.setNombre("Aventura");

            GeneroUpdateDTO dto2 = new GeneroUpdateDTO();
            dto2.setNombre("Aventura");

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            GeneroUpdateDTO dto = new GeneroUpdateDTO();
            dto.setNombre("Aventura");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            GeneroUpdateDTO dto = new GeneroUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("toString contiene nombre cuando está informado")
        void testToStringContieneNombre() {

            // Arrange
            GeneroUpdateDTO dto = new GeneroUpdateDTO();
            dto.setNombre("Aventura");

            // Act
            String resultado = dto.toString();

            // Assert
            assertTrue(resultado.contains("Aventura"));
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            GeneroUpdateDTO dto = new GeneroUpdateDTO();
            dto.setNombre("Aventura");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Aventura"));
        }
    }
}