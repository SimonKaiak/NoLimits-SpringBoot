package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoProductoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoProductoUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoProductoRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.catalogos.TipoProductoService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

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
public class TipoProductoServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private TipoProductoService tipoProductoService;

    @MockBean
    private TipoProductoRepository tipoProductoRepository;

    @MockBean
    private ProductoRepository productoRepository;

    // ================== HELPERS ==================

    private TipoProductoModel entity() {
        TipoProductoModel m = new TipoProductoModel();
        m.setId(1L);
        m.setNombre("Videojuego");
        m.setDescripcion("Juego digital");
        m.setActivo(true);
        return m;
    }

    private TipoProductoRequestDTO req(String nombre, String desc, Boolean activo) {
        TipoProductoRequestDTO dto = new TipoProductoRequestDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(desc);
        dto.setActivo(activo);
        return dto;
    }

    private TipoProductoUpdateDTO upd(String nombre, String desc, Boolean activo) {
        TipoProductoUpdateDTO dto = new TipoProductoUpdateDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(desc);
        dto.setActivo(activo);
        return dto;
    }

    // ================== FIND ==================

    @Test
    public void testFindAll() {
        when(tipoProductoRepository.findAll()).thenReturn(List.of(entity()));
        List<TipoProductoResponseDTO> lista = tipoProductoService.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Videojuego", lista.get(0).getNombre());
    }

    @Test
    public void testFindById() {
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        TipoProductoResponseDTO dto = tipoProductoService.findById(1L);
        assertNotNull(dto);
        assertEquals("Videojuego", dto.getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(tipoProductoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> tipoProductoService.findById(99L));
    }

    @Test
    public void testFindByNombreLike() {
        when(tipoProductoRepository.findByNombreContainingIgnoreCase("video"))
                .thenReturn(List.of(entity()));
        List<TipoProductoResponseDTO> lista = tipoProductoService.findByNombre("video");
        assertEquals(1, lista.size());
    }

    @Test
    public void testFindByNombreExactIgnoreCase() {
        when(tipoProductoRepository.findByNombreIgnoreCase("Videojuego"))
                .thenReturn(Optional.of(entity()));
        TipoProductoResponseDTO dto = tipoProductoService.findByNombreExactIgnoreCase("Videojuego");
        assertNotNull(dto);
        assertEquals("Videojuego", dto.getNombre());
    }

    @Test
    public void testFindByNombreExactIgnoreCase_NoExiste() {
        when(tipoProductoRepository.findByNombreIgnoreCase("X")).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class,
                () -> tipoProductoService.findByNombreExactIgnoreCase("X"));
    }

    // ================== SAVE ==================

    @Test
    public void testSave_Exito() {
        when(tipoProductoRepository.existsByNombreIgnoreCase("Accesorio")).thenReturn(false);
        when(tipoProductoRepository.save(any())).thenAnswer(inv -> {
            TipoProductoModel m = inv.getArgument(0);
            m.setId(2L);
            return m;
        });
        TipoProductoResponseDTO dto = tipoProductoService.save(req("Accesorio", "Accesorios varios", true));
        assertNotNull(dto);
        assertEquals("Accesorio", dto.getNombre());
    }

    @Test
    public void testSave_ActivoNull_DefaultTrue() {
        when(tipoProductoRepository.existsByNombreIgnoreCase("Accesorio")).thenReturn(false);
        when(tipoProductoRepository.save(any())).thenAnswer(inv -> {
            TipoProductoModel m = inv.getArgument(0);
            m.setId(2L);
            return m;
        });
        TipoProductoResponseDTO dto = tipoProductoService.save(req("Accesorio", "Desc", null));
        assertNotNull(dto);
        assertTrue(dto.getActivo());
    }

    @Test
    public void testSave_NombreObligatorio() {
        assertThrows(IllegalArgumentException.class,
                () -> tipoProductoService.save(req(null, "Desc", true)));
        verify(tipoProductoRepository, never()).save(any());
    }

    @Test
    public void testSave_NombreDuplicado() {
        when(tipoProductoRepository.existsByNombreIgnoreCase("Videojuego")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> tipoProductoService.save(req("Videojuego", "Desc", true)));
    }

    @Test
    public void testSave_MismoNombreQueEntidadExistente_NoDuplicado() {
        // Si se guarda una nueva entidad (id null), y el nombre existe en BD → lanza excepción
        when(tipoProductoRepository.existsByNombreIgnoreCase("Videojuego")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> tipoProductoService.save(req("Videojuego", "Desc", true)));
    }

    // ================== UPDATE ==================

    @Test
    public void testUpdate_Exito() {
        TipoProductoModel existente = entity();
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoProductoRepository.existsByNombreIgnoreCase("Hardware")).thenReturn(false);
        when(tipoProductoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoProductoResponseDTO dto = tipoProductoService.update(1L, req("Hardware", "Desc hardware", false));
        assertEquals("Hardware", dto.getNombre());
        assertFalse(dto.getActivo());
    }

    @Test
    public void testUpdate_MismoNombre_NoVerificaDuplicado() {
        TipoProductoModel existente = entity(); // nombre = "Videojuego"
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoProductoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Mismo nombre → no debe llamar a existsByNombreIgnoreCase
        TipoProductoResponseDTO dto = tipoProductoService.update(1L, req("Videojuego", "Desc actualizada", true));
        assertEquals("Videojuego", dto.getNombre());
        verify(tipoProductoRepository, never()).existsByNombreIgnoreCase(anyString());
    }

    @Test
    public void testUpdate_NombreDuplicado() {
        TipoProductoModel existente = entity();
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoProductoRepository.existsByNombreIgnoreCase("Hardware")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> tipoProductoService.update(1L, req("Hardware", "Desc", true)));
    }

    @Test
    public void testUpdate_NoExiste() {
        when(tipoProductoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class,
                () -> tipoProductoService.update(99L, req("X", "D", true)));
    }

    // ================== PATCH ==================

    @Test
    public void testPatch_CambiaSoloNombre_OK() {
        TipoProductoModel existente = entity();
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoProductoRepository.existsByNombreIgnoreCase("Hardware")).thenReturn(false);
        when(tipoProductoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoProductoResponseDTO dto = tipoProductoService.patch(1L, upd("Hardware", null, null));
        assertEquals("Hardware", dto.getNombre());
        assertEquals("Juego digital", dto.getDescripcion()); // no cambió
        assertTrue(dto.getActivo()); // no cambió
    }

    @Test
    public void testPatch_CambiaSoloDescripcion() {
        TipoProductoModel existente = entity();
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoProductoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoProductoResponseDTO dto = tipoProductoService.patch(1L, upd(null, "Nueva descripción", null));
        assertEquals("Videojuego", dto.getNombre()); // no cambió
        assertEquals("Nueva descripción", dto.getDescripcion());
    }

    @Test
    public void testPatch_CambiaSoloActivo() {
        TipoProductoModel existente = entity();
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoProductoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TipoProductoResponseDTO dto = tipoProductoService.patch(1L, upd(null, null, false));
        assertEquals("Videojuego", dto.getNombre()); // no cambió
        assertFalse(dto.getActivo());
    }

    @Test
    public void testPatch_NombreDuplicado_LanzaIllegalArgument() {
        TipoProductoModel existente = entity();
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoProductoRepository.existsByNombreIgnoreCase("Hardware")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> tipoProductoService.patch(1L, upd("Hardware", null, null)));
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        TipoProductoModel existente = entity();
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> tipoProductoService.patch(1L, upd("  ", null, null)));
    }

    // ================== DELETE ==================

    @Test
    public void testDeleteById_ExisteSinProductos() {
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(productoRepository.existsByTipoProducto_Id(1L)).thenReturn(false);

        tipoProductoService.deleteById(1L);
        verify(tipoProductoRepository, times(1)).delete(any());
    }

    @Test
    public void testDeleteById_NoExiste() {
        when(tipoProductoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> tipoProductoService.deleteById(99L));
    }

    @Test
    public void testDeleteById_ConProductosAsociados() {
        when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(productoRepository.existsByTipoProducto_Id(1L)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> tipoProductoService.deleteById(1L));
        verify(tipoProductoRepository, never()).delete(any());
    }

    // ================== BÚSQUEDAS POR ESTADO ==================

    @Test
    public void testFindActivos() {
        when(tipoProductoRepository.findByActivoTrue()).thenReturn(List.of(entity()));
        List<TipoProductoResponseDTO> lista = tipoProductoService.findActivos();
        assertEquals(1, lista.size());
        assertTrue(lista.get(0).getActivo());
    }

    @Test
    public void testFindInactivos() {
        TipoProductoModel inactivo = entity();
        inactivo.setActivo(false);
        when(tipoProductoRepository.findByActivoFalse()).thenReturn(List.of(inactivo));
        List<TipoProductoResponseDTO> lista = tipoProductoService.findInactivos();
        assertEquals(1, lista.size());
        assertFalse(lista.get(0).getActivo());
    }

    // ================== RESUMEN ==================

    @Test
    public void testObtenerTipoProductoConNombres() {
        List<Object[]> resultados = new ArrayList<>();
        resultados.add(new Object[]{1L, "Videojuego", "Desc", true});
        when(tipoProductoRepository.obtenerTipoProductoResumen()).thenReturn(resultados);

        List<Map<String, Object>> resumen = tipoProductoService.obtenerTipoProductoConNombres();
        assertNotNull(resumen);
        assertEquals(1, resumen.size());
        assertEquals(1L, resumen.get(0).get("ID"));
        assertEquals("Videojuego", resumen.get(0).get("Nombre"));
    }

    // ================== PAGINACIÓN ==================

    @Test
    public void testListarPaginado_SinFiltro() {
        Page<TipoProductoModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(tipoProductoRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = tipoProductoService.listarPaginado(1, 10, null);
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }

    @Test
    public void testListarPaginado_ConFiltro() {
        Page<TipoProductoModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(tipoProductoRepository.findByNombreContainingIgnoreCase(any(), any(Pageable.class)))
                .thenReturn(page);

        var resultado = tipoProductoService.listarPaginado(1, 10, "video");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }

    @Test
    public void testListarPaginado_FiltroBlanco_UsaTodos() {
        Page<TipoProductoModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(tipoProductoRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = tipoProductoService.listarPaginado(1, 10, "  ");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }
}