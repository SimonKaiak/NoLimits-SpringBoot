package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.repository.MetodoEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MetodoEnvioService {

    @Autowired
    private MetodoEnvioRepository metodoEnvioRepository;

    public List<MetodoEnvioModel> findAll() {
        return metodoEnvioRepository.findAll();
    }

    public MetodoEnvioModel findById(Long id) {
        return metodoEnvioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Método de envío no encontrado con ID: " + id));
    }

    public MetodoEnvioModel save(MetodoEnvioModel m) {
        if (m.getNombre() == null || m.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El nombre del método de envío es obligatorio");
        m.setNombre(m.getNombre().trim());
        return metodoEnvioRepository.save(m);
    }

    public MetodoEnvioModel update(Long id, MetodoEnvioModel in) {
        MetodoEnvioModel m = findById(id);
        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío");
            m.setNombre(nuevo);
        }
        if (in.getDescripcion() != null) m.setDescripcion(in.getDescripcion());
        return metodoEnvioRepository.save(m);
    }

    public void deleteById(Long id) {
        findById(id);
        metodoEnvioRepository.deleteById(id);
    }
}