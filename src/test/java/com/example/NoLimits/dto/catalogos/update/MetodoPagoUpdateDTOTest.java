package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoPagoUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoPagoUpdateDTO")
class MetodoPagoUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();

            dto.setNombre("Transferencia Bancaria");
            dto.setActivo(true);

            assertEquals("Transferencia Bancaria", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();

            assertNull(dto.getNombre());
            assertNull(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            dto1.setNombre("Transferencia Bancaria");
            dto1.setActivo(true);

            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();
            dto2.setNombre("Transferencia Bancaria");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            dto1.setNombre("Transferencia Bancaria");

            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();
            dto2.setNombre("Tarjeta de Crédito");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            // Arrange
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            dto1.setActivo(true);

            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            dto1.setNombre("Transferencia Bancaria");

            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            // Arrange
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            dto1.setActivo(true);

            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            dto1.setNombre("Transferencia Bancaria");

            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();
            dto2.setNombre("Tarjeta de Crédito");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            // Arrange
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            dto1.setActivo(true);

            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual para objetos equivalentes")
        void testHashCodeIgual() {

            // Arrange
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            dto1.setNombre("Transferencia Bancaria");
            dto1.setActivo(true);

            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();
            dto2.setNombre("Transferencia Bancaria");
            dto2.setActivo(true);

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();
            dto.setNombre("Transferencia Bancaria");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("toString contiene nombre cuando está informado")
        void testToStringContieneNombre() {

            // Arrange
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();
            dto.setNombre("Transferencia Bancaria");

            // Act
            String resultado = dto.toString();

            // Assert
            assertTrue(resultado.contains("Transferencia Bancaria"));
        }

        @Test
        @DisplayName("toString contiene activo cuando está informado")
        void testToStringContieneActivo() {

            // Arrange
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();
            dto.setActivo(true);

            // Act
            String resultado = dto.toString();

            // Assert
            assertTrue(resultado.contains("activo=true"));
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();
            dto.setNombre("Transferencia Bancaria");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Transferencia Bancaria"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}