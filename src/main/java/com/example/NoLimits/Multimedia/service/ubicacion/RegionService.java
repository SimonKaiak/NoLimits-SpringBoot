package com.example.NoLimits.Multimedia.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.RegionRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.RegionResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.RegionUpdateDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.RegionRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    /* ======================== BÁSICOS ======================== */

    public List<RegionResponseDTO> findAll() {
        return regionRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RegionResponseDTO findById(Long id) {
        RegionModel region = getRegionOrThrow(id);
        return toResponseDTO(region);
    }

    /* ======================== CREAR ======================== */

    public RegionResponseDTO save(RegionRequestDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la región es obligatorio");
        }

        RegionModel r = new RegionModel();
        r.setNombre(dto.getNombre().trim());

        RegionModel guardada = regionRepository.save(r);
        return toResponseDTO(guardada);
    }

    /* ======================== ACTUALIZAR (PUT) ======================== */

    public RegionResponseDTO update(Long id, RegionUpdateDTO in) {
        RegionModel r = getRegionOrThrow(id);
        aplicarCambiosDesdeUpdateDTO(in, r);
        RegionModel actualizada = regionRepository.save(r);
        return toResponseDTO(actualizada);
    }

    /* ======================== PATCH ======================== */

    public RegionResponseDTO patch(Long id, RegionUpdateDTO in) {
        RegionModel r = getRegionOrThrow(id);
        aplicarCambiosDesdeUpdateDTO(in, r);
        RegionModel actualizada = regionRepository.save(r);
        return toResponseDTO(actualizada);
    }

    /* ======================== ELIMINAR ======================== */

    public void deleteById(Long id) {
        // Verifica que exista
        getRegionOrThrow(id);

        // Bloquea si tiene comunas asociadas
        if (comunaRepository.existsByRegion_Id(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar: la región tiene comunas asociadas."
            );
        }

        regionRepository.deleteById(id);
    }

    /* ======================== PRIVADOS ======================== */

    private RegionModel getRegionOrThrow(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Región no encontrada con ID: " + id));
    }

    private RegionResponseDTO toResponseDTO(RegionModel entity) {
        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        return dto;
    }

    private void aplicarCambiosDesdeUpdateDTO(RegionUpdateDTO in, RegionModel r) {
        if (in.getNombre() != null) {
            String v = in.getNombre().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            r.setNombre(v);
        }
    }
}