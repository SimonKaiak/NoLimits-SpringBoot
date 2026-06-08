package com.example.NoLimits.service.usuario;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.usuario.request.RolRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.RolUpdateDTO;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.service.usuario.RolService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("RolServiceTest")
class RolServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private RolService rolService;

    @MockBean
    private RolRepository rolRepository;

    private RolModel crearRolModel() {
        RolModel rol = new RolModel();
        rol.setId(1L);
        rol.setNombre("ADMIN");
        rol.setDescripcion("Rol administrador");
        rol.setActivo(true);
        return rol;
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

    @Nested
    @DisplayName("describe findAll()")
    class DescribeFindAll {

        @Test
        @DisplayName("test: debe devolver una lista con roles")
        void testFindAll_devuelveLista() {
            when(rolRepository.findAll()).thenReturn(List.of(crearRolModel()));

            List<RolResponseDTO> result = rolService.findAll();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getId());
            assertEquals("ADMIN", result.get(0).getNombre());
            assertEquals("Rol administrador", result.get(0).getDescripcion());
            assertTrue(result.get(0).getActivo());

            verify(rolRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("it: debe devolver lista vacía cuando no existen roles")
        void itFindAll_devuelveListaVacia() {
            when(rolRepository.findAll()).thenReturn(List.of());

            List<RolResponseDTO> result = rolService.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(rolRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("it: debe devolver todos los roles existentes")
        void itFindAll_devuelveMultiplesRoles() {
            RolModel rol2 = new RolModel();
            rol2.setId(2L);
            rol2.setNombre("CLIENTE");
            rol2.setDescripcion("Rol cliente");
            rol2.setActivo(true);

            when(rolRepository.findAll()).thenReturn(List.of(crearRolModel(), rol2));

            List<RolResponseDTO> result = rolService.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("ADMIN", result.get(0).getNombre());
            assertEquals("CLIENTE", result.get(1).getNombre());
            assertEquals("Rol cliente", result.get(1).getDescripcion());
            assertTrue(result.get(1).getActivo());

            verify(rolRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("describe findById()")
    class DescribeFindById {

        @Test
        @DisplayName("test: debe devolver un rol cuando el ID existe")
        void testFindById_existe_devuelveDTO() {
            when(rolRepository.findById(1L)).thenReturn(Optional.of(crearRolModel()));

            RolResponseDTO result = rolService.findById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("ADMIN", result.getNombre());
            assertEquals("Rol administrador", result.getDescripcion());
            assertTrue(result.getActivo());

            verify(rolRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("it: debe lanzar RecursoNoEncontradoException con mensaje correcto")
        void itFindById_noExiste_lanzaExceptionConMensaje() {
            when(rolRepository.findById(99L)).thenReturn(Optional.empty());

            RecursoNoEncontradoException ex = assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> rolService.findById(99L)
            );

            assertTrue(ex.getMessage().contains("Rol no encontrado con ID: 99"));

            verify(rolRepository, times(1)).findById(99L);
        }
    }

    @Nested
    @DisplayName("describe save()")
    class DescribeSave {

        @Test
        @DisplayName("test: debe crear un rol correctamente")
        void testSave_creaRolOK() {
            RolRequestDTO dto = crearRequestDTO();

            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> {
                        RolModel rol = inv.getArgument(0);
                        rol.setId(2L);
                        return rol;
                    });

            RolResponseDTO result = rolService.save(dto);

            assertNotNull(result);
            assertEquals(2L, result.getId());
            assertEquals("CLIENTE", result.getNombre());
            assertEquals("Rol cliente", result.getDescripcion());
            assertTrue(result.getActivo());

            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe normalizar el nombre quitando espacios")
        void itSave_normalizaNombre() {
            RolRequestDTO dto = new RolRequestDTO();
            dto.setNombre("   CLIENTE   ");
            dto.setDescripcion("Rol cliente final");
            dto.setActivo(true);

            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> {
                        RolModel rol = inv.getArgument(0);
                        rol.setId(3L);
                        return rol;
                    });

            RolResponseDTO result = rolService.save(dto);

            assertNotNull(result);
            assertEquals(3L, result.getId());
            assertEquals("CLIENTE", result.getNombre());
            assertEquals("Rol cliente final", result.getDescripcion());
            assertTrue(result.getActivo());

            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe permitir guardar sin descripción")
        void itSave_sinDescripcion_guardaOK() {
            RolRequestDTO dto = new RolRequestDTO();
            dto.setNombre("SOPORTE");
            dto.setDescripcion(null);
            dto.setActivo(true);

            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> {
                        RolModel rol = inv.getArgument(0);
                        rol.setId(4L);
                        return rol;
                    });

            RolResponseDTO result = rolService.save(dto);

            assertNotNull(result);
            assertEquals(4L, result.getId());
            assertEquals("SOPORTE", result.getNombre());
            assertNull(result.getDescripcion());
            assertTrue(result.getActivo());

            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe lanzar IllegalArgumentException si el nombre es null")
        void itSave_nombreNull_lanzaException() {
            RolRequestDTO dto = new RolRequestDTO();
            dto.setNombre(null);
            dto.setActivo(true);

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> rolService.save(dto)
            );

            assertTrue(ex.getMessage().contains("El nombre del rol es obligatorio"));

            verify(rolRepository, never()).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe lanzar IllegalArgumentException si el nombre está vacío")
        void itSave_nombreVacio_lanzaException() {
            RolRequestDTO dto = new RolRequestDTO();
            dto.setNombre("   ");
            dto.setActivo(true);

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> rolService.save(dto)
            );

            assertTrue(ex.getMessage().contains("El nombre del rol es obligatorio"));

            verify(rolRepository, never()).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe lanzar IllegalArgumentException si activo es null")
        void itSave_activoNull_lanzaException() {
            RolRequestDTO dto = new RolRequestDTO();
            dto.setNombre("CLIENTE");
            dto.setActivo(null);

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> rolService.save(dto)
            );

            assertTrue(ex.getMessage().contains("El estado 'activo' del rol es obligatorio"));

            verify(rolRepository, never()).save(any(RolModel.class));
        }
    }

    @Nested
    @DisplayName("describe update()")
    class DescribeUpdate {

        @Test
        @DisplayName("test: debe actualizar todos los campos")
        void testUpdate_actualizaTodosLosCampos() {
            RolModel existente = crearRolModel();

            when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            RolResponseDTO result = rolService.update(1L, crearUpdateDTO());

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("VENDEDOR", result.getNombre());
            assertEquals("Rol vendedor", result.getDescripcion());
            assertFalse(result.getActivo());

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe actualizar solo el nombre y quitar espacios")
        void itUpdate_soloNombre_normalizaEspacios() {
            RolModel existente = crearRolModel();

            RolUpdateDTO dto = new RolUpdateDTO();
            dto.setNombre("   SUPER_ADMIN   ");

            when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            RolResponseDTO result = rolService.update(1L, dto);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("SUPER_ADMIN", result.getNombre());
            assertEquals("Rol administrador", result.getDescripcion());
            assertTrue(result.getActivo());

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe actualizar solo la descripción si solo viene descripción")
        void itUpdate_soloDescripcion() {
            RolModel existente = crearRolModel();

            RolUpdateDTO dto = new RolUpdateDTO();
            dto.setDescripcion("Nueva descripción");

            when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            RolResponseDTO result = rolService.update(1L, dto);

            assertNotNull(result);
            assertEquals("ADMIN", result.getNombre());
            assertEquals("Nueva descripción", result.getDescripcion());
            assertTrue(result.getActivo());

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe actualizar solo activo")
        void itUpdate_soloActivo() {
            RolModel existente = crearRolModel();

            RolUpdateDTO dto = new RolUpdateDTO();
            dto.setActivo(false);

            when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            RolResponseDTO result = rolService.update(1L, dto);

            assertNotNull(result);
            assertEquals("ADMIN", result.getNombre());
            assertEquals("Rol administrador", result.getDescripcion());
            assertFalse(result.getActivo());

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe guardar manteniendo los datos cuando el DTO viene vacío")
        void itUpdate_dtoVacio_mantieneDatos() {
            RolModel existente = crearRolModel();

            when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            RolResponseDTO result = rolService.update(1L, new RolUpdateDTO());

            assertNotNull(result);
            assertEquals("ADMIN", result.getNombre());
            assertEquals("Rol administrador", result.getDescripcion());
            assertTrue(result.getActivo());

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe lanzar IllegalArgumentException si el nombre viene vacío")
        void itUpdate_nombreVacio_lanzaException() {
            RolModel existente = crearRolModel();

            RolUpdateDTO dto = new RolUpdateDTO();
            dto.setNombre("   ");

            when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> rolService.update(1L, dto)
            );

            assertTrue(ex.getMessage().contains("El nombre no puede estar vacío"));

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, never()).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe lanzar RecursoNoEncontradoException si el ID no existe")
        void itUpdate_idNoExiste_lanzaException() {
            when(rolRepository.findById(99L)).thenReturn(Optional.empty());

            RecursoNoEncontradoException ex = assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> rolService.update(99L, crearUpdateDTO())
            );

            assertTrue(ex.getMessage().contains("Rol no encontrado con ID: 99"));

            verify(rolRepository, times(1)).findById(99L);
            verify(rolRepository, never()).save(any(RolModel.class));
        }
    }

    @Nested
    @DisplayName("describe patch()")
    class DescribePatch {

        @Test
        @DisplayName("test: debe cambiar solo activo")
        void testPatch_cambiaSoloActivo() {
            RolModel existente = crearRolModel();

            RolUpdateDTO dto = new RolUpdateDTO();
            dto.setActivo(false);

            when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            RolResponseDTO result = rolService.patch(1L, dto);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("ADMIN", result.getNombre());
            assertEquals("Rol administrador", result.getDescripcion());
            assertFalse(result.getActivo());

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe actualizar nombre, descripción y activo usando patch")
        void itPatch_actualizaTodosLosCampos() {
            RolModel existente = crearRolModel();

            RolUpdateDTO dto = new RolUpdateDTO();
            dto.setNombre("   MODERADOR   ");
            dto.setDescripcion("Rol moderador");
            dto.setActivo(false);

            when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            RolResponseDTO result = rolService.patch(1L, dto);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("MODERADOR", result.getNombre());
            assertEquals("Rol moderador", result.getDescripcion());
            assertFalse(result.getActivo());

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe mantener el estado si el DTO viene vacío")
        void itPatch_dtoVacio_mantieneEstado() {
            RolModel existente = crearRolModel();

            when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(rolRepository.save(any(RolModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            RolResponseDTO result = rolService.patch(1L, new RolUpdateDTO());

            assertNotNull(result);
            assertEquals("ADMIN", result.getNombre());
            assertEquals("Rol administrador", result.getDescripcion());
            assertTrue(result.getActivo());

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).save(any(RolModel.class));
        }

        @Test
        @DisplayName("it: debe lanzar RecursoNoEncontradoException si el ID no existe")
        void itPatch_idNoExiste_lanzaException() {
            when(rolRepository.findById(99L)).thenReturn(Optional.empty());

            RecursoNoEncontradoException ex = assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> rolService.patch(99L, new RolUpdateDTO())
            );

            assertTrue(ex.getMessage().contains("Rol no encontrado con ID: 99"));

            verify(rolRepository, times(1)).findById(99L);
            verify(rolRepository, never()).save(any(RolModel.class));
        }
    }

    @Nested
    @DisplayName("describe deleteById()")
    class DescribeDeleteById {

        @Test
        @DisplayName("test: debe eliminar si el rol existe y no tiene usuarios asociados")
        void testDeleteById_sinUsuarios_eliminaOK() {
            when(rolRepository.findById(1L)).thenReturn(Optional.of(crearRolModel()));
            when(rolRepository.existeUsuarioConRol(1L)).thenReturn(false);

            rolService.deleteById(1L);

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).existeUsuarioConRol(1L);
            verify(rolRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("it: debe lanzar IllegalStateException si existen usuarios con ese rol")
        void itDeleteById_conUsuarios_lanzaException() {
            when(rolRepository.findById(1L)).thenReturn(Optional.of(crearRolModel()));
            when(rolRepository.existeUsuarioConRol(1L)).thenReturn(true);

            IllegalStateException ex = assertThrows(
                    IllegalStateException.class,
                    () -> rolService.deleteById(1L)
            );

            assertTrue(ex.getMessage().contains("No se puede eliminar: hay usuarios con este rol."));

            verify(rolRepository, times(1)).findById(1L);
            verify(rolRepository, times(1)).existeUsuarioConRol(1L);
            verify(rolRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("it: debe lanzar RecursoNoEncontradoException si el rol no existe")
        void itDeleteById_noExiste_lanzaException() {
            when(rolRepository.findById(99L)).thenReturn(Optional.empty());

            RecursoNoEncontradoException ex = assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> rolService.deleteById(99L)
            );

            assertTrue(ex.getMessage().contains("Rol no encontrado con ID: 99"));

            verify(rolRepository, times(1)).findById(99L);
            verify(rolRepository, never()).existeUsuarioConRol(anyLong());
            verify(rolRepository, never()).deleteById(anyLong());
        }
    }
}