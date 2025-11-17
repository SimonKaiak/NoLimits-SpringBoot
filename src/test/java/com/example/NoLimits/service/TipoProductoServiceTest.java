package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.TipoProductoModel;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.TipoProductoRepository;
import com.example.NoLimits.Multimedia.service.TipoProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@ActiveProfiles("test")
public class TipoProductoServiceTest {

    @Autowired
    private TipoProductoService tipoProductoService;

    @MockBean
    private TipoProductoRepository tipoProductoRepository;

    @MockBean
    private ProductoRepository productoRepository; // necesario para deleteById()

    // ================== HELPERS ==================

    private TipoProductoModel createTipoProducto() {
        TipoProductoModel tipoProducto = new TipoProductoModel();
        tipoProducto.setId(1L);
        tipoProducto.setNombre("Videojuegos");
        tipoProducto.setDescripcion("Categoría para videojuegos");
        tipoProducto.setActivo(true);
        return tipoProducto;
    }

    // ================== TESTS ==================

    @Test
    public void testFindAll() {
        when(tipoProductoRepository.findAll()).thenReturn(List.of(createTipoProducto()));

        List<TipoProductoModel> tipos = tipoProductoService.findAll();

        assertNotNull(tipos);
        assertEquals(1, tipos.size());
        assertEquals("Videojuegos", tipos.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(createTipoProducto()));

        TipoProductoModel tipo = tipoProductoService.findById(1L);

        assertNotNull(tipo);
        assertEquals("Videojuegos", tipo.getNombre());
        assertEquals(1L, tipo.getId());
        assertTrue(Boolean.TRUE.equals(tipo.getActivo()));
    }

    @Test
    public void testFindById_NoExiste() {
        when(tipoProductoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tipoProductoService.findById(99L));
    }

    @Test
    public void testFindByNombreLike() {
        when(tipoProductoRepository.findByNombreContainingIgnoreCase("video"))
                .thenReturn(List.of(createTipoProducto()));

        List<TipoProductoModel> tipos = tipoProductoService.findByNombre("video");

        assertNotNull(tipos);
        assertEquals(1, tipos.size());
        assertEquals("Videojuegos", tipos.get(0).getNombre());
    }

    @Test
    public void testFindByNombreExactIgnoreCase() {
        when(tipoProductoRepository.findByNombreIgnoreCase("Videojuegos"))
                .thenReturn(Optional.of(createTipoProducto()));

        TipoProductoModel tipo = tipoProductoService.findByNombreExactIgnoreCase("Videojuegos");

        assertNotNull(tipo);
        assertEquals("Videojuegos", tipo.getNombre());
    }

    @Test
    public void testFindByNombreExactIgnoreCase_NoExiste() {
        when(tipoProductoRepository.findByNombreIgnoreCase("X"))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tipoProductoService.findByNombreExactIgnoreCase("X"));
    }

    @Test
    public void testSave_Exito() {
        TipoProductoModel nuevo = new TipoProductoModel();
        nuevo.setNombre("Accesorios");
        nuevo.setDescripcion("Categoría de accesorios");
        // activo null -> el servicio lo debería setear en true

        when(tipoProductoRepository.existsByNombreIgnoreCase("Accesorios"))
                .thenReturn(false);
        when(tipoProductoRepository.save(any(TipoProductoModel.class)))
                .thenAnswer(inv -> {
                    TipoProductoModel t = inv.getArgument(0);
                    t.setId(1L);
                    return t;
                });

        TipoProductoModel saved = tipoProductoService.save(nuevo);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("Accesorios", saved.getNombre());
        assertTrue(Boolean.TRUE.equals(saved.getActivo()));
    }

    @Test
    public void testSave_NombreObligatorio() {
        TipoProductoModel sinNombre = new TipoProductoModel();
        sinNombre.setNombre("   "); // solo espacios

        assertThrows(IllegalArgumentException.class,
                () -> tipoProductoService.save(sinNombre));
    }

    @Test
    public void testSave_NombreDuplicado() {
        TipoProductoModel tipo = new TipoProductoModel();
        tipo.setNombre("Videojuegos");

        when(tipoProductoRepository.existsByNombreIgnoreCase("Videojuegos"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> tipoProductoService.save(tipo));
    }

    @Test
    public void testUpdate_Exito() {
        TipoProductoModel existente = createTipoProducto(); // nombre: Videojuegos
        TipoProductoModel cambios = new TipoProductoModel();
        cambios.setNombre("Películas");
        cambios.setDescripcion("Categoría para películas");
        cambios.setActivo(false);

        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        // No hay duplicado para "Películas"
        when(tipoProductoRepository.existsByNombreIgnoreCase("Películas")).thenReturn(false);
        when(tipoProductoRepository.save(any(TipoProductoModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        TipoProductoModel actualizado = tipoProductoService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("Películas", actualizado.getNombre());
        assertEquals("Categoría para películas", actualizado.getDescripcion());
        assertFalse(actualizado.getActivo());
    }

    @Test
    public void testUpdate_NombreDuplicado() {
        TipoProductoModel existente = createTipoProducto(); // Videojuegos
        TipoProductoModel cambios = new TipoProductoModel();
        cambios.setNombre("Accesorios");

        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        // Simulamos que ya existe otro tipo con nombre "Accesorios"
        when(tipoProductoRepository.existsByNombreIgnoreCase("Accesorios")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> tipoProductoService.update(1L, cambios));
    }

    @Test
    public void testPatch_DelegandoEnUpdate() {
        TipoProductoModel existente = createTipoProducto(); // Videojuegos
        TipoProductoModel patchData = new TipoProductoModel();
        patchData.setNombre("Películas"); // solo cambia nombre

        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoProductoRepository.existsByNombreIgnoreCase("Películas")).thenReturn(false);
        when(tipoProductoRepository.save(any(TipoProductoModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        TipoProductoModel patched = tipoProductoService.patch(1L, patchData);

        assertNotNull(patched);
        assertEquals("Películas", patched.getNombre());
        assertEquals(existente.getDescripcion(), patched.getDescripcion()); // se mantiene
        assertTrue(Boolean.TRUE.equals(patched.getActivo()));              // se mantiene
    }

    @Test
    public void testDeleteById_ExisteSinProductos() {
        TipoProductoModel tipo = createTipoProducto();

        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(productoRepository.existsByTipoProducto_Id(1L)).thenReturn(false);

        tipoProductoService.deleteById(1L);

        verify(tipoProductoRepository, times(1)).delete(tipo);
    }

    @Test
    public void testDeleteById_NoExiste() {
        when(tipoProductoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tipoProductoService.deleteById(99L));

        verify(tipoProductoRepository, never()).delete(any(TipoProductoModel.class));
    }

    @Test
    public void testDeleteById_ConProductosAsociados() {
        TipoProductoModel tipo = createTipoProducto();

        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(productoRepository.existsByTipoProducto_Id(1L)).thenReturn(true);

        assertThrows(ResponseStatusException.class,
                () -> tipoProductoService.deleteById(1L));

        verify(tipoProductoRepository, never()).delete(any(TipoProductoModel.class));
    }

    @Test
    public void testFindActivos() {
        when(tipoProductoRepository.findByActivoTrue()).thenReturn(List.of(createTipoProducto()));

        List<TipoProductoModel> activos = tipoProductoService.findActivos();

        assertNotNull(activos);
        assertEquals(1, activos.size());
        assertTrue(Boolean.TRUE.equals(activos.get(0).getActivo()));
    }

    @Test
    public void testFindInactivos() {
        TipoProductoModel inactivo = createTipoProducto();
        inactivo.setActivo(false);

        when(tipoProductoRepository.findByActivoFalse()).thenReturn(List.of(inactivo));

        List<TipoProductoModel> inactivos = tipoProductoService.findInactivos();

        assertNotNull(inactivos);
        assertEquals(1, inactivos.size());
        assertFalse(inactivos.get(0).getActivo());
    }

    @Test
    public void testObtenerTipoProductoConNombres() {
        // Fila simulando: id, nombre, descripcion, activo
        Object[] fila = new Object[]{
            1L,
            "Videojuegos",
            "Categoría para videojuegos",
            true
        };

        // Muy importante: tipado explícito
        List<Object[]> filas = List.<Object[]>of(fila);

        when(tipoProductoRepository.obtenerTipoProductoResumen())
                .thenReturn(filas);

        List<Map<String, Object>> resumen = tipoProductoService.obtenerTipoProductoConNombres();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());

        Map<String, Object> row = resumen.get(0);
        assertEquals(1L, row.get("ID"));
        assertEquals("Videojuegos", row.get("Nombre"));
        assertEquals("Categoría para videojuegos", row.get("Descripcion"));
        assertEquals(true, row.get("Activo"));
    }
}