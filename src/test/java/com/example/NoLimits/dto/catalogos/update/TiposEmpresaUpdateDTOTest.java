package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposEmpresaUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposEmpresaUpdateDTO")
class TiposEmpresaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposEmpresaUpdateDTO dto = new TiposEmpresaUpdateDTO();

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
            TiposEmpresaUpdateDTO dto = new TiposEmpresaUpdateDTO();

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
            TiposEmpresaUpdateDTO dto1 = new TiposEmpresaUpdateDTO();
            dto1.setEmpresaId(5L);
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaUpdateDTO dto2 = new TiposEmpresaUpdateDTO();
            dto2.setEmpresaId(5L);
            dto2.setTipoEmpresaId(2L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia empresaId")
        void testEqualsEmpresaIdDistinto() {

            // Arrange
            TiposEmpresaUpdateDTO dto1 =
                    new TiposEmpresaUpdateDTO();
            dto1.setEmpresaId(5L);

            TiposEmpresaUpdateDTO dto2 =
                    new TiposEmpresaUpdateDTO();
            dto2.setEmpresaId(10L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia tipoEmpresaId")
        void testEqualsTipoEmpresaIdDistinto() {

            // Arrange
            TiposEmpresaUpdateDTO dto1 =
                    new TiposEmpresaUpdateDTO();
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaUpdateDTO dto2 =
                    new TiposEmpresaUpdateDTO();
            dto2.setTipoEmpresaId(3L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TiposEmpresaUpdateDTO dto =
                    new TiposEmpresaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TiposEmpresaUpdateDTO dto =
                    new TiposEmpresaUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TiposEmpresaUpdateDTO dto =
                    new TiposEmpresaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene empresaId")
        void testEqualsEmpresaIdNuloVsNoNulo() {

            // Arrange
            TiposEmpresaUpdateDTO dto1 =
                    new TiposEmpresaUpdateDTO();
            dto1.setEmpresaId(5L);

            TiposEmpresaUpdateDTO dto2 =
                    new TiposEmpresaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene tipoEmpresaId")
        void testEqualsTipoEmpresaIdNuloVsNoNulo() {

            // Arrange
            TiposEmpresaUpdateDTO dto1 =
                    new TiposEmpresaUpdateDTO();
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaUpdateDTO dto2 =
                    new TiposEmpresaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            TiposEmpresaUpdateDTO dto1 =
                    new TiposEmpresaUpdateDTO();

            TiposEmpresaUpdateDTO dto2 =
                    new TiposEmpresaUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia empresaId")
        void testHashCodeEmpresaIdDistinto() {

            // Arrange
            TiposEmpresaUpdateDTO dto1 =
                    new TiposEmpresaUpdateDTO();
            dto1.setEmpresaId(5L);

            TiposEmpresaUpdateDTO dto2 =
                    new TiposEmpresaUpdateDTO();
            dto2.setEmpresaId(10L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia tipoEmpresaId")
        void testHashCodeTipoEmpresaIdDistinto() {

            // Arrange
            TiposEmpresaUpdateDTO dto1 =
                    new TiposEmpresaUpdateDTO();
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaUpdateDTO dto2 =
                    new TiposEmpresaUpdateDTO();
            dto2.setTipoEmpresaId(3L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente")
        void testHashCodeConsistente() {

            // Arrange
            TiposEmpresaUpdateDTO dto =
                    new TiposEmpresaUpdateDTO();
            dto.setEmpresaId(5L);
            dto.setTipoEmpresaId(2L);

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
            TiposEmpresaUpdateDTO dto =
                    new TiposEmpresaUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposEmpresaUpdateDTO dto = new TiposEmpresaUpdateDTO();
            dto.setEmpresaId(5L);
            dto.setTipoEmpresaId(2L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("empresaId=5"));
            assertTrue(resultado.contains("tipoEmpresaId=2"));
        }
    }
}