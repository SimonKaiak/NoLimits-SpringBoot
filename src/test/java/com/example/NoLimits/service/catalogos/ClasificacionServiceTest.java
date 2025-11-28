package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.repository.catalogos.ClasificacionRepository;
import com.example.NoLimits.Multimedia.service.catalogos.ClasificacionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ClasificacionServiceTest {

    @Autowired
    private ClasificacionService clasificacionService;

    @MockBean
    private ClasificacionRepository clasificacionRepository;

    private ClasificacionModel createClasificacion() {
        ClasificacionModel c = new ClasificacionModel();
        c.setId(1L);
        c.setNombre("T");
        c.setDescripcion("Contenido apto para adolescentes.");
        c.setActivo(true);
        return c;
    }

    @Test
    public void testFindAll() {
        when(clasificacionRepository.findAll()).thenReturn(List.of(createClasificacion()));

        List<ClasificacionModel> lista = clasificacionService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("T", lista.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(createClasificacion()));

        ClasificacionModel c = clasificacionService.findById(1L);

        assertNotNull(c);
        assertEquals(1L, c.getId());
        assertEquals("T", c.getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(clasificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> clasificacionService.findById(99L));
    }

    @Test
    public void testSave_Exito() {
        ClasificacionModel nueva = new ClasificacionModel();
        nueva.setNombre("E");
        nueva.setDescripcion("Para todo público.");
        nueva.setActivo(true);

        when(clasificacionRepository.existsByNombreIgnoreCase("E")).thenReturn(false);
        when(clasificacionRepository.save(any(ClasificacionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ClasificacionModel guardada = clasificacionService.save(nueva);

        assertNotNull(guardada);
        assertEquals("E", guardada.getNombre());
        assertTrue(guardada.isActivo());
    }

    @Test
    public void testSave_NombreVacio() {
        ClasificacionModel c = new ClasificacionModel();
        c.setNombre("  "); // blanco

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.save(c));
    }

    @Test
    public void testSave_NombreDuplicado() {
        ClasificacionModel c = new ClasificacionModel();
        c.setNombre("T");

        when(clasificacionRepository.existsByNombreIgnoreCase("T"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.save(c));
    }

    @Test
    public void testUpdate() {
        ClasificacionModel existente = createClasificacion();

        ClasificacionModel cambios = new ClasificacionModel();
        cambios.setNombre("M");
        cambios.setDescripcion("Solo para adultos.");
        cambios.setActivo(false);

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.existsByNombreIgnoreCase("M")).thenReturn(false);
        when(clasificacionRepository.save(any(ClasificacionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ClasificacionModel actualizada = clasificacionService.update(1L, cambios);

        assertNotNull(actualizada);
        assertEquals("M", actualizada.getNombre());
        assertEquals("Solo para adultos.", actualizada.getDescripcion());
        assertFalse(actualizada.isActivo());
    }

    @Test
    public void testUpdate_NombreDuplicado() {
        ClasificacionModel existente = createClasificacion(); // nombre T

        ClasificacionModel cambios = new ClasificacionModel();
        cambios.setNombre("E"); // nuevo nombre que ya existe
        cambios.setDescripcion("Otra");
        cambios.setActivo(true);

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.existsByNombreIgnoreCase("E")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.update(1L, cambios));
    }

    @Test
    public void testDeleteById() {
        ClasificacionModel existente = createClasificacion();

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        doNothing().when(clasificacionRepository).delete(existente);

        clasificacionService.deleteById(1L);

        verify(clasificacionRepository, times(1)).delete(existente);
    }

    @Test
    public void testFindByNombreContainingIgnoreCase() {
        when(clasificacionRepository.findByNombreContainingIgnoreCase("t"))
                .thenReturn(List.of(createClasificacion()));

        List<ClasificacionModel> lista =
                clasificacionService.findByNombreContainingIgnoreCase("t");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("T", lista.get(0).getNombre());
    }

    @Test
    public void testFindByNombreExactIgnoreCase_Exito() {
        when(clasificacionRepository.findByNombreIgnoreCase("t"))
                .thenReturn(Optional.of(createClasificacion()));

        ClasificacionModel c = clasificacionService.findByNombreExactIgnoreCase("t");

        assertNotNull(c);
        assertEquals("T", c.getNombre());
    }

    @Test
    public void testFindByNombreExactIgnoreCase_NoExiste() {
        when(clasificacionRepository.findByNombreIgnoreCase("x"))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> clasificacionService.findByNombreExactIgnoreCase("x"));
    }

    @Test
    public void testFindActivas() {
        when(clasificacionRepository.findByActivoTrue())
                .thenReturn(List.of(createClasificacion()));

        List<ClasificacionModel> lista = clasificacionService.findActivas();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertTrue(lista.get(0).isActivo());
    }

    @Test
    public void testFindInactivas() {
        ClasificacionModel inactiva = createClasificacion();
        inactiva.setActivo(false);

        when(clasificacionRepository.findByActivoFalse())
                .thenReturn(List.of(inactiva));

        List<ClasificacionModel> lista = clasificacionService.findInactivas();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertFalse(lista.get(0).isActivo());
    }

    @Test
    public void testObtenerClasificacionesConDatos() {
        Object[] fila = new Object[] {
                1L,
                "T",
                "Contenido apto para adolescentes.",
                true
        };

        when(clasificacionRepository.obtenerClasificacionesResumen())
                .thenReturn(Collections.singletonList(fila));

        List<Map<String, Object>> resumen =
                clasificacionService.obtenerClasificacionesConDatos();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());

        var item = resumen.get(0);
        assertEquals(1L, item.get("ID"));
        assertEquals("T", item.get("Nombre"));
        assertEquals("Contenido apto para adolescentes.", item.get("Descripcion"));
        assertEquals(true, item.get("Activo"));
    }

    // ================== TESTS PATCH ==================

    @Test
    public void testPatch_DescripcionYActivo() {
        ClasificacionModel existente = createClasificacion();

        Map<String, Object> campos = new HashMap<>();
        campos.put("descripcion", "Actualizada: solo para adultos.");
        campos.put("activo", false);

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.save(any(ClasificacionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ClasificacionModel actualizada = clasificacionService.patch(1L, campos);

        assertNotNull(actualizada);
        assertEquals("T", actualizada.getNombre()); // nombre se mantiene
        assertEquals("Actualizada: solo para adultos.", actualizada.getDescripcion());
        assertFalse(actualizada.isActivo());
    }

    @Test
    public void testPatch_CambiaNombre_Exito() {
        ClasificacionModel existente = createClasificacion();

        Map<String, Object> campos = new HashMap<>();
        campos.put("nombre", "M");

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.existsByNombreIgnoreCase("M")).thenReturn(false);
        when(clasificacionRepository.save(any(ClasificacionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ClasificacionModel actualizada = clasificacionService.patch(1L, campos);

        assertNotNull(actualizada);
        assertEquals("M", actualizada.getNombre());
        assertEquals("Contenido apto para adolescentes.", actualizada.getDescripcion());
        assertTrue(actualizada.isActivo());
    }

    @Test
    public void testPatch_NombreDuplicado() {
        ClasificacionModel existente = createClasificacion();

        Map<String, Object> campos = new HashMap<>();
        campos.put("nombre", "E");

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.existsByNombreIgnoreCase("E")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.patch(1L, campos));
    }

    @Test
    public void testPatch_NombreVacio() {
        ClasificacionModel existente = createClasificacion();

        Map<String, Object> campos = new HashMap<>();
        campos.put("nombre", "  "); // blanco

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.patch(1L, campos));
    }

    @Test
    public void testPatch_ActivoTipoIncorrecto() {
        ClasificacionModel existente = createClasificacion();

        Map<String, Object> campos = new HashMap<>();
        campos.put("activo", "si"); // no boolean

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.patch(1L, campos));
    }

    @Test
    public void testPatch_IdNoExiste() {
        Map<String, Object> campos = new HashMap<>();
        campos.put("descripcion", "No importa");

        when(clasificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> clasificacionService.patch(99L, campos));
    }
}