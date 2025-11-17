package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.GeneroModel;
import com.example.NoLimits.Multimedia.model.GenerosModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.GeneroRepository;
import com.example.NoLimits.Multimedia.repository.GenerosRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GenerosService {

    @Autowired
    private GenerosRepository generosRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private GeneroRepository generoRepository;

    /**
     * Obtiene todas las relaciones Producto–Género de un producto.
     */
    public List<GenerosModel> findByProducto(Long productoId) {
        return generosRepository.findByProducto_Id(productoId);
    }

    /**
     * Obtiene todas las relaciones Producto–Género de un género.
     */
    public List<GenerosModel> findByGenero(Long generoId) {
        return generosRepository.findByGenero_Id(generoId);
    }

    /**
     * Vincula Producto ↔ Género si no existe la relación.
     * Si ya existe, devuelve la relación existente.
     */
    public GenerosModel link(Long productoId, Long generoId) {
        // Validar existencia de Producto
        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        // Validar existencia de Género
        GeneroModel g = generoRepository.findById(generoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Género no encontrado con ID: " + generoId));

        // Si la relación ya existe, la retornamos
        if (generosRepository.existsByProducto_IdAndGenero_Id(productoId, generoId)) {
            return generosRepository.findByProducto_Id(productoId).stream()
                    .filter(rel -> rel.getGenero() != null
                            && generoId.equals(rel.getGenero().getId()))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "La relación Producto-Género existe en BD pero no se pudo recuperar."));
        }

        // Crear nueva relación
        GenerosModel rel = new GenerosModel();
        rel.setProducto(p);
        rel.setGenero(g);
        return generosRepository.save(rel);
    }

    /**
     * Elimina el vínculo Producto ↔ Género si existe.
     */
    public void unlink(Long productoId, Long generoId) {
        // Validar existencia de Producto y Género
        productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        generoRepository.findById(generoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Género no encontrado con ID: " + generoId));

        if (generosRepository.existsByProducto_IdAndGenero_Id(productoId, generoId)) {
            generosRepository.deleteByProducto_IdAndGenero_Id(productoId, generoId);
        }
    }

    /**
     * Devuelve un resumen crudo de relaciones Producto–Género.
     * Cada fila: [relId, productoId, productoNombre, generoId, generoNombre]
     */
    public List<Object[]> obtenerResumen(Long productoId, Long generoId) {
        return generosRepository.obtenerResumen(productoId, generoId);
    }
}