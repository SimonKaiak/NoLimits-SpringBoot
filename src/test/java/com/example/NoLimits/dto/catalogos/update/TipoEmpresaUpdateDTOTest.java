package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoEmpresaUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoEmpresaUpdateDTO")
class TipoEmpresaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            TipoEmpresaUpdateDTO dto = new TipoEmpresaUpdateDTO();

            dto.setNombre("Distribuidora");

            assertEquals("Distribuidora", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoEmpresaUpdateDTO dto = new TipoEmpresaUpdateDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoEmpresaUpdateDTO dto1 = new TipoEmpresaUpdateDTO();
            dto1.setNombre("Distribuidora");

            TipoEmpresaUpdateDTO dto2 = new TipoEmpresaUpdateDTO();
            dto2.setNombre("Distribuidora");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            TipoEmpresaUpdateDTO dto1 =
                    new TipoEmpresaUpdateDTO();
            dto1.setNombre("Distribuidora");

            TipoEmpresaUpdateDTO dto2 =
                    new TipoEmpresaUpdateDTO();
            dto2.setNombre("Publisher");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TipoEmpresaUpdateDTO dto =
                    new TipoEmpresaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TipoEmpresaUpdateDTO dto =
                    new TipoEmpresaUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TipoEmpresaUpdateDTO dto =
                    new TipoEmpresaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            TipoEmpresaUpdateDTO dto1 =
                    new TipoEmpresaUpdateDTO();
            dto1.setNombre("Distribuidora");

            TipoEmpresaUpdateDTO dto2 =
                    new TipoEmpresaUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen nombre null")
        void testEqualsAmbosNulos() {

            // Arrange
            TipoEmpresaUpdateDTO dto1 =
                    new TipoEmpresaUpdateDTO();

            TipoEmpresaUpdateDTO dto2 =
                    new TipoEmpresaUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            TipoEmpresaUpdateDTO dto1 =
                    new TipoEmpresaUpdateDTO();
            dto1.setNombre("Distribuidora");

            TipoEmpresaUpdateDTO dto2 =
                    new TipoEmpresaUpdateDTO();
            dto2.setNombre("Publisher");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual para objetos equivalentes")
        void testHashCodeIgual() {

            // Arrange
            TipoEmpresaUpdateDTO dto1 =
                    new TipoEmpresaUpdateDTO();
            dto1.setNombre("Distribuidora");

            TipoEmpresaUpdateDTO dto2 =
                    new TipoEmpresaUpdateDTO();
            dto2.setNombre("Distribuidora");

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            TipoEmpresaUpdateDTO dto =
                    new TipoEmpresaUpdateDTO();
            dto.setNombre("Distribuidora");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            TipoEmpresaUpdateDTO dto =
                    new TipoEmpresaUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("toString contiene nombre cuando está informado")
        void testToStringContieneNombre() {

            // Arrange
            TipoEmpresaUpdateDTO dto =
                    new TipoEmpresaUpdateDTO();
            dto.setNombre("Distribuidora");

            // Act
            String resultado = dto.toString();

            // Assert
            assertTrue(resultado.contains("Distribuidora"));
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoEmpresaUpdateDTO dto = new TipoEmpresaUpdateDTO();
            dto.setNombre("Distribuidora");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Distribuidora"));
        }
    }
}