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
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoProductoRepository;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;

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
public class ProductoServiceTest {

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
}