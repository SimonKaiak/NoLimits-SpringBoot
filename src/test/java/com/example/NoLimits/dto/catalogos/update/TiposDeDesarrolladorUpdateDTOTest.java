package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposDeDesarrolladorUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposDeDesarrolladorUpdateDTO")
class TiposDeDesarrolladorUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();

            dto.setDesarrolladorId(3L);
            dto.setTipoDeDesarrolladorId(2L);

            assertEquals(3L, dto.getDesarrolladorId());
            assertEquals(2L, dto.getTipoDeDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();

            assertNull(dto.getDesarrolladorId());
            assertNull(dto.getTipoDeDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TiposDeDesarrolladorUpdateDTO dto1 = new TiposDeDesarrolladorUpdateDTO();
            dto1.setDesarrolladorId(3L);
            dto1.setTipoDeDesarrolladorId(2L);

            TiposDeDesarrolladorUpdateDTO dto2 = new TiposDeDesarrolladorUpdateDTO();
            dto2.setDesarrolladorId(3L);
            dto2.setTipoDeDesarrolladorId(2L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposDeDesarrolladorUpdateDTO dto = new TiposDeDesarrolladorUpdateDTO();
            dto.setDesarrolladorId(3L);
            dto.setTipoDeDesarrolladorId(2L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("desarrolladorId=3"));
            assertTrue(resultado.contains("tipoDeDesarrolladorId=2"));
        }
    }
}