package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoEmpresaRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoEmpresaRequestDTO")
class TipoEmpresaRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            TipoEmpresaRequestDTO dto = new TipoEmpresaRequestDTO();

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
            TipoEmpresaRequestDTO dto = new TipoEmpresaRequestDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoEmpresaRequestDTO dto1 = new TipoEmpresaRequestDTO();
            dto1.setNombre("Publisher");

            TipoEmpresaRequestDTO dto2 = new TipoEmpresaRequestDTO();
            dto2.setNombre("Publisher");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            TipoEmpresaRequestDTO dto1 =
                    new TipoEmpresaRequestDTO();
            dto1.setNombre("Publisher");

            TipoEmpresaRequestDTO dto2 =
                    new TipoEmpresaRequestDTO();
            dto2.setNombre("Distribuidor");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TipoEmpresaRequestDTO dto =
                    new TipoEmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TipoEmpresaRequestDTO dto =
                    new TipoEmpresaRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TipoEmpresaRequestDTO dto =
                    new TipoEmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            TipoEmpresaRequestDTO dto1 =
                    new TipoEmpresaRequestDTO();
            dto1.setNombre("Publisher");

            TipoEmpresaRequestDTO dto2 =
                    new TipoEmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos nombres son null")
        void testEqualsAmbosNulos() {

            // Arrange
            TipoEmpresaRequestDTO dto1 =
                    new TipoEmpresaRequestDTO();

            TipoEmpresaRequestDTO dto2 =
                    new TipoEmpresaRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos nombres son iguales")
        void testEqualsMismoNombre() {

            // Arrange
            TipoEmpresaRequestDTO dto1 =
                    new TipoEmpresaRequestDTO();
            dto1.setNombre("Publisher");

            TipoEmpresaRequestDTO dto2 =
                    new TipoEmpresaRequestDTO();
            dto2.setNombre("Publisher");

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            TipoEmpresaRequestDTO dto1 =
                    new TipoEmpresaRequestDTO();
            dto1.setNombre("Publisher");

            TipoEmpresaRequestDTO dto2 =
                    new TipoEmpresaRequestDTO();
            dto2.setNombre("Distribuidor");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode coincide cuando los objetos son iguales")
        void testHashCodeObjetosIguales() {

            // Arrange
            TipoEmpresaRequestDTO dto1 =
                    new TipoEmpresaRequestDTO();
            dto1.setNombre("Publisher");

            TipoEmpresaRequestDTO dto2 =
                    new TipoEmpresaRequestDTO();
            dto2.setNombre("Publisher");

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            TipoEmpresaRequestDTO dto =
                    new TipoEmpresaRequestDTO();
            dto.setNombre("Publisher");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString contiene el valor del nombre")
        void testToStringContieneNombre() {

            // Arrange
            TipoEmpresaRequestDTO dto =
                    new TipoEmpresaRequestDTO();
            dto.setNombre("Publisher");

            // Act
            String resultado = dto.toString();

            // Assert
            assertTrue(resultado.contains("Publisher"));
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            TipoEmpresaRequestDTO dto =
                    new TipoEmpresaRequestDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoEmpresaRequestDTO dto = new TipoEmpresaRequestDTO();
            dto.setNombre("Publisher");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Publisher"));
        }
    }
}