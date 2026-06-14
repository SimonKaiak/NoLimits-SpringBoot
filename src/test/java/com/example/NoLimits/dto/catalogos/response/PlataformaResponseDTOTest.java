package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PlataformaResponseDTO")
class PlataformaResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            PlataformaResponseDTO dto = new PlataformaResponseDTO();

            dto.setId(1L);
            dto.setNombre("PC");

            assertEquals(1L, dto.getId());
            assertEquals("PC", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            PlataformaResponseDTO dto = new PlataformaResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            PlataformaResponseDTO dto1 = new PlataformaResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("PC");

            PlataformaResponseDTO dto2 = new PlataformaResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("PC");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el id")
        void testEqualsIdDistinto() {

            // Arrange
            PlataformaResponseDTO dto1 = new PlataformaResponseDTO();
            dto1.setId(1L);

            PlataformaResponseDTO dto2 = new PlataformaResponseDTO();
            dto2.setId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            PlataformaResponseDTO dto1 = new PlataformaResponseDTO();
            dto1.setNombre("PC");

            PlataformaResponseDTO dto2 = new PlataformaResponseDTO();
            dto2.setNombre("PlayStation 5");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            PlataformaResponseDTO dto = new PlataformaResponseDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            PlataformaResponseDTO dto = new PlataformaResponseDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            PlataformaResponseDTO dto = new PlataformaResponseDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando un id es null")
        void testEqualsIdNull() {

            // Arrange
            PlataformaResponseDTO dto1 =
                    new PlataformaResponseDTO();
            dto1.setId(1L);

            PlataformaResponseDTO dto2 =
                    new PlataformaResponseDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando un nombre es null")
        void testEqualsNombreNull() {

            // Arrange
            PlataformaResponseDTO dto1 =
                    new PlataformaResponseDTO();
            dto1.setNombre("PC");

            PlataformaResponseDTO dto2 =
                    new PlataformaResponseDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode funciona con campos null")
        void testHashCodeConCamposNull() {

            // Arrange
            PlataformaResponseDTO dto =
                    new PlataformaResponseDTO();

            // Act + Assert
            assertDoesNotThrow(dto::hashCode);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            PlataformaResponseDTO dto = new PlataformaResponseDTO();
            dto.setId(1L);
            dto.setNombre("PC");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=PC"));
        }
    }
}