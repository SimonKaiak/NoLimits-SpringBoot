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
import com.example.NoLimits.Multimedia.model.producto.ImagenesModel;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
        void testFindAll_ValoresNull() {

                Object[] fila = {
                        null,
                        "Juego",
                        null,
                        "Tipo",
                        "Estado",
                        "Saga",
                        "Portada",
                        "Imagen"
                };

                when(productoRepository.obtenerProductosResumen())
                        .thenReturn(Collections.singletonList(fila));

                List<ProductoResumenDTO> resultado =
                        productoService.findAll();

                assertNull(resultado.get(0).getId());
                assertNull(resultado.get(0).getPrecio());
        }

        @Test
        public void testFindBySagaIgnoreCase() {

                List<Object[]> filas = Collections.singletonList(filaResumen());
                PageImpl<Object[]> page = new PageImpl<>(filas);

                when(productoRepository.obtenerResumenPorSaga(
                        eq("Spiderman"),
                        any()))
                        .thenReturn(page);

                var resultado =
                        productoService.findBySagaIgnoreCase(
                                "Spiderman",
                                1,
                                20);

                assertNotNull(resultado);
                assertEquals(1, resultado.getContenido().size());
                assertEquals(
                        "Spiderman",
                        resultado.getContenido().get(0).getSaga()
                );
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
    @DisplayName("save continúa cuando Steam falla")
    void testSave_ContinuaCuandoSteamFalla() {

        ProductoRequestDTO dto = requestBase();

        when(tipoProductoRepository.findById(1L))
                .thenReturn(Optional.of(tipoProducto()));

        when(clasificacionRepository.findById(1L))
                .thenReturn(Optional.of(clasificacion()));

        when(estadoRepository.findById(1L))
                .thenReturn(Optional.of(estado()));

        when(productoRepository.save(any()))
                .thenAnswer(inv -> {
                        ProductoModel p = inv.getArgument(0);
                        p.setId(1L);
                        return p;
                });

        ProductoModel productoSinLinks = productoEntity();
        productoSinLinks.setLinksCompra(new HashSet<>());

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoSinLinks))
                .thenReturn(Optional.of(productoEntity()));

        assertDoesNotThrow(() -> productoService.save(dto));
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

    @Test
    @DisplayName("guarda producto aunque falle Steam")
    void testSave_SteamFalla_NoRompeGuardado() {

        ProductoRequestDTO dto = requestBase();

        when(tipoProductoRepository.findById(1L))
                .thenReturn(Optional.of(tipoProducto()));

        when(clasificacionRepository.findById(1L))
                .thenReturn(Optional.of(clasificacion()));

        when(estadoRepository.findById(1L))
                .thenReturn(Optional.of(estado()));

        when(productoRepository.save(any()))
                .thenAnswer(inv -> {
                        ProductoModel p = inv.getArgument(0);
                        p.setId(1L);
                        return p;
                });

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoEntity()));

        doThrow(new RuntimeException("steam error"))
                .when(scrapingClientService)
                .obtenerPrecioSteam(any());

        assertDoesNotThrow(() -> productoService.save(dto));
        }

        @Test
        @DisplayName("save con tipo producto inexistente")
        void testSave_TipoProductoNoExiste() {

                ProductoRequestDTO dto = requestBase();

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.save(dto));
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

    @Test
    public void testUpdate_ClasificacionNoExiste() {

        ProductoRequestDTO dto = requestBase();

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoEntity()));

        when(tipoProductoRepository.findById(1L))
                .thenReturn(Optional.of(tipoProducto()));

        when(clasificacionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.update(1L, dto));
    }

    @Test
    public void testUpdate_EstadoNoExiste() {

        ProductoRequestDTO dto = requestBase();

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoEntity()));

        when(tipoProductoRepository.findById(1L))
                .thenReturn(Optional.of(tipoProducto()));

        when(clasificacionRepository.findById(1L))
                .thenReturn(Optional.of(clasificacion()));

        when(estadoRepository.findById(1L))
                .thenReturn(Optional.empty());

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

    @Test
    void testPatch_ProductoNoExiste_Lanza404() {

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setPrecio(1000.0);

        when(productoRepository.findByIdFull(99L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.patch(99L, dto));
    }

    @Test
    void testPatch_ClasificacionNoExiste() {

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setClasificacionId(99L);

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoEntity()));

        when(clasificacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> productoService.patch(1L, dto)
        );
     }

     @Test
     void testPatch_EstadoNoExiste() {

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setEstadoId(99L);

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoEntity()));

        when(estadoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> productoService.patch(1L, dto)
        );
    }

    @Test
    void testPatch_TipoEmpresaNoExiste() {

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setTipoEmpresaId(99L);

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoEntity()));

        when(tipoEmpresaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                 RecursoNoEncontradoException.class,
                () -> productoService.patch(1L, dto)
        );
    }

    @Test
    void testPatch_TipoDesarrolladorNoExiste() {

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setTipoDesarrolladorId(99L);

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoEntity()));

        when(tipoDeDesarrolladorRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> productoService.patch(1L, dto)
        );
    }

    @Test
    void testPatch_ImagenesVacias() {

        ProductoModel producto = productoEntity();
        producto.setImagenes(new ArrayList<>());

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setImagenesRutas(Collections.emptyList());

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(producto))
                .thenReturn(Optional.of(producto));

        when(productoRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        productoService.patch(1L, dto);

        assertEquals(0, producto.getImagenes().size());
    }

    @Test
    void testPatch_ImagenesCollectionNull() {

        ProductoModel producto = productoEntity();
        producto.setImagenes(null);

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setImagenesRutas(List.of("img1.webp"));

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(producto))
                .thenReturn(Optional.of(producto));

         when(productoRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        productoService.patch(1L, dto);

        assertNotNull(producto.getImagenes());
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

    @Test
    void testDeleteById_SinMovimientos() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(productoEntity()));

        when(detalleVentaRepository.findByProducto_Id(1L))
                .thenReturn(Collections.emptyList());

        productoService.deleteById(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    void testDeleteById_ConMovimientos() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(productoEntity()));

        when(detalleVentaRepository.findByProducto_Id(1L))
                .thenReturn(List.of(mock(DetalleVentaModel.class)));

        assertThrows(
                 IllegalStateException.class,
                () -> productoService.deleteById(1L)
        );
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
    public void testFindBySagaCompleto_FiltraProductosNoEncontrados() {

        when(productoRepository.findIdsBySagaIgnoreCase("Halo"))
                .thenReturn(List.of(1L, 2L));

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.of(productoEntity()));

        when(productoRepository.findByIdFull(2L))
                .thenReturn(Optional.empty());

        List<ProductoResponseDTO> resultado =
                productoService.findBySagaCompleto("Halo");

        assertEquals(1, resultado.size());
    }

    @Test
    void testFindBySagaCompleto_Vacio() {

        when(productoRepository.findIdsBySagaIgnoreCase("Halo"))
                .thenReturn(List.of(1L));

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.empty());

        List<ProductoResponseDTO> resultado =
                productoService.findBySagaCompleto("Halo");

        assertTrue(resultado.isEmpty());
    }
  

    @Test
    public void testFindBySagaCompleto_IgnoraIdsNoEncontrados() {

        when(productoRepository.findIdsBySagaIgnoreCase("Spiderman"))
                .thenReturn(List.of(1L));

        when(productoRepository.findByIdFull(1L))
                .thenReturn(Optional.empty());

        var resultado =
                productoService.findBySagaCompleto("Spiderman");

        assertEquals(0, resultado.size());
    }

    @Test
    public void testExisteProductoPorLinkCompra_Null() {

        when(productoRepository.existsByLinksCompraUrl(""))
                .thenReturn(false);

        boolean existe =
                productoService.existeProductoPorLinkCompra(null);

        assertFalse(existe);
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
    public void testExisteProductoPorLinkCompra_SinHash() {

        when(productoRepository.existsByLinksCompraUrl(
                "https://mercadolibre.cl/producto"))
                .thenReturn(true);

        boolean existe =
                productoService.existeProductoPorLinkCompra(
                        "https://mercadolibre.cl/producto"
                );

        assertTrue(existe);

        verify(productoRepository)
                .existsByLinksCompraUrl(
                        "https://mercadolibre.cl/producto"
                );
    }

    @Test
    public void testExisteProductoPorLinkCompra_EliminaFragmento() {

        when(productoRepository.existsByLinksCompraUrl(
                 "https://mercadolibre.cl/producto"))
                .thenReturn(true);

        boolean existe =
                productoService.existeProductoPorLinkCompra(
                        "https://mercadolibre.cl/producto#reviews"
                );  

        assertTrue(existe);
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

        @Test
        @DisplayName("lanza excepción cuando appId está vacío")
        void testActualizarPrecioDesdeSteam_AppIdVacio() {

                ProductoModel producto = productoEntity();

                ProductoLinkCompraModel linkSteam = new ProductoLinkCompraModel();
                linkSteam.setAppId("");

                producto.setLinksCompra(new HashSet<>(List.of(linkSteam)));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.actualizarPrecioDesdeSteam(1L));

                verify(scrapingClientService, never())
                        .obtenerPrecioSteam(any());

                verify(productoRepository, never())
                        .save(any());
        }

        @Test
        @DisplayName("continua cuando embedding falla")
        void testActualizarPrecioDesdeSteam_EmbeddingFalla() {

                ProductoModel producto = productoEntity();

                ProductoLinkCompraModel link = new ProductoLinkCompraModel();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);

                link.setPlataforma(plataforma);
                link.setAppId("730");

                producto.setLinksCompra(new HashSet<>(List.of(link)));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(scrapingClientService.obtenerPrecioSteam("730"))
                        .thenReturn(Map.of(
                                "nombre", "CS2",
                                "precio", 0,
                                "precioFormato", "Free",
                                "moneda", "CLP",
                                "urlPlataforma", "url"
                        ));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                doThrow(new RuntimeException("error"))
                        .when(productoEmbeddingService)
                        .guardarEmbeddingProducto(any(), any());

                assertDoesNotThrow(
                        () -> productoService.actualizarPrecioDesdeSteam(1L)
                );
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

        @Test
        @DisplayName("lanza excepción cuando url está vacía")
        void testPatch_LinkCompraSinUrl_LanzaExcepcion() {

                ProductoModel producto = productoEntity();
                producto.setLinksCompra(new HashSet<>());

                LinkCompraDTO linkDTO = new LinkCompraDTO();
                linkDTO.setPlataformaId(1L);
                linkDTO.setUrl("");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(linkDTO));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                assertThrows(IllegalArgumentException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando la plataforma no existe")
        void testPatch_LinkCompra_PlataformaNoExiste() {

                ProductoModel producto = productoEntity();
                producto.setLinksCompra(new HashSet<>());

                LinkCompraDTO linkDTO = new LinkCompraDTO();
                linkDTO.setPlataformaId(99L);
                linkDTO.setUrl("https://store.steampowered.com/app/730");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(linkDTO));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(plataformaRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("usa nombre de plataforma cuando label es null")
        void testPatch_LinkCompraSinLabel_UsaNombrePlataforma() {

                ProductoModel producto = productoEntity();
                producto.setLinksCompra(new HashSet<>());

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);
                plataforma.setNombre("Steam");

                LinkCompraDTO link = new LinkCompraDTO();
                link.setPlataformaId(1L);
                link.setUrl("https://steam.com/app/730");
                link.setLabel(null);

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(link));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(plataformaRepository.findById(1L))
                        .thenReturn(Optional.of(plataforma));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                ProductoLinkCompraModel creado =
                producto.getLinksCompra().iterator().next();

                assertEquals("Steam", creado.getLabel());
        }

        @Test
        @DisplayName("inicializa colección linksCompra cuando es null")
        void testPatch_LinksCompraCollectionNull() {

                ProductoModel producto = productoEntity();
                producto.setLinksCompra(null);

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);
                plataforma.setNombre("Steam");

                LinkCompraDTO link = new LinkCompraDTO();
                link.setPlataformaId(1L);
                link.setUrl("https://steam.com/app/730");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(link));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(plataformaRepository.findById(1L))
                        .thenReturn(Optional.of(plataforma));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertNotNull(producto.getLinksCompra());
        }

        @Test
        @DisplayName("mantiene appId existente cuando appId viene null")
        void testPatch_LinkCompraMantieneAppId() {

                ProductoModel producto = productoEntity();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);

                ProductoLinkCompraModel link = new ProductoLinkCompraModel();
                link.setProducto(producto);
                link.setPlataforma(plataforma);
                link.setAppId("730");

                producto.setLinksCompra(new HashSet<>(List.of(link)));

                LinkCompraDTO dtoLink = new LinkCompraDTO();
                dtoLink.setPlataformaId(1L);
                dtoLink.setUrl("https://steam.com/app/730");
                dtoLink.setAppId(null);

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(dtoLink));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals("730", link.getAppId());
        }

        @Test
        @DisplayName("ignora links duplicados de misma plataforma")
        void testPatch_LinkCompraDuplicadoMismaPlataforma() {

                ProductoModel producto = productoEntity();

                producto.setLinksCompra(new HashSet<>());

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);
                plataforma.setNombre("Steam");

                LinkCompraDTO l1 = new LinkCompraDTO();
                l1.setPlataformaId(1L);
                l1.setUrl("url1");

                LinkCompraDTO l2 = new LinkCompraDTO();
                l2.setPlataformaId(1L);
                l2.setUrl("url2");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(l1, l2));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(plataformaRepository.findById(1L))
                        .thenReturn(Optional.of(plataforma));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);
        }

        @Test
        @DisplayName("cubre merge function cuando existen plataformas duplicadas")
        void testPatch_CubreMergeFunctionToMap() {

                ProductoModel producto = productoEntity();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);
                plataforma.setNombre("Steam");

                ProductoLinkCompraModel link1 = new ProductoLinkCompraModel();
                link1.setProducto(producto);
                link1.setPlataforma(plataforma);
                link1.setUrl("url1");

                ProductoLinkCompraModel link2 = new ProductoLinkCompraModel();
                link2.setProducto(producto);
                link2.setPlataforma(plataforma);
                link2.setUrl("url2");

                Set<ProductoLinkCompraModel> links = new HashSet<>();
                links.add(link1);
                links.add(link2);

                producto.setLinksCompra(links);

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                verify(productoRepository).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando linkCompra tiene plataformaId null y url válida")
        void testPatch_LinkCompra_PlataformaIdNull_RamaDirecta() {

                ProductoModel producto = productoEntity();
                producto.setLinksCompra(null);

                LinkCompraDTO link = new LinkCompraDTO();
                link.setPlataformaId(null);
                link.setUrl("https://steam.com/app/730");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(link));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                assertThrows(
           IllegalArgumentException.class,
                        () -> productoService.patch(1L, dto)
                );
        }

        @Test
        @DisplayName("cubre merge function de links duplicados")
        void testPatch_LinkDuplicado_MergeFunction() {

                ProductoModel producto = productoEntity();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);

                ProductoLinkCompraModel l1 = new ProductoLinkCompraModel();
                l1.setPlataforma(plataforma);

                ProductoLinkCompraModel l2 = new ProductoLinkCompraModel();
                l2.setPlataforma(plataforma);

                Set<ProductoLinkCompraModel> links = new HashSet<>();
                links.add(l1);
                links.add(l2);

                producto.setLinksCompra(links);

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);
        }

        @Test
        @DisplayName("lanza excepción cuando plataformaId es null")
        void testPatch_PlataformaIdNull() {

                ProductoModel producto = productoEntity();
                producto.setLinksCompra(null);

                LinkCompraDTO link = new LinkCompraDTO();
                link.setPlataformaId(null);
                link.setUrl("https://steam.com/app/730");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(link));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                assertThrows(
                IllegalArgumentException.class,
                        () -> productoService.patch(1L, dto)
                );
        }

        @Test
        @DisplayName("usa nombre plataforma cuando label viene vacío")
        void testPatch_LinkCompra_LabelBlank() {

                ProductoModel producto = productoEntity();
                producto.setLinksCompra(new HashSet<>());

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);
                plataforma.setNombre("Steam");

                LinkCompraDTO link = new LinkCompraDTO();
                link.setPlataformaId(1L);
                link.setUrl("https://steam.com");
                link.setLabel("   ");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(link));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(plataformaRepository.findById(1L))
                        .thenReturn(Optional.of(plataforma));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                ProductoLinkCompraModel creado =
                producto.getLinksCompra().iterator().next();

                assertEquals("Steam", creado.getLabel());
        }

        @Test
        @DisplayName("usa appId existente cuando viene vacío")
        void testPatch_LinkCompra_AppIdBlank() {

                ProductoModel producto = productoEntity();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);
                plataforma.setNombre("Steam");

                ProductoLinkCompraModel existente =
                        new ProductoLinkCompraModel();

                existente.setPlataforma(plataforma);
                existente.setAppId("730");

                producto.setLinksCompra(
                        new HashSet<>(List.of(existente))
                );

                LinkCompraDTO link = new LinkCompraDTO();
                link.setPlataformaId(1L);
                link.setUrl("https://steam.com");
                link.setAppId("   ");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setLinksCompra(List.of(link));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals("730", existente.getAppId());
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
        @DisplayName("elimina empresas no incluidas en patch")
        void testPatch_RemueveEmpresaExistente() {

                ProductoModel producto = productoEntity();

                EmpresaModel empresa = new EmpresaModel();
                empresa.setId(1L);

                EmpresasModel rel = new EmpresasModel();
                rel.setProducto(producto);
                rel.setEmpresa(empresa);

                producto.setEmpresas(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setEmpresasIds(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(0, producto.getEmpresas().size());
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

        @Test
        @DisplayName("lanza excepción cuando un género no existe")
        void testPatch_GeneroNoExiste_Lanza404() {
                ProductoModel producto = productoEntity();
                 producto.setGeneros(new HashSet<>());

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setGenerosIds(List.of(99L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(generoRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());

    
        }


        @Test
        @DisplayName("lanza excepción cuando un desarrollador no existe")
        void testPatch_DesarrolladorNoExiste_Lanza404() {
                ProductoModel producto = productoEntity();
                producto.setDesarrolladores(new HashSet<>());

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setDesarrolladoresIds(List.of(99L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(desarrolladorRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());
        
        }

        @Test
        @DisplayName("lanza excepción cuando una plataforma no existe")
        void testPatch_PlataformaNoExiste_Lanza404() {
                ProductoModel producto = productoEntity();
                producto.setPlataformas(new HashSet<>());

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setPlataformasIds(List.of(99L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(plataformaRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());

        }

        @Test
        @DisplayName("elimina relaciones que ya no vienen en el DTO")
        void testPatch_EliminaRelacionesExistentes() {

                ProductoModel producto = productoEntity();

                EmpresaModel empresa = new EmpresaModel();
                empresa.setId(1L);

                EmpresasModel relacion = new EmpresasModel();
                relacion.setProducto(producto);
                relacion.setEmpresa(empresa);

                producto.setEmpresas(new HashSet<>(List.of(relacion)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setEmpresasIds(List.of()); // vacío

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(0, producto.getEmpresas().size());
        }

        @Test
        @DisplayName("limpia imágenes cuando recibe lista vacía")
        void testPatch_ImagenesVacias() {

                ProductoModel producto = productoEntity();

                ImagenesModel img = new ImagenesModel();
                img.setRuta("/img/test.webp");

                producto.setImagenes(new ArrayList<>(List.of(img)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setImagenesRutas(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(0, producto.getImagenes().size());
        }

        @Test
        @DisplayName("mantiene empresas cuando empresasIds es null")
        void testPatch_EmpresasNull_NoModifica() {

                ProductoModel producto = productoEntity();

                EmpresaModel empresa = new EmpresaModel();
                empresa.setId(1L);

                EmpresasModel rel = new EmpresasModel();
                rel.setProducto(producto);
                rel.setEmpresa(empresa);

                producto.setEmpresas(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setEmpresasIds(null);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getEmpresas().size());
        }

        @Test
        @DisplayName("ignora empresa con id null")
        void testPatch_EmpresaIdNull() {

                ProductoModel producto = productoEntity();

                EmpresaModel empresa = new EmpresaModel();
                empresa.setId(null);

                EmpresasModel rel = new EmpresasModel();
                rel.setEmpresa(empresa);
                rel.setProducto(producto);

                producto.setEmpresas(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setEmpresasIds(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getEmpresas().size());
        }

        @Test
        @DisplayName("mantiene géneros cuando generosIds es null")
        void testPatch_GenerosNull_NoModifica() {

                ProductoModel producto = productoEntity();

                GeneroModel genero = new GeneroModel();
                genero.setId(1L);

                GenerosModel rel = new GenerosModel();
                rel.setProducto(producto);
                rel.setGenero(genero);

                producto.setGeneros(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setGenerosIds(null);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getGeneros().size());
        }

        @Test
        @DisplayName("ignora genero con id null")
        void testPatch_GeneroIdNull() {

                ProductoModel producto = productoEntity();

                GeneroModel genero = new GeneroModel();
                genero.setId(null);

                GenerosModel rel = new GenerosModel();
                rel.setGenero(genero);
                rel.setProducto(producto);

                producto.setGeneros(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setGenerosIds(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getGeneros().size());
        }

        @Test
        @DisplayName("mantiene plataformas cuando plataformasIds es null")
        void testPatch_PlataformasNull_NoModifica() {

                ProductoModel producto = productoEntity();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);

                PlataformasModel rel = new PlataformasModel();
                rel.setProducto(producto);
                rel.setPlataforma(plataforma);

                producto.setPlataformas(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setPlataformasIds(null);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getPlataformas().size());
        }


        @Test
        @DisplayName("ignora plataforma con id null")
        void testPatch_PlataformaIdNull() {

                ProductoModel producto = productoEntity();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(null);

                PlataformasModel rel = new PlataformasModel();
                rel.setPlataforma(plataforma);
                rel.setProducto(producto);

                producto.setPlataformas(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setPlataformasIds(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getPlataformas().size());
        }

        @Test
        @DisplayName("mantiene desarrolladores cuando desarrolladoresIds es null")
        void testPatch_DesarrolladoresNull_NoModifica() {

                ProductoModel producto = productoEntity();

                DesarrolladorModel dev = new DesarrolladorModel();
                dev.setId(1L);

                DesarrolladoresModel rel = new DesarrolladoresModel();
                rel.setProducto(producto);
                rel.setDesarrollador(dev);

                producto.setDesarrolladores(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setDesarrolladoresIds(null);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getDesarrolladores().size());
        }

        @Test
        @DisplayName("ignora desarrollador con id null")
        void testPatch_DesarrolladorIdNull() {

                ProductoModel producto = productoEntity();

                DesarrolladorModel dev = new DesarrolladorModel();
                dev.setId(null);

                DesarrolladoresModel rel = new DesarrolladoresModel();
                rel.setDesarrollador(dev);
                rel.setProducto(producto);

                producto.setDesarrolladores(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setDesarrolladoresIds(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getDesarrolladores().size());
        }

        @Test
        @DisplayName("inicializa colección empresas cuando es null")
        void testPatch_EmpresasCollectionNull() {

                ProductoModel producto = productoEntity();
                producto.setEmpresas(null);

                EmpresaModel empresa = new EmpresaModel();
                empresa.setId(1L);

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setEmpresasIds(List.of(1L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(empresaRepository.findById(1L))
                        .thenReturn(Optional.of(empresa));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertNotNull(producto.getEmpresas());
        }

        @Test
        @DisplayName("no agrega empresa duplicada")
        void testPatch_EmpresaDuplicada_NoAgrega() {

                ProductoModel producto = productoEntity();

                EmpresaModel empresa = new EmpresaModel();
                empresa.setId(1L);

                EmpresasModel rel = new EmpresasModel();
                rel.setProducto(producto);
                rel.setEmpresa(empresa);

                producto.setEmpresas(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setEmpresasIds(List.of(1L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getEmpresas().size());

                verify(empresaRepository, never()).findById(any());
        }

        @Test
        @DisplayName("no agrega género duplicado")
        void testPatch_GeneroDuplicado_NoAgrega() {

                ProductoModel producto = productoEntity();

                GeneroModel genero = new GeneroModel();
                genero.setId(1L);

                GenerosModel rel = new GenerosModel();
                rel.setProducto(producto);
                rel.setGenero(genero);

                producto.setGeneros(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setGenerosIds(List.of(1L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getGeneros().size());

                verify(generoRepository, never()).findById(any());
        }

        @Test
        @DisplayName("no agrega plataforma duplicada")
        void testPatch_PlataformaDuplicada_NoAgrega() {

                ProductoModel producto = productoEntity();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);

                PlataformasModel rel = new PlataformasModel();
                rel.setProducto(producto);
                rel.setPlataforma(plataforma);

                producto.setPlataformas(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setPlataformasIds(List.of(1L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getPlataformas().size());

                verify(plataformaRepository, never()).findById(any());
        }

        @Test
        @DisplayName("no agrega desarrollador duplicado")
        void testPatch_DesarrolladorDuplicado_NoAgrega() {

                ProductoModel producto = productoEntity();

                DesarrolladorModel dev = new DesarrolladorModel();
                dev.setId(1L);

                DesarrolladoresModel rel = new DesarrolladoresModel();
                rel.setProducto(producto);
                rel.setDesarrollador(dev);

                producto.setDesarrolladores(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setDesarrolladoresIds(List.of(1L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(1, producto.getDesarrolladores().size());

                verify(desarrolladorRepository, never()).findById(any());
        }

        @Test
        @DisplayName("inicializa colección de géneros cuando es null")
        void testPatch_GenerosCollectionNull() {

                ProductoModel producto = productoEntity();
                producto.setGeneros(null);

                GeneroModel genero = new GeneroModel();
                genero.setId(1L);

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setGenerosIds(List.of(1L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(generoRepository.findById(1L))
                        .thenReturn(Optional.of(genero));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertNotNull(producto.getGeneros());
        }

        @Test
        @DisplayName("inicializa colección de plataformas cuando es null")
        void testPatch_PlataformasCollectionNull() {

                ProductoModel producto = productoEntity();
                producto.setPlataformas(null);

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);
                plataforma.setNombre("PC");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setPlataformasIds(List.of(1L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(plataformaRepository.findById(1L))
                        .thenReturn(Optional.of(plataforma));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertNotNull(producto.getPlataformas());
                assertEquals(1, producto.getPlataformas().size());
        }

        @Test
        @DisplayName("inicializa colección de desarrolladores cuando es null")
        void testPatch_DesarrolladoresCollectionNull() {

                ProductoModel producto = productoEntity();
                producto.setDesarrolladores(null);

                DesarrolladorModel desarrollador = new DesarrolladorModel();
                desarrollador.setId(1L);
                desarrollador.setNombre("Valve Studio");

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setDesarrolladoresIds(List.of(1L));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(desarrolladorRepository.findById(1L))
                        .thenReturn(Optional.of(desarrollador));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertNotNull(producto.getDesarrolladores());
                assertEquals(1, producto.getDesarrolladores().size());
        }

        @Test
        @DisplayName("elimina géneros no incluidos en patch")
        void testPatch_RemueveGeneroExistente() {

                ProductoModel producto = productoEntity();

                GeneroModel genero = new GeneroModel();
                genero.setId(1L);

                GenerosModel rel = new GenerosModel();
                rel.setProducto(producto);
                rel.setGenero(genero);

                producto.setGeneros(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setGenerosIds(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(0, producto.getGeneros().size());
        }

        @Test
        @DisplayName("elimina plataformas no incluidas en patch")
        void testPatch_RemuevePlataformaExistente() {

                ProductoModel producto = productoEntity();

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setId(1L);

                PlataformasModel rel = new PlataformasModel();
                rel.setProducto(producto);
                rel.setPlataforma(plataforma);

                producto.setPlataformas(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setPlataformasIds(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(0, producto.getPlataformas().size());
        }

        @Test
        @DisplayName("elimina desarrolladores no incluidos en patch")
        void testPatch_RemueveDesarrolladorExistente() {

                ProductoModel producto = productoEntity();

                DesarrolladorModel dev = new DesarrolladorModel();
                dev.setId(1L);

                DesarrolladoresModel rel = new DesarrolladoresModel();
                rel.setProducto(producto);
                rel.setDesarrollador(dev);

                producto.setDesarrolladores(new HashSet<>(List.of(rel)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setDesarrolladoresIds(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(0, producto.getDesarrolladores().size());
        }

        @Test
        @DisplayName("tipo producto inexistente")
        void testPatch_TipoProductoNoExiste() {

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setTipoProductoId(999L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(productoEntity()));

                when(tipoProductoRepository.findById(999L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));
        }

        @Test
        @DisplayName("clasificacion inexistente")
        void testPatch_ClasificacionNoExiste() {

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setClasificacionId(999L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(productoEntity()));

                when(clasificacionRepository.findById(999L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));
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
        @DisplayName("actualiza tipo producto")
        void testPatch_TipoProducto_OK() {

                ProductoModel producto = productoEntity();

                TipoProductoModel tipo = tipoProducto();

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setTipoProductoId(1L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipo));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(tipo, producto.getTipoProducto());
        }

        @Test
        @DisplayName("actualiza clasificación")
        void testPatch_Clasificacion_OK() {

                ProductoModel producto = productoEntity();

                ClasificacionModel clasificacion = clasificacion();

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setClasificacionId(1L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(clasificacion, producto.getClasificacion());
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
        @DisplayName("limpia imágenes cuando lista viene vacía")
        void testPatch_ImagenesListaVacia() {

                ProductoModel producto = productoEntity();

                ImagenesModel img = new ImagenesModel();
                img.setRuta("/img/test.webp");

                producto.setImagenes(new ArrayList<>(List.of(img)));

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setImagenesRutas(List.of());

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertEquals(0, producto.getImagenes().size());
        }

        @Test
        @DisplayName("inicializa imágenes cuando colección es null")
        void testPatch_ImagenesCollectionNull() {

                ProductoModel producto = productoEntity();
                producto.setImagenes(null);

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setImagenesRutas(List.of("/img/test.webp"));

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto))
                        .thenReturn(Optional.of(producto));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> inv.getArgument(0));

                productoService.patch(1L, dto);

                assertNotNull(producto.getImagenes());
                assertEquals(1, producto.getImagenes().size());
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

        @Test
        @DisplayName("Lanza excepcion cuando tipo desarrollador no existe")
        void testPatch_TipoDesarrolladorNoExiste_Lanza404(){
                 ProductoModel producto = productoEntity();

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setTipoDesarrolladorId(99L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(tipoDeDesarrolladorRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());

        }

        @Test
        @DisplayName("lanza excepción cuando clasificación no existe")
        void testPatch_ClasificacionInvalida_Lanza404() {
                ProductoModel producto = productoEntity();

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setClasificacionId(99L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(clasificacionRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());
        }
    
        @Test
        @DisplayName("lanza excepción cuando estado no existe")
        void testPatch_EstadoInvalido_Lanza404() {

                ProductoModel producto = productoEntity();

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setEstadoId(99L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(estadoRepository.findById(99L))
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

        @Test
        @DisplayName("guarda producto sin tipo empresa ni tipo desarrollador")
        void testSave_SinTiposOpcionales() {

                ProductoRequestDTO dto = requestBase();

                dto.setTipoEmpresaId(null);
                dto.setTipoDesarrolladorId(null);

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> {
                                ProductoModel p = inv.getArgument(0);
                                p.setId(1L);
                                return p;
                        });

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(productoEntity()));

                productoService.save(dto);

                verify(productoRepository).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando tipo empresa no existe")
        void testSave_TipoEmpresaNoExiste() {

                ProductoRequestDTO dto = requestBase();
                dto.setTipoEmpresaId(99L);

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(tipoEmpresaRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.save(dto));

                verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando tipo desarrollador no existe")
        void testSave_TipoDesarrolladorNoExiste() {

                ProductoRequestDTO dto = requestBase();
                dto.setTipoDesarrolladorId(99L);

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(tipoDeDesarrolladorRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.save(dto));

                verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando empresa no existe")
        void testSave_EmpresaNoExiste() {

                ProductoRequestDTO dto = requestBase();
                dto.setEmpresasIds(List.of(99L));

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(empresaRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.save(dto));

                verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando género no existe")
        void testSave_GeneroNoExiste() {

                ProductoRequestDTO dto = requestBase();
                dto.setGenerosIds(List.of(99L));

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(generoRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.save(dto));

                verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando plataforma no existe")
        void testSave_PlataformaNoExiste() {

                ProductoRequestDTO dto = requestBase();
                dto.setPlataformasIds(List.of(99L));

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(plataformaRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.save(dto));

                verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("save continúa cuando Steam falla")
        void testSave_ContinuaCuandoSteamFalla() {

                ProductoRequestDTO dto = requestBase();

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> {
                                ProductoModel p = inv.getArgument(0);
                                p.setId(1L);
                                return p;
                        });

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(productoEntity()));

                ProductoResponseDTO response =
                        productoService.save(dto);

                assertNotNull(response);
        }

        @Test
        @DisplayName("lanza excepción cuando tipo producto no existe")
        void testPatch_TipoProductoNoExiste_Lanza404() {

                ProductoModel producto = productoEntity();

                ProductoUpdateDTO dto = new ProductoUpdateDTO();
                dto.setTipoProductoId(999L);

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(producto));

                when(tipoProductoRepository.findById(999L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.patch(1L, dto));

                verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando desarrollador no existe")
        void testSave_DesarrolladorNoExiste() {

                ProductoRequestDTO dto = requestBase();
                dto.setDesarrolladoresIds(List.of(99L));

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(desarrolladorRepository.findById(99L))
                        .thenReturn(Optional.empty());

                assertThrows(RecursoNoEncontradoException.class,
                        () -> productoService.save(dto));

                verify(productoRepository, never()).save(any());
        }

        @Test
        @DisplayName("save permite tipo empresa null")
        void testSave_TipoEmpresaNull() {

                ProductoRequestDTO dto = requestBase();

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> {
                                ProductoModel p = inv.getArgument(0);
                                p.setId(1L);
                                return p;
                        });

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(productoEntity()));

                assertNotNull(productoService.save(dto));
        }

        @Test
        @DisplayName("save permite tipo desarrollador null")
        void testSave_TipoDesarrolladorNull() {

                ProductoRequestDTO dto = requestBase();

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> {
                                ProductoModel p = inv.getArgument(0);
                                p.setId(1L);
                                return p;
                        });

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(productoEntity()));

                assertNotNull(productoService.save(dto));
        }

        @Test
        @DisplayName("guarda producto con listas vacías")
        void testSave_ListasVacias() {

                ProductoRequestDTO dto = requestBase();

                dto.setEmpresasIds(List.of());
                dto.setGenerosIds(List.of());
                dto.setPlataformasIds(List.of());
                dto.setDesarrolladoresIds(List.of());

                when(tipoProductoRepository.findById(1L))
                        .thenReturn(Optional.of(tipoProducto()));

                when(clasificacionRepository.findById(1L))
                        .thenReturn(Optional.of(clasificacion()));

                when(estadoRepository.findById(1L))
                        .thenReturn(Optional.of(estado()));

                when(productoRepository.save(any()))
                        .thenAnswer(inv -> {
                                ProductoModel p = inv.getArgument(0);
                                p.setId(1L);
                                return p;
                        });

                when(productoRepository.findByIdFull(1L))
                        .thenReturn(Optional.of(productoEntity()));

                ProductoResponseDTO resultado =
                         productoService.save(dto);

                assertNotNull(resultado);
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
        @DisplayName("actualiza embedding con relaciones null")
        void testActualizarEmbeddingProducto_RelacionesNull() {

                ProductoModel producto = new ProductoModel();

                producto.setId(1L);
                producto.setNombre("Halo");

                producto.setTipoProducto(null);
                producto.setClasificacion(null);
                producto.setEstado(null);

                producto.setGeneros(null);
                producto.setEmpresas(null);
                producto.setPlataformas(null);
                producto.setDesarrolladores(null);

                assertDoesNotThrow(
                        () -> productoService.actualizarEmbeddingProducto(producto)
                );
        }

        @Test
        @DisplayName("actualiza embedding con colecciones vacías")
        void testActualizarEmbeddingProducto_ColeccionesVacias() {

                ProductoModel producto = new ProductoModel();
                producto.setId(1L);
                producto.setNombre("Halo");

                producto.setGeneros(new HashSet<>());
                producto.setEmpresas(new HashSet<>());
                producto.setPlataformas(new HashSet<>());
                producto.setDesarrolladores(new HashSet<>());

                assertDoesNotThrow(
                        () -> productoService.actualizarEmbeddingProducto(producto)
                );
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