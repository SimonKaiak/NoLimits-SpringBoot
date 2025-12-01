package com.example.NoLimits.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladoresRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladoresResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.DesarrolladoresUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladoresModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladoresRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.catalogos.DesarrolladoresService;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class DesarrolladoresServiceTest {

    @Autowired
    private DesarrolladoresService desarrolladoresService;

    @MockBean
    private DesarrolladoresRepository desarrolladoresRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private DesarrolladorRepository desarrolladorRepository;

    // ================== HELPERS ==================

    private ProductoModel producto() {
        ProductoModel p = new ProductoModel();
        p.setId(1L);
        p.setNombre("Producto Test");
        return p;
    }

    private ProductoModel producto2() {
        ProductoModel p = new ProductoModel();
        p.setId(2L);
        p.setNombre("Producto Alternativo");
        return p;
    }

    private DesarrolladorModel desarrollador() {
        DesarrolladorModel d = new DesarrolladorModel();
        d.setId(10L);
        d.setNombre("Insomniac Games");
        return d;
    }

    private DesarrolladorModel desarrollador2() {
        DesarrolladorModel d = new DesarrolladorModel();
        d.setId(20L);
        d.setNombre("Naughty Dog");
        return d;
    }

    private DesarrolladoresModel relacion() {
        DesarrolladoresModel rel = new DesarrolladoresModel();
        rel.setId(100L);
        rel.setProducto(producto());
        rel.setDesarrollador(desarrollador());
        return rel;
    }

    private DesarrolladoresRequestDTO requestDTO(Long productoId, Long desarrolladorId) {
        DesarrolladoresRequestDTO dto = new DesarrolladoresRequestDTO();
        dto.setProductoId(productoId);
        dto.setDesarrolladorId(desarrolladorId);
        return dto;
    }

    private DesarrolladoresUpdateDTO updateDTO(Long productoId, Long desarrolladorId) {
        DesarrolladoresUpdateDTO dto = new DesarrolladoresUpdateDTO();
        dto.setProductoId(productoId);
        dto.setDesarrolladorId(desarrolladorId);
        return dto;
    }

    // ================== FIND ==================

    @Test
    void testFindByProducto() {
        when(desarrolladoresRepository.findByProducto_Id(1L))
                .thenReturn(List.of(relacion()));

        List<DesarrolladoresResponseDTO> lista = desarrolladoresService.findByProducto(1L);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        DesarrolladoresResponseDTO dto = lista.get(0);
        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getProductoId());
        assertEquals(10L, dto.getDesarrolladorId());
    }

    @Test
    void testFindByDesarrollador() {
        when(desarrolladoresRepository.findByDesarrollador_Id(10L))
                .thenReturn(List.of(relacion()));

        List<DesarrolladoresResponseDTO> lista = desarrolladoresService.findByDesarrollador(10L);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        DesarrolladoresResponseDTO dto = lista.get(0);
        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getProductoId());
        assertEquals(10L, dto.getDesarrolladorId());
    }

    // ================== LINK ==================

    @Test
    void testLink_CreaNuevaRelacion() {
        DesarrolladoresRequestDTO dto = requestDTO(1L, 10L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.of(desarrollador()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 10L))
                .thenReturn(false);

        when(desarrolladoresRepository.save(any(DesarrolladoresModel.class)))
                .thenAnswer(invocation -> {
                    DesarrolladoresModel rel = invocation.getArgument(0);
                    rel.setId(100L);
                    return rel;
                });

        DesarrolladoresResponseDTO result = desarrolladoresService.link(dto);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(1L, result.getProductoId());
        assertEquals(10L, result.getDesarrolladorId());
        verify(desarrolladoresRepository, times(1)).save(any(DesarrolladoresModel.class));
    }

    @Test
    void testLink_YaExiste_NoDuplica() {
        DesarrolladoresRequestDTO dto = requestDTO(1L, 10L);
        DesarrolladoresModel existente = relacion();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.of(desarrollador()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 10L))
                .thenReturn(true);
        when(desarrolladoresRepository.findByProducto_Id(1L))
                .thenReturn(List.of(existente));

        DesarrolladoresResponseDTO result = desarrolladoresService.link(dto);

        assertNotNull(result);
        assertEquals(existente.getId(), result.getId());
        assertEquals(1L, result.getProductoId());
        assertEquals(10L, result.getDesarrolladorId());
        verify(desarrolladoresRepository, never()).save(any(DesarrolladoresModel.class));
    }

    @Test
    void testLink_ProductoNoExiste_Lanza404() {
        DesarrolladoresRequestDTO dto = requestDTO(1L, 10L);
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladoresService.link(dto));

        verify(desarrolladorRepository, never()).findById(anyLong());
        verify(desarrolladoresRepository, never()).save(any(DesarrolladoresModel.class));
    }

    @Test
    void testLink_DesarrolladorNoExiste_Lanza404() {
        DesarrolladoresRequestDTO dto = requestDTO(1L, 10L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladoresService.link(dto));

        verify(desarrolladoresRepository, never()).save(any(DesarrolladoresModel.class));
    }

    // ================== PATCH ==================

    @Test
    void testPatch_CambiaSoloProducto_OK() {
        DesarrolladoresModel existente = relacion();
        DesarrolladoresUpdateDTO parciales = updateDTO(2L, null);

        when(desarrolladoresRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto2()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(2L, 10L))
                .thenReturn(false);
        when(desarrolladoresRepository.save(any(DesarrolladoresModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        DesarrolladoresResponseDTO result = desarrolladoresService.patch(100L, parciales);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(2L, result.getProductoId());
        assertEquals(10L, result.getDesarrolladorId());
    }

    @Test
    void testPatch_CambiaSoloDesarrollador_OK() {
        DesarrolladoresModel existente = relacion();
        DesarrolladoresUpdateDTO parciales = updateDTO(null, 20L);

        when(desarrolladoresRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.findById(20L)).thenReturn(Optional.of(desarrollador2()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 20L))
                .thenReturn(false);
        when(desarrolladoresRepository.save(any(DesarrolladoresModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        DesarrolladoresResponseDTO result = desarrolladoresService.patch(100L, parciales);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(1L, result.getProductoId());
        assertEquals(20L, result.getDesarrolladorId());
    }

    @Test
    void testPatch_CambiaAmbos_OK() {
        DesarrolladoresModel existente = relacion();
        DesarrolladoresUpdateDTO parciales = updateDTO(2L, 20L);

        when(desarrolladoresRepository.findById(100L)).thenReturn(Optional.of(existente));

        // primero cambia producto
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto2()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(2L, 10L))
                .thenReturn(false);

        // luego cambia desarrollador (producto ya actualizado a 2L)
        when(desarrolladorRepository.findById(20L)).thenReturn(Optional.of(desarrollador2()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(2L, 20L))
                .thenReturn(false);

        when(desarrolladoresRepository.save(any(DesarrolladoresModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        DesarrolladoresResponseDTO result = desarrolladoresService.patch(100L, parciales);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(2L, result.getProductoId());
        assertEquals(20L, result.getDesarrolladorId());
    }

    @Test
    void testPatch_RelacionNoExiste_Lanza404() {
        DesarrolladoresUpdateDTO parciales = updateDTO(null, null);
        when(desarrolladoresRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladoresService.patch(999L, parciales));
    }

    @Test
    void testPatch_ProductoNuevoNoExiste_Lanza404() {
        DesarrolladoresModel existente = relacion();
        DesarrolladoresUpdateDTO parciales = updateDTO(2L, null);

        when(desarrolladoresRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladoresService.patch(100L, parciales));
    }

    @Test
    void testPatch_DesarrolladorNuevoNoExiste_Lanza404() {
        DesarrolladoresModel existente = relacion();
        DesarrolladoresUpdateDTO parciales = updateDTO(null, 20L);

        when(desarrolladoresRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladoresService.patch(100L, parciales));
    }

    @Test
    void testPatch_ProductoNuevoGeneraDuplicado_LanzaIllegalState() {
        DesarrolladoresModel existente = relacion();
        DesarrolladoresUpdateDTO parciales = updateDTO(2L, null);

        when(desarrolladoresRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto2()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(2L, 10L))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> desarrolladoresService.patch(100L, parciales));
    }

    @Test
    void testPatch_DesarrolladorNuevoGeneraDuplicado_LanzaIllegalState() {
        DesarrolladoresModel existente = relacion();
        DesarrolladoresUpdateDTO parciales = updateDTO(null, 20L);

        when(desarrolladoresRepository.findById(100L)).thenReturn(Optional.of(existente));
        when(desarrolladorRepository.findById(20L)).thenReturn(Optional.of(desarrollador2()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 20L))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> desarrolladoresService.patch(100L, parciales));
    }

    // ================== UNLINK ==================

    @Test
    void testUnlink_ExisteRelacion() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.of(desarrollador()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 10L))
                .thenReturn(true);

        desarrolladoresService.unlink(1L, 10L);

        verify(desarrolladoresRepository, times(1))
                .deleteByProducto_IdAndDesarrollador_Id(1L, 10L);
    }

    @Test
    void testUnlink_NoExisteRelacion_NoRevienta() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.of(desarrollador()));
        when(desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(1L, 10L))
                .thenReturn(false);

        assertDoesNotThrow(() -> desarrolladoresService.unlink(1L, 10L));

        verify(desarrolladoresRepository, never())
                .deleteByProducto_IdAndDesarrollador_Id(anyLong(), anyLong());
    }

    @Test
    void testUnlink_ProductoNoExiste_Lanza404() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladoresService.unlink(1L, 10L));

        verify(desarrolladoresRepository, never())
                .deleteByProducto_IdAndDesarrollador_Id(anyLong(), anyLong());
    }

    @Test
    void testUnlink_DesarrolladorNoExiste_Lanza404() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(desarrolladorRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> desarrolladoresService.unlink(1L, 10L));

        verify(desarrolladoresRepository, never())
                .deleteByProducto_IdAndDesarrollador_Id(anyLong(), anyLong());
    }
}