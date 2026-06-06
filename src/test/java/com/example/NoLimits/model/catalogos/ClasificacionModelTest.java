package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClasificacionModelTest {

    @Nested
    @DisplayName("Constructor personalizado")
    class ConstructorPersonalizado {

        @Test
        @DisplayName("crea correctamente una clasificación")
        void testConstructorPersonalizado() {

            ClasificacionModel model =
                    new ClasificacionModel(
                            1L,
                            "T",
                            "Contenido apto para adolescentes",
                            true
                    );

            assertEquals(1L, model.getId());
            assertEquals("T", model.getNombre());
            assertEquals("Contenido apto para adolescentes", model.getDescripcion());
            assertTrue(model.isActivo());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            ClasificacionModel model = new ClasificacionModel();

            model.setId(10L);
            model.setNombre("M");
            model.setDescripcion("Mayores de edad");
            model.setActivo(false);

            assertEquals(10L, model.getId());
            assertEquals("M", model.getNombre());
            assertEquals("Mayores de edad", model.getDescripcion());
            assertFalse(model.isActivo());
        }
    }

    @Nested
    @DisplayName("Valor por defecto")
    class ValorPorDefecto {

        @Test
        @DisplayName("activo inicia en true")
        void testActivoPorDefecto() {

            ClasificacionModel model = new ClasificacionModel();

            assertTrue(model.isActivo());
        }
    }
}