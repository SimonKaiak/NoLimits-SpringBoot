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
        @DisplayName("equals retorna true para misma instancia")
        void equalsMismaInstancia() {
            LinkCompraDTO dto = new LinkCompraDTO();
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false con null")
        void equalsConNull() {
            assertNotEquals(null, new LinkCompraDTO());
        }

        @Test
        @DisplayName("equals retorna false con otra clase")
        void equalsConOtraClase() {
            assertNotEquals("texto", new LinkCompraDTO());
        }

        @Test
        @DisplayName("objetos vacíos son iguales entre sí")
        void objetosVaciosSonIguales() {
            assertEquals(new LinkCompraDTO(), new LinkCompraDTO());
            assertEquals(new LinkCompraDTO().hashCode(), new LinkCompraDTO().hashCode());
        }

        @Test
        @DisplayName("equals detecta diferencia en plataformaId")
        void equalsPlataformaIdDistinto() {
            LinkCompraDTO dto1 = new LinkCompraDTO(); dto1.setPlataformaId(1L);
            LinkCompraDTO dto2 = new LinkCompraDTO(); dto2.setPlataformaId(2L);
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detecta diferencia en url")
        void equalsUrlDistinta() {
            LinkCompraDTO dto1 = new LinkCompraDTO(); dto1.setUrl("https://steam.com");
            LinkCompraDTO dto2 = new LinkCompraDTO(); dto2.setUrl("https://epic.com");
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detecta diferencia en label")
        void equalsLabelDistinto() {
            LinkCompraDTO dto1 = new LinkCompraDTO(); dto1.setLabel("Steam");
            LinkCompraDTO dto2 = new LinkCompraDTO(); dto2.setLabel("Epic");
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detecta diferencia en appId")
        void equalsAppIdDistinto() {
            LinkCompraDTO dto1 = new LinkCompraDTO(); dto1.setAppId("111");
            LinkCompraDTO dto2 = new LinkCompraDTO(); dto2.setAppId("222");
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detecta diferencia en precioActual")
        void equalsPrecioDistinto() {
            LinkCompraDTO dto1 = new LinkCompraDTO(); dto1.setPrecioActual(1000.0);
            LinkCompraDTO dto2 = new LinkCompraDTO(); dto2.setPrecioActual(2000.0);
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null vs valor — plataformaId")
        void nullVsValorPlataformaId() {
            LinkCompraDTO dto1 = new LinkCompraDTO();
            LinkCompraDTO dto2 = new LinkCompraDTO(); dto2.setPlataformaId(1L);
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null vs valor — url")
        void nullVsValorUrl() {
            LinkCompraDTO dto1 = new LinkCompraDTO();
            LinkCompraDTO dto2 = new LinkCompraDTO(); dto2.setUrl("https://steam.com");
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null vs valor — label")
        void nullVsValorLabel() {
            LinkCompraDTO dto1 = new LinkCompraDTO();
            LinkCompraDTO dto2 = new LinkCompraDTO(); dto2.setLabel("Steam");
            assertNotEquals(dto1, dto2);
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

        @Test
        @DisplayName("toString objeto vacío no lanza excepción")
        void toStringVacio() {
            assertNotNull(new LinkCompraDTO().toString());
        }

        @Test
        @DisplayName("null vs valor — appId")
        void nullVsValorAppId() {

            LinkCompraDTO dto1 = new LinkCompraDTO();

            LinkCompraDTO dto2 = new LinkCompraDTO();
            dto2.setAppId("12345");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null vs valor — precioActual")
        void nullVsValorPrecioActual() {

            LinkCompraDTO dto1 = new LinkCompraDTO();

            LinkCompraDTO dto2 = new LinkCompraDTO();
            dto2.setPrecioActual(19990.0);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("valor vs null — plataformaId")
        void valorVsNullPlataformaId() {

            LinkCompraDTO dto1 = new LinkCompraDTO();
            dto1.setPlataformaId(1L);

            LinkCompraDTO dto2 = new LinkCompraDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("valor vs null — url")
        void valorVsNullUrl() {

            LinkCompraDTO dto1 = new LinkCompraDTO();
            dto1.setUrl("https://steam.com");

            LinkCompraDTO dto2 = new LinkCompraDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("valor vs null — label")
        void valorVsNullLabel() {

            LinkCompraDTO dto1 = new LinkCompraDTO();
            dto1.setLabel("Steam");

            LinkCompraDTO dto2 = new LinkCompraDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("valor vs null — appId")
        void valorVsNullAppId() {

            LinkCompraDTO dto1 = new LinkCompraDTO();
            dto1.setAppId("12345");

            LinkCompraDTO dto2 = new LinkCompraDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("valor vs null — precioActual")
        void valorVsNullPrecioActual() {

            LinkCompraDTO dto1 = new LinkCompraDTO();
            dto1.setPrecioActual(19990.0);

            LinkCompraDTO dto2 = new LinkCompraDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("todos los campos iguales")
        void todosLosCamposIguales() {

            LinkCompraDTO dto1 = new LinkCompraDTO();
            dto1.setPlataformaId(1L);
            dto1.setUrl("https://steam.com");
            dto1.setLabel("Steam");
            dto1.setAppId("12345");
            dto1.setPrecioActual(19990.0);

            LinkCompraDTO dto2 = new LinkCompraDTO();
            dto2.setPlataformaId(1L);
            dto2.setUrl("https://steam.com");
            dto2.setLabel("Steam");
            dto2.setAppId("12345");
            dto2.setPrecioActual(19990.0);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
}