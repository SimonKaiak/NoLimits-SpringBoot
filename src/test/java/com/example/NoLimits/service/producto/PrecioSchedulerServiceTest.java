package com.example.NoLimits.service.producto;

import com.example.NoLimits.Multimedia.service.producto.PrecioSchedulerService;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PrecioSchedulerServiceTest {

    @Mock
    private ProductoService productoService;

    private PrecioSchedulerService precioSchedulerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        precioSchedulerService =
                new PrecioSchedulerService(productoService);
    }

    @Nested
    @DisplayName("Unitario - actualizarPreciosSteamDiariamente")
    class ActualizarPreciosSteamDiariamente {

        @Test
        @DisplayName("actualiza los precios de todos los productos con appId")
        void actualizaTodosLosProductos() {

            when(productoService.obtenerIdsProductosConAppId())
                    .thenReturn(List.of(1L, 2L, 3L));

            precioSchedulerService.actualizarPreciosSteamDiariamente();

            verify(productoService).obtenerIdsProductosConAppId();

            verify(productoService).actualizarPrecioDesdeSteam(1L);
            verify(productoService).actualizarPrecioDesdeSteam(2L);
            verify(productoService).actualizarPrecioDesdeSteam(3L);
        }

        @Test
        @DisplayName("continua procesando cuando un producto genera error")
        void continuaCuandoUnProductoGeneraError() {

            when(productoService.obtenerIdsProductosConAppId())
                    .thenReturn(List.of(1L, 2L, 3L));

            doThrow(new RuntimeException("Error Steam"))
                    .when(productoService)
                    .actualizarPrecioDesdeSteam(2L);

            precioSchedulerService.actualizarPreciosSteamDiariamente();

            verify(productoService).actualizarPrecioDesdeSteam(1L);
            verify(productoService).actualizarPrecioDesdeSteam(2L);
            verify(productoService).actualizarPrecioDesdeSteam(3L);
        }

        @Test
        @DisplayName("no procesa productos cuando no existen appId")
        void noHaceNadaCuandoNoHayProductos() {

            when(productoService.obtenerIdsProductosConAppId())
                    .thenReturn(List.of());

            precioSchedulerService.actualizarPreciosSteamDiariamente();

            verify(productoService).obtenerIdsProductosConAppId();

            verify(productoService, never())
                    .actualizarPrecioDesdeSteam(anyLong());
        }
    }
}