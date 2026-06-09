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
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class MetodoPagoServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @MockBean
    private MetodoPagoRepository metodoPagoRepository;

    @MockBean
    private VentaRepository ventaRepository;

    // ==========================
    // HELPERS
    // ==========================

    private MetodoPagoModel entity() {
        MetodoPagoModel m = new MetodoPagoModel();
        m.setId(1L);
        m.setNombre("Tarjeta de Crédito");
        m.setActivo(true);
        return m;
    }

    private MetodoPagoRequestDTO req(String nombre, Boolean activo) {
        MetodoPagoRequestDTO dto = new MetodoPagoRequestDTO();
        dto.setNombre(nombre);
        dto.setActivo(activo);
        return dto;
    }

    private MetodoPagoUpdateDTO upd(String nombre, Boolean activo) {
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
        when(metodoPagoRepository.findAll()).thenReturn(List.of(entity()));
        List<MetodoPagoResponseDTO> lista = metodoPagoService.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Tarjeta de Crédito", lista.get(0).getNombre());
        assertTrue(lista.get(0).getActivo());
    }

    @Test
    public void testFindById_Existe() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        MetodoPagoResponseDTO dto = metodoPagoService.findById(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testFindById_NoExiste_Lanza404() {
        when(metodoPagoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> metodoPagoService.findById(99L));
    }

    // ==========================
    // save (POST)
    // ==========================

    @Test
    public void testSave_OK() {
        when(metodoPagoRepository.existsByNombreIgnoreCase("Débito")).thenReturn(false);
        when(metodoPagoRepository.save(any())).thenAnswer(inv -> {
            MetodoPagoModel m = inv.getArgument(0);
            m.setId(2L);
            return m;
        });
        MetodoPagoResponseDTO dto = metodoPagoService.save(req("Débito", true));
        assertNotNull(dto);
        assertEquals("Débito", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    public void testSave_ActivoNull_DefaultTrue() {
        when(metodoPagoRepository.existsByNombreIgnoreCase("Efectivo")).thenReturn(false);
        when(metodoPagoRepository.save(any())).thenAnswer(inv -> {
            MetodoPagoModel m = inv.getArgument(0);
            m.setId(3L);
            return m;
        });
        MetodoPagoResponseDTO dto = metodoPagoService.save(req("Efectivo", null));
        assertTrue(dto.getActivo());
    }

    @Test
    public void testSave_NombreNull_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> metodoPagoService.save(req(null, true)));
        verify(metodoPagoRepository, never()).save(any());
    }

    @Test
    public void testSave_NombreVacio_LanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> metodoPagoService.save(req("   ", true)));
        verify(metodoPagoRepository, never()).save(any());
    }

    @Test
    public void testSave_NombreDuplicado_LanzaIllegalArgument() {
        when(metodoPagoRepository.existsByNombreIgnoreCase("Tarjeta de Crédito")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> metodoPagoService.save(req("Tarjeta de Crédito", true)));
    }

    // ==========================
    // update (PUT)
    // ==========================

    @Test
    public void testUpdate_OK() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoPagoRepository.existsByNombreIgnoreCase("PayPal")).thenReturn(false);
        when(metodoPagoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoPagoResponseDTO dto = metodoPagoService.update(1L, req("PayPal", false));
        assertEquals("PayPal", dto.getNombre());
        assertFalse(dto.getActivo());
    }

    @Test
    public void testUpdate_MismoNombre_NoVerificaDuplicado() {
        // Si el nombre no cambia, no debe verificar duplicado
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoPagoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoPagoResponseDTO dto = metodoPagoService.update(1L, req("Tarjeta de Crédito", false));
        assertEquals("Tarjeta de Crédito", dto.getNombre());
        verify(metodoPagoRepository, never()).existsByNombreIgnoreCase(any());
    }

    @Test
    public void testUpdate_ActivoNull_MantieneSinCambio() {
        MetodoPagoModel existente = entity(); // activo = true
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.existsByNombreIgnoreCase("PayPal")).thenReturn(false);
        when(metodoPagoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoPagoResponseDTO dto = metodoPagoService.update(1L, req("PayPal", null));
        assertEquals("PayPal", dto.getNombre());
        assertTrue(dto.getActivo()); // activo no cambió
    }

    @Test
    public void testUpdate_NombreVacio_LanzaIllegalArgument() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class, () -> metodoPagoService.update(1L, req("   ", true)));
        verify(metodoPagoRepository, never()).save(any());
    }

    @Test
    public void testUpdate_NombreDuplicado_LanzaIllegalArgument() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoPagoRepository.existsByNombreIgnoreCase("Débito")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> metodoPagoService.update(1L, req("Débito", true)));
    }

    // ==========================
    // patch (PATCH)
    // ==========================

    @Test
    public void testPatch_CambiaSoloNombre() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoPagoRepository.existsByNombreIgnoreCase("Transferencia")).thenReturn(false);
        when(metodoPagoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoPagoResponseDTO dto = metodoPagoService.patch(1L, upd("Transferencia", null));
        assertEquals("Transferencia", dto.getNombre());
        assertTrue(dto.getActivo()); // no cambió
    }

    @Test
    public void testPatch_CambiaSoloActivo() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoPagoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MetodoPagoResponseDTO dto = metodoPagoService.patch(1L, upd(null, false));
        assertEquals("Tarjeta de Crédito", dto.getNombre()); // no cambió
        assertFalse(dto.getActivo());
    }

    @Test
    public void testPatch_NombreVacio_LanzaIllegalArgument() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        assertThrows(IllegalArgumentException.class, () -> metodoPagoService.patch(1L, upd("   ", null)));
        verify(metodoPagoRepository, never()).save(any());
    }

    @Test
    public void testPatch_NombreDuplicado_LanzaIllegalArgument() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(metodoPagoRepository.existsByNombreIgnoreCase("Débito")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> metodoPagoService.patch(1L, upd("Débito", null)));
    }

    // ==========================
    // deleteById
    // ==========================

    @Test
    public void testDeleteById_OK() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(ventaRepository.findByMetodoPagoModel_Id(1L)).thenReturn(Collections.emptyList());

        metodoPagoService.deleteById(1L);
        verify(metodoPagoRepository, times(1)).delete(any());
    }

    @Test
    public void testDeleteById_EnUso_LanzaIllegalState() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(entity()));
        when(ventaRepository.findByMetodoPagoModel_Id(1L)).thenReturn(List.of(new VentaModel()));

        assertThrows(IllegalStateException.class, () -> metodoPagoService.deleteById(1L));
        verify(metodoPagoRepository, never()).delete(any());
    }

    @Test
    public void testDeleteById_NoExiste_Lanza404() {
        when(metodoPagoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> metodoPagoService.deleteById(99L));
        verify(metodoPagoRepository, never()).delete(any());
    }

    // ==========================
    // findByNombre
    // ==========================

    @Test
    public void testFindByNombre_Encontrado() {
        when(metodoPagoRepository.findByNombreIgnoreCase("tarjeta de crédito"))
                .thenReturn(Optional.of(entity()));
        var res = metodoPagoService.findByNombre("  tarjeta de crédito  ");
        assertTrue(res.isPresent());
        assertEquals("Tarjeta de Crédito", res.get().getNombre());
    }

    @Test
    public void testFindByNombre_NoEncontrado_RetornaEmpty() {
        when(metodoPagoRepository.findByNombreIgnoreCase("inexistente"))
                .thenReturn(Optional.empty());
        var res = metodoPagoService.findByNombre("inexistente");
        assertTrue(res.isEmpty());
    }

    @Test
    public void testFindByNombre_Null_RetornaEmpty() {
        var res = metodoPagoService.findByNombre(null);
        assertTrue(res.isEmpty());
        verify(metodoPagoRepository, never()).findByNombreIgnoreCase(any());
    }

    @Test
    public void testFindByNombre_Blank_RetornaEmpty() {
        var res = metodoPagoService.findByNombre("   ");
        assertTrue(res.isEmpty());
        verify(metodoPagoRepository, never()).findByNombreIgnoreCase(any());
    }

    // ==========================
    // obtenerMetodoPagoConDatos
    // ==========================

    @Test
    public void testObtenerMetodoPagoConDatos_ConActivo() {
        List<Object[]> resultados = new ArrayList<>();
        resultados.add(new Object[]{1L, "Tarjeta de Crédito", true});
        when(metodoPagoRepository.getMetodoPagoResumen()).thenReturn(resultados);

        List<Map<String, Object>> resumen = metodoPagoService.obtenerMetodoPagoConDatos();
        assertNotNull(resumen);
        assertEquals(1, resumen.size());
        assertEquals(1L, resumen.get(0).get("ID"));
        assertEquals("Tarjeta de Crédito", resumen.get(0).get("Nombre"));
        assertEquals(true, resumen.get(0).get("Activo"));
    }

    @Test
    public void testObtenerMetodoPagoConDatos_SinActivoEnFila() {
        // Fila con solo 2 elementos (length <= 2) → no pone "Activo"
        List<Object[]> resultados = new ArrayList<>();
        resultados.add(new Object[]{1L, "Tarjeta de Crédito"});
        when(metodoPagoRepository.getMetodoPagoResumen()).thenReturn(resultados);

        List<Map<String, Object>> resumen = metodoPagoService.obtenerMetodoPagoConDatos();
        assertNotNull(resumen);
        assertEquals(1, resumen.size());
        assertFalse(resumen.get(0).containsKey("Activo"));
    }

    // ==========================
    // findAllPaged
    // ==========================

    @Test
    public void testFindAllPaged_SinFiltro() {
        Page<MetodoPagoModel> page = new PageImpl<>(List.of(entity()), PageRequest.of(0, 10), 1);
        when(metodoPagoRepository.findAll(any(Pageable.class))).thenReturn(page);

        var resultado = metodoPagoService.findAllPaged(1, 10);
        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());
    }
}