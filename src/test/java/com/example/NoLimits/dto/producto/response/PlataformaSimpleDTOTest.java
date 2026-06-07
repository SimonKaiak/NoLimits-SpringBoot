package com.example.NoLimits.dto.producto.response;

import com.example.NoLimits.Multimedia.dto.producto.response.PlataformaSimpleDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlataformaSimpleDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            PlataformaSimpleDTO dto = new PlataformaSimpleDTO();

            dto.setId(1L);
            dto.setNombre("Netflix");

            assertEquals(1L, dto.getId());
            assertEquals("Netflix", dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            PlataformaSimpleDTO dto = new PlataformaSimpleDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            PlataformaSimpleDTO dto1 = new PlataformaSimpleDTO();
            dto1.setId(1L);
            dto1.setNombre("Netflix");

            PlataformaSimpleDTO dto2 = new PlataformaSimpleDTO();
            dto2.setId(1L);
            dto2.setNombre("Netflix");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            PlataformaSimpleDTO dto = new PlataformaSimpleDTO();
            dto.setNombre("Netflix");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("Netflix"));
        }
    }
}