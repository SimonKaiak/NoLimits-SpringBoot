package com.example.NoLimits.dto.catalogos.request;

import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoEnvioRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoEnvioRequestDTO")
class MetodoEnvioRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();

            dto.setNombre("Retiro en tienda");
            dto.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto.setActivo(true);

            assertEquals("Retiro en tienda", dto.getNombre());
            assertEquals("Retiro presencial en sucursal Plaza Oeste", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();

            assertNull(dto.getNombre());
            assertNull(dto.getDescripcion());
            assertNull(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("Debe validar equals y hashCode correctamente")
        void debeValidarEqualsYHashCode() {
            MetodoEnvioRequestDTO dto1 = new MetodoEnvioRequestDTO();
            dto1.setNombre("Retiro en tienda");
            dto1.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto1.setActivo(true);

            MetodoEnvioRequestDTO dto2 = new MetodoEnvioRequestDTO();
            dto2.setNombre("Retiro en tienda");
            dto2.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();
            dto.setNombre("Retiro en tienda");
            dto.setDescripcion("Retiro presencial en sucursal Plaza Oeste");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Retiro en tienda"));
            assertTrue(resultado.contains("descripcion=Retiro presencial en sucursal Plaza Oeste"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}