package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoEnvioResponseDTO")
class MetodoEnvioResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoEnvioResponseDTO dto = new MetodoEnvioResponseDTO();

            dto.setId(1L);
            dto.setNombre("Retiro en tienda");
            dto.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto.setActivo(true);

            assertEquals(1L, dto.getId());
            assertEquals("Retiro en tienda", dto.getNombre());
            assertEquals("Retiro presencial en sucursal Plaza Oeste", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            MetodoEnvioResponseDTO dto = new MetodoEnvioResponseDTO();

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
            MetodoEnvioResponseDTO dto1 = new MetodoEnvioResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Retiro en tienda");
            dto1.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto1.setActivo(true);

            MetodoEnvioResponseDTO dto2 = new MetodoEnvioResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Retiro en tienda");
            dto2.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el id")
        void testEqualsIdDistinto() {

            // Arrange
            MetodoEnvioResponseDTO dto1 = new MetodoEnvioResponseDTO();
            dto1.setId(1L);

            MetodoEnvioResponseDTO dto2 = new MetodoEnvioResponseDTO();
            dto2.setId(2L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            MetodoEnvioResponseDTO dto1 = new MetodoEnvioResponseDTO();
            dto1.setNombre("Retiro en tienda");

            MetodoEnvioResponseDTO dto2 = new MetodoEnvioResponseDTO();
            dto2.setNombre("Despacho a domicilio");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia la descripción")
        void testEqualsDescripcionDistinta() {

            // Arrange
            MetodoEnvioResponseDTO dto1 = new MetodoEnvioResponseDTO();
            dto1.setDescripcion("Retiro presencial");

            MetodoEnvioResponseDTO dto2 = new MetodoEnvioResponseDTO();
            dto2.setDescripcion("Entrega a domicilio");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia activo")
        void testEqualsActivoDistinto() {

            // Arrange
            MetodoEnvioResponseDTO dto1 = new MetodoEnvioResponseDTO();
            dto1.setActivo(true);

            MetodoEnvioResponseDTO dto2 = new MetodoEnvioResponseDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            MetodoEnvioResponseDTO dto = new MetodoEnvioResponseDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            MetodoEnvioResponseDTO dto = new MetodoEnvioResponseDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            MetodoEnvioResponseDTO dto = new MetodoEnvioResponseDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoEnvioResponseDTO dto = new MetodoEnvioResponseDTO();
            dto.setId(1L);
            dto.setNombre("Retiro en tienda");
            dto.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Retiro en tienda"));
            assertTrue(resultado.contains("descripcion=Retiro presencial en sucursal Plaza Oeste"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}