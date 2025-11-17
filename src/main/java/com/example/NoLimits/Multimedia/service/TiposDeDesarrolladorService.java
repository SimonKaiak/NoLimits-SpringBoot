package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.TiposDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.model.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.TiposDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.TipoDeDesarrolladorRepository;
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

        // Si ya existe, devolvemos la misma relación
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
}