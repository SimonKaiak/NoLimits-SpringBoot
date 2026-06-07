package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TiposDeDesarrolladorModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TiposDeDesarrolladorModel Tests")
class TiposDeDesarrolladorModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear relacion correctamente")
        void debeCrearRelacionCorrectamente() {

            DesarrolladorModel desarrollador =
                    new DesarrolladorModel();
            desarrollador.setId(1L);

            TipoDeDesarrolladorModel tipo =
                    new TipoDeDesarrolladorModel();
            tipo.setId(2L);

            TiposDeDesarrolladorModel model =
                    new TiposDeDesarrolladorModel(
                            100L,
                            desarrollador,
                            tipo
                    );

            assertEquals(100L, model.getId());
            assertEquals(desarrollador, model.getDesarrollador());
            assertEquals(tipo, model.getTipoDeDesarrollador());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            DesarrolladorModel desarrollador =
                    new DesarrolladorModel();

            TipoDeDesarrolladorModel tipo =
                    new TipoDeDesarrolladorModel();

            TiposDeDesarrolladorModel model =
                    new TiposDeDesarrolladorModel();

            model.setId(1L);
            model.setDesarrollador(desarrollador);
            model.setTipoDeDesarrollador(tipo);

            assertEquals(1L, model.getId());
            assertEquals(desarrollador, model.getDesarrollador());
            assertEquals(tipo, model.getTipoDeDesarrollador());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("objetos con mismo id deben ser iguales")
        void objetosConMismoIdDebenSerIguales() {

            TiposDeDesarrolladorModel r1 =
                    new TiposDeDesarrolladorModel();
            r1.setId(1L);

            TiposDeDesarrolladorModel r2 =
                    new TiposDeDesarrolladorModel();
            r2.setId(1L);

            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        @DisplayName("objetos con distinto id no deben ser iguales")
        void objetosConDistintoIdNoDebenSerIguales() {

            TiposDeDesarrolladorModel r1 =
                    new TiposDeDesarrolladorModel();
            r1.setId(1L);

            TiposDeDesarrolladorModel r2 =
                    new TiposDeDesarrolladorModel();
            r2.setId(2L);

            assertNotEquals(r1, r2);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("toString no debe retornar null")
        void toStringNoDebeRetornarNull() {

            TiposDeDesarrolladorModel model =
                    new TiposDeDesarrolladorModel();

            assertNotNull(model.toString());
        }
    }
}