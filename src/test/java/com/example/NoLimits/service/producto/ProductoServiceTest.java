package com.example.NoLimits.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

    // Mock adicional necesario por validación en deleteById
    @MockBean
    private DetalleVentaRepository detalleVentaRepository;

    private ProductoModel createProducto() {
        ProductoModel producto = new ProductoModel();
        producto.setId(1L);
        producto.setNombre("Teclado Mecánico");
        producto.setPrecio(29990.0);

        // Tipo de producto
        producto.setTipoProducto(new TipoProductoModel(1L, "Accesorio", null, true, null));

        // Clasificación (opcional, pero la seteamos para los tests)
        ClasificacionModel clasificacion = new ClasificacionModel();
        clasificacion.setId(1L);
        clasificacion.setNombre("Todo público");
        producto.setClasificacion(clasificacion);

        // Estado (obligatorio en el modelo)
        EstadoModel estado = new EstadoModel();
        estado.setId(1L);
        estado.setNombre("Activo");
        estado.setActivo(true);
        producto.setEstado(estado);

        return producto;
    }

    @Test
    public void testFindAll() {
        when(productoRepository.findAll()).thenReturn(List.of(createProducto()));

        List<ProductoModel> productos = productoService.findAll();

        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testFindById() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(createProducto()));

        ProductoModel producto = productoService.findById(1L);

        assertNotNull(producto);
        assertEquals("Teclado Mecánico", producto.getNombre());
        assertEquals(29990.0, producto.getPrecio());
        assertEquals("Accesorio", producto.getTipoProducto().getNombre());
        assertEquals("Activo", producto.getEstado().getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> productoService.findById(99L));
    }

    @Test
    public void testSave() {
        ProductoModel producto = createProducto();
        when(productoRepository.save(producto)).thenReturn(producto);

        ProductoModel savedProducto = productoService.save(producto);

        assertNotNull(savedProducto);
        assertEquals("Teclado Mecánico", savedProducto.getNombre());
        assertEquals("Accesorio", savedProducto.getTipoProducto().getNombre());
        assertEquals("Activo", savedProducto.getEstado().getNombre());
    }

    @Test
    public void testUpdate() {
        ProductoModel productoOriginal = createProducto();

        ProductoModel cambios = new ProductoModel();
        cambios.setNombre("Mouse Gamer");
        cambios.setPrecio(19990.0);
        cambios.setTipoProducto(new TipoProductoModel(2L, "Periférico", null, true, null));

        ClasificacionModel nuevaClasificacion = new ClasificacionModel();
        nuevaClasificacion.setId(2L);
        nuevaClasificacion.setNombre("Mayores de 13");
        cambios.setClasificacion(nuevaClasificacion);

        EstadoModel nuevoEstado = new EstadoModel();
        nuevoEstado.setId(2L);
        nuevoEstado.setNombre("Agotado");
        nuevoEstado.setActivo(true);
        cambios.setEstado(nuevoEstado);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoOriginal));
        when(productoRepository.save(any(ProductoModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoModel actualizado = productoService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("Mouse Gamer", actualizado.getNombre());
        assertEquals(19990.0, actualizado.getPrecio());
        assertEquals("Periférico", actualizado.getTipoProducto().getNombre());
        assertEquals("Mayores de 13", actualizado.getClasificacion().getNombre());
        assertEquals("Agotado", actualizado.getEstado().getNombre());
    }

    @Test
    public void testPatch() {
        ProductoModel productoOriginal = createProducto();

        ProductoModel patch = new ProductoModel();
        patch.setPrecio(25000.0);
        EstadoModel nuevoEstado = new EstadoModel();
        nuevoEstado.setId(3L);
        nuevoEstado.setNombre("Descontinuado");
        nuevoEstado.setActivo(true);
        patch.setEstado(nuevoEstado);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoOriginal));
        when(productoRepository.save(any(ProductoModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoModel patched = productoService.patch(1L, patch);

        assertNotNull(patched);
        // Nombre se mantiene
        assertEquals("Teclado Mecánico", patched.getNombre());
        // Precio actualizado
        assertEquals(25000.0, patched.getPrecio());
        // Estado actualizado
        assertEquals("Descontinuado", patched.getEstado().getNombre());
    }

    @Test
    public void testDeleteById() {
        ProductoModel producto = createProducto();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        // Sin movimientos en detalle_venta
        when(detalleVentaRepository.findByProducto_Id(1L)).thenReturn(Collections.emptyList());
        doNothing().when(productoRepository).deleteById(1L);

        productoService.deleteById(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByNombre() {
        when(productoRepository.findByNombre("Teclado Mecánico")).thenReturn(List.of(createProducto()));

        List<ProductoModel> productos = productoService.findByNombre("Teclado Mecánico");

        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testFindByNombreContainingIgnoreCase() {
        when(productoRepository.findByNombreContainingIgnoreCase("teclado")).thenReturn(List.of(createProducto()));

        List<ProductoModel> productos = productoService.findByNombreContainingIgnoreCase("teclado");

        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testFindByTipoProducto() {
        when(productoRepository.findByTipoProducto_Id(1L)).thenReturn(List.of(createProducto()));

        List<ProductoModel> productos = productoService.findByTipoProducto(1L);

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Accesorio", productos.get(0).getTipoProducto().getNombre());
    }

    @Test
    public void testFindByClasificacion() {
        when(productoRepository.findByClasificacion_Id(1L)).thenReturn(List.of(createProducto()));

        List<ProductoModel> productos = productoService.findByClasificacion(1L);

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals(1L, productos.get(0).getClasificacion().getId());
    }

    @Test
    public void testFindByEstado() {
        when(productoRepository.findByEstado_Id(1L)).thenReturn(List.of(createProducto()));

        List<ProductoModel> productos = productoService.findByEstado(1L);

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Activo", productos.get(0).getEstado().getNombre());
    }

    @Test
    public void testFindByTipoProductoAndEstado() {
        when(productoRepository.findByTipoProducto_IdAndEstado_Id(1L, 1L))
                .thenReturn(List.of(createProducto()));

        List<ProductoModel> productos = productoService.findByTipoProductoAndEstado(1L, 1L);

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Accesorio", productos.get(0).getTipoProducto().getNombre());
        assertEquals("Activo", productos.get(0).getEstado().getNombre());
    }

    @Test
    public void testObtenerProductosConDatos() {
        Object[] fila = new Object[] {
                1L,
                "Teclado Mecánico",
                29990.0,
                "Accesorio",
                "Activo"
        };

        when(productoRepository.obtenerProductosResumen())
                .thenReturn(java.util.Collections.singletonList(fila));

        var resumen = productoService.obtenerProductosConDatos();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());

        var item = resumen.get(0);
        assertEquals(1L, item.get("ID"));
        assertEquals("Teclado Mecánico", item.get("Nombre"));
        assertEquals(29990.0, item.get("Precio"));
        assertEquals("Accesorio", item.get("Tipo Producto"));
        assertEquals("Activo", item.get("Estado"));
    }
}