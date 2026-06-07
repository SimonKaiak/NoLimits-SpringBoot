package com.example.NoLimits.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.producto.request.ProductoRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResumenDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ProductoUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.ClasificacionRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.GeneroRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoProductoRepository;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.ai.ProductoEmbeddingService;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;
import com.example.NoLimits.Multimedia.service.scraping.ScrapingClientService;
import com.example.NoLimits.config.AbstractContainerBaseTest;
import com.example.NoLimits.Multimedia.repository.catalogos.*;
import com.example.NoLimits.Multimedia.service.scraping.ScrapingClientService;
import com.example.NoLimits.Multimedia.service.ai.ProductoEmbeddingService;
import com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresasModel;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.model.catalogos.GenerosModel;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformasModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladoresModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoLinkCompraModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import java.util.HashSet;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ProductoServiceTest extends AbstractContainerBaseTest{

    @Autowired
    private ProductoService productoService;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private DetalleVentaRepository detalleVentaRepository;

    @MockBean
    private TipoProductoRepository tipoProductoRepository;

    @MockBean
    private ClasificacionRepository clasificacionRepository;

    @MockBean
    private EstadoRepository estadoRepository;

    @MockBean 
    private PlataformaRepository plataformaRepository;

    @MockBean
    private GeneroRepository generoRepository;

    @MockBean 
    private EmpresaRepository empresaRepository;

    @MockBean 
    private DesarrolladorRepository desarrolladorRepository;

    @MockBean 
    private TipoEmpresaRepository tipoEmpresaRepository;

    @MockBean 
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;

    @MockBean 
    private ScrapingClientService scrapingClientService;

    @MockBean 
    private ProductoEmbeddingService productoEmbeddingService;

    // ==========================
    // Helpers
    // ==========================

    private TipoProductoModel tipoProducto() {
        TipoProductoModel t = new TipoProductoModel();
        t.setId(1L);
        t.setNombre("Accesorio");
        t.setActivo(true);
        return t;
    }

    private ClasificacionModel clasificacion() {
        ClasificacionModel c = new ClasificacionModel();
        c.setId(1L);
        c.setNombre("Todo público");
        return c;
    }

    private EstadoModel estado() {
        EstadoModel e = new EstadoModel();
        e.setId(1L);
        e.setNombre("Activo");
        e.setActivo(true);
        return e;
    }

    private ProductoModel productoEntity() {
        ProductoModel p = new ProductoModel();
        p.setId(1L);
        p.setNombre("Teclado Mecánico");
        p.setPrecio(29990.0);
        p.setTipoProducto(tipoProducto());
        p.setClasificacion(clasificacion());
        p.setEstado(estado());
        p.setSaga("Spiderman");
        p.setPortadaSaga("/assets/img/sagas/spidermanSaga.webp");
        return p;
    }

    private ProductoRequestDTO requestBase() {
        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setNombre("Teclado Mecánico");
        dto.setPrecio(29990.0);
        dto.setTipoProductoId(1L);
        dto.setClasificacionId(1L);
        dto.setEstadoId(1L);
        return dto;
    }

    private Object[] filaResumen() {
        return new Object[]{
            1L,
            "Teclado Mecánico",
            29990.0,
            "Accesorio",
            "Activo",
            "Spiderman",
            "/assets/img/sagas/spidermanSaga.webp",
            null  // imagenPortada
        };
    }

    // ==========================
    // findAll / findById
    // ==========================

        @Test
        public void testFindAll() {
        List<Object[]> filas = Collections.singletonList(filaResumen());
        when(productoRepository.obtenerProductosResumen())
                .thenReturn(filas);

        List<ProductoResumenDTO> productos = productoService.findAll();

        assertNotNull(productos);
        assertEquals(1, productos.size());
        ProductoResumenDTO dto = productos.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Teclado Mecánico", dto.getNombre());
        assertEquals(29990.0, dto.getPrecio());
        assertEquals("Accesorio", dto.getTipoProductoNombre());
        assertEquals("Activo", dto.getEstadoNombre());
        }

    @Test
    public void testFindById_Existe() {
        when(productoRepository.findByIdFull(1L)).thenReturn(Optional.of(productoEntity()));

        ProductoResponseDTO dto = productoService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Teclado Mecánico", dto.getNombre());
        assertEquals(29990.0, dto.getPrecio());
        assertEquals("Accesorio", dto.getTipoProductoNombre());
        assertEquals("Activo", dto.getEstadoNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(productoRepository.findByIdFull(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.findById(99L));
    }

    // ==========================
    // save (POST)
    // ==========================

    @Test
    public void testSave_OK() {
        ProductoRequestDTO dto = requestBase();

        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(tipoProducto()));
        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(clasificacion()));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado()));
        when(productoRepository.save(any(ProductoModel.class)))
                .thenAnswer(inv -> {
                    ProductoModel p = inv.getArgument(0);
                    p.setId(1L);
                    return p;
                });
        when(productoRepository.findByIdFull(1L)).thenReturn(Optional.of(productoEntity()));

        ProductoResponseDTO res = productoService.save(dto);

        assertNotNull(res);
        assertEquals(1L, res.getId());
        assertEquals("Teclado Mecánico", res.getNombre());
        assertEquals(29990.0, res.getPrecio());
        assertEquals("Accesorio", res.getTipoProductoNombre());
        assertEquals("Activo", res.getEstadoNombre());
    }

    @Test
    public void testSave_SinTipoProducto_Lanza404() {
        ProductoRequestDTO dto = requestBase();
        dto.setTipoProductoId(null);

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.save(dto));

        verify(productoRepository, never()).save(any(ProductoModel.class));
    }

    @Test
    public void testSave_SinClasificacion_Lanza404() {
        ProductoRequestDTO dto = requestBase();
        dto.setClasificacionId(null);

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.save(dto));

        verify(productoRepository, never()).save(any(ProductoModel.class));
    }

    @Test
    public void testSave_SinEstado_Lanza404() {
        ProductoRequestDTO dto = requestBase();
        dto.setEstadoId(null);

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.save(dto));

        verify(productoRepository, never()).save(any(ProductoModel.class));
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    public void testUpdate_OK() {
        ProductoModel existente = productoEntity();
        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setNombre("Mouse Gamer");
        dto.setPrecio(19990.0);
        dto.setTipoProductoId(1L);
        dto.setClasificacionId(1L);
        dto.setEstadoId(1L);

        ProductoModel actualizado = productoEntity();
        actualizado.setNombre("Mouse Gamer");
        actualizado.setPrecio(19990.0);

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(existente))
                .thenReturn(Optional.of(actualizado));
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(tipoProducto()));
        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(clasificacion()));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado()));
        when(productoRepository.save(any(ProductoModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ProductoResponseDTO res = productoService.update(1L, dto);

        assertNotNull(res);
        assertEquals(1L, res.getId());
        assertEquals("Mouse Gamer", res.getNombre());
        assertEquals(19990.0, res.getPrecio());
    }

    @Test
    public void testUpdate_NoExiste_Lanza404() {
        ProductoRequestDTO dto = requestBase();

        when(productoRepository.findByIdFull(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.update(1L, dto));
    }

    // ==========================
    // patch (PATCH)
    // ==========================

    @Test
    public void testPatch_CambiaPrecioYEstado() {
        ProductoModel existente = productoEntity();
        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setPrecio(25000.0);
        dto.setEstadoId(2L);

        EstadoModel nuevoEstado = new EstadoModel();
        nuevoEstado.setId(2L);
        nuevoEstado.setNombre("Descontinuado");
        nuevoEstado.setActivo(true);

        ProductoModel recargado = productoEntity();
        recargado.setPrecio(25000.0);
        recargado.setEstado(nuevoEstado);

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(existente))
                .thenReturn(Optional.of(recargado));
        when(estadoRepository.findById(2L)).thenReturn(Optional.of(nuevoEstado));
        when(productoRepository.save(any(ProductoModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ProductoResponseDTO res = productoService.patch(1L, dto);

        assertNotNull(res);
        assertEquals(1L, res.getId());
        assertEquals("Teclado Mecánico", res.getNombre());
        assertEquals(25000.0, res.getPrecio());
        assertEquals("Descontinuado", res.getEstadoNombre());
    }

    @Test
    public void testPatch_TipoProductoInvalido_Lanza404() {
        ProductoModel existente = productoEntity();
        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setTipoProductoId(99L);

        when(productoRepository.findByIdFull(1L)).thenReturn(Optional.of(existente));
        when(tipoProductoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.patch(1L, dto));
    }

    // ==========================
    // deleteById
    // ==========================

    @Test
    public void testDeleteById_OK() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEntity()));
        when(detalleVentaRepository.findByProducto_Id(1L))
                .thenReturn(Collections.emptyList());
        doNothing().when(productoRepository).deleteById(1L);

        productoService.deleteById(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_ConMovimientos_LanzaIllegalState() {
        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(productoEntity()));

        DetalleVentaModel movimiento = new DetalleVentaModel();

        when(detalleVentaRepository.findByProducto_Id(1L))
                .thenReturn(List.of(movimiento));

        assertThrows(IllegalStateException.class,
                () -> productoService.deleteById(1L));

        verify(productoRepository, never()).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_Lanza404() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.deleteById(99L));

        verify(productoRepository, never()).deleteById(99L);
    }

    // ==========================
    // Búsquedas
    // ==========================

    @Test
    public void testFindByNombre() {
        when(productoRepository.findByNombre("Teclado Mecánico"))
                .thenReturn(List.of(productoEntity()));

        List<ProductoResponseDTO> productos =
                productoService.findByNombre("Teclado Mecánico");

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Teclado Mecánico", productos.get(0).getNombre());
    }

        @Test
        public void testFindByNombreContainingIgnoreCase() {
        List<Object[]> filas = Collections.singletonList(filaResumen());
        var page = new PageImpl<>(filas);
        when(productoRepository.obtenerResumenPorNombre(eq("teclado"), any()))
                .thenReturn(page);

        var resultado = productoService.findByNombreContainingIgnoreCase("teclado", 1, 20);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        assertEquals("Teclado Mecánico", resultado.getContenido().get(0).getNombre());
        }

        @Test
        public void testFindByTipoProducto() {
        List<Object[]> filas = Collections.singletonList(filaResumen());
        var page = new PageImpl<>(filas);
        when(productoRepository.obtenerResumenPorTipo(eq(1L), any()))
                .thenReturn(page);

        var resultado = productoService.findByTipoProducto(1L, 1, 20);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        }

    @Test
    public void testFindByClasificacion() {
        when(productoRepository.findByClasificacion_Id(1L))
                .thenReturn(List.of(productoEntity()));

        List<ProductoResponseDTO> productos =
                productoService.findByClasificacion(1L);

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals(1L, productos.get(0).getClasificacionId());
    }

        @Test
        public void testFindByEstado() {
        List<Object[]> filas = Collections.singletonList(filaResumen());
        var page = new PageImpl<>(filas);
        when(productoRepository.obtenerResumenPorEstado(eq(1L), any()))
                .thenReturn(page);

        var resultado = productoService.findByEstado(1L, 1, 20);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        }

    @Test
    public void testFindByTipoProductoAndEstado() {
        when(productoRepository.findByTipoProducto_IdAndEstado_Id(1L, 1L))
                .thenReturn(List.of(productoEntity()));

        List<ProductoResponseDTO> productos =
                productoService.findByTipoProductoAndEstado(1L, 1L);

        assertNotNull(productos);
        assertEquals(1, productos.size());
        ProductoResponseDTO dto = productos.get(0);
        assertEquals(1L, dto.getTipoProductoId());
        assertEquals(1L, dto.getEstadoId());
    }

    // ==========================
    // obtenerProductosConDatos
    // ==========================

    @Test
    public void testObtenerProductosConDatos() {
        Object[] fila = new Object[] {
                1L,
                "Teclado Mecánico",
                29990.0,
                "Accesorio",
                "Activo",
                "Spiderman",
                "/assets/img/sagas/spidermanSaga.webp",
                null  // imagenPortada
        };

        when(productoRepository.obtenerProductosResumen())
                .thenReturn(Collections.singletonList(fila));

        List<Map<String, Object>> resumen =
                productoService.obtenerProductosConDatos();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());

        Map<String, Object> item = resumen.get(0);
        assertEquals(1L, item.get("ID"));
        assertEquals("Teclado Mecánico", item.get("Nombre"));
        assertEquals(29990.0, item.get("Precio"));
        assertEquals("Accesorio", item.get("Tipo Producto"));
        assertEquals("Activo", item.get("Estado"));
        assertEquals("Spiderman", item.get("Saga"));
        assertEquals("/assets/img/sagas/spidermanSaga.webp", item.get("Portada Saga"));
    }

    @Test
    public void testFindAllPaged_OK() {
        List<Object[]> filas = Collections.singletonList(filaResumen());
        var page = new PageImpl<>(filas);

        when(productoRepository.obtenerResumenPaginado(any()))
                .thenReturn(page);

        var resultado = productoService.findAllPaged(1, 20);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        assertEquals("Teclado Mecánico", resultado.getContenido().get(0).getNombre());
    }

    @Test
    public void testFindBySaga_OK() {
        List<Object[]> filas = Collections.singletonList(filaResumen());
        var page = new PageImpl<>(filas);

        when(productoRepository.obtenerResumenPorSaga(eq("Spiderman"), any()))
                .thenReturn(page);

        var resultado = productoService.findBySaga("Spiderman", 1, 20);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
        assertEquals("Spiderman", resultado.getContenido().get(0).getSaga());
    }

    @Test
    public void testFindBySagaCompleto_OK() {
        when(productoRepository.findIdsBySagaIgnoreCase("Spiderman"))
                .thenReturn(List.of(1L));

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoEntity()));

        var resultado = productoService.findBySagaCompleto("Spiderman");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Teclado Mecánico", resultado.get(0).getNombre());
    }

    @Test
    public void testObtenerSagasDistinct() {
        when(productoRepository.findDistinctSagas())
                .thenReturn(List.of("Spiderman", "Batman"));

        var resultado = productoService.obtenerSagasDistinct();

        assertEquals(2, resultado.size());
        assertEquals("Spiderman", resultado.get(0));
    }

    @Test
    public void testObtenerSagasDistinctPorTipoProducto() {
        when(productoRepository.findDistinctSagasByTipoProductoId(1L))
                .thenReturn(List.of("Spiderman"));

        var resultado = productoService.obtenerSagasDistinctPorTipoProducto(1L);

        assertEquals(1, resultado.size());
        assertEquals("Spiderman", resultado.get(0));
    }

    @Test
    public void testObtenerSagasConPortada() {
        Object[] fila = new Object[] {
                "Spiderman",
                "/assets/img/sagas/spidermanSaga.webp"
        };

        List<Object[]> filas = Collections.singletonList(fila);

        when(productoRepository.findDistinctSagasWithPortada())
                .thenReturn(filas);

        var resultado = productoService.obtenerSagasConPortada();

        assertEquals(1, resultado.size());
        assertEquals("Spiderman", resultado.get(0).get("nombre"));
        assertEquals("/assets/img/sagas/spidermanSaga.webp", resultado.get(0).get("portadaSaga"));
    }

    @Test
    public void testExisteProductoPorLinkCompra_LimpiaHash() {
        when(productoRepository.existsByLinksCompraUrl("https://mercadolibre.cl/producto"))
                .thenReturn(true);

        boolean existe = productoService.existeProductoPorLinkCompra(
                "https://mercadolibre.cl/producto#reviews"
        );

        assertEquals(true, existe);
        verify(productoRepository).existsByLinksCompraUrl("https://mercadolibre.cl/producto");
    }

    @Test
    public void testObtenerIdsProductosConAppId() {
        when(productoRepository.findIdsConAppId())
                .thenReturn(List.of(1L, 2L, 3L));

        var resultado = productoService.obtenerIdsProductosConAppId();

        assertEquals(3, resultado.size());
        assertEquals(1L, resultado.get(0));
    }
    
    @Nested
    @DisplayName("Unitario - actualizarPrecioDesdeSteam")
    class ActualizarPrecioDesdeSteam {

        @Test
        @DisplayName("actualiza precio desde Steam correctamente")
        void testActualizarPrecioDesdeSteam_OK() {
            ProductoModel producto = productoEntity();

            ProductoLinkCompraModel linkSteam = new ProductoLinkCompraModel();

            PlataformaModel plataforma = new PlataformaModel();
            plataforma.setId(1L);
            plataforma.setNombre("Steam");

            linkSteam.setPlataforma(plataforma);
            linkSteam.setAppId("730");
            linkSteam.setUrl("https://store.steampowered.com/app/730");
            linkSteam.setProducto(producto);

            producto.setLinksCompra(new HashSet<>(List.of(linkSteam)));

            Map<String, Object> datosSteam = Map.of(
                    "nombre", "Counter-Strike 2",
                    "precio", 0,
                    "precioFormato", "Free To Play",
                    "moneda", "CLP",
                    "urlPlataforma", "https://store.steampowered.com/app/730"
            );

            when(productoRepository.findByIdFull(1L))
                    .thenReturn(Optional.of(producto))
                    .thenReturn(Optional.of(producto));

            when(scrapingClientService.obtenerPrecioSteam("730"))
                    .thenReturn(datosSteam);

            when(productoRepository.save(any(ProductoModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            doNothing().when(productoEmbeddingService)
                    .guardarEmbeddingProducto(eq(1L), any(String.class));

            ProductoResponseDTO resultado =
                    productoService.actualizarPrecioDesdeSteam(1L);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals(0.0, producto.getPrecio());
            assertEquals("Counter-Strike 2", linkSteam.getNombrePlataforma());
            assertEquals("Free To Play", linkSteam.getPrecioFormato());
            assertEquals("CLP", linkSteam.getMoneda());

            verify(scrapingClientService).obtenerPrecioSteam("730");
            verify(productoRepository).save(producto);
        }

        @Test
        @DisplayName("lanza excepción cuando el producto no existe")
        void testActualizarPrecioDesdeSteam_ProductoNoExiste() {
            when(productoRepository.findByIdFull(99L))
                    .thenReturn(Optional.empty());

            assertThrows(RecursoNoEncontradoException.class,
                    () -> productoService.actualizarPrecioDesdeSteam(99L));

            verify(scrapingClientService, never()).obtenerPrecioSteam(any());
            verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando el producto no tiene appId")
        void testActualizarPrecioDesdeSteam_SinAppId() {
            ProductoModel producto = productoEntity();
            producto.setLinksCompra(new HashSet<>());

            when(productoRepository.findByIdFull(1L))
                    .thenReturn(Optional.of(producto));

            assertThrows(RecursoNoEncontradoException.class,
                    () -> productoService.actualizarPrecioDesdeSteam(1L));

            verify(scrapingClientService, never()).obtenerPrecioSteam(any());
            verify(productoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Unitario - limpiarUrlMercadoLibre")
    class LimpiarUrlMercadoLibre {

        @Test
        @DisplayName("retorna string vacío cuando la URL es null")
        void testExisteProductoPorLinkCompra_Null() {
            when(productoRepository.existsByLinksCompraUrl(""))
                    .thenReturn(false);

            boolean resultado = productoService.existeProductoPorLinkCompra(null);

            assertEquals(false, resultado);
            verify(productoRepository).existsByLinksCompraUrl("");
        }
    }

    @Nested
    @DisplayName("Unitario - syncLinksCompra mediante patch")
    class SyncLinksCompra {
        
        @Test
        @DisplayName("agrega un nuevo link de compra al producto")
        void testPatch_AgregaNuevoLinkCompra() {
                ProductoModel producto = productoEntity();
                producto.setLinksCompra(new HashSet<>());

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);
                plataforma.setNombre("Steam");

                LinkCompraDTO linkDTO = new LinkCompraDTO();
                linkDTO.setPlataformaId(1L);
                linkDTO.setUrl(" https://store.steampowered.com/app/730 ");
                linkDTO.setLabel(" Steam ");
                linkDTO.setAppId(" 730 ");
                linkDTO.setPrecioActual(0.0);

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(linkDTO));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(plataformaRepository.findById(1L))
                        .thenReturn(Optional.of(plataforma));

                when(productoRepository.save(any(ProductoModel.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                ProductoResponseDTO resultado = productoService.patch(1L, dto);

                assertNotNull(resultado);
                assertEquals(1, producto.getLinksCompra().size());

                ProductoLinkCompraModel link = producto.getLinksCompra().iterator().next();

                assertEquals("https://store.steampowered.com/app/730", link.getUrl());
                assertEquals("Steam", link.getLabel());
                assertEquals("730", link.getAppId());
                assertEquals(0.0, link.getPrecioActual());

                verify(plataformaRepository).findById(1L);
                verify(productoRepository).save(producto);
        }

        @Test
        @DisplayName("actualiza un link de compra existente")
        void testPatch_ActualizaLinkCompraExistente() {
                ProductoModel producto = productoEntity();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);
                plataforma.setNombre("Steam");

                ProductoLinkCompraModel linkExistente = new ProductoLinkCompraModel();
                linkExistente.setProducto(producto);
                linkExistente.setPlataforma(plataforma);
                linkExistente.setUrl("https://url-antigua.cl");
                linkExistente.setLabel("Steam");
                linkExistente.setAppId("111");

                producto.setLinksCompra(new HashSet<>(List.of(linkExistente)));

                LinkCompraDTO linkDTO = new LinkCompraDTO();
                linkDTO.setPlataformaId(1L);
                linkDTO.setUrl("https://store.steampowered.com/app/730");
                linkDTO.setLabel("Steam Oficial");
                linkDTO.setAppId("730");
                linkDTO.setPrecioActual(9990.0);

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(linkDTO));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any(ProductoModel.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                ProductoResponseDTO resultado = productoService.patch(1L, dto);

                assertNotNull(resultado);
                assertEquals(1, producto.getLinksCompra().size());

                assertEquals("https://store.steampowered.com/app/730", linkExistente.getUrl());
                assertEquals("Steam Oficial", linkExistente.getLabel());
                assertEquals("730", linkExistente.getAppId());
                assertEquals(9990.0, linkExistente.getPrecioActual());
                assertNotNull(linkExistente.getFechaUltimaActualizacion());

                verify(plataformaRepository, never()).findById(any());
                verify(productoRepository).save(producto);
        }

        @Test
        @DisplayName("lanza excepción cuando plataformaId es null")
        void testPatch_LinkCompraSinPlataformaId_LanzaExcepcion() {
                ProductoModel producto = productoEntity();
                producto.setLinksCompra(new HashSet<>());

                LinkCompraDTO linkDTO = new LinkCompraDTO();
                linkDTO.setPlataformaId(null);
                linkDTO.setUrl("https://store.steampowered.com/app/730");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(linkDTO));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                assertThrows(IllegalArgumentException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Unitario - relaciones del producto mediante patch")
    class RelacionesProductoPatch {

        @Test
        @DisplayName("agrega empresas, géneros, plataformas y desarrolladores al producto")
        void testPatch_AgregaRelacionesProducto() {
                ProductoModel producto = productoEntity();

                producto.setEmpresas(new HashSet<>());
                producto.setGeneros(new HashSet<>());
                producto.setPlataformas(new HashSet<>());
                producto.setDesarrolladores(new HashSet<>());

                EmpresaModel empresa = new EmpresaModel();
                empresa.setId(1L);
                empresa.setNombre("Valve");

                GeneroModel genero = new GeneroModel();
                genero.setId(2L);
                genero.setNombre("Acción");

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(3L);
                plataforma.setNombre("PC");

                DesarrolladorModel desarrollador = new DesarrolladorModel();
                desarrollador.setId(4L);
                desarrollador.setNombre("Valve Studio");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setEmpresasIds(List.of(1L));
                dto.setGenerosIds(List.of(2L));
                dto.setPlataformasIds(List.of(3L));
                dto.setDesarrolladoresIds(List.of(4L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
                when(generoRepository.findById(2L)).thenReturn(Optional.of(genero));
                when(plataformaRepository.findById(3L)).thenReturn(Optional.of(plataforma));
                when(desarrolladorRepository.findById(4L)).thenReturn(Optional.of(desarrollador));

                when(productoRepository.save(any(ProductoModel.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                ProductoResponseDTO resultado = productoService.patch(1L, dto);

                assertNotNull(resultado);
                assertEquals(1, producto.getEmpresas().size());
                assertEquals(1, producto.getGeneros().size());
                assertEquals(1, producto.getPlataformas().size());
                assertEquals(1, producto.getDesarrolladores().size());

                verify(empresaRepository).findById(1L);
                verify(generoRepository).findById(2L);
                verify(plataformaRepository).findById(3L);
                verify(desarrolladorRepository).findById(4L);
                verify(productoRepository).save(producto);
        }

        @Test
        @DisplayName("lanza excepción cuando una empresa no existe")
        void testPatch_EmpresaNoExiste_Lanza404() {
                ProductoModel producto = productoEntity();
                producto.setEmpresas(new HashSet<>());

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setEmpresasIds(List.of(99L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(empresaRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Unitario - patch campos adicionales")
    class PatchCamposAdicionales {

        @Test
        @DisplayName("actualiza sinopsis, trailer, año, saga y portada")
        void testPatch_ActualizaCamposExtra() {
                ProductoModel producto = productoEntity();

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setSinopsis("Nueva sinopsis");
                dto.setUrlTrailer("https://youtube.com/trailer");
                dto.setAnio(2025);
                dto.setSaga("Batman");
                dto.setPortadaSaga("/img/batman.webp");

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any(ProductoModel.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                ProductoResponseDTO res = productoService.patch(1L, dto);

                assertNotNull(res);
                assertEquals("Nueva sinopsis", producto.getSinopsis());
                assertEquals("https://youtube.com/trailer", producto.getUrlTrailer());
                assertEquals(2025, producto.getAnio());
                assertEquals("Batman", producto.getSaga());
                assertEquals("/img/batman.webp", producto.getPortadaSaga());
        }

        @Test
        @DisplayName("actualiza tipo empresa y tipo desarrollador")
        void testPatch_TipoEmpresaYTipoDesarrollador_OK() {
                ProductoModel producto = productoEntity();

                TipoEmpresaModel tipoEmpresa = new TipoEmpresaModel();
                tipoEmpresa.setId(1L);
                tipoEmpresa.setNombre("Publisher");

                TipoDeDesarrolladorModel tipoDesarrollador = new TipoDeDesarrolladorModel();
                tipoDesarrollador.setId(1L);
                tipoDesarrollador.setNombre("Estudio");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setTipoEmpresaId(1L);
                dto.setTipoDesarrolladorId(1L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(tipoEmpresaRepository.findById(1L))
                        .thenReturn(Optional.of(tipoEmpresa));

                when(tipoDeDesarrolladorRepository.findById(1L))
                        .thenReturn(Optional.of(tipoDesarrollador));

                when(productoRepository.save(any(ProductoModel.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                ProductoResponseDTO res = productoService.patch(1L, dto);

                assertNotNull(res);
                assertEquals("Publisher", producto.getTipoEmpresa().getNombre());
                assertEquals("Estudio", producto.getTipoDesarrollador().getNombre());
        }

        @Test
        @DisplayName("actualiza imágenes del producto")
        void testPatch_Imagenes_OK() {
                ProductoModel producto = productoEntity();

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setImagenesRutas(List.of("/img/uno.webp", "/img/dos.webp"));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any(ProductoModel.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                ProductoResponseDTO res = productoService.patch(1L, dto);

                assertNotNull(res);
                assertEquals(2, producto.getImagenes().size());
        }

        @Test
        @DisplayName("lanza excepción cuando tipo empresa no existe")
        void testPatch_TipoEmpresaNoExiste_Lanza404() {
                ProductoModel producto = productoEntity();

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setTipoEmpresaId(99L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(tipoEmpresaRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Unitario - save completo con relaciones")
    class SaveCompletoConRelaciones {

        @Test
        @DisplayName("guarda producto con tipo empresa, tipo desarrollador, imágenes y relaciones")
        void testSave_CompletoConRelaciones_OK() {
                ProductoRequestDTO dto = requestBase();

                dto.setSinopsis("Sinopsis demo");
                dto.setUrlTrailer("https://youtube.com/trailer");
                dto.setAnio(2025);
                dto.setSaga("Batman");
                dto.setPortadaSaga("/img/batman.webp");
                dto.setTipoEmpresaId(1L);
                dto.setTipoDesarrolladorId(1L);
                dto.setImagenesRutas(List.of("/img/uno.webp", "/img/dos.webp"));
                dto.setEmpresasIds(List.of(1L));
                dto.setGenerosIds(List.of(2L));
                dto.setPlataformasIds(List.of(3L));
                dto.setDesarrolladoresIds(List.of(4L));

                TipoEmpresaModel tipoEmpresa = new TipoEmpresaModel();
                tipoEmpresa.setId(1L);
                tipoEmpresa.setNombre("Publisher");

                TipoDeDesarrolladorModel tipoDesarrollador = new TipoDeDesarrolladorModel();
                tipoDesarrollador.setId(1L);
                tipoDesarrollador.setNombre("Estudio");

                EmpresaModel empresa = new EmpresaModel();
                empresa.setId(1L);
                empresa.setNombre("Valve");

                GeneroModel genero = new GeneroModel();
                genero.setId(2L);
                genero.setNombre("Acción");

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(3L);
                plataforma.setNombre("PC");

                DesarrolladorModel desarrollador = new DesarrolladorModel();
                desarrollador.setId(4L);
                desarrollador.setNombre("Valve Studio");

                when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(tipoProducto()));
                when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(clasificacion()));
                when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado()));
                when(tipoEmpresaRepository.findById(1L)).thenReturn(Optional.of(tipoEmpresa));
                when(tipoDeDesarrolladorRepository.findById(1L)).thenReturn(Optional.of(tipoDesarrollador));
                when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
                when(generoRepository.findById(2L)).thenReturn(Optional.of(genero));
                when(plataformaRepository.findById(3L)).thenReturn(Optional.of(plataforma));
                when(desarrolladorRepository.findById(4L)).thenReturn(Optional.of(desarrollador));

                when(productoRepository.save(any(ProductoModel.class)))
                        .thenAnswer(inv -> {
                        ProductoModel p = inv.getArgument(0);
                        p.setId(1L);
                        return p;
                        });

                ProductoModel recargado = productoEntity();
                recargado.setSinopsis("Sinopsis demo");
                recargado.setUrlTrailer("https://youtube.com/trailer");
                recargado.setAnio(2025);
                recargado.setSaga("Batman");
                recargado.setPortadaSaga("/img/batman.webp");
                recargado.setTipoEmpresa(tipoEmpresa);
                recargado.setTipoDesarrollador(tipoDesarrollador);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(recargado));

                ProductoResponseDTO res = productoService.save(dto);

                assertNotNull(res);
                assertEquals(1L, res.getId());
                assertEquals("Batman", res.getSaga());

                verify(tipoEmpresaRepository).findById(1L);
                verify(tipoDeDesarrolladorRepository).findById(1L);
                verify(empresaRepository).findById(1L);
                verify(generoRepository).findById(2L);
                verify(plataformaRepository).findById(3L);
                verify(desarrolladorRepository).findById(4L);
                verify(productoRepository).save(any(ProductoModel.class));
        }
    }

        @Test
        public void testActualizarEmbeddingProducto_OK() {
        ProductoModel producto = productoEntity();
        doNothing().when(productoEmbeddingService)
                .guardarEmbeddingProducto(eq(1L), any(String.class));

        productoService.actualizarEmbeddingProducto(producto);

        verify(productoEmbeddingService).guardarEmbeddingProducto(eq(1L), any(String.class));
        }

        @Test
        public void testActualizarEmbeddingProducto_ErrorNoRompe() {
        ProductoModel producto = productoEntity();
        org.mockito.Mockito.doThrow(new RuntimeException("fallo"))
                .when(productoEmbeddingService)
                .guardarEmbeddingProducto(any(), any());

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(
                () -> productoService.actualizarEmbeddingProducto(producto));
        }
}