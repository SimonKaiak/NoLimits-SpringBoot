package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposDeDesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TiposDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposDeDesarrolladorRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TiposDeDesarrolladorService {

    @Autowired
    private TiposDeDesarrolladorRepository tiposDeDesarrolladorRepository;

    @Autowired
    private DesarrolladorRepository desarrolladorRepository;

    @Autowired
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;

    // ========= LISTAR POR DESARROLLADOR =========
    public List<TiposDeDesarrolladorResponseDTO> findByDesarrollador(Long desarrolladorId) {
        return tiposDeDesarrolladorRepository.findByDesarrollador_Id(desarrolladorId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // ========= LISTAR POR TIPO =========
    public List<TiposDeDesarrolladorResponseDTO> findByTipo(Long tipoId) {
        return tiposDeDesarrolladorRepository.findByTipoDeDesarrollador_Id(tipoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /** Crea el vínculo Desarrollador ↔ TipoDeDesarrollador (idempotente a nivel de BD). */
    public TiposDeDesarrolladorResponseDTO link(Long desarrolladorId, Long tipoId) {
        DesarrolladorModel dev = desarrolladorRepository.findById(desarrolladorId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Desarrollador no encontrado: " + desarrolladorId));

        TipoDeDesarrolladorModel tipo = tipoDeDesarrolladorRepository.findById(tipoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de desarrollador no encontrado: " + tipoId));

        if (tiposDeDesarrolladorRepository
                .findByDesarrollador_IdAndTipoDeDesarrollador_Id(desarrolladorId, tipoId)
                .isPresent()) {
            throw new IllegalStateException("Ya existe esta relación");
        }

        TiposDeDesarrolladorModel link = new TiposDeDesarrolladorModel();
        link.setDesarrollador(dev);
        link.setTipoDeDesarrollador(tipo);

        TiposDeDesarrolladorModel guardado = tiposDeDesarrolladorRepository.save(link);
        return toResponseDTO(guardado);
    }

    /** Elimina el vínculo Desarrollador ↔ TipoDeDesarrollador. */
    public void unlink(Long desarrolladorId, Long tipoId) {
        TiposDeDesarrolladorModel link = tiposDeDesarrolladorRepository
                .findByDesarrollador_IdAndTipoDeDesarrollador_Id(desarrolladorId, tipoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Relación no encontrada"));

        tiposDeDesarrolladorRepository.delete(link);
    }

    /**
     * PATCH: Actualiza parcialmente la relación Desarrollador–TipoDeDesarrollador.
     * Permite cambiar el desarrollador o el tipo asociado.
     */
    public TiposDeDesarrolladorResponseDTO patch(Long relacionId, TiposDeDesarrolladorUpdateDTO dto) {

        TiposDeDesarrolladorModel rel = tiposDeDesarrolladorRepository.findById(relacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación no encontrada: " + relacionId));

        Long nuevoDesarrolladorId = dto.getDesarrolladorId();
        Long nuevoTipoId = dto.getTipoDeDesarrolladorId();

        // Cambiar DESARROLLADOR
        if (nuevoDesarrolladorId != null) {
            DesarrolladorModel nuevoDev = desarrolladorRepository.findById(nuevoDesarrolladorId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Desarrollador no encontrado: " + nuevoDesarrolladorId));

            if (tiposDeDesarrolladorRepository.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(
                    nuevoDesarrolladorId,
                    rel.getTipoDeDesarrollador().getId())) {
                throw new IllegalArgumentException("Ya existe esa relación con el nuevo desarrollador");
            }

            rel.setDesarrollador(nuevoDev);
        }

        // Cambiar TIPO
        if (nuevoTipoId != null) {
            TipoDeDesarrolladorModel nuevoTipo = tipoDeDesarrolladorRepository.findById(nuevoTipoId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Tipo de desarrollador no encontrado: " + nuevoTipoId));

            if (tiposDeDesarrolladorRepository.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(
                    rel.getDesarrollador().getId(),
                    nuevoTipoId)) {
                throw new IllegalArgumentException("Ya existe esa relación con el nuevo tipo");
            }

            rel.setTipoDeDesarrollador(nuevoTipo);
        }

        TiposDeDesarrolladorModel guardado = tiposDeDesarrolladorRepository.save(rel);
        return toResponseDTO(guardado);
    }

    // ========= MAPPER A RESPONSE DTO =========
    private TiposDeDesarrolladorResponseDTO toResponseDTO(TiposDeDesarrolladorModel entity) {
        TiposDeDesarrolladorResponseDTO dto = new TiposDeDesarrolladorResponseDTO();
        dto.setId(entity.getId());

        if (entity.getDesarrollador() != null) {
            dto.setDesarrolladorId(entity.getDesarrollador().getId());
        }
        if (entity.getTipoDeDesarrollador() != null) {
            dto.setTipoDeDesarrolladorId(entity.getTipoDeDesarrollador().getId());
        }

        return dto;
    }
}