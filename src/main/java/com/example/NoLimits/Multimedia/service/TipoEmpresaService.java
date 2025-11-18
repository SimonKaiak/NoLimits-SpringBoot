package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.repository.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.TiposEmpresaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TipoEmpresaService {

    @Autowired
    private TipoEmpresaRepository tipoEmpresaRepository;

    @Autowired
    private TiposEmpresaRepository tiposEmpresaRepository;

    public List<TipoEmpresaModel> findAll() {
        return tipoEmpresaRepository.findAll();
    }

    public TipoEmpresaModel findById(Long id) {
        return tipoEmpresaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de empresa no encontrado: " + id));
    }

    public TipoEmpresaModel save(TipoEmpresaModel t) {
        if (t.getNombre() == null || t.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        String nombre = t.getNombre().trim();
        if (tipoEmpresaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe un tipo de empresa con ese nombre");
        }

        t.setNombre(nombre);
        return tipoEmpresaRepository.save(t);
    }

    public TipoEmpresaModel update(Long id, TipoEmpresaModel in) {
        TipoEmpresaModel t = findById(id);

        if (in.getNombre() != null) {
            String v = in.getNombre().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (!v.equalsIgnoreCase(t.getNombre())
                    && tipoEmpresaRepository.existsByNombreIgnoreCase(v)) {
                throw new IllegalArgumentException("Ya existe un tipo de empresa con ese nombre");
            }
            t.setNombre(v);
        }

        return tipoEmpresaRepository.save(t);
    }

    // PATCH: actualización parcial
    public TipoEmpresaModel patch(Long id, TipoEmpresaModel in) {
        TipoEmpresaModel t = findById(id);

        if (in.getNombre() != null) {
            String v = in.getNombre().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (!v.equalsIgnoreCase(t.getNombre())
                    && tipoEmpresaRepository.existsByNombreIgnoreCase(v)) {
                throw new IllegalArgumentException("Ya existe un tipo de empresa con ese nombre");
            }
            t.setNombre(v);
        }

        return tipoEmpresaRepository.save(t);
    }

    public void deleteById(Long id) {
        findById(id);

        if (tiposEmpresaRepository.existsByTipoEmpresa_Id(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar: hay relaciones en tipos_empresa.");
        }

        tipoEmpresaRepository.deleteById(id);
    }
}