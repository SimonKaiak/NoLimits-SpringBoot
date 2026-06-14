package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.PlataformasUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PlataformasUpdateDTO")
class PlataformasUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();

            dto.setProductoId(10L);
            dto.setPlataformaId(1L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(1L, dto.getPlataformaId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();

            assertNull(dto.getProductoId());
            assertNull(dto.getPlataformaId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            PlataformasUpdateDTO dto1 = new PlataformasUpdateDTO();
            dto1.setProductoId(10L);
            dto1.setPlataformaId(1L);

            PlataformasUpdateDTO dto2 = new PlataformasUpdateDTO();
            dto2.setProductoId(10L);
            dto2.setPlataformaId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el productoId")
        void testEqualsProductoIdDistinto() {

            // Arrange
            PlataformasUpdateDTO dto1 = new PlataformasUpdateDTO();
            dto1.setProductoId(10L);

            PlataformasUpdateDTO dto2 = new PlataformasUpdateDTO();
            dto2.setProductoId(20L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el plataformaId")
        void testEqualsPlataformaIdDistinto() {

            // Arrange
            PlataformasUpdateDTO dto1 = new PlataformasUpdateDTO();
            dto1.setPlataformaId(1L);

            PlataformasUpdateDTO dto2 = new PlataformasUpdateDTO();
            dto2.setPlataformaId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene productoId")
        void testEqualsProductoIdNuloVsNoNulo() {

            // Arrange
            PlataformasUpdateDTO dto1 = new PlataformasUpdateDTO();
            dto1.setProductoId(10L);

            PlataformasUpdateDTO dto2 = new PlataformasUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene plataformaId")
        void testEqualsPlataformaIdNuloVsNoNulo() {

            // Arrange
            PlataformasUpdateDTO dto1 = new PlataformasUpdateDTO();
            dto1.setPlataformaId(1L);

            PlataformasUpdateDTO dto2 = new PlataformasUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            PlataformasUpdateDTO dto1 = new PlataformasUpdateDTO();
            PlataformasUpdateDTO dto2 = new PlataformasUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el productoId")
        void testHashCodeProductoIdDistinto() {

            // Arrange
            PlataformasUpdateDTO dto1 = new PlataformasUpdateDTO();
            dto1.setProductoId(10L);

            PlataformasUpdateDTO dto2 = new PlataformasUpdateDTO();
            dto2.setProductoId(20L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el plataformaId")
        void testHashCodePlataformaIdDistinto() {

            // Arrange
            PlataformasUpdateDTO dto1 = new PlataformasUpdateDTO();
            dto1.setPlataformaId(1L);

            PlataformasUpdateDTO dto2 = new PlataformasUpdateDTO();
            dto2.setPlataformaId(2L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual para objetos equivalentes")
        void testHashCodeIgual() {

            // Arrange
            PlataformasUpdateDTO dto1 = new PlataformasUpdateDTO();
            dto1.setProductoId(10L);
            dto1.setPlataformaId(1L);

            PlataformasUpdateDTO dto2 = new PlataformasUpdateDTO();
            dto2.setProductoId(10L);
            dto2.setPlataformaId(1L);

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();
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
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("toString contiene productoId cuando está informado")
        void testToStringContieneProductoId() {

            // Arrange
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();
            dto.setProductoId(10L);

            // Act
            String resultado = dto.toString();

            // Assert
            assertTrue(resultado.contains("productoId=10"));
        }

        @Test
        @DisplayName("toString contiene plataformaId cuando está informado")
        void testToStringContienePlataformaId() {

            // Arrange
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();
            dto.setPlataformaId(1L);

            // Act
            String resultado = dto.toString();

            // Assert
            assertTrue(resultado.contains("plataformaId=1"));
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            PlataformasUpdateDTO dto = new PlataformasUpdateDTO();
            dto.setProductoId(10L);
            dto.setPlataformaId(1L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("plataformaId=1"));
        }
    }
}