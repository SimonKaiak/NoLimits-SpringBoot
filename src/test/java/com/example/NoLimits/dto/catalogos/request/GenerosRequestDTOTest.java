package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.GenerosRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GenerosRequestDTO")
class GenerosRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            GenerosRequestDTO dto = new GenerosRequestDTO();

            dto.setProductoId(10L);
            dto.setGeneroId(1L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(1L, dto.getGeneroId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            GenerosRequestDTO dto = new GenerosRequestDTO();

            assertNull(dto.getProductoId());
            assertNull(dto.getGeneroId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            GenerosRequestDTO dto1 = new GenerosRequestDTO();
            dto1.setProductoId(10L);
            dto1.setGeneroId(1L);

            GenerosRequestDTO dto2 = new GenerosRequestDTO();
            dto2.setProductoId(10L);
            dto2.setGeneroId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            GenerosRequestDTO dto = new GenerosRequestDTO();
            dto.setProductoId(10L);
            dto.setGeneroId(1L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("generoId=1"));
        }
    }
}