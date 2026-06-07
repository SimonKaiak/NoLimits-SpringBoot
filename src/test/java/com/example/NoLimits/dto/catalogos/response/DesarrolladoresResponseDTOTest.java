package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladoresResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DesarrolladoresResponseDTO")
class DesarrolladoresResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            DesarrolladoresResponseDTO dto = new DesarrolladoresResponseDTO();

            dto.setId(5L);
            dto.setProductoId(10L);
            dto.setDesarrolladorId(1L);
            dto.setDesarrolladorNombre("Insomniac Games");

            assertEquals(5L, dto.getId());
            assertEquals(10L, dto.getProductoId());
            assertEquals(1L, dto.getDesarrolladorId());
            assertEquals("Insomniac Games", dto.getDesarrolladorNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            DesarrolladoresResponseDTO dto = new DesarrolladoresResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getProductoId());
            assertNull(dto.getDesarrolladorId());
            assertNull(dto.getDesarrolladorNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            DesarrolladoresResponseDTO dto1 = new DesarrolladoresResponseDTO();
            dto1.setId(5L);
            dto1.setProductoId(10L);
            dto1.setDesarrolladorId(1L);
            dto1.setDesarrolladorNombre("Insomniac Games");

            DesarrolladoresResponseDTO dto2 = new DesarrolladoresResponseDTO();
            dto2.setId(5L);
            dto2.setProductoId(10L);
            dto2.setDesarrolladorId(1L);
            dto2.setDesarrolladorNombre("Insomniac Games");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            DesarrolladoresResponseDTO dto = new DesarrolladoresResponseDTO();
            dto.setId(5L);
            dto.setProductoId(10L);
            dto.setDesarrolladorId(1L);
            dto.setDesarrolladorNombre("Insomniac Games");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=5"));
            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("desarrolladorId=1"));
            assertTrue(resultado.contains("desarrolladorNombre=Insomniac Games"));
        }
    }
}