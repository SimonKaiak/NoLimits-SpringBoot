package com.example.NoLimits.Multimedia.service.usuario;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.usuario.request.RolRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.RolUpdateDTO;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    // =========================================================
    // MÉTODOS PÚBLICOS EXPONIENDO DTOs
    // =========================================================

    public List<RolResponseDTO> findAll() {
        return rolRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RolResponseDTO findById(Long id) {
        RolModel rol = findEntityById(id);
        return toResponseDTO(rol);
    }

    /**
     * CREATE – usa RolRequestDTO (sin ID en el body).
     */
    public RolResponseDTO save(RolRequestDTO dto) {

        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }

        if (dto.getActivo() == null) {
            throw new IllegalArgumentException("El estado 'activo' del rol es obligatorio");
        }

        String nombre = dto.getNombre().trim();

        RolModel rol = new RolModel();
        rol.setNombre(nombre);
        rol.setDescripcion(dto.getDescripcion());
        rol.setActivo(dto.getActivo());

        RolModel guardado = rolRepository.save(rol);
        return toResponseDTO(guardado);
    }

    /**
     * UPDATE (PUT) – usa RolUpdateDTO.
     * Se actualizan solo los campos no nulos.
     */
    public RolResponseDTO update(Long id, RolUpdateDTO in) {
        RolModel rol = findEntityById(id);

        // nombre
        if (in.getNombre() != null) {
            String v = in.getNombre().trim();
            if (v.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            rol.setNombre(v);
        }

        // descripción
        if (in.getDescripcion() != null) {
            rol.setDescripcion(in.getDescripcion());
        }

        // activo
        if (in.getActivo() != null) {
            rol.setActivo(in.getActivo());
        }

        RolModel actualizado = rolRepository.save(rol);
        return toResponseDTO(actualizado);
    }

    /**
     * PATCH – actualización parcial.
     * Misma lógica que update: solo se aplican campos no nulos.
     */
    public RolResponseDTO patch(Long id, RolUpdateDTO in) {
        return update(id, in);
    }

    public void deleteById(Long id) {
        // Verifica que el rol exista
        findEntityById(id);

        // Bloquea si hay usuarios asociados a este rol
        if (rolRepository.existeUsuarioConRol(id)) {
            throw new IllegalStateException("No se puede eliminar: hay usuarios con este rol.");
        }

        rolRepository.deleteById(id);
    }

    // =========================================================
    // MÉTODOS PRIVADOS DE APOYO
    // =========================================================

    private RolModel findEntityById(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Rol no encontrado con ID: " + id));
    }

    private RolResponseDTO toResponseDTO(RolModel entity) {
        RolResponseDTO dto = new RolResponseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setActivo(entity.getActivo());
        return dto;
    }
}