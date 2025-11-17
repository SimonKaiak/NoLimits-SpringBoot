// Ruta: src/main/java/com/example/NoLimits/Multimedia/service/TiposEmpresaService.java
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

    public TiposEmpresaModel link(Long empresaId, Long tipoId) {
        EmpresaModel emp = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empresa no encontrada: " + empresaId));
        TipoEmpresaModel tipo = tipoEmpresaRepository.findById(tipoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de empresa no encontrado: " + tipoId));

        if (tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(empresaId, tipoId)) {
            throw new IllegalStateException("La relación ya existe");
        }

        TiposEmpresaModel link = new TiposEmpresaModel();
        link.setEmpresa(emp);
        link.setTipoEmpresa(tipo);
        return tiposEmpresaRepository.save(link);
    }

    public void unlink(Long empresaId, Long tipoId) {
        TiposEmpresaModel link = tiposEmpresaRepository
                .findByEmpresa_IdAndTipoEmpresa_Id(empresaId, tipoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Relación no encontrada"));
        tiposEmpresaRepository.delete(link);
    }
}