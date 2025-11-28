package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoEnvioRepository;

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
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Método de envío no encontrado con ID: " + id));
    }

    public MetodoEnvioModel save(MetodoEnvioModel m) {
        if (m.getNombre() == null || m.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del método de envío es obligatorio");
        }

        m.setNombre(m.getNombre().trim());
        if (m.getActivo() == null) {
            m.setActivo(true);
        }
        return metodoEnvioRepository.save(m);
    }

    public MetodoEnvioModel update(Long id, MetodoEnvioModel in) {
        MetodoEnvioModel m = findById(id);

        if (in.getNombre() == null || in.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        m.setNombre(in.getNombre().trim());
        m.setDescripcion(in.getDescripcion());
        if (in.getActivo() != null) {
            m.setActivo(in.getActivo());
        }

        return metodoEnvioRepository.save(m);
    }

    public MetodoEnvioModel patch(Long id, MetodoEnvioModel in) {
        MetodoEnvioModel m = findById(id);

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            m.setNombre(nuevo);
        }

        if (in.getDescripcion() != null) {
            m.setDescripcion(in.getDescripcion());
        }

        if (in.getActivo() != null) {
            m.setActivo(in.getActivo());
        }

        return metodoEnvioRepository.save(m);
    }


    public void deleteById(Long id) {
        findById(id);
        metodoEnvioRepository.deleteById(id);
    }
}