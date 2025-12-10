package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoEnvioRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoEnvioUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoEnvioRepository;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoEnvioService;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@SpringBootTest
@ActiveProfiles("test")
public class MetodoEnvioServiceTest {

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @MockBean
    private MetodoEnvioRepository metodoEnvioRepository;

    // ==========================
    // HELPERS
    // ==========================

    private MetodoEnvioModel createMetodoEnvioEntity() {
        MetodoEnvioModel m = new MetodoEnvioModel();
        m.setId(1L);
        m.setNombre("Despacho a domicilio");
        m.setDescripcion("Entrega en la casa del cliente");
        m.setActivo(true);
        return m;
    }

    private MetodoEnvioRequestDTO createRequest(String nombre, String descripcion, Boolean activo) {
        MetodoEnvioRequestDTO dto = new MetodoEnvioRequestDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setActivo(activo);
        return dto;
    }

    private MetodoEnvioUpdateDTO createUpdate(String nombre, String descripcion, Boolean activo) {
        MetodoEnvioUpdateDTO dto = new MetodoEnvioUpdateDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setActivo(activo);
        return dto;
    }

    // ==========================
    // findAll / findById
    // ==========================

    @Test
    public void testFindAll() {
        when(metodoEnvioRepository.findAll()).thenReturn(List.of(createMetodoEnvioEntity()));

        List<MetodoEnvioResponseDTO> lista = metodoEnvioService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        MetodoEnvioResponseDTO dto = lista.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Despacho a domicilio", dto.getNombre());
        assertEquals("Entrega en la casa del cliente", dto.getDescripcion());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testFindById_Existe() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(createMetodoEnvioEntity()));

        MetodoEnvioResponseDTO dto = metodoEnvioService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Despacho a domicilio", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testFindById_NoExiste() {
        when(metodoEnvioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> metodoEnvioService.findById(99L));
    }

    // ==========================
    // create (POST)
    // ==========================

    @Test
    public void testCreate_OK() {
        MetodoEnvioRequestDTO in = createRequest("  Retiro en tienda  ",
                "Retiro en la sucursal principal", true);

        when(metodoEnvioRepository.save(any(MetodoEnvioModel.class)))
                .thenAnswer(invocation -> {
                    MetodoEnvioModel m = invocation.getArgument(0);
                    m.setId(2L);
                    return m;
                });

        MetodoEnvioResponseDTO saved = metodoEnvioService.create(in);

        assertNotNull(saved);
        assertEquals(2L, saved.getId());
        // nombre debe venir trimmeado
        assertEquals("Retiro en tienda", saved.getNombre());
        assertEquals("Retiro en la sucursal principal", saved.getDescripcion());
        assertTrue(saved.getActivo());
    }

    @Test
    public void testCreate_ActivoNull_DefaultTrue() {
        MetodoEnvioRequestDTO in = createRequest("Envío Express",
                "Entrega rápida", null);

        when(metodoEnvioRepository.save(any(MetodoEnvioModel.class)))
                .thenAnswer(invocation -> {
                    MetodoEnvioModel m = invocation.getArgument(0);
                    m.setId(3L);
                    return m;
                });

        MetodoEnvioResponseDTO saved = metodoEnvioService.create(in);

        assertNotNull(saved);
        assertEquals(3L, saved.getId());
        assertEquals("Envío Express", saved.getNombre());
        assertTrue(saved.getActivo(),
                "Si activo viene null en el RequestDTO, debe quedar true por defecto");
    }

    @Test
    public void testCreate_NombreVacio_LanzaIllegalArgument() {
        MetodoEnvioRequestDTO in = createRequest("   ", "desc", true);

        assertThrows(IllegalArgumentException.class,
                () -> metodoEnvioService.create(in));

        verify(metodoEnvioRepository, never()).save(any(MetodoEnvioModel.class));
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    public void testUpdate_OK() {
        MetodoEnvioModel existente = createMetodoEnvioEntity();
        MetodoEnvioRequestDTO in = createRequest("Retiro en sucursal",
                "Retiro presencial", false);

        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoEnvioRepository.save(any(MetodoEnvioModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetodoEnvioResponseDTO actualizado = metodoEnvioService.update(1L, in);

        assertNotNull(actualizado);
        assertEquals(1L, actualizado.getId());
        assertEquals("Retiro en sucursal", actualizado.getNombre());
        assertEquals("Retiro presencial", actualizado.getDescripcion());
        assertFalse(actualizado.getActivo());
    }

    @Test
    public void testUpdate_ActivoNull_MantieneValorAnterior() {
        MetodoEnvioModel existente = createMetodoEnvioEntity(); // activo=true
        MetodoEnvioRequestDTO in = createRequest("Despacho a domicilio",
                "Entrega en la casa del cliente", null);

        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoEnvioRepository.save(any(MetodoEnvioModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetodoEnvioResponseDTO actualizado = metodoEnvioService.update(1L, in);

        assertNotNull(actualizado);
        assertTrue(actualizado.getActivo(),
                "Si activo viene null en PUT, debe mantenerse el valor anterior");
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        MetodoEnvioModel existente = createMetodoEnvioEntity();
        MetodoEnvioRequestDTO in = createRequest("   ", "desc", true);

        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> metodoEnvioService.update(1L, in));

        verify(metodoEnvioRepository, never()).save(any(MetodoEnvioModel.class));
    }

    // ==========================
    // patch (PATCH)
    // ==========================

    @Test
    public void testPatch_CambiaSoloNombre() {
        MetodoEnvioModel existente = createMetodoEnvioEntity(); // activo=true
        MetodoEnvioUpdateDTO in = createUpdate("Retiro en tienda", null, null);

        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoEnvioRepository.save(any(MetodoEnvioModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetodoEnvioResponseDTO patched = metodoEnvioService.patch(1L, in);

        assertNotNull(patched);
        assertEquals("Retiro en tienda", patched.getNombre());
        assertEquals("Entrega en la casa del cliente", patched.getDescripcion());
        assertTrue(patched.getActivo());
    }

    @Test
    public void testPatch_CambiaDescripcionYActivo() {
        MetodoEnvioModel existente = createMetodoEnvioEntity(); // activo=true
        MetodoEnvioUpdateDTO in = createUpdate(null,
                "Nueva descripción", false);

        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoEnvioRepository.save(any(MetodoEnvioModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetodoEnvioResponseDTO patched = metodoEnvioService.patch(1L, in);

        assertNotNull(patched);
        assertEquals("Despacho a domicilio", patched.getNombre());
        assertEquals("Nueva descripción", patched.getDescripcion());
        assertFalse(patched.getActivo());
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        MetodoEnvioModel existente = createMetodoEnvioEntity();
        MetodoEnvioUpdateDTO in = createUpdate("   ", null, null);

        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> metodoEnvioService.patch(1L, in));

        verify(metodoEnvioRepository, never()).save(any(MetodoEnvioModel.class));
    }

    // ==========================
    // deleteById
    // ==========================

    @Test
    public void testDeleteById_Existe() {
        when(metodoEnvioRepository.findById(1L)).thenReturn(Optional.of(createMetodoEnvioEntity()));

        metodoEnvioService.deleteById(1L);

        verify(metodoEnvioRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_Lanza404() {
        when(metodoEnvioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> metodoEnvioService.deleteById(99L));

        verify(metodoEnvioRepository, never()).deleteById(anyLong());
    }
}