package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class ClasificacionModelTest {

    @Nested
    @DisplayName("Constructor personalizado")
    class ConstructorPersonalizado {

        @Test
        @DisplayName("crea correctamente una clasificación")
        void testConstructorPersonalizado() {

            ClasificacionModel model =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Contenido apto para adolescentes",
                            true
                    );

            assertEquals(1L, model.getId());
            assertEquals("T", model.getNombre());
            assertEquals("Contenido apto para adolescentes", model.getDescripcion());
            assertTrue(model.isActivo());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            ClasificacionModel model = new ClasificacionModel();

            model.setId(10L);
            model.setNombre("M");
            model.setDescripcion("Mayores de edad");
            model.setActivo(false);

            assertEquals(10L, model.getId());
            assertEquals("M", model.getNombre());
            assertEquals("Mayores de edad", model.getDescripcion());
            assertFalse(model.isActivo());
        }
    }

    @Nested
    @DisplayName("Valor por defecto")
    class ValorPorDefecto {

        @Test
        @DisplayName("activo inicia en true")
        void testActivoPorDefecto() {

            ClasificacionModel model = new ClasificacionModel();

            assertTrue(model.isActivo());
        }
    }

    @Nested
    @DisplayName("Productos")
    class Productos {

        @Test
        @DisplayName("setProductos y getProductos funcionan")
        void testProductos() {

            ClasificacionModel model = new ClasificacionModel();

            model.setProductos(java.util.List.of());

            assertNotNull(model.getProductos());
        }
    }

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("all args constructor asigna todos los campos")
        void testAllArgsConstructor() {

            List<ProductoModel> productos = new ArrayList<>();

            ClasificacionModel model =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Descripcion",
                            true,
                            productos
                    );

            assertEquals(1L, model.getId());
            assertEquals("T", model.getNombre());
            assertEquals("Descripcion", model.getDescripcion());
            assertTrue(model.isActivo());
            assertEquals(productos, model.getProductos());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        @DisplayName("equals mismo objeto")
        void testEqualsMismoObjeto() {

            ClasificacionModel model =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true
                    );

            assertEquals(model, model);
        }

        @Test
        @DisplayName("equals con null")
        void testEqualsNull() {

            ClasificacionModel model =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true
                    );

            assertNotEquals(null, model);
        }

        @Test
        @DisplayName("equals con otra clase")
        void testEqualsOtraClase() {

            ClasificacionModel model =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true
                    );

            assertNotEquals("texto", model);
        }

        @Test
        @DisplayName("equals y hashCode para objetos iguales")
        void testEqualsHashCode() {

            ClasificacionModel a =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true
                    );

            ClasificacionModel b =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true
                    );

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("equals retorna false cuando los datos son distintos")
        void testEqualsDistinto() {

            ClasificacionModel a =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true
                    );

            ClasificacionModel b =
                    new ClasificacionModel(
                            2L,
                            "M",
                            "Otra",
                            false
                    );

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals cubre campos nulos")
        void testEqualsCamposNull() {

            ClasificacionModel a =
                    new ClasificacionModel(
                            1L,
                            null,
                            null,
                            true
                    );

            ClasificacionModel b =
                    new ClasificacionModel(
                            1L,
                            null,
                            null,
                            true
                    );

            assertEquals(a, b);
        }

        @Test
        @DisplayName("toString retorna valor")
        void testToString() {

            ClasificacionModel model =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true
                    );

            assertNotNull(model.toString());
        }

        @Test
        @DisplayName("equals retorna false cuando nombre es distinto")
        void testEqualsNombreDistinto() {

            ClasificacionModel a =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true
                    );

            ClasificacionModel b =
                    new ClasificacionModel(
                            1L,
                            "M",
                            "Desc",
                            true
                    );

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals retorna false cuando descripcion es distinta")
        void testEqualsDescripcionDistinta() {

            ClasificacionModel a =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc1",
                            true
                    );

            ClasificacionModel b =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc2",
                            true
                    );

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals retorna false cuando activo es distinto")
        void testEqualsActivoDistinto() {

            ClasificacionModel a =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true
                    );

            ClasificacionModel b =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            false
                    );

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals retorna false cuando productos son distintos")
        void testEqualsProductosDistintos() {

            ClasificacionModel a =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true,
                            java.util.List.of()
                    );

            ClasificacionModel b =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Desc",
                            true,
                            null
                    );

            assertNotEquals(a, b);
        }
    }
}