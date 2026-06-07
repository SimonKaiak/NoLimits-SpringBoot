package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GeneroModel Tests")
class GeneroModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear genero correctamente")
        void debeCrearGeneroCorrectamente() {

            GeneroModel model = new GeneroModel(
                    1L,
                    "Acción",
                    null
            );

            assertEquals(1L, model.getId());
            assertEquals("Acción", model.getNombre());
            assertNull(model.getProductos());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            GeneroModel model = new GeneroModel();

            model.setId(10L);
            model.setNombre("RPG");

            assertEquals(10L, model.getId());
            assertEquals("RPG", model.getNombre());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("objetos con mismo id deben ser iguales")
        void objetosConMismoIdDebenSerIguales() {

            GeneroModel g1 = new GeneroModel();
            g1.setId(1L);

            GeneroModel g2 = new GeneroModel();
            g2.setId(1L);

            assertEquals(g1, g2);
            assertEquals(g1.hashCode(), g2.hashCode());
        }

        @Test
        @DisplayName("objetos con distinto id no deben ser iguales")
        void objetosConDistintoIdNoDebenSerIguales() {

            GeneroModel g1 = new GeneroModel();
            g1.setId(1L);

            GeneroModel g2 = new GeneroModel();
            g2.setId(2L);

            assertNotEquals(g1, g2);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("debe contener id y nombre")
        void debeContenerIdYNombre() {

            GeneroModel model = new GeneroModel();
            model.setId(5L);
            model.setNombre("Aventura");

            String resultado = model.toString();

            assertTrue(resultado.contains("5"));
            assertTrue(resultado.contains("Aventura"));
        }
    }
}