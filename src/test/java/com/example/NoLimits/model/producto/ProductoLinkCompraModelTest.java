package com.example.NoLimits.model.producto;

import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoLinkCompraModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoLinkCompraModel Tests")
class ProductoLinkCompraModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear link de compra correctamente")
        void debeCrearLinkDeCompraCorrectamente() {

            ProductoModel producto = new ProductoModel();
            producto.setId(1L);

            PlataformaModel plataforma = new PlataformaModel();
            plataforma.setId(2L);

            LocalDateTime fecha = LocalDateTime.now();

            ProductoLinkCompraModel model =
                    new ProductoLinkCompraModel(
                            10L,
                            producto,
                            plataforma,
                            "https://store.com/game",
                            "Comprar",
                            "Steam",
                            "12345",
                            19990.0,
                            "$19.990",
                            "CLP",
                            fecha
                    );

            assertEquals(10L, model.getId());
            assertEquals(producto, model.getProducto());
            assertEquals(plataforma, model.getPlataforma());
            assertEquals("https://store.com/game", model.getUrl());
            assertEquals("Comprar", model.getLabel());
            assertEquals("Steam", model.getNombrePlataforma());
            assertEquals("12345", model.getAppId());
            assertEquals(19990.0, model.getPrecioActual());
            assertEquals("$19.990", model.getPrecioFormato());
            assertEquals("CLP", model.getMoneda());
            assertEquals(fecha, model.getFechaUltimaActualizacion());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            ProductoModel producto = new ProductoModel();
            PlataformaModel plataforma = new PlataformaModel();
            LocalDateTime fecha = LocalDateTime.now();

            ProductoLinkCompraModel model = new ProductoLinkCompraModel();

            model.setId(1L);
            model.setProducto(producto);
            model.setPlataforma(plataforma);
            model.setUrl("https://test.com");
            model.setLabel("Ver");
            model.setNombrePlataforma("Epic");
            model.setAppId("999");
            model.setPrecioActual(5000.0);
            model.setPrecioFormato("$5.000");
            model.setMoneda("CLP");
            model.setFechaUltimaActualizacion(fecha);

            assertEquals(1L, model.getId());
            assertEquals(producto, model.getProducto());
            assertEquals(plataforma, model.getPlataforma());
            assertEquals("https://test.com", model.getUrl());
            assertEquals("Ver", model.getLabel());
            assertEquals("Epic", model.getNombrePlataforma());
            assertEquals("999", model.getAppId());
            assertEquals(5000.0, model.getPrecioActual());
            assertEquals("$5.000", model.getPrecioFormato());
            assertEquals("CLP", model.getMoneda());
            assertEquals(fecha, model.getFechaUltimaActualizacion());
        }
    }

    @Nested
    @DisplayName("Constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("debe crear objeto vacío")
        void debeCrearObjetoVacio() {

            ProductoLinkCompraModel model =
                    new ProductoLinkCompraModel();

            assertNull(model.getId());
            assertNull(model.getProducto());
            assertNull(model.getPlataforma());
            assertNull(model.getUrl());
            assertNull(model.getLabel());
            assertNull(model.getNombrePlataforma());
            assertNull(model.getAppId());
            assertNull(model.getPrecioActual());
            assertNull(model.getPrecioFormato());
            assertNull(model.getMoneda());
            assertNull(model.getFechaUltimaActualizacion());
        }
    }
}