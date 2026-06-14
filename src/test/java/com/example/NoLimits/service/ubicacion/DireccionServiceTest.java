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

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
@DisplayName("Tests de DireccionService")
class DireccionServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private DireccionService direccionService;

    @MockBean
    private DireccionRepository direccionRepository;

    @MockBean
    private ComunaRepository comunaRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

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

        DireccionModel direccion = new DireccionModel();
        direccion.setId(100L);
        direccion.setCalle("Siempre Viva");
        direccion.setNumero("742");
        direccion.setComuna(comuna);
        direccion.setUsuarioModel(usuario);
        direccion.setCodigoPostal("8320000");
        direccion.setActivo(true);

        return direccion;
    }

    private DireccionRequestDTO crearRequestBasico() {

        DireccionRequestDTO dto = new DireccionRequestDTO();
        dto.setCalle(" Siempre Viva ");
        dto.setNumero(" 742 ");
        dto.setComplemento("Depto 1");
        dto.setCodigoPostal("8320000");
        dto.setComunaId(10L);
        dto.setUsuarioId(5L);
        dto.setActivo(null);

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

    // =====================================================
    // FIND ALL
    // =====================================================

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Retorna todas las direcciones")
        void testFindAll() {

                // Arrange
                when(direccionRepository.findAll())
                        .thenReturn(List.of(crearDireccionEntity()));

                // Act
                List<DireccionResponseDTO> result =
                        direccionService.findAll();

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size());

                DireccionResponseDTO dto = result.get(0);

                assertEquals(100L, dto.getId());
                assertEquals("Siempre Viva", dto.getCalle());
                assertEquals("Santiago", dto.getComuna());
                assertEquals("Región Metropolitana", dto.getRegion());

                verify(direccionRepository, times(1)).findAll();
        }
    }

    // =====================================================
    // FIND BY ID
    // =====================================================

    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("Retorna dirección cuando existe")
        void testFindById_Existe() {

                // Arrange
                DireccionModel entity = crearDireccionEntity();

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(entity));

                // Act
                DireccionResponseDTO result =
                        direccionService.findById(100L);

                // Assert
                assertNotNull(result);
                assertEquals("Siempre Viva", result.getCalle());
                assertEquals("742", result.getNumero());
                assertEquals("Santiago", result.getComuna());
        }

        @Test
        @DisplayName("Lanza excepción cuando la dirección no existe")
        void testFindById_NoExiste() {

                // Arrange
                when(direccionRepository.findById(999L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.findById(999L)
                );
        }

        @Test
        @DisplayName("Retorna DTO sin región cuando la comuna no tiene región")
        void testFindById_ComunaSinRegion() {

                // Arrange
                DireccionModel direccion = new DireccionModel();
                direccion.setId(100L);
                direccion.setCalle("Av. Siempre Viva");
                direccion.setNumero("123");
                direccion.setActivo(true);

                ComunaModel comuna = new ComunaModel();
                comuna.setNombre("Puente Alto");
                comuna.setRegion(null);

                direccion.setComuna(comuna);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(direccion));

                // Act
                DireccionResponseDTO result =
                        direccionService.findById(100L);

                // Assert
                assertNotNull(result);
                assertEquals("Puente Alto", result.getComuna());
                assertNull(result.getRegion());

                verify(direccionRepository).findById(100L);
        }

        @Test
        @DisplayName("Retorna DTO sin comuna")
        void testFindById_SinComuna() {

                // Arrange
                DireccionModel direccion = new DireccionModel();
                direccion.setId(100L);
                direccion.setCalle("Av. Siempre Viva");
                direccion.setNumero("123");
                direccion.setActivo(true);
                direccion.setComuna(null);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(direccion));

                // Act
                DireccionResponseDTO result =
                        direccionService.findById(100L);

                // Assert
                assertNotNull(result);
                assertNull(result.getComuna());
                assertNull(result.getRegion());

                verify(direccionRepository).findById(100L);
        }
    }

    // =====================================================
    // SAVE
    // =====================================================

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("Guarda dirección correctamente")
        void testSave_OK() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();

                ComunaModel comuna = new ComunaModel();
                comuna.setId(10L);
                comuna.setNombre("Santiago");

                UsuarioModel usuario = new UsuarioModel();
                usuario.setId(5L);
                usuario.setNombre("Juan");

                when(comunaRepository.findById(10L))
                        .thenReturn(Optional.of(comuna));

                when(usuarioRepository.findById(5L))
                        .thenReturn(Optional.of(usuario));

                when(direccionRepository.save(any(DireccionModel.class)))
                        .thenAnswer(invocation -> {
                        DireccionModel direccion = invocation.getArgument(0);
                        direccion.setId(100L);
                        return direccion;
                        });

                // Act
                DireccionResponseDTO result =
                        direccionService.save(request);

                // Assert
                assertNotNull(result);
                assertEquals(100L, result.getId());
                assertEquals("Siempre Viva", result.getCalle());
                assertEquals("742", result.getNumero());
                assertEquals("Santiago", result.getComuna());
                assertTrue(result.getActivo());
        }

        @Test
        @DisplayName("Guarda dirección con activo en false")
        void testSave_ActivoFalse() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();
                request.setActivo(false);

                ComunaModel comuna = new ComunaModel();
                comuna.setId(10L);

                UsuarioModel usuario = new UsuarioModel();
                usuario.setId(5L);

                when(comunaRepository.findById(10L))
                        .thenReturn(Optional.of(comuna));

                when(usuarioRepository.findById(5L))
                        .thenReturn(Optional.of(usuario));

                when(direccionRepository.save(any()))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                DireccionResponseDTO result =
                        direccionService.save(request);

                // Assert
                assertFalse(result.getActivo());
        }

        @Test
        @DisplayName("Lanza excepción cuando la calle está vacía")
        void testSave_CalleObligatoria() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();
                request.setCalle("   ");

                // Act + Assert
                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.save(request)
                );

                assertTrue(ex.getMessage().contains("La calle es obligatoria"));

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando la calle es null")
        void testSave_CalleNull() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();
                request.setCalle(null);

                // Act + Assert
                assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.save(request)
                );
        }

        @Test
        @DisplayName("Lanza excepción cuando el número está vacío")
        void testSave_NumeroObligatorio() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();
                request.setNumero("   ");

                // Act + Assert
                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.save(request)
                );

                assertTrue(ex.getMessage().contains("El número es obligatorio"));

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el número es null")
        void testSave_NumeroNull() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();
                request.setNumero(null);

                // Act + Assert
                assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.save(request)
                );
        }

        @Test
        @DisplayName("Lanza excepción cuando no se especifica comuna")
        void testSave_ComunaNoEspecificada() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();
                request.setComunaId(null);

                // Act + Assert
                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.save(request)
                );

                assertTrue(ex.getMessage()
                        .contains("Debe especificar una comuna válida"));

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando la comuna no existe")
        void testSave_ComunaNoEncontrada() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();

                when(comunaRepository.findById(10L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.save(request)
                );

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando no se especifica usuario")
        void testSave_UsuarioNoEspecificado() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();
                request.setUsuarioId(null);

                // Act + Assert
                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.save(request)
                );

                assertTrue(ex.getMessage()
                        .contains("Debe especificar un usuario válido"));

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el usuario no existe")
        void testSave_UsuarioNoEncontrado() {

                // Arrange
                DireccionRequestDTO request = crearRequestBasico();

                ComunaModel comuna = new ComunaModel();
                comuna.setId(10L);

                when(comunaRepository.findById(10L))
                        .thenReturn(Optional.of(comuna));

                when(usuarioRepository.findById(5L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.save(request)
                );

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }
    }
    
    // =====================================================
    // UPDATE (PUT)
    // =====================================================

    @Nested
    @DisplayName("update()")
    class UpdateTests {

        @Test
        @DisplayName("Actualiza dirección correctamente")
        void testUpdate_OK() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO update = crearUpdateBasico();

                ComunaModel nuevaComuna = new ComunaModel();
                nuevaComuna.setId(20L);
                nuevaComuna.setNombre("Puente Alto");

                UsuarioModel nuevoUsuario = new UsuarioModel();
                nuevoUsuario.setId(6L);
                nuevoUsuario.setNombre("Pedro");

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(comunaRepository.findById(20L))
                        .thenReturn(Optional.of(nuevaComuna));

                when(usuarioRepository.findById(6L))
                        .thenReturn(Optional.of(nuevoUsuario));

                when(direccionRepository.save(any(DireccionModel.class)))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                DireccionResponseDTO result =
                        direccionService.update(100L, update);

                // Assert
                assertNotNull(result);
                assertEquals("Nueva Calle", result.getCalle());
                assertEquals("123", result.getNumero());
                assertEquals("Puente Alto", result.getComuna());
                assertFalse(result.getActivo());
        }

        @Test
        @DisplayName("Lanza excepción cuando la dirección no existe")
        void testUpdate_DireccionNoExiste() {

                // Arrange
                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.update(
                                100L,
                                crearUpdateBasico()
                        )
                );
        }

        @Test
        @DisplayName("Lanza excepción cuando la calle está vacía")
        void testUpdate_CalleObligatoria() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO update = crearUpdateBasico();
                update.setCalle("   ");

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                // Act + Assert
                assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.update(100L, update)
                );

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el número está vacío")
        void testUpdate_NumeroObligatorio() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO update = crearUpdateBasico();
                update.setNumero(" ");

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                // Act + Assert
                assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.update(100L, update)
                );

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando la comuna es null")
        void testUpdate_ComunaNull() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO update = crearUpdateBasico();
                update.setComunaId(null);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                // Act + Assert
                assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.update(100L, update)
                );
        }

        @Test
        @DisplayName("Lanza excepción cuando la comuna no existe")
        void testUpdate_ComunaNoEncontrada() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO update = crearUpdateBasico();
                update.setComunaId(99L);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(comunaRepository.findById(99L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.update(100L, update)
                );

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el usuario es null")
        void testUpdate_UsuarioNull() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO update = crearUpdateBasico();
                update.setUsuarioId(null);

                ComunaModel comuna = new ComunaModel();
                comuna.setId(update.getComunaId());

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(comunaRepository.findById(update.getComunaId()))
                        .thenReturn(Optional.of(comuna));

                // Act + Assert
                assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.update(100L, update)
                );
        }

        @Test
        @DisplayName("Lanza excepción cuando el usuario no existe")
        void testUpdate_UsuarioNoEncontrado() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO update = crearUpdateBasico();
                update.setUsuarioId(999L);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(comunaRepository.findById(20L))
                        .thenReturn(Optional.of(new ComunaModel()));

                when(usuarioRepository.findById(999L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.update(100L, update)
                );

                verify(direccionRepository, never())
                        .save(any());
        }

        @Test
        @DisplayName("Usa activo=true cuando activo es null")
        void testUpdate_ActivoNull_UsaTruePorDefecto() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO update = crearUpdateBasico();
                update.setActivo(null);

                ComunaModel comuna = new ComunaModel();
                comuna.setId(20L);

                UsuarioModel usuario = new UsuarioModel();
                usuario.setId(6L);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(comunaRepository.findById(20L))
                        .thenReturn(Optional.of(comuna));

                when(usuarioRepository.findById(6L))
                        .thenReturn(Optional.of(usuario));

                when(direccionRepository.save(any()))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                DireccionResponseDTO result =
                        direccionService.update(100L, update);

                // Assert
                assertTrue(result.getActivo());
        }
    }

    // =====================================================
    // PATCH
    // =====================================================

    @Nested
    @DisplayName("patch()")
    class PatchTests {

        @Test
        @DisplayName("Actualiza calle y número")
        void testPatch_CambiaSoloCalleYNumero() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setCalle("Otra Calle");
                patch.setNumero("999");

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(direccionRepository.save(any(DireccionModel.class)))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                DireccionResponseDTO result =
                        direccionService.patch(100L, patch);

                // Assert
                assertNotNull(result);
                assertEquals("Otra Calle", result.getCalle());
                assertEquals("999", result.getNumero());
                assertEquals("Santiago", result.getComuna());
        }

        @Test
        @DisplayName("Lanza excepción cuando la dirección no existe")
        void testPatch_DireccionNoExiste() {

                // Arrange
                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.patch(
                                100L,
                                new DireccionUpdateDTO()
                        )
                );
        }

        @Test
        @DisplayName("Lanza excepción cuando la calle está vacía")
        void testPatch_CalleVacia() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setCalle("   ");

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                // Act + Assert
                assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.patch(100L, patch)
                );
        }

        @Test
        @DisplayName("Lanza excepción cuando el número está vacío")
        void testPatch_NumeroVacio() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setNumero(" ");

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                // Act + Assert
                assertThrows(
                        IllegalArgumentException.class,
                        () -> direccionService.patch(100L, patch)
                );
        }

        @Test
        @DisplayName("Lanza excepción cuando la comuna no existe")
        void testPatch_ComunaNoEncontrada_LanzaRecursoNoEncontrado() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setComunaId(99L);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(comunaRepository.findById(99L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.patch(100L, patch)
                );

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Actualiza comuna")
        void testPatch_ActualizaComuna() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                ComunaModel comuna = new ComunaModel();
                comuna.setId(20L);
                comuna.setNombre("Puente Alto");

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setComunaId(20L);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(comunaRepository.findById(20L))
                        .thenReturn(Optional.of(comuna));

                when(direccionRepository.save(any()))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                DireccionResponseDTO result =
                        direccionService.patch(100L, patch);

                // Assert
                assertEquals("Puente Alto", result.getComuna());
        }

        @Test
        @DisplayName("Actualiza usuario")
        void testPatch_ActualizaUsuario() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                UsuarioModel usuario = new UsuarioModel();
                usuario.setId(99L);

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setUsuarioId(99L);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(usuarioRepository.findById(99L))
                        .thenReturn(Optional.of(usuario));

                when(direccionRepository.save(any()))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                direccionService.patch(100L, patch);

                // Assert
                verify(usuarioRepository).findById(99L);
        }

        @Test
        @DisplayName("Lanza excepción cuando el usuario no existe")
        void testPatch_UsuarioNoEncontrado() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setUsuarioId(999L);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(usuarioRepository.findById(999L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.patch(100L, patch)
                );

                verify(direccionRepository, never())
                        .save(any(DireccionModel.class));
        }

        @Test
        @DisplayName("Actualiza complemento")
        void testPatch_ActualizaComplemento() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setComplemento("Depto 101");

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(direccionRepository.save(any()))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                direccionService.patch(100L, patch);

                // Assert
                assertEquals("Depto 101", existente.getComplemento());
        }

        @Test
        @DisplayName("Actualiza código postal")
        void testPatch_ActualizaCodigoPostal() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setCodigoPostal("1234567");

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(direccionRepository.save(any()))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                direccionService.patch(100L, patch);

                // Assert
                assertEquals("1234567", existente.getCodigoPostal());
        }

        @Test
        @DisplayName("Actualiza estado activo")
        void testPatch_ActualizaActivo() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setActivo(false);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(direccionRepository.save(any()))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                direccionService.patch(100L, patch);

                // Assert
                assertFalse(existente.getActivo());
        }

        @Test
        @DisplayName("Actualiza complemento, código postal y activo")
        void testPatch_ComplementoCodigoPostalYActivo() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                DireccionUpdateDTO patch = new DireccionUpdateDTO();
                patch.setComplemento("Depto 404");
                patch.setCodigoPostal("7500000");
                patch.setActivo(false);

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                when(direccionRepository.save(any(DireccionModel.class)))
                        .thenAnswer(i -> i.getArgument(0));

                // Act
                DireccionResponseDTO response =
                        direccionService.patch(100L, patch);

                // Assert
                assertEquals("Depto 404", response.getComplemento());
                assertEquals("7500000", response.getCodigoPostal());
                assertFalse(response.getActivo());
        }
    }

    // =====================================================
    // DELETE BY ID
    // =====================================================

    @Nested
    @DisplayName("deleteById()")
    class DeleteByIdTests {

        @Test
        @DisplayName("Elimina dirección cuando existe")
        void testDeleteById_OK() {

                // Arrange
                DireccionModel existente = crearDireccionEntity();

                when(direccionRepository.findById(100L))
                        .thenReturn(Optional.of(existente));

                // Act
                direccionService.deleteById(100L);

                // Assert
                verify(direccionRepository, times(1))
                        .delete(existente);
        }

        @Test
        @DisplayName("Lanza excepción cuando la dirección no existe")
        void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {

                // Arrange
                when(direccionRepository.findById(999L))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                        RecursoNoEncontradoException.class,
                        () -> direccionService.deleteById(999L)
                );

                verify(direccionRepository, never())
                        .delete(any(DireccionModel.class));
        }
    }

}