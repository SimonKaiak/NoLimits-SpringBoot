package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.EstadoUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EstadoUpdateDTO")
class EstadoUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EstadoUpdateDTO dto = new EstadoUpdateDTO();

            dto.setNombre("Agotado");
            dto.setDescripcion("Producto sin stock disponible");
            dto.setActivo(true);

            assertEquals("Agotado", dto.getNombre());
            assertEquals("Producto sin stock disponible", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            EstadoUpdateDTO dto = new EstadoUpdateDTO();

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
            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setNombre("Agotado");
            dto1.setDescripcion("Producto sin stock disponible");
            dto1.setActivo(true);

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();
            dto2.setNombre("Agotado");
            dto2.setDescripcion("Producto sin stock disponible");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setNombre("Agotado");

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();
            dto2.setNombre("Disponible");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia la descripcion")
        void testEqualsDescripcionDistinta() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setDescripcion("Producto sin stock disponible");

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();
            dto2.setDescripcion("Producto disponible para compra");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setActivo(true);

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            EstadoUpdateDTO dto = new EstadoUpdateDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            EstadoUpdateDTO dto = new EstadoUpdateDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            EstadoUpdateDTO dto = new EstadoUpdateDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setNombre("Agotado");

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene descripcion")
        void testEqualsDescripcionNuloVsNoNulo() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setDescripcion("Producto sin stock disponible");

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setActivo(true);

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setNombre("Agotado");

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();
            dto2.setNombre("Disponible");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia la descripcion")
        void testHashCodeDescripcionDistinta() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setDescripcion("Producto sin stock disponible");

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();
            dto2.setDescripcion("Producto disponible para compra");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setActivo(true);

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual cuando ambos objetos tienen los mismos valores")
        void testHashCodeIgual() {

            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setNombre("Agotado");
            dto1.setDescripcion("Producto sin stock disponible");
            dto1.setActivo(true);

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();
            dto2.setNombre("Agotado");
            dto2.setDescripcion("Producto sin stock disponible");
            dto2.setActivo(true);

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            EstadoUpdateDTO dto = new EstadoUpdateDTO();
            dto.setNombre("Agotado");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            EstadoUpdateDTO dto = new EstadoUpdateDTO();

            String resultado = dto.toString();

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EstadoUpdateDTO dto = new EstadoUpdateDTO();
            dto.setNombre("Agotado");
            dto.setDescripcion("Producto sin stock disponible");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Agotado"));
            assertTrue(resultado.contains("descripcion=Producto sin stock disponible"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}