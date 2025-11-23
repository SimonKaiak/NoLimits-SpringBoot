// Ruta: backend/src/main/java/com/example/NoLimits/Multimedia/service/DesarrolladorService.java

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

        if (desarrolladorRepository.existsByNombreIgnoreCase(normalizado)) {
            throw new IllegalArgumentException("Ya existe un desarrollador con ese nombre");
        }

        d.setNombre(normalizado);

        // si desde el front no envían nada, queda true por defecto
        if (d.getId() == null && !d.isActivo()) {
            d.setActivo(true);
        }

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

        // actualizar activo si viene en el body
        // (Jackson setea false si viene explícito en el JSON)
        if (in.isActivo() != d.isActivo()) {
            d.setActivo(in.isActivo());
        }

        return desarrolladorRepository.save(d);
    }

    public DesarrolladorModel patch(Long id, DesarrolladorModel in) {
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

        // para patch podrías manejar activo con un wrapper Boolean, pero
        // si lo dejas así y el JSON trae "activo": false, también se aplicará
        if (in.isActivo() != d.isActivo()) {
            d.setActivo(in.isActivo());
        }

        return desarrolladorRepository.save(d);
    }

    public void deleteById(Long id) {
        findById(id);
        desarrolladorRepository.deleteById(id);
    }

    public List<DesarrolladorModel> findByNombre(String nombre) {
        String filtro = (nombre == null) ? "" : nombre.trim();
        return desarrolladorRepository.findByNombreContainingIgnoreCase(filtro);
    }
}