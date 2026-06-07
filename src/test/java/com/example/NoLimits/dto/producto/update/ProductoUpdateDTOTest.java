package com.example.NoLimits.dto.producto.update;

import com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ProductoUpdateDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductoUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene todos los campos correctamente")
        void testGettersSetters() {

            ProductoUpdateDTO dto = new ProductoUpdateDTO();

            LinkCompraDTO link = new LinkCompraDTO();
            link.setPlataformaId(1L);
            link.setUrl("https://store.steampowered.com/app/test");

            dto.setNombre("Spider-Man Remaster");
            dto.setPrecio(14990.0);
            dto.setSinopsis("Un joven héroe descubre sus poderes.");
            dto.setUrlTrailer("https://youtube.com/test");
            dto.setAnio(2011);

            dto.setTipoProductoId(2L);
            dto.setClasificacionId(3L);
            dto.setEstadoId(1L);

            dto.setTipoEmpresaId(10L);
            dto.setTipoDesarrolladorId(20L);

            dto.setSaga("Marvel");
            dto.setPortadaSaga("sagas/marvel.webp");

            dto.setPlataformasIds(List.of(1L, 2L));
            dto.setGenerosIds(List.of(3L, 4L));
            dto.setEmpresasIds(List.of(5L));
            dto.setDesarrolladoresIds(List.of(6L, 7L));

            dto.setImagenesRutas(List.of("img1.webp", "img2.webp"));
            dto.setLinksCompra(List.of(link));

            assertEquals("Spider-Man Remaster", dto.getNombre());
            assertEquals(14990.0, dto.getPrecio());
            assertEquals("Un joven héroe descubre sus poderes.", dto.getSinopsis());
            assertEquals("https://youtube.com/test", dto.getUrlTrailer());
            assertEquals(2011, dto.getAnio());

            assertEquals(2L, dto.getTipoProductoId());
            assertEquals(3L, dto.getClasificacionId());
            assertEquals(1L, dto.getEstadoId());

            assertEquals(10L, dto.getTipoEmpresaId());
            assertEquals(20L, dto.getTipoDesarrolladorId());

            assertEquals("Marvel", dto.getSaga());
            assertEquals("sagas/marvel.webp", dto.getPortadaSaga());

            assertEquals(List.of(1L, 2L), dto.getPlataformasIds());
            assertEquals(List.of(3L, 4L), dto.getGenerosIds());
            assertEquals(List.of(5L), dto.getEmpresasIds());
            assertEquals(List.of(6L, 7L), dto.getDesarrolladoresIds());

            assertEquals(List.of("img1.webp", "img2.webp"), dto.getImagenesRutas());

            assertEquals(1, dto.getLinksCompra().size());
            assertEquals(1L, dto.getLinksCompra().get(0).getPlataformaId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con todos los campos nulos")
        void testValoresPorDefecto() {

            ProductoUpdateDTO dto = new ProductoUpdateDTO();

            assertNull(dto.getNombre());
            assertNull(dto.getPrecio());
            assertNull(dto.getSinopsis());
            assertNull(dto.getUrlTrailer());
            assertNull(dto.getAnio());

            assertNull(dto.getTipoProductoId());
            assertNull(dto.getClasificacionId());
            assertNull(dto.getEstadoId());

            assertNull(dto.getTipoEmpresaId());
            assertNull(dto.getTipoDesarrolladorId());

            assertNull(dto.getSaga());
            assertNull(dto.getPortadaSaga());

            assertNull(dto.getPlataformasIds());
            assertNull(dto.getGenerosIds());
            assertNull(dto.getEmpresasIds());
            assertNull(dto.getDesarrolladoresIds());

            assertNull(dto.getImagenesRutas());
            assertNull(dto.getLinksCompra());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            ProductoUpdateDTO dto1 = new ProductoUpdateDTO();
            dto1.setNombre("Spider-Man");
            dto1.setPrecio(14990.0);

            ProductoUpdateDTO dto2 = new ProductoUpdateDTO();
            dto2.setNombre("Spider-Man");
            dto2.setPrecio(14990.0);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            ProductoUpdateDTO dto = new ProductoUpdateDTO();
            dto.setNombre("Spider-Man");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("Spider-Man"));
        }
    }
}