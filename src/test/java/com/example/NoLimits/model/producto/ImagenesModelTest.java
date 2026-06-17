package com.example.NoLimits.model.producto;

import com.example.NoLimits.Multimedia.model.producto.ImagenesModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ImagenesModel Tests")
class ImagenesModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear imagen correctamente")
        void debeCrearImagenCorrectamente() {

            ProductoModel producto = new ProductoModel();
            producto.setId(10L);

            ImagenesModel imagen =
                    new ImagenesModel(
                            1L,
                            "/assets/img/spiderman.webp",
                            "Spider-Man",
                            producto
                    );

            assertEquals(1L, imagen.getId());
            assertEquals("/assets/img/spiderman.webp", imagen.getRuta());
            assertEquals("Spider-Man", imagen.getAltText());
            assertEquals(producto, imagen.getProducto());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            ProductoModel producto = new ProductoModel();

            ImagenesModel imagen = new ImagenesModel();

            imagen.setId(5L);
            imagen.setRuta("/img/test.webp");
            imagen.setAltText("Imagen de prueba");
            imagen.setProducto(producto);

            assertEquals(5L, imagen.getId());
            assertEquals("/img/test.webp", imagen.getRuta());
            assertEquals("Imagen de prueba", imagen.getAltText());
            assertEquals(producto, imagen.getProducto());
        }
    }

    @Nested
    @DisplayName("Constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("debe crear objeto vacío")
        void debeCrearObjetoVacio() {

            ImagenesModel imagen = new ImagenesModel();

            assertNull(imagen.getId());
            assertNull(imagen.getRuta());
            assertNull(imagen.getAltText());
            assertNull(imagen.getProducto());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        @DisplayName("objetos con mismo contenido son iguales")
        void objetosConMismoContenidoSonIguales() {

            ImagenesModel i1 = new ImagenesModel();
            i1.setId(1L);
            i1.setRuta("ruta");

            ImagenesModel i2 = new ImagenesModel();
            i2.setId(1L);
            i2.setRuta("ruta");

            assertEquals(i1, i2);
            assertEquals(i1.hashCode(), i2.hashCode());
        }

        @Test
        @DisplayName("toString no debe ser null")
        void toStringNoDebeSerNull() {

            ImagenesModel imagen = new ImagenesModel();

            assertNotNull(imagen.toString());
        }

        @Test
        @DisplayName("misma instancia retorna true")
        void mismaInstancia() {

            ImagenesModel imagen = new ImagenesModel();

            assertEquals(imagen, imagen);
        }

        @Test
        @DisplayName("comparación con null retorna false")
        void comparacionConNull() {

            ImagenesModel imagen = new ImagenesModel();

            assertNotEquals(null, imagen);
        }

        @Test
        @DisplayName("comparación con otra clase retorna false")
        void comparacionConOtraClase() {

            ImagenesModel imagen = new ImagenesModel();

            assertNotEquals("texto", imagen);
        }

        @Test
        @DisplayName("objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            ImagenesModel i1 = new ImagenesModel();
            ImagenesModel i2 = new ImagenesModel();

            assertEquals(i1, i2);
        }

        @Test
        @DisplayName("objetos vacíos tienen mismo hashCode")
        void objetosVaciosMismoHashCode() {

            ImagenesModel i1 = new ImagenesModel();
            ImagenesModel i2 = new ImagenesModel();

            assertEquals(i1.hashCode(), i2.hashCode());
        }

        @Test
        @DisplayName("diferente id")
        void diferenteId() {

            ImagenesModel i1 = new ImagenesModel();
            i1.setId(1L);

            ImagenesModel i2 = new ImagenesModel();
            i2.setId(2L);

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("diferente ruta")
        void diferenteRuta() {

            ImagenesModel i1 = new ImagenesModel();
            i1.setRuta("ruta1");

            ImagenesModel i2 = new ImagenesModel();
            i2.setRuta("ruta2");

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("diferente altText")
        void diferenteAltText() {

            ImagenesModel i1 = new ImagenesModel();
            i1.setAltText("uno");

            ImagenesModel i2 = new ImagenesModel();
            i2.setAltText("dos");

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("diferente producto")
        void diferenteProducto() {

            ProductoModel p1 = new ProductoModel();
            p1.setId(1L);

            ProductoModel p2 = new ProductoModel();
            p2.setId(2L);

            ImagenesModel i1 = new ImagenesModel();
            i1.setProducto(p1);

            ImagenesModel i2 = new ImagenesModel();
            i2.setProducto(p2);

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("null vs valor id")
        void nullVsValorId() {

            ImagenesModel i1 = new ImagenesModel();

            ImagenesModel i2 = new ImagenesModel();
            i2.setId(1L);

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("valor vs null id")
        void valorVsNullId() {

            ImagenesModel i1 = new ImagenesModel();
            i1.setId(1L);

            ImagenesModel i2 = new ImagenesModel();

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("null vs valor ruta")
        void nullVsValorRuta() {

            ImagenesModel i1 = new ImagenesModel();

            ImagenesModel i2 = new ImagenesModel();
            i2.setRuta("ruta");

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("valor vs null ruta")
        void valorVsNullRuta() {

            ImagenesModel i1 = new ImagenesModel();
            i1.setRuta("ruta");

            ImagenesModel i2 = new ImagenesModel();

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("null vs valor altText")
        void nullVsValorAltText() {

            ImagenesModel i1 = new ImagenesModel();

            ImagenesModel i2 = new ImagenesModel();
            i2.setAltText("texto");

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("valor vs null altText")
        void valorVsNullAltText() {

            ImagenesModel i1 = new ImagenesModel();
            i1.setAltText("texto");

            ImagenesModel i2 = new ImagenesModel();

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("null vs valor producto")
        void nullVsValorProducto() {

            ProductoModel producto = new ProductoModel();

            ImagenesModel i1 = new ImagenesModel();

            ImagenesModel i2 = new ImagenesModel();
            i2.setProducto(producto);

            assertNotEquals(i1, i2);
        }

        @Test
        @DisplayName("valor vs null producto")
        void valorVsNullProducto() {

            ProductoModel producto = new ProductoModel();

            ImagenesModel i1 = new ImagenesModel();
            i1.setProducto(producto);

            ImagenesModel i2 = new ImagenesModel();

            assertNotEquals(i1, i2);
        }
    }
}
