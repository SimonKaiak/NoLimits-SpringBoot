package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.GenerosRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GenerosRequestDTO")
class GenerosRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            GenerosRequestDTO dto = new GenerosRequestDTO();

            dto.setProductoId(10L);
            dto.setGeneroId(1L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(1L, dto.getGeneroId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            GenerosRequestDTO dto = new GenerosRequestDTO();

            assertNull(dto.getProductoId());
            assertNull(dto.getGeneroId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            GenerosRequestDTO dto1 = new GenerosRequestDTO();
            dto1.setProductoId(10L);
            dto1.setGeneroId(1L);

            GenerosRequestDTO dto2 = new GenerosRequestDTO();
            dto2.setProductoId(10L);
            dto2.setGeneroId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el productoId")
        void testEqualsProductoIdDistinto() {

            // Arrange
            GenerosRequestDTO dto1 = new GenerosRequestDTO();
            dto1.setProductoId(10L);

            GenerosRequestDTO dto2 = new GenerosRequestDTO();
            dto2.setProductoId(20L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el generoId")
        void testEqualsGeneroIdDistinto() {

            // Arrange
            GenerosRequestDTO dto1 = new GenerosRequestDTO();
            dto1.setGeneroId(1L);

            GenerosRequestDTO dto2 = new GenerosRequestDTO();
            dto2.setGeneroId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            GenerosRequestDTO dto = new GenerosRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            GenerosRequestDTO dto = new GenerosRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            GenerosRequestDTO dto = new GenerosRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene productoId")
        void testEqualsProductoIdNuloVsNoNulo() {

            // Arrange
            GenerosRequestDTO dto1 = new GenerosRequestDTO();
            dto1.setProductoId(10L);

            GenerosRequestDTO dto2 = new GenerosRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene generoId")
        void testEqualsGeneroIdNuloVsNoNulo() {

            // Arrange
            GenerosRequestDTO dto1 = new GenerosRequestDTO();
            dto1.setGeneroId(1L);

            GenerosRequestDTO dto2 = new GenerosRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el productoId")
        void testHashCodeProductoIdDistinto() {

            // Arrange
            GenerosRequestDTO dto1 = new GenerosRequestDTO();
            dto1.setProductoId(10L);

            GenerosRequestDTO dto2 = new GenerosRequestDTO();
            dto2.setProductoId(20L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el generoId")
        void testHashCodeGeneroIdDistinto() {

            // Arrange
            GenerosRequestDTO dto1 = new GenerosRequestDTO();
            dto1.setGeneroId(1L);

            GenerosRequestDTO dto2 = new GenerosRequestDTO();
            dto2.setGeneroId(2L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            GenerosRequestDTO dto1 = new GenerosRequestDTO();
            GenerosRequestDTO dto2 = new GenerosRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            GenerosRequestDTO dto = new GenerosRequestDTO();
            dto.setProductoId(10L);
            dto.setGeneroId(1L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("generoId=1"));
        }
    }
}