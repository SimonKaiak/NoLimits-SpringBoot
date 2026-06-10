package com.example.NoLimits.controller.producto;

import com.example.NoLimits.Multimedia.controller.producto.ProductoController;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResumenDTO;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Producto Controller Tests")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Nested
    @DisplayName("Listado de Productos")
    class ListadoProductosTests {

        @Test
        @DisplayName("Debe listar productos correctamente")
        void debeListarProductos() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                when(productoService.findAll())
                        .thenReturn(List.of(producto));

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 si no hay productos")
        void debeRetornarNoContentSiNoHayProductos() throws Exception {

                // Arrange
                when(productoService.findAll())
                        .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos"))
                        .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe listar productos paginados")
        void debeListarProductosPaginados() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findAllPaged(1, 20))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/paginacion"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.pagina").value(1))
                        .andExpect(jsonPath("$.totalPaginas").value(1))
                        .andExpect(jsonPath("$.totalElementos").value(1));
        }

        @Test
        @DisplayName("Debe corregir page y size inválidos al listar productos paginados")
        void debeCorregirPageYSizeInvalidos() throws Exception {

                // Arrange
                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(), 1, 0, 0);

                when(productoService.findAllPaged(1, 20))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/paginacion")
                                .param("page", "0")
                                .param("size", "100"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe corregir size menor a 1 al listar productos paginados")
        void debeCorregirSizeMenorAUnoListarProductosPaginados() throws Exception {

                // Arrange
                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(), 1, 0, 0);

                when(productoService.findAllPaged(1, 20))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/paginacion")
                                .param("size", "0"))
                        .andExpect(status().isOk());
        }

    }

    @Nested
    @DisplayName("Busqueda por ID")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Debe buscar producto por ID")
        void debeBuscarProductoPorId() throws Exception {

                // Arrange
                ProductoResponseDTO producto = new ProductoResponseDTO();

                when(productoService.findById(1L))
                        .thenReturn(producto);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/1"))
                        .andExpect(status().isOk());
        }

    }

    @Nested
    @DisplayName("Crear Producto")
    class CrearProductoTests {

         @Test
        @DisplayName("Debe crear producto")
        void debeCrearProducto() throws Exception {

                // Arrange
                ProductoResponseDTO producto = new ProductoResponseDTO();

                when(productoService.save(any()))
                        .thenReturn(producto);

                // Act & Assert
                mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                        .andExpect(status().isCreated());
        }

    }

    @Nested
    @DisplayName("Actualizar Producto")
    class ActualizarProductoTests {


        @Test
        @DisplayName("Debe actualizar producto con PUT")
        void debeActualizarProductoConPut() throws Exception {

                // Arrange
                ProductoResponseDTO producto = new ProductoResponseDTO();

                when(productoService.update(eq(1L), any()))
                        .thenReturn(producto);

                // Act & Assert
                mockMvc.perform(put("/api/v1/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe editar producto con PATCH")
        void debeEditarProductoConPatch() throws Exception {

                // Arrange
                ProductoResponseDTO producto = new ProductoResponseDTO();

                when(productoService.patch(eq(1L), any()))
                        .thenReturn(producto);

                // Act & Assert
                mockMvc.perform(patch("/api/v1/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                        .andExpect(status().isOk());
        }

    }

    // =========================
    // ELIMINAR PRODUCTO
    // =========================

    @Nested
    @DisplayName("Eliminar Producto")
    class EliminarProductoTests {

         @Test
        @DisplayName("Debe eliminar producto")
        void debeEliminarProducto() throws Exception {

                // Arrange
                doNothing().when(productoService)
                        .deleteById(1L);

                // Act & Assert
                mockMvc.perform(delete("/api/v1/productos/1"))
                        .andExpect(status().isNoContent());
        }

    }

    // =========================
    // PRECIO STEAM
    // =========================

    @Nested
    @DisplayName("Precio Steam")
    class PrecioSteamTests {

         @Test
        @DisplayName("Debe actualizar precio desde Steam")
        void debeActualizarPrecioDesdeSteam() throws Exception {

                // Arrange
                ProductoResponseDTO producto = new ProductoResponseDTO();

                when(productoService.actualizarPrecioDesdeSteam(1L))
                        .thenReturn(producto);

                // Act & Assert
                mockMvc.perform(
                        patch("/api/v1/productos/1/actualizar-precio-steam"))
                        .andExpect(status().isOk());
        }
    }

    // =========================
    // BUSQUEDAS
    // =========================

    @Nested
    @DisplayName("Busqueda de Productos")
    class BusquedaProductosTests {

        @Test
        @DisplayName("Debe buscar productos por nombre")
        void debeBuscarProductosPorNombre() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findByNombreContainingIgnoreCase(
                        eq("spider"), eq(1), eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/buscar")
                        .param("nombre", "spider"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe corregir size menor a 1 al buscar por nombre")
        void debeCorregirSizeMenorAUnoBuscarPorNombre() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findByNombreContainingIgnoreCase(
                        eq("spider"),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/buscar")
                                .param("nombre", "spider")
                                .param("size", "0"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe corregir size mayor a 50 al buscar por nombre")
        void debeCorregirSizeMayorA50BuscarPorNombre() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findByNombreContainingIgnoreCase(
                        eq("spider"),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/buscar")
                                .param("nombre", "spider")
                                .param("size", "100"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando no encuentra productos por nombre")
        void debeRetornarNoContentCuandoNoEncuentraPorNombre() throws Exception {

                // Arrange
                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(), 1, 0, 0);

                when(productoService.findByNombreContainingIgnoreCase(
                        eq("inexistente"), eq(1), eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/buscar")
                        .param("nombre", "inexistente"))
                        .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe buscar productos por tipo")
        void debeBuscarProductosPorTipo() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findByTipoProducto(1L, 1, 20))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/tipo/1"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe corregir size mayor a 50 al buscar por tipo")
        void debeCorregirSizeMayorA50BuscarPorTipo() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findByTipoProducto(
                        eq(1L),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/tipo/1")
                                .param("size", "100"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe corregir page a 1 al buscar productos por tipo")
        void debeCorregirPageABuscarProductosPorTipo() throws Exception {

                // Arrange
                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(), 1, 0, 0);

                when(productoService.findByTipoProducto(
                        eq(1L),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/tipo/1")
                                .param("page", "0"))
                        .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe corregir size menor a 1 al buscar por tipo")
        void debeCorregirSizeMenorAUnoBuscarPorTipo() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findByTipoProducto(
                        eq(1L),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/tipo/1")
                                .param("size", "0"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe buscar productos por estado")
        void debeBuscarProductosPorEstado() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findByEstado(1L, 1, 20))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/estado/1"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe corregir size mayor a 50 al buscar por estado")
        void debeCorregirSizeMayorA50BuscarPorEstado() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findByEstado(
                        eq(1L),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/estado/1")
                                .param("size", "100"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe corregir page a 1 al buscar productos por estado")
        void debeCorregirPageABuscarProductosPorEstado() throws Exception {

                // Arrange
                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(), 1, 0, 0);

                when(productoService.findByEstado(
                        eq(1L),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/estado/1")
                                .param("page", "0"))
                        .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe corregir size menor a 1 al buscar por estado")
        void debeCorregirSizeMenorAUnoBuscarPorEstado() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findByEstado(
                        eq(1L),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/estado/1")
                                .param("size", "0"))
                        .andExpect(status().isOk());
        }

    }

    // =========================
    // SAGAS
    // =========================

    @Nested
    @DisplayName("Sagas")
    class SagaTests {

        @Test
        @DisplayName("Debe listar sagas")
        void debeListarSagas() throws Exception {

                // Arrange
                when(productoService.obtenerSagasDistinct())
                        .thenReturn(List.of("Spider-Man"));

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0]").value("Spider-Man"));
        }

        @Test
        @DisplayName("Debe retornar 204 si no hay sagas")
        void debeRetornarNoContentSiNoHaySagas() throws Exception {

                // Arrange
                when(productoService.obtenerSagasDistinct())
                        .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas"))
                        .andExpect(status().isNoContent());
        }

         @Test
        @DisplayName("Debe buscar productos por saga")
        void debeBuscarProductosPorSaga() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findBySagaIgnoreCase(
                        "Spider-Man", 1, 20))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas/Spider-Man"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe corregir size mayor a 50 al buscar por saga")
        void debeCorregirSizeMayorA50BuscarPorSaga() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findBySagaIgnoreCase(
                        eq("Spider-Man"),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas/Spider-Man")
                                .param("size", "100"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe corregir page a 1 al buscar productos por saga")
        void debeCorregirPageABuscarProductosPorSaga() throws Exception {

                // Arrange
                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(), 1, 0, 0);

                when(productoService.findBySagaIgnoreCase(
                        eq("Spider-Man"),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas/Spider-Man")
                                .param("page", "0"))
                        .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe corregir size menor a 1 al buscar por saga")
        void debeCorregirSizeMenorAUnoBuscarPorSaga() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findBySagaIgnoreCase(
                        eq("Spider-Man"),
                        eq(1),
                        eq(20)))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas/Spider-Man")
                                .param("size", "0"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe listar sagas con portada")
        void debeListarSagasConPortada() throws Exception {

                // Arrange
                when(productoService.obtenerSagasConPortada())
                        .thenReturn(List.of(
                                Map.of("saga", "Spider-Man")));

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas/resumen"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando no existen sagas con portada")
        void debeRetornarNoContentCuandoNoHaySagasConPortada() throws Exception {

                // Arrange
                when(productoService.obtenerSagasConPortada())
                        .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas/resumen"))
                        .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe listar sagas por tipo de producto")
        void debeListarSagasPorTipoProducto() throws Exception {

                // Arrange
                when(productoService
                        .obtenerSagasDistinctPorTipoProducto(1L))
                        .thenReturn(List.of("Spider-Man"));

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas/tipo/1"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando no existen sagas para el tipo")
        void debeRetornarNoContentCuandoNoHaySagasPorTipo() throws Exception {

                // Arrange
                when(productoService.obtenerSagasDistinctPorTipoProducto(1L))
                        .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/sagas/tipo/1"))
                        .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe obtener productos completos de una saga")
        void debeObtenerProductosCompletosDeSaga() throws Exception {

                // Arrange
                ProductoResponseDTO producto = new ProductoResponseDTO();

                when(productoService.findBySagaCompleto("Spider-Man"))
                        .thenReturn(List.of(producto));

                // Act & Assert
                mockMvc.perform(
                        get("/api/v1/productos/sagas/Spider-Man/completo"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando la saga no tiene productos")
        void debeRetornarNoContentCuandoSagaNoTieneProductos() throws Exception {

                // Arrange
                when(productoService.findBySagaCompleto("Spider-Man"))
                        .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(
                        get("/api/v1/productos/sagas/Spider-Man/completo"))
                        .andExpect(status().isNoContent());
        }

    }
    //==========================
    // ALIAS DE SAGA
    //==========================

    @Nested 
    @DisplayName("Alias de Saga")
    class SagaAliasTests {

        @Test
        @DisplayName("Debe buscar productos por saga usando alias")
        void debeBuscarProductosPorSagaAlias() throws Exception {

                // Arrange
                ProductoResumenDTO producto = new ProductoResumenDTO();

                PagedResponse<ProductoResumenDTO> respuesta =
                        new PagedResponse<>(List.of(producto), 1, 1, 1);

                when(productoService.findBySagaIgnoreCase(
                        "Spider-Man", 1, 20))
                        .thenReturn(respuesta);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/saga/Spider-Man"))
                        .andExpect(status().isOk());
        }

    }

    // =========================
    // RESUMEN
    // =========================

    @Nested
    @DisplayName("Resumen de Productos")
    class ResumenProductosTests {

         @Test
        @DisplayName("Debe obtener resumen de productos")
        void debeObtenerResumenProductos() throws Exception {

                // Arrange
                when(productoService.obtenerProductosConDatos())
                        .thenReturn(List.of(
                        Map.of("id", 1)));

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/resumen"))
                        .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando no existen productos en el resumen")
        void debeRetornarNoContentCuandoResumenEstaVacio() throws Exception {

                // Arrange
                when(productoService.obtenerProductosConDatos())
                        .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/resumen"))
                        .andExpect(status().isNoContent());
        }

    }

    // =========================
    // LINKS
    // =========================

    @Nested
    @DisplayName("Verificacion de Links")
    class VerificacionLinkTests {

        @Test
        @DisplayName("Debe verificar si existe producto por link")
        void debeVerificarSiExisteProductoPorLink() throws Exception {

                // Arrange
                when(productoService.existeProductoPorLinkCompra(
                        "https://mercadolibre.cl/producto"))
                        .thenReturn(true);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/existe-link")
                        .param("url", "https://mercadolibre.cl/producto"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("true"));
        }

        @Test
        @DisplayName("Debe retornar false cuando el producto no existe por link")
        void debeRetornarFalseCuandoNoExisteProductoPorLink() throws Exception {

                // Arrange
                when(productoService.existeProductoPorLinkCompra(
                        "https://mercadolibre.cl/inexistente"))
                        .thenReturn(false);

                // Act & Assert
                mockMvc.perform(get("/api/v1/productos/existe-link")
                                .param("url",
                                        "https://mercadolibre.cl/inexistente"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("false"));
        }

    }
}
