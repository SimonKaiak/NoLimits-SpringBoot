package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoProductoUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoProductoUpdateDTO")
class TipoProductoUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TipoProductoUpdateDTO dto = new TipoProductoUpdateDTO();

            dto.setNombre("Videojuego");
            dto.setDescripcion("Categoría para videojuegos en distintas plataformas");
            dto.setActivo(true);

            assertEquals("Videojuego", dto.getNombre());
            assertEquals("Categoría para videojuegos en distintas plataformas", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoProductoUpdateDTO dto = new TipoProductoUpdateDTO();

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
            TipoProductoUpdateDTO dto1 = new TipoProductoUpdateDTO();
            dto1.setNombre("Videojuego");
            dto1.setDescripcion("Categoría para videojuegos en distintas plataformas");
            dto1.setActivo(true);

            TipoProductoUpdateDTO dto2 = new TipoProductoUpdateDTO();
            dto2.setNombre("Videojuego");
            dto2.setDescripcion("Categoría para videojuegos en distintas plataformas");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();
            dto1.setNombre("Videojuego");

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();
            dto2.setNombre("Película");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia la descripcion")
        void testEqualsDescripcionDistinta() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();
            dto1.setDescripcion("Categoría para videojuegos");

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();
            dto2.setDescripcion("Categoría para películas");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia activo")
        void testEqualsActivoDistinto() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();
            dto1.setActivo(true);

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TipoProductoUpdateDTO dto =
                    new TipoProductoUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TipoProductoUpdateDTO dto =
                    new TipoProductoUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TipoProductoUpdateDTO dto =
                    new TipoProductoUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();
            dto1.setNombre("Videojuego");

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene descripcion")
        void testEqualsDescripcionNuloVsNoNulo() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();
            dto1.setDescripcion("Categoría para videojuegos");

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();
            dto1.setActivo(true);

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();
            dto1.setNombre("Videojuego");

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();
            dto2.setNombre("Película");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia la descripcion")
        void testHashCodeDescripcionDistinta() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();
            dto1.setDescripcion("Categoría para videojuegos");

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();
            dto2.setDescripcion("Categoría para películas");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia activo")
        void testHashCodeActivoDistinto() {

            // Arrange
            TipoProductoUpdateDTO dto1 =
                    new TipoProductoUpdateDTO();
            dto1.setActivo(true);

            TipoProductoUpdateDTO dto2 =
                    new TipoProductoUpdateDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente")
        void testHashCodeConsistente() {

            // Arrange
            TipoProductoUpdateDTO dto =
                    new TipoProductoUpdateDTO();
            dto.setNombre("Videojuego");
            dto.setDescripcion("Categoría para videojuegos");
            dto.setActivo(true);

            // Act
            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            TipoProductoUpdateDTO dto =
                    new TipoProductoUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoProductoUpdateDTO dto = new TipoProductoUpdateDTO();
            dto.setNombre("Videojuego");
            dto.setDescripcion("Categoría para videojuegos en distintas plataformas");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Videojuego"));
            assertTrue(resultado.contains("descripcion=Categoría para videojuegos en distintas plataformas"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}