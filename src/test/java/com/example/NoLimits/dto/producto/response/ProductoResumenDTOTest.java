package com.example.NoLimits.dto.producto.response;

import com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.PlataformaSimpleDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoResponseDTO - Cobertura de ramas equals/hashCode")
class ProductoResponseDTOBranchTest {

    private LinkCompraDTO link(Long plataformaId, String url) {
        LinkCompraDTO l = new LinkCompraDTO();
        l.setPlataformaId(plataformaId);
        l.setUrl(url);
        return l;
    }

    private PlataformaSimpleDTO plataforma(Long id, String nombre) {
        PlataformaSimpleDTO p = new PlataformaSimpleDTO();
        p.setId(id);
        p.setNombre(nombre);
        return p;
    }

    private ProductoResponseDTO full() {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(10L);
        dto.setNombre("Spider-Man (2002)");
        dto.setPrecio(12990.0);
        dto.setSinopsis("Un joven héroe descubre sus poderes.");
        dto.setUrlTrailer("https://www.youtube.com/watch?v=test");
        dto.setAnio(2011);
        dto.setTipoProductoId(2L);
        dto.setTipoProductoNombre("PELÍCULA");
        dto.setClasificacionId(3L);
        dto.setClasificacionNombre("TE+7");
        dto.setEstadoId(1L);
        dto.setEstadoNombre("Activo");
        dto.setSaga("Spiderman");
        dto.setPortadaSaga("/assets/img/sagas/spidermanSaga.webp");
        dto.setPlataformas(List.of(plataforma(1L, "Netflix")));
        dto.setGeneros(List.of("Acción"));
        dto.setEmpresas(List.of("Sony Pictures"));
        dto.setDesarrolladores(List.of("Sony Imageworks"));
        dto.setImagenes(List.of("/assets/img/peliculas/spiderman1.webp"));
        dto.setLinksCompra(List.of(link(1L, "https://store.steampowered.com/app/test")));
        dto.setTipoEmpresaId(1L);
        dto.setTipoEmpresaNombre("Publisher");
        dto.setTipoDesarrolladorId(2L);
        dto.setTipoDesarrolladorNombre("Estudio");
        return dto;
    }

    /**
     * Instancia con todos los campos distintos a full(), del mismo tipo.
     */
    private ProductoResponseDTO fullAlternativo() {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(20L);
        dto.setNombre("Mario Kart 8");
        dto.setPrecio(29990.0);
        dto.setSinopsis("Carreras de karts.");
        dto.setUrlTrailer("https://www.youtube.com/watch?v=otro");
        dto.setAnio(2017);
        dto.setTipoProductoId(99L);
        dto.setTipoProductoNombre("VIDEOJUEGO");
        dto.setClasificacionId(98L);
        dto.setClasificacionNombre("E");
        dto.setEstadoId(97L);
        dto.setEstadoNombre("Inactivo");
        dto.setSaga("Mario");
        dto.setPortadaSaga("/assets/img/sagas/marioSaga.webp");
        dto.setPlataformas(List.of(plataforma(2L, "Switch")));
        dto.setGeneros(List.of("Carreras"));
        dto.setEmpresas(List.of("Nintendo"));
        dto.setDesarrolladores(List.of("Nintendo EPD"));
        dto.setImagenes(List.of("/assets/img/videojuegos/mariokart.webp"));
        dto.setLinksCompra(List.of(link(2L, "https://www.nintendo.com/otro")));
        dto.setTipoEmpresaId(100L);
        dto.setTipoEmpresaNombre("Desarrolladora");
        dto.setTipoDesarrolladorId(200L);
        dto.setTipoDesarrolladorNombre("Interno");
        return dto;
    }

    @Nested
    @DisplayName("equals - casos generales")
    class EqualsGenerales {

        @Test
        @DisplayName("es igual a sí mismo")
        void igualASiMismo() {
            ProductoResponseDTO dto = full();
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("no es igual a null")
        void noEsIgualANull() {
            ProductoResponseDTO dto = full();
            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("no es igual a un objeto de otra clase")
        void noEsIgualAOtraClase() {
            ProductoResponseDTO dto = full();
            assertNotEquals(dto, "no soy un ProductoResponseDTO");
        }

        @Test
        @DisplayName("dos instancias completas con los mismos valores son iguales")
        void instanciasCompletasIgualesSonIguales() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = full();
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("dos instancias vacías son iguales")
        void instanciasVaciasSonIguales() {
            ProductoResponseDTO dto1 = new ProductoResponseDTO();
            ProductoResponseDTO dto2 = new ProductoResponseDTO();
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
    }

    @Nested
    @DisplayName("equals - diferencias entre instancias")
    class EqualsDiferencias {

        @Test
        @DisplayName("dos instancias completas con todos los campos distintos no son iguales")
        void instanciasCompletamenteDistintasNoSonIguales() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = fullAlternativo();

            assertNotEquals(dto1, dto2);
            assertNotEquals(dto2, dto1);
        }

        @Test
        @DisplayName("una instancia completa no es igual a una vacía (todos los campos null vs no-null)")
        void instanciaCompletaVsVaciaNoSonIguales() {
            ProductoResponseDTO completo = full();
            ProductoResponseDTO vacio = new ProductoResponseDTO();

            assertNotEquals(completo, vacio);
            assertNotEquals(vacio, completo);
        }

        @Test
        @DisplayName("difiere solo en id")
        void difiereSoloEnId() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = full();
            dto2.setId(999L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("difiere solo en id null vs no null")
        void difiereSoloEnIdNull() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = full();
            dto2.setId(null);

            assertNotEquals(dto1, dto2);
            assertNotEquals(dto2, dto1);
        }

        @Test
        @DisplayName("difiere solo en nombre")
        void difiereSoloEnNombre() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = full();
            dto2.setNombre("Otro nombre");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("difiere solo en precio")
        void difiereSoloEnPrecio() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = full();
            dto2.setPrecio(1.0);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("difiere solo en anio")
        void difiereSoloEnAnio() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = full();
            dto2.setAnio(1999);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("difiere solo en plataformas")
        void difiereSoloEnPlataformas() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = full();
            dto2.setPlataformas(List.of(plataforma(99L, "Xbox")));

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("difiere solo en linksCompra")
        void difiereSoloEnLinksCompra() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = full();
            dto2.setLinksCompra(List.of(link(99L, "https://otro.com")));

            assertNotEquals(dto1, dto2);
        }
    }

    @Nested
    @DisplayName("hashCode")
    class HashCodeTest {

        @Test
        @DisplayName("hashCode es consistente entre llamadas")
        void hashCodeConsistente() {
            ProductoResponseDTO dto = full();
            assertEquals(dto.hashCode(), dto.hashCode());
        }

        @Test
        @DisplayName("hashCode difiere cuando los valores difieren")
        void hashCodeDifiereConValoresDistintos() {
            ProductoResponseDTO dto1 = full();
            ProductoResponseDTO dto2 = fullAlternativo();
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode de instancia vacía no lanza excepción")
        void hashCodeDeInstanciaVacia() {
            ProductoResponseDTO dto = new ProductoResponseDTO();
            assertDoesNotThrow(dto::hashCode);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringTest {

        @Test
        @DisplayName("toString de instancia completa contiene los valores de los campos")
        void toStringContieneValores() {
            ProductoResponseDTO dto = full();
            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("nombre"));
            assertTrue(resultado.contains("Spider-Man"));
        }

        @Test
        @DisplayName("toString de instancia vacía no lanza excepción")
        void toStringDeInstanciaVacia() {
            ProductoResponseDTO dto = new ProductoResponseDTO();
            assertDoesNotThrow(dto::toString);
        }
    }
}