package com.example.NoLimits.service.usuario;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.usuario.request.RolRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.RolUpdateDTO;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.service.usuario.RolService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class RolServiceTest {

    @Autowired
    private RolService rolService;

    @MockBean
    private RolRepository rolRepository;

    // ================== HELPERS ==================

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

    // ================== FIND ==================

    @Test
    public void testFindAll() {
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
    public void testFindById_Existe() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(crearRolModel()));

        RolResponseDTO result = rolService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ADMIN", result.getNombre());
        assertTrue(result.getActivo());
    }

    @Test
    public void testFindById_NoExiste() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> rolService.findById(99L));
    }

    // ================== SAVE (CREATE) ==================

    @Test
    public void testSave_OK() {
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
        assertEquals("CLIENTE", result.getNombre()); // normalizado
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
    public void testSave_ActivoNull_LanzaIllegalArgumentException() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("CLIENTE");
        dto.setActivo(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> rolService.save(dto));

        assertTrue(ex.getMessage().contains("estado 'activo' del rol es obligatorio"));
        verify(rolRepository, never()).save(any(RolModel.class));
    }

    // ================== UPDATE (PUT) ==================

    @Test
    public void testUpdate_CambiaTodosLosCampos() {
        RolModel existente = crearRolModel(); // ADMIN, true

        RolUpdateDTO in = crearUpdateDTO();   // VENDEDOR, "Rol vendedor", false

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(RolModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RolResponseDTO result = rolService.update(1L, in);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("VENDEDOR", result.getNombre());
        assertEquals("Rol vendedor", result.getDescripcion());
        assertEquals(false, result.getActivo());
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
        RolUpdateDTO in = crearUpdateDTO();

        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> rolService.update(99L, in));
        verify(rolRepository, never()).save(any(RolModel.class));
    }

    // ================== PATCH (delegado a update) ==================

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
        assertEquals("ADMIN", result.getNombre()); // nombre se mantiene
        assertEquals(false, result.getActivo());
    }

    // ================== DELETE ==================

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
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> rolService.deleteById(1L));

        verify(rolRepository, never()).deleteById(anyLong());
    }
}