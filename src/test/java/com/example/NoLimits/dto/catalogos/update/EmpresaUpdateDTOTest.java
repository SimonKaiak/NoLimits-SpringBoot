package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresaUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EmpresaUpdateDTO")
class EmpresaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();

            dto.setNombre("Warner Bros Games");
            dto.setActivo(true);

            assertEquals("Warner Bros Games", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();

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
            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            dto1.setNombre("Warner Bros Games");
            dto1.setActivo(true);

            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();
            dto2.setNombre("Warner Bros Games");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            dto1.setNombre("Warner Bros Games");

            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();
            dto2.setNombre("Electronic Arts");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            dto1.setActivo(true);

            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            dto1.setNombre("Warner Bros Games");

            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            dto1.setActivo(true);

            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            dto1.setNombre("Warner Bros Games");

            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();
            dto2.setNombre("Electronic Arts");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            dto1.setActivo(true);

            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual cuando ambos objetos tienen los mismos valores")
        void testHashCodeIgual() {

            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            dto1.setNombre("Warner Bros Games");
            dto1.setActivo(true);

            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();
            dto2.setNombre("Warner Bros Games");
            dto2.setActivo(true);

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es consistente para la misma instancia")
        void testHashCodeConsistente() {

            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();
            dto.setNombre("Warner Bros Games");

            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();

            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("toString no retorna null")
        void testToStringNoEsNull() {

            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();

            String resultado = dto.toString();

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();
            dto.setNombre("Warner Bros Games");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Warner Bros Games"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}