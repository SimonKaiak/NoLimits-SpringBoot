package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.GeneroRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GeneroResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.GeneroUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.repository.catalogos.GeneroRepository;
import com.example.NoLimits.Multimedia.service.catalogos.GeneroService;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class GeneroServiceTest {

    @Autowired
    private GeneroService generoService;

    @MockBean
    private GeneroRepository generoRepository;

    // ================== HELPERS ==================

    private GeneroModel createGenero() {
        GeneroModel g = new GeneroModel();
        g.setId(10L);
        g.setNombre("Acción");
        return g;
    }

    private GeneroRequestDTO createRequestDTO() {
        GeneroRequestDTO dto = new GeneroRequestDTO();
        dto.setNombre("Acción");
        return dto;
    }

    private GeneroUpdateDTO createUpdateDTO() {
        GeneroUpdateDTO dto = new GeneroUpdateDTO();
        dto.setNombre("Aventura");
        return dto;
    }

    // ================== TESTS FIND ==================

    @Test
    public void testFindAll() {
        when(generoRepository.findAll()).thenReturn(List.of(createGenero()));

        List<GeneroResponseDTO> lista = generoService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getId());
        assertEquals("Acción", lista.get(0).getNombre());
    }

    @Test
    public void testFindById_Existe() {
        when(generoRepository.findById(10L)).thenReturn(Optional.of(createGenero()));

        GeneroResponseDTO g = generoService.findById(10L);

        assertNotNull(g);
        assertEquals(10L, g.getId());
        assertEquals("Acción", g.getNombre());
    }

    @Test
    public void testFindById_NoExiste() {
        when(generoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generoService.findById(99L));
    }

    @Test
    public void testFindByNombreContaining() {
        when(generoRepository.findByNombreContainingIgnoreCase("ción"))
                .thenReturn(List.of(createGenero()));

        List<GeneroResponseDTO> lista =
                generoService.findByNombreContaining("ción");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Acción", lista.get(0).getNombre());
    }

    // ================== TESTS SAVE ==================

    @Test
    public void testSave_Valido() {
        GeneroRequestDTO dto = new GeneroRequestDTO();
        dto.setNombre("  Acción  ");

        when(generoRepository.save(any(GeneroModel.class)))
                .thenAnswer(invocation -> {
                    GeneroModel g = invocation.getArgument(0);
                    g.setId(10L);
                    return g;
                });

        GeneroResponseDTO resultado = generoService.save(dto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("Acción", resultado.getNombre()); // normalizado
    }

    @Test
    public void testSave_NombreNull_LanzaIllegalArgument() {
        GeneroRequestDTO dto = new GeneroRequestDTO();
        dto.setNombre(null);

        assertThrows(IllegalArgumentException.class,
                () -> generoService.save(dto));
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgument() {
        GeneroRequestDTO dto = new GeneroRequestDTO();
        dto.setNombre("   ");

        assertThrows(IllegalArgumentException.class,
                () -> generoService.save(dto));
    }

    // ================== TESTS UPDATE ==================

    @Test
    public void testUpdate_CambiaNombreValido() {
        GeneroModel original = createGenero();
        GeneroUpdateDTO cambios = new GeneroUpdateDTO();
        cambios.setNombre("Aventura");

        when(generoRepository.findById(10L)).thenReturn(Optional.of(original));
        when(generoRepository.save(any(GeneroModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GeneroResponseDTO actualizado = generoService.update(10L, cambios);

        assertNotNull(actualizado);
        assertEquals("Aventura", actualizado.getNombre());
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        GeneroModel original = createGenero();
        GeneroUpdateDTO cambios = new GeneroUpdateDTO();
        cambios.setNombre("   ");

        when(generoRepository.findById(10L)).thenReturn(Optional.of(original));

        assertThrows(IllegalArgumentException.class,
                () -> generoService.update(10L, cambios));
    }

    @Test
    public void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {
        GeneroUpdateDTO cambios = createUpdateDTO();

        when(generoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generoService.update(99L, cambios));
    }

    // ================== TESTS PATCH (delegado a update) ==================

    @Test
    public void testPatch_CambiaNombreValido() {
        GeneroModel original = createGenero();
        GeneroUpdateDTO cambios = new GeneroUpdateDTO();
        cambios.setNombre("Aventura");

        when(generoRepository.findById(10L)).thenReturn(Optional.of(original));
        when(generoRepository.save(any(GeneroModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GeneroResponseDTO actualizado = generoService.patch(10L, cambios);

        assertNotNull(actualizado);
        assertEquals("Aventura", actualizado.getNombre());
    }

    // ================== TESTS DELETE ==================

    @Test
    public void testDeleteById_Existe_Elimina() {
        when(generoRepository.findById(10L)).thenReturn(Optional.of(createGenero()));

        generoService.deleteById(10L);

        verify(generoRepository, times(1)).deleteById(10L);
    }

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(generoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> generoService.deleteById(10L));
    }

    // ================== TESTS RESUMEN ==================

    @Test
    public void testObtenerGenerosResumen() {
        Object[] fila = new Object[] { 10L, "Acción", 5L };

        when(generoRepository.obtenerGenerosResumen())
                .thenReturn(List.<Object[]>of(fila));

        List<Object[]> resumen = generoService.obtenerGenerosResumen();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());
        assertEquals(10L, resumen.get(0)[0]);
        assertEquals("Acción", resumen.get(0)[1]);
        assertEquals(5L, resumen.get(0)[2]);
    }
}