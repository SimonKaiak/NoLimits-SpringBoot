package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.GeneroModel;
import com.example.NoLimits.Multimedia.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    public List<GeneroModel> findAll() {
        return generoRepository.findAll();
    }

    public GeneroModel findById(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Género no encontrado con ID: " + id));
    }

    public List<GeneroModel> findByNombreContaining(String nombre) {
        return generoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public GeneroModel save(GeneroModel g) {
        if (g.getNombre() == null || g.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del género es obligatorio");
        }
        String nombre = g.getNombre().trim();
        g.setNombre(nombre);
        return generoRepository.save(g);
    }

    public GeneroModel update(Long id, GeneroModel in) {
        GeneroModel g = findById(id);

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            g.setNombre(nuevo);
        }

        return generoRepository.save(g);
    }

    public GeneroModel patch(Long id, GeneroModel in) {
        GeneroModel g = findById(id);

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            g.setNombre(nuevo);
        }

        return generoRepository.save(g);
    }

    public void deleteById(Long id) {
        findById(id);
        generoRepository.deleteById(id);
    }

    public List<Object[]> obtenerGenerosResumen() {
        return generoRepository.obtenerGenerosResumen();
    }
}