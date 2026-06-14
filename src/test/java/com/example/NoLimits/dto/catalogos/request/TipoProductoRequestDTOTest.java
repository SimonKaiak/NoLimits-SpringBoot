package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoProductoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoProductoRequestDTO")
class TipoProductoRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TipoProductoRequestDTO dto = new TipoProductoRequestDTO();

            dto.setNombre("Película");
            dto.setDescripcion("Categoría general para clasificar productos");
            dto.setActivo(true);

            assertEquals("Película", dto.getNombre());
            assertEquals("Categoría general para clasificar productos", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoProductoRequestDTO dto = new TipoProductoRequestDTO();

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
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setNombre("Película");
            dto1.setDescripcion("Categoría general para clasificar productos");
            dto1.setActivo(true);

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();
            dto2.setNombre("Película");
            dto2.setDescripcion("Categoría general para clasificar productos");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setNombre("Película");

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();
            dto2.setNombre("Serie");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia la descripcion")
        void testEqualsDescripcionDistinta() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setDescripcion("Descripción 1");

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();
            dto2.setDescripcion("Descripción 2");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setActivo(true);

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TipoProductoRequestDTO dto = new TipoProductoRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TipoProductoRequestDTO dto = new TipoProductoRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TipoProductoRequestDTO dto = new TipoProductoRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setNombre("Película");

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene descripcion")
        void testEqualsDescripcionNuloVsNoNulo() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setDescripcion("Descripción");

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setActivo(true);

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setNombre("Película");

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();
            dto2.setNombre("Serie");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia la descripcion")
        void testHashCodeDescripcionDistinta() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setDescripcion("Descripción 1");

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();
            dto2.setDescripcion("Descripción 2");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            // Arrange
            TipoProductoRequestDTO dto1 = new TipoProductoRequestDTO();
            dto1.setActivo(true);

            TipoProductoRequestDTO dto2 = new TipoProductoRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            TipoProductoRequestDTO dto = new TipoProductoRequestDTO();
            dto.setNombre("Película");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            TipoProductoRequestDTO dto = new TipoProductoRequestDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoProductoRequestDTO dto = new TipoProductoRequestDTO();
            dto.setNombre("Película");
            dto.setDescripcion("Categoría general para clasificar productos");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Película"));
            assertTrue(resultado.contains("descripcion=Categoría general para clasificar productos"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}