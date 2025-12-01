package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.ClasificacionRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.ClasificacionResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.ClasificacionUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.repository.catalogos.ClasificacionRepository;

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
public class ClasificacionService {

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    // ================== MAPPERS ==================

    /**
     * Convierte una entidad ClasificacionModel a su DTO de respuesta.
     */
    private ClasificacionResponseDTO toResponseDTO(ClasificacionModel model) {
        if (model == null) return null;

        ClasificacionResponseDTO dto = new ClasificacionResponseDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setDescripcion(model.getDescripcion());
        dto.setActivo(model.isActivo());
        return dto;
    }

    /**
     * Convierte un DTO de creación/PUT a una entidad nueva.
     */
    private ClasificacionModel fromRequestDTO(ClasificacionRequestDTO dto) {
        ClasificacionModel model = new ClasificacionModel();
        model.setNombre(dto.getNombre());
        model.setDescripcion(dto.getDescripcion());
        // 'activo' ya viene por defecto en true en el modelo,
        // pero si en el DTO viene especificado, lo respetamos.
        if (dto.getActivo() != null) {
            model.setActivo(dto.getActivo());
        }
        return model;
    }

    /**
     * Obtiene la entidad por ID o lanza excepción de recurso no encontrado.
     */
    private ClasificacionModel getEntityById(Long id) {
        return clasificacionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Clasificación no encontrada con ID: " + id));
    }

    // ================== CRUD BÁSICO ==================

    public List<ClasificacionResponseDTO> findAll() {
        return clasificacionRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClasificacionResponseDTO findById(Long id) {
        ClasificacionModel model = getEntityById(id);
        return toResponseDTO(model);
    }

    public ClasificacionResponseDTO create(ClasificacionRequestDTO dto) {

        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la clasificación es obligatorio");
        }

        String nombreNormalizado = dto.getNombre().trim();
        if (clasificacionRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new IllegalArgumentException("Ya existe una clasificación con el nombre: " + nombreNormalizado);
        }

        ClasificacionModel clasificacion = fromRequestDTO(dto);
        clasificacion.setNombre(nombreNormalizado); // guardo el nombre normalizado

        ClasificacionModel guardada = clasificacionRepository.save(clasificacion);
        return toResponseDTO(guardada);
    }

    /**
     * PUT: actualización completa.
     */
    public ClasificacionResponseDTO update(Long id, ClasificacionRequestDTO dto) {
        ClasificacionModel existente = getEntityById(id);

        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la clasificación es obligatorio en PUT");
        }

        String nombreNormalizado = dto.getNombre().trim();
        if (!nombreNormalizado.equalsIgnoreCase(existente.getNombre())
                && clasificacionRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new IllegalArgumentException("Ya existe una clasificación con el nombre: " + nombreNormalizado);
        }

        existente.setNombre(nombreNormalizado);
        existente.setDescripcion(dto.getDescripcion());

        // En PUT esperamos que 'activo' venga siempre informado.
        if (dto.getActivo() == null) {
            throw new IllegalArgumentException("El campo 'activo' es obligatorio en PUT");
        }
        existente.setActivo(dto.getActivo());

        ClasificacionModel actualizada = clasificacionRepository.save(existente);
        return toResponseDTO(actualizada);
    }

    /**
     * PATCH: actualización parcial.
     * Solo modifica los campos presentes en el DTO de actualización.
     */
    public ClasificacionResponseDTO patch(Long id, ClasificacionUpdateDTO campos) {
        ClasificacionModel existente = getEntityById(id);

        if (campos.getNombre() != null) {
            String nombre = campos.getNombre();
            if (nombre.isBlank()) {
                throw new IllegalArgumentException("El nombre de la clasificación no puede ser vacío");
            }

            String nombreNormalizado = nombre.trim();
            if (!nombreNormalizado.equalsIgnoreCase(existente.getNombre())
                    && clasificacionRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
                throw new IllegalArgumentException("Ya existe una clasificación con el nombre: " + nombreNormalizado);
            }
            existente.setNombre(nombreNormalizado);
        }

        if (campos.getDescripcion() != null) {
            existente.setDescripcion(campos.getDescripcion());
        }

        if (campos.getActivo() != null) {
            existente.setActivo(campos.getActivo());
        }

        ClasificacionModel actualizada = clasificacionRepository.save(existente);
        return toResponseDTO(actualizada);
    }

    public void deleteById(Long id) {
        ClasificacionModel existente = getEntityById(id);
        // Por ahora eliminación física. Más adelante se puede cambiar a "soft delete" (activo = false).
        clasificacionRepository.delete(existente);
    }

    // ================== BÚSQUEDAS ESPECÍFICAS ==================

    public List<ClasificacionResponseDTO> findByNombreContainingIgnoreCase(String nombre) {
        return clasificacionRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClasificacionResponseDTO findByNombreExactIgnoreCase(String nombre) {
        ClasificacionModel model = clasificacionRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Clasificación no encontrada con nombre: " + nombre));
        return toResponseDTO(model);
    }

    public List<ClasificacionResponseDTO> findActivas() {
        return clasificacionRepository.findByActivoTrue()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ClasificacionResponseDTO> findInactivas() {
        return clasificacionRepository.findByActivoFalse()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
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