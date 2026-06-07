package com.example.NoLimits.model.ubicacion;

import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RegionModel Tests")
class RegionModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear region correctamente")
        void debeCrearRegionCorrectamente() {

            List<ComunaModel> comunas = new ArrayList<>();

            RegionModel region = new RegionModel(
                    13L,
                    "Región Metropolitana",
                    comunas
            );

            assertEquals(13L, region.getId());
            assertEquals("Región Metropolitana", region.getNombre());
            assertEquals(comunas, region.getComunas());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            List<ComunaModel> comunas = new ArrayList<>();

            RegionModel region = new RegionModel();

            region.setId(1L);
            region.setNombre("Valparaíso");
            region.setComunas(comunas);

            assertEquals(1L, region.getId());
            assertEquals("Valparaíso", region.getNombre());
            assertEquals(comunas, region.getComunas());
        }
    }

    @Nested
    @DisplayName("Constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("debe crear objeto vacío")
        void debeCrearObjetoVacio() {

            RegionModel region = new RegionModel();

            assertNull(region.getId());
            assertNull(region.getNombre());
            assertNull(region.getComunas());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        @DisplayName("objetos con mismo contenido son iguales")
        void objetosConMismoContenidoSonIguales() {

            RegionModel r1 = new RegionModel();
            r1.setId(13L);
            r1.setNombre("Metropolitana");

            RegionModel r2 = new RegionModel();
            r2.setId(13L);
            r2.setNombre("Metropolitana");

            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        @DisplayName("toString no debe retornar null")
        void toStringNoDebeRetornarNull() {

            RegionModel region = new RegionModel();

            assertNotNull(region.toString());
        }
    }
}