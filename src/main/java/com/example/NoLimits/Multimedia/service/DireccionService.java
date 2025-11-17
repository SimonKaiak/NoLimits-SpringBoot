package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DireccionModel;
import com.example.NoLimits.Multimedia.repository.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    public List<DireccionModel> findAll() {
        return direccionRepository.findAll();
    }

    public DireccionModel findById(Long id) {
        return direccionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Dirección no encontrada con ID: " + id));
    }

    public DireccionModel save(DireccionModel direccion) {
        if (direccion.getComuna() == null || direccion.getComuna().getId() == null)
            throw new IllegalArgumentException("Debe especificar una comuna válida");

        direccion.setComuna(comunaRepository.findById(direccion.getComuna().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada")));

        return direccionRepository.save(direccion);
    }

    public void deleteById(Long id) {
        DireccionModel existente = findById(id);
        direccionRepository.delete(existente);
    }
}