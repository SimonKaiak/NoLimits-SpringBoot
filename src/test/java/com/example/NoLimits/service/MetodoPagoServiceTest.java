package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import com.example.NoLimits.Multimedia.repository.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.VentaRepository;
import com.example.NoLimits.Multimedia.service.MetodoPagoService;

@SpringBootTest
@ActiveProfiles("test")
public class MetodoPagoServiceTest {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @MockBean
    private MetodoPagoRepository metodoPagoRepository;

    @MockBean
    private VentaRepository ventaRepository;

    private MetodoPagoModel createMetodoPago() {
        MetodoPagoModel metodoPago = new MetodoPagoModel();
        metodoPago.setId(1L);
        metodoPago.setNombre("Tarjeta de Crédito");
        metodoPago.setActivo(true);
        return metodoPago;
    }

    @Test
    public void testFindAll() {
        when(metodoPagoRepository.findAll()).thenReturn(List.of(createMetodoPago()));
        List<MetodoPagoModel> metodos = metodoPagoService.findAll();
        assertNotNull(metodos);
        assertEquals(1, metodos.size());
    }

    @Test
    public void testFindById() {
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(createMetodoPago()));
        MetodoPagoModel metodoPago = metodoPagoService.findById(1L);
        assertNotNull(metodoPago);
        assertEquals("Tarjeta de Crédito", metodoPago.getNombre());
    }

    @Test
    public void testSave() {
        MetodoPagoModel metodoPago = createMetodoPago();
        when(metodoPagoRepository.existsByNombreIgnoreCase("Tarjeta de Crédito")).thenReturn(false);
        when(metodoPagoRepository.save(metodoPago)).thenReturn(metodoPago);

        MetodoPagoModel savedMetodoPago = metodoPagoService.save(metodoPago);

        assertNotNull(savedMetodoPago);
        assertEquals("Tarjeta de Crédito", savedMetodoPago.getNombre());
    }

    @Test
    public void testUpdate() {
        MetodoPagoModel existingMetodoPago = createMetodoPago();
        MetodoPagoModel newDetails = new MetodoPagoModel();
        newDetails.setNombre("PayPal");
        newDetails.setActivo(false);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existingMetodoPago));
        when(metodoPagoRepository.existsByNombreIgnoreCase("PayPal")).thenReturn(false);
        when(metodoPagoRepository.save(any(MetodoPagoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetodoPagoModel updatedMetodoPago = metodoPagoService.update(1L, newDetails);

        assertNotNull(updatedMetodoPago);
        assertEquals("PayPal", updatedMetodoPago.getNombre());
        assertEquals(false, updatedMetodoPago.getActivo());
    }

    @Test
    public void testPatch() {
        MetodoPagoModel existingMetodoPago = createMetodoPago();
        MetodoPagoModel patchDetails = new MetodoPagoModel();
        patchDetails.setNombre("Transferencia Bancaria");

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existingMetodoPago));
        when(metodoPagoRepository.existsByNombreIgnoreCase("Transferencia Bancaria")).thenReturn(false);
        when(metodoPagoRepository.save(any(MetodoPagoModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetodoPagoModel patchedMetodoPago = metodoPagoService.patch(1L, patchDetails);

        assertNotNull(patchedMetodoPago);
        assertEquals("Transferencia Bancaria", patchedMetodoPago.getNombre());
    }

    @Test
    public void testDeleteById() {
        MetodoPagoModel metodo = createMetodoPago();

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodo));
        when(ventaRepository.findByMetodoPagoModel_Id(1L)).thenReturn(Collections.emptyList());
        doNothing().when(metodoPagoRepository).deleteById(1L);

        metodoPagoService.deleteById(1L);

        verify(metodoPagoRepository, times(1)).deleteById(1L);
    }
}