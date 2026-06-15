package com.example.NoLimits.dto.producto.response;

import com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.PlataformaSimpleDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductoResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene todos los campos correctamente")
        void testGettersSetters() {

            ProductoResponseDTO dto = new ProductoResponseDTO();

            PlataformaSimpleDTO plataforma = new PlataformaSimpleDTO();
            plataforma.setId(1L);
            plataforma.setNombre("Steam");

            LinkCompraDTO link = new LinkCompraDTO();
            link.setPlataformaId(1L);
            link.setUrl("https://store.steampowered.com/app/test");

            dto.setId(10L);
            dto.setNombre("Spider-Man");
            dto.setPrecio(12990.0);
            dto.setSinopsis("Sinopsis");
            dto.setUrlTrailer("https://youtube.com/test");
            dto.setAnio(2011);

            dto.setTipoProductoId(2L);
            dto.setTipoProductoNombre("PELÍCULA");

            dto.setClasificacionId(3L);
            dto.setClasificacionNombre("TE+7");

            dto.setEstadoId(1L);
            dto.setEstadoNombre("Activo");

            dto.setSaga("Spider-Man");
            dto.setPortadaSaga("/assets/img/sagas/spidermanSaga.webp");

            dto.setPlataformas(List.of(plataforma));
            dto.setGeneros(List.of("Acción"));
            dto.setEmpresas(List.of("Sony"));
            dto.setDesarrolladores(List.of("Insomniac"));

            dto.setImagenes(List.of("img1.webp", "img2.webp"));
            dto.setLinksCompra(List.of(link));

            dto.setTipoEmpresaId(1L);
            dto.setTipoEmpresaNombre("Publisher");

            dto.setTipoDesarrolladorId(2L);
            dto.setTipoDesarrolladorNombre("Estudio");

            assertEquals(10L, dto.getId());
            assertEquals("Spider-Man", dto.getNombre());
            assertEquals(12990.0, dto.getPrecio());
            assertEquals("Sinopsis", dto.getSinopsis());
            assertEquals("https://youtube.com/test", dto.getUrlTrailer());
            assertEquals(2011, dto.getAnio());

            assertEquals(2L, dto.getTipoProductoId());
            assertEquals("PELÍCULA", dto.getTipoProductoNombre());

            assertEquals(3L, dto.getClasificacionId());
            assertEquals("TE+7", dto.getClasificacionNombre());

            assertEquals(1L, dto.getEstadoId());
            assertEquals("Activo", dto.getEstadoNombre());

            assertEquals("Spider-Man", dto.getSaga());
            assertEquals("/assets/img/sagas/spidermanSaga.webp", dto.getPortadaSaga());

            assertEquals(1, dto.getPlataformas().size());
            assertEquals("Steam", dto.getPlataformas().get(0).getNombre());

            assertEquals(List.of("Acción"), dto.getGeneros());
            assertEquals(List.of("Sony"), dto.getEmpresas());
            assertEquals(List.of("Insomniac"), dto.getDesarrolladores());

            assertEquals(List.of("img1.webp", "img2.webp"), dto.getImagenes());

            assertEquals(1, dto.getLinksCompra().size());

            assertEquals(1L, dto.getTipoEmpresaId());
            assertEquals("Publisher", dto.getTipoEmpresaNombre());

            assertEquals(2L, dto.getTipoDesarrolladorId());
            assertEquals("Estudio", dto.getTipoDesarrolladorNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            ProductoResponseDTO dto = new ProductoResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
            assertNull(dto.getPrecio());
            assertNull(dto.getSinopsis());
            assertNull(dto.getUrlTrailer());
            assertNull(dto.getAnio());

            assertNull(dto.getTipoProductoId());
            assertNull(dto.getTipoProductoNombre());

            assertNull(dto.getClasificacionId());
            assertNull(dto.getClasificacionNombre());

            assertNull(dto.getEstadoId());
            assertNull(dto.getEstadoNombre());

            assertNull(dto.getSaga());
            assertNull(dto.getPortadaSaga());

            assertNull(dto.getPlataformas());
            assertNull(dto.getGeneros());
            assertNull(dto.getEmpresas());
            assertNull(dto.getDesarrolladores());

            assertNull(dto.getImagenes());
            assertNull(dto.getLinksCompra());

            assertNull(dto.getTipoEmpresaId());
            assertNull(dto.getTipoEmpresaNombre());

            assertNull(dto.getTipoDesarrolladorId());
            assertNull(dto.getTipoDesarrolladorNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            ProductoResponseDTO dto1 = new ProductoResponseDTO();
            dto1.setId(10L);
            dto1.setNombre("Spider-Man");

            ProductoResponseDTO dto2 = new ProductoResponseDTO();
            dto2.setId(10L);
            dto2.setNombre("Spider-Man");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Equals retorna true para misma instancia")
        void equalsMismaInstancia() {

            // Arrange
            ProductoResponseDTO dto = new ProductoResponseDTO();

            // Act & Assert
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("Equals retorna false con null")
        void equalsConNull() {

            // Arrange
            ProductoResponseDTO dto = new ProductoResponseDTO();

            // Act & Assert
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("Equals retorna false con otra clase")
        void equalsConOtraClase() {

            // Arrange
            ProductoResponseDTO dto = new ProductoResponseDTO();

            // Act & Assert
            assertNotEquals("texto", dto);
        }

        @Test
        @DisplayName("Equals retorna false cuando cambia id")
        void equalsDistintoId() {

            // Arrange
            ProductoResponseDTO dto1 = new ProductoResponseDTO();
            dto1.setId(1L);

            ProductoResponseDTO dto2 = new ProductoResponseDTO();
            dto2.setId(2L);

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Equals retorna false cuando cambia nombre")
        void equalsDistintoNombre() {

            // Arrange
            ProductoResponseDTO dto1 = new ProductoResponseDTO();
            dto1.setNombre("Spider-Man");

            ProductoResponseDTO dto2 = new ProductoResponseDTO();
            dto2.setNombre("Batman");

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Equals retorna false cuando cambia precio")
        void equalsDistintoPrecio() {

            // Arrange
            ProductoResponseDTO dto1 = new ProductoResponseDTO();
            dto1.setPrecio(1000.0);

            ProductoResponseDTO dto2 = new ProductoResponseDTO();
            dto2.setPrecio(2000.0);

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("HashCode objeto vacío")
        void hashCodeObjetoVacio() {

            // Arrange
            ProductoResponseDTO dto = new ProductoResponseDTO();

            // Act
            int hash = dto.hashCode();

            // Assert
            assertNotEquals(0, hash);
        }

        @Test
        @DisplayName("ToString objeto vacío")
        void toStringObjetoVacio() {

            // Arrange
            ProductoResponseDTO dto = new ProductoResponseDTO();

            // Act
            String resultado = dto.toString();

            // Assert
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            ProductoResponseDTO dto = new ProductoResponseDTO();
            dto.setNombre("Spider-Man");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("Spider-Man"));
        }
    }

    @Nested
    @DisplayName("Colecciones")
    class Colecciones {

        @Test
        @DisplayName("Permite listas vacías")
        void listasVacias() {

            // Arrange
            ProductoResponseDTO dto = new ProductoResponseDTO();

            // Act
            dto.setPlataformas(List.of());
            dto.setGeneros(List.of());
            dto.setEmpresas(List.of());
            dto.setDesarrolladores(List.of());
            dto.setImagenes(List.of());
            dto.setLinksCompra(List.of());

            // Assert
            assertTrue(dto.getPlataformas().isEmpty());
            assertTrue(dto.getGeneros().isEmpty());
            assertTrue(dto.getEmpresas().isEmpty());
            assertTrue(dto.getDesarrolladores().isEmpty());
            assertTrue(dto.getImagenes().isEmpty());
            assertTrue(dto.getLinksCompra().isEmpty());
        }

        @Test
        @DisplayName("Mantiene múltiples elementos")
        void multiplesElementos() {

            // Arrange
            ProductoResponseDTO dto = new ProductoResponseDTO();

            // Act
            dto.setGeneros(List.of("Acción", "Aventura", "Drama"));

            // Assert
            assertEquals(3, dto.getGeneros().size());
            assertTrue(dto.getGeneros().contains("Drama"));
        }

        @Test
        @DisplayName("Mantiene imágenes correctamente")
        void multiplesImagenes() {

            // Arrange
            ProductoResponseDTO dto = new ProductoResponseDTO();

            // Act
            dto.setImagenes(List.of(
                    "img1.webp",
                    "img2.webp",
                    "img3.webp"
            ));

            // Assert
            assertEquals(3, dto.getImagenes().size());
        }
    }

    @Nested
    @DisplayName("Igualdad avanzada")
    class IgualdadAvanzada {

        @Test
        @DisplayName("Objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            // Arrange
            ProductoResponseDTO dto1 = new ProductoResponseDTO();
            ProductoResponseDTO dto2 = new ProductoResponseDTO();

            // Act & Assert
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Objetos vacíos generan mismo hash")
        void objetosVaciosMismoHash() {

            // Arrange
            ProductoResponseDTO dto1 = new ProductoResponseDTO();
            ProductoResponseDTO dto2 = new ProductoResponseDTO();

            // Act & Assert
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Equals detecta diferencia en listas")
        void equalsListaDiferente() {

            // Arrange
            ProductoResponseDTO dto1 = new ProductoResponseDTO();
            dto1.setGeneros(List.of("Acción"));

            ProductoResponseDTO dto2 = new ProductoResponseDTO();
            dto2.setGeneros(List.of("Drama"));

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("Equals detecta diferencia en saga")
        void equalsSagaDiferente() {

            // Arrange
            ProductoResponseDTO dto1 = new ProductoResponseDTO();
            dto1.setSaga("Spider-Man");

            ProductoResponseDTO dto2 = new ProductoResponseDTO();
            dto2.setSaga("Batman");

            // Act & Assert
            assertNotEquals(dto1, dto2);
        }

    }

}