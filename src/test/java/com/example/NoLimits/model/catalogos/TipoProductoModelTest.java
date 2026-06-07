package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoProductoModel Tests")
class TipoProductoModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear tipo de producto correctamente")
        void debeCrearTipoProductoCorrectamente() {

            TipoProductoModel model = new TipoProductoModel(
                    1L,
                    "Videojuego",
                    "Producto interactivo",
                    true,
                    null
            );

            assertEquals(1L, model.getId());
            assertEquals("Videojuego", model.getNombre());
            assertEquals("Producto interactivo", model.getDescripcion());
            assertTrue(model.getActivo());
            assertNull(model.getProductos());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            TipoProductoModel model = new TipoProductoModel();

            model.setId(10L);
            model.setNombre("Película");
            model.setDescripcion("Contenido audiovisual");
            model.setActivo(false);

            assertEquals(10L, model.getId());
            assertEquals("Película", model.getNombre());
            assertEquals("Contenido audiovisual", model.getDescripcion());
            assertFalse(model.getActivo());
        }
    }

    @Nested
    @DisplayName("Valor por defecto")
    class ValorPorDefecto {

        @Test
        @DisplayName("activo inicia en true")
        void activoIniciaEnTrue() {

            TipoProductoModel model = new TipoProductoModel();

            assertTrue(model.getActivo());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("objetos con mismo id deben ser iguales")
        void objetosConMismoIdDebenSerIguales() {

            TipoProductoModel t1 = new TipoProductoModel();
            t1.setId(1L);

            TipoProductoModel t2 = new TipoProductoModel();
            t2.setId(1L);

            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());
        }

        @Test
        @DisplayName("objetos con distinto id no deben ser iguales")
        void objetosConDistintoIdNoDebenSerIguales() {

            TipoProductoModel t1 = new TipoProductoModel();
            t1.setId(1L);

            TipoProductoModel t2 = new TipoProductoModel();
            t2.setId(2L);

            assertNotEquals(t1, t2);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("debe contener id y nombre")
        void debeContenerIdYNombre() {

            TipoProductoModel model = new TipoProductoModel();
            model.setId(5L);
            model.setNombre("Accesorio");

            String resultado = model.toString();

            assertTrue(resultado.contains("5"));
            assertTrue(resultado.contains("Accesorio"));
        }
    }
}
