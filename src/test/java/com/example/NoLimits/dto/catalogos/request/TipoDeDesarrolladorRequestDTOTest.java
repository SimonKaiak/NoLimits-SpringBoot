package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoDeDesarrolladorRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoDeDesarrolladorRequestDTO")
class TipoDeDesarrolladorRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            TipoDeDesarrolladorRequestDTO dto = new TipoDeDesarrolladorRequestDTO();

            dto.setNombre("Estudio");

            assertEquals("Estudio", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoDeDesarrolladorRequestDTO dto = new TipoDeDesarrolladorRequestDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoDeDesarrolladorRequestDTO dto1 = new TipoDeDesarrolladorRequestDTO();
            dto1.setNombre("Estudio");

            TipoDeDesarrolladorRequestDTO dto2 = new TipoDeDesarrolladorRequestDTO();
            dto2.setNombre("Estudio");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoDeDesarrolladorRequestDTO dto = new TipoDeDesarrolladorRequestDTO();
            dto.setNombre("Estudio");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Estudio"));
        }
    }
}