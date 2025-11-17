// Ruta: src/main/java/com/example/NoLimits/Multimedia/service/EmpresaService.java
package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.EmpresaModel;
import com.example.NoLimits.Multimedia.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<EmpresaModel> findAll() {
        return empresaRepository.findAll();
    }

    public EmpresaModel findById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empresa no encontrada con ID: " + id));
    }

    public EmpresaModel save(EmpresaModel e) {
        if (e.getNombre() == null || e.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la empresa es obligatorio");
        }

        String nombre = e.getNombre().trim();
        if (empresaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe una empresa con ese nombre");
        }

        e.setNombre(nombre);
        return empresaRepository.save(e);
    }

    public EmpresaModel update(Long id, EmpresaModel in) {
        EmpresaModel e = findById(id);

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }

            if (!nuevo.equalsIgnoreCase(e.getNombre())
                    && empresaRepository.existsByNombreIgnoreCase(nuevo)) {
                throw new IllegalArgumentException("Ya existe una empresa con ese nombre");
            }

            e.setNombre(nuevo);
        }

        return empresaRepository.save(e);
    }

    public void deleteById(Long id) {
        // 404 si no existe
        findById(id);
        empresaRepository.deleteById(id);
    }
}