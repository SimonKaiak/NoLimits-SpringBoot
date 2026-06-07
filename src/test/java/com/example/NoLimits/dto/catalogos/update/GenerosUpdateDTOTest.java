package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.GenerosUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GenerosUpdateDTO")
class GenerosUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            GenerosUpdateDTO dto = new GenerosUpdateDTO();

            dto.setProductoId(10L);
            dto.setGeneroId(2L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(2L, dto.getGeneroId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            GenerosUpdateDTO dto = new GenerosUpdateDTO();

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
            GenerosUpdateDTO dto1 = new GenerosUpdateDTO();
            dto1.setProductoId(10L);
            dto1.setGeneroId(2L);

            GenerosUpdateDTO dto2 = new GenerosUpdateDTO();
            dto2.setProductoId(10L);
            dto2.setGeneroId(2L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            GenerosUpdateDTO dto = new GenerosUpdateDTO();
            dto.setProductoId(10L);
            dto.setGeneroId(2L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("generoId=2"));
        }
    }
}