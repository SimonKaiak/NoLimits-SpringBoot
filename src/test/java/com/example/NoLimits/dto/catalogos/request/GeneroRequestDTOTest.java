package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.GeneroRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GeneroRequestDTO")
class GeneroRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            GeneroRequestDTO dto = new GeneroRequestDTO();

            dto.setNombre("Acción");

            assertEquals("Acción", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            GeneroRequestDTO dto = new GeneroRequestDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            GeneroRequestDTO dto1 = new GeneroRequestDTO();
            dto1.setNombre("Acción");

            GeneroRequestDTO dto2 = new GeneroRequestDTO();
            dto2.setNombre("Acción");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            GeneroRequestDTO dto1 = new GeneroRequestDTO();
            dto1.setNombre("Acción");

            GeneroRequestDTO dto2 = new GeneroRequestDTO();
            dto2.setNombre("Aventura");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            GeneroRequestDTO dto = new GeneroRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            GeneroRequestDTO dto = new GeneroRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            GeneroRequestDTO dto = new GeneroRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            GeneroRequestDTO dto1 = new GeneroRequestDTO();
            dto1.setNombre("Acción");

            GeneroRequestDTO dto2 = new GeneroRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            GeneroRequestDTO dto1 = new GeneroRequestDTO();
            dto1.setNombre("Acción");

            GeneroRequestDTO dto2 = new GeneroRequestDTO();
            dto2.setNombre("Aventura");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna true cuando ambos nombres son null")
        void testEqualsAmbosNulos() {

            // Arrange
            GeneroRequestDTO dto1 = new GeneroRequestDTO();
            GeneroRequestDTO dto2 = new GeneroRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode coincide cuando ambos objetos son iguales")
        void testHashCodeObjetosIguales() {

            // Arrange
            GeneroRequestDTO dto1 = new GeneroRequestDTO();
            dto1.setNombre("Acción");

            GeneroRequestDTO dto2 = new GeneroRequestDTO();
            dto2.setNombre("Acción");

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            GeneroRequestDTO dto = new GeneroRequestDTO();
            dto.setNombre("Acción");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Acción"));
        }
    }
}