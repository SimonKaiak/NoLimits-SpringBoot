package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TiposDeDesarrolladorRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposDeDesarrolladorRequestDTO")
class TiposDeDesarrolladorRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposDeDesarrolladorRequestDTO dto = new TiposDeDesarrolladorRequestDTO();

            dto.setDesarrolladorId(10L);
            dto.setTipoDeDesarrolladorId(1L);

            assertEquals(10L, dto.getDesarrolladorId());
            assertEquals(1L, dto.getTipoDeDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TiposDeDesarrolladorRequestDTO dto = new TiposDeDesarrolladorRequestDTO();

            assertNull(dto.getDesarrolladorId());
            assertNull(dto.getTipoDeDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TiposDeDesarrolladorRequestDTO dto1 = new TiposDeDesarrolladorRequestDTO();
            dto1.setDesarrolladorId(10L);
            dto1.setTipoDeDesarrolladorId(1L);

            TiposDeDesarrolladorRequestDTO dto2 = new TiposDeDesarrolladorRequestDTO();
            dto2.setDesarrolladorId(10L);
            dto2.setTipoDeDesarrolladorId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposDeDesarrolladorRequestDTO dto = new TiposDeDesarrolladorRequestDTO();
            dto.setDesarrolladorId(10L);
            dto.setTipoDeDesarrolladorId(1L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("desarrolladorId=10"));
            assertTrue(resultado.contains("tipoDeDesarrolladorId=1"));
        }
    }
}