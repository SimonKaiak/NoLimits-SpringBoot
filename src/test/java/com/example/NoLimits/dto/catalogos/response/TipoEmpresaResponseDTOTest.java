package com.example.NoLimits.dto.catalogos.response;

import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoEmpresaResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoEmpresaResponseDTO")
class TipoEmpresaResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            TipoEmpresaResponseDTO dto = new TipoEmpresaResponseDTO();

            dto.setId(1L);
            dto.setNombre("Publisher");

            assertEquals(1L, dto.getId());
            assertEquals("Publisher", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoEmpresaResponseDTO dto = new TipoEmpresaResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoEmpresaResponseDTO dto1 = new TipoEmpresaResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("Publisher");

            TipoEmpresaResponseDTO dto2 = new TipoEmpresaResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("Publisher");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoEmpresaResponseDTO dto = new TipoEmpresaResponseDTO();
            dto.setId(1L);
            dto.setNombre("Publisher");

            String resultado = dto.toString();

            assertTrue(resultado.contains("id=1"));
            assertTrue(resultado.contains("nombre=Publisher"));
        }
    }
}