package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.EmpresaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    // ================== HELPERS INTERNOS ==================

    /**
     * Busca la entidad Empresa por ID o lanza excepción si no existe.
     */
    private EmpresaModel findEntityById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Empresa no encontrada con ID: " + id));
    }

    /**
     * Convierte una entidad EmpresaModel a un EmpresaResponseDTO.
     */
    private EmpresaResponseDTO toResponseDTO(EmpresaModel e) {
        EmpresaResponseDTO dto = new EmpresaResponseDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setActivo(e.getActivo());
        return dto;
    }

    // ================== LECTURAS (GET) ==================

    public List<EmpresaResponseDTO> findAll() {
        List<EmpresaModel> entidades = empresaRepository.findAll();
        return entidades.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EmpresaResponseDTO findById(Long id) {
        EmpresaModel e = findEntityById(id);
        return toResponseDTO(e);
    }

    // ================== CREAR (POST) ==================

    /**
     * Crea una nueva empresa a partir de un EmpresaRequestDTO.
     */
    public EmpresaResponseDTO create(EmpresaRequestDTO request) {

        // Validación de nombre obligatorio
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la empresa es obligatorio");
        }

        String nombre = request.getNombre().trim();

        // Validar nombre único (ignora mayúsculas/minúsculas)
        if (empresaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe una empresa con ese nombre");
        }

        // Mapear DTO → entidad
        EmpresaModel entidad = new EmpresaModel();
        entidad.setNombre(nombre);

        // Si el DTO maneja 'activo' se respeta, si no, se deja el valor por defecto de la entidad
        if (request.getActivo() != null) {
            entidad.setActivo(request.getActivo());
        }

        EmpresaModel guardada = empresaRepository.save(entidad);
        return toResponseDTO(guardada);
    }

    // ================== ACTUALIZAR COMPLETO (PUT) ==================

    /**
     * Actualización completa de una empresa usando EmpresaRequestDTO
     * (en tu patrón, RequestDTO se usa para POST y PUT).
     */
    public EmpresaResponseDTO update(Long id, EmpresaRequestDTO in) {
        EmpresaModel e = findEntityById(id);

        // Nombre obligatorio en PUT
        if (in.getNombre() == null || in.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        String nuevo = in.getNombre().trim();

        if (!nuevo.equalsIgnoreCase(e.getNombre())
                && empresaRepository.existsByNombreIgnoreCase(nuevo)) {
            throw new IllegalArgumentException("Ya existe una empresa con ese nombre");
        }

        e.setNombre(nuevo);

        // En PUT normalmente también se envía el estado 'activo' explícito
        if (in.getActivo() != null) {
            e.setActivo(in.getActivo());
        }

        EmpresaModel actualizada = empresaRepository.save(e);
        return toResponseDTO(actualizada);
    }

    // ================== ACTUALIZACIÓN PARCIAL (PATCH) ==================

    /**
     * Actualización parcial de una empresa usando EmpresaUpdateDTO.
     * Aquí todos los campos son opcionales.
     */
    public EmpresaResponseDTO patch(Long id, EmpresaUpdateDTO in) {
        EmpresaModel e = findEntityById(id);

        // Nombre (opcional en PATCH)
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

        // Activo (opcional en PATCH)
        if (in.getActivo() != null) {
            e.setActivo(in.getActivo());
        }

        EmpresaModel actualizada = empresaRepository.save(e);
        return toResponseDTO(actualizada);
    }

    // ================== ELIMINAR (DELETE) ==================

    public void deleteById(Long id) {
        // Verificar existencia primero
        findEntityById(id);
        empresaRepository.deleteById(id);
    }
}