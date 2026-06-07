package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.PlataformaUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PlataformaUpdateDTO")
class PlataformaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();

            dto.setNombre("PlayStation 5");

            assertEquals("PlayStation 5", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            PlataformaUpdateDTO dto1 = new PlataformaUpdateDTO();
            dto1.setNombre("PlayStation 5");

            PlataformaUpdateDTO dto2 = new PlataformaUpdateDTO();
            dto2.setNombre("PlayStation 5");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            PlataformaUpdateDTO dto = new PlataformaUpdateDTO();
            dto.setNombre("PlayStation 5");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=PlayStation 5"));
        }
    }
}