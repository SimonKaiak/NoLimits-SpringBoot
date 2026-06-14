package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.ClasificacionUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ClasificacionUpdateDTO")
class ClasificacionUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            ClasificacionUpdateDTO dto = new ClasificacionUpdateDTO();

            dto.setNombre("T");
            dto.setDescripcion("Contenido apto para adolescentes.");
            dto.setActivo(true);

            assertEquals("T", dto.getNombre());
            assertEquals("Contenido apto para adolescentes.", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            ClasificacionUpdateDTO dto = new ClasificacionUpdateDTO();

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
            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setNombre("T");
            dto1.setDescripcion("Contenido apto para adolescentes.");
            dto1.setActivo(true);

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();
            dto2.setNombre("T");
            dto2.setDescripcion("Contenido apto para adolescentes.");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el nombre")
        void testEqualsNombreDistinto() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setNombre("T");

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();
            dto2.setNombre("M");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia la descripcion")
        void testEqualsDescripcionDistinta() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setDescripcion("Contenido apto para adolescentes.");

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();
            dto2.setDescripcion("Solo para adultos.");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia el activo")
        void testEqualsActivoDistinto() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setActivo(true);

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            ClasificacionUpdateDTO dto = new ClasificacionUpdateDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna true cuando compara consigo mismo")
        void testEqualsMismaInstancia() {

            ClasificacionUpdateDTO dto = new ClasificacionUpdateDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsOtroTipo() {

            ClasificacionUpdateDTO dto = new ClasificacionUpdateDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene nombre")
        void testEqualsNombreNuloVsNoNulo() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setNombre("T");

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene descripcion")
        void testEqualsDescripcionNuloVsNoNulo() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setDescripcion("Contenido apto para adolescentes.");

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando solo uno tiene activo")
        void testEqualsActivoNuloVsNoNulo() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setActivo(true);

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna true cuando ambos objetos tienen campos null")
        void testEqualsAmbosNulos() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el nombre")
        void testHashCodeNombreDistinto() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setNombre("T");

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();
            dto2.setNombre("M");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia la descripcion")
        void testHashCodeDescripcionDistinta() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setDescripcion("Contenido apto para adolescentes.");

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();
            dto2.setDescripcion("Solo para adultos.");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el activo")
        void testHashCodeActivoDistinto() {

            ClasificacionUpdateDTO dto1 = new ClasificacionUpdateDTO();
            dto1.setActivo(true);

            ClasificacionUpdateDTO dto2 = new ClasificacionUpdateDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            ClasificacionUpdateDTO dto = new ClasificacionUpdateDTO();
            dto.setNombre("T");
            dto.setDescripcion("Contenido apto para adolescentes.");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=T"));
            assertTrue(resultado.contains("descripcion=Contenido apto para adolescentes."));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}