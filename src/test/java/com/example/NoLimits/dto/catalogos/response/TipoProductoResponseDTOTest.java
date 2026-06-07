package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoProductoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoProductoResponseDTO")
class TipoProductoResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TipoProductoResponseDTO dto = new TipoProductoResponseDTO();

            dto.setId(1L);
            dto.setNombre("Película");
            dto.setDescripcion("Categoría general para clasificar productos");
            dto.setActivo(true);

            assertEquals(1L, dto.getId());
            assertEquals("Película", dto.getNombre());
            assertEquals("Categoría general para clasificar productos", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoProductoResponseDTO dto = new TipoProductoResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
            assertNull(dto.getDescripcion());
            assertNull(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoProductoResponseDTO dto1 = new TipoProductoResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Película");
            dto1.setDescripcion("Categoría general para clasificar productos");
            dto1.setActivo(true);

            TipoProductoResponseDTO dto2 = new TipoProductoResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Película");
            dto2.setDescripcion("Categoría general para clasificar productos");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoProductoResponseDTO dto = new TipoProductoResponseDTO();
            dto.setId(1L);
            dto.setNombre("Película");
            dto.setDescripcion("Categoría general para clasificar productos");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Película"));
            assertTrue(resultado.contains("descripcion=Categoría general para clasificar productos"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}