package com.example.NoLimits.model.producto;

import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DetalleVentaModel Tests")
class DetalleVentaModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear detalle correctamente")
        void debeCrearDetalleCorrectamente() {

            VentaModel venta = new VentaModel();
            ProductoModel producto = new ProductoModel();

            DetalleVentaModel detalle =
                    new DetalleVentaModel(
                            1L,
                            venta,
                            producto,
                            2,
                            1000f
                    );

            assertEquals(1L, detalle.getId());
            assertEquals(venta, detalle.getVenta());
            assertEquals(producto, detalle.getProducto());
            assertEquals(2, detalle.getCantidad());
            assertEquals(1000f, detalle.getPrecioUnitario());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            VentaModel venta = new VentaModel();
            ProductoModel producto = new ProductoModel();

            DetalleVentaModel detalle = new DetalleVentaModel();

            detalle.setId(10L);
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(3);
            detalle.setPrecioUnitario(500f);

            assertEquals(10L, detalle.getId());
            assertEquals(venta, detalle.getVenta());
            assertEquals(producto, detalle.getProducto());
            assertEquals(3, detalle.getCantidad());
            assertEquals(500f, detalle.getPrecioUnitario());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("cantidad inicia en 1")
        void cantidadIniciaEnUno() {

            DetalleVentaModel detalle = new DetalleVentaModel();

            assertEquals(1, detalle.getCantidad());
        }
    }

    @Nested
    @DisplayName("Metodo getSubtotal")
    class GetSubtotal {

        @Test
        @DisplayName("retorna subtotal calculado")
        void retornaSubtotalCalculado() {

            DetalleVentaModel detalle = new DetalleVentaModel();

            detalle.setCantidad(2);
            detalle.setPrecioUnitario(1500f);

            assertEquals(3000f, detalle.getSubtotal());
        }

        @Test
        @DisplayName("retorna cero cuando precio es null")
        void retornaCeroCuandoPrecioEsNull() {

            DetalleVentaModel detalle = new DetalleVentaModel();

            detalle.setCantidad(2);
            detalle.setPrecioUnitario(null);

            assertEquals(0f, detalle.getSubtotal());
        }

        @Test
        @DisplayName("retorna cero cuando cantidad es null")
        void retornaCeroCuandoCantidadEsNull() {

            DetalleVentaModel detalle = new DetalleVentaModel();

            detalle.setCantidad(null);
            detalle.setPrecioUnitario(1000f);

            assertEquals(0f, detalle.getSubtotal());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        @DisplayName("objetos con mismo contenido son iguales")
        void objetosConMismoContenidoSonIguales() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setId(1L);

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setId(1L);

            assertEquals(d1, d2);
            assertEquals(d1.hashCode(), d2.hashCode());
        }

        @Test
        @DisplayName("toString no debe ser null")
        void toStringNoDebeSerNull() {

            DetalleVentaModel detalle = new DetalleVentaModel();

            assertNotNull(detalle.toString());
        }
    }
}