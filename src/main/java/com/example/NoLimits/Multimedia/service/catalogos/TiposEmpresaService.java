package com.example.NoLimits.Multimedia.service.catalogos;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TiposEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TiposEmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TiposEmpresaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposEmpresaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TiposEmpresaService {

    @Autowired
    private TiposEmpresaRepository tiposEmpresaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TipoEmpresaRepository tipoEmpresaRepository;

    public List<TiposEmpresaResponseDTO> findAllByEmpresa(Long empresaId) {
        return tiposEmpresaRepository.findAll().stream()
                .filter(rel -> rel.getEmpresa() != null
                        && rel.getEmpresa().getId().equals(empresaId))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TiposEmpresaResponseDTO link(Long empresaId, Long tipoId) {
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

        return toResponseDTO(tiposEmpresaRepository.save(link));
    }

    public void unlink(Long empresaId, Long tipoId) {
        TiposEmpresaModel link = tiposEmpresaRepository
                .findByEmpresa_IdAndTipoEmpresa_Id(empresaId, tipoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación no encontrada"));

        tiposEmpresaRepository.delete(link);
    }

    public TiposEmpresaResponseDTO patch(Long relacionId, TiposEmpresaUpdateDTO dto) {

        TiposEmpresaModel rel = tiposEmpresaRepository.findById(relacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación no encontrada con ID: " + relacionId));

        if (dto.getEmpresaId() != null) {
            EmpresaModel nuevaEmpresa = empresaRepository.findById(dto.getEmpresaId())
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Empresa no encontrada: " + dto.getEmpresaId()));

            if (tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(
                    dto.getEmpresaId(), rel.getTipoEmpresa().getId())) {
                throw new IllegalArgumentException("Ya existe una relación con esa empresa y ese tipo");
            }

            rel.setEmpresa(nuevaEmpresa);
        }

        if (dto.getTipoEmpresaId() != null) {
            TipoEmpresaModel nuevoTipo = tipoEmpresaRepository.findById(dto.getTipoEmpresaId())
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Tipo de empresa no encontrado: " + dto.getTipoEmpresaId()));

            if (tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(
                    rel.getEmpresa().getId(), dto.getTipoEmpresaId())) {
                throw new IllegalArgumentException("Ya existe una relación con ese tipo y esa empresa");
            }

            rel.setTipoEmpresa(nuevoTipo);
        }

        return toResponseDTO(tiposEmpresaRepository.save(rel));
    }

    private TiposEmpresaResponseDTO toResponseDTO(TiposEmpresaModel model) {
        TiposEmpresaResponseDTO dto = new TiposEmpresaResponseDTO();
        dto.setId(model.getId());
        dto.setEmpresaId(
                model.getEmpresa() != null ? model.getEmpresa().getId() : null
        );
        dto.setTipoEmpresaId(
                model.getTipoEmpresa() != null ? model.getTipoEmpresa().getId() : null
        );
        dto.setTipoEmpresaNombre(
                model.getTipoEmpresa() != null ? model.getTipoEmpresa().getNombre() : null
        );
        return dto;
    }
}