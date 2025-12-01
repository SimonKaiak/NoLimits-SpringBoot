package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposEmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TiposEmpresaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposEmpresaRepository;
import com.example.NoLimits.Multimedia.service.catalogos.TiposEmpresaService;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class TiposEmpresaServiceTest {

    @Autowired
    private TiposEmpresaService tiposEmpresaService;

    @MockBean
    private TiposEmpresaRepository tiposEmpresaRepository;

    @MockBean
    private EmpresaRepository empresaRepository;

    @MockBean
    private TipoEmpresaRepository tipoEmpresaRepository;

    // ==========================
    // Helpers
    // ==========================

    private EmpresaModel empresa() {
        EmpresaModel e = new EmpresaModel();
        e.setId(1L);
        e.setNombre("Sony");
        return e;
    }

    private TipoEmpresaModel tipoEmpresa() {
        TipoEmpresaModel t = new TipoEmpresaModel();
        t.setId(2L);
        t.setNombre("Publisher");
        return t;
    }

    private TiposEmpresaModel relacion() {
        TiposEmpresaModel rel = new TiposEmpresaModel();
        rel.setId(10L);
        rel.setEmpresa(empresa());
        rel.setTipoEmpresa(tipoEmpresa());
        return rel;
    }

    private TiposEmpresaUpdateDTO updateDto(Long empresaId, Long tipoId) {
        TiposEmpresaUpdateDTO dto = new TiposEmpresaUpdateDTO();
        dto.setEmpresaId(empresaId);
        dto.setTipoEmpresaId(tipoId);
        return dto;
    }

    // ==========================
    // findAllByEmpresa
    // ==========================

    @Test
    public void testFindAllByEmpresa() {
        TiposEmpresaModel rel = relacion();
        // una relación para la empresa 1 y otra para otra empresa para comprobar el filtro
        TiposEmpresaModel relOtra = new TiposEmpresaModel();
        relOtra.setId(11L);
        EmpresaModel otraEmpresa = new EmpresaModel();
        otraEmpresa.setId(99L);
        relOtra.setEmpresa(otraEmpresa);
        relOtra.setTipoEmpresa(tipoEmpresa());

        when(tiposEmpresaRepository.findAll())
                .thenReturn(List.of(rel, relOtra));

        List<TiposEmpresaResponseDTO> resultado =
                tiposEmpresaService.findAllByEmpresa(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        TiposEmpresaResponseDTO dto = resultado.get(0);
        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getEmpresaId());
        assertEquals(2L, dto.getTipoEmpresaId());
        assertEquals("Publisher", dto.getTipoEmpresaNombre());
    }

    // ==========================
    // link
    // ==========================

    @Test
    public void testLink_CreaRelacionCuandoNoExiste() {
        EmpresaModel emp = empresa();
        TipoEmpresaModel tipo = tipoEmpresa();

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(tipoEmpresaRepository.findById(2L)).thenReturn(Optional.of(tipo));
        when(tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(1L, 2L))
                .thenReturn(false);
        when(tiposEmpresaRepository.save(any(TiposEmpresaModel.class)))
                .thenAnswer(invocation -> {
                    TiposEmpresaModel rel = invocation.getArgument(0);
                    rel.setId(10L);
                    return rel;
                });

        TiposEmpresaResponseDTO res = tiposEmpresaService.link(1L, 2L);

        assertNotNull(res);
        assertEquals(10L, res.getId());
        assertEquals(1L, res.getEmpresaId());
        assertEquals(2L, res.getTipoEmpresaId());
        assertEquals("Publisher", res.getTipoEmpresaNombre());
        verify(tiposEmpresaRepository, times(1)).save(any(TiposEmpresaModel.class));
    }

    @Test
    public void testLink_LanzaExcepcionSiEmpresaNoExiste() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tiposEmpresaService.link(1L, 2L));

        verify(tiposEmpresaRepository, never()).save(any(TiposEmpresaModel.class));
    }

    @Test
    public void testLink_LanzaExcepcionSiTipoEmpresaNoExiste() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa()));
        when(tipoEmpresaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tiposEmpresaService.link(1L, 2L));

        verify(tiposEmpresaRepository, never()).save(any(TiposEmpresaModel.class));
    }

    @Test
    public void testLink_LanzaExcepcionSiRelacionYaExiste() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa()));
        when(tipoEmpresaRepository.findById(2L)).thenReturn(Optional.of(tipoEmpresa()));
        when(tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(1L, 2L))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> tiposEmpresaService.link(1L, 2L));

        verify(tiposEmpresaRepository, never()).save(any(TiposEmpresaModel.class));
    }

    // ==========================
    // unlink
    // ==========================

    @Test
    public void testUnlink_EliminaCuandoExiste() {
        TiposEmpresaModel rel = relacion();

        when(tiposEmpresaRepository.findByEmpresa_IdAndTipoEmpresa_Id(1L, 2L))
                .thenReturn(Optional.of(rel));

        tiposEmpresaService.unlink(1L, 2L);

        verify(tiposEmpresaRepository, times(1)).delete(rel);
    }

    @Test
    public void testUnlink_LanzaExcepcionSiNoExiste() {
        when(tiposEmpresaRepository.findByEmpresa_IdAndTipoEmpresa_Id(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tiposEmpresaService.unlink(1L, 2L));

        verify(tiposEmpresaRepository, never()).delete(any(TiposEmpresaModel.class));
    }

    // ==========================
    // patch
    // ==========================

    @Test
    public void testPatch_CambiaEmpresa() {
        TiposEmpresaModel rel = relacion();
        EmpresaModel nuevaEmpresa = new EmpresaModel();
        nuevaEmpresa.setId(99L);
        nuevaEmpresa.setNombre("Microsoft");

        when(tiposEmpresaRepository.findById(10L))
                .thenReturn(Optional.of(rel));
        when(empresaRepository.findById(99L))
                .thenReturn(Optional.of(nuevaEmpresa));
        when(tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(99L, 2L))
                .thenReturn(false);
        when(tiposEmpresaRepository.save(any(TiposEmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TiposEmpresaUpdateDTO dto = updateDto(99L, null);

        TiposEmpresaResponseDTO res = tiposEmpresaService.patch(10L, dto);

        assertNotNull(res);
        assertEquals(10L, res.getId());
        assertEquals(99L, res.getEmpresaId());
        assertEquals(2L, res.getTipoEmpresaId());
    }

    @Test
    public void testPatch_CambiaTipoEmpresa() {
        TiposEmpresaModel rel = relacion();
        TipoEmpresaModel nuevoTipo = new TipoEmpresaModel();
        nuevoTipo.setId(5L);
        nuevoTipo.setNombre("Distribuidora");

        when(tiposEmpresaRepository.findById(10L))
                .thenReturn(Optional.of(rel));
        when(tipoEmpresaRepository.findById(5L))
                .thenReturn(Optional.of(nuevoTipo));
        when(tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(1L, 5L))
                .thenReturn(false);
        when(tiposEmpresaRepository.save(any(TiposEmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TiposEmpresaUpdateDTO dto = updateDto(null, 5L);

        TiposEmpresaResponseDTO res = tiposEmpresaService.patch(10L, dto);

        assertNotNull(res);
        assertEquals(10L, res.getId());
        assertEquals(1L, res.getEmpresaId());
        assertEquals(5L, res.getTipoEmpresaId());
        assertEquals("Distribuidora", res.getTipoEmpresaNombre());
    }

    @Test
    public void testPatch_CambiaEmpresa_Duplicada_LanzaIllegalArgument() {
        TiposEmpresaModel rel = relacion();

        when(tiposEmpresaRepository.findById(10L))
                .thenReturn(Optional.of(rel));
        when(empresaRepository.findById(99L))
                .thenReturn(Optional.of(empresa()));
        when(tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(99L, 2L))
                .thenReturn(true);

        TiposEmpresaUpdateDTO dto = updateDto(99L, null);

        assertThrows(IllegalArgumentException.class,
                () -> tiposEmpresaService.patch(10L, dto));

        verify(tiposEmpresaRepository, never()).save(any(TiposEmpresaModel.class));
    }

    @Test
    public void testPatch_CambiaTipo_Duplicada_LanzaIllegalArgument() {
        TiposEmpresaModel rel = relacion();
        TipoEmpresaModel nuevoTipo = tipoEmpresa();
        nuevoTipo.setId(5L);

        when(tiposEmpresaRepository.findById(10L))
                .thenReturn(Optional.of(rel));
        when(tipoEmpresaRepository.findById(5L))
                .thenReturn(Optional.of(nuevoTipo));
        when(tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(1L, 5L))
                .thenReturn(true);

        TiposEmpresaUpdateDTO dto = updateDto(null, 5L);

        assertThrows(IllegalArgumentException.class,
                () -> tiposEmpresaService.patch(10L, dto));

        verify(tiposEmpresaRepository, never()).save(any(TiposEmpresaModel.class));
    }

    @Test
    public void testPatch_NoExisteRelacion_LanzaRecursoNoEncontrado() {
        TiposEmpresaUpdateDTO dto = updateDto(99L, 5L);

        when(tiposEmpresaRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tiposEmpresaService.patch(10L, dto));
    }
}