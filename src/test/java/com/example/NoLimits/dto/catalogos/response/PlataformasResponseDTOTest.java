package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformasResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PlataformasResponseDTO")
class PlataformasResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            PlataformasResponseDTO dto = new PlataformasResponseDTO();

            dto.setId(5L);
            dto.setProductoId(10L);
            dto.setPlataformaId(1L);
            dto.setPlataformaNombre("PC");

            assertEquals(5L, dto.getId());
            assertEquals(10L, dto.getProductoId());
            assertEquals(1L, dto.getPlataformaId());
            assertEquals("PC", dto.getPlataformaNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            PlataformasResponseDTO dto = new PlataformasResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getProductoId());
            assertNull(dto.getPlataformaId());
            assertNull(dto.getPlataformaNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            PlataformasResponseDTO dto1 = new PlataformasResponseDTO();
            dto1.setId(5L);
            dto1.setProductoId(10L);
            dto1.setPlataformaId(1L);
            dto1.setPlataformaNombre("PC");

            PlataformasResponseDTO dto2 = new PlataformasResponseDTO();
            dto2.setId(5L);
            dto2.setProductoId(10L);
            dto2.setPlataformaId(1L);
            dto2.setPlataformaNombre("PC");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            PlataformasResponseDTO dto = new PlataformasResponseDTO();
            dto.setId(5L);
            dto.setProductoId(10L);
            dto.setPlataformaId(1L);
            dto.setPlataformaNombre("PC");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=5"));
            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("plataformaId=1"));
            assertTrue(resultado.contains("plataformaNombre=PC"));
        }
    }
}