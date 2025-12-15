package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ComunaModel;
import com.example.NoLimits.Multimedia.model.DireccionModel;
import com.example.NoLimits.Multimedia.repository.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.DireccionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Dirección no encontrada con ID: " + id));
    }

    public DireccionModel save(DireccionModel direccion) {

        if (direccion.getComuna() == null || direccion.getComuna().getId() == null) {
            throw new IllegalArgumentException("Debe especificar una comuna válida");
        }

        ComunaModel comuna = comunaRepository.findById(direccion.getComuna().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada"));

        direccion.setComuna(comuna);

        return direccionRepository.save(direccion);
    }

    /** PATCH: actualización parcial de una dirección. */
    public DireccionModel patch(Long id, DireccionModel entrada) {

        DireccionModel existente = findById(id);

        // calle
        if (entrada.getCalle() != null) {
            if (entrada.getCalle().isBlank()) {
                throw new IllegalArgumentException("La calle no puede estar vacía");
            }
            existente.setCalle(entrada.getCalle());
        }

        // número
        if (entrada.getNumero() != null) {
            if (entrada.getNumero().isBlank()) {
                throw new IllegalArgumentException("El número no puede estar vacío");
            }
            existente.setNumero(entrada.getNumero());
        }

        // complemento (depto, block, etc.)
        if (entrada.getComplemento() != null) {
            existente.setComplemento(entrada.getComplemento());
        }

        // código postal
        if (entrada.getCodigoPostal() != null) {
            existente.setCodigoPostal(entrada.getCodigoPostal());
        }

        // comuna
        if (entrada.getComuna() != null && entrada.getComuna().getId() != null) {
            ComunaModel comuna = comunaRepository.findById(entrada.getComuna().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada"));
            existente.setComuna(comuna);
        }

        // usuario (se asume que viene con un ID válido; si no, fallará por la capa JPA/BD)
        if (entrada.getUsuarioModel() != null) {
            existente.setUsuarioModel(entrada.getUsuarioModel());
        }

        return direccionRepository.save(existente);
    }

    public void deleteById(Long id) {
        DireccionModel existente = findById(id);
        direccionRepository.delete(existente);
    }
}