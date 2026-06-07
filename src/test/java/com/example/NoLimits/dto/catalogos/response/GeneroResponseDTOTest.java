package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.GeneroResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GeneroResponseDTO")
class GeneroResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            GeneroResponseDTO dto = new GeneroResponseDTO();

            dto.setId(1L);
            dto.setNombre("Acción");

            assertEquals(1L, dto.getId());
            assertEquals("Acción", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            GeneroResponseDTO dto = new GeneroResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            GeneroResponseDTO dto1 = new GeneroResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Acción");

            GeneroResponseDTO dto2 = new GeneroResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Acción");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            GeneroResponseDTO dto = new GeneroResponseDTO();
            dto.setId(1L);
            dto.setNombre("Acción");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Acción"));
        }
    }
}