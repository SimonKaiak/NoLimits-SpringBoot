package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.GenerosResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GenerosResponseDTO")
class GenerosResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            GenerosResponseDTO dto = new GenerosResponseDTO();

            dto.setId(5L);
            dto.setProductoId(10L);
            dto.setGeneroId(1L);
            dto.setGeneroNombre("Acción");

            assertEquals(5L, dto.getId());
            assertEquals(10L, dto.getProductoId());
            assertEquals(1L, dto.getGeneroId());
            assertEquals("Acción", dto.getGeneroNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            GenerosResponseDTO dto = new GenerosResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getProductoId());
            assertNull(dto.getGeneroId());
            assertNull(dto.getGeneroNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            GenerosResponseDTO dto1 = new GenerosResponseDTO();
            dto1.setId(5L);
            dto1.setProductoId(10L);
            dto1.setGeneroId(1L);
            dto1.setGeneroNombre("Acción");

            GenerosResponseDTO dto2 = new GenerosResponseDTO();
            dto2.setId(5L);
            dto2.setProductoId(10L);
            dto2.setGeneroId(1L);
            dto2.setGeneroNombre("Acción");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            GenerosResponseDTO dto = new GenerosResponseDTO();
            dto.setId(5L);
            dto.setProductoId(10L);
            dto.setGeneroId(1L);
            dto.setGeneroNombre("Acción");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=5"));
            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("generoId=1"));
            assertTrue(resultado.contains("generoNombre=Acción"));
        }
    }
}