package com.example.NoLimits.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.RegionRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.RegionResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.RegionUpdateDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.RegionRepository;
import com.example.NoLimits.Multimedia.service.ubicacion.RegionService;
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

/**
 * RegionServiceTest — Pruebas unitarias del servicio de regiones.
 *
 * Cubre: findAll, findById, save, update, patch, deleteById.
 */

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Tests de RegionService")
class RegionServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private RegionService regionService;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private ComunaRepository comunaRepository;

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private RegionModel crearRegion() {

        RegionModel region = new RegionModel();
        region.setId(1L);
        region.setNombre("Región Metropolitana");

        return region;
    }

    // =====================================================
    // FIND ALL
    // =====================================================

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Retorna lista de regiones")
        void testFindAll_DevuelveLista() {

            // Arrange
            when(regionRepository.findAll())
                    .thenReturn(List.of(crearRegion()));

            // Act
            List<RegionResponseDTO> result =
                    regionService.findAll();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());

            RegionResponseDTO dto = result.get(0);

            assertEquals(1L, dto.getId());
            assertEquals("Región Metropolitana", dto.getNombre());

            verify(regionRepository, times(1))
                    .findAll();
        }

        @Test
        @DisplayName("Retorna lista vacía cuando no existen regiones")
        void testFindAll_ListaVacia_DevuelveVacio() {

            // Arrange
            when(regionRepository.findAll())
                    .thenReturn(List.of());

            // Act
            List<RegionResponseDTO> result =
                    regionService.findAll();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(regionRepository, times(1))
                    .findAll();
        }

        @Test
        @DisplayName("Retorna múltiples regiones")
        void testFindAll_MultipleRegiones_DevuelveTodas() {

            // Arrange
            RegionModel region2 = new RegionModel();
            region2.setId(2L);
            region2.setNombre("Región de Valparaíso");

            when(regionRepository.findAll())
                    .thenReturn(List.of(crearRegion(), region2));

            // Act
            List<RegionResponseDTO> result =
                    regionService.findAll();

            // Assert
            assertEquals(2, result.size());
            assertEquals(
                    "Región Metropolitana",
                    result.get(0).getNombre()
            );
            assertEquals(
                    "Región de Valparaíso",
                    result.get(1).getNombre()
            );

            verify(regionRepository, times(1))
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
        @DisplayName("Retorna región cuando existe")
        void testFindById_Existe_DevuelveDTO() {

            // Arrange
            when(regionRepository.findById(1L))
                    .thenReturn(Optional.of(crearRegion()));

            // Act
            RegionResponseDTO result =
                    regionService.findById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(
                    "Región Metropolitana",
                    result.getNombre()
            );

            verify(regionRepository, times(1))
                    .findById(1L);
        }

        @Test
        @DisplayName("Lanza excepción cuando la región no existe")
        void testFindById_NoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            when(regionRepository.findById(99L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> regionService.findById(99L)
            );

            verify(regionRepository, times(1))
                    .findById(99L);
        }
    }

    // =====================================================
    // SAVE
    // =====================================================

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("Guarda una región correctamente")
        void testSave_OK_CreaRegion() {

            // Arrange
            RegionRequestDTO dto = new RegionRequestDTO();
            dto.setNombre("  Región X  ");

            when(regionRepository.save(any(RegionModel.class)))
                    .thenAnswer(invocation -> {
                        RegionModel region =
                                invocation.getArgument(0);
                        region.setId(5L);
                        return region;
                    });

            // Act
            RegionResponseDTO result =
                    regionService.save(dto);

            // Assert
            assertNotNull(result);
            assertEquals(5L, result.getId());
            assertEquals("Región X", result.getNombre());

            verify(regionRepository, times(1))
                    .save(any(RegionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el nombre está vacío")
        void testSave_NombreVacio_LanzaIllegalArgumentException() {

            // Arrange
            RegionRequestDTO dto = new RegionRequestDTO();
            dto.setNombre("   ");

            // Act
            IllegalArgumentException ex =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> regionService.save(dto)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "nombre de la región es obligatorio"
                    )
            );

            verify(regionRepository, never())
                    .save(any(RegionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el nombre es null")
        void testSave_NombreNull_LanzaIllegalArgumentException() {

            // Arrange
            RegionRequestDTO dto = new RegionRequestDTO();
            dto.setNombre(null);

            // Act + Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> regionService.save(dto)
            );

            verify(regionRepository, never())
                    .save(any(RegionModel.class));
        }

        @Test
        @DisplayName("Normaliza espacios del nombre")
        void testSave_NombreConEspacios_SeNormaliza() {

            // Arrange
            RegionRequestDTO dto = new RegionRequestDTO();
            dto.setNombre("  Región del Maule  ");

            when(regionRepository.save(any(RegionModel.class)))
                    .thenAnswer(invocation -> {
                        RegionModel region =
                                invocation.getArgument(0);
                        region.setId(7L);
                        return region;
                    });

            // Act
            RegionResponseDTO result =
                    regionService.save(dto);

            // Assert
            assertEquals(
                    "Región del Maule",
                    result.getNombre()
            );

            verify(regionRepository, times(1))
                    .save(any(RegionModel.class));
        }
    }

        // =====================================================
    // UPDATE (PUT)
    // =====================================================

    @Nested
    @DisplayName("update()")
    class UpdateTests {

        @Test
        @DisplayName("Actualiza el nombre de una región")
        void testUpdate_CambiaNombre() {

            // Arrange
            RegionModel existente = crearRegion();

            RegionUpdateDTO dto = new RegionUpdateDTO();
            dto.setNombre("  Nueva Región  ");

            when(regionRepository.findById(1L))
                    .thenReturn(Optional.of(existente));

            when(regionRepository.save(any(RegionModel.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            RegionResponseDTO result =
                    regionService.update(1L, dto);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Nueva Región", result.getNombre());

            verify(regionRepository, times(1))
                    .findById(1L);

            verify(regionRepository, times(1))
                    .save(any(RegionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el nombre está vacío")
        void testUpdate_NombreVacio_LanzaIllegalArgument() {

            // Arrange
            RegionModel existente = crearRegion();

            RegionUpdateDTO dto = new RegionUpdateDTO();
            dto.setNombre("   ");

            when(regionRepository.findById(1L))
                    .thenReturn(Optional.of(existente));

            // Act + Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> regionService.update(1L, dto)
            );

            verify(regionRepository, never())
                    .save(any(RegionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando la región no existe")
        void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            RegionUpdateDTO dto = new RegionUpdateDTO();
            dto.setNombre("Región Nueva");

            when(regionRepository.findById(99L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> regionService.update(99L, dto)
            );

            verify(regionRepository, never())
                    .save(any(RegionModel.class));
        }

        @Test
        @DisplayName("Mantiene el nombre actual cuando no se envía uno nuevo")
        void testUpdate_SinNombre_MantieneValorActual() {

            // Arrange
            RegionModel existente = crearRegion();

            RegionUpdateDTO dto = new RegionUpdateDTO();
            dto.setNombre(null);

            when(regionRepository.findById(1L))
                    .thenReturn(Optional.of(existente));

            when(regionRepository.save(any(RegionModel.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            RegionResponseDTO result =
                    regionService.update(1L, dto);

            // Assert
            assertNotNull(result);
            assertEquals(
                    "Región Metropolitana",
                    result.getNombre()
            );

            verify(regionRepository, times(1))
                    .save(any(RegionModel.class));
        }
    }

    // =====================================================
    // PATCH
    // =====================================================

    @Nested
    @DisplayName("patch()")
    class PatchTests {

        @Test
        @DisplayName("Actualiza parcialmente una región")
        void testPatch_CambiaNombre() {

            // Arrange
            RegionModel existente = crearRegion();

            RegionUpdateDTO dto = new RegionUpdateDTO();
            dto.setNombre("  Región Patch  ");

            when(regionRepository.findById(1L))
                    .thenReturn(Optional.of(existente));

            when(regionRepository.save(any(RegionModel.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            RegionResponseDTO result =
                    regionService.patch(1L, dto);

            // Assert
            assertNotNull(result);
            assertEquals(
                    "Región Patch",
                    result.getNombre()
            );

            verify(regionRepository, times(1))
                    .save(any(RegionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando el nombre está vacío")
        void testPatch_NombreVacio_LanzaIllegalArgumentException() {

            // Arrange
            RegionModel existente = crearRegion();

            RegionUpdateDTO dto = new RegionUpdateDTO();
            dto.setNombre("   ");

            when(regionRepository.findById(1L))
                    .thenReturn(Optional.of(existente));

            // Act + Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> regionService.patch(1L, dto)
            );

            verify(regionRepository, never())
                    .save(any(RegionModel.class));
        }

        @Test
        @DisplayName("Lanza excepción cuando la región no existe")
        void testPatch_IdNoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            when(regionRepository.findById(99L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> regionService.patch(
                            99L,
                            new RegionUpdateDTO()
                    )
            );

            verify(regionRepository, times(1))
                    .findById(99L);
        }
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Nested
    @DisplayName("deleteById()")
    class DeleteByIdTests {

        @Test
        @DisplayName("Lanza excepción cuando existen comunas asociadas")
        void testDeleteById_ConComunas_LanzaIllegalStateException() {

            // Arrange
            when(regionRepository.findById(1L))
                    .thenReturn(Optional.of(crearRegion()));

            when(comunaRepository.existsByRegion_Id(1L))
                    .thenReturn(true);

            // Act
            IllegalStateException ex =
                    assertThrows(
                            IllegalStateException.class,
                            () -> regionService.deleteById(1L)
                    );

            // Assert
            assertTrue(
                    ex.getMessage().contains(
                            "tiene comunas asociadas"
                    )
            );

            verify(regionRepository, never())
                    .deleteById(anyLong());
        }

        @Test
        @DisplayName("Elimina una región sin comunas asociadas")
        void testDeleteById_SinComunas_EliminaOK() {

            // Arrange
            when(regionRepository.findById(1L))
                    .thenReturn(Optional.of(crearRegion()));

            when(comunaRepository.existsByRegion_Id(1L))
                    .thenReturn(false);

            // Act
            regionService.deleteById(1L);

            // Assert
            verify(regionRepository, times(1))
                    .deleteById(1L);
        }

        @Test
        @DisplayName("Lanza excepción cuando la región no existe")
        void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            when(regionRepository.findById(99L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(
                    RecursoNoEncontradoException.class,
                    () -> regionService.deleteById(99L)
            );

            verify(regionRepository, never())
                    .deleteById(anyLong());
        }
    }
}

