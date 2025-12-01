package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoPagoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoPagoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoPagoUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.service.catalogos.MetodoPagoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@SpringBootTest
@ActiveProfiles("test")
public class MetodoPagoServiceTest {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @MockBean
    private MetodoPagoRepository metodoPagoRepository;

    @MockBean
    private VentaRepository ventaRepository;

    // ==========================
    // HELPERS
    // ==========================

    private MetodoPagoModel createMetodoPagoEntity() {
        MetodoPagoModel m = new MetodoPagoModel();
        m.setId(1L);
        m.setNombre("Tarjeta de Crédito");
        m.setActivo(true);
        return m;
    }

    private MetodoPagoRequestDTO createRequest(String nombre, Boolean activo) {
        MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();
        dto.setNombre(nombre);
        dto.setActivo(activo);
        return dto;
    }

    private MetodoPagoUpdateDTO createUpdate(String nombre, Boolean activo) {
        MetodoPagoUpdateDTO dto = new MetodoPagoUpdateDTO();
        dto.setNombre(nombre);
        dto.setActivo(activo);
        return dto;
    }

    // ==========================
    // findAll / findById
    // ==========================

    @Test
    public void testFindAll() {
        when(metodoPagoRepository.findAll()).thenReturn(List.of(createMetodoPagoEntity()));

        List<MetodoPagoResponseDTO> lista = metodoPagoService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        MetodoPagoResponseDTO dto = lista.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Tarjeta de Crédito", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testFindById_Existe() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(createMetodoPagoEntity()));

        MetodoPagoResponseDTO dto = metodoPagoService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Tarjeta de Crédito", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testFindById_NoExiste_Lanza404() {
        when(metodoPagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> metodoPagoService.findById(99L));
    }

    // ==========================
    // save (POST)
    // ==========================

    @Test
    public void testSave_OK() {
        MetodoPagoRequestDTO request = createRequest("  Débito  ", true);

        when(metodoPagoRepository.existsByNombreIgnoreCase("Débito"))
                .thenReturn(false);
        when(metodoPagoRepository.save(any(MetodoPagoModel.class)))
                .thenAnswer(invocation -> {
                    MetodoPagoModel m = invocation.getArgument(0);
                    m.setId(2L);
                    return m;
                });

        MetodoPagoResponseDTO saved = metodoPagoService.save(request);

        assertNotNull(saved);
        assertEquals(2L, saved.getId());
        assertEquals("Débito", saved.getNombre());
        assertTrue(saved.getActivo());
    }

    @Test
    public void testSave_ActivoNull_DefaultTrue() {
        MetodoPagoRequestDTO request = createRequest("Efectivo", null);

        when(metodoPagoRepository.existsByNombreIgnoreCase("Efectivo"))
                .thenReturn(false);
        when(metodoPagoRepository.save(any(MetodoPagoModel.class)))
                .thenAnswer(invocation -> {
                    MetodoPagoModel m = invocation.getArgument(0);
                    m.setId(3L);
                    return m;
                });

        MetodoPagoResponseDTO saved = metodoPagoService.save(request);

        assertNotNull(saved);
        assertEquals(3L, saved.getId());
        assertEquals("Efectivo", saved.getNombre());
        assertTrue(saved.getActivo(),
                "Si activo viene null en el request, debe quedar true por defecto");
    }

    @Test
    public void testSave_NombreNull_LanzaIllegalArgument() {
        MetodoPagoRequestDTO request = createRequest(null, true);

        assertThrows(IllegalArgumentException.class,
                () -> metodoPagoService.save(request));

        verify(metodoPagoRepository, never()).save(any(MetodoPagoModel.class));
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgument() {
        MetodoPagoRequestDTO request = createRequest("   ", true);

        assertThrows(IllegalArgumentException.class,
                () -> metodoPagoService.save(request));

        verify(metodoPagoRepository, never()).save(any(MetodoPagoModel.class));
    }

    @Test
    public void testSave_NombreDuplicado_LanzaIllegalArgument() {
        MetodoPagoRequestDTO request = createRequest("Tarjeta de Crédito", true);

        when(metodoPagoRepository.existsByNombreIgnoreCase("Tarjeta de Crédito"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> metodoPagoService.save(request));
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    public void testUpdate_OK() {
        MetodoPagoModel existente = createMetodoPagoEntity();
        MetodoPagoRequestDTO request = createRequest("PayPal", false);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.existsByNombreIgnoreCase("PayPal"))
                .thenReturn(false);
        when(metodoPagoRepository.save(any(MetodoPagoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetodoPagoResponseDTO actualizado = metodoPagoService.update(1L, request);

        assertNotNull(actualizado);
        assertEquals(1L, actualizado.getId());
        assertEquals("PayPal", actualizado.getNombre());
        assertFalse(actualizado.getActivo());
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        MetodoPagoModel existente = createMetodoPagoEntity();
        MetodoPagoRequestDTO request = createRequest("   ", true);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> metodoPagoService.update(1L, request));

        verify(metodoPagoRepository, never()).save(any(MetodoPagoModel.class));
    }

    @Test
    public void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        MetodoPagoModel existente = createMetodoPagoEntity();
        MetodoPagoRequestDTO request = createRequest("Débito", true);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.existsByNombreIgnoreCase("Débito"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> metodoPagoService.update(1L, request));
    }

    // ==========================
    // patch (PATCH)
    // ==========================

    @Test
    public void testPatch_CambiaSoloNombre() {
        MetodoPagoModel existente = createMetodoPagoEntity(); // activo=true
        MetodoPagoUpdateDTO request = createUpdate("Transferencia Bancaria", null);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.existsByNombreIgnoreCase("Transferencia Bancaria"))
                .thenReturn(false);
        when(metodoPagoRepository.save(any(MetodoPagoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetodoPagoResponseDTO patched = metodoPagoService.patch(1L, request);

        assertNotNull(patched);
        assertEquals(1L, patched.getId());
        assertEquals("Transferencia Bancaria", patched.getNombre());
        assertTrue(patched.getActivo());
    }

    @Test
    public void testPatch_CambiaSoloActivo() {
        MetodoPagoModel existente = createMetodoPagoEntity();
        MetodoPagoUpdateDTO request = createUpdate(null, false);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.save(any(MetodoPagoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetodoPagoResponseDTO patched = metodoPagoService.patch(1L, request);

        assertNotNull(patched);
        assertEquals("Tarjeta de Crédito", patched.getNombre());
        assertFalse(patched.getActivo());
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        MetodoPagoModel existente = createMetodoPagoEntity();
        MetodoPagoUpdateDTO request = createUpdate("   ", null);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> metodoPagoService.patch(1L, request));

        verify(metodoPagoRepository, never()).save(any(MetodoPagoModel.class));
    }

    @Test
    public void testPatch_NombreDuplicado_LanzaIllegalArgument() {
        MetodoPagoModel existente = createMetodoPagoEntity();
        MetodoPagoUpdateDTO request = createUpdate("Débito", null);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.existsByNombreIgnoreCase("Débito"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> metodoPagoService.patch(1L, request));
    }

    // ==========================
    // deleteById
    // ==========================

    @Test
    public void testDeleteById_OK() {
        MetodoPagoModel existente = createMetodoPagoEntity();

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ventaRepository.findByMetodoPagoModel_Id(1L))
                .thenReturn(Collections.emptyList());

        metodoPagoService.deleteById(1L);

        verify(metodoPagoRepository, times(1)).delete(existente);
    }

    @Test
    public void testDeleteById_EnUso_LanzaIllegalState() {
        MetodoPagoModel existente = createMetodoPagoEntity();
        VentaModel ventaDummy = new VentaModel();

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ventaRepository.findByMetodoPagoModel_Id(1L))
                .thenReturn(List.of(ventaDummy)); // lista no vacía y del tipo correcto

        assertThrows(IllegalStateException.class,
                () -> metodoPagoService.deleteById(1L));

        verify(metodoPagoRepository, never()).delete(any(MetodoPagoModel.class));
    }

    @Test
    public void testDeleteById_NoExiste_Lanza404() {
        when(metodoPagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> metodoPagoService.deleteById(99L));

        verify(metodoPagoRepository, never()).delete(any(MetodoPagoModel.class));
    }

    // ==========================
    // findByNombre
    // ==========================

    @Test
    public void testFindByNombre_Encontrado() {
        MetodoPagoModel entity = createMetodoPagoEntity();

        when(metodoPagoRepository.findByNombreIgnoreCase("tarjeta de crédito"))
                .thenReturn(Optional.of(entity));

        Optional<MetodoPagoResponseDTO> res =
                metodoPagoService.findByNombre("  tarjeta de crédito  ");

        assertTrue(res.isPresent());
        assertEquals("Tarjeta de Crédito", res.get().getNombre());
    }

    @Test
    public void testFindByNombre_Null_RetornaEmpty() {
        Optional<MetodoPagoResponseDTO> res =
                metodoPagoService.findByNombre(null);

        assertTrue(res.isEmpty());
        verify(metodoPagoRepository, never()).findByNombreIgnoreCase(any());
    }

    @Test
    public void testFindByNombre_Blank_RetornaEmpty() {
        Optional<MetodoPagoResponseDTO> res =
                metodoPagoService.findByNombre("   ");

        assertTrue(res.isEmpty());
        verify(metodoPagoRepository, never()).findByNombreIgnoreCase(any());
    }

    // ==========================
    // obtenerMetodoPagoConDatos
    // ==========================

    @Test
    public void testObtenerMetodoPagoConDatos() {
        Object[] fila = new Object[] { 1L, "Tarjeta de Crédito", true };
        List<Object[]> resultados = new ArrayList<>();
        resultados.add(fila);

        when(metodoPagoRepository.getMetodoPagoResumen())
                .thenReturn(resultados);

        List<Map<String, Object>> resumen = metodoPagoService.obtenerMetodoPagoConDatos();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());

        Map<String, Object> row = resumen.get(0);
        assertEquals(1L, row.get("ID"));
        assertEquals("Tarjeta de Crédito", row.get("Nombre"));
        assertEquals(true, row.get("Activo"));
    }
}