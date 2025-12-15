package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ClasificacionModel;
import com.example.NoLimits.Multimedia.repository.ClasificacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ClasificacionService {

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    // ================== CRUD BÁSICO ==================

    public List<ClasificacionModel> findAll() {
        return clasificacionRepository.findAll();
    }

    public ClasificacionModel findById(Long id) {
        return clasificacionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Clasificación no encontrada con ID: " + id));
    }

    public ClasificacionModel save(ClasificacionModel clasificacion) {
        if (clasificacion.getNombre() == null || clasificacion.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la clasificación es obligatorio");
        }

        String nombreNormalizado = clasificacion.getNombre().trim();
        if (clasificacionRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new IllegalArgumentException("Ya existe una clasificación con el nombre: " + nombreNormalizado);
        }

        clasificacion.setNombre(nombreNormalizado);
        // 'activo' ya viene por defecto en true en el modelo
        return clasificacionRepository.save(clasificacion);
    }

    /**
     * PUT: actualización completa.
     */
    public ClasificacionModel update(Long id, ClasificacionModel detalles) {
        ClasificacionModel existente = findById(id);

        if (detalles.getNombre() == null || detalles.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la clasificación es obligatorio en PUT");
        }

        String nombreNormalizado = detalles.getNombre().trim();
        if (!nombreNormalizado.equalsIgnoreCase(existente.getNombre())
                && clasificacionRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new IllegalArgumentException("Ya existe una clasificación con el nombre: " + nombreNormalizado);
        }
        existente.setNombre(nombreNormalizado);

        existente.setDescripcion(detalles.getDescripcion());
        existente.setActivo(detalles.isActivo());

        return clasificacionRepository.save(existente);
    }

    /**
     * PATCH: actualización parcial.
     * Solo modifica los campos presentes en el mapa.
     */
    public ClasificacionModel patch(Long id, Map<String, Object> campos) {
        ClasificacionModel existente = findById(id);

        if (campos.containsKey("nombre")) {
            Object valor = campos.get("nombre");
            if (valor == null || valor.toString().isBlank()) {
                throw new IllegalArgumentException("El nombre de la clasificación no puede ser vacío");
            }

            String nombreNormalizado = valor.toString().trim();
            if (!nombreNormalizado.equalsIgnoreCase(existente.getNombre())
                    && clasificacionRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
                throw new IllegalArgumentException("Ya existe una clasificación con el nombre: " + nombreNormalizado);
            }
            existente.setNombre(nombreNormalizado);
        }

        if (campos.containsKey("descripcion")) {
            Object valor = campos.get("descripcion");
            existente.setDescripcion(valor != null ? valor.toString() : null);
        }

        if (campos.containsKey("activo")) {
            Object valor = campos.get("activo");
            if (valor == null) {
                throw new IllegalArgumentException("El campo 'activo' no puede ser null");
            }
            if (valor instanceof Boolean booleanValue) {
                existente.setActivo(booleanValue);
            } else {
                throw new IllegalArgumentException("El campo 'activo' debe ser boolean (true/false)");
            }
        }

        return clasificacionRepository.save(existente);
    }

    public void deleteById(Long id) {
        ClasificacionModel existente = findById(id);
        // Por ahora eliminación física. Más adelante se puede cambiar a "soft delete" (activo = false).
        clasificacionRepository.delete(existente);
    }

    // ================== BÚSQUEDAS ESPECÍFICAS ==================

    public List<ClasificacionModel> findByNombreContainingIgnoreCase(String nombre) {
        return clasificacionRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public ClasificacionModel findByNombreExactIgnoreCase(String nombre) {
        return clasificacionRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Clasificación no encontrada con nombre: " + nombre));
    }

    public List<ClasificacionModel> findActivas() {
        return clasificacionRepository.findByActivoTrue();
    }

    public List<ClasificacionModel> findInactivas() {
        return clasificacionRepository.findByActivoFalse();
    }

    // ================== RESUMEN ==================

    public List<Map<String, Object>> obtenerClasificacionesConDatos() {
        List<Object[]> resultados = clasificacionRepository.obtenerClasificacionesResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID", fila[0]);
            datos.put("Nombre", fila[1]);
            datos.put("Descripcion", fila[2]);
            datos.put("Activo", fila[3]);
            lista.add(datos);
        }

        return lista;
    }
}