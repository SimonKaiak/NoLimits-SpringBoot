package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladorResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DesarrolladorResponseDTO")
class DesarrolladorResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            DesarrolladorResponseDTO dto = new DesarrolladorResponseDTO();

            dto.setId(1L);
            dto.setNombre("Insomniac Games");
            dto.setActivo(true);

            assertEquals(1L, dto.getId());
            assertEquals("Insomniac Games", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            DesarrolladorResponseDTO dto = new DesarrolladorResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
            assertNull(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            DesarrolladorResponseDTO dto1 = new DesarrolladorResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Insomniac Games");
            dto1.setActivo(true);

            DesarrolladorResponseDTO dto2 = new DesarrolladorResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Insomniac Games");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            DesarrolladorResponseDTO dto = new DesarrolladorResponseDTO();
            dto.setId(1L);
            dto.setNombre("Insomniac Games");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Insomniac Games"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}