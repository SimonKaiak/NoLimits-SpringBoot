package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoDeDesarrolladorModel Tests")
class TipoDeDesarrolladorModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear tipo de desarrollador correctamente")
        void debeCrearTipoDeDesarrolladorCorrectamente() {

            TipoDeDesarrolladorModel model =
                    new TipoDeDesarrolladorModel(
                            1L,
                            "Estudio",
                            null
                    );

            assertEquals(1L, model.getId());
            assertEquals("Estudio", model.getNombre());
            assertNull(model.getDesarrolladores());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            TipoDeDesarrolladorModel model =
                    new TipoDeDesarrolladorModel();

            model.setId(10L);
            model.setNombre("Publisher");

            assertEquals(10L, model.getId());
            assertEquals("Publisher", model.getNombre());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("objetos con mismo contenido son iguales")
        void objetosConMismoContenidoSonIguales() {

            TipoDeDesarrolladorModel t1 =
                    new TipoDeDesarrolladorModel();
            t1.setId(1L);
            t1.setNombre("Estudio");

            TipoDeDesarrolladorModel t2 =
                    new TipoDeDesarrolladorModel();
            t2.setId(1L);
            t2.setNombre("Estudio");

            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());
        }

        @Test
        @DisplayName("objetos con distinto id no son iguales")
        void objetosConDistintoIdNoSonIguales() {

            TipoDeDesarrolladorModel t1 =
                    new TipoDeDesarrolladorModel();
            t1.setId(1L);

            TipoDeDesarrolladorModel t2 =
                    new TipoDeDesarrolladorModel();
            t2.setId(2L);

            assertNotEquals(t1, t2);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("toString no debe ser null")
        void toStringNoDebeSerNull() {

            TipoDeDesarrolladorModel model =
                    new TipoDeDesarrolladorModel();

            assertNotNull(model.toString());
        }
    }
}