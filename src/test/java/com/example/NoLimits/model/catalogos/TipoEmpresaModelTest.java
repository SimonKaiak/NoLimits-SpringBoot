package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoEmpresaModel Tests")
class TipoEmpresaModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear tipo de empresa correctamente")
        void debeCrearTipoDeEmpresaCorrectamente() {

            TipoEmpresaModel model =
                    new TipoEmpresaModel(
                            1L,
                            "Publisher",
                            null
                    );

            assertEquals(1L, model.getId());
            assertEquals("Publisher", model.getNombre());
            assertNull(model.getEmpresas());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            TipoEmpresaModel model =
                    new TipoEmpresaModel();

            model.setId(10L);
            model.setNombre("Distribuidora");

            assertEquals(10L, model.getId());
            assertEquals("Distribuidora", model.getNombre());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("objetos con mismo contenido son iguales")
        void objetosConMismoContenidoSonIguales() {

            TipoEmpresaModel t1 =
                    new TipoEmpresaModel();
            t1.setId(1L);
            t1.setNombre("Publisher");

            TipoEmpresaModel t2 =
                    new TipoEmpresaModel();
            t2.setId(1L);
            t2.setNombre("Publisher");

            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());
        }

        @Test
        @DisplayName("objetos con distinto id no son iguales")
        void objetosConDistintoIdNoSonIguales() {

            TipoEmpresaModel t1 =
                    new TipoEmpresaModel();
            t1.setId(1L);

            TipoEmpresaModel t2 =
                    new TipoEmpresaModel();
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

            TipoEmpresaModel model =
                    new TipoEmpresaModel();

            assertNotNull(model.toString());
        }
    }
}