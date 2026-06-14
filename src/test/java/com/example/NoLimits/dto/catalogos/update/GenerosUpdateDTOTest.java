package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.GenerosUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GenerosUpdateDTO")
class GenerosUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            GenerosUpdateDTO dto = new GenerosUpdateDTO();

            dto.setProductoId(10L);
            dto.setGeneroId(2L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(2L, dto.getGeneroId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            GenerosUpdateDTO dto = new GenerosUpdateDTO();

            assertNull(dto.getProductoId());
            assertNull(dto.getGeneroId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setProductoId(10L);
            dto1.setGeneroId(2L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();
            dto2.setProductoId(10L);
            dto2.setGeneroId(2L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el productoId")
        void testEqualsProductoIdDistinto() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setProductoId(10L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();
            dto2.setProductoId(20L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el generoId")
        void testEqualsGeneroIdDistinto() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setGeneroId(1L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();
            dto2.setGeneroId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            GenerosUpdateDTO dto = new GenerosUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            GenerosUpdateDTO dto = new GenerosUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            GenerosUpdateDTO dto = new GenerosUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene productoId")
        void testEqualsProductoIdNuloVsNoNulo() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setProductoId(10L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene generoId")
        void testEqualsGeneroIdNuloVsNoNulo() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setGeneroId(2L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando productoId coincide y generoId es distinto")
        void testEqualsGeneroDistintoConProductoIgual() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setProductoId(10L);
            dto1.setGeneroId(1L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();
            dto2.setProductoId(10L);
            dto2.setGeneroId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando generoId coincide y productoId es distinto")
        void testEqualsProductoDistintoConGeneroIgual() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setProductoId(10L);
            dto1.setGeneroId(2L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();
            dto2.setProductoId(20L);
            dto2.setGeneroId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el productoId")
        void testHashCodeProductoIdDistinto() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setProductoId(10L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();
            dto2.setProductoId(20L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el generoId")
        void testHashCodeGeneroIdDistinto() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setGeneroId(1L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();
            dto2.setGeneroId(2L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual cuando ambos objetos tienen los mismos valores")
        void testHashCodeIgual() {

            // Arrange
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setProductoId(10L);
            dto1.setGeneroId(2L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();
            dto2.setProductoId(10L);
            dto2.setGeneroId(2L);

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            GenerosUpdateDTO dto = new GenerosUpdateDTO();
            dto.setProductoId(10L);

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            GenerosUpdateDTO dto = new GenerosUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            GenerosUpdateDTO dto = new GenerosUpdateDTO();
            dto.setProductoId(10L);
            dto.setGeneroId(2L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("generoId=2"));
        }
    }
}