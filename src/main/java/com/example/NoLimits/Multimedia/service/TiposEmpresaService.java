package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.EmpresaModel;
import com.example.NoLimits.Multimedia.model.TiposEmpresaModel;
import com.example.NoLimits.Multimedia.model.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.repository.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.TiposEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.TipoEmpresaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TiposEmpresaService {

    @Autowired private TiposEmpresaRepository tiposEmpresaRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private TipoEmpresaRepository tipoEmpresaRepository;

    public List<TiposEmpresaModel> findAll() {
        return tiposEmpresaRepository.findAll();
    }

    /** Crea el vínculo Empresa ↔ TipoEmpresa */
    public TiposEmpresaModel link(Long empresaId, Long tipoId) {
        EmpresaModel emp = empresaRepository.findById(empresaId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Empresa no encontrada: " + empresaId));

        TipoEmpresaModel tipo = tipoEmpresaRepository.findById(tipoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de empresa no encontrado: " + tipoId));

        if (tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(empresaId, tipoId)) {
            throw new IllegalStateException("La relación ya existe");
        }

        TiposEmpresaModel link = new TiposEmpresaModel();
        link.setEmpresa(emp);
        link.setTipoEmpresa(tipo);

        return tiposEmpresaRepository.save(link);
    }

    /** Elimina el vínculo */
    public void unlink(Long empresaId, Long tipoId) {
        TiposEmpresaModel link = tiposEmpresaRepository
                .findByEmpresa_IdAndTipoEmpresa_Id(empresaId, tipoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación no encontrada"));

        tiposEmpresaRepository.delete(link);
    }

    /**
     * PATCH: Actualiza parcialmente la relación Empresa–TipoEmpresa.
     * Permite cambiar la empresa asociada o el tipo asociado.
     */
    public TiposEmpresaModel patch(Long relacionId, Long nuevaEmpresaId, Long nuevoTipoId) {

        TiposEmpresaModel rel = tiposEmpresaRepository.findById(relacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación no encontrada con ID: " + relacionId));

        if (nuevaEmpresaId != null) {
            EmpresaModel nuevaEmpresa = empresaRepository.findById(nuevaEmpresaId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Empresa no encontrada: " + nuevaEmpresaId));

            if (tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(
                    nuevaEmpresaId, rel.getTipoEmpresa().getId())) {
                throw new IllegalArgumentException("Ya existe una relación con esa empresa y ese tipo");
            }

            rel.setEmpresa(nuevaEmpresa);
        }

        if (nuevoTipoId != null) {
            TipoEmpresaModel nuevoTipo = tipoEmpresaRepository.findById(nuevoTipoId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Tipo de empresa no encontrado: " + nuevoTipoId));

            if (tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(
                    rel.getEmpresa().getId(), nuevoTipoId)) {
                throw new IllegalArgumentException("Ya existe una relación con ese tipo y esa empresa");
            }

            rel.setTipoEmpresa(nuevoTipo);
        }

        return tiposEmpresaRepository.save(rel);
    }
}