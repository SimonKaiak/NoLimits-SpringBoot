package com.example.NoLimits.Multimedia.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.producto.request.ImagenesRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ImagenesResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ImagenesUpdateDTO;
import com.example.NoLimits.Multimedia.model.producto.ImagenesModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.producto.ImagenesRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImagenesService {

    @Autowired
    private ImagenesRepository imagenesRepository;

    @Autowired
    private ProductoRepository productoRepository;

    /* ===================== BÁSICOS ===================== */

    public List<ImagenesResponseDTO> findAll() {
        return imagenesRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ImagenesResponseDTO findById(Long id) {
        ImagenesModel entity = getImagenEntityOrThrow(id);
        return toResponseDTO(entity);
    }

    public List<ImagenesResponseDTO> findByProducto(Long productoId) {
        return imagenesRepository.findByProducto_Id(productoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ImagenesResponseDTO> findByRutaContainingIgnoreCase(String ruta) {
        return imagenesRepository.findByRutaContainingIgnoreCase(ruta)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ===================== CREAR ===================== */

    public ImagenesResponseDTO save(ImagenesRequestDTO dto) {

        if (dto.getProductoId() == null) {
            throw new IllegalArgumentException("Debe indicar el producto de la imagen");
        }

        ProductoModel p = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con ID: " + dto.getProductoId()));

        if (dto.getRuta() == null || dto.getRuta().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta/URL de la imagen es obligatoria");
        }

        ImagenesModel img = new ImagenesModel();
        img.setProducto(p);
        img.setRuta(dto.getRuta().trim());

        if (dto.getAltText() != null) {
            String alt = dto.getAltText().trim();
            img.setAltText(alt.isEmpty() ? null : alt);
        }

        ImagenesModel guardada = imagenesRepository.save(img);
        return toResponseDTO(guardada);
    }

    /* ===================== ACTUALIZAR (PUT) ===================== */

    public ImagenesResponseDTO update(Long id, ImagenesUpdateDTO in) {
        ImagenesModel e = getImagenEntityOrThrow(id);

        aplicarCambiosDesdeUpdateDTO(in, e);

        ImagenesModel actualizada = imagenesRepository.save(e);
        return toResponseDTO(actualizada);
    }

    /* ===================== PATCH ===================== */

    public ImagenesResponseDTO patch(Long id, ImagenesUpdateDTO in) {
        ImagenesModel e = getImagenEntityOrThrow(id);

        aplicarCambiosDesdeUpdateDTO(in, e);

        ImagenesModel actualizada = imagenesRepository.save(e);
        return toResponseDTO(actualizada);
    }

    /* ===================== ELIMINAR ===================== */

    public void deleteById(Long id) {
        ImagenesModel e = getImagenEntityOrThrow(id);
        imagenesRepository.delete(e);
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

    /* ===================== MAPEOS PRIVADOS ===================== */

    private ImagenesModel getImagenEntityOrThrow(Long id) {
        return imagenesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Imagen no encontrada con ID: " + id));
    }

    private ImagenesResponseDTO toResponseDTO(ImagenesModel entity) {
        ImagenesResponseDTO dto = new ImagenesResponseDTO();
        dto.setId(entity.getId());
        dto.setRuta(entity.getRuta());
        dto.setAltText(entity.getAltText());
        dto.setProductoId(entity.getProducto() != null ? entity.getProducto().getId() : null);
        return dto;
    }

    /**
     * Aplica los cambios de un DTO de actualización (PUT/PATCH) a la entidad.
     */
    private void aplicarCambiosDesdeUpdateDTO(ImagenesUpdateDTO in, ImagenesModel e) {

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

        if (in.getProductoId() != null) {
            ProductoModel nuevoProd = productoRepository.findById(in.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con ID: " + in.getProductoId()));
            e.setProducto(nuevoProd);
        }
    }
}