package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresasResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EmpresasResponseDTO")
class EmpresasResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EmpresasResponseDTO dto = new EmpresasResponseDTO();

            dto.setId(7L);
            dto.setProductoId(10L);
            dto.setEmpresaId(5L);
            dto.setEmpresaNombre("Sony Pictures");

            assertEquals(7L, dto.getId());
            assertEquals(10L, dto.getProductoId());
            assertEquals(5L, dto.getEmpresaId());
            assertEquals("Sony Pictures", dto.getEmpresaNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            EmpresasResponseDTO dto = new EmpresasResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getProductoId());
            assertNull(dto.getEmpresaId());
            assertNull(dto.getEmpresaNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            EmpresasResponseDTO dto1 = new EmpresasResponseDTO();
            dto1.setId(7L);
            dto1.setProductoId(10L);
            dto1.setEmpresaId(5L);
            dto1.setEmpresaNombre("Sony Pictures");

            EmpresasResponseDTO dto2 = new EmpresasResponseDTO();
            dto2.setId(7L);
            dto2.setProductoId(10L);
            dto2.setEmpresaId(5L);
            dto2.setEmpresaNombre("Sony Pictures");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el id")
        void testEqualsIdDistinto() {

            // Arrange
            EmpresasResponseDTO dto1 = new EmpresasResponseDTO();
            dto1.setId(7L);

            EmpresasResponseDTO dto2 = new EmpresasResponseDTO();
            dto2.setId(8L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia productoId")
        void testEqualsProductoIdDistinto() {

            // Arrange
            EmpresasResponseDTO dto1 = new EmpresasResponseDTO();
            dto1.setProductoId(10L);

            EmpresasResponseDTO dto2 = new EmpresasResponseDTO();
            dto2.setProductoId(20L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia empresaId")
        void testEqualsEmpresaIdDistinto() {

            // Arrange
            EmpresasResponseDTO dto1 = new EmpresasResponseDTO();
            dto1.setEmpresaId(5L);

            EmpresasResponseDTO dto2 = new EmpresasResponseDTO();
            dto2.setEmpresaId(6L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia empresaNombre")
        void testEqualsEmpresaNombreDistinto() {

            // Arrange
            EmpresasResponseDTO dto1 = new EmpresasResponseDTO();
            dto1.setEmpresaNombre("Sony Pictures");

            EmpresasResponseDTO dto2 = new EmpresasResponseDTO();
            dto2.setEmpresaNombre("Warner Bros");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            EmpresasResponseDTO dto = new EmpresasResponseDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            EmpresasResponseDTO dto = new EmpresasResponseDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            EmpresasResponseDTO dto = new EmpresasResponseDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EmpresasResponseDTO dto = new EmpresasResponseDTO();
            dto.setId(7L);
            dto.setProductoId(10L);
            dto.setEmpresaId(5L);
            dto.setEmpresaNombre("Sony Pictures");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=7"));
            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("empresaId=5"));
            assertTrue(resultado.contains("empresaNombre=Sony Pictures"));
        }
    }
}