package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.DesarrolladoresUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DesarrolladoresUpdateDTO")
class DesarrolladoresUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            DesarrolladoresUpdateDTO dto = new DesarrolladoresUpdateDTO();

            dto.setProductoId(10L);
            dto.setDesarrolladorId(3L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(3L, dto.getDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            DesarrolladoresUpdateDTO dto = new DesarrolladoresUpdateDTO();

            assertNull(dto.getProductoId());
            assertNull(dto.getDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            DesarrolladoresUpdateDTO dto1 = new DesarrolladoresUpdateDTO();
            dto1.setProductoId(10L);
            dto1.setDesarrolladorId(3L);

            DesarrolladoresUpdateDTO dto2 = new DesarrolladoresUpdateDTO();
            dto2.setProductoId(10L);
            dto2.setDesarrolladorId(3L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            DesarrolladoresUpdateDTO dto = new DesarrolladoresUpdateDTO();
            dto.setProductoId(10L);
            dto.setDesarrolladorId(3L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("desarrolladorId=3"));
        }
    }
}