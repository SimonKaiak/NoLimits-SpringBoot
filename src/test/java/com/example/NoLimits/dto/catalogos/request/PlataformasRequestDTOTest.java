package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.PlataformasRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PlataformasRequestDTO")
class PlataformasRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            PlataformasRequestDTO dto = new PlataformasRequestDTO();

            dto.setProductoId(10L);
            dto.setPlataformaId(1L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(1L, dto.getPlataformaId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            PlataformasRequestDTO dto = new PlataformasRequestDTO();

            assertNull(dto.getProductoId());
            assertNull(dto.getPlataformaId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            PlataformasRequestDTO dto1 = new PlataformasRequestDTO();
            dto1.setProductoId(10L);
            dto1.setPlataformaId(1L);

            PlataformasRequestDTO dto2 = new PlataformasRequestDTO();
            dto2.setProductoId(10L);
            dto2.setPlataformaId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            PlataformasRequestDTO dto = new PlataformasRequestDTO();
            dto.setProductoId(10L);
            dto.setPlataformaId(1L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("plataformaId=1"));
        }
    }
}