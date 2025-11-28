package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.model.catalogos.GenerosModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.GeneroRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.GenerosRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;

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

    public List<GenerosModel> findByProducto(Long productoId) {
        return generosRepository.findByProducto_Id(productoId);
    }

    public List<GenerosModel> findByGenero(Long generoId) {
        return generosRepository.findByGenero_Id(generoId);
    }

    public GenerosModel link(Long productoId, Long generoId) {
        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        GeneroModel g = generoRepository.findById(generoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Género no encontrado con ID: " + generoId));

        if (generosRepository.existsByProducto_IdAndGenero_Id(productoId, generoId)) {
            return generosRepository.findByProducto_Id(productoId).stream()
                    .filter(rel -> rel.getGenero() != null
                            && generoId.equals(rel.getGenero().getId()))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "La relación Producto-Género existe en BD pero no se pudo recuperar."));
        }

        GenerosModel rel = new GenerosModel();
        rel.setProducto(p);
        rel.setGenero(g);
        return generosRepository.save(rel);
    }

    public void unlink(Long productoId, Long generoId) {
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
     * PATCH: Actualiza parcialmente la relación Producto–Género.
     * Se puede cambiar el producto o el género asociado.
     */
    public GenerosModel patch(Long relacionId, Long nuevoProductoId, Long nuevoGeneroId) {

        GenerosModel rel = generosRepository.findById(relacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación Producto–Género no encontrada con ID: " + relacionId));

        if (nuevoProductoId != null) {
            ProductoModel nuevoProducto = productoRepository.findById(nuevoProductoId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Producto no encontrado con ID: " + nuevoProductoId));

            if (generosRepository.existsByProducto_IdAndGenero_Id(nuevoProductoId, rel.getGenero().getId())) {
                throw new IllegalArgumentException("Ya existe una relación con ese producto y género");
            }

            rel.setProducto(nuevoProducto);
        }

        if (nuevoGeneroId != null) {
            GeneroModel nuevoGenero = generoRepository.findById(nuevoGeneroId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Género no encontrado con ID: " + nuevoGeneroId));

            if (generosRepository.existsByProducto_IdAndGenero_Id(rel.getProducto().getId(), nuevoGeneroId)) {
                throw new IllegalArgumentException("Ya existe una relación con ese producto y género");
            }

            rel.setGenero(nuevoGenero);
        }

        return generosRepository.save(rel);
    }

    public List<Object[]> obtenerResumen(Long productoId, Long generoId) {
        return generosRepository.obtenerResumen(productoId, generoId);
    }
}