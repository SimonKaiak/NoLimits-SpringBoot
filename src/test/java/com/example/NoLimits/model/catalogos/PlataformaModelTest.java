package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlataformaModel Tests")
class PlataformaModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear plataforma correctamente")
        void debeCrearPlataformaCorrectamente() {

            PlataformaModel model = new PlataformaModel(
                    1L,
                    "PC",
                    null,
                    null
            );

            assertEquals(1L, model.getId());
            assertEquals("PC", model.getNombre());
            assertNull(model.getProductos());
            assertNull(model.getLinksCompra());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            PlataformaModel model = new PlataformaModel();

            model.setId(10L);
            model.setNombre("PlayStation 5");

            assertEquals(10L, model.getId());
            assertEquals("PlayStation 5", model.getNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("listas deben inicializarse vacías")
        void listasDebenInicializarseVacias() {

            PlataformaModel model = new PlataformaModel();

            assertNotNull(model.getProductos());
            assertNotNull(model.getLinksCompra());

            assertTrue(model.getProductos().isEmpty());
            assertTrue(model.getLinksCompra().isEmpty());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("objetos con mismo id deben ser iguales")
        void objetosConMismoIdDebenSerIguales() {

            PlataformaModel p1 = new PlataformaModel();
            p1.setId(1L);

            PlataformaModel p2 = new PlataformaModel();
            p2.setId(1L);

            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        @DisplayName("objetos con distinto id no deben ser iguales")
        void objetosConDistintoIdNoDebenSerIguales() {

            PlataformaModel p1 = new PlataformaModel();
            p1.setId(1L);

            PlataformaModel p2 = new PlataformaModel();
            p2.setId(2L);

            assertNotEquals(p1, p2);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("debe contener id y nombre")
        void debeContenerIdYNombre() {

            PlataformaModel model = new PlataformaModel();
            model.setId(5L);
            model.setNombre("Xbox Series X");

            String resultado = model.toString();

            assertTrue(resultado.contains("5"));
            assertTrue(resultado.contains("Xbox Series X"));
        }
    }
}