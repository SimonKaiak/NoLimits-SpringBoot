package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoEmpresaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoEmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposEmpresaRepository;
import com.example.NoLimits.Multimedia.service.catalogos.TipoEmpresaService;

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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class TipoEmpresaServiceTest {

    @Autowired
    private TipoEmpresaService tipoEmpresaService;

    @MockBean
    private TipoEmpresaRepository tipoEmpresaRepository;

    @MockBean
    private TiposEmpresaRepository tiposEmpresaRepository;

    // ==========================
    // Helpers
    // ==========================

    private TipoEmpresaModel tipoEmpresaEntity() {
        TipoEmpresaModel t = new TipoEmpresaModel();
        t.setId(1L);
        t.setNombre("Publisher");
        return t;
    }

    private TipoEmpresaRequestDTO request(String nombre) {
        TipoEmpresaRequestDTO dto = new TipoEmpresaRequestDTO();
        dto.setNombre(nombre);
        return dto;
    }

    private TipoEmpresaUpdateDTO update(String nombre) {
        TipoEmpresaUpdateDTO dto = new TipoEmpresaUpdateDTO();
        dto.setNombre(nombre);
        return dto;
    }

    // ==========================
    // findAll / findById
    // ==========================

    @Test
    public void testFindAll() {
        when(tipoEmpresaRepository.findAll())
                .thenReturn(List.of(tipoEmpresaEntity()));

        List<TipoEmpresaResponseDTO> resultado = tipoEmpresaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        TipoEmpresaResponseDTO dto = resultado.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Publisher", dto.getNombre());
    }

    @Test
    public void testFindById_Existe() {
        when(tipoEmpresaRepository.findById(1L))
                .thenReturn(Optional.of(tipoEmpresaEntity()));

        TipoEmpresaResponseDTO dto = tipoEmpresaService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Publisher", dto.getNombre());
    }

    @Test
    public void testFindById_NoExiste_LanzaRecursoNoEncontrado() {
        when(tipoEmpresaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tipoEmpresaService.findById(1L));
    }

    // ==========================
    // save
    // ==========================

    @Test
    public void testSave_Valido() {
        TipoEmpresaRequestDTO entrada = request("Publisher");

        when(tipoEmpresaRepository.save(any(TipoEmpresaModel.class)))
                .thenAnswer(invocation -> {
                    TipoEmpresaModel m = invocation.getArgument(0);
                    m.setId(1L);
                    return m;
                });

        TipoEmpresaResponseDTO resultado = tipoEmpresaService.save(entrada);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Publisher", resultado.getNombre());
        verify(tipoEmpresaRepository, times(1)).save(any(TipoEmpresaModel.class));
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    public void testUpdate_CambiaNombre() {
        TipoEmpresaModel existente = tipoEmpresaEntity();
        TipoEmpresaRequestDTO entrada = request("Distribuidora");

        when(tipoEmpresaRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(tipoEmpresaRepository.save(any(TipoEmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TipoEmpresaResponseDTO resultado = tipoEmpresaService.update(1L, entrada);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Distribuidora", resultado.getNombre());
    }

    @Test
    public void testUpdate_NoExiste_LanzaRecursoNoEncontrado() {
        TipoEmpresaRequestDTO entrada = request("Distribuidora");

        when(tipoEmpresaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tipoEmpresaService.update(1L, entrada));
    }

    // ==========================
    // patch (PATCH)
    // ==========================

    @Test
    public void testPatch_CambiaNombre() {
        TipoEmpresaModel existente = tipoEmpresaEntity();
        TipoEmpresaUpdateDTO dtoUpdate = update("Distribuidora");

        when(tipoEmpresaRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(tipoEmpresaRepository.save(any(TipoEmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TipoEmpresaResponseDTO resultado = tipoEmpresaService.patch(1L, dtoUpdate);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Distribuidora", resultado.getNombre());
    }

    @Test
    public void testPatch_SinNombre_NoCambia() {
        TipoEmpresaModel existente = tipoEmpresaEntity();
        TipoEmpresaUpdateDTO dtoUpdate = update(null);

        when(tipoEmpresaRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(tipoEmpresaRepository.save(any(TipoEmpresaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TipoEmpresaResponseDTO resultado = tipoEmpresaService.patch(1L, dtoUpdate);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Publisher", resultado.getNombre());
    }

    @Test
    public void testPatch_NoExiste_LanzaRecursoNoEncontrado() {
        TipoEmpresaUpdateDTO dtoUpdate = update("Distribuidora");

        when(tipoEmpresaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> tipoEmpresaService.patch(1L, dtoUpdate));
    }

    // ==========================
    // deleteById
    // ==========================

    @Test
    public void testDeleteById_SinRelaciones_Elimina() {
        when(tiposEmpresaRepository.existsByTipoEmpresa_Id(1L))
                .thenReturn(false);

        tipoEmpresaService.deleteById(1L);

        verify(tipoEmpresaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_ConRelaciones_LanzaIllegalState() {
        when(tiposEmpresaRepository.existsByTipoEmpresa_Id(1L))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> tipoEmpresaService.deleteById(1L));

        verify(tipoEmpresaRepository, never()).deleteById(anyLong());
    }
}