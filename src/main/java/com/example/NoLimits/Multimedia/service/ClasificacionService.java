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

    public ClasificacionModel update(Long id, ClasificacionModel detalles) {
        ClasificacionModel existente = findById(id);

        if (detalles.getNombre() != null && !detalles.getNombre().isBlank()) {
            String nombreNormalizado = detalles.getNombre().trim();
            // Validar duplicados solo si cambia el nombre
            if (!nombreNormalizado.equalsIgnoreCase(existente.getNombre())
                    && clasificacionRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
                throw new IllegalArgumentException("Ya existe una clasificación con el nombre: " + nombreNormalizado);
            }
            existente.setNombre(nombreNormalizado);
        }

        if (detalles.getDescripcion() != null) {
            existente.setDescripcion(detalles.getDescripcion());
        }

        // Para update completo podrías decidir actualizar 'activo' aquí.
        // Si más adelante haces PATCH, conviene manejarlo allá.
        existente.setActivo(detalles.isActivo());

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