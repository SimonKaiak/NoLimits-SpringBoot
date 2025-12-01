package com.example.NoLimits.Multimedia.service.catalogos;

import java.util.List;
import java.util.stream.Collectors;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformasResponseDTO;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformasModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformasRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PlataformasService {

    @Autowired
    private PlataformasRepository plataformasRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PlataformaRepository plataformaRepository;

    // ================== CONSULTAS BÁSICAS ==================

    public List<PlataformasResponseDTO> findByProducto(Long productoId) {
        return plataformasRepository.findByProducto_Id(productoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PlataformasResponseDTO> findByPlataforma(Long plataformaId) {
        return plataformasRepository.findByPlataforma_Id(plataformaId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ================== VÍNCULO PRODUCTO ↔ PLATAFORMA ==================

    /**
     * Crea el vínculo Producto ↔ Plataforma si no existe.
     */
    public PlataformasResponseDTO link(Long productoId, Long plataformaId) {
        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        PlataformaModel pl = plataformaRepository.findById(plataformaId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Plataforma no encontrada con ID: " + plataformaId));

        if (plataformasRepository.existsByProducto_IdAndPlataforma_Id(productoId, plataformaId)) {
            PlataformasModel existente = plantasExisting(productoId, plataformaId, p, pl);
            return toResponseDTO(existente);
        }

        PlataformasModel rel = new PlataformasModel();
        rel.setProducto(p);
        rel.setPlataforma(pl);

        PlataformasModel guardado = plataformasRepository.save(rel);
        return toResponseDTO(guardado);
    }

    private PlataformasModel plantasExisting(Long productoId,
                                             Long plataformaId,
                                             ProductoModel p,
                                             PlataformaModel pl) {

        return plataformasRepository.findByProducto_Id(productoId).stream()
                .filter(r -> r.getPlataforma().getId().equals(plataformaId))
                .findFirst()
                .orElseGet(() -> {
                    PlataformasModel r = new PlataformasModel();
                    r.setProducto(p);
                    r.setPlataforma(pl);
                    return r;
                });
    }

    /**
     * Elimina el vínculo Producto ↔ Plataforma (idempotente).
     */
    public void unlink(Long productoId, Long plataformaId) {
        productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        plataformaRepository.findById(plataformaId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Plataforma no encontrada con ID: " + plataformaId));

        if (plataformasRepository.existsByProducto_IdAndPlataforma_Id(productoId, plataformaId)) {
            plataformasRepository.deleteByProducto_IdAndPlataforma_Id(productoId, plataformaId);
        }
    }

    /**
     * PATCH: Actualiza parcialmente la relación Producto–Plataforma.
     * Se puede cambiar el producto y/o la plataforma asociados.
     */
    public PlataformasResponseDTO patch(Long relacionId,
                                        Long nuevoProductoId,
                                        Long nuevaPlataformaId) {

        PlataformasModel rel = plataformasRepository.findById(relacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Relación Producto–Plataforma no encontrada con ID: " + relacionId));

        // Cambiar producto
        if (nuevoProductoId != null) {
            ProductoModel nuevoProducto = productoRepository.findById(nuevoProductoId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Producto no encontrado con ID: " + nuevoProductoId));

            if (plataformasRepository.existsByProducto_IdAndPlataforma_Id(
                    nuevoProductoId, rel.getPlataforma().getId())) {
                throw new IllegalArgumentException(
                        "Ya existe una relación con ese producto y plataforma");
            }

            rel.setProducto(nuevoProducto);
        }

        // Cambiar plataforma
        if (nuevaPlataformaId != null) {
            PlataformaModel nuevaPlataforma = plataformaRepository.findById(nuevaPlataformaId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException(
                                    "Plataforma no encontrada con ID: " + nuevaPlataformaId));

            if (plataformasRepository.existsByProducto_IdAndPlataforma_Id(
                    rel.getProducto().getId(), nuevaPlataformaId)) {
                throw new IllegalArgumentException(
                        "Ya existe una relación con ese producto y plataforma");
            }

            rel.setPlataforma(nuevaPlataforma);
        }

        PlataformasModel guardado = plataformasRepository.save(rel);
        return toResponseDTO(guardado);
    }

    // ================== MAPPER ENTITY → DTO ==================

    private PlataformasResponseDTO toResponseDTO(PlataformasModel rel) {
        if (rel == null) {
            return null;
        }

        PlataformasResponseDTO dto = new PlataformasResponseDTO();
        dto.setId(rel.getId());
        dto.setProductoId(
                rel.getProducto() != null ? rel.getProducto().getId() : null
        );
        dto.setPlataformaId(
                rel.getPlataforma() != null ? rel.getPlataforma().getId() : null
        );
        dto.setPlataformaNombre(
                rel.getPlataforma() != null ? rel.getPlataforma().getNombre() : null
        );
        return dto;
    }
}