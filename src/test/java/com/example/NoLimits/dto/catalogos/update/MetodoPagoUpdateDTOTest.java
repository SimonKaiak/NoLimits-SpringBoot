package com.example.NoLimits.dto.catalogos.update;

import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoPagoUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MetodoPagoUpdateDTO")
class MetodoPagoUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersYSetters {

        @Test
        @DisplayName("Debe asignar y obtener todos los campos correctamente")
        void debeAsignarYObtenerCamposCorrectamente() {
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();

            dto.setNombre("Transferencia Bancaria");
            dto.setActivo(true);

            assertEquals("Transferencia Bancaria", dto.getNombre());
            assertEquals(true, dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("Debe tener valores nulos al crear el DTO")
        void debeTenerValoresNulosPorDefecto() {
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();

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
            MetodoPagoUpdateDTO dto1 = new MetodoPagoUpdateDTO();
            dto1.setNombre("Transferencia Bancaria");
            dto1.setActivo(true);

            MetodoPagoUpdateDTO dto2 = new MetodoPagoUpdateDTO();
            dto2.setNombre("Transferencia Bancaria");
            dto2.setActivo(true);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Debe validar toString correctamente")
        void debeValidarToString() {
            MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();
            dto.setNombre("Transferencia Bancaria");
            dto.setActivo(true);

            String resultado = dto.toString();

            assertTrue(resultado.contains("nombre=Transferencia Bancaria"));
            assertTrue(resultado.contains("activo=true"));
        }
    }
}