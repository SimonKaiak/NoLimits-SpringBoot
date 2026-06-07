package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoEmpresaRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TipoEmpresaRequestDTO")
class TipoEmpresaRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener el nombre correctamente")
        void debeAsignarYObtenerNombreCorrectamente() {
            TipoEmpresaRequestDTO dto = new TipoEmpresaRequestDTO();

            dto.setNombre("Publisher");

            assertEquals("Publisher", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            TipoEmpresaRequestDTO dto = new TipoEmpresaRequestDTO();

            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            TipoEmpresaRequestDTO dto1 = new TipoEmpresaRequestDTO();
            dto1.setNombre("Publisher");

            TipoEmpresaRequestDTO dto2 = new TipoEmpresaRequestDTO();
            dto2.setNombre("Publisher");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            TipoEmpresaRequestDTO dto = new TipoEmpresaRequestDTO();
            dto.setNombre("Publisher");

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Publisher"));
        }
    }
}