package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoDeDesarrolladorResponseDTO")
class TipoDeDesarrolladorResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TipoDeDesarrolladorResponseDTO dto = new TipoDeDesarrolladorResponseDTO();

            dto.setId(1L);
            dto.setNombre("Estudio");

            assertEquals(1L, dto.getId());
            assertEquals("Estudio", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoDeDesarrolladorResponseDTO dto = new TipoDeDesarrolladorResponseDTO();

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
            TipoDeDesarrolladorResponseDTO dto1 = new TipoDeDesarrolladorResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Estudio");

            TipoDeDesarrolladorResponseDTO dto2 = new TipoDeDesarrolladorResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Estudio");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoDeDesarrolladorResponseDTO dto = new TipoDeDesarrolladorResponseDTO();
            dto.setId(1L);
            dto.setNombre("Estudio");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Estudio"));
        }
    }
}