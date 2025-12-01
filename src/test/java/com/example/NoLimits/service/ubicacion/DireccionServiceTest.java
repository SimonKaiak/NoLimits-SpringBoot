package com.example.NoLimits.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.DireccionResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.DireccionUpdateDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.service.ubicacion.DireccionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DireccionServiceTest {

    @Autowired
    private DireccionService direccionService;

    @MockBean
    private DireccionRepository direccionRepository;

    @MockBean
    private ComunaRepository comunaRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private DireccionModel crearDireccionEntity() {
        RegionModel region = new RegionModel();
        region.setId(1L);
        region.setNombre("Región Metropolitana");

        ComunaModel comuna = new ComunaModel();
        comuna.setId(10L);
        comuna.setNombre("Santiago");
        comuna.setRegion(region);

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(5L);
        usuario.setNombre("Juan");

        DireccionModel d = new DireccionModel();
        d.setId(100L);
        d.setCalle("Siempre Viva");
        d.setNumero("742");
        d.setComuna(comuna);
        d.setUsuarioModel(usuario);
        d.setCodigoPostal("8320000");
        d.setActivo(true);
        return d;
    }

    private DireccionRequestDTO crearRequestBasico() {
        DireccionRequestDTO dto = new DireccionRequestDTO();
        dto.setCalle(" Siempre Viva ");
        dto.setNumero(" 742 ");
        dto.setComplemento("Depto 1");
        dto.setCodigoPostal("8320000");
        dto.setComunaId(10L);
        dto.setUsuarioId(5L);
        dto.setActivo(null); // para probar default true
        return dto;
    }

    private DireccionUpdateDTO crearUpdateBasico() {
        DireccionUpdateDTO dto = new DireccionUpdateDTO();
        dto.setCalle("Nueva Calle");
        dto.setNumero("123");
        dto.setComplemento("Casa");
        dto.setCodigoPostal("0000000");
        dto.setComunaId(20L);
        dto.setUsuarioId(6L);
        dto.setActivo(false);
        return dto;
    }

    /* ========== findAll / findById ========== */

    @Test
    public void testFindAll() {
        when(direccionRepository.findAll()).thenReturn(List.of(crearDireccionEntity()));

        List<DireccionResponseDTO> result = direccionService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        DireccionResponseDTO dto = result.get(0);
        assertEquals(100L, dto.getId());
        assertEquals("Siempre Viva", dto.getCalle());
        assertEquals("Santiago", dto.getComuna());
        assertEquals("Región Metropolitana", dto.getRegion());
        verify(direccionRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Existe() {
        DireccionModel entity = crearDireccionEntity();
        when(direccionRepository.findById(100L)).thenReturn(Optional.of(entity));

        DireccionResponseDTO result = direccionService.findById(100L);

        assertNotNull(result);
        assertEquals("Siempre Viva", result.getCalle());
        assertEquals("742", result.getNumero());
        assertEquals("Santiago", result.getComuna());
    }

    @Test
    public void testFindById_NoExiste() {
        when(direccionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> direccionService.findById(999L));
    }

    /* ========== save ========== */

    @Test
    public void testSave_OK() {
        DireccionRequestDTO request = crearRequestBasico();

        ComunaModel comuna = new ComunaModel();
        comuna.setId(10L);
        comuna.setNombre("Santiago");

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(5L);
        usuario.setNombre("Juan");

        when(comunaRepository.findById(10L)).thenReturn(Optional.of(comuna));
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(direccionRepository.save(any(DireccionModel.class)))
                .thenAnswer(invocation -> {
                    DireccionModel d = invocation.getArgument(0);
                    d.setId(100L);
                    return d;
                });

        DireccionResponseDTO result = direccionService.save(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        // debe venir trimmeado
        assertEquals("Siempre Viva", result.getCalle());
        assertEquals("742", result.getNumero());
        assertEquals("Santiago", result.getComuna());
        assertTrue(result.getActivo());
    }

    @Test
    public void testSave_CalleObligatoria() {
        DireccionRequestDTO request = crearRequestBasico();
        request.setCalle("   ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> direccionService.save(request));

        assertTrue(ex.getMessage().contains("La calle es obligatoria"));
        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    @Test
    public void testSave_NumeroObligatorio() {
        DireccionRequestDTO request = crearRequestBasico();
        request.setNumero("   ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> direccionService.save(request));

        assertTrue(ex.getMessage().contains("El número es obligatorio"));
        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    @Test
    public void testSave_ComunaNoEspecificada() {
        DireccionRequestDTO request = crearRequestBasico();
        request.setComunaId(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> direccionService.save(request));

        assertTrue(ex.getMessage().contains("Debe especificar una comuna válida"));
        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    @Test
    public void testSave_ComunaNoEncontrada() {
        DireccionRequestDTO request = crearRequestBasico();
        request.setComunaId(10L);

        when(comunaRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> direccionService.save(request));

        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    @Test
    public void testSave_UsuarioNoEspecificado() {
        DireccionRequestDTO request = crearRequestBasico();
        request.setUsuarioId(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> direccionService.save(request));

        assertTrue(ex.getMessage().contains("Debe especificar un usuario válido"));
        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    @Test
    public void testSave_UsuarioNoEncontrado() {
        DireccionRequestDTO request = crearRequestBasico();

        ComunaModel comuna = new ComunaModel();
        comuna.setId(10L);
        when(comunaRepository.findById(10L)).thenReturn(Optional.of(comuna));
        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> direccionService.save(request));

        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    /* ========== update (PUT) ========== */

    @Test
    public void testUpdate_OK() {
        DireccionModel existente = crearDireccionEntity();
        DireccionUpdateDTO update = crearUpdateBasico();

        ComunaModel nuevaComuna = new ComunaModel();
        nuevaComuna.setId(20L);
        nuevaComuna.setNombre("Puente Alto");

        UsuarioModel nuevoUsuario = new UsuarioModel();
        nuevoUsuario.setId(6L);
        nuevoUsuario.setNombre("Pedro");

        when(direccionRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(comunaRepository.findById(20L)).thenReturn(Optional.of(nuevaComuna));
        when(usuarioRepository.findById(6L)).thenReturn(Optional.of(nuevoUsuario));
        when(direccionRepository.save(any(DireccionModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DireccionResponseDTO result = direccionService.update(100L, update);

        assertNotNull(result);
        assertEquals("Nueva Calle", result.getCalle());
        assertEquals("123", result.getNumero());
        assertEquals("Puente Alto", result.getComuna());
        assertEquals(false, result.getActivo());
    }

    @Test
    public void testUpdate_CalleObligatoria() {
        DireccionModel existente = crearDireccionEntity();
        DireccionUpdateDTO update = crearUpdateBasico();
        update.setCalle("   ");

        when(direccionRepository.findById(100L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> direccionService.update(100L, update));

        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    /* ========== patch ========== */

    @Test
    public void testPatch_CambiaSoloCalleYNumero() {
        DireccionModel existente = crearDireccionEntity();

        DireccionUpdateDTO patch = new DireccionUpdateDTO();
        patch.setCalle("Otra Calle");
        patch.setNumero("999");

        when(direccionRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(direccionRepository.save(any(DireccionModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DireccionResponseDTO result = direccionService.patch(100L, patch);

        assertNotNull(result);
        assertEquals("Otra Calle", result.getCalle());
        assertEquals("999", result.getNumero());
        // comuna y región se mantienen
        assertEquals("Santiago", result.getComuna());
    }

    @Test
    public void testPatch_ComunaNoEncontrada_LanzaRecursoNoEncontrado() {
        DireccionModel existente = crearDireccionEntity();

        DireccionUpdateDTO patch = new DireccionUpdateDTO();
        patch.setComunaId(99L);

        when(direccionRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> direccionService.patch(100L, patch));

        verify(direccionRepository, never()).save(any(DireccionModel.class));
    }

    /* ========== deleteById ========== */

    @Test
    public void testDeleteById_OK() {
        DireccionModel existente = crearDireccionEntity();
        when(direccionRepository.findById(100L)).thenReturn(Optional.of(existente));

        direccionService.deleteById(100L);

        verify(direccionRepository, times(1)).delete(existente);
    }

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(direccionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> direccionService.deleteById(999L));

        verify(direccionRepository, never()).delete(any(DireccionModel.class));
    }
}