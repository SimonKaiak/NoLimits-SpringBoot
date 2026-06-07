package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.EmpresasRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EmpresasRequestDTO")
class EmpresasRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            EmpresasRequestDTO dto = new EmpresasRequestDTO();

            dto.setProductoId(10L);
            dto.setEmpresaId(5L);

            assertEquals(10L, dto.getProductoId());
            assertEquals(5L, dto.getEmpresaId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            EmpresasRequestDTO dto = new EmpresasRequestDTO();

            assertNull(dto.getProductoId());
            assertNull(dto.getEmpresaId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            EmpresasRequestDTO dto1 = new EmpresasRequestDTO();
            dto1.setProductoId(10L);
            dto1.setEmpresaId(5L);

            EmpresasRequestDTO dto2 = new EmpresasRequestDTO();
            dto2.setProductoId(10L);
            dto2.setEmpresaId(5L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            EmpresasRequestDTO dto = new EmpresasRequestDTO();
            dto.setProductoId(10L);
            dto.setEmpresaId(5L);

            String resultado = dto.toString();

            assertTrue(resultado.contains("productoId=10"));
            assertTrue(resultado.contains("empresaId=5"));
        }
    }
}