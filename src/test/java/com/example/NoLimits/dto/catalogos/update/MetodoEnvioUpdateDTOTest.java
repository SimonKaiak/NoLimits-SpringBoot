package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoEnvioUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoEnvioUpdateDTO")
class MetodoEnvioUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();

            dto.setNombre("Despacho a domicilio");
            dto.setDescripcion("Entrega directa a la dirección del cliente");
            dto.setActivo(true);

            assertEquals("Despacho a domicilio", dto.getNombre());
            assertEquals("Entrega directa a la dirección del cliente", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();

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
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setNombre("Despacho a domicilio");
            dto1.setDescripcion("Entrega directa a la dirección del cliente");
            dto1.setActivo(true);

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();
            dto2.setNombre("Despacho a domicilio");
            dto2.setDescripcion("Entrega directa a la dirección del cliente");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setNombre("Despacho a domicilio");

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();
            dto2.setNombre("Retiro en tienda");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia la descripcion")
        void testEqualsDescripcionDistinta() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setDescripcion("Entrega directa al cliente");

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();
            dto2.setDescripcion("Retiro en sucursal");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setActivo(true);

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setNombre("Despacho a domicilio");

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene descripcion")
        void testEqualsDescripcionNuloVsNoNulo() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setDescripcion("Entrega directa");

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setActivo(true);

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setNombre("Despacho a domicilio");

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();
            dto2.setNombre("Retiro en tienda");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia la descripcion")
        void testHashCodeDescripcionDistinta() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setDescripcion("Entrega directa");

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();
            dto2.setDescripcion("Retiro en sucursal");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setActivo(true);

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual para objetos equivalentes")
        void testHashCodeIgual() {

            // Arrange
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setNombre("Despacho a domicilio");
            dto1.setDescripcion("Entrega directa");
            dto1.setActivo(true);

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();
            dto2.setNombre("Despacho a domicilio");
            dto2.setDescripcion("Entrega directa");
            dto2.setActivo(true);

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            // Arrange
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();
            dto.setNombre("Despacho a domicilio");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            // Act + Assert
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            // Arrange
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();
            dto.setNombre("Despacho a domicilio");
            dto.setDescripcion("Entrega directa a la dirección del cliente");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Despacho a domicilio"));
            assertTrue(resultado.contains("descripcion=Entrega directa a la dirección del cliente"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}