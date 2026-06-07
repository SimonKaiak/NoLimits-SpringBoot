package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.GeneroUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GeneroUpdateDTO")
class GeneroUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            GeneroUpdateDTO dto = new GeneroUpdateDTO();

            dto.setNombre("Aventura");

            assertEquals("Aventura", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            GeneroUpdateDTO dto = new GeneroUpdateDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            GeneroUpdateDTO dto1 = new GeneroUpdateDTO();
            dto1.setNombre("Aventura");

            GeneroUpdateDTO dto2 = new GeneroUpdateDTO();
            dto2.setNombre("Aventura");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            GeneroUpdateDTO dto = new GeneroUpdateDTO();
            dto.setNombre("Aventura");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Aventura"));
        }
    }
}