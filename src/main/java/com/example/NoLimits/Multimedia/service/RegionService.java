package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.RegionModel;
import com.example.NoLimits.Multimedia.repository.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.RegionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    public List<RegionModel> findAll() {
        return regionRepository.findAll();
    }

    public RegionModel findById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Región no encontrada con ID: " + id));
    }

    public RegionModel save(RegionModel r) {
        if (r.getNombre() == null || r.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la región es obligatorio");
        }

        r.setNombre(r.getNombre().trim());
        return regionRepository.save(r);
    }

    public RegionModel update(Long id, RegionModel in) {
        RegionModel r = findById(id);

        if (in.getNombre() != null) {
            String v = in.getNombre().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            r.setNombre(v);
        }

        return regionRepository.save(r);
    }

    /* ======================== PATCH ======================== */

    public RegionModel patch(Long id, RegionModel in) {
        RegionModel r = findById(id);

        if (in.getNombre() != null) {
            String v = in.getNombre().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            r.setNombre(v);
        }

        return regionRepository.save(r);
    }

    public void deleteById(Long id) {
        // Verifica que exista
        findById(id);

        // Bloquea si tiene comunas asociadas
        if (comunaRepository.existsByRegion_Id(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar: la región tiene comunas asociadas."
            );
        }

        regionRepository.deleteById(id);
    }
}