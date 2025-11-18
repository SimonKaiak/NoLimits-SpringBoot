package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.PlataformaModel;
import com.example.NoLimits.Multimedia.repository.PlataformaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PlataformaService {

    @Autowired
    private PlataformaRepository plataformaRepository;

    public List<PlataformaModel> findAll() {
        return plataformaRepository.findAll();
    }

    public PlataformaModel findById(Long id) {
        return plataformaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Plataforma no encontrada con ID: " + id));
    }

    public PlataformaModel save(PlataformaModel p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la plataforma es obligatorio");
        }

        p.setNombre(p.getNombre().trim());
        return plataformaRepository.save(p);
    }

    public PlataformaModel update(Long id, PlataformaModel in) {
        PlataformaModel p = findById(id);

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            p.setNombre(nuevo);
        }

        return plataformaRepository.save(p);
    }

    public PlataformaModel patch(Long id, PlataformaModel in) {
        PlataformaModel p = findById(id);

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            p.setNombre(nuevo);
        }

        return plataformaRepository.save(p);
    }

    public void deleteById(Long id) {
        findById(id);
        plataformaRepository.deleteById(id);
    }
}