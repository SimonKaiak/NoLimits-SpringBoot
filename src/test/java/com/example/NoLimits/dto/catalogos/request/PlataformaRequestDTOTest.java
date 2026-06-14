package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.PlataformaRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PlataformaRequestDTO")
class PlataformaRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            PlataformaRequestDTO dto = new PlataformaRequestDTO();

            dto.setNombre("PC");

            assertEquals("PC", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            PlataformaRequestDTO dto = new PlataformaRequestDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            PlataformaRequestDTO dto1 = new PlataformaRequestDTO();
            dto1.setNombre("PC");

            PlataformaRequestDTO dto2 = new PlataformaRequestDTO();
            dto2.setNombre("PC");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            PlataformaRequestDTO dto1 = new PlataformaRequestDTO();
            dto1.setNombre("PC");

            PlataformaRequestDTO dto2 = new PlataformaRequestDTO();
            dto2.setNombre("PlayStation 5");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            PlataformaRequestDTO dto = new PlataformaRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            PlataformaRequestDTO dto = new PlataformaRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            PlataformaRequestDTO dto = new PlataformaRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            PlataformaRequestDTO dto1 = new PlataformaRequestDTO();
            dto1.setNombre("PC");

            PlataformaRequestDTO dto2 = new PlataformaRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos nombres son null")
        void testEqualsAmbosNulos() {

            // Arrange
            PlataformaRequestDTO dto1 = new PlataformaRequestDTO();
            PlataformaRequestDTO dto2 = new PlataformaRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            PlataformaRequestDTO dto1 = new PlataformaRequestDTO();
            dto1.setNombre("PC");

            PlataformaRequestDTO dto2 = new PlataformaRequestDTO();
            dto2.setNombre("PlayStation 5");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode coincide cuando los objetos son iguales")
        void testHashCodeObjetosIguales() {

            // Arrange
            PlataformaRequestDTO dto1 = new PlataformaRequestDTO();
            dto1.setNombre("PC");

            PlataformaRequestDTO dto2 = new PlataformaRequestDTO();
            dto2.setNombre("PC");

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            PlataformaRequestDTO dto = new PlataformaRequestDTO();
            dto.setNombre("PC");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            PlataformaRequestDTO dto = new PlataformaRequestDTO();
            dto.setNombre("PC");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=PC"));
        }
    }
}