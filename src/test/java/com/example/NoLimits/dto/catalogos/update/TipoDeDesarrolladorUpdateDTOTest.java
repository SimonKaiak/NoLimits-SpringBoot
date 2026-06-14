package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoDeDesarrolladorUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoDeDesarrolladorUpdateDTO")
class TipoDeDesarrolladorUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            TipoDeDesarrolladorUpdateDTO dto = new TipoDeDesarrolladorUpdateDTO();

            dto.setNombre("Publisher");

            assertEquals("Publisher", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoDeDesarrolladorUpdateDTO dto = new TipoDeDesarrolladorUpdateDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoDeDesarrolladorUpdateDTO dto1 = new TipoDeDesarrolladorUpdateDTO();
            dto1.setNombre("Publisher");

            TipoDeDesarrolladorUpdateDTO dto2 = new TipoDeDesarrolladorUpdateDTO();
            dto2.setNombre("Publisher");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto1 =
                    new TipoDeDesarrolladorUpdateDTO();
            dto1.setNombre("Publisher");

            TipoDeDesarrolladorUpdateDTO dto2 =
                    new TipoDeDesarrolladorUpdateDTO();
            dto2.setNombre("Estudio");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto =
                    new TipoDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto =
                    new TipoDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto =
                    new TipoDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto1 =
                    new TipoDeDesarrolladorUpdateDTO();
            dto1.setNombre("Publisher");

            TipoDeDesarrolladorUpdateDTO dto2 =
                    new TipoDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen nombre null")
        void testEqualsAmbosNulos() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto1 =
                    new TipoDeDesarrolladorUpdateDTO();

            TipoDeDesarrolladorUpdateDTO dto2 =
                    new TipoDeDesarrolladorUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto1 =
                    new TipoDeDesarrolladorUpdateDTO();
            dto1.setNombre("Publisher");

            TipoDeDesarrolladorUpdateDTO dto2 =
                    new TipoDeDesarrolladorUpdateDTO();
            dto2.setNombre("Estudio");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual para objetos equivalentes")
        void testHashCodeIgual() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto1 =
                    new TipoDeDesarrolladorUpdateDTO();
            dto1.setNombre("Publisher");

            TipoDeDesarrolladorUpdateDTO dto2 =
                    new TipoDeDesarrolladorUpdateDTO();
            dto2.setNombre("Publisher");

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto =
                    new TipoDeDesarrolladorUpdateDTO();
            dto.setNombre("Publisher");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto =
                    new TipoDeDesarrolladorUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("toString contiene nombre cuando está informado")
        void testToStringContieneNombre() {

            // Arrange
            TipoDeDesarrolladorUpdateDTO dto =
                    new TipoDeDesarrolladorUpdateDTO();
            dto.setNombre("Publisher");

            // Act
            String resultado = dto.toString();

            // Assert
            assertTrue(resultado.contains("Publisher"));
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoDeDesarrolladorUpdateDTO dto = new TipoDeDesarrolladorUpdateDTO();
            dto.setNombre("Publisher");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Publisher"));
        }
    }
}