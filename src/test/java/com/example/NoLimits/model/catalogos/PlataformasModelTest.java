package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformasModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlataformasModel Tests")
class PlataformasModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear relación correctamente")
        void debeCrearRelacionCorrectamente() {

            ProductoModel producto = new ProductoModel();
            producto.setId(1L);

            PlataformaModel plataforma = new PlataformaModel();
            plataforma.setId(2L);

            PlataformasModel model =
                    new PlataformasModel(
                            100L,
                            producto,
                            plataforma
                    );

            assertEquals(100L, model.getId());
            assertEquals(producto, model.getProducto());
            assertEquals(plataforma, model.getPlataforma());
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

            PlataformaModel plataforma = new PlataformaModel();
            plataforma.setId(20L);

            PlataformasModel model = new PlataformasModel();

            model.setId(1L);
            model.setProducto(producto);
            model.setPlataforma(plataforma);

            assertEquals(1L, model.getId());
            assertEquals(producto, model.getProducto());
            assertEquals(plataforma, model.getPlataforma());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("mismo producto y plataforma deben ser iguales")
        void mismoProductoYPlataformaDebenSerIguales() {

            ProductoModel producto = new ProductoModel();
            producto.setId(1L);

            PlataformaModel plataforma = new PlataformaModel();
            plataforma.setId(2L);

            PlataformasModel r1 =
                    new PlataformasModel(
                            10L,
                            producto,
                            plataforma
                    );

            PlataformasModel r2 =
                    new PlataformasModel(
                            99L,
                            producto,
                            plataforma
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

            PlataformaModel plataforma = new PlataformaModel();
            plataforma.setId(3L);

            PlataformasModel r1 =
                    new PlataformasModel(
                            1L,
                            producto1,
                            plataforma
                    );

            PlataformasModel r2 =
                    new PlataformasModel(
                            1L,
                            producto2,
                            plataforma
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

            PlataformasModel model = new PlataformasModel();

            assertNotNull(model.toString());
        }
    }
}