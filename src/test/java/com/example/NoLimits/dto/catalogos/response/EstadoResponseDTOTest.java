package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EstadoResponseDTO")
class EstadoResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EstadoResponseDTO dto = new EstadoResponseDTO();

            dto.setId(1L);
            dto.setNombre("Activo");
            dto.setDescripcion("Producto disponible para su compra");
            dto.setActivo(true);

            assertEquals(1L, dto.getId());
            assertEquals("Activo", dto.getNombre());
            assertEquals("Producto disponible para su compra", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            EstadoResponseDTO dto = new EstadoResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
            assertNull(dto.getDescripcion());
            assertNull(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            EstadoResponseDTO dto1 = new EstadoResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Activo");
            dto1.setDescripcion("Producto disponible para su compra");
            dto1.setActivo(true);

            EstadoResponseDTO dto2 = new EstadoResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Activo");
            dto2.setDescripcion("Producto disponible para su compra");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el id")
        void testEqualsIdDistinto() {

            // Arrange
            EstadoResponseDTO dto1 = new EstadoResponseDTO();
            dto1.setId(1L);

            EstadoResponseDTO dto2 = new EstadoResponseDTO();
            dto2.setId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            EstadoResponseDTO dto1 = new EstadoResponseDTO();
            dto1.setNombre("Activo");

            EstadoResponseDTO dto2 = new EstadoResponseDTO();
            dto2.setNombre("Inactivo");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia la descripción")
        void testEqualsDescripcionDistinta() {

            // Arrange
            EstadoResponseDTO dto1 = new EstadoResponseDTO();
            dto1.setDescripcion("Producto disponible");

            EstadoResponseDTO dto2 = new EstadoResponseDTO();
            dto2.setDescripcion("Producto agotado");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia activo")
        void testEqualsActivoDistinto() {

            // Arrange
            EstadoResponseDTO dto1 = new EstadoResponseDTO();
            dto1.setActivo(true);

            EstadoResponseDTO dto2 = new EstadoResponseDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            EstadoResponseDTO dto = new EstadoResponseDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            EstadoResponseDTO dto = new EstadoResponseDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            EstadoResponseDTO dto = new EstadoResponseDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EstadoResponseDTO dto = new EstadoResponseDTO();
            dto.setId(1L);
            dto.setNombre("Activo");
            dto.setDescripcion("Producto disponible para su compra");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Activo"));
            assertTrue(resultado.contains("descripcion=Producto disponible para su compra"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}