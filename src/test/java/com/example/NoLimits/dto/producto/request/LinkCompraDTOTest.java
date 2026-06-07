package com.example.NoLimits.dto.producto.request;

import com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkCompraDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            LinkCompraDTO dto = new LinkCompraDTO();

            dto.setPlataformaId(1L);
            dto.setUrl("https://store.steampowered.com/app/test");
            dto.setLabel("Steam");
            dto.setAppId("12345");
            dto.setPrecioActual(19990.0);

            assertEquals(1L, dto.getPlataformaId());
            assertEquals("https://store.steampowered.com/app/test", dto.getUrl());
            assertEquals("Steam", dto.getLabel());
            assertEquals("12345", dto.getAppId());
            assertEquals(19990.0, dto.getPrecioActual());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            LinkCompraDTO dto = new LinkCompraDTO();

            assertNull(dto.getPlataformaId());
            assertNull(dto.getUrl());
            assertNull(dto.getLabel());
            assertNull(dto.getAppId());
            assertNull(dto.getPrecioActual());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            LinkCompraDTO dto1 = new LinkCompraDTO();
            dto1.setPlataformaId(1L);
            dto1.setUrl("https://store.steampowered.com");

            LinkCompraDTO dto2 = new LinkCompraDTO();
            dto2.setPlataformaId(1L);
            dto2.setUrl("https://store.steampowered.com");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            LinkCompraDTO dto = new LinkCompraDTO();
            dto.setLabel("Steam");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("Steam"));
        }
    }
}