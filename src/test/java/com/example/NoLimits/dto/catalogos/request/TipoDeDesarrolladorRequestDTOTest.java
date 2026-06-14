package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoDeDesarrolladorRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoDeDesarrolladorRequestDTO")
class TipoDeDesarrolladorRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            TipoDeDesarrolladorRequestDTO dto = new TipoDeDesarrolladorRequestDTO();

            dto.setNombre("Estudio");

            assertEquals("Estudio", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoDeDesarrolladorRequestDTO dto = new TipoDeDesarrolladorRequestDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoDeDesarrolladorRequestDTO dto1 = new TipoDeDesarrolladorRequestDTO();
            dto1.setNombre("Estudio");

            TipoDeDesarrolladorRequestDTO dto2 = new TipoDeDesarrolladorRequestDTO();
            dto2.setNombre("Estudio");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            TipoDeDesarrolladorRequestDTO dto1 =
                    new TipoDeDesarrolladorRequestDTO();
            dto1.setNombre("Estudio");

            TipoDeDesarrolladorRequestDTO dto2 =
                    new TipoDeDesarrolladorRequestDTO();
            dto2.setNombre("Publisher");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            TipoDeDesarrolladorRequestDTO dto =
                    new TipoDeDesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            TipoDeDesarrolladorRequestDTO dto =
                    new TipoDeDesarrolladorRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            TipoDeDesarrolladorRequestDTO dto =
                    new TipoDeDesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            TipoDeDesarrolladorRequestDTO dto1 =
                    new TipoDeDesarrolladorRequestDTO();
            dto1.setNombre("Estudio");

            TipoDeDesarrolladorRequestDTO dto2 =
                    new TipoDeDesarrolladorRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos nombres son null")
        void testEqualsAmbosNulos() {

            // Arrange
            TipoDeDesarrolladorRequestDTO dto1 =
                    new TipoDeDesarrolladorRequestDTO();

            TipoDeDesarrolladorRequestDTO dto2 =
                    new TipoDeDesarrolladorRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            TipoDeDesarrolladorRequestDTO dto1 =
                    new TipoDeDesarrolladorRequestDTO();
            dto1.setNombre("Estudio");

            TipoDeDesarrolladorRequestDTO dto2 =
                    new TipoDeDesarrolladorRequestDTO();
            dto2.setNombre("Publisher");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode coincide cuando los objetos son iguales")
        void testHashCodeObjetosIguales() {

            // Arrange
            TipoDeDesarrolladorRequestDTO dto1 =
                    new TipoDeDesarrolladorRequestDTO();
            dto1.setNombre("Estudio");

            TipoDeDesarrolladorRequestDTO dto2 =
                    new TipoDeDesarrolladorRequestDTO();
            dto2.setNombre("Estudio");

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            TipoDeDesarrolladorRequestDTO dto =
                    new TipoDeDesarrolladorRequestDTO();
            dto.setNombre("Estudio");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoDeDesarrolladorRequestDTO dto = new TipoDeDesarrolladorRequestDTO();
            dto.setNombre("Estudio");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Estudio"));
        }
    }
}