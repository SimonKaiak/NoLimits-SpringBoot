package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EstadoResponseDTO")
class EstadoResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EstadoResponseDTO dto = new EstadoResponseDTO();

            dto.setId(1L);
            dto.setNombre("Activo");
            dto.setDescripcion("Producto disponible para su compra");
            dto.setActivo(true);

            assertEquals(1L, dto.getId());
            assertEquals("Activo", dto.getNombre());
            assertEquals("Producto disponible para su compra", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            EstadoResponseDTO dto = new EstadoResponseDTO();

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
            EstadoResponseDTO dto1 = new EstadoResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Activo");
            dto1.setDescripcion("Producto disponible para su compra");
            dto1.setActivo(true);

            EstadoResponseDTO dto2 = new EstadoResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Activo");
            dto2.setDescripcion("Producto disponible para su compra");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EstadoResponseDTO dto = new EstadoResponseDTO();
            dto.setId(1L);
            dto.setNombre("Activo");
            dto.setDescripcion("Producto disponible para su compra");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Activo"));
            assertTrue(resultado.contains("descripcion=Producto disponible para su compra"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}