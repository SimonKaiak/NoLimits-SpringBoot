package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.model.catalogos.GenerosModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GenerosModel Tests")
class GenerosModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear relación correctamente")
        void debeCrearRelacionCorrectamente() {

            ProductoModel producto = new ProductoModel();
            producto.setId(1L);

            GeneroModel genero = new GeneroModel();
            genero.setId(2L);

            GenerosModel model =
                    new GenerosModel(
                            100L,
                            producto,
                            genero
                    );

            assertEquals(100L, model.getId());
            assertEquals(producto, model.getProducto());
            assertEquals(genero, model.getGenero());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            ProductoModel producto = new ProductoModel();
            producto.setId(10L);

            GeneroModel genero = new GeneroModel();
            genero.setId(20L);

            GenerosModel model = new GenerosModel();

            model.setId(1L);
            model.setProducto(producto);
            model.setGenero(genero);

            assertEquals(1L, model.getId());
            assertEquals(producto, model.getProducto());
            assertEquals(genero, model.getGenero());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("mismo producto y género deben ser iguales")
        void mismoProductoYGeneroDebenSerIguales() {

            ProductoModel producto = new ProductoModel();
            producto.setId(1L);

            GeneroModel genero = new GeneroModel();
            genero.setId(2L);

            GenerosModel r1 =
                    new GenerosModel(
                            10L,
                            producto,
                            genero
                    );

            GenerosModel r2 =
                    new GenerosModel(
                            99L,
                            producto,
                            genero
                    );

            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        @DisplayName("relaciones distintas no deben ser iguales")
        void relacionesDistintasNoDebenSerIguales() {

            ProductoModel producto1 = new ProductoModel();
            producto1.setId(1L);

            ProductoModel producto2 = new ProductoModel();
            producto2.setId(2L);

            GeneroModel genero = new GeneroModel();
            genero.setId(3L);

            GenerosModel r1 =
                    new GenerosModel(
                            1L,
                            producto1,
                            genero
                    );

            GenerosModel r2 =
                    new GenerosModel(
                            1L,
                            producto2,
                            genero
                    );

            assertNotEquals(r1, r2);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("no debe retornar null")
        void noDebeRetornarNull() {

            GenerosModel model = new GenerosModel();

            assertNotNull(model.toString());
        }
    }
}