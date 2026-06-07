package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoEnvioUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoEnvioUpdateDTO")
class MetodoEnvioUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();

            dto.setNombre("Despacho a domicilio");
            dto.setDescripcion("Entrega directa a la dirección del cliente");
            dto.setActivo(true);

            assertEquals("Despacho a domicilio", dto.getNombre());
            assertEquals("Entrega directa a la dirección del cliente", dto.getDescripcion());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();

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
            MetodoEnvioUpdateDTO dto1 = new MetodoEnvioUpdateDTO();
            dto1.setNombre("Despacho a domicilio");
            dto1.setDescripcion("Entrega directa a la dirección del cliente");
            dto1.setActivo(true);

            MetodoEnvioUpdateDTO dto2 = new MetodoEnvioUpdateDTO();
            dto2.setNombre("Despacho a domicilio");
            dto2.setDescripcion("Entrega directa a la dirección del cliente");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();
            dto.setNombre("Despacho a domicilio");
            dto.setDescripcion("Entrega directa a la dirección del cliente");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Despacho a domicilio"));
            assertTrue(resultado.contains("descripcion=Entrega directa a la dirección del cliente"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}