package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.EstadoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EstadoRequestDTO")
class EstadoRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EstadoRequestDTO dto = new EstadoRequestDTO();

            dto.setNombre("Activo");
            dto.setDescripcion("Producto disponible para su compra");
            dto.setActivo(true);

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
            EstadoRequestDTO dto = new EstadoRequestDTO();

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
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setNombre("Activo");
            dto1.setDescripcion("Producto disponible para su compra");
            dto1.setActivo(true);

            EstadoRequestDTO dto2 = new EstadoRequestDTO();
            dto2.setNombre("Activo");
            dto2.setDescripcion("Producto disponible para su compra");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setNombre("Activo");

            EstadoRequestDTO dto2 = new EstadoRequestDTO();
            dto2.setNombre("Inactivo");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia la descripcion")
        void testEqualsDescripcionDistinta() {

            // Arrange
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setDescripcion("Disponible");

            EstadoRequestDTO dto2 = new EstadoRequestDTO();
            dto2.setDescripcion("No disponible");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            // Arrange
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setActivo(true);

            EstadoRequestDTO dto2 = new EstadoRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            EstadoRequestDTO dto = new EstadoRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            EstadoRequestDTO dto = new EstadoRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            EstadoRequestDTO dto = new EstadoRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setNombre("Activo");

            EstadoRequestDTO dto2 = new EstadoRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene descripcion")
        void testEqualsDescripcionNuloVsNoNulo() {

            // Arrange
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setDescripcion("Disponible");

            EstadoRequestDTO dto2 = new EstadoRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            // Arrange
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setActivo(true);

            EstadoRequestDTO dto2 = new EstadoRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setNombre("Activo");

            EstadoRequestDTO dto2 = new EstadoRequestDTO();
            dto2.setNombre("Inactivo");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia la descripcion")
        void testHashCodeDescripcionDistinta() {

            // Arrange
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setDescripcion("Disponible");

            EstadoRequestDTO dto2 = new EstadoRequestDTO();
            dto2.setDescripcion("No disponible");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            // Arrange
            EstadoRequestDTO dto1 = new EstadoRequestDTO();
            dto1.setActivo(true);

            EstadoRequestDTO dto2 = new EstadoRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EstadoRequestDTO dto = new EstadoRequestDTO();
            dto.setNombre("Activo");
            dto.setDescripcion("Producto disponible para su compra");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Activo"));
            assertTrue(resultado.contains("descripcion=Producto disponible para su compra"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}