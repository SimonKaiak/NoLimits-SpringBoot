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
    }
}