package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladoresRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DesarrolladoresRequestDTO")
class DesarrolladoresRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            DesarrolladoresRequestDTO dto = new DesarrolladoresRequestDTO();

            dto.setProductoId(10L);
            dto.setDesarrolladorId(1L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(1L, dto.getDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            DesarrolladoresRequestDTO dto = new DesarrolladoresRequestDTO();

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
            DesarrolladoresRequestDTO dto1 = new DesarrolladoresRequestDTO();
            dto1.setProductoId(10L);
            dto1.setDesarrolladorId(1L);

            DesarrolladoresRequestDTO dto2 = new DesarrolladoresRequestDTO();
            dto2.setProductoId(10L);
            dto2.setDesarrolladorId(1L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            DesarrolladoresRequestDTO dto = new DesarrolladoresRequestDTO();
            dto.setProductoId(10L);
            dto.setDesarrolladorId(1L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("desarrolladorId=1"));
        }
    }
}