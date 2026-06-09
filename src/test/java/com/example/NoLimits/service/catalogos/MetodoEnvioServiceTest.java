package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoEnvioRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoEnvioUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoEnvioRepository;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoEnvioService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class MetodoEnvioServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @MockBean
    private MetodoEnvioRepository metodoEnvioRepository;

    // ==========================
    // HELPERS
    // ==========================

    private MetodoEnvioModel entity() {
        MetodoEnvioModel m = new MetodoEnvioModel();
        m.setId(1L);
        m.setNombre("Despacho a domicilio");
        m.setDescripcion("Entrega en la casa del cliente");
        m.setActivo(true);
        return m;
    }

    private MetodoEnvioRequestDTO req(String nombre, String desc, Boolean activo) {
        MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(desc);
        dto.setActivo(activo);
        return dto;
    }

    private MetodoEnvioUpdateDTO upd(String nombre, String desc, Boolean activo) {
        MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(desc);
        dto.setActivo(activo);
        return dto;
    }

    // ==========================
    // findAll / findById
    // ==========================

    @Test
    public void testFindAll() {
        when(metodoEnvioRepository.findAll()).thenReturn(List.of(entity()));
        List<MetodoEnvioResponseDTO> lista = metodoEnvioService.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Despacho a domicilio", lista.get(0).getNombre());
    }

    @Test
    public void testFindById_Existe() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(entity()));
        MetodoEnvioResponseDTO dto = metodoEnvioService.findById(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testFindById_NoExiste() {
        when(metodoEnvioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> metodoEnvioService.findById(99L));
    }

    // ==========================
    // create (POST)
    // ==========================

    @Test
    public void testCreate_OK() {
        when(metodoEnvioRepository.save(any())).thenAnswer(inv -> {
            MetodoEnvioModel m = inv.getArgument(0);
            m.setId(1L);
            return m;
        });
        MetodoEnvioResponseDTO dto = metodoEnvioService.create(req("Retiro en tienda", "En sucursal", true));
        assertNotNull(dto);
        assertEquals("Retiro en tienda", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testCreate_ActivoNull_DefaultTrue() {
        when(metodoEnvioRepository.save(any())).thenAnswer(inv -> {
            MetodoEnvioModel m = inv.getArgument(0);
            m.setId(1L);
            return m;
        });
        MetodoEnvioResponseDTO dto = metodoEnvioService.create(req("Retiro en tienda", null, null));
        assertNotNull(dto);
        assertTrue(dto.getActivo());
    }

    @Test
    public void testCreate_ActivoFalse_RespetaValor() {
        when(metodoEnvioRepository.save(any())).thenAnswer(inv -> {
            MetodoEnvioModel m = inv.getArgument(0);
            m.setId(1L);
            return m;
        });
        MetodoEnvioResponseDTO dto = metodoEnvioService.create(req("Retiro en tienda", null, false));
        assertNotNull(dto);
        assertFalse(dto.getActivo());
    }

    @Test
    public void testCreate_NombreVacio_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> metodoEnvioService.create(req("  ", null, null)));
        verify(metodoEnvioRepository, never()).save(any());
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    public void testUpdate_OK() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoEnvioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoEnvioResponseDTO dto = metodoEnvioService.update(1L,
                req("Nuevo nombre", "Nueva desc", false));
        assertEquals("Nuevo nombre", dto.getNombre());
        assertFalse(dto.getActivo());
    }

    @Test
    public void testUpdate_ActivoNull_MantieneSinCambio() {
        MetodoEnvioModel existente = entity(); // activo = true
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoEnvioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoEnvioResponseDTO dto = metodoEnvioService.update(1L,
                req("Nuevo nombre", null, null));
        assertEquals("Nuevo nombre", dto.getNombre());
        assertTrue(dto.getActivo()); // activo no cambió
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class,
                () -> metodoEnvioService.update(1L, req("  ", null, null)));
        verify(metodoEnvioRepository, never()).save(any());
    }

    @Test
    public void testUpdate_NoExiste_Lanza404() {
        when(metodoEnvioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class,
                () -> metodoEnvioService.update(99L, req("X", null, null)));
    }

    // ==========================
    // patch (PATCH)
    // ==========================

    @Test
    public void testPatch_CambiaSoloNombre() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoEnvioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoEnvioResponseDTO dto = metodoEnvioService.patch(1L, upd("Nuevo nombre", null, null));
        assertEquals("Nuevo nombre", dto.getNombre());
        assertEquals("Entrega en la casa del cliente", dto.getDescripcion()); // no cambió
    }

    @Test
    public void testPatch_CambiaDescripcion() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoEnvioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoEnvioResponseDTO dto = metodoEnvioService.patch(1L, upd(null, "Nueva descripción", null));
        assertEquals("Despacho a domicilio", dto.getNombre()); // no cambió
        assertEquals("Nueva descripción", dto.getDescripcion());
    }

    @Test
    public void testPatch_CambiaActivo() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoEnvioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoEnvioResponseDTO dto = metodoEnvioService.patch(1L, upd(null, null, false));
        assertFalse(dto.getActivo());
        assertEquals("Despacho a domicilio", dto.getNombre()); // no cambió
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class,
                () -> metodoEnvioService.patch(1L, upd("  ", null, null)));
    }

    @Test
    public void testPatch_NoExiste_Lanza404() {
        when(metodoEnvioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class,
                () -> metodoEnvioService.patch(99L, upd("X", null, null)));
    }

    @Test
    public void testPatch_TodosNulos_NoModificaNada() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoEnvioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoEnvioResponseDTO dto = metodoEnvioService.patch(1L, upd(null, null, null));
        assertEquals("Despacho a domicilio", dto.getNombre());
        assertEquals("Entrega en la casa del cliente", dto.getDescripcion());
        assertTrue(dto.getActivo());
    }

    // ==========================
    // deleteById
    // ==========================

    @Test
    public void testDeleteById_Existe() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(entity()));
        metodoEnvioService.deleteById(1L);
        verify(metodoEnvioRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_Lanza404() {
        when(metodoEnvioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> metodoEnvioService.deleteById(99L));
        verify(metodoEnvioRepository, never()).deleteById(any());
    }

    // ==========================
    // findAllPaged
    // ==========================

    @Test
    public void testFindAllPaged_SinFiltro() {
        Page<MetodoEnvioModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(metodoEnvioRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = metodoEnvioService.findAllPaged(1, 10, null);
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }

    @Test
    public void testFindAllPaged_ConFiltro() {
        Page<MetodoEnvioModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(metodoEnvioRepository.findByNombreContainingIgnoreCase(any(), any(Pageable.class)))
                .thenReturn(page);

        var resultado = metodoEnvioService.findAllPaged(1, 10, "despacho");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }

    @Test
    public void testFindAllPaged_FiltroBlanco_UsaTodos() {
        Page<MetodoEnvioModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(metodoEnvioRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = metodoEnvioService.findAllPaged(1, 10, "   ");
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }
}