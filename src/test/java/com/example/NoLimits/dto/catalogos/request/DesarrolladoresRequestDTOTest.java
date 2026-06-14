package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladoresRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DesarrolladoresRequestDTO")
class DesarrolladoresRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            DesarrolladoresRequestDTO dto = new DesarrolladoresRequestDTO();

            dto.setProductoId(10L);
            dto.setDesarrolladorId(1L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(1L, dto.getDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            DesarrolladoresRequestDTO dto = new DesarrolladoresRequestDTO();

            assertNull(dto.getProductoId());
            assertNull(dto.getDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            DesarrolladoresRequestDTO dto1 = new DesarrolladoresRequestDTO();
            dto1.setProductoId(10L);
            dto1.setDesarrolladorId(1L);

            DesarrolladoresRequestDTO dto2 = new DesarrolladoresRequestDTO();
            dto2.setProductoId(10L);
            dto2.setDesarrolladorId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el productoId")
        void testEqualsProductoIdDistinto() {

            // Arrange
            DesarrolladoresRequestDTO dto1 =
                    new DesarrolladoresRequestDTO();
            dto1.setProductoId(10L);

            DesarrolladoresRequestDTO dto2 =
                    new DesarrolladoresRequestDTO();
            dto2.setProductoId(20L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el desarrolladorId")
        void testEqualsDesarrolladorIdDistinto() {

            // Arrange
            DesarrolladoresRequestDTO dto1 =
                    new DesarrolladoresRequestDTO();
            dto1.setDesarrolladorId(1L);

            DesarrolladoresRequestDTO dto2 =
                    new DesarrolladoresRequestDTO();
            dto2.setDesarrolladorId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            DesarrolladoresRequestDTO dto =
                    new DesarrolladoresRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            DesarrolladoresRequestDTO dto =
                    new DesarrolladoresRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            DesarrolladoresRequestDTO dto =
                    new DesarrolladoresRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el productoId")
        void testHashCodeProductoIdDistinto() {

            // Arrange
            DesarrolladoresRequestDTO dto1 =
                    new DesarrolladoresRequestDTO();
            dto1.setProductoId(10L);

            DesarrolladoresRequestDTO dto2 =
                    new DesarrolladoresRequestDTO();
            dto2.setProductoId(20L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el desarrolladorId")
        void testHashCodeDesarrolladorIdDistinto() {

            // Arrange
            DesarrolladoresRequestDTO dto1 =
                    new DesarrolladoresRequestDTO();
            dto1.setDesarrolladorId(1L);

            DesarrolladoresRequestDTO dto2 =
                    new DesarrolladoresRequestDTO();
            dto2.setDesarrolladorId(2L);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            DesarrolladoresRequestDTO dto = new DesarrolladoresRequestDTO();
            dto.setProductoId(10L);
            dto.setDesarrolladorId(1L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("desarrolladorId=1"));
        }
    }
}