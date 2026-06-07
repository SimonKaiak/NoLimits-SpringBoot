package com.example.NoLimits.model.ubicacion;

import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ComunaModel Tests")
class ComunaModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear comuna correctamente")
        void debeCrearComunaCorrectamente() {

            RegionModel region = new RegionModel();
            region.setId(13L);

            List<DireccionModel> direcciones = new ArrayList<>();

            ComunaModel comuna = new ComunaModel(
                    13101L,
                    "Santiago",
                    region,
                    direcciones
            );

            assertEquals(13101L, comuna.getId());
            assertEquals("Santiago", comuna.getNombre());
            assertEquals(region, comuna.getRegion());
            assertEquals(direcciones, comuna.getDirecciones());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            RegionModel region = new RegionModel();
            List<DireccionModel> direcciones = new ArrayList<>();

            ComunaModel comuna = new ComunaModel();

            comuna.setId(1L);
            comuna.setNombre("Providencia");
            comuna.setRegion(region);
            comuna.setDirecciones(direcciones);

            assertEquals(1L, comuna.getId());
            assertEquals("Providencia", comuna.getNombre());
            assertEquals(region, comuna.getRegion());
            assertEquals(direcciones, comuna.getDirecciones());
        }
    }

    @Nested
    @DisplayName("Constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("debe crear objeto vacío")
        void debeCrearObjetoVacio() {

            ComunaModel comuna = new ComunaModel();

            assertNull(comuna.getId());
            assertNull(comuna.getNombre());
            assertNull(comuna.getRegion());
            assertNull(comuna.getDirecciones());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        @DisplayName("objetos con mismo contenido son iguales")
        void objetosConMismoContenidoSonIguales() {

            ComunaModel c1 = new ComunaModel();
            c1.setId(1L);
            c1.setNombre("Santiago");

            ComunaModel c2 = new ComunaModel();
            c2.setId(1L);
            c2.setNombre("Santiago");

            assertEquals(c1, c2);
            assertEquals(c1.hashCode(), c2.hashCode());
        }

        @Test
        @DisplayName("toString no debe retornar null")
        void toStringNoDebeRetornarNull() {

            ComunaModel comuna = new ComunaModel();

            assertNotNull(comuna.toString());
        }
    }
}