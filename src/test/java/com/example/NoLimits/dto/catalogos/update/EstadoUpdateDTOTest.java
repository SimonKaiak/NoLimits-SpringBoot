package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.EstadoUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EstadoUpdateDTO")
class EstadoUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EstadoUpdateDTO dto = new EstadoUpdateDTO();

            dto.setNombre("Agotado");
            dto.setDescripcion("Producto sin stock disponible");
            dto.setActivo(true);

            assertEquals("Agotado", dto.getNombre());
            assertEquals("Producto sin stock disponible", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            EstadoUpdateDTO dto = new EstadoUpdateDTO();

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
            EstadoUpdateDTO dto1 = new EstadoUpdateDTO();
            dto1.setNombre("Agotado");
            dto1.setDescripcion("Producto sin stock disponible");
            dto1.setActivo(true);

            EstadoUpdateDTO dto2 = new EstadoUpdateDTO();
            dto2.setNombre("Agotado");
            dto2.setDescripcion("Producto sin stock disponible");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EstadoUpdateDTO dto = new EstadoUpdateDTO();
            dto.setNombre("Agotado");
            dto.setDescripcion("Producto sin stock disponible");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Agotado"));
            assertTrue(resultado.contains("descripcion=Producto sin stock disponible"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}