package com.example.NoLimits.dto.producto.request;

import com.example.NoLimits.Multimedia.dto.producto.request.ProductoRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductoRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene todos los campos correctamente")
        void testGettersSetters() {

            ProductoRequestDTO dto = new ProductoRequestDTO();

            LinkCompraDTO linkCompra = new LinkCompraDTO();
            linkCompra.setPlataformaId(1L);
            linkCompra.setUrl("https://store.steampowered.com/app/test");

            dto.setNombre("Spider-Man");
            dto.setPrecio(12990.0);
            dto.setSinopsis("Un joven héroe descubre sus poderes.");
            dto.setUrlTrailer("https://www.youtube.com/watch?v=test");
            dto.setAnio(2002);
            dto.setTipoProductoId(2L);
            dto.setClasificacionId(3L);
            dto.setEstadoId(1L);
            dto.setSaga("Marvel");
            dto.setPortadaSaga("sagas/marvel.webp");
            dto.setPlataformasIds(List.of(1L, 2L));
            dto.setGenerosIds(List.of(3L, 4L));
            dto.setEmpresasIds(List.of(5L));
            dto.setDesarrolladoresIds(List.of(7L, 8L));
            dto.setImagenesRutas(List.of("productos/spiderman1.webp", "productos/spiderman2.webp"));
            dto.setLinksCompra(List.of(linkCompra));
            dto.setTipoEmpresaId(10L);
            dto.setTipoDesarrolladorId(20L);

            assertEquals("Spider-Man", dto.getNombre());
            assertEquals(12990.0, dto.getPrecio());
            assertEquals("Un joven héroe descubre sus poderes.", dto.getSinopsis());
            assertEquals("https://www.youtube.com/watch?v=test", dto.getUrlTrailer());
            assertEquals(2002, dto.getAnio());
            assertEquals(2L, dto.getTipoProductoId());
            assertEquals(3L, dto.getClasificacionId());
            assertEquals(1L, dto.getEstadoId());
            assertEquals("Marvel", dto.getSaga());
            assertEquals("sagas/marvel.webp", dto.getPortadaSaga());
            assertEquals(List.of(1L, 2L), dto.getPlataformasIds());
            assertEquals(List.of(3L, 4L), dto.getGenerosIds());
            assertEquals(List.of(5L), dto.getEmpresasIds());
            assertEquals(List.of(7L, 8L), dto.getDesarrolladoresIds());
            assertEquals(List.of("productos/spiderman1.webp", "productos/spiderman2.webp"), dto.getImagenesRutas());
            assertEquals(1, dto.getLinksCompra().size());
            assertEquals(1L, dto.getLinksCompra().get(0).getPlataformaId());
            assertEquals("https://store.steampowered.com/app/test", dto.getLinksCompra().get(0).getUrl());
            assertEquals(10L, dto.getTipoEmpresaId());
            assertEquals(20L, dto.getTipoDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con todos los campos nulos")
        void testValoresPorDefecto() {

            ProductoRequestDTO dto = new ProductoRequestDTO();

            assertNull(dto.getNombre());
            assertNull(dto.getPrecio());
            assertNull(dto.getSinopsis());
            assertNull(dto.getUrlTrailer());
            assertNull(dto.getAnio());
            assertNull(dto.getTipoProductoId());
            assertNull(dto.getClasificacionId());
            assertNull(dto.getEstadoId());
            assertNull(dto.getSaga());
            assertNull(dto.getPortadaSaga());
            assertNull(dto.getPlataformasIds());
            assertNull(dto.getGenerosIds());
            assertNull(dto.getEmpresasIds());
            assertNull(dto.getDesarrolladoresIds());
            assertNull(dto.getImagenesRutas());
            assertNull(dto.getLinksCompra());
            assertNull(dto.getTipoEmpresaId());
            assertNull(dto.getTipoDesarrolladorId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            ProductoRequestDTO dto1 = new ProductoRequestDTO();
            dto1.setNombre("Spider-Man");
            dto1.setPrecio(12990.0);

            ProductoRequestDTO dto2 = new ProductoRequestDTO();
            dto2.setNombre("Spider-Man");
            dto2.setPrecio(12990.0);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            ProductoRequestDTO dto = new ProductoRequestDTO();
            dto.setNombre("Spider-Man");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("Spider-Man"));
        }
    }
}