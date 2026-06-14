package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.DesarrolladorUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DesarrolladorUpdateDTO")
class DesarrolladorUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            DesarrolladorUpdateDTO dto = new DesarrolladorUpdateDTO();

            dto.setNombre("Insomniac Games");
            dto.setActivo(true);

            assertEquals("Insomniac Games", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            DesarrolladorUpdateDTO dto = new DesarrolladorUpdateDTO();

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
            DesarrolladorUpdateDTO dto1 = new DesarrolladorUpdateDTO();
            dto1.setNombre("Insomniac Games");
            dto1.setActivo(true);

            DesarrolladorUpdateDTO dto2 = new DesarrolladorUpdateDTO();
            dto2.setNombre("Insomniac Games");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            DesarrolladorUpdateDTO dto1 = new DesarrolladorUpdateDTO();
            dto1.setNombre("Insomniac Games");

            DesarrolladorUpdateDTO dto2 = new DesarrolladorUpdateDTO();
            dto2.setNombre("Naughty Dog");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            DesarrolladorUpdateDTO dto1 = new DesarrolladorUpdateDTO();
            dto1.setActivo(true);

            DesarrolladorUpdateDTO dto2 = new DesarrolladorUpdateDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            DesarrolladorUpdateDTO dto = new DesarrolladorUpdateDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            DesarrolladorUpdateDTO dto = new DesarrolladorUpdateDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            DesarrolladorUpdateDTO dto = new DesarrolladorUpdateDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            DesarrolladorUpdateDTO dto1 = new DesarrolladorUpdateDTO();
            dto1.setNombre("Insomniac Games");

            DesarrolladorUpdateDTO dto2 = new DesarrolladorUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            DesarrolladorUpdateDTO dto1 = new DesarrolladorUpdateDTO();
            dto1.setActivo(true);

            DesarrolladorUpdateDTO dto2 = new DesarrolladorUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            DesarrolladorUpdateDTO dto1 = new DesarrolladorUpdateDTO();
            DesarrolladorUpdateDTO dto2 = new DesarrolladorUpdateDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            DesarrolladorUpdateDTO dto1 = new DesarrolladorUpdateDTO();
            dto1.setNombre("Insomniac Games");

            DesarrolladorUpdateDTO dto2 = new DesarrolladorUpdateDTO();
            dto2.setNombre("Naughty Dog");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            DesarrolladorUpdateDTO dto1 = new DesarrolladorUpdateDTO();
            dto1.setActivo(true);

            DesarrolladorUpdateDTO dto2 = new DesarrolladorUpdateDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode es igual cuando ambos objetos tienen los mismos valores")
        void testHashCodeIgual() {

            DesarrolladorUpdateDTO dto1 = new DesarrolladorUpdateDTO();
            dto1.setNombre("Insomniac Games");
            dto1.setActivo(true);

            DesarrolladorUpdateDTO dto2 = new DesarrolladorUpdateDTO();
            dto2.setNombre("Insomniac Games");
            dto2.setActivo(true);

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            DesarrolladorUpdateDTO dto = new DesarrolladorUpdateDTO();
            dto.setNombre("Insomniac Games");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Insomniac Games"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}