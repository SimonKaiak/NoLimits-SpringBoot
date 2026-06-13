package com.example.NoLimits.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.ComunaRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.ComunaUpdateDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.RegionRepository;
import com.example.NoLimits.Multimedia.service.ubicacion.ComunaService;
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
@DisplayName("Tests de ComunaService")
class ComunaServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private ComunaService comunaService;

    @MockBean
    private ComunaRepository comunaRepository;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private DireccionRepository direccionRepository;

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private RegionModel crearRegion(Long id, String nombre) {

        RegionModel region = new RegionModel();
        region.setId(id);
        region.setNombre(nombre);

        return region;
    }

    private ComunaModel crearComuna() {

        RegionModel region =
                crearRegion(1L, "Región Metropolitana");

        ComunaModel comuna = new ComunaModel();
        comuna.setId(10L);
        comuna.setNombre("Santiago");
        comuna.setRegion(region);

        return comuna;
    }

    // =====================================================
    // FIND ALL
    // =====================================================

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Retorna todas las comunas")
        void testFindAll() {

            // Arrange
            ComunaModel comuna = crearComuna();

            when(comunaRepository.findAll())
                    .thenReturn(List.of(comuna));

            // Act
            List<ComunaResponseDTO> result =
                    comunaService.findAll();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());

            ComunaResponseDTO dto = result.get(0);

            assertEquals(10L, dto.getId());
            assertEquals("Santiago", dto.getNombre());
            assertEquals(1L, dto.getRegionId());
            assertEquals(
                    "Región Metropolitana",
                    dto.getRegionNombre()
            );

            verify(comunaRepository, times(1))
                    .findAll();
        }
    }

    // =====================================================
    // FIND BY ID
    // =====================================================

    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("Retorna comuna cuando existe")
        void testFindById_Existe() {

            // Arrange
            ComunaModel comuna = crearComuna();

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(comuna));

            // Act
            ComunaResponseDTO result =
                    comunaService.findById(10L);

            // Assert
            assertNotNull(result);
            assertEquals(10L, result.getId());
            assertEquals("Santiago", result.getNombre());
            assertEquals(1L, result.getRegionId());

            verify(comunaRepository, times(1))
                    .findById(10L);
        }

        @Test
        @DisplayName("Retorna región null cuando la comuna no tiene región")
        void testFindById_ComunaSinRegion_RetornaRegionNull() {

            // Arrange
            ComunaModel comuna = new ComunaModel();
            comuna.setId(20L);
            comuna.setNombre("Comuna sin región");
            comuna.setRegion(null);

            when(comunaRepository.findById(20L))
                    .thenReturn(Optional.of(comuna));

            // Act
            ComunaResponseDTO result =
                    comunaService.findById(20L);

            // Assert
            assertNotNull(result);
            assertEquals(20L, result.getId());
            assertEquals(
                    "Comuna sin región",
                    result.getNombre()
            );
            assertNull(result.getRegionId());
            assertNull(result.getRegionNombre());

            verify(comunaRepository, times(1))
                    .findById(20L);
        }

        @Test
        @DisplayName("Lanza excepción cuando la comuna no existe")
        void testFindById_NoExiste() {

            // Arrange
            when(comunaRepository.findById(999L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> comunaService.findById(999L)
            );

            verify(comunaRepository, times(1))
                    .findById(999L);
        }
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Nested
    @DisplayName("create()")
    class CreateTests {

        @Test
        @DisplayName("Lanza excepción cuando el nombre es null")
        void testCreate_NombreNulo_LanzaIllegalArgumentException() {

            // Arrange
            ComunaRequestDTO request =
                    new ComunaRequestDTO();

            request.setNombre(null);
            request.setRegionId(1L);

            // Act
            IllegalArgumentException ex =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> comunaService.create(request)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "nombre de la comuna es obligatorio"
                    )
            );

            verify(comunaRepository, never())
                    .save(any(ComunaModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el nombre está vacío")
        void testCreate_NombreVacio_LanzaIllegalArgumentException() {

            // Arrange
            ComunaRequestDTO request =
                    new ComunaRequestDTO();

            request.setNombre("   ");
            request.setRegionId(1L);

            // Act
            IllegalArgumentException ex =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> comunaService.create(request)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "nombre de la comuna es obligatorio"
                    )
            );

            verify(comunaRepository, never())
                    .save(any(ComunaModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando no se especifica una región")
        void testCreate_RegionNoEspecificada_LanzaIllegalArgumentException() {

            // Arrange
            ComunaRequestDTO request =
                    new ComunaRequestDTO();

            request.setNombre("Santiago");
            request.setRegionId(null);

            // Act
            IllegalArgumentException ex =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> comunaService.create(request)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "Debe especificar una región válida"
                    )
            );

            verify(comunaRepository, never())
                    .save(any(ComunaModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando la región no existe")
        void testCreate_RegionNoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            ComunaRequestDTO request =
                    new ComunaRequestDTO();

            request.setNombre("Santiago");
            request.setRegionId(1L);

            when(regionRepository.findById(1L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> comunaService.create(request)
            );

            verify(comunaRepository, never())
                    .save(any(ComunaModel.class));
        }

        @Test
        @DisplayName("Crea una comuna correctamente")
        void testCreate_Valida_OK() {

            // Arrange
            ComunaRequestDTO request =
                    new ComunaRequestDTO();

            request.setNombre("  Santiago  ");
            request.setRegionId(1L);

            RegionModel region =
                    crearRegion(
                            1L,
                            "Región Metropolitana"
                    );

            when(regionRepository.findById(1L))
                    .thenReturn(Optional.of(region));

            when(comunaRepository.save(any(ComunaModel.class)))
                    .thenAnswer(invocation -> {
                        ComunaModel comuna =
                                invocation.getArgument(0);
                        comuna.setId(10L);
                        return comuna;
                    });

            // Act
            ComunaResponseDTO result =
                    comunaService.create(request);

            // Assert
            assertNotNull(result);
            assertEquals(10L, result.getId());
            assertEquals("Santiago", result.getNombre());
            assertEquals(1L, result.getRegionId());
            assertEquals(
                    "Región Metropolitana",
                    result.getRegionNombre()
            );

            verify(regionRepository, times(1))
                    .findById(1L);

            verify(comunaRepository, times(1))
                    .save(any(ComunaModel.class));
        }
    }

        // =====================================================
    // UPDATE
    // =====================================================

    @Nested
    @DisplayName("update()")
    class UpdateTests {

        @Test
        @DisplayName("Actualiza nombre y región correctamente")
        void testUpdate_CambiaNombreYRegion() {

            // Arrange
            ComunaModel existente = crearComuna();

            RegionModel nuevaRegion =
                    crearRegion(2L, "Otra Región");

            ComunaUpdateDTO update =
                    new ComunaUpdateDTO();

            update.setNombre("  Ñuñoa  ");
            update.setRegionId(2L);

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(existente));

            when(regionRepository.findById(2L))
                    .thenReturn(Optional.of(nuevaRegion));

            when(comunaRepository.save(any(ComunaModel.class)))
                    .thenAnswer(invocation ->
                            invocation.getArgument(0));

            // Act
            ComunaResponseDTO result =
                    comunaService.update(10L, update);

            // Assert
            assertNotNull(result);
            assertEquals("Ñuñoa", result.getNombre());
            assertEquals(2L, result.getRegionId());
            assertEquals(
                    "Otra Región",
                    result.getRegionNombre()
            );

            verify(comunaRepository, times(1))
                    .save(any(ComunaModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el nombre es null")
        void testUpdate_NombreNulo_LanzaIllegalArgument() {

            // Arrange
            ComunaModel existente = crearComuna();

            ComunaUpdateDTO update =
                    new ComunaUpdateDTO();

            update.setNombre(null);
            update.setRegionId(1L);

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(existente));

            // Act
            IllegalArgumentException ex =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> comunaService.update(10L, update)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "nombre de la comuna es obligatorio"
                    )
            );
        }

        @Test
        @DisplayName("Lanza excepción cuando el nombre está vacío")
        void testUpdate_NombreVacio_LanzaIllegalArgument() {

            // Arrange
            ComunaModel existente = crearComuna();

            ComunaUpdateDTO update =
                    new ComunaUpdateDTO();

            update.setNombre("   ");
            update.setRegionId(1L);

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(existente));

            // Act
            IllegalArgumentException ex =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> comunaService.update(10L, update)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "nombre no puede estar vacío"
                    )
            );
        }

        @Test
        @DisplayName("Lanza excepción cuando la región es null")
        void testUpdate_RegionNull_LanzaIllegalArgument() {

            // Arrange
            ComunaModel existente = crearComuna();

            ComunaUpdateDTO update =
                    new ComunaUpdateDTO();

            update.setNombre("Santiago");
            update.setRegionId(null);

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(existente));

            // Act
            IllegalArgumentException ex =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> comunaService.update(10L, update)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "Debe especificar una región válida"
                    )
            );
        }

        @Test
        @DisplayName("Lanza excepción cuando la región no existe")
        void testUpdate_RegionNoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            ComunaModel existente = crearComuna();

            ComunaUpdateDTO update =
                    new ComunaUpdateDTO();

            update.setNombre("Santiago");
            update.setRegionId(99L);

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(existente));

            when(regionRepository.findById(99L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> comunaService.update(10L, update)
            );
        }
    }

    // =====================================================
    // PATCH
    // =====================================================

    @Nested
    @DisplayName("patch()")
    class PatchTests {

        @Test
        @DisplayName("Actualiza solamente el nombre")
        void testPatch_CambiaSoloNombre() {

            // Arrange
            ComunaModel existente = crearComuna();

            ComunaUpdateDTO patch =
                    new ComunaUpdateDTO();

            patch.setNombre("Providencia");

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(existente));

            when(comunaRepository.save(any(ComunaModel.class)))
                    .thenAnswer(invocation ->
                            invocation.getArgument(0));

            // Act
            ComunaResponseDTO result =
                    comunaService.patch(10L, patch);

            // Assert
            assertNotNull(result);
            assertEquals(
                    "Providencia",
                    result.getNombre()
            );
            assertEquals(1L, result.getRegionId());

            verify(comunaRepository, times(1))
                    .save(any(ComunaModel.class));
        }

        @Test
        @DisplayName("Actualiza solamente la región")
        void testPatch_CambiaSoloRegion() {

            // Arrange
            ComunaModel existente = crearComuna();

            RegionModel nuevaRegion =
                    crearRegion(3L, "Nueva Región");

            ComunaUpdateDTO patch =
                    new ComunaUpdateDTO();

            patch.setRegionId(3L);

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(existente));

            when(regionRepository.findById(3L))
                    .thenReturn(Optional.of(nuevaRegion));

            when(comunaRepository.save(any(ComunaModel.class)))
                    .thenAnswer(invocation ->
                            invocation.getArgument(0));

            // Act
            ComunaResponseDTO result =
                    comunaService.patch(10L, patch);

            // Assert
            assertNotNull(result);
            assertEquals(
                    "Santiago",
                    result.getNombre()
            );
            assertEquals(3L, result.getRegionId());
            assertEquals(
                    "Nueva Región",
                    result.getRegionNombre()
            );
        }

        @Test
        @DisplayName("Lanza excepción cuando el nombre está vacío")
        void testPatch_NombreVacio_LanzaIllegalArgument() {

            // Arrange
            ComunaModel existente = crearComuna();

            ComunaUpdateDTO patch =
                    new ComunaUpdateDTO();

            patch.setNombre("   ");

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(existente));

            // Act
            IllegalArgumentException ex =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> comunaService.patch(10L, patch)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "nombre no puede estar vacío"
                    )
            );
        }

        @Test
        @DisplayName("Lanza excepción cuando la región no existe")
        void testPatch_RegionNoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            ComunaModel existente = crearComuna();

            ComunaUpdateDTO patch =
                    new ComunaUpdateDTO();

            patch.setRegionId(99L);

            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(existente));

            when(regionRepository.findById(99L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> comunaService.patch(10L, patch)
            );
        }

        @Test
        @DisplayName("Lanza excepción cuando la comuna no existe")
        void testPatch_IdNoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            ComunaUpdateDTO patch =
                    new ComunaUpdateDTO();

            patch.setNombre("Otra");

            when(comunaRepository.findById(999L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> comunaService.patch(999L, patch)
            );
        }
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Nested
    @DisplayName("deleteById()")
    class DeleteByIdTests {

        @Test
        @DisplayName("Lanza excepción cuando existen direcciones asociadas")
        void testDeleteById_ConDirecciones_LanzaIllegalStateException() {

            // Arrange
            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(crearComuna()));

            when(direccionRepository.existsByComuna_Id(10L))
                    .thenReturn(true);

            // Act
            IllegalStateException ex =
                    assertThrows(
                            IllegalStateException.class,
                            () -> comunaService.deleteById(10L)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "la comuna tiene direcciones asociadas"
                    )
            );

            verify(comunaRepository, never())
                    .deleteById(anyLong());
        }

        @Test
        @DisplayName("Elimina correctamente una comuna sin direcciones")
        void testDeleteById_SinDirecciones_EliminaOK() {

            // Arrange
            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.of(crearComuna()));

            when(direccionRepository.existsByComuna_Id(10L))
                    .thenReturn(false);

            // Act
            comunaService.deleteById(10L);

            // Assert
            verify(comunaRepository, times(1))
                    .deleteById(10L);
        }

        @Test
        @DisplayName("Lanza excepción cuando la comuna no existe")
        void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            when(comunaRepository.findById(10L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> comunaService.deleteById(10L)
            );

            verify(comunaRepository, never())
                    .deleteById(anyLong());
        }
    }
}
