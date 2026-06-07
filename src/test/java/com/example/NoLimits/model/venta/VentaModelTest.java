package com.example.NoLimits.model.venta;

import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VentaModel Tests")
class VentaModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        void debeCrearVentaCorrectamente() {

            UsuarioModel usuario = new UsuarioModel();
            MetodoPagoModel metodoPago = new MetodoPagoModel();
            MetodoEnvioModel metodoEnvio = new MetodoEnvioModel();
            EstadoModel estado = new EstadoModel();
            List<DetalleVentaModel> detalles = new ArrayList<>();

            VentaModel venta = new VentaModel(
                    1L,
                    LocalDate.of(2025, 7, 6),
                    LocalTime.of(14, 30),
                    usuario,
                    metodoPago,
                    metodoEnvio,
                    estado,
                    detalles
            );

            assertEquals(1L, venta.getId());
            assertEquals(LocalDate.of(2025, 7, 6), venta.getFechaCompra());
            assertEquals(LocalTime.of(14, 30), venta.getHoraCompra());
            assertEquals(usuario, venta.getUsuarioModel());
            assertEquals(metodoPago, venta.getMetodoPagoModel());
            assertEquals(metodoEnvio, venta.getMetodoEnvioModel());
            assertEquals(estado, venta.getEstado());
            assertEquals(detalles, venta.getDetalles());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        void debeAsignarYObtenerPropiedades() {

            VentaModel venta = new VentaModel();

            venta.setId(10L);
            venta.setFechaCompra(LocalDate.now());
            venta.setHoraCompra(LocalTime.now());

            assertEquals(10L, venta.getId());
            assertNotNull(venta.getFechaCompra());
            assertNotNull(venta.getHoraCompra());
        }
    }

    @Nested
    @DisplayName("Total Venta")
    class TotalVenta {

        @Test
        void retornaCeroCuandoDetallesEsNull() {

            VentaModel venta = new VentaModel();
            venta.setDetalles(null);

            assertEquals(0f, venta.getTotalVenta());
        }

        @Test
        void retornaCeroCuandoDetallesEstaVacio() {

            VentaModel venta = new VentaModel();
            venta.setDetalles(new ArrayList<>());

            assertEquals(0f, venta.getTotalVenta());
        }

        @Test
        void calculaTotalCorrectamente() {

            DetalleVentaModel d1 = new DetalleVentaModel();
            d1.setCantidad(2);
            d1.setPrecioUnitario(1000f);

            DetalleVentaModel d2 = new DetalleVentaModel();
            d2.setCantidad(3);
            d2.setPrecioUnitario(500f);

            List<DetalleVentaModel> detalles = List.of(d1, d2);

            VentaModel venta = new VentaModel();
            venta.setDetalles(detalles);

            assertEquals(3500f, venta.getTotalVenta());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        void objetosConMismoContenidoSonIguales() {

            VentaModel v1 = new VentaModel();
            v1.setId(1L);

            VentaModel v2 = new VentaModel();
            v2.setId(1L);

            assertEquals(v1, v2);
            assertEquals(v1.hashCode(), v2.hashCode());
        }

        @Test
        void toStringNoDebeSerNull() {

            VentaModel venta = new VentaModel();

            assertNotNull(venta.toString());
        }
    }
}