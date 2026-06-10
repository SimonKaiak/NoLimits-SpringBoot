package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EstadoModel Tests")
class EstadoModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear estado correctamente")
        void debeCrearEstadoCorrectamente() {

            EstadoModel model = new EstadoModel(
                    1L,
                    "Activo",
                    "Producto disponible",
                    true,
                    null
            );

            assertEquals(1L, model.getId());
            assertEquals("Activo", model.getNombre());
            assertEquals("Producto disponible", model.getDescripcion());
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

            EstadoModel model = new EstadoModel();

            model.setId(10L);
            model.setNombre("Agotado");
            model.setDescripcion("Sin stock");
            model.setActivo(false);

            assertEquals(10L, model.getId());
            assertEquals("Agotado", model.getNombre());
            assertEquals("Sin stock", model.getDescripcion());
            assertFalse(model.getActivo());
        }

        @Test
        @DisplayName("debe asignar y obtener productos")
        void debeAsignarYObtenerProductos() {

            EstadoModel model = new EstadoModel();

            model.setProductos(new java.util.ArrayList<>());

            assertNotNull(model.getProductos());
        }
    }

    @Nested
    @DisplayName("Valor por defecto")
    class ValorPorDefecto {

        @Test
        @DisplayName("activo inicia en true")
        void activoIniciaEnTrue() {

            EstadoModel model = new EstadoModel();

            assertTrue(model.getActivo());
        }
    }

    @Nested
    @DisplayName("Metodo esActivo")
    class MetodoEsActivo {

        @Test
        @DisplayName("retorna true cuando activo es true")
        void retornaTrueCuandoActivoEsTrue() {

            EstadoModel model = new EstadoModel();
            model.setActivo(true);

            assertTrue(model.esActivo());
        }

        @Test
        @DisplayName("retorna false cuando activo es false")
        void retornaFalseCuandoActivoEsFalse() {

            EstadoModel model = new EstadoModel();
            model.setActivo(false);

            assertFalse(model.esActivo());
        }

        @Test
        @DisplayName("retorna false cuando activo es null")
        void retornaFalseCuandoActivoEsNull() {

            EstadoModel model = new EstadoModel();
            model.setActivo(null);

            assertFalse(model.esActivo());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        @DisplayName("objetos con mismo contenido son iguales")
        void objetosConMismoContenidoSonIguales() {

            EstadoModel e1 = new EstadoModel();
            e1.setId(1L);
            e1.setNombre("Activo");

            EstadoModel e2 = new EstadoModel();
            e2.setId(1L);
            e2.setNombre("Activo");

            assertEquals(e1, e2);
            assertEquals(e1.hashCode(), e2.hashCode());
        }

        @Test
        @DisplayName("toString no debe ser null")
        void toStringNoDebeSerNull() {

            EstadoModel model = new EstadoModel();

            assertNotNull(model.toString());
        }

        @Test
        @DisplayName("equals con null")
        void equalsConNull() {

            EstadoModel model = new EstadoModel();

            assertNotEquals(null, model);
        }

        @Test
        @DisplayName("equals con otra clase")
        void equalsConOtraClase() {

            EstadoModel model = new EstadoModel();

            assertNotEquals("texto", model);
        }

        @Test
        @DisplayName("equals consigo mismo")
        void equalsConsigoMismo() {

            EstadoModel model = new EstadoModel();

            assertEquals(model, model);
        }

        @Test
        @DisplayName("equals con id distinto")
        void equalsIdDistinto() {

            EstadoModel a =
                    new EstadoModel(
                            1L,
                            "Activo",
                            "Desc",
                            true,
                            null
                    );

            EstadoModel b =
                    new EstadoModel(
                            2L,
                            "Activo",
                            "Desc",
                            true,
                            null
                    );

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals con campos null")
        void equalsCamposNull() {

            EstadoModel a =
                    new EstadoModel(
                            1L,
                            null,
                            null,
                            true,
                            null
                    );

            EstadoModel b =
                    new EstadoModel(
                            1L,
                            null,
                            null,
                            true,
                            null
                    );

            assertEquals(a, b);
        }

        @Test
        @DisplayName("equals con descripcion distinta")
        void equalsDescripcionDistinta() {

            EstadoModel a = new EstadoModel(
                    1L,
                    "Activo",
                    "Descripcion 1",
                    true,
                    null
            );

            EstadoModel b = new EstadoModel(
                    1L,
                    "Activo",
                    "Descripcion 2",
                    true,
                    null
            );

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals con activo distinto")
        void equalsActivoDistinto() {

            EstadoModel a = new EstadoModel(
                    1L,
                    "Activo",
                    "Descripcion",
                    true,
                    null
            );

            EstadoModel b = new EstadoModel(
                    1L,
                    "Activo",
                    "Descripcion",
                    false,
                    null
            );

            assertNotEquals(a, b);
        }
    }
}