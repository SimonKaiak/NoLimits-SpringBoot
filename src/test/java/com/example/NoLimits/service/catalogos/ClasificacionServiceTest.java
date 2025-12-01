package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.ClasificacionRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.ClasificacionResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.ClasificacionUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.repository.catalogos.ClasificacionRepository;
import com.example.NoLimits.Multimedia.service.catalogos.ClasificacionService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
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

    // ================== HELPERS ==================

    private ClasificacionModel createClasificacion() {
        ClasificacionModel c = new ClasificacionModel();
        c.setId(1L);
        c.setNombre("T");
        c.setDescripcion("Contenido apto para adolescentes.");
        c.setActivo(true);
        return c;
    }

    private ClasificacionRequestDTO createRequestDTO() {
        ClasificacionRequestDTO dto = new ClasificacionRequestDTO();
        dto.setNombre("T");
        dto.setDescripcion("Contenido apto para adolescentes.");
        dto.setActivo(true);
        return dto;
    }

    private ClasificacionUpdateDTO createUpdateDTO() {
        ClasificacionUpdateDTO dto = new ClasificacionUpdateDTO();
        dto.setNombre("M");
        dto.setDescripcion("Solo para adultos.");
        dto.setActivo(false);
        return dto;
    }

    // ================== TESTS CRUD BÁSICO ==================

    @Test
    public void testFindAll() {
        when(clasificacionRepository.findAll()).thenReturn(List.of(createClasificacion()));

        List<ClasificacionResponseDTO> lista = clasificacionService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("T", lista.get(0).getNombre());
    }

    @Test
    public void testFindById_Exito() {
        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(createClasificacion()));

        ClasificacionResponseDTO dto = clasificacionService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("T", dto.getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(clasificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> clasificacionService.findById(99L));
    }

    @Test
    public void testCreate_Exito() {
        ClasificacionRequestDTO request = new ClasificacionRequestDTO();
        request.setNombre("  E  ");
        request.setDescripcion("Para todo público.");
        // activo null → se usa el valor por defecto del modelo

        when(clasificacionRepository.existsByNombreIgnoreCase("E")).thenReturn(false);
        when(clasificacionRepository.save(any(ClasificacionModel.class)))
                .thenAnswer(inv -> {
                    ClasificacionModel m = inv.getArgument(0);
                    m.setId(1L);
                    return m;
                });

        ClasificacionResponseDTO respuesta = clasificacionService.create(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("E", respuesta.getNombre()); // nombre normalizado
        assertEquals("Para todo público.", respuesta.getDescripcion());
    }

    @Test
    public void testCreate_NombreNull() {
        ClasificacionRequestDTO request = new ClasificacionRequestDTO();
        request.setNombre(null);

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.create(request));
    }

    @Test
    public void testCreate_NombreVacio() {
        ClasificacionRequestDTO request = new ClasificacionRequestDTO();
        request.setNombre("   ");

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.create(request));
    }

    @Test
    public void testCreate_NombreDuplicado() {
        ClasificacionRequestDTO request = new ClasificacionRequestDTO();
        request.setNombre("T");

        when(clasificacionRepository.existsByNombreIgnoreCase("T")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.create(request));
    }

    @Test
    public void testUpdate_Exito() {
        ClasificacionModel existente = createClasificacion();

        ClasificacionRequestDTO cambios = new ClasificacionRequestDTO();
        cambios.setNombre("M");
        cambios.setDescripcion("Solo para adultos.");
        cambios.setActivo(false);

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.existsByNombreIgnoreCase("M")).thenReturn(false);
        when(clasificacionRepository.save(any(ClasificacionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ClasificacionResponseDTO actualizada = clasificacionService.update(1L, cambios);

        assertNotNull(actualizada);
        assertEquals("M", actualizada.getNombre());
        assertEquals("Solo para adultos.", actualizada.getDescripcion());
        assertFalse(actualizada.getActivo());
    }

    @Test
    public void testUpdate_NombreDuplicado() {
        ClasificacionModel existente = createClasificacion(); // nombre T

        ClasificacionRequestDTO cambios = new ClasificacionRequestDTO();
        cambios.setNombre("E");
        cambios.setDescripcion("Otra");
        cambios.setActivo(true);

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.existsByNombreIgnoreCase("E")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.update(1L, cambios));
    }

    @Test
    public void testUpdate_ActivoObligatorio() {
        ClasificacionModel existente = createClasificacion();

        ClasificacionRequestDTO cambios = new ClasificacionRequestDTO();
        cambios.setNombre("M");
        cambios.setDescripcion("Solo para adultos.");
        cambios.setActivo(null); // en PUT es obligatorio

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));

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

    // ================== TESTS BÚSQUEDAS ESPECÍFICAS ==================

    @Test
    public void testFindByNombreContainingIgnoreCase() {
        when(clasificacionRepository.findByNombreContainingIgnoreCase("t"))
                .thenReturn(List.of(createClasificacion()));

        List<ClasificacionResponseDTO> lista =
                clasificacionService.findByNombreContainingIgnoreCase("t");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("T", lista.get(0).getNombre());
    }

    @Test
    public void testFindByNombreExactIgnoreCase_Exito() {
        when(clasificacionRepository.findByNombreIgnoreCase("t"))
                .thenReturn(Optional.of(createClasificacion()));

        ClasificacionResponseDTO dto = clasificacionService.findByNombreExactIgnoreCase("t");

        assertNotNull(dto);
        assertEquals("T", dto.getNombre());
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

        List<ClasificacionResponseDTO> lista = clasificacionService.findActivas();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertTrue(lista.get(0).getActivo());
    }

    @Test
    public void testFindInactivas() {
        ClasificacionModel inactiva = createClasificacion();
        inactiva.setActivo(false);

        when(clasificacionRepository.findByActivoFalse())
                .thenReturn(List.of(inactiva));

        List<ClasificacionResponseDTO> lista = clasificacionService.findInactivas();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertFalse(lista.get(0).getActivo());
    }

    // ================== TEST RESUMEN ==================

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

        Map<String, Object> item = resumen.get(0);
        assertEquals(1L, item.get("ID"));
        assertEquals("T", item.get("Nombre"));
        assertEquals("Contenido apto para adolescentes.", item.get("Descripcion"));
        assertEquals(true, item.get("Activo"));
    }

    // ================== TESTS PATCH (DTO) ==================

    @Test
    public void testPatch_DescripcionYActivo() {
        ClasificacionModel existente = createClasificacion();

        ClasificacionUpdateDTO cambios = new ClasificacionUpdateDTO();
        cambios.setDescripcion("Actualizada: solo para adultos.");
        cambios.setActivo(false);

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.save(any(ClasificacionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ClasificacionResponseDTO actualizada = clasificacionService.patch(1L, cambios);

        assertNotNull(actualizada);
        assertEquals("T", actualizada.getNombre()); // nombre se mantiene
        assertEquals("Actualizada: solo para adultos.", actualizada.getDescripcion());
        assertFalse(actualizada.getActivo());
    }

    @Test
    public void testPatch_CambiaNombre_Exito() {
        ClasificacionModel existente = createClasificacion();

        ClasificacionUpdateDTO cambios = new ClasificacionUpdateDTO();
        cambios.setNombre("M");

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.existsByNombreIgnoreCase("M")).thenReturn(false);
        when(clasificacionRepository.save(any(ClasificacionModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ClasificacionResponseDTO actualizada = clasificacionService.patch(1L, cambios);

        assertNotNull(actualizada);
        assertEquals("M", actualizada.getNombre());
        assertEquals("Contenido apto para adolescentes.", actualizada.getDescripcion());
        assertTrue(actualizada.getActivo());
    }

    @Test
    public void testPatch_NombreDuplicado() {
        ClasificacionModel existente = createClasificacion();

        ClasificacionUpdateDTO cambios = new ClasificacionUpdateDTO();
        cambios.setNombre("E");

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.existsByNombreIgnoreCase("E")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.patch(1L, cambios));
    }

    @Test
    public void testPatch_NombreVacio() {
        ClasificacionModel existente = createClasificacion();

        ClasificacionUpdateDTO cambios = new ClasificacionUpdateDTO();
        cambios.setNombre("   "); // blanco

        when(clasificacionRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> clasificacionService.patch(1L, cambios));
    }

    @Test
    public void testPatch_IdNoExiste() {
        ClasificacionUpdateDTO cambios = new ClasificacionUpdateDTO();
        cambios.setDescripcion("No importa");

        when(clasificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> clasificacionService.patch(99L, cambios));
    }
}