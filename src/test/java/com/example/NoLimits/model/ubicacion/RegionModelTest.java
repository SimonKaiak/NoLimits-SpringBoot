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

            List<ComunaModel> comunas1 = new ArrayList<>();
            List<ComunaModel> comunas2 = new ArrayList<>();

            RegionModel r1 = new RegionModel();
            r1.setId(13L);
            r1.setNombre("Metropolitana");
            r1.setComunas(comunas1);

            RegionModel r2 = new RegionModel();
            r2.setId(13L);
            r2.setNombre("Metropolitana");
            r2.setComunas(comunas2);

            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        @DisplayName("equals retorna true para misma instancia")
        void equalsMismaInstancia() {

            // Arrange
            RegionModel region = new RegionModel();

            // Act & Assert
            assertEquals(region, region);
        }

        @Test
        @DisplayName("equals retorna false con null")
        void equalsConNull() {

            // Arrange
            RegionModel region = new RegionModel();

            // Act & Assert
            assertNotEquals(null, region);
        }

        @Test
        @DisplayName("equals retorna false con otra clase")
        void equalsConOtraClase() {

            // Arrange
            RegionModel region = new RegionModel();

            // Act & Assert
            assertNotEquals("texto", region);
        }

        @Test
        @DisplayName("equals detecta id diferente")
        void equalsIdDiferente() {

            // Arrange
            RegionModel r1 = new RegionModel();
            r1.setId(1L);

            RegionModel r2 = new RegionModel();
            r2.setId(2L);

            // Act & Assert
            assertNotEquals(r1, r2);
        }

        @Test
        @DisplayName("equals detecta nombre diferente")
        void equalsNombreDiferente() {

            // Arrange
            RegionModel r1 = new RegionModel();
            r1.setNombre("Metropolitana");

            RegionModel r2 = new RegionModel();
            r2.setNombre("Valparaíso");

            // Act & Assert
            assertNotEquals(r1, r2);
        }

        @Test
        @DisplayName("equals detecta diferencia cuando un nombre es null")
        void equalsNombreNullVsValor() {

            // Arrange
            RegionModel r1 = new RegionModel();

            RegionModel r2 = new RegionModel();
            r2.setNombre("Metropolitana");

            // Act & Assert
            assertNotEquals(r1, r2);
        }

        @Test
        @DisplayName("equals detecta lista comunas diferente")
        void equalsComunasDiferentes() {

            // Arrange
            RegionModel r1 = new RegionModel();
            r1.setComunas(new ArrayList<>());

            RegionModel r2 = new RegionModel();

            List<ComunaModel> comunas = new ArrayList<>();
            comunas.add(new ComunaModel());

            r2.setComunas(comunas);

            // Act & Assert
            assertNotEquals(r1, r2);
        }

        @Test
        @DisplayName("equals detecta diferencia cuando una lista es null")
        void equalsComunasNullVsLista() {

            // Arrange
            RegionModel r1 = new RegionModel();

            RegionModel r2 = new RegionModel();
            r2.setComunas(new ArrayList<>());

            // Act & Assert
            assertNotEquals(r1, r2);
        }

        @Test
        @DisplayName("objetos vacíos son iguales")
        void objetosVaciosIguales() {

            // Arrange
            RegionModel r1 = new RegionModel();
            RegionModel r2 = new RegionModel();

            // Act & Assert
            assertEquals(r1, r2);
        }

        @Test
        @DisplayName("hashCode objeto vacío")
        void hashCodeObjetoVacio() {

            // Arrange
            RegionModel region = new RegionModel();

            // Act
            int hash = region.hashCode();

            // Assert
            assertNotEquals(0, hash);
        }

        @Test
        @DisplayName("objetos vacíos generan mismo hash")
        void objetosVaciosMismoHash() {

            // Arrange
            RegionModel r1 = new RegionModel();
            RegionModel r2 = new RegionModel();

            // Act & Assert
            assertEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        @DisplayName("toString contiene nombre")
        void toStringContieneNombre() {

            // Arrange
            RegionModel region = new RegionModel();
            region.setNombre("Metropolitana");

            // Act
            String resultado = region.toString();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("Metropolitana"));
        }

        @Test
        @DisplayName("toString no debe retornar null")
        void toStringNoDebeRetornarNull() {

            RegionModel region = new RegionModel();

            assertNotNull(region.toString());
        }
    }
}