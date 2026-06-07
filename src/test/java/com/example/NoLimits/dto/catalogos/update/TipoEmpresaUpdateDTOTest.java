package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoEmpresaUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoEmpresaUpdateDTO")
class TipoEmpresaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            TipoEmpresaUpdateDTO dto = new TipoEmpresaUpdateDTO();

            dto.setNombre("Distribuidora");

            assertEquals("Distribuidora", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoEmpresaUpdateDTO dto = new TipoEmpresaUpdateDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoEmpresaUpdateDTO dto1 = new TipoEmpresaUpdateDTO();
            dto1.setNombre("Distribuidora");

            TipoEmpresaUpdateDTO dto2 = new TipoEmpresaUpdateDTO();
            dto2.setNombre("Distribuidora");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoEmpresaUpdateDTO dto = new TipoEmpresaUpdateDTO();
            dto.setNombre("Distribuidora");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Distribuidora"));
        }
    }
}