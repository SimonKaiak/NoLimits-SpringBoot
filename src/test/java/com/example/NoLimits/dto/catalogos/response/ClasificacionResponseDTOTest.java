package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.ClasificacionResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ClasificacionResponseDTO")
class ClasificacionResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            ClasificacionResponseDTO dto = new ClasificacionResponseDTO();

            dto.setId(1L);
            dto.setNombre("T");
            dto.setDescripcion("Contenido apto para adolescentes.");
            dto.setActivo(true);

            assertEquals(1L, dto.getId());
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
            ClasificacionResponseDTO dto = new ClasificacionResponseDTO();

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
            ClasificacionResponseDTO dto1 = new ClasificacionResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("T");
            dto1.setDescripcion("Contenido apto para adolescentes.");
            dto1.setActivo(true);

            ClasificacionResponseDTO dto2 = new ClasificacionResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("T");
            dto2.setDescripcion("Contenido apto para adolescentes.");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            ClasificacionResponseDTO dto = new ClasificacionResponseDTO();
            dto.setId(1L);
            dto.setNombre("T");
            dto.setDescripcion("Contenido apto para adolescentes.");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=T"));
            assertTrue(resultado.contains("descripcion=Contenido apto para adolescentes."));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}