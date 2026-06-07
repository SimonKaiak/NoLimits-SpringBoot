package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposDeDesarrolladorResponseDTO")
class TiposDeDesarrolladorResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposDeDesarrolladorResponseDTO dto = new TiposDeDesarrolladorResponseDTO();

            dto.setId(5L);
            dto.setDesarrolladorId(10L);
            dto.setTipoDeDesarrolladorId(1L);
            dto.setTipoDeDesarrolladorNombre("Estudio");

            assertEquals(5L, dto.getId());
            assertEquals(10L, dto.getDesarrolladorId());
            assertEquals(1L, dto.getTipoDeDesarrolladorId());
            assertEquals("Estudio", dto.getTipoDeDesarrolladorNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TiposDeDesarrolladorResponseDTO dto = new TiposDeDesarrolladorResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getDesarrolladorId());
            assertNull(dto.getTipoDeDesarrolladorId());
            assertNull(dto.getTipoDeDesarrolladorNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TiposDeDesarrolladorResponseDTO dto1 = new TiposDeDesarrolladorResponseDTO();
            dto1.setId(5L);
            dto1.setDesarrolladorId(10L);
            dto1.setTipoDeDesarrolladorId(1L);
            dto1.setTipoDeDesarrolladorNombre("Estudio");

            TiposDeDesarrolladorResponseDTO dto2 = new TiposDeDesarrolladorResponseDTO();
            dto2.setId(5L);
            dto2.setDesarrolladorId(10L);
            dto2.setTipoDeDesarrolladorId(1L);
            dto2.setTipoDeDesarrolladorNombre("Estudio");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposDeDesarrolladorResponseDTO dto = new TiposDeDesarrolladorResponseDTO();
            dto.setId(5L);
            dto.setDesarrolladorId(10L);
            dto.setTipoDeDesarrolladorId(1L);
            dto.setTipoDeDesarrolladorNombre("Estudio");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=5"));
            assertTrue(resultado.contains("desarrolladorId=10"));
            assertTrue(resultado.contains("tipoDeDesarrolladorId=1"));
            assertTrue(resultado.contains("tipoDeDesarrolladorNombre=Estudio"));
        }
    }
}