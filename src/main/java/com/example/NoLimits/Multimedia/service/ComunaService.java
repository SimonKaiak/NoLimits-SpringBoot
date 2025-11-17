package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ComunaModel;
import com.example.NoLimits.Multimedia.repository.ComunaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    public List<ComunaModel> findAll() {
        return comunaRepository.findAll();
    }

    public ComunaModel findById(Long id) {
        return comunaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada con ID: " + id));
    }

    public ComunaModel save(ComunaModel comuna) {
        if (comuna.getNombre() == null || comuna.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la comuna es obligatorio");
        }
        return comunaRepository.save(comuna);
    }

    public ComunaModel update(Long id, ComunaModel detalles) {
        ComunaModel existente = findById(id);
        if (detalles.getNombre() != null) existente.setNombre(detalles.getNombre());
        if (detalles.getRegion() != null) existente.setRegion(detalles.getRegion());
        return comunaRepository.save(existente);
    }

    public void deleteById(Long id) {
        ComunaModel existente = findById(id);
        comunaRepository.delete(existente);
    }
}