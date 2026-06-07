package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DesarrolladorModel Tests")
class DesarrolladorModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear desarrollador correctamente")
        void debeCrearDesarrolladorCorrectamente() {

            DesarrolladorModel model = new DesarrolladorModel(
                    1L,
                    "Nintendo",
                    true,
                    null,
                    null
            );

            assertEquals(1L, model.getId());
            assertEquals("Nintendo", model.getNombre());
            assertTrue(model.isActivo());
            assertNull(model.getProductos());
            assertNull(model.getTipos());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            DesarrolladorModel model = new DesarrolladorModel();

            model.setId(10L);
            model.setNombre("Valve");
            model.setActivo(false);

            assertEquals(10L, model.getId());
            assertEquals("Valve", model.getNombre());
            assertFalse(model.isActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("activo debe iniciar en true")
        void activoDebeIniciarEnTrue() {

            DesarrolladorModel model = new DesarrolladorModel();

            assertTrue(model.isActivo());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("objetos con mismo id deben ser iguales")
        void objetosConMismoIdDebenSerIguales() {

            DesarrolladorModel d1 = new DesarrolladorModel();
            d1.setId(1L);

            DesarrolladorModel d2 = new DesarrolladorModel();
            d2.setId(1L);

            assertEquals(d1, d2);
            assertEquals(d1.hashCode(), d2.hashCode());
        }

        @Test
        @DisplayName("objetos con distinto id no deben ser iguales")
        void objetosConDistintoIdNoDebenSerIguales() {

            DesarrolladorModel d1 = new DesarrolladorModel();
            d1.setId(1L);

            DesarrolladorModel d2 = new DesarrolladorModel();
            d2.setId(2L);

            assertNotEquals(d1, d2);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("debe contener id y nombre")
        void debeContenerIdYNombre() {

            DesarrolladorModel model = new DesarrolladorModel();
            model.setId(5L);
            model.setNombre("Capcom");

            String resultado = model.toString();

            assertTrue(resultado.contains("5"));
            assertTrue(resultado.contains("Capcom"));
        }
    }
}