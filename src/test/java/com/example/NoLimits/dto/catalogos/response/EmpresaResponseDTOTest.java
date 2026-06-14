package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresaResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EmpresaResponseDTO")
class EmpresaResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EmpresaResponseDTO dto = new EmpresaResponseDTO();

            dto.setId(1L);
            dto.setNombre("Sony Pictures");
            dto.setActivo(true);

            assertEquals(1L, dto.getId());
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
            EmpresaResponseDTO dto = new EmpresaResponseDTO();

            assertNull(dto.getId());
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
            EmpresaResponseDTO dto1 = new EmpresaResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Sony Pictures");
            dto1.setActivo(true);

            EmpresaResponseDTO dto2 = new EmpresaResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Sony Pictures");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el id")
        void testEqualsIdDistinto() {

            // Arrange
            EmpresaResponseDTO dto1 = new EmpresaResponseDTO();
            dto1.setId(1L);

            EmpresaResponseDTO dto2 = new EmpresaResponseDTO();
            dto2.setId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            EmpresaResponseDTO dto1 = new EmpresaResponseDTO();
            dto1.setNombre("Sony Pictures");

            EmpresaResponseDTO dto2 = new EmpresaResponseDTO();
            dto2.setNombre("Warner Bros");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia activo")
        void testEqualsActivoDistinto() {

            // Arrange
            EmpresaResponseDTO dto1 = new EmpresaResponseDTO();
            dto1.setActivo(true);

            EmpresaResponseDTO dto2 = new EmpresaResponseDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            EmpresaResponseDTO dto = new EmpresaResponseDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            EmpresaResponseDTO dto = new EmpresaResponseDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            EmpresaResponseDTO dto = new EmpresaResponseDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EmpresaResponseDTO dto = new EmpresaResponseDTO();
            dto.setId(1L);
            dto.setNombre("Sony Pictures");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Sony Pictures"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}