package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TiposDeDesarrolladorRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposDeDesarrolladorRequestDTO")
class TiposDeDesarrolladorRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposDeDesarrolladorRequestDTO dto = new TiposDeDesarrolladorRequestDTO();

            dto.setDesarrolladorId(10L);
            dto.setTipoDeDesarrolladorId(1L);

            assertEquals(10L, dto.getDesarrolladorId());
            assertEquals(1L, dto.getTipoDeDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TiposDeDesarrolladorRequestDTO dto = new TiposDeDesarrolladorRequestDTO();

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
            TiposDeDesarrolladorRequestDTO dto1 = new TiposDeDesarrolladorRequestDTO();
            dto1.setDesarrolladorId(10L);
            dto1.setTipoDeDesarrolladorId(1L);

            TiposDeDesarrolladorRequestDTO dto2 = new TiposDeDesarrolladorRequestDTO();
            dto2.setDesarrolladorId(10L);
            dto2.setTipoDeDesarrolladorId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el desarrolladorId")
        void testEqualsDesarrolladorIdDistinto() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto1 =
                    new TiposDeDesarrolladorRequestDTO();
            dto1.setDesarrolladorId(10L);

            TiposDeDesarrolladorRequestDTO dto2 =
                    new TiposDeDesarrolladorRequestDTO();
            dto2.setDesarrolladorId(20L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el tipoDeDesarrolladorId")
        void testEqualsTipoDeDesarrolladorIdDistinto() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto1 =
                    new TiposDeDesarrolladorRequestDTO();
            dto1.setTipoDeDesarrolladorId(1L);

            TiposDeDesarrolladorRequestDTO dto2 =
                    new TiposDeDesarrolladorRequestDTO();
            dto2.setTipoDeDesarrolladorId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto =
                    new TiposDeDesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto =
                    new TiposDeDesarrolladorRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto =
                    new TiposDeDesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene desarrolladorId")
        void testEqualsDesarrolladorIdNuloVsNoNulo() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto1 =
                    new TiposDeDesarrolladorRequestDTO();
            dto1.setDesarrolladorId(10L);

            TiposDeDesarrolladorRequestDTO dto2 =
                    new TiposDeDesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene tipoDeDesarrolladorId")
        void testEqualsTipoDeDesarrolladorIdNuloVsNoNulo() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto1 =
                    new TiposDeDesarrolladorRequestDTO();
            dto1.setTipoDeDesarrolladorId(1L);

            TiposDeDesarrolladorRequestDTO dto2 =
                    new TiposDeDesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto1 =
                    new TiposDeDesarrolladorRequestDTO();

            TiposDeDesarrolladorRequestDTO dto2 =
                    new TiposDeDesarrolladorRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando desarrolladorId coincide y tipoDeDesarrolladorId es distinto")
        void testEqualsTipoDeDesarrolladorDistintoConDesarrolladorIgual() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto1 =
                    new TiposDeDesarrolladorRequestDTO();
            dto1.setDesarrolladorId(10L);
            dto1.setTipoDeDesarrolladorId(1L);

            TiposDeDesarrolladorRequestDTO dto2 =
                    new TiposDeDesarrolladorRequestDTO();
            dto2.setDesarrolladorId(10L);
            dto2.setTipoDeDesarrolladorId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el desarrolladorId")
        void testHashCodeDesarrolladorIdDistinto() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto1 =
                    new TiposDeDesarrolladorRequestDTO();
            dto1.setDesarrolladorId(10L);

            TiposDeDesarrolladorRequestDTO dto2 =
                    new TiposDeDesarrolladorRequestDTO();
            dto2.setDesarrolladorId(20L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el tipoDeDesarrolladorId")
        void testHashCodeTipoDeDesarrolladorIdDistinto() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto1 =
                    new TiposDeDesarrolladorRequestDTO();
            dto1.setTipoDeDesarrolladorId(1L);

            TiposDeDesarrolladorRequestDTO dto2 =
                    new TiposDeDesarrolladorRequestDTO();
            dto2.setTipoDeDesarrolladorId(2L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode coincide cuando los objetos son iguales")
        void testHashCodeObjetosIguales() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto1 =
                    new TiposDeDesarrolladorRequestDTO();
            dto1.setDesarrolladorId(10L);
            dto1.setTipoDeDesarrolladorId(1L);

            TiposDeDesarrolladorRequestDTO dto2 =
                    new TiposDeDesarrolladorRequestDTO();
            dto2.setDesarrolladorId(10L);
            dto2.setTipoDeDesarrolladorId(1L);

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            TiposDeDesarrolladorRequestDTO dto =
                    new TiposDeDesarrolladorRequestDTO();
            dto.setDesarrolladorId(10L);

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposDeDesarrolladorRequestDTO dto = new TiposDeDesarrolladorRequestDTO();
            dto.setDesarrolladorId(10L);
            dto.setTipoDeDesarrolladorId(1L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("desarrolladorId=10"));
            assertTrue(resultado.contains("tipoDeDesarrolladorId=1"));
        }
    }
}