package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TiposEmpresaRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposEmpresaRequestDTO")
class TiposEmpresaRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposEmpresaRequestDTO dto = new TiposEmpresaRequestDTO();

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
            TiposEmpresaRequestDTO dto = new TiposEmpresaRequestDTO();

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
            TiposEmpresaRequestDTO dto1 = new TiposEmpresaRequestDTO();
            dto1.setEmpresaId(5L);
            dto1.setTipoEmpresaId(2L);

            TiposEmpresaRequestDTO dto2 = new TiposEmpresaRequestDTO();
            dto2.setEmpresaId(5L);
            dto2.setTipoEmpresaId(2L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposEmpresaRequestDTO dto = new TiposEmpresaRequestDTO();
            dto.setEmpresaId(5L);
            dto.setTipoEmpresaId(2L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("empresaId=5"));
            assertTrue(resultado.contains("tipoEmpresaId=2"));
        }
    }
}