package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladorRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DesarrolladorRequestDTO")
class DesarrolladorRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            DesarrolladorRequestDTO dto = new DesarrolladorRequestDTO();

            dto.setNombre("Insomniac Games");
            dto.setActivo(true);

            assertEquals("Insomniac Games", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            DesarrolladorRequestDTO dto = new DesarrolladorRequestDTO();

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
            DesarrolladorRequestDTO dto1 = new DesarrolladorRequestDTO();
            dto1.setNombre("Insomniac Games");
            dto1.setActivo(true);

            DesarrolladorRequestDTO dto2 = new DesarrolladorRequestDTO();
            dto2.setNombre("Insomniac Games");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            DesarrolladorRequestDTO dto1 =
                    new DesarrolladorRequestDTO();
            dto1.setNombre("Insomniac Games");

            DesarrolladorRequestDTO dto2 =
                    new DesarrolladorRequestDTO();
            dto2.setNombre("Naughty Dog");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            // Arrange
            DesarrolladorRequestDTO dto1 =
                    new DesarrolladorRequestDTO();
            dto1.setActivo(true);

            DesarrolladorRequestDTO dto2 =
                    new DesarrolladorRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            DesarrolladorRequestDTO dto =
                    new DesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            DesarrolladorRequestDTO dto =
                    new DesarrolladorRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            DesarrolladorRequestDTO dto =
                    new DesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            DesarrolladorRequestDTO dto1 =
                    new DesarrolladorRequestDTO();
            dto1.setNombre("Insomniac Games");

            DesarrolladorRequestDTO dto2 =
                    new DesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            // Arrange
            DesarrolladorRequestDTO dto1 =
                    new DesarrolladorRequestDTO();
            dto1.setActivo(true);

            DesarrolladorRequestDTO dto2 =
                    new DesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            DesarrolladorRequestDTO dto1 =
                    new DesarrolladorRequestDTO();
            dto1.setNombre("Insomniac Games");

            DesarrolladorRequestDTO dto2 =
                    new DesarrolladorRequestDTO();
            dto2.setNombre("Naughty Dog");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            // Arrange
            DesarrolladorRequestDTO dto1 =
                    new DesarrolladorRequestDTO();
            dto1.setActivo(true);

            DesarrolladorRequestDTO dto2 =
                    new DesarrolladorRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            DesarrolladorRequestDTO dto = new DesarrolladorRequestDTO();
            dto.setNombre("Insomniac Games");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Insomniac Games"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}