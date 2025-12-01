package com.example.NoLimits.Multimedia.service.catalogos;

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
    public List<DesarrolladoresResponseDTO> findByProducto(Long productoId) {
        List<DesarrolladoresModel> relaciones =
                desarrolladoresRepository.findByProducto_Id(productoId);
        return relaciones.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /** Listar relaciones por desarrollador */
    public List<DesarrolladoresResponseDTO> findByDesarrollador(Long desarrolladorId) {
        List<DesarrolladoresModel> relaciones =
                desarrolladoresRepository.findByDesarrollador_Id(desarrolladorId);
        return relaciones.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /** Crear vínculo Producto ↔ Desarrollador (idempotente) */
    public DesarrolladoresResponseDTO link(DesarrolladoresRequestDTO dto) {

        Long productoId = dto.getProductoId();
        Long desarrolladorId = dto.getDesarrolladorId();

        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        DesarrolladorModel d = desarrolladorRepository.findById(desarrolladorId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Desarrollador no encontrado con ID: " + desarrolladorId));

        // Si ya existe la relación, devolverla (idempotente)
        if (desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(productoId, desarrolladorId)) {
            DesarrolladoresModel existente = desarrolladoresRepository.findByProducto_Id(productoId).stream()
                    .filter(rel -> rel.getDesarrollador().getId().equals(desarrolladorId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "La relación existe pero no se pudo recuperar desde la base de datos.")
                    );
            return toResponseDTO(existente);
        }

        DesarrolladoresModel rel = new DesarrolladoresModel();
        rel.setProducto(p);
        rel.setDesarrollador(d);

        DesarrolladoresModel guardada = desarrolladoresRepository.save(rel);
        return toResponseDTO(guardada);
    }

    /** PATCH: Actualizar parcialmente la relación Producto ↔ Desarrollador */
    public DesarrolladoresResponseDTO patch(Long relacionId, DesarrolladoresUpdateDTO parciales) {

        DesarrolladoresModel existente = desarrolladoresRepository.findById(relacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación no encontrada con ID: " + relacionId));

        Long nuevoProductoId = parciales.getProductoId();
        Long nuevoDesarrolladorId = parciales.getDesarrolladorId();

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

        DesarrolladoresModel actualizado = desarrolladoresRepository.save(existente);
        return toResponseDTO(actualizado);
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

    // ================== MAPEOS A DTO ==================

    private DesarrolladoresResponseDTO toResponseDTO(DesarrolladoresModel rel) {
        DesarrolladoresResponseDTO dto = new DesarrolladoresResponseDTO();
        dto.setId(rel.getId());
        dto.setProductoId(rel.getProducto() != null ? rel.getProducto().getId() : null);
        dto.setDesarrolladorId(rel.getDesarrollador() != null ? rel.getDesarrollador().getId() : null);

        // Si tu DTO tiene campos extra (nombres, etc.), puedes mapearlos aquí:
        // dto.setProductoNombre(rel.getProducto() != null ? rel.getProducto().getNombre() : null);
        // dto.setDesarrolladorNombre(rel.getDesarrollador() != null ? rel.getDesarrollador().getNombre() : null);

        return dto;
    }
}