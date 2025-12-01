package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.EstadoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EstadoUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.service.catalogos.EstadoService;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@SpringBootTest
@ActiveProfiles("test")
public class EstadoServiceTest {

    @Autowired
    private EstadoService estadoService;

    @MockBean
    private EstadoRepository estadoRepository;

    // ==========================
    // HELPERS
    // ==========================

    private EstadoModel createEstadoModel() {
        EstadoModel e = new EstadoModel();
        e.setId(1L);
        e.setNombre("PAGADA");
        e.setDescripcion("Pago confirmado por la pasarela");
        e.setActivo(true);
        return e;
    }

    private EstadoRequestDTO createRequest(String nombre, String descripcion, Boolean activo) {
        EstadoRequestDTO dto = new EstadoRequestDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setActivo(activo);
        return dto;
    }

    private EstadoUpdateDTO createUpdate(String nombre, String descripcion, Boolean activo) {
        EstadoUpdateDTO dto = new EstadoUpdateDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setActivo(activo);
        return dto;
    }

    // ==========================
    // findAll / findById
    // ==========================

    @Test
    public void testFindAll() {
        when(estadoRepository.findAll()).thenReturn(List.of(createEstadoModel()));

        List<EstadoResponseDTO> estados = estadoService.findAll();

        assertNotNull(estados);
        assertEquals(1, estados.size());
        EstadoResponseDTO dto = estados.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("PAGADA", dto.getNombre());
        assertEquals("Pago confirmado por la pasarela", dto.getDescripcion());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testFindById_Existe() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(createEstadoModel()));

        EstadoResponseDTO dto = estadoService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("PAGADA", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testFindById_NoExiste_Lanza404() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> estadoService.findById(99L));

        verify(estadoRepository, times(1)).findById(99L);
    }

    // ==========================
    // save (POST)
    // ==========================

    @Test
    public void testSave_OK() {
        EstadoRequestDTO entrada = createRequest("  ENVIADA  ",
                "Pedido enviado al cliente", true);

        when(estadoRepository.findByNombreIgnoreCase("ENVIADA"))
                .thenReturn(Optional.empty());

        when(estadoRepository.save(any(EstadoModel.class)))
                .thenAnswer(invocation -> {
                    EstadoModel e = invocation.getArgument(0);
                    e.setId(1L);
                    return e;
                });

        EstadoResponseDTO guardado = estadoService.save(entrada);

        assertNotNull(guardado);
        assertEquals(1L, guardado.getId());
        assertEquals("ENVIADA", guardado.getNombre());
        assertEquals("Pedido enviado al cliente", guardado.getDescripcion());
        assertTrue(guardado.getActivo());
    }

    @Test
    public void testSave_ActivoNull_DefaultTrue() {
        EstadoRequestDTO entrada = createRequest("EN PROCESO",
                "Pedido en preparación", null);

        when(estadoRepository.findByNombreIgnoreCase("EN PROCESO"))
                .thenReturn(Optional.empty());

        when(estadoRepository.save(any(EstadoModel.class)))
                .thenAnswer(invocation -> {
                    EstadoModel e = invocation.getArgument(0);
                    e.setId(2L);
                    return e;
                });

        EstadoResponseDTO guardado = estadoService.save(entrada);

        assertNotNull(guardado);
        assertEquals(2L, guardado.getId());
        assertEquals("EN PROCESO", guardado.getNombre());
        assertTrue(guardado.getActivo(),
                "Cuando activo viene null en el DTO, debe quedar true por defecto");
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgument() {
        EstadoRequestDTO entrada = createRequest("   ", "desc", true);

        assertThrows(IllegalArgumentException.class,
                () -> estadoService.save(entrada));

        verify(estadoRepository, never()).save(any(EstadoModel.class));
    }

    @Test
    public void testSave_NombreDuplicado_LanzaIllegalArgument() {
        EstadoRequestDTO entrada = createRequest("PAGADA", "desc", true);

        when(estadoRepository.findByNombreIgnoreCase("PAGADA"))
                .thenReturn(Optional.of(createEstadoModel()));

        assertThrows(IllegalArgumentException.class,
                () -> estadoService.save(entrada));
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    public void testUpdate_OK_CambiaNombreDescripcionYActivo() {
        EstadoModel original = createEstadoModel(); // PAGADA, activo=true
        EstadoRequestDTO cambios = createRequest("ENVIADA",
                "Pedido enviado", false);

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));
        when(estadoRepository.findByNombreIgnoreCase("ENVIADA"))
                .thenReturn(Optional.empty());
        when(estadoRepository.save(any(EstadoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EstadoResponseDTO actualizado = estadoService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals(1L, actualizado.getId());
        assertEquals("ENVIADA", actualizado.getNombre());
        assertEquals("Pedido enviado", actualizado.getDescripcion());
        assertFalse(actualizado.getActivo());
    }

    @Test
    public void testUpdate_ActivoNull_MantieneValorAnterior() {
        EstadoModel original = createEstadoModel(); // activo=true
        EstadoRequestDTO cambios = createRequest("PAGADA",
                "Pago confirmado por la pasarela", null);

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));
        // mismo nombre: validarYNormalizarNombre no debería lanzar duplicado
        when(estadoRepository.findByNombreIgnoreCase("PAGADA"))
                .thenReturn(Optional.of(original));
        when(estadoRepository.save(any(EstadoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EstadoResponseDTO actualizado = estadoService.update(1L, cambios);

        assertNotNull(actualizado);
        assertTrue(actualizado.getActivo(),
                "Si activo viene null en PUT, debe mantenerse el valor anterior");
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        EstadoModel original = createEstadoModel();
        EstadoRequestDTO cambios = createRequest("   ", "desc", true);

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));

        assertThrows(IllegalArgumentException.class,
                () -> estadoService.update(1L, cambios));

        verify(estadoRepository, never()).save(any(EstadoModel.class));
    }

    @Test
    public void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        EstadoModel original = createEstadoModel();
        EstadoRequestDTO cambios = createRequest("ENVIADA", "desc", true);

        EstadoModel otro = new EstadoModel();
        otro.setId(2L);
        otro.setNombre("ENVIADA");

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));
        when(estadoRepository.findByNombreIgnoreCase("ENVIADA"))
                .thenReturn(Optional.of(otro));

        assertThrows(IllegalArgumentException.class,
                () -> estadoService.update(1L, cambios));
    }

    // ==========================
    // patch (PATCH)
    // ==========================

    @Test
    public void testPatch_CambiaSoloNombre() {
        EstadoModel original = createEstadoModel(); // PAGADA
        EstadoUpdateDTO cambios = createUpdate("ENVIADA", null, null);

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));
        when(estadoRepository.findByNombreIgnoreCase("ENVIADA"))
                .thenReturn(Optional.empty());
        when(estadoRepository.save(any(EstadoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EstadoResponseDTO actualizado = estadoService.patch(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("ENVIADA", actualizado.getNombre());
        assertEquals("Pago confirmado por la pasarela", actualizado.getDescripcion());
        assertTrue(actualizado.getActivo());
    }

    @Test
    public void testPatch_CambiaSoloDescripcion() {
        EstadoModel original = createEstadoModel(); // desc original
        EstadoUpdateDTO cambios = createUpdate(null, "Nueva descripción", null);

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));
        when(estadoRepository.save(any(EstadoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EstadoResponseDTO actualizado = estadoService.patch(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("PAGADA", actualizado.getNombre());
        assertEquals("Nueva descripción", actualizado.getDescripcion());
        assertTrue(actualizado.getActivo());
    }

    @Test
    public void testPatch_CambiaSoloActivo() {
        EstadoModel original = createEstadoModel(); // activo=true
        EstadoUpdateDTO cambios = createUpdate(null, null, false);

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));
        when(estadoRepository.save(any(EstadoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EstadoResponseDTO actualizado = estadoService.patch(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("PAGADA", actualizado.getNombre());
        assertFalse(actualizado.getActivo());
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        EstadoModel original = createEstadoModel();
        EstadoUpdateDTO cambios = createUpdate("   ", null, null);

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));

        assertThrows(IllegalArgumentException.class,
                () -> estadoService.patch(1L, cambios));

        verify(estadoRepository, never()).save(any(EstadoModel.class));
    }

    @Test
    public void testPatch_NombreDuplicado_LanzaIllegalArgument() {
        EstadoModel original = createEstadoModel();

        EstadoModel otro = new EstadoModel();
        otro.setId(2L);
        otro.setNombre("ENVIADA");

        EstadoUpdateDTO cambios = createUpdate("ENVIADA", null, null);

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(original));
        when(estadoRepository.findByNombreIgnoreCase("ENVIADA"))
                .thenReturn(Optional.of(otro));

        assertThrows(IllegalArgumentException.class,
                () -> estadoService.patch(1L, cambios));
    }

    // ==========================
    // deleteById
    // ==========================

    @Test
    public void testDeleteById_Existe() {
        when(estadoRepository.existsById(1L)).thenReturn(true);

        estadoService.deleteById(1L);

        verify(estadoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_Lanza404() {
        when(estadoRepository.existsById(99L)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> estadoService.deleteById(99L));

        verify(estadoRepository, never()).deleteById(anyLong());
    }

    // ==========================
    // findByNombreLike
    // ==========================

    @Test
    public void testFindByNombreLike_Ok() {
        when(estadoRepository.findByNombreContainingIgnoreCase("PAG"))
                .thenReturn(List.of(createEstadoModel()));

        List<EstadoResponseDTO> lista = estadoService.findByNombreLike("PAG");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("PAGADA", lista.get(0).getNombre());
    }

    @Test
    public void testFindByNombreLike_Null_RetornaListaVacia() {
        List<EstadoResponseDTO> lista = estadoService.findByNombreLike(null);

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    @Test
    public void testFindByNombreLike_Vacio_RetornaListaVacia() {
        List<EstadoResponseDTO> lista = estadoService.findByNombreLike("   ");

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    // ==========================
    // findByNombreExact
    // ==========================

    @Test
    public void testFindByNombreExact_OK() {
        when(estadoRepository.findByNombreIgnoreCase("PAGADA"))
                .thenReturn(Optional.of(createEstadoModel()));

        EstadoResponseDTO dto = estadoService.findByNombreExact("PAGADA");

        assertNotNull(dto);
        assertEquals("PAGADA", dto.getNombre());
    }

    @Test
    public void testFindByNombreExact_Null_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> estadoService.findByNombreExact(null));
    }

    @Test
    public void testFindByNombreExact_Blank_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> estadoService.findByNombreExact("   "));
    }

    @Test
    public void testFindByNombreExact_NoExiste_Lanza404() {
        when(estadoRepository.findByNombreIgnoreCase("PENDIENTE"))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> estadoService.findByNombreExact("PENDIENTE"));
    }

    // ==========================
    // findActivos / findInactivos
    // ==========================

    @Test
    public void testFindActivos() {
        EstadoModel e1 = createEstadoModel();
        when(estadoRepository.findByActivoTrue())
                .thenReturn(List.of(e1));

        List<EstadoResponseDTO> activos = estadoService.findActivos();

        assertNotNull(activos);
        assertEquals(1, activos.size());
        assertTrue(activos.get(0).getActivo());
    }

    @Test
    public void testFindInactivos() {
        EstadoModel e = createEstadoModel();
        e.setId(2L);
        e.setNombre("CANCELADA");
        e.setActivo(false);

        when(estadoRepository.findByActivoFalse())
                .thenReturn(List.of(e));

        List<EstadoResponseDTO> inactivos = estadoService.findInactivos();

        assertNotNull(inactivos);
        assertEquals(1, inactivos.size());
        assertFalse(inactivos.get(0).getActivo());
        assertEquals("CANCELADA", inactivos.get(0).getNombre());
    }

    // ==========================
    // obtenerEstadosResumen
    // ==========================

    @Test
    public void testObtenerEstadosResumen() {
        Object[] fila = new Object[] { 1L, "PAGADA", "Pago confirmado", true };

        // Lista tipada correctamente como List<Object[]>
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        filas.add(fila);

        when(estadoRepository.obtenerEstadosResumen()).thenReturn(filas);

        java.util.List<java.util.Map<String, Object>> resumen = estadoService.obtenerEstadosResumen();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());

        java.util.Map<String, Object> row = resumen.get(0);
        assertEquals(1L, row.get("ID"));
        assertEquals("PAGADA", row.get("Nombre"));
        assertEquals("Pago confirmado", row.get("Descripcion"));
        assertEquals(true, row.get("Activo"));
    }
}