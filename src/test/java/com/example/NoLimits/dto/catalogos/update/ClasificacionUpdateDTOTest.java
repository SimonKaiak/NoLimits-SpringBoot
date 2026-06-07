package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.ClasificacionUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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