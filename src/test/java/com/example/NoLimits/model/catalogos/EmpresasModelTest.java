package com.example.NoLimits.model.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresasModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmpresasModel Tests")
class EmpresasModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear relación correctamente")
        void debeCrearRelacionCorrectamente() {

            ProductoModel producto = new ProductoModel();
            producto.setId(1L);

            EmpresaModel empresa = new EmpresaModel();
            empresa.setId(2L);

            EmpresasModel model =
                    new EmpresasModel(
                            100L,
                            producto,
                            empresa
                    );

            assertEquals(100L, model.getId());
            assertEquals(producto, model.getProducto());
            assertEquals(empresa, model.getEmpresa());
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

            EmpresaModel empresa = new EmpresaModel();
            empresa.setId(20L);

            EmpresasModel model = new EmpresasModel();

            model.setId(1L);
            model.setProducto(producto);
            model.setEmpresa(empresa);

            assertEquals(1L, model.getId());
            assertEquals(producto, model.getProducto());
            assertEquals(empresa, model.getEmpresa());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("mismo producto y empresa deben ser iguales")
        void mismoProductoYEmpresaDebenSerIguales() {

            ProductoModel producto = new ProductoModel();
            producto.setId(1L);

            EmpresaModel empresa = new EmpresaModel();
            empresa.setId(2L);

            EmpresasModel r1 =
                    new EmpresasModel(
                            10L,
                            producto,
                            empresa
                    );

            EmpresasModel r2 =
                    new EmpresasModel(
                            99L,
                            producto,
                            empresa
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

            EmpresaModel empresa = new EmpresaModel();
            empresa.setId(3L);

            EmpresasModel r1 =
                    new EmpresasModel(
                            1L,
                            producto1,
                            empresa
                    );

            EmpresasModel r2 =
                    new EmpresasModel(
                            1L,
                            producto2,
                            empresa
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

            EmpresasModel model = new EmpresasModel();

            assertNotNull(model.toString());
        }
    }
}