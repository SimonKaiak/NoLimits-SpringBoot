package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.DesarrolladorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DesarrolladorService {

    @Autowired
    private DesarrolladorRepository desarrolladorRepository;

    public List<DesarrolladorModel> findAll() {
        return desarrolladorRepository.findAll();
    }

    public DesarrolladorModel findById(Long id) {
        return desarrolladorRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Desarrollador no encontrado con ID: " + id));
    }

    public DesarrolladorModel save(DesarrolladorModel d) {
        if (d.getNombre() == null || d.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del desarrollador es obligatorio");
        }

        String normalizado = d.getNombre().trim();

        // evitar duplicados
        if (desarrolladorRepository.existsByNombreIgnoreCase(normalizado)) {
            throw new IllegalArgumentException("Ya existe un desarrollador con ese nombre");
        }

        d.setNombre(normalizado);
        return desarrolladorRepository.save(d);
    }

    public DesarrolladorModel update(Long id, DesarrolladorModel in) {
        DesarrolladorModel d = findById(id);

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (!nuevo.equalsIgnoreCase(d.getNombre())
                    && desarrolladorRepository.existsByNombreIgnoreCase(nuevo)) {
                throw new IllegalArgumentException("Ya existe un desarrollador con ese nombre");
            }
            d.setNombre(nuevo);
        }

        return desarrolladorRepository.save(d);
    }

    public void deleteById(Long id) {
        // 404 si no existe
        findById(id);
        desarrolladorRepository.deleteById(id);
    }

    // búsquedas de apoyo
    public List<DesarrolladorModel> findByNombre(String nombre) {
        String filtro = (nombre == null) ? "" : nombre.trim();
        return desarrolladorRepository.findByNombreContainingIgnoreCase(filtro);
    }
}