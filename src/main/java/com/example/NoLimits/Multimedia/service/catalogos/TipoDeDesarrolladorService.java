package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposDeDesarrolladorRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TipoDeDesarrolladorService {

    @Autowired
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;

    @Autowired
    private TiposDeDesarrolladorRepository tiposDeDesarrolladorRepository;

    public List<TipoDeDesarrolladorModel> findAll() {
        return tipoDeDesarrolladorRepository.findAll();
    }

    public TipoDeDesarrolladorModel findById(Long id) {
        return tipoDeDesarrolladorRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de desarrollador no encontrado: " + id));
    }

    public TipoDeDesarrolladorModel save(TipoDeDesarrolladorModel t) {
        if (t.getNombre() == null || t.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        String normalizado = t.getNombre().trim();
        if (tipoDeDesarrolladorRepository.existsByNombreIgnoreCase(normalizado)) {
            throw new IllegalArgumentException("Ya existe un tipo de desarrollador con ese nombre");
        }

        t.setNombre(normalizado);
        return tipoDeDesarrolladorRepository.save(t);
    }

    public TipoDeDesarrolladorModel update(Long id, TipoDeDesarrolladorModel in) {
        TipoDeDesarrolladorModel t = findById(id);

        if (in.getNombre() != null) {
            String v = in.getNombre().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (!v.equalsIgnoreCase(t.getNombre())
                    && tipoDeDesarrolladorRepository.existsByNombreIgnoreCase(v)) {
                throw new IllegalArgumentException("Ya existe un tipo de desarrollador con ese nombre");
            }
            t.setNombre(v);
        }

        return tipoDeDesarrolladorRepository.save(t);
    }

    public TipoDeDesarrolladorModel patch(Long id, TipoDeDesarrolladorModel in) {
        TipoDeDesarrolladorModel t = findById(id);

        if (in.getNombre() != null) {
            String v = in.getNombre().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (!v.equalsIgnoreCase(t.getNombre())
                    && tipoDeDesarrolladorRepository.existsByNombreIgnoreCase(v)) {
                throw new IllegalArgumentException("Ya existe un tipo de desarrollador con ese nombre");
            }
            t.setNombre(v);
        }

        return tipoDeDesarrolladorRepository.save(t);
    }

    public void deleteById(Long id) {
        findById(id);
        if (tiposDeDesarrolladorRepository.existsByTipoDeDesarrollador_Id(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar: hay relaciones en tipos_de_desarrollador.");
        }
        tipoDeDesarrolladorRepository.deleteById(id);
    }
}