package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.EmpresaRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EmpresaRequestDTO")
class EmpresaRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EmpresaRequestDTO dto = new EmpresaRequestDTO();

            dto.setNombre("Sony Pictures");
            dto.setActivo(true);

            assertEquals("Sony Pictures", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            EmpresaRequestDTO dto = new EmpresaRequestDTO();

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
            EmpresaRequestDTO dto1 = new EmpresaRequestDTO();
            dto1.setNombre("Sony Pictures");
            dto1.setActivo(true);

            EmpresaRequestDTO dto2 = new EmpresaRequestDTO();
            dto2.setNombre("Sony Pictures");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            EmpresaRequestDTO dto1 =
                    new EmpresaRequestDTO();
            dto1.setNombre("Sony Pictures");

            EmpresaRequestDTO dto2 =
                    new EmpresaRequestDTO();
            dto2.setNombre("Warner Bros");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            // Arrange
            EmpresaRequestDTO dto1 =
                    new EmpresaRequestDTO();
            dto1.setActivo(true);

            EmpresaRequestDTO dto2 =
                    new EmpresaRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            EmpresaRequestDTO dto =
                    new EmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            EmpresaRequestDTO dto =
                    new EmpresaRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            EmpresaRequestDTO dto =
                    new EmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            EmpresaRequestDTO dto1 =
                    new EmpresaRequestDTO();
            dto1.setNombre("Sony Pictures");

            EmpresaRequestDTO dto2 =
                    new EmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            // Arrange
            EmpresaRequestDTO dto1 =
                    new EmpresaRequestDTO();
            dto1.setActivo(true);

            EmpresaRequestDTO dto2 =
                    new EmpresaRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            EmpresaRequestDTO dto1 =
                    new EmpresaRequestDTO();
            dto1.setNombre("Sony Pictures");

            EmpresaRequestDTO dto2 =
                    new EmpresaRequestDTO();
            dto2.setNombre("Warner Bros");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            // Arrange
            EmpresaRequestDTO dto1 =
                    new EmpresaRequestDTO();
            dto1.setActivo(true);

            EmpresaRequestDTO dto2 =
                    new EmpresaRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EmpresaRequestDTO dto = new EmpresaRequestDTO();
            dto.setNombre("Sony Pictures");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Sony Pictures"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}