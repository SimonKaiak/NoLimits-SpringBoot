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

            RegionModel region1 = new RegionModel();
            region1.setId(13L);
            region1.setNombre("Metropolitana");

            RegionModel region2 = new RegionModel();
            region2.setId(13L);
            region2.setNombre("Metropolitana");

            List<DireccionModel> direcciones1 = new ArrayList<>();
            List<DireccionModel> direcciones2 = new ArrayList<>();

            ComunaModel c1 = new ComunaModel();
            c1.setId(1L);
            c1.setNombre("Santiago");
            c1.setRegion(region1);
            c1.setDirecciones(direcciones1);

            ComunaModel c2 = new ComunaModel();
            c2.setId(1L);
            c2.setNombre("Santiago");
            c2.setRegion(region2);
            c2.setDirecciones(direcciones2);

            assertEquals(c1, c2);
            assertEquals(c1.hashCode(), c2.hashCode());
        }

        @Test
        @DisplayName("equals retorna true para misma instancia")
        void equalsMismaInstancia() {

            // Arrange
            ComunaModel comuna = new ComunaModel();

            // Act & Assert
            assertEquals(comuna, comuna);
        }

        @Test
        @DisplayName("equals retorna false con null")
        void equalsConNull() {

            // Arrange
            ComunaModel comuna = new ComunaModel();

            // Act & Assert
            assertNotEquals(null, comuna);
        }

        @Test
        @DisplayName("equals retorna false con otra clase")
        void equalsConOtraClase() {

            // Arrange
            ComunaModel comuna = new ComunaModel();

            // Act & Assert
            assertNotEquals("texto", comuna);
        }

        @Test
        @DisplayName("objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            // Arrange
            ComunaModel c1 = new ComunaModel();
            ComunaModel c2 = new ComunaModel();

            // Act & Assert
            assertEquals(c1, c2);
        }

        @Test
        @DisplayName("objetos vacíos generan mismo hash")
        void objetosVaciosMismoHash() {

            // Arrange
            ComunaModel c1 = new ComunaModel();
            ComunaModel c2 = new ComunaModel();

            // Act & Assert
            assertEquals(c1.hashCode(), c2.hashCode());
        }

        @Test
        @DisplayName("hashCode objeto vacío")
        void hashCodeObjetoVacio() {

            // Arrange
            ComunaModel comuna = new ComunaModel();

            // Act
            int hash = comuna.hashCode();

            // Assert
            assertNotEquals(0, hash);
        }

        @Test
        @DisplayName("equals detecta id diferente")
        void equalsIdDiferente() {

            // Arrange
            ComunaModel c1 = new ComunaModel();
            c1.setId(1L);

            ComunaModel c2 = new ComunaModel();
            c2.setId(2L);

            // Act & Assert
            assertNotEquals(c1, c2);
        }

        @Test
        @DisplayName("equals detecta nombre diferente")
        void equalsNombreDiferente() {

            // Arrange
            ComunaModel c1 = new ComunaModel();
            c1.setNombre("Santiago");

            ComunaModel c2 = new ComunaModel();
            c2.setNombre("Providencia");

            // Act & Assert
            assertNotEquals(c1, c2);
        }

        @Test
        @DisplayName("equals detecta region diferente")
        void equalsRegionDiferente() {

            // Arrange
            ComunaModel c1 = new ComunaModel();
            c1.setRegion(new RegionModel());

            ComunaModel c2 = new ComunaModel();

            // Act & Assert
            assertNotEquals(c1, c2);
        }

        @Test
        @DisplayName("equals detecta lista direcciones diferente")
        void equalsDireccionesDiferentes() {

            // Arrange
            ComunaModel c1 = new ComunaModel();
            c1.setDirecciones(new ArrayList<>());

            ComunaModel c2 = new ComunaModel();

            List<DireccionModel> direcciones = new ArrayList<>();
            direcciones.add(new DireccionModel());

            c2.setDirecciones(direcciones);

            // Act & Assert
            assertNotEquals(c1, c2);
        }

        @Test
        @DisplayName("equals detecta nombre null versus valor")
        void equalsNombreNullVsValor() {

            // Arrange
            ComunaModel c1 = new ComunaModel();

            ComunaModel c2 = new ComunaModel();
            c2.setNombre("Santiago");

            // Act & Assert
            assertNotEquals(c1, c2);
        }

        @Test
        @DisplayName("equals detecta region null versus valor")
        void equalsRegionNullVsValor() {

            // Arrange
            ComunaModel c1 = new ComunaModel();

            ComunaModel c2 = new ComunaModel();
            c2.setRegion(new RegionModel());

            // Act & Assert
            assertNotEquals(c1, c2);
        }

        @Test
        @DisplayName("equals detecta direcciones null versus lista")
        void equalsDireccionesNullVsLista() {

            // Arrange
            ComunaModel c1 = new ComunaModel();

            ComunaModel c2 = new ComunaModel();
            c2.setDirecciones(new ArrayList<>());

            // Act & Assert
            assertNotEquals(c1, c2);
        }

        @Test
        @DisplayName("toString contiene nombre")
        void toStringContieneNombre() {

            // Arrange
            ComunaModel comuna = new ComunaModel();
            comuna.setNombre("Santiago");

            // Act
            String resultado = comuna.toString();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("Santiago"));
        }

        @Test
        @DisplayName("toString no debe retornar null")
        void toStringNoDebeRetornarNull() {

            ComunaModel comuna = new ComunaModel();

            assertNotNull(comuna.toString());
        }
    }
}