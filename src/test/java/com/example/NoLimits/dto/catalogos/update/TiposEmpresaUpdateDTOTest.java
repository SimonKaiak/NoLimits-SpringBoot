package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposEmpresaUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposEmpresaUpdateDTO")
class TiposEmpresaUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposEmpresaUpdateDTO dto = new TiposEmpresaUpdateDTO();

            dto.setEmpresaId(5L);
            dto.setTipoEmpresaId(2L);

            assertEquals(5L, dto.getEmpresaId());
            assertEquals(2L, dto.getTipoEmpresaId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TiposEmpresaUpdateDTO dto = new TiposEmpresaUpdateDTO();

            assertNull(dto.getEmpresaId());
            assertNull(dto.getTipoEmpresaId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TiposEmpresaUpdateDTO dto1 = new TiposEmpresaUpdateDTO();
            dto1.setEmpresaId(5L);
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaUpdateDTO dto2 = new TiposEmpresaUpdateDTO();
            dto2.setEmpresaId(5L);
            dto2.setTipoEmpresaId(2L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposEmpresaUpdateDTO dto = new TiposEmpresaUpdateDTO();
            dto.setEmpresaId(5L);
            dto.setTipoEmpresaId(2L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("empresaId=5"));
            assertTrue(resultado.contains("tipoEmpresaId=2"));
        }
    }
}