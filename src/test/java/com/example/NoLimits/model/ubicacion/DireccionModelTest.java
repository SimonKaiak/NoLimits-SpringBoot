package com.example.NoLimits.model.ubicacion;

import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DireccionModel Tests")
class DireccionModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear direccion correctamente")
        void debeCrearDireccionCorrectamente() {

            ComunaModel comuna = new ComunaModel();
            UsuarioModel usuario = new UsuarioModel();

            DireccionModel direccion = new DireccionModel(
                    1L,
                    "Av. Siempre Viva",
                    "742",
                    "Depto 1204-B",
                    "8320000",
                    comuna,
                    true,
                    usuario
            );

            assertEquals(1L, direccion.getId());
            assertEquals("Av. Siempre Viva", direccion.getCalle());
            assertEquals("742", direccion.getNumero());
            assertEquals("Depto 1204-B", direccion.getComplemento());
            assertEquals("8320000", direccion.getCodigoPostal());
            assertEquals(comuna, direccion.getComuna());
            assertTrue(direccion.getActivo());
            assertEquals(usuario, direccion.getUsuarioModel());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            ComunaModel comuna = new ComunaModel();
            UsuarioModel usuario = new UsuarioModel();

            DireccionModel direccion = new DireccionModel();

            direccion.setId(10L);
            direccion.setCalle("Providencia");
            direccion.setNumero("123");
            direccion.setComplemento("Casa");
            direccion.setCodigoPostal("7500000");
            direccion.setComuna(comuna);
            direccion.setActivo(false);
            direccion.setUsuarioModel(usuario);

            assertEquals(10L, direccion.getId());
            assertEquals("Providencia", direccion.getCalle());
            assertEquals("123", direccion.getNumero());
            assertEquals("Casa", direccion.getComplemento());
            assertEquals("7500000", direccion.getCodigoPostal());
            assertEquals(comuna, direccion.getComuna());
            assertFalse(direccion.getActivo());
            assertEquals(usuario, direccion.getUsuarioModel());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("activo inicia en true")
        void activoIniciaEnTrue() {

            DireccionModel direccion = new DireccionModel();

            assertTrue(direccion.getActivo());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        @DisplayName("objetos con mismo contenido son iguales")
        void objetosConMismoContenidoSonIguales() {

            DireccionModel d1 = new DireccionModel();
            d1.setId(1L);

            DireccionModel d2 = new DireccionModel();
            d2.setId(1L);

            assertEquals(d1, d2);
            assertEquals(d1.hashCode(), d2.hashCode());
        }

        @Test
        @DisplayName("equals retorna true para misma instancia")
        void equalsMismaInstancia() {

            // Arrange
            DireccionModel direccion = new DireccionModel();

            // Act & Assert
            assertEquals(direccion, direccion);
        }

        @Test
        @DisplayName("equals retorna false con null")
        void equalsConNull() {

            // Arrange
            DireccionModel direccion = new DireccionModel();

            // Act & Assert
            assertNotEquals(null, direccion);
        }

        @Test
        @DisplayName("equals retorna false con otra clase")
        void equalsConOtraClase() {

            // Arrange
            DireccionModel direccion = new DireccionModel();

            // Act & Assert
            assertNotEquals("texto", direccion);
        }

        @Test
        @DisplayName("objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            // Arrange
            DireccionModel d1 = new DireccionModel();
            DireccionModel d2 = new DireccionModel();

            // Act & Assert
            assertEquals(d1, d2);
        }

        @Test
        @DisplayName("objetos vacíos generan mismo hash")
        void objetosVaciosMismoHash() {

            // Arrange
            DireccionModel d1 = new DireccionModel();
            DireccionModel d2 = new DireccionModel();

            // Act & Assert
            assertEquals(d1.hashCode(), d2.hashCode());
        }

        @Test
        @DisplayName("hashCode objeto vacío")
        void hashCodeObjetoVacio() {

            // Arrange
            DireccionModel direccion = new DireccionModel();

            // Act
            int hash = direccion.hashCode();

            // Assert
            assertNotEquals(0, hash);
        }

        @Test
        @DisplayName("equals detecta calle diferente")
        void equalsCalleDiferente() {

            // Arrange
            DireccionModel d1 = new DireccionModel();
            d1.setCalle("Providencia");

            DireccionModel d2 = new DireccionModel();
            d2.setCalle("Las Condes");

            // Act & Assert
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("equals detecta numero diferente")
        void equalsNumeroDiferente() {

            // Arrange
            DireccionModel d1 = new DireccionModel();
            d1.setNumero("100");

            DireccionModel d2 = new DireccionModel();
            d2.setNumero("200");

            // Act & Assert
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("equals detecta complemento diferente")
        void equalsComplementoDiferente() {

            // Arrange
            DireccionModel d1 = new DireccionModel();
            d1.setComplemento("Casa");

            DireccionModel d2 = new DireccionModel();
            d2.setComplemento("Depto");

            // Act & Assert
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("equals detecta codigo postal diferente")
        void equalsCodigoPostalDiferente() {

            // Arrange
            DireccionModel d1 = new DireccionModel();
            d1.setCodigoPostal("7500000");

            DireccionModel d2 = new DireccionModel();
            d2.setCodigoPostal("8320000");

            // Act & Assert
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("equals detecta activo diferente")
        void equalsActivoDiferente() {

            // Arrange
            DireccionModel d1 = new DireccionModel();
            d1.setActivo(true);

            DireccionModel d2 = new DireccionModel();
            d2.setActivo(false);

            // Act & Assert
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("equals detecta comuna diferente")
        void equalsComunaDiferente() {

            // Arrange
            DireccionModel d1 = new DireccionModel();
            d1.setComuna(new ComunaModel());

            DireccionModel d2 = new DireccionModel();

            // Act & Assert
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("equals detecta usuario diferente")
        void equalsUsuarioDiferente() {

            // Arrange
            DireccionModel d1 = new DireccionModel();
            d1.setUsuarioModel(new UsuarioModel());

            DireccionModel d2 = new DireccionModel();

            // Act & Assert
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("toString contiene calle")
        void toStringContieneCalle() {

            // Arrange
            DireccionModel direccion = new DireccionModel();
            direccion.setCalle("Providencia");

            // Act
            String resultado = direccion.toString();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("Providencia"));
        }

        @Test
        @DisplayName("toString no debe retornar null")
        void toStringNoDebeRetornarNull() {

            DireccionModel direccion = new DireccionModel();

            assertNotNull(direccion.toString());
        }
    }
}