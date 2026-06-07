package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposEmpresaResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TiposEmpresaResponseDTO")
class TiposEmpresaResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TiposEmpresaResponseDTO dto = new TiposEmpresaResponseDTO();

            dto.setId(10L);
            dto.setEmpresaId(5L);
            dto.setTipoEmpresaId(2L);
            dto.setTipoEmpresaNombre("Publisher");

            assertEquals(10L, dto.getId());
            assertEquals(5L, dto.getEmpresaId());
            assertEquals(2L, dto.getTipoEmpresaId());
            assertEquals("Publisher", dto.getTipoEmpresaNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TiposEmpresaResponseDTO dto = new TiposEmpresaResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getEmpresaId());
            assertNull(dto.getTipoEmpresaId());
            assertNull(dto.getTipoEmpresaNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TiposEmpresaResponseDTO dto1 = new TiposEmpresaResponseDTO();
            dto1.setId(10L);
            dto1.setEmpresaId(5L);
            dto1.setTipoEmpresaId(2L);
            dto1.setTipoEmpresaNombre("Publisher");

            TiposEmpresaResponseDTO dto2 = new TiposEmpresaResponseDTO();
            dto2.setId(10L);
            dto2.setEmpresaId(5L);
            dto2.setTipoEmpresaId(2L);
            dto2.setTipoEmpresaNombre("Publisher");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TiposEmpresaResponseDTO dto = new TiposEmpresaResponseDTO();
            dto.setId(10L);
            dto.setEmpresaId(5L);
            dto.setTipoEmpresaId(2L);
            dto.setTipoEmpresaNombre("Publisher");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=10"));
            assertTrue(resultado.contains("empresaId=5"));
            assertTrue(resultado.contains("tipoEmpresaId=2"));
            assertTrue(resultado.contains("tipoEmpresaNombre=Publisher"));
        }
    }
}