package com.example.NoLimits.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ImagenesModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.ImagenesRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.service.ImagenesService;

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
        // No es necesario setear tipo/estado/clasificación para estos tests
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

    // ================== TESTS ==================

    @Test
    public void testFindAll() {
        when(imagenesRepository.findAll())
                .thenReturn(List.of(createImagen(1L, 10L)));

        List<ImagenesModel> lista = imagenesService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(1L, lista.get(0).getId());
    }

    @Test
    public void testFindById_Existe() {
        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(createImagen(1L, 10L)));

        ImagenesModel img = imagenesService.findById(1L);

        assertNotNull(img);
        assertEquals(1L, img.getId());
        assertEquals("/assets/img/productos/10.webp", img.getRuta());
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

        List<ImagenesModel> lista = imagenesService.findByProducto(10L);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getProducto().getId());
    }

    @Test
    public void testSave_Ok() {
        ImagenesModel input = new ImagenesModel();
        input.setRuta("  /assets/img/Peliculas/spiderman.webp  ");
        input.setAltText("  Spider-Man posando  ");
        ProductoModel refProd = new ProductoModel();
        refProd.setId(10L);
        input.setProducto(refProd);

        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(createProducto(10L)));
        when(imagenesRepository.save(any(ImagenesModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ImagenesModel guardada = imagenesService.save(input);

        assertNotNull(guardada);
        assertEquals("/assets/img/Peliculas/spiderman.webp", guardada.getRuta());
        assertEquals("Spider-Man posando", guardada.getAltText());
        assertNotNull(guardada.getProducto());
        assertEquals(10L, guardada.getProducto().getId());
    }

    @Test
    public void testSave_SinProducto_LanzaIllegalArgument() {
        ImagenesModel input = new ImagenesModel();
        input.setRuta("/assets/img/test.webp");
        // sin producto

        assertThrows(IllegalArgumentException.class,
                () -> imagenesService.save(input));
    }

    @Test
    public void testSave_ProductoNoExiste_LanzaRecursoNoEncontrado() {
        ImagenesModel input = new ImagenesModel();
        input.setRuta("/assets/img/test.webp");
        ProductoModel p = new ProductoModel();
        p.setId(999L);
        input.setProducto(p);

        when(productoRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> imagenesService.save(input));
    }

    @Test
    public void testSave_RutaVacia_LanzaIllegalArgument() {
        ImagenesModel input = new ImagenesModel();
        input.setRuta("   ");
        ProductoModel p = new ProductoModel();
        p.setId(1L);
        input.setProducto(p);

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(createProducto(1L)));

        assertThrows(IllegalArgumentException.class,
                () -> imagenesService.save(input));
    }

    @Test
    public void testUpdate_CambiaRutaYAltText() {
        ImagenesModel existente = createImagen(1L, 10L);
        ImagenesModel cambios = new ImagenesModel();
        cambios.setRuta("   /assets/img/Peliculas/spiderman-remaster.webp  ");
        cambios.setAltText("   Nuevo alt text   ");
        // sin cambiar producto

        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(imagenesRepository.save(any(ImagenesModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ImagenesModel actualizado = imagenesService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("/assets/img/Peliculas/spiderman-remaster.webp", actualizado.getRuta());
        assertEquals("Nuevo alt text", actualizado.getAltText());
        assertEquals(10L, actualizado.getProducto().getId()); // producto se mantiene
    }

    @Test
    public void testUpdate_CambiaProducto() {
        ImagenesModel existente = createImagen(1L, 10L);

        ImagenesModel cambios = new ImagenesModel();
        ProductoModel nuevoProdRef = new ProductoModel();
        nuevoProdRef.setId(20L);
        cambios.setProducto(nuevoProdRef);

        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(productoRepository.findById(20L))
                .thenReturn(Optional.of(createProducto(20L)));
        when(imagenesRepository.save(any(ImagenesModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ImagenesModel actualizado = imagenesService.update(1L, cambios);

        assertNotNull(actualizado);
        assertEquals(20L, actualizado.getProducto().getId());
    }

    @Test
    public void testUpdate_RutaVacia_LanzaIllegalArgument() {
        ImagenesModel existente = createImagen(1L, 10L);

        ImagenesModel cambios = new ImagenesModel();
        cambios.setRuta("   "); // se vuelve string vacío tras trim

        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class,
                () -> imagenesService.update(1L, cambios));
    }

    @Test
    public void testDeleteById() {
        ImagenesModel existente = createImagen(1L, 10L);

        when(imagenesRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        doNothing().when(imagenesRepository).deleteById(1L);

        imagenesService.deleteById(1L);

        verify(imagenesRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {
        when(imagenesRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> imagenesService.deleteById(99L));

        verify(imagenesRepository, never()).deleteById(any());
    }
}