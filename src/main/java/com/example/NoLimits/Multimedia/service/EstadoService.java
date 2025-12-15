package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.EstadoModel;
import com.example.NoLimits.Multimedia.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    // ================== CRUD BÁSICO ==================

    public List<EstadoModel> findAll() {
        return estadoRepository.findAll();
    }

    public EstadoModel findById(Long id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + id));
    }

    public EstadoModel save(EstadoModel e) {
        if (e.getNombre() == null || e.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado es obligatorio");
        }

        String nombre = e.getNombre().trim();
        if (estadoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe un estado con el nombre: " + nombre);
        }

        e.setNombre(nombre);

        // Si no viene definido, por defecto true
        if (e.getActivo() == null) {
            e.setActivo(true);
        }

        return estadoRepository.save(e);
    }

    /**
     * Actualización tipo PUT: se espera que vengan todos los campos obligatorios.
     */
    public EstadoModel update(Long id, EstadoModel in) {
        EstadoModel existente = findById(id);

        // nombre
        if (in.getNombre() != null) {
            String nuevoNombre = in.getNombre().trim();
            if (nuevoNombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }

            // Verificar duplicado con otro registro
            estadoRepository.findByNombreIgnoreCase(nuevoNombre)
                    .ifPresent(otro -> {
                        if (!otro.getId().equals(id)) {
                            throw new IllegalArgumentException(
                                    "Ya existe otro estado con el nombre: " + nuevoNombre
                            );
                        }
                    });

            existente.setNombre(nuevoNombre);
        }

        // descripción
        if (in.getDescripcion() != null) {
            existente.setDescripcion(in.getDescripcion());
        }

        // activo
        if (in.getActivo() != null) {
            existente.setActivo(in.getActivo());
        }

        return estadoRepository.save(existente);
    }

    /**
     * Actualización parcial tipo PATCH: solo modifica los campos enviados.
     */
    public EstadoModel patch(Long id, EstadoModel in) {
        EstadoModel existente = findById(id);

        // nombre (opcional)
        if (in.getNombre() != null) {
            String nuevoNombre = in.getNombre().trim();
            if (nuevoNombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }

            estadoRepository.findByNombreIgnoreCase(nuevoNombre)
                    .ifPresent(otro -> {
                        if (!otro.getId().equals(id)) {
                            throw new IllegalArgumentException(
                                    "Ya existe otro estado con el nombre: " + nuevoNombre
                            );
                        }
                    });

            existente.setNombre(nuevoNombre);
        }

        // descripción (opcional)
        if (in.getDescripcion() != null) {
            existente.setDescripcion(in.getDescripcion());
        }

        // activo (opcional)
        if (in.getActivo() != null) {
            existente.setActivo(in.getActivo());
        }

        return estadoRepository.save(existente);
    }

    public void deleteById(Long id) {
        // 404 si no existe
        findById(id);
        estadoRepository.deleteById(id);
    }

    // ================== CONSULTAS ADICIONALES ==================

    public List<EstadoModel> findByNombreLike(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }
        return estadoRepository.findByNombreContainingIgnoreCase(nombre.trim());
    }

    public EstadoModel findByNombreExact(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el nombre del estado");
        }
        return estadoRepository.findByNombreIgnoreCase(nombre.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Estado no encontrado con nombre: " + nombre));
    }

    public List<EstadoModel> findActivos() {
        return estadoRepository.findByActivoTrue();
    }

    public List<EstadoModel> findInactivos() {
        return estadoRepository.findByActivoFalse();
    }

    // Resumen tipo tabla (id, nombre, descripcion, activo)
    public List<Map<String, Object>> obtenerEstadosResumen() {
        List<Object[]> resultados = estadoRepository.obtenerEstadosResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("ID", fila[0]);
            map.put("Nombre", fila[1]);
            map.put("Descripcion", fila[2]);
            map.put("Activo", fila[3]);
            lista.add(map);
        }

        return lista;
    }
}