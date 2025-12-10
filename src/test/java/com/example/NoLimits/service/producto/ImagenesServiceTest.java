package com.example.NoLimits.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.producto.request.ImagenesRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ImagenesResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ImagenesUpdateDTO;
import com.example.NoLimits.Multimedia.model.producto.ImagenesModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.producto.ImagenesRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.service.producto.ImagenesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ImagenesServiceTest {

    @Autowired
    private ImagenesService imagenesService;

    @MockBean
    private ImagenesRepository imagenesRepository;

    @MockBean
    private ProductoRepository productoRepository;

    // ================== HELPERS ==================

    private ProductoModel createProducto(Long id) {
        ProductoModel p = new ProductoModel();
        p.setId(id);
        p.setNombre("Producto X");
        p.setPrecio(9990.0);
        return p;
    }

    private ImagenesModel createImagen(Long id, Long productoId) {
        ImagenesModel img = new ImagenesModel();
        img.setId(id);
        img.setRuta("/assets/img/productos/" + productoId + ".webp");
        img.setAltText("Imagen " + productoId);
        img.setProducto(createProducto(productoId));
        return img;
    }

    // ================== FIND ==================

    @Test
    public void testFindAll() {
        when(imagenesRepository.findAll())
                .thenReturn(List.of(createImagen(1L, 10L)));

        List<ImagenesResponseDTO> lista = imagenesService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());

        ImagenesResponseDTO dto = lista.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("/assets/img/productos/10.webp", dto.getRuta());
        assertEquals("Imagen 10", dto.getAltText());
        assertEquals(10L, dto.getProductoId());
    }

    @Test
    public void testFindById_Existe() {
        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(createImagen(1L, 10L)));

        ImagenesResponseDTO dto = imagenesService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("/assets/img/productos/10.webp", dto.getRuta());
        assertEquals("Imagen 10", dto.getAltText());
        assertEquals(10L, dto.getProductoId());
    }

    @Test
    public void testFindById_NoExiste() {
        when(imagenesRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> imagenesService.findById(99L));
    }

    @Test
    public void testFindByProducto() {
        when(imagenesRepository.findByProducto_Id(10L))
                .thenReturn(List.of(createImagen(1L, 10L)));

        List<ImagenesResponseDTO> lista = imagenesService.findByProducto(10L);

        assertNotNull(lista);
        assertEquals(1, lista.size());

        ImagenesResponseDTO dto = lista.get(0);
        assertEquals(10L, dto.getProductoId());
    }

    @Test
    public void testFindByRutaContainingIgnoreCase() {
        when(imagenesRepository.findByRutaContainingIgnoreCase("productos"))
                .thenReturn(List.of(createImagen(1L, 10L)));

        List<ImagenesResponseDTO> lista = imagenesService.findByRutaContainingIgnoreCase("productos");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("/assets/img/productos/10.webp", lista.get(0).getRuta());
    }

    // ================== SAVE ==================

    @Test
    public void testSave_Ok() {
        ImagenesRequestDTO request = new ImagenesRequestDTO();
        request.setProductoId(10L);
        request.setRuta("  /assets/img/Peliculas/spiderman.webp  ");
        request.setAltText("  Spider-Man posando  ");

        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(createProducto(10L)));
        when(imagenesRepository.save(any(ImagenesModel.class)))
                .thenAnswer(inv -> {
                    ImagenesModel img = inv.getArgument(0);
                    img.setId(1L);
                    return img;
                });

        ImagenesResponseDTO guardada = imagenesService.save(request);

        assertNotNull(guardada);
        assertEquals(1L, guardada.getId());
        assertEquals("/assets/img/Peliculas/spiderman.webp", guardada.getRuta());
        assertEquals("Spider-Man posando", guardada.getAltText());
        assertEquals(10L, guardada.getProductoId());
    }

    @Test
    public void testSave_SinProducto_LanzaIllegalArgument() {
        ImagenesRequestDTO request = new ImagenesRequestDTO();
        request.setRuta("/assets/img/test.webp");
        // sin productoId

        assertThrows(IllegalArgumentException.class,
                () -> imagenesService.save(request));

        verify(productoRepository, never()).findById(anyLong());
        verify(imagenesRepository, never()).save(any(ImagenesModel.class));
    }

    @Test
    public void testSave_ProductoNoExiste_LanzaRecursoNoEncontrado() {
        ImagenesRequestDTO request = new ImagenesRequestDTO();
        request.setProductoId(999L);
        request.setRuta("/assets/img/test.webp");

        when(productoRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> imagenesService.save(request));

        verify(imagenesRepository, never()).save(any(ImagenesModel.class));
    }

    @Test
    public void testSave_RutaVacia_LanzaIllegalArgument() {
        ImagenesRequestDTO request = new ImagenesRequestDTO();
        request.setProductoId(1L);
        request.setRuta("   ");

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(createProducto(1L)));

        assertThrows(IllegalArgumentException.class,
                () -> imagenesService.save(request));

        verify(imagenesRepository, never()).save(any(ImagenesModel.class));
    }

    // ================== UPDATE / PATCH ==================

    @Test
    public void testUpdate_CambiaRutaYAltText() {
        ImagenesModel existente = createImagen(1L, 10L);

        ImagenesUpdateDTO cambios = new ImagenesUpdateDTO();
        cambios.setRuta("   /assets/img/Peliculas/spiderman-remaster.webp  ");
        cambios.setAltText("   Nuevo alt text   ");

        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(imagenesRepository.save(any(ImagenesModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ImagenesResponseDTO actualizado = imagenesService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("/assets/img/Peliculas/spiderman-remaster.webp", actualizado.getRuta());
        assertEquals("Nuevo alt text", actualizado.getAltText());
        assertEquals(10L, actualizado.getProductoId());
    }

    @Test
    public void testUpdate_CambiaProducto() {
        ImagenesModel existente = createImagen(1L, 10L);

        ImagenesUpdateDTO cambios = new ImagenesUpdateDTO();
        cambios.setProductoId(20L);

        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(productoRepository.findById(20L))
                .thenReturn(Optional.of(createProducto(20L)));
        when(imagenesRepository.save(any(ImagenesModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ImagenesResponseDTO actualizado = imagenesService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals(20L, actualizado.getProductoId());
    }

    @Test
    public void testUpdate_RutaVacia_LanzaIllegalArgument() {
        ImagenesModel existente = createImagen(1L, 10L);

        ImagenesUpdateDTO cambios = new ImagenesUpdateDTO();
        cambios.setRuta("   ");

        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> imagenesService.update(1L, cambios));
    }

    @Test
    public void testPatch_SeComportaComoUpdate() {
        ImagenesModel existente = createImagen(1L, 10L);

        ImagenesUpdateDTO cambios = new ImagenesUpdateDTO();
        cambios.setAltText("   Texto parche   ");

        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(imagenesRepository.save(any(ImagenesModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ImagenesResponseDTO patched = imagenesService.patch(1L, cambios);

        assertNotNull(patched);
        assertEquals("Texto parche", patched.getAltText());
        assertEquals(10L, patched.getProductoId());
    }

    // ================== DELETE ==================

    @Test
    public void testDeleteById() {
        ImagenesModel existente = createImagen(1L, 10L);

        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        doNothing().when(imagenesRepository).delete(existente);

        imagenesService.deleteById(1L);

        verify(imagenesRepository, times(1)).delete(existente);
    }

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(imagenesRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> imagenesService.deleteById(99L));

        verify(imagenesRepository, never()).delete(any(ImagenesModel.class));
    }

    @Test
    public void testDeleteByProducto_Ok() {
        when(productoRepository.existsById(10L)).thenReturn(true);
        when(imagenesRepository.deleteByProducto_Id(10L)).thenReturn(3L);

        long borradas = imagenesService.deleteByProducto(10L);

        assertEquals(3L, borradas);
        verify(imagenesRepository, times(1)).deleteByProducto_Id(10L);
    }

    @Test
    public void testDeleteByProducto_ProductoNoExiste_Lanza404() {
        when(productoRepository.existsById(10L)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> imagenesService.deleteByProducto(10L));

        verify(imagenesRepository, never()).deleteByProducto_Id(anyLong());
    }

    // ================== RESUMEN ==================

    @Test
        public void testObtenerImagenesResumen() {
        // Simula una fila: id, ruta, altText, productoId
        Object[] fila = new Object[] {
                1L,
                "/assets/img/productos/10.webp",
                "Imagen 10",
                10L
        };

        // Construimos la lista "a mano" para que sea List<Object[]>
        List<Object[]> filasMock = new ArrayList<>();
        filasMock.add(fila);

        when(imagenesRepository.obtenerImagenesResumen())
                .thenReturn(filasMock);

        List<Map<String, Object>> resumen = imagenesService.obtenerImagenesResumen();

        assertNotNull(resumen);
        assertEquals(1, resumen.size());

        Map<String, Object> row = resumen.get(0);
        assertEquals(1L, row.get("ID"));
        assertEquals("/assets/img/productos/10.webp", row.get("Ruta"));
        assertEquals("Imagen 10", row.get("AltText"));
        assertEquals(10L, row.get("ProductoId"));
        }
}