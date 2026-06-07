package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoProductoUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoProductoUpdateDTO")
class TipoProductoUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TipoProductoUpdateDTO dto = new TipoProductoUpdateDTO();

            dto.setNombre("Videojuego");
            dto.setDescripcion("Categoría para videojuegos en distintas plataformas");
            dto.setActivo(true);

            assertEquals("Videojuego", dto.getNombre());
            assertEquals("Categoría para videojuegos en distintas plataformas", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoProductoUpdateDTO dto = new TipoProductoUpdateDTO();

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
            TipoProductoUpdateDTO dto1 = new TipoProductoUpdateDTO();
            dto1.setNombre("Videojuego");
            dto1.setDescripcion("Categoría para videojuegos en distintas plataformas");
            dto1.setActivo(true);

            TipoProductoUpdateDTO dto2 = new TipoProductoUpdateDTO();
            dto2.setNombre("Videojuego");
            dto2.setDescripcion("Categoría para videojuegos en distintas plataformas");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoProductoUpdateDTO dto = new TipoProductoUpdateDTO();
            dto.setNombre("Videojuego");
            dto.setDescripcion("Categoría para videojuegos en distintas plataformas");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Videojuego"));
            assertTrue(resultado.contains("descripcion=Categoría para videojuegos en distintas plataformas"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}