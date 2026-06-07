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
    }
}
