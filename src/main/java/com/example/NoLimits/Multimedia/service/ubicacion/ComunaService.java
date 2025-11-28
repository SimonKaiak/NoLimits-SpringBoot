package com.example.NoLimits.Multimedia.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.RegionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    public List<ComunaModel> findAll() {
        return comunaRepository.findAll();
    }

    public ComunaModel findById(Long id) {
        return comunaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Comuna no encontrada con ID: " + id));
    }

    public ComunaModel save(ComunaModel comuna) {
        if (comuna.getNombre() == null || comuna.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la comuna es obligatorio");
        }
        comuna.setNombre(comuna.getNombre().trim());

        if (comuna.getRegion() == null || comuna.getRegion().getId() == null) {
            throw new IllegalArgumentException("Debe especificar una región válida");
        }

        RegionModel region = regionRepository.findById(comuna.getRegion().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Región no encontrada"));
        comuna.setRegion(region);

        return comunaRepository.save(comuna);
    }

    public ComunaModel update(Long id, ComunaModel detalles) {
        ComunaModel existente = findById(id);

        if (detalles.getNombre() != null) {
            String nombre = detalles.getNombre().trim();
            if (nombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            existente.setNombre(nombre);
        }

        if (detalles.getRegion() != null && detalles.getRegion().getId() != null) {
            RegionModel region = regionRepository.findById(detalles.getRegion().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Región no encontrada"));
            existente.setRegion(region);
        }

        return comunaRepository.save(existente);
    }

    /** PATCH — Actualización parcial */
    public ComunaModel patch(Long id, ComunaModel parciales) {
        ComunaModel existente = findById(id);

        if (parciales.getNombre() != null) {
            String nombre = parciales.getNombre().trim();
            if (nombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            existente.setNombre(nombre);
        }

        if (parciales.getRegion() != null && parciales.getRegion().getId() != null) {
            RegionModel region = regionRepository.findById(parciales.getRegion().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Región no encontrada"));
            existente.setRegion(region);
        }

        return comunaRepository.save(existente);
    }

    public void deleteById(Long id) {
        // Verifica que exista
        findById(id);

        // Bloquea si hay direcciones asociadas
        if (direccionRepository.existsByComuna_Id(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar: la comuna tiene direcciones asociadas."
            );
        }

        comunaRepository.deleteById(id);
    }
}