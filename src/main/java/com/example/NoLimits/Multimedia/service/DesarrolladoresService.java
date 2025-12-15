package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.DesarrolladoresModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.DesarrolladoresRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DesarrolladoresService {

    @Autowired
    private DesarrolladoresRepository desarrolladoresRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DesarrolladorRepository desarrolladorRepository;

    /** Listar relaciones por producto */
    public List<DesarrolladoresModel> findByProducto(Long productoId) {
        return desarrolladoresRepository.findByProducto_Id(productoId);
    }

    /** Listar relaciones por desarrollador */
    public List<DesarrolladoresModel> findByDesarrollador(Long desarrolladorId) {
        return desarrolladoresRepository.findByDesarrollador_Id(desarrolladorId);
    }

    /** Crear vínculo Producto ↔ Desarrollador (idempotente) */
    public DesarrolladoresModel link(Long productoId, Long desarrolladorId) {

        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        DesarrolladorModel d = desarrolladorRepository.findById(desarrolladorId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Desarrollador no encontrado con ID: " + desarrolladorId));

        if (desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(productoId, desarrolladorId)) {
            return desarrolladoresRepository.findByProducto_Id(productoId).stream()
                    .filter(rel -> rel.getDesarrollador().getId().equals(desarrolladorId))
                    .findFirst()
                    .orElseGet(() -> {
                        DesarrolladoresModel rel = new DesarrolladoresModel();
                        rel.setProducto(p);
                        rel.setDesarrollador(d);
                        return rel;
                    });
        }

        DesarrolladoresModel rel = new DesarrolladoresModel();
        rel.setProducto(p);
        rel.setDesarrollador(d);

        return desarrolladoresRepository.save(rel);
    }

    /** PATCH: Actualizar parcialmente la relación Producto ↔ Desarrollador */
    public DesarrolladoresModel patch(Long relacionId, DesarrolladoresModel parciales) {

        DesarrolladoresModel existente = desarrolladoresRepository.findById(relacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación no encontrada con ID: " + relacionId));

        Long nuevoProductoId = parciales.getProducto() != null
                ? parciales.getProducto().getId()
                : null;

        Long nuevoDesarrolladorId = parciales.getDesarrollador() != null
                ? parciales.getDesarrollador().getId()
                : null;

        // Cambiar PRODUCTO si viene nuevo ID
        if (nuevoProductoId != null) {

            ProductoModel nuevoProducto = productoRepository.findById(nuevoProductoId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Producto no encontrado con ID: " + nuevoProductoId));

            // evitar duplicado (producto, desarrollador)
            if (desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(
                    nuevoProducto.getId(),
                    existente.getDesarrollador().getId()
            )) {
                throw new IllegalStateException("Ya existe una relación con ese producto y desarrollador.");
            }

            existente.setProducto(nuevoProducto);
        }

        // Cambiar DESARROLLADOR si viene nuevo ID
        if (nuevoDesarrolladorId != null) {

            DesarrolladorModel nuevoDev = desarrolladorRepository.findById(nuevoDesarrolladorId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Desarrollador no encontrado con ID: " + nuevoDesarrolladorId));

            // evitar duplicado
            if (desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(
                    existente.getProducto().getId(),
                    nuevoDev.getId()
            )) {
                throw new IllegalStateException("Ya existe una relación con ese producto y desarrollador.");
            }

            existente.setDesarrollador(nuevoDev);
        }

        return desarrolladoresRepository.save(existente);
    }

    /** Eliminar vínculo Producto ↔ Desarrollador */
    public void unlink(Long productoId, Long desarrolladorId) {

        productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        desarrolladorRepository.findById(desarrolladorId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Desarrollador no encontrado con ID: " + desarrolladorId));

        if (!desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(productoId, desarrolladorId)) {
            return; // idempotente
        }

        desarrolladoresRepository.deleteByProducto_IdAndDesarrollador_Id(productoId, desarrolladorId);
    }
}