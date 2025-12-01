package com.example.NoLimits.Multimedia.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.ComunaRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.ComunaUpdateDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.RegionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    // ===============================================================
    // MÉTODOS PÚBLICOS EXPONIENDO DTOs
    // ===============================================================

    public List<ComunaResponseDTO> findAll() {
        return comunaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ComunaResponseDTO findById(Long id) {
        ComunaModel comuna = findEntityById(id);
        return toResponseDTO(comuna);
    }

    /**
     * CREATE – usa ComunaRequestDTO (sin ID).
     */
    public ComunaResponseDTO create(ComunaRequestDTO requestDTO) {

        // Validar nombre
        if (requestDTO.getNombre() == null || requestDTO.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la comuna es obligatorio");
        }
        String nombre = requestDTO.getNombre().trim();

        // Validar región
        if (requestDTO.getRegionId() == null) {
            throw new IllegalArgumentException("Debe especificar una región válida");
        }

        RegionModel region = regionRepository.findById(requestDTO.getRegionId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Región no encontrada"));

        ComunaModel comuna = new ComunaModel();
        comuna.setNombre(nombre);
        comuna.setRegion(region);

        ComunaModel guardada = comunaRepository.save(comuna);
        return toResponseDTO(guardada);
    }

    /**
     * UPDATE (PUT completo) – usa ComunaUpdateDTO.
     * Para un PUT exigimos que venga nombre y regionId no nulos.
     */
    public ComunaResponseDTO update(Long id, ComunaUpdateDTO updateDTO) {
        ComunaModel existente = findEntityById(id);

        // 1) Nombre nulo → obligatorio
        if (updateDTO.getNombre() == null) {
            throw new IllegalArgumentException("El nombre de la comuna es obligatorio");
        }

        // 2) Nombre vacío o solo espacios → no puede estar vacío
        String nombre = updateDTO.getNombre().trim();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        // Validar región
        if (updateDTO.getRegionId() == null) {
            throw new IllegalArgumentException("Debe especificar una región válida");
        }

        RegionModel region = regionRepository.findById(updateDTO.getRegionId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Región no encontrada"));

        existente.setNombre(nombre);
        existente.setRegion(region);

        ComunaModel actualizada = comunaRepository.save(existente);
        return toResponseDTO(actualizada);
    }

    /**
     * PATCH — Actualización parcial.
     * Solo se modifican los campos que vengan no nulos.
     */
    public ComunaResponseDTO patch(Long id, ComunaUpdateDTO parciales) {

        ComunaModel existente = findEntityById(id);

        // PATCH nombre
        if (parciales.getNombre() != null) {
            String nombre = parciales.getNombre().trim();
            if (nombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            existente.setNombre(nombre);
        }

        // PATCH región
        if (parciales.getRegionId() != null) {
            RegionModel region = regionRepository.findById(parciales.getRegionId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Región no encontrada"));
            existente.setRegion(region);
        }

        ComunaModel actualizada = comunaRepository.save(existente);
        return toResponseDTO(actualizada);
    }

    public void deleteById(Long id) {
        // Verifica que exista (lanzará RecursoNoEncontradoException si no)
        findEntityById(id);

        // Bloquea si hay direcciones asociadas
        if (direccionRepository.existsByComuna_Id(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar: la comuna tiene direcciones asociadas."
            );
        }

        comunaRepository.deleteById(id);
    }

    // ===============================================================
    // MÉTODOS PRIVADOS DE APOYO (ENTIDAD + MAPEOS)
    // ===============================================================

    private ComunaModel findEntityById(Long id) {
        return comunaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Comuna no encontrada con ID: " + id));
    }

    private ComunaResponseDTO toResponseDTO(ComunaModel entity) {
        ComunaResponseDTO dto = new ComunaResponseDTO();

        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setRegionId(
                entity.getRegion() != null ? entity.getRegion().getId() : null
        );
        dto.setRegionNombre(
                entity.getRegion() != null ? entity.getRegion().getNombre() : null
        );

        return dto;
    }
}