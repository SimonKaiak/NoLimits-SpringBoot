package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoDeDesarrolladorUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoDeDesarrolladorUpdateDTO")
class TipoDeDesarrolladorUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            TipoDeDesarrolladorUpdateDTO dto = new TipoDeDesarrolladorUpdateDTO();

            dto.setNombre("Publisher");

            assertEquals("Publisher", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoDeDesarrolladorUpdateDTO dto = new TipoDeDesarrolladorUpdateDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoDeDesarrolladorUpdateDTO dto1 = new TipoDeDesarrolladorUpdateDTO();
            dto1.setNombre("Publisher");

            TipoDeDesarrolladorUpdateDTO dto2 = new TipoDeDesarrolladorUpdateDTO();
            dto2.setNombre("Publisher");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoDeDesarrolladorUpdateDTO dto = new TipoDeDesarrolladorUpdateDTO();
            dto.setNombre("Publisher");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Publisher"));
        }
    }
}