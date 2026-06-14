package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoPagoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoPagoRequestDTO")
class MetodoPagoRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();

            dto.setNombre("Tarjeta de Crédito");
            dto.setActivo(true);

            assertEquals("Tarjeta de Crédito", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();

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
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setNombre("Tarjeta de Crédito");
            dto1.setActivo(true);

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();
            dto2.setNombre("Tarjeta de Crédito");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setNombre("Tarjeta de Crédito");

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();
            dto2.setNombre("Transferencia");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            // Arrange
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setActivo(true);

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setNombre("Tarjeta de Crédito");

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            // Arrange
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setActivo(true);

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando nombre coincide pero activo es distinto")
        void testEqualsActivoDistintoConNombreIgual() {

            // Arrange
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setNombre("Tarjeta de Crédito");
            dto1.setActivo(true);

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();
            dto2.setNombre("Tarjeta de Crédito");
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setNombre("Tarjeta de Crédito");

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();
            dto2.setNombre("Transferencia");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            // Arrange
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setActivo(true);

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode coincide cuando los objetos son iguales")
        void testHashCodeObjetosIguales() {

            // Arrange
            MetodoPagoRequestDTO dto1 = new MetodoPagoRequestDTO();
            dto1.setNombre("Tarjeta de Crédito");
            dto1.setActivo(true);

            MetodoPagoRequestDTO dto2 = new MetodoPagoRequestDTO();
            dto2.setNombre("Tarjeta de Crédito");
            dto2.setActivo(true);

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();
            dto.setNombre("Tarjeta de Crédito");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Tarjeta de Crédito"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}