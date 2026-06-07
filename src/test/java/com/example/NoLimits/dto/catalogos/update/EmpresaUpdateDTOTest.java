package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresaUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EmpresaUpdateDTO")
class EmpresaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();

            dto.setNombre("Warner Bros Games");
            dto.setActivo(true);

            assertEquals("Warner Bros Games", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();

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
            EmpresaUpdateDTO dto1 = new EmpresaUpdateDTO();
            dto1.setNombre("Warner Bros Games");
            dto1.setActivo(true);

            EmpresaUpdateDTO dto2 = new EmpresaUpdateDTO();
            dto2.setNombre("Warner Bros Games");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EmpresaUpdateDTO dto = new EmpresaUpdateDTO();
            dto.setNombre("Warner Bros Games");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Warner Bros Games"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}