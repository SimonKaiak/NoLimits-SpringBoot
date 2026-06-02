package com.example.NoLimits.service.usuario;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.usuario.request.RolRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.RolUpdateDTO;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.service.usuario.RolService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * RolServiceTest — Pruebas unitarias del servicio de roles.
 *
 * Cubre: findAll, findById, save, update, patch, deleteById.
 */
@SpringBootTest
@ActiveProfiles("test")
public class RolServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private RolService rolService;

    @MockBean
    private RolRepository rolRepository;

    // ===================== HELPERS =====================

    private RolModel crearRolModel() {
        RolModel r = new RolModel();
        r.setId(1L);
        r.setNombre("ADMIN");
        r.setDescripcion("Rol administrador");
        r.setActivo(true);
        return r;
    }

    private RolRequestDTO crearRequestDTO() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("CLIENTE");
        dto.setDescripcion("Rol cliente");
        dto.setActivo(true);
        return dto;
    }

    private RolUpdateDTO crearUpdateDTO() {
        RolUpdateDTO dto = new RolUpdateDTO();
        dto.setNombre("VENDEDOR");
        dto.setDescripcion("Rol vendedor");
        dto.setActivo(false);
        return dto;
    }

    // ===================== FIND ALL =====================

    @Test
    public void testFindAll_DevuelveLista() {
        when(rolRepository.findAll()).thenReturn(List.of(crearRolModel()));

        List<RolResponseDTO> result = rolService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        RolResponseDTO dto = result.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("ADMIN", dto.getNombre());
        assertEquals("Rol administrador", dto.getDescripcion());
        assertTrue(dto.getActivo());
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_ListaVacia_DevuelveVacio() {
        when(rolRepository.findAll()).thenReturn(List.of());

        List<RolResponseDTO> result = rolService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAll_MultipleRoles_DevuelveTodos() {
        RolModel r2 = new RolModel();
        r2.setId(2L);
        r2.setNombre("CLIENTE");
        r2.setDescripcion("Rol cliente");
        r2.setActivo(true);

        when(rolRepository.findAll()).thenReturn(List.of(crearRolModel(), r2));

        List<RolResponseDTO> result = rolService.findAll();

        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getNombre());
        assertEquals("CLIENTE", result.get(1).getNombre());
    }

    // ===================== FIND BY ID =====================

    @Test
    public void testFindById_Existe_DevuelveDTO() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(crearRolModel()));

        RolResponseDTO result = rolService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ADMIN", result.getNombre());
        assertTrue(result.getActivo());
    }

    @Test
    public void testFindById_NoExiste_LanzaRecursoNoEncontrado() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> rolService.findById(99L));
    }

    // ===================== SAVE (CREATE) =====================

    @Test
    public void testSave_OK_CreaRol() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("  CLIENTE  ");
        dto.setDescripcion("Rol de cliente final");
        dto.setActivo(true);

        when(rolRepository.save(any(RolModel.class)))
                .thenAnswer(inv -> {
                    RolModel r = inv.getArgument(0);
                    r.setId(2L);
                    return r;
                });

        RolResponseDTO result = rolService.save(dto);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("CLIENTE", result.getNombre()); // nombre normalizado sin espacios
        assertEquals("Rol de cliente final", result.getDescripcion());
        assertTrue(result.getActivo());
        verify(rolRepository, times(1)).save(any(RolModel.class));
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgumentException() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("   ");
        dto.setActivo(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> rolService.save(dto));

        assertTrue(ex.getMessage().contains("nombre del rol es obligatorio"));
        verify(rolRepository, never()).save(any(RolModel.class));
    }

    @Test
    public void testSave_NombreNull_LanzaIllegalArgumentException() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre(null);
        dto.setActivo(true);

        assertThrows(IllegalArgumentException.class,
                () -> rolService.save(dto));

        verify(rolRepository, never()).save(any(RolModel.class));
    }

    @Test
    public void testSave_ActivoNull_LanzaIllegalArgumentException() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("CLIENTE");
        dto.setActivo(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> rolService.save(dto));

        assertTrue(ex.getMessage().contains("estado 'activo' del rol es obligatorio"));
        verify(rolRepository, never()).save(any(RolModel.class));
    }

    @Test
    public void testSave_SinDescripcion_GuardaOK() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("SOPORTE");
        dto.setDescripcion(null); // sin descripción
        dto.setActivo(true);

        when(rolRepository.save(any(RolModel.class)))
                .thenAnswer(inv -> {
                    RolModel r = inv.getArgument(0);
                    r.setId(3L);
                    return r;
                });

        RolResponseDTO result = rolService.save(dto);

        assertNotNull(result);
        assertEquals("SOPORTE", result.getNombre());
        assertNull(result.getDescripcion());
    }

    // ===================== UPDATE (PUT) =====================

    @Test
    public void testUpdate_CambiaTodosLosCampos() {
        RolModel existente = crearRolModel(); // ADMIN, activo=true

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(RolModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RolResponseDTO result = rolService.update(1L, crearUpdateDTO()); // VENDEDOR, activo=false

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("VENDEDOR", result.getNombre());
        assertEquals("Rol vendedor", result.getDescripcion());
        assertFalse(result.getActivo());
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        RolModel existente = crearRolModel();

        RolUpdateDTO in = new RolUpdateDTO();
        in.setNombre("   ");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> rolService.update(1L, in));

        assertTrue(ex.getMessage().contains("nombre no puede estar vacío"));
        verify(rolRepository, never()).save(any(RolModel.class));
    }

    @Test
    public void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> rolService.update(99L, crearUpdateDTO()));

        verify(rolRepository, never()).save(any(RolModel.class));
    }

    @Test
    public void testUpdate_SoloDescripcion_CambiaDescripcion() {
        RolModel existente = crearRolModel();

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(RolModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RolUpdateDTO in = new RolUpdateDTO();
        in.setDescripcion("Nueva descripción");

        RolResponseDTO result = rolService.update(1L, in);

        assertNotNull(result);
        assertEquals("ADMIN", result.getNombre()); // nombre no cambia
        assertEquals("Nueva descripción", result.getDescripcion());
    }

    // ===================== PATCH =====================

    @Test
    public void testPatch_CambiaSoloActivo() {
        RolModel existente = crearRolModel(); // activo = true

        RolUpdateDTO in = new RolUpdateDTO();
        in.setActivo(false);

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(RolModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RolResponseDTO result = rolService.patch(1L, in);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ADMIN", result.getNombre()); // nombre no cambia
        assertFalse(result.getActivo());
    }

    @Test
    public void testPatch_SinCambios_MantieneEstadoActual() {
        RolModel existente = crearRolModel();

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(RolModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RolResponseDTO result = rolService.patch(1L, new RolUpdateDTO()); // DTO vacío

        assertNotNull(result);
        assertEquals("ADMIN", result.getNombre());
        assertTrue(result.getActivo());
    }

    @Test
    public void testPatch_IdNoExiste_LanzaRecursoNoEncontrado() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> rolService.patch(99L, new RolUpdateDTO()));
    }

    // ===================== DELETE =====================

    @Test
    public void testDeleteById_ConUsuarios_LanzaIllegalStateException() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(crearRolModel()));
        when(rolRepository.existeUsuarioConRol(1L)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> rolService.deleteById(1L));

        assertTrue(ex.getMessage().contains("hay usuarios con este rol"));
        verify(rolRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteById_SinUsuarios_EliminaOK() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(crearRolModel()));
        when(rolRepository.existeUsuarioConRol(1L)).thenReturn(false);

        rolService.deleteById(1L);

        verify(rolRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> rolService.deleteById(99L));

        verify(rolRepository, never()).deleteById(anyLong());
    }
}