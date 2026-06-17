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
        @DisplayName("misma instancia retorna true")
        void mismaInstancia() {

            DetalleVentaModel detalle = new DetalleVentaModel();

            assertEquals(detalle, detalle);
        }

        @Test
        @DisplayName("comparación con null retorna false")
        void comparacionConNull() {

            DetalleVentaModel detalle = new DetalleVentaModel();

            assertNotEquals(null, detalle);
        }

        @Test
        @DisplayName("comparación con otra clase retorna false")
        void comparacionConOtraClase() {

            DetalleVentaModel detalle = new DetalleVentaModel();

            assertNotEquals("texto", detalle);
        }

        @Test
        @DisplayName("objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            DetalleVentaModel d2 = new DetalleVentaModel();

            assertEquals(d1, d2);
        }

        @Test
        @DisplayName("objetos vacíos tienen mismo hashCode")
        void objetosVaciosMismoHashCode() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            DetalleVentaModel d2 = new DetalleVentaModel();

            assertEquals(d1.hashCode(), d2.hashCode());
        }

        @Test
        @DisplayName("toString no debe ser null")
        void toStringNoDebeSerNull() {

            DetalleVentaModel detalle = new DetalleVentaModel();

            assertNotNull(detalle.toString());
        }

        @Test
        @DisplayName("diferente id")
        void diferenteId() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setId(1L);

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setId(2L);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente cantidad")
        void diferenteCantidad() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setCantidad(1);

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setCantidad(2);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente precioUnitario")
        void diferentePrecioUnitario() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setPrecioUnitario(1000f);

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setPrecioUnitario(2000f);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente venta")
        void diferenteVenta() {

            VentaModel v1 = new VentaModel();
            v1.setId(1L);

            VentaModel v2 = new VentaModel();
            v2.setId(2L);

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setVenta(v1);

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setVenta(v2);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente producto")
        void diferenteProducto() {

            ProductoModel p1 = new ProductoModel();
            p1.setId(1L);

            ProductoModel p2 = new ProductoModel();
            p2.setId(2L);

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setProducto(p1);

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setProducto(p2);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor id")
        void nullVsValorId() {

            DetalleVentaModel d1 = new DetalleVentaModel();

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setId(1L);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("valor vs null id")
        void valorVsNullId() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setId(1L);

            DetalleVentaModel d2 = new DetalleVentaModel();

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor cantidad")
        void nullVsValorCantidad() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setCantidad(null);

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setCantidad(1);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("valor vs null cantidad")
        void valorVsNullCantidad() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setCantidad(1);

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setCantidad(null);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor precio")
        void nullVsValorPrecio() {

            DetalleVentaModel d1 = new DetalleVentaModel();

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setPrecioUnitario(1000f);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("valor vs null precio")
        void valorVsNullPrecio() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setPrecioUnitario(1000f);

            DetalleVentaModel d2 = new DetalleVentaModel();

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor venta")
        void nullVsValorVenta() {

            VentaModel venta = new VentaModel();

            DetalleVentaModel d1 = new DetalleVentaModel();

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setVenta(venta);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("valor vs null venta")
        void valorVsNullVenta() {

            VentaModel venta = new VentaModel();

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setVenta(venta);

            DetalleVentaModel d2 = new DetalleVentaModel();

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor producto")
        void nullVsValorProducto() {

            ProductoModel producto = new ProductoModel();

            DetalleVentaModel d1 = new DetalleVentaModel();

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setProducto(producto);

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("valor vs null producto")
        void valorVsNullProducto() {

            ProductoModel producto = new ProductoModel();

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setProducto(producto);

            DetalleVentaModel d2 = new DetalleVentaModel();

            assertNotEquals(d1, d2);
        }
    }
}