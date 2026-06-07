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
        @DisplayName("genera toString correctamente")
        void testToString() {

            ProductoResponseDTO dto = new ProductoResponseDTO();
            dto.setNombre("Spider-Man");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("Spider-Man"));
        }
    }
}