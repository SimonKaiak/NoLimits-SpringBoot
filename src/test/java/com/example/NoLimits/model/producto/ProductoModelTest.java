package com.example.NoLimits.model.producto;

import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoModel Tests")
class ProductoModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear producto correctamente")
        void debeCrearProductoCorrectamente() {
            ProductoModel producto = new ProductoModel();
            producto.setId(1L);
            producto.setNombre("Spider-Man");
            producto.setPrecio(10000.0);
            producto.setSinopsis("Película");
            producto.setAnio(2002);

            assertEquals(1L, producto.getId());
            assertEquals("Spider-Man", producto.getNombre());
            assertEquals(10000.0, producto.getPrecio());
            assertEquals("Película", producto.getSinopsis());
            assertEquals(2002, producto.getAnio());
        }

        @Test
        @DisplayName("constructor completo asigna valores correctamente")
        void constructorCompletoLombok() {

            ProductoModel producto = new ProductoModel(
                    1L,
                    "Spider-Man",
                    10000.0,
                    "Sinopsis",
                    "trailer",
                    2002,
                    "Marvel",
                    "portada.webp",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            assertEquals(1L, producto.getId());
            assertEquals("Spider-Man", producto.getNombre());
            assertEquals(10000.0, producto.getPrecio());
            assertEquals("Sinopsis", producto.getSinopsis());
            assertEquals("trailer", producto.getUrlTrailer());
            assertEquals(2002, producto.getAnio());
            assertEquals("Marvel", producto.getSaga());
            assertEquals("portada.webp", producto.getPortadaSaga());
        }
    }

    @Nested
    @DisplayName("Colecciones por defecto")
    class ColeccionesPorDefecto {

        @Test
        @DisplayName("deben inicializarse vacias")
        void debenInicializarseVacias() {
            ProductoModel producto = new ProductoModel();
            assertNotNull(producto.getImagenes());
            assertNotNull(producto.getPlataformas());
            assertNotNull(producto.getGeneros());
            assertNotNull(producto.getEmpresas());
            assertNotNull(producto.getDesarrolladores());
            assertNotNull(producto.getLinksCompra());

            assertTrue(producto.getImagenes().isEmpty());
            assertTrue(producto.getPlataformas().isEmpty());
            assertTrue(producto.getGeneros().isEmpty());
            assertTrue(producto.getEmpresas().isEmpty());
            assertTrue(producto.getDesarrolladores().isEmpty());
            assertTrue(producto.getLinksCompra().isEmpty());
        }
    }

    @Nested
    @DisplayName("Metodo aplicarDescuento")
    class AplicarDescuento {

        @Test
        @DisplayName("debe aplicar descuento correctamente")
        void debeAplicarDescuentoCorrectamente() {
            ProductoModel producto = new ProductoModel();
            producto.setPrecio(10000.0);
            producto.aplicarDescuento(10);
            assertEquals(9000.0, producto.getPrecio());
        }

        @Test
        @DisplayName("no debe modificar precio si descuento es cero")
        void noDebeModificarPrecioSiDescuentoEsCero() {
            ProductoModel producto = new ProductoModel();
            producto.setPrecio(10000.0);
            producto.aplicarDescuento(0);
            assertEquals(10000.0, producto.getPrecio());
        }

        @Test
        @DisplayName("porcentaje negativo no modifica precio")
        void porcentajeNegativoNoModifica() {
            ProductoModel p = new ProductoModel();
            p.setPrecio(10000.0);
            p.aplicarDescuento(-5);
            assertEquals(10000.0, p.getPrecio());
        }

        @Test
        @DisplayName("no debe modificar precio si es null")
        void noDebeModificarPrecioSiEsNull() {
            ProductoModel producto = new ProductoModel();
            producto.aplicarDescuento(10);
            assertNull(producto.getPrecio());
        }
    }

    @Nested
    @DisplayName("Metodo esDisponible")
    class EsDisponible {

        @Test
        @DisplayName("retorna true cuando precio es positivo")
        void retornaTrueCuandoPrecioEsPositivo() {
            ProductoModel producto = new ProductoModel();
            producto.setPrecio(1000.0);
            assertTrue(producto.esDisponible());
        }

        @Test
        @DisplayName("retorna false cuando precio es cero")
        void retornaFalseCuandoPrecioEsCero() {
            ProductoModel producto = new ProductoModel();
            producto.setPrecio(0.0);
            assertFalse(producto.esDisponible());
        }

        @Test
        @DisplayName("retorna false cuando precio es null")
        void retornaFalseCuandoPrecioEsNull() {
            ProductoModel producto = new ProductoModel();
            assertFalse(producto.esDisponible());
        }

        @Test
        @DisplayName("retorna false cuando precio es negativo")
        void retornaFalseCuandoPrecioEsNegativo() {
            ProductoModel producto = new ProductoModel();
            producto.setPrecio(-1.0);
            assertFalse(producto.esDisponible());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("productos con mismo id son iguales")
        void productosConMismoIdSonIguales() {
            ProductoModel p1 = new ProductoModel(); p1.setId(1L);
            ProductoModel p2 = new ProductoModel(); p2.setId(1L);
            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        @DisplayName("productos con distinto id no son iguales")
        void productosConDistintoIdNoSonIguales() {
            ProductoModel p1 = new ProductoModel(); p1.setId(1L);
            ProductoModel p2 = new ProductoModel(); p2.setId(2L);
            assertNotEquals(p1, p2);
        }

        @Test
        void equalsConNull() {
            ProductoModel p = new ProductoModel(); p.setId(1L);
            assertNotEquals(null, p);
        }

        @Test
        void equalsConOtraClase() {
            ProductoModel p = new ProductoModel(); p.setId(1L);
            assertNotEquals("texto", p);
        }

        @Test
        void objetosSinIdSonIguales() {
            assertEquals(new ProductoModel(), new ProductoModel());
        }

        @Test
        void nullVsValorId() {
            ProductoModel p1 = new ProductoModel();
            ProductoModel p2 = new ProductoModel(); p2.setId(1L);
            assertNotEquals(p1, p2);
        }
    }

    @Nested
    @DisplayName("Cobertura adicional Equals y HashCode")
    class CoberturaAdicionalEqualsHashCode {

        @Test
        @DisplayName("Comparación con otra clase")
        void testEqualsOtraClase() {

            ProductoModel producto = new ProductoModel();

            assertNotEquals("texto", producto);
        }

        @Test
        @DisplayName("Objetos vacíos son iguales")
        void testObjetosVaciosSonIguales() {

            ProductoModel p1 = new ProductoModel();
            ProductoModel p2 = new ProductoModel();

            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("debe contener id y nombre")
        void debeContenerIdYNombre() {
            ProductoModel producto = new ProductoModel();
            producto.setId(5L);
            producto.setNombre("God of War");
            String texto = producto.toString();
            assertTrue(texto.contains("5"));
            assertTrue(texto.contains("God of War"));
        }

        @Test
        @DisplayName("objeto vacío genera toString sin errores")
        void toStringObjetoVacio() {
            assertNotNull(new ProductoModel().toString());
        }
    }
}