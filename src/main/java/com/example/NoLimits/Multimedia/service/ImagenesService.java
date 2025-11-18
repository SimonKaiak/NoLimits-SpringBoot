package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ImagenesModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.ImagenesRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ImagenesService {

    @Autowired
    private ImagenesRepository imagenesRepository;

    @Autowired
    private ProductoRepository productoRepository;

    /* ===================== BÁSICOS ===================== */

    public List<ImagenesModel> findAll() {
        return imagenesRepository.findAll();
    }

    public ImagenesModel findById(Long id) {
        return imagenesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Imagen no encontrada con ID: " + id));
    }

    public List<ImagenesModel> findByProducto(Long productoId) {
        return imagenesRepository.findByProducto_Id(productoId);
    }

    public List<ImagenesModel> findByRutaContainingIgnoreCase(String ruta) {
        return imagenesRepository.findByRutaContainingIgnoreCase(ruta);
    }

    /* ===================== CREAR / ACTUALIZAR ===================== */

    public ImagenesModel save(ImagenesModel img) {
        if (img.getProducto() == null || img.getProducto().getId() == null) {
            throw new IllegalArgumentException("Debe indicar el producto de la imagen");
        }

        ProductoModel p = productoRepository.findById(img.getProducto().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con ID: " + img.getProducto().getId()));

        img.setProducto(p);

        if (img.getRuta() == null || img.getRuta().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta/URL de la imagen es obligatoria");
        }
        img.setRuta(img.getRuta().trim());

        if (img.getAltText() != null) {
            String alt = img.getAltText().trim();
            img.setAltText(alt.isEmpty() ? null : alt);
        }

        return imagenesRepository.save(img);
    }

    public ImagenesModel update(Long id, ImagenesModel in) {
        ImagenesModel e = findById(id);

        if (in.getRuta() != null) {
            String v = in.getRuta().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("La ruta/URL no puede ser vacía");
            }
            e.setRuta(v);
        }

        if (in.getAltText() != null) {
            String alt = in.getAltText().trim();
            e.setAltText(alt.isEmpty() ? null : alt);
        }

        if (in.getProducto() != null && in.getProducto().getId() != null) {
            Long nuevoProdId = in.getProducto().getId();
            Long actualProdId = (e.getProducto() != null ? e.getProducto().getId() : null);

            if (actualProdId == null || !actualProdId.equals(nuevoProdId)) {
                ProductoModel p = productoRepository.findById(nuevoProdId)
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Producto no encontrado con ID: " + nuevoProdId));
                e.setProducto(p);
            }
        }

        return imagenesRepository.save(e);
    }

    /* ===================== PATCH ===================== */

    public ImagenesModel patch(Long id, ImagenesModel in) {
        ImagenesModel e = findById(id);

        if (in.getRuta() != null) {
            String v = in.getRuta().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("La ruta/URL no puede ser vacía");
            }
            e.setRuta(v);
        }

        if (in.getAltText() != null) {
            String alt = in.getAltText().trim();
            e.setAltText(alt.isEmpty() ? null : alt);
        }

        if (in.getProducto() != null && in.getProducto().getId() != null) {
            ProductoModel nuevoProd = productoRepository.findById(in.getProducto().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con ID: " + in.getProducto().getId()));
            e.setProducto(nuevoProd);
        }

        return imagenesRepository.save(e);
    }

    /* ===================== ELIMINAR ===================== */

    public void deleteById(Long id) {
        findById(id);
        imagenesRepository.deleteById(id);
    }

    public long deleteByProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new RecursoNoEncontradoException(
                    "Producto no encontrado con ID: " + productoId);
        }
        return imagenesRepository.deleteByProducto_Id(productoId);
    }

    /* ===================== RESUMEN ===================== */

    public List<Map<String, Object>> obtenerImagenesResumen() {
        List<Object[]> resultados = imagenesRepository.obtenerImagenesResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID", fila[0]);
            datos.put("Ruta", fila[1]);
            datos.put("AltText", fila[2]);
            datos.put("ProductoId", fila[3]);
            lista.add(datos);
        }
        return lista;
    }
}