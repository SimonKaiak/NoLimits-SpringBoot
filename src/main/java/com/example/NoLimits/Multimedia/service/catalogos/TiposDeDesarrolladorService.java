package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
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

    public List<TiposDeDesarrolladorModel> findAll() {
        return tiposDeDesarrolladorRepository.findAll();
    }

    public List<TiposDeDesarrolladorModel> findByDesarrollador(Long desarrolladorId) {
        return tiposDeDesarrolladorRepository.findByDesarrollador_Id(desarrolladorId);
    }

    public List<TiposDeDesarrolladorModel> findByTipo(Long tipoId) {
        return tiposDeDesarrolladorRepository.findByTipoDeDesarrollador_Id(tipoId);
    }

    /** Crea el vínculo Desarrollador ↔ TipoDeDesarrollador (idempotente). */
    public TiposDeDesarrolladorModel link(Long desarrolladorId, Long tipoId) {
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

        return tiposDeDesarrolladorRepository
                .findByDesarrollador_IdAndTipoDeDesarrollador_Id(desarrolladorId, tipoId)
                .orElseGet(() -> {
                    TiposDeDesarrolladorModel link = new TiposDeDesarrolladorModel();
                    link.setDesarrollador(dev);
                    link.setTipoDeDesarrollador(tipo);
                    return tiposDeDesarrolladorRepository.save(link);
                });
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
    public TiposDeDesarrolladorModel patch(Long relacionId, Long nuevoDesarrolladorId, Long nuevoTipoId) {

        TiposDeDesarrolladorModel rel = tiposDeDesarrolladorRepository.findById(relacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación no encontrada: " + relacionId));

        if (nuevoDesarrolladorId != null) {
            DesarrolladorModel nuevoDev = desarrolladorRepository.findById(nuevoDesarrolladorId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Desarrollador no encontrado: " + nuevoDesarrolladorId));

            if (tiposDeDesarrolladorRepository.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(
                    nuevoDesarrolladorId, rel.getTipoDeDesarrollador().getId())) {
                throw new IllegalArgumentException("Ya existe esa relación con el nuevo desarrollador");
            }

            rel.setDesarrollador(nuevoDev);
        }

        if (nuevoTipoId != null) {
            TipoDeDesarrolladorModel nuevoTipo = tipoDeDesarrolladorRepository.findById(nuevoTipoId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Tipo de desarrollador no encontrado: " + nuevoTipoId));

            if (tiposDeDesarrolladorRepository.existsByDesarrollador_IdAndTipoDeDesarrollador_Id(
                    rel.getDesarrollador().getId(), nuevoTipoId)) {
                throw new IllegalArgumentException("Ya existe esa relación con el nuevo tipo");
            }

            rel.setTipoDeDesarrollador(nuevoTipo);
        }

        return tiposDeDesarrolladorRepository.save(rel);
    }
}