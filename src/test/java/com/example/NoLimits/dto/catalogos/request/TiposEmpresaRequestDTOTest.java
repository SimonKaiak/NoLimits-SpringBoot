package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TiposEmpresaRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposEmpresaRequestDTO")
class TiposEmpresaRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposEmpresaRequestDTO dto = new TiposEmpresaRequestDTO();

            dto.setEmpresaId(5L);
            dto.setTipoEmpresaId(2L);

            assertEquals(5L, dto.getEmpresaId());
            assertEquals(2L, dto.getTipoEmpresaId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TiposEmpresaRequestDTO dto = new TiposEmpresaRequestDTO();

            assertNull(dto.getEmpresaId());
            assertNull(dto.getTipoEmpresaId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TiposEmpresaRequestDTO dto1 = new TiposEmpresaRequestDTO();
            dto1.setEmpresaId(5L);
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaRequestDTO dto2 = new TiposEmpresaRequestDTO();
            dto2.setEmpresaId(5L);
            dto2.setTipoEmpresaId(2L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el empresaId")
        void testEqualsEmpresaIdDistinto() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();
            dto1.setEmpresaId(5L);

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();
            dto2.setEmpresaId(10L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el tipoEmpresaId")
        void testEqualsTipoEmpresaIdDistinto() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();
            dto1.setTipoEmpresaId(1L);

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();
            dto2.setTipoEmpresaId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TiposEmpresaRequestDTO dto =
                    new TiposEmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TiposEmpresaRequestDTO dto =
                    new TiposEmpresaRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TiposEmpresaRequestDTO dto =
                    new TiposEmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene empresaId")
        void testEqualsEmpresaIdNuloVsNoNulo() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();
            dto1.setEmpresaId(5L);

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene tipoEmpresaId")
        void testEqualsTipoEmpresaIdNuloVsNoNulo() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando empresaId coincide y tipoEmpresaId es distinto")
        void testEqualsTipoEmpresaDistintoConEmpresaIgual() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();
            dto1.setEmpresaId(5L);
            dto1.setTipoEmpresaId(1L);

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();
            dto2.setEmpresaId(5L);
            dto2.setTipoEmpresaId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando tipoEmpresaId coincide y empresaId es distinto")
        void testEqualsEmpresaDistintaConTipoEmpresaIgual() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();
            dto1.setEmpresaId(5L);
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();
            dto2.setEmpresaId(10L);
            dto2.setTipoEmpresaId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el empresaId")
        void testHashCodeEmpresaIdDistinto() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();
            dto1.setEmpresaId(5L);

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();
            dto2.setEmpresaId(10L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el tipoEmpresaId")
        void testHashCodeTipoEmpresaIdDistinto() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();
            dto1.setTipoEmpresaId(1L);

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();
            dto2.setTipoEmpresaId(2L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode coincide cuando los objetos son iguales")
        void testHashCodeObjetosIguales() {

            // Arrange
            TiposEmpresaRequestDTO dto1 =
                    new TiposEmpresaRequestDTO();
            dto1.setEmpresaId(5L);
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaRequestDTO dto2 =
                    new TiposEmpresaRequestDTO();
            dto2.setEmpresaId(5L);
            dto2.setTipoEmpresaId(2L);

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            TiposEmpresaRequestDTO dto =
                    new TiposEmpresaRequestDTO();
            dto.setEmpresaId(5L);

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            TiposEmpresaRequestDTO dto =
                    new TiposEmpresaRequestDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposEmpresaRequestDTO dto = new TiposEmpresaRequestDTO();
            dto.setEmpresaId(5L);
            dto.setTipoEmpresaId(2L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("empresaId=5"));
            assertTrue(resultado.contains("tipoEmpresaId=2"));
        }
    }
}