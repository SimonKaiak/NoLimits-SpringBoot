package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.RolModel;
import com.example.NoLimits.Multimedia.repository.RolRepository;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RolService {

    @Autowired private RolRepository rolRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<RolModel> findAll() { return rolRepository.findAll(); }

    public RolModel findById(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con ID: " + id));
    }

    public RolModel save(RolModel r) {
        if (r.getNombre() == null || r.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        r.setNombre(r.getNombre().trim());
        return rolRepository.save(r);
    }

    public RolModel update(Long id, RolModel in) {
        RolModel r = findById(id);
        if (in.getNombre() != null) {
            String v = in.getNombre().trim();
            if (v.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío");
            r.setNombre(v);
        }
        return rolRepository.save(r);
    }

    public void deleteById(Long id) {
        findById(id);
        if (usuarioRepository.existsByRol_Id(id))
            throw new IllegalStateException("No se puede eliminar: hay usuarios con este rol.");
        rolRepository.deleteById(id);
    }
}