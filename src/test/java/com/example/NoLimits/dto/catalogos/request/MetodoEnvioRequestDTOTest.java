package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoEnvioRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoEnvioRequestDTO")
class MetodoEnvioRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();

            dto.setNombre("Retiro en tienda");
            dto.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto.setActivo(true);

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
            MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();

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
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setNombre("Retiro en tienda");
            dto1.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto1.setActivo(true);

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setNombre("Retiro en tienda");
            dto2.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setNombre("Retiro en tienda");

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setNombre("Despacho a domicilio");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia la descripcion")
        void testEqualsDescripcionDistinta() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setDescripcion("Retiro presencial");

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setDescripcion("Entrega en domicilio");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setActivo(true);

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            // Arrange
            MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();

            // Act + Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            // Arrange
            MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();

            // Act + Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            // Arrange
            MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();

            // Act + Assert
            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setNombre("Retiro en tienda");

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene descripcion")
        void testEqualsDescripcionNuloVsNoNulo() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setDescripcion("Retiro presencial");

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setActivo(true);

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setNombre("Retiro en tienda");

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setNombre("Despacho a domicilio");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia la descripcion")
        void testHashCodeDescripcionDistinta() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setDescripcion("Retiro presencial");

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setDescripcion("Entrega en domicilio");

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setActivo(true);

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando nombre coincide pero descripcion es distinta")
        void testEqualsNombreIgualDescripcionDistinta() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setNombre("Retiro");
            dto1.setDescripcion("Sucursal A");

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setNombre("Retiro");
            dto2.setDescripcion("Sucursal B");

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando nombre y descripcion coinciden pero activo es distinto")
        void testEqualsActivoDistintoConDemasCamposIguales() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setNombre("Retiro");
            dto1.setDescripcion("Sucursal");
            dto1.setActivo(true);

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setNombre("Retiro");
            dto2.setDescripcion("Sucursal");
            dto2.setActivo(false);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando todos los campos coinciden")
        void testEqualsTodosLosCamposIguales() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setNombre("Retiro");
            dto1.setDescripcion("Sucursal");
            dto1.setActivo(true);

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setNombre("Retiro");
            dto2.setDescripcion("Sucursal");
            dto2.setActivo(true);

            // Act + Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode coincide cuando los objetos son iguales")
        void testHashCodeObjetosIguales() {

            // Arrange
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setNombre("Retiro");
            dto1.setDescripcion("Sucursal");
            dto1.setActivo(true);

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setNombre("Retiro");
            dto2.setDescripcion("Sucursal");
            dto2.setActivo(true);

            // Act + Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();
            dto.setNombre("Retiro en tienda");
            dto.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Retiro en tienda"));
            assertTrue(resultado.contains("descripcion=Retiro presencial en sucursal Plaza Oeste"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}