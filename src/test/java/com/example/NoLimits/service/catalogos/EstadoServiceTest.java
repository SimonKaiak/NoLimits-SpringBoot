package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.EstadoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EstadoUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.service.catalogos.EstadoService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class EstadoServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private EstadoService estadoService;

    @MockBean
    private EstadoRepository estadoRepository;

    // ================== HELPERS ==================

    private EstadoModel entity() {
        EstadoModel e = new EstadoModel();
        e.setId(1L);
        e.setNombre("Activo");
        e.setDescripcion("Estado activo");
        e.setActivo(true);
        return e;
    }

    private EstadoRequestDTO req(String nombre, String desc, Boolean activo) {
        EstadoRequestDTO dto = new EstadoRequestDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(desc);
        dto.setActivo(activo);
        return dto;
    }

    private EstadoUpdateDTO upd(String nombre, String desc, Boolean activo) {
        EstadoUpdateDTO dto = new EstadoUpdateDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(desc);
        dto.setActivo(activo);
        return dto;
    }

    // ================== FIND ==================

    @Test
    public void testFindAll() {
        when(estadoRepository.findAll()).thenReturn(List.of(entity()));
        List<EstadoResponseDTO> lista = estadoService.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Activo", lista.get(0).getNombre());
    }

    @Test
    public void testFindById_Existe() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        EstadoResponseDTO dto = estadoService.findById(1L);
        assertNotNull(dto);
        assertEquals("Activo", dto.getNombre());
    }

    @Test
    public void testFindById_NoExiste_Lanza404() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> estadoService.findById(99L));
    }

    // ================== SAVE ==================

    @Test
    public void testSave_OK() {
        when(estadoRepository.findByNombreIgnoreCase("Pendiente")).thenReturn(Optional.empty());
        when(estadoRepository.save(any())).thenAnswer(inv -> {
            EstadoModel e = inv.getArgument(0);
            e.setId(2L);
            return e;
        });
        EstadoResponseDTO dto = estadoService.save(req("Pendiente", "En espera", true));
        assertNotNull(dto);
        assertEquals("Pendiente", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testSave_ActivoNull_DefaultTrue() {
        when(estadoRepository.findByNombreIgnoreCase("Pendiente")).thenReturn(Optional.empty());
        when(estadoRepository.save(any())).thenAnswer(inv -> {
            EstadoModel e = inv.getArgument(0);
            e.setId(2L);
            return e;
        });
        EstadoResponseDTO dto = estadoService.save(req("Pendiente", "Desc", null));
        assertTrue(dto.getActivo());
    }

    @Test
    public void testSave_ActivoFalse_RespetaValor() {
        when(estadoRepository.findByNombreIgnoreCase("Pendiente")).thenReturn(Optional.empty());
        when(estadoRepository.save(any())).thenAnswer(inv -> {
            EstadoModel e = inv.getArgument(0);
            e.setId(2L);
            return e;
        });
        EstadoResponseDTO dto = estadoService.save(req("Pendiente", "Desc", false));
        assertFalse(dto.getActivo());
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> estadoService.save(req("  ", "Desc", true)));
        verify(estadoRepository, never()).save(any());
    }

    @Test
    public void testSave_NombreDuplicado_LanzaIllegalArgument() {
        EstadoModel otro = entity();
        otro.setId(99L); // diferente ID → es duplicado
        when(estadoRepository.findByNombreIgnoreCase("Activo")).thenReturn(Optional.of(otro));
        assertThrows(IllegalArgumentException.class, () -> estadoService.save(req("Activo", "Desc", true)));
    }

    // ================== UPDATE ==================

    @Test
    public void testUpdate_OK_CambiaNombreDescripcionYActivo() {
        EstadoModel existente = entity();
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.findByNombreIgnoreCase("Inactivo")).thenReturn(Optional.empty());
        when(estadoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EstadoResponseDTO dto = estadoService.update(1L, req("Inactivo", "Desc nueva", false));
        assertEquals("Inactivo", dto.getNombre());
        assertFalse(dto.getActivo());
    }

    @Test
    public void testUpdate_MismoNombre_NoVerificaDuplicado() {
        EstadoModel existente = entity(); // nombre = "Activo", id = 1L
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        // mismo nombre y mismo id → no debe lanzar
        when(estadoRepository.findByNombreIgnoreCase("Activo")).thenReturn(Optional.of(existente));
        when(estadoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EstadoResponseDTO dto = estadoService.update(1L, req("Activo", "Desc nueva", false));
        assertEquals("Activo", dto.getNombre());
    }

    @Test
    public void testUpdate_ActivoNull_MantieneValorAnterior() {
        EstadoModel existente = entity(); // activo = true
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.findByNombreIgnoreCase("Inactivo")).thenReturn(Optional.empty());
        when(estadoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EstadoResponseDTO dto = estadoService.update(1L, req("Inactivo", "Desc", null));
        assertTrue(dto.getActivo()); // activo no cambió
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class, () -> estadoService.update(1L, req("  ", "Desc", true)));
    }

    @Test
    public void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        EstadoModel existente = entity();
        EstadoModel otro = new EstadoModel();
        otro.setId(99L);
        otro.setNombre("Inactivo");
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.findByNombreIgnoreCase("Inactivo")).thenReturn(Optional.of(otro));
        assertThrows(IllegalArgumentException.class, () -> estadoService.update(1L, req("Inactivo", "Desc", true)));
    }

    @Test
    public void testUpdate_NoExiste_Lanza404() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> estadoService.update(99L, req("X", null, null)));
    }

    // ================== PATCH ==================

    @Test
    public void testPatch_CambiaSoloNombre() {
        EstadoModel existente = entity();
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.findByNombreIgnoreCase("Inactivo")).thenReturn(Optional.empty());
        when(estadoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EstadoResponseDTO dto = estadoService.patch(1L, upd("Inactivo", null, null));
        assertEquals("Inactivo", dto.getNombre());
        assertEquals("Estado activo", dto.getDescripcion()); // no cambió
        assertTrue(dto.getActivo()); // no cambió
    }

    @Test
    public void testPatch_CambiaSoloDescripcion() {
        EstadoModel existente = entity();
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EstadoResponseDTO dto = estadoService.patch(1L, upd(null, "Nueva descripción", null));
        assertEquals("Activo", dto.getNombre()); // no cambió
        assertEquals("Nueva descripción", dto.getDescripcion());
    }

    @Test
    public void testPatch_CambiaSoloActivo() {
        EstadoModel existente = entity();
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EstadoResponseDTO dto = estadoService.patch(1L, upd(null, null, false));
        assertFalse(dto.getActivo());
        assertEquals("Activo", dto.getNombre()); // no cambió
    }

    @Test
    public void testPatch_TodosNulos_NoModificaNada() {
        EstadoModel existente = entity();
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EstadoResponseDTO dto = estadoService.patch(1L, upd(null, null, null));
        assertEquals("Activo", dto.getNombre());
        assertEquals("Estado activo", dto.getDescripcion());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class, () -> estadoService.patch(1L, upd("  ", null, null)));
    }

    @Test
    public void testPatch_NombreDuplicado_LanzaIllegalArgument() {
        EstadoModel existente = entity();
        EstadoModel otro = new EstadoModel();
        otro.setId(99L);
        otro.setNombre("Inactivo");
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.findByNombreIgnoreCase("Inactivo")).thenReturn(Optional.of(otro));
        assertThrows(IllegalArgumentException.class, () -> estadoService.patch(1L, upd("Inactivo", null, null)));
    }

    @Test
    public void testPatch_NoExiste_Lanza404() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> estadoService.patch(99L, upd("X", null, null)));
    }

    // ================== DELETE ==================

    @Test
    public void testDeleteById_Existe() {
        when(estadoRepository.existsById(1L)).thenReturn(true);
        estadoService.deleteById(1L);
        verify(estadoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_Lanza404() {
        when(estadoRepository.existsById(99L)).thenReturn(false);
        assertThrows(RecursoNoEncontradoException.class, () -> estadoService.deleteById(99L));
        verify(estadoRepository, never()).deleteById(any());
    }

    // ================== BÚSQUEDAS ==================

    @Test
    public void testFindByNombreLike_Ok() {
        when(estadoRepository.findByNombreContainingIgnoreCase("acti")).thenReturn(List.of(entity()));
        List<EstadoResponseDTO> lista = estadoService.findByNombreLike("acti");
        assertEquals(1, lista.size());
    }

    @Test
    public void testFindByNombreLike_Null_RetornaListaVacia() {
        List<EstadoResponseDTO> lista = estadoService.findByNombreLike(null);
        assertTrue(lista.isEmpty());
        verify(estadoRepository, never()).findByNombreContainingIgnoreCase(anyString());
    }

    @Test
    public void testFindByNombreLike_Vacio_RetornaListaVacia() {
        List<EstadoResponseDTO> lista = estadoService.findByNombreLike("  ");
        assertTrue(lista.isEmpty());
        verify(estadoRepository, never()).findByNombreContainingIgnoreCase(anyString());
    }

    @Test
    public void testFindByNombreExact_OK() {
        when(estadoRepository.findByNombreIgnoreCase("Activo")).thenReturn(Optional.of(entity()));
        EstadoResponseDTO dto = estadoService.findByNombreExact("Activo");
        assertNotNull(dto);
        assertEquals("Activo", dto.getNombre());
    }

    @Test
    public void testFindByNombreExact_Null_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> estadoService.findByNombreExact(null));
    }

    @Test
    public void testFindByNombreExact_Blank_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> estadoService.findByNombreExact("  "));
    }

    @Test
    public void testFindByNombreExact_NoExiste_Lanza404() {
        when(estadoRepository.findByNombreIgnoreCase("Inexistente")).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> estadoService.findByNombreExact("Inexistente"));
    }

    @Test
    public void testFindActivos() {
        when(estadoRepository.findByActivoTrue()).thenReturn(List.of(entity()));
        List<EstadoResponseDTO> lista = estadoService.findActivos();
        assertEquals(1, lista.size());
        assertTrue(lista.get(0).getActivo());
    }

    @Test
    public void testFindInactivos() {
        EstadoModel inactivo = entity();
        inactivo.setActivo(false);
        when(estadoRepository.findByActivoFalse()).thenReturn(List.of(inactivo));
        List<EstadoResponseDTO> lista = estadoService.findInactivos();
        assertEquals(1, lista.size());
        assertFalse(lista.get(0).getActivo());
    }

    @Test
    public void testObtenerEstadosResumen() {
        List<Object[]> resultados = new ArrayList<>();
        resultados.add(new Object[]{1L, "Activo", "Estado activo", true});
        when(estadoRepository.obtenerEstadosResumen()).thenReturn(resultados);

        List<Map<String, Object>> resumen = estadoService.obtenerEstadosResumen();
        assertNotNull(resumen);
        assertEquals(1, resumen.size());
        assertEquals(1L, resumen.get(0).get("ID"));
        assertEquals("Activo", resumen.get(0).get("Nombre"));
    }

    // ================== PAGINACIÓN ==================

    @Test
    public void testListarPaginado_SinFiltro() {
        Page<EstadoModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(estadoRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = estadoService.listarPaginado(1, 10, null);
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }

    @Test
    public void testListarPaginado_ConFiltro() {
        Page<EstadoModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(estadoRepository.findByNombreContainingIgnoreCase(any(), any(Pageable.class))).thenReturn(page);

        var resultado = estadoService.listarPaginado(1, 10, "acti");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }

    @Test
    public void testListarPaginado_FiltroBlanco_UsaTodos() {
        Page<EstadoModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(estadoRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = estadoService.listarPaginado(1, 10, "   ");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }
}