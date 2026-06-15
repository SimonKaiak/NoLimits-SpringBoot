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
    @DisplayName("Colecciones")
    class Colecciones {

        @Test
        @DisplayName("Permite listas vacías")
        void listasVacias() {

            // Arrange
            ProductoRequestDTO dto = new ProductoRequestDTO();

            // Act
            dto.setPlataformasIds(List.of());
            dto.setGenerosIds(List.of());
            dto.setEmpresasIds(List.of());
            dto.setDesarrolladoresIds(List.of());
            dto.setImagenesRutas(List.of());
            dto.setLinksCompra(List.of());

            // Assert
            assertTrue(dto.getPlataformasIds().isEmpty());
            assertTrue(dto.getGenerosIds().isEmpty());
            assertTrue(dto.getEmpresasIds().isEmpty());
            assertTrue(dto.getDesarrolladoresIds().isEmpty());
            assertTrue(dto.getImagenesRutas().isEmpty());
            assertTrue(dto.getLinksCompra().isEmpty());
        }

        @Test
        @DisplayName("Mantiene múltiples plataformas")
        void multiplesPlataformas() {

            // Arrange
            ProductoRequestDTO dto = new ProductoRequestDTO();

            // Act
            dto.setPlataformasIds(List.of(1L, 2L, 3L));

            // Assert
            assertEquals(3, dto.getPlataformasIds().size());
        }

        @Test
        @DisplayName("Mantiene múltiples géneros")
        void multiplesGeneros() {

            // Arrange
            ProductoRequestDTO dto = new ProductoRequestDTO();

            // Act
            dto.setGenerosIds(List.of(10L, 20L, 30L));

            // Assert
            assertEquals(3, dto.getGenerosIds().size());
        }

        @Test
        @DisplayName("Mantiene múltiples imágenes")
        void multiplesImagenes() {

            // Arrange
            ProductoRequestDTO dto = new ProductoRequestDTO();

            // Act
            dto.setImagenesRutas(
                    List.of(
                            "img1.webp",
                            "img2.webp",
                            "img3.webp"
                    )
            );

            // Assert
            assertEquals(3, dto.getImagenesRutas().size());
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
        @DisplayName("Equals retorna true para misma instancia")
        void equalsMismaInstancia() {

            // Arrange
            ProductoRequestDTO dto = new ProductoRequestDTO();

            // Act & Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("Equals retorna false con null")
        void equalsConNull() {

            // Arrange
            ProductoRequestDTO dto = new ProductoRequestDTO();

            // Act & Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Equals retorna false con otra clase")
        void equalsConOtraClase() {

            // Arrange
            ProductoRequestDTO dto = new ProductoRequestDTO();

            // Act & Assert
            assertNotEquals("texto", dto);
        }

        @Test
        @DisplayName("HashCode objeto vacío")
        void hashCodeObjetoVacio() {

            // Arrange
            ProductoRequestDTO dto = new ProductoRequestDTO();

            // Act
            int hash = dto.hashCode();

            // Assert
            assertNotEquals(0, hash);
        }

        @Test
        @DisplayName("ToString objeto vacío")
        void toStringObjetoVacio() {

            // Arrange
            ProductoRequestDTO dto = new ProductoRequestDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
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

    @Nested
    @DisplayName("Igualdad avanzada")
    class IgualdadAvanzada {

        @Test
        @DisplayName("Objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            // Arrange
            ProductoRequestDTO dto1 = new ProductoRequestDTO();
            ProductoRequestDTO dto2 = new ProductoRequestDTO();

            // Act & Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Objetos vacíos generan mismo hash")
        void objetosVaciosMismoHash() {

            // Arrange
            ProductoRequestDTO dto1 = new ProductoRequestDTO();
            ProductoRequestDTO dto2 = new ProductoRequestDTO();

            // Act & Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Equals detecta diferencia en nombre")
        void equalsNombreDistinto() {

            // Arrange
            ProductoRequestDTO dto1 = new ProductoRequestDTO();
            dto1.setNombre("Spider-Man");

            ProductoRequestDTO dto2 = new ProductoRequestDTO();
            dto2.setNombre("Batman");

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Equals detecta diferencia en precio")
        void equalsPrecioDistinto() {

            // Arrange
            ProductoRequestDTO dto1 = new ProductoRequestDTO();
            dto1.setPrecio(1000.0);

            ProductoRequestDTO dto2 = new ProductoRequestDTO();
            dto2.setPrecio(2000.0);

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Equals detecta diferencia en saga")
        void equalsSagaDistinta() {

            // Arrange
            ProductoRequestDTO dto1 = new ProductoRequestDTO();
            dto1.setSaga("Marvel");

            ProductoRequestDTO dto2 = new ProductoRequestDTO();
            dto2.setSaga("DC");

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Equals detecta diferencia en listas")
        void equalsListaDiferente() {

            // Arrange
            ProductoRequestDTO dto1 = new ProductoRequestDTO();
            dto1.setPlataformasIds(List.of(1L));

            ProductoRequestDTO dto2 = new ProductoRequestDTO();
            dto2.setPlataformasIds(List.of(2L));

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Equals detecta diferencia en links")
        void equalsLinksDiferentes() {

            // Arrange
            LinkCompraDTO link1 = new LinkCompraDTO();
            link1.setPlataformaId(1L);

            LinkCompraDTO link2 = new LinkCompraDTO();
            link2.setPlataformaId(2L);

            ProductoRequestDTO dto1 = new ProductoRequestDTO();
            dto1.setLinksCompra(List.of(link1));

            ProductoRequestDTO dto2 = new ProductoRequestDTO();
            dto2.setLinksCompra(List.of(link2));

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }
    }

}