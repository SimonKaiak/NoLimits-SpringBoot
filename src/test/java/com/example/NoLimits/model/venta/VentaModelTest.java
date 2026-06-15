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

    private VentaModel crearVentaBase() {

        VentaModel venta = new VentaModel();

        venta.setId(1L);
        venta.setFechaCompra(LocalDate.of(2025, 7, 6));
        venta.setHoraCompra(LocalTime.of(14, 30));

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(1L);

        MetodoPagoModel pago = new MetodoPagoModel();
        pago.setId(1L);

        MetodoEnvioModel envio = new MetodoEnvioModel();
        envio.setId(1L);

        EstadoModel estado = new EstadoModel();
        estado.setId(1L);

        venta.setUsuarioModel(usuario);
        venta.setMetodoPagoModel(pago);
        venta.setMetodoEnvioModel(envio);
        venta.setEstado(estado);
        venta.setDetalles(new ArrayList<>());

        return venta;
    }

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

        @Test
        @DisplayName("Debe asignar todas las relaciones")
        void debeAsignarTodasLasRelaciones() {

            VentaModel venta = new VentaModel();

            UsuarioModel usuario = new UsuarioModel();
            MetodoPagoModel pago = new MetodoPagoModel();
            MetodoEnvioModel envio = new MetodoEnvioModel();
            EstadoModel estado = new EstadoModel();
            List<DetalleVentaModel> detalles = new ArrayList<>();

            venta.setUsuarioModel(usuario);
            venta.setMetodoPagoModel(pago);
            venta.setMetodoEnvioModel(envio);
            venta.setEstado(estado);
            venta.setDetalles(detalles);

            assertEquals(usuario, venta.getUsuarioModel());
            assertEquals(pago, venta.getMetodoPagoModel());
            assertEquals(envio, venta.getMetodoEnvioModel());
            assertEquals(estado, venta.getEstado());
            assertEquals(detalles, venta.getDetalles());
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
    @DisplayName("Equals, HashCode y ToString")
    class EqualsHashCodeToString {

       
        @Test
        @DisplayName("Equals mismo objeto")
        void equalsMismoObjeto() {

            VentaModel venta = crearVentaBase();

            assertEquals(venta, venta);
        }


        @Test
        @DisplayName("Objetos iguales")
        void objetosIguales() {

            VentaModel v1 = crearVentaBase();
            VentaModel v2 = crearVentaBase();

            assertEquals(v1, v2);
            assertEquals(v1.hashCode(), v2.hashCode());
        }

        @Test
        @DisplayName("Distinto id")
        void distintoId() {

            VentaModel v1 = crearVentaBase();

            VentaModel v2 = crearVentaBase();
            v2.setId(99L);

            assertNotEquals(v1, v2);
        }

        @Test
        @DisplayName("Distinta fecha")
        void distintaFecha() {

            VentaModel v1 = crearVentaBase();

            VentaModel v2 = crearVentaBase();
            v2.setFechaCompra(LocalDate.of(2026, 1, 1));

            assertNotEquals(v1, v2);
        }

        @Test
        @DisplayName("Distinta hora")
        void distintaHora() {

            VentaModel v1 = crearVentaBase();

            VentaModel v2 = crearVentaBase();
            v2.setHoraCompra(LocalTime.of(10, 0));

            assertNotEquals(v1, v2);
        }

        @Test
        @DisplayName("Distinto usuario")
        void distintoUsuario() {

            VentaModel v1 = crearVentaBase();

            VentaModel v2 = crearVentaBase();

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(99L);

            v2.setUsuarioModel(usuario);

            assertNotEquals(v1, v2);
        }

                @Test
        @DisplayName("Distinto metodo pago")
        void distintoMetodoPago() {

            VentaModel v1 = crearVentaBase();

            VentaModel v2 = crearVentaBase();

            MetodoPagoModel metodo = new MetodoPagoModel();
            metodo.setId(99L);

            v2.setMetodoPagoModel(metodo);

            assertNotEquals(v1, v2);
        }

        @Test
        @DisplayName("Distinto metodo envio")
        void distintoMetodoEnvio() {

            VentaModel v1 = crearVentaBase();

            VentaModel v2 = crearVentaBase();

            MetodoEnvioModel metodo = new MetodoEnvioModel();
            metodo.setId(99L);

            v2.setMetodoEnvioModel(metodo);

            assertNotEquals(v1, v2);
        }

        @Test
        @DisplayName("Distinto estado")
        void distintoEstado() {

            VentaModel v1 = crearVentaBase();

            VentaModel v2 = crearVentaBase();

            EstadoModel estado = new EstadoModel();
            estado.setId(99L);

            v2.setEstado(estado);

            assertNotEquals(v1, v2);
        }

        @Test
        @DisplayName("Distinta lista de detalles")
        void distintaListaDetalles() {

            VentaModel v1 = crearVentaBase();

            VentaModel v2 = crearVentaBase();
            v2.setDetalles(List.of(new DetalleVentaModel()));

            assertNotEquals(v1, v2);
        }

        @Test
        @DisplayName("HashCode consistente")
        void hashCodeConsistente() {

            VentaModel venta = crearVentaBase();

            int hash1 = venta.hashCode();
            int hash2 = venta.hashCode();

            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Equals con null retorna false")
        void equalsRetornaFalseConNull() {

            VentaModel venta = crearVentaBase();

            assertNotEquals(null, venta);
        }

        @Test
        @DisplayName("Equals con clase distinta retorna false")
        void equalsRetornaFalseConObjetoDeOtraClase() {

            VentaModel venta = crearVentaBase();

            assertNotEquals("texto", venta);
        }

        @Test
        @DisplayName("ToString contiene nombre clase")
        void toStringContieneNombreClase() {

            VentaModel venta = crearVentaBase();

            String resultado = venta.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("VentaModel"));
        }
    }
    
}