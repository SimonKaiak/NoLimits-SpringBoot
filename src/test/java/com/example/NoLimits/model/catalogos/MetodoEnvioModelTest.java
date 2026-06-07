package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MetodoEnvioModel Tests")
class MetodoEnvioModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear metodo de envio correctamente")
        void debeCrearMetodoEnvioCorrectamente() {

            MetodoEnvioModel model = new MetodoEnvioModel(
                    1L,
                    "Retiro en tienda",
                    "Retiro presencial",
                    true,
                    null
            );

            assertEquals(1L, model.getId());
            assertEquals("Retiro en tienda", model.getNombre());
            assertEquals("Retiro presencial", model.getDescripcion());
            assertTrue(model.getActivo());
            assertNull(model.getVentas());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            MetodoEnvioModel model = new MetodoEnvioModel();

            model.setId(10L);
            model.setNombre("Despacho");
            model.setDescripcion("Envío a domicilio");
            model.setActivo(false);

            assertEquals(10L, model.getId());
            assertEquals("Despacho", model.getNombre());
            assertEquals("Envío a domicilio", model.getDescripcion());
            assertFalse(model.getActivo());
        }
    }

    @Nested
    @DisplayName("Valor por defecto")
    class ValorPorDefecto {

        @Test
        @DisplayName("activo inicia en true")
        void activoIniciaEnTrue() {

            MetodoEnvioModel model = new MetodoEnvioModel();

            assertTrue(model.getActivo());
        }
    }

    @Nested
    @DisplayName("Metodo esActivo")
    class MetodoEsActivo {

        @Test
        @DisplayName("retorna true cuando activo es true")
        void retornaTrueCuandoActivoEsTrue() {

            MetodoEnvioModel model = new MetodoEnvioModel();
            model.setActivo(true);

            assertTrue(model.esActivo());
        }

        @Test
        @DisplayName("retorna false cuando activo es false")
        void retornaFalseCuandoActivoEsFalse() {

            MetodoEnvioModel model = new MetodoEnvioModel();
            model.setActivo(false);

            assertFalse(model.esActivo());
        }

        @Test
        @DisplayName("retorna false cuando activo es null")
        void retornaFalseCuandoActivoEsNull() {

            MetodoEnvioModel model = new MetodoEnvioModel();
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

            MetodoEnvioModel m1 = new MetodoEnvioModel();
            m1.setId(1L);
            m1.setNombre("Retiro");

            MetodoEnvioModel m2 = new MetodoEnvioModel();
            m2.setId(1L);
            m2.setNombre("Retiro");

            assertEquals(m1, m2);
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        @DisplayName("toString no debe ser null")
        void toStringNoDebeSerNull() {

            MetodoEnvioModel model = new MetodoEnvioModel();

            assertNotNull(model.toString());
        }
    }
}