package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoEnvioRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoEnvioResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoEnvioUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoEnvioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MetodoEnvioService {

    @Autowired
    private MetodoEnvioRepository metodoEnvioRepository;

    // ================== HELPERS ==================

    private MetodoEnvioModel findEntityById(Long id) {
        return metodoEnvioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Método de envío no encontrado con ID: " + id));
    }

    private MetodoEnvioResponseDTO toResponseDTO(MetodoEnvioModel m) {
        MetodoEnvioResponseDTO dto = new MetodoEnvioResponseDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setDescripcion(m.getDescripcion());
        dto.setActivo(m.getActivo());
        return dto;
    }

    // ================== LECTURAS ==================

    public List<MetodoEnvioResponseDTO> findAll() {
        return metodoEnvioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MetodoEnvioResponseDTO findById(Long id) {
        return toResponseDTO(findEntityById(id));
    }

    // ================== PAGINACIÓN REAL ==================

    public PagedResponse<MetodoEnvioResponseDTO> findAllPaged(int page, int size, String search) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

    Page<MetodoEnvioModel> result;

    if (search == null || search.trim().isEmpty()) {
        result = metodoEnvioRepository.findAll(pageable);
    } else {
        result = metodoEnvioRepository.findByNombreContainingIgnoreCase(search.trim(), pageable);
    }

    List<MetodoEnvioResponseDTO> contenido =
            result.getContent().stream().map(this::toResponseDTO).toList();

    return new PagedResponse<>(
            contenido,
            page,
            result.getTotalPages(),
            result.getTotalElements()
        );
    }

    // ================== CREAR ==================

    public MetodoEnvioResponseDTO create(MetodoEnvioRequestDTO in) {

        if (in.getNombre() == null || in.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del método de envío es obligatorio");
        }

        MetodoEnvioModel m = new MetodoEnvioModel();
        m.setNombre(in.getNombre().trim());
        m.setDescripcion(in.getDescripcion());

        // si no viene activo en el request, queda true por defecto
        if (in.getActivo() != null) {
            m.setActivo(in.getActivo());
        } else {
            m.setActivo(true);
        }

        MetodoEnvioModel guardado = metodoEnvioRepository.save(m);
        return toResponseDTO(guardado);
    }

    // ================== UPDATE (PUT) ==================

    public MetodoEnvioResponseDTO update(Long id, MetodoEnvioRequestDTO in) {
        MetodoEnvioModel m = findEntityById(id);

        if (in.getNombre() == null || in.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        m.setNombre(in.getNombre().trim());
        m.setDescripcion(in.getDescripcion());

        if (in.getActivo() != null) {
            m.setActivo(in.getActivo());
        }

        MetodoEnvioModel actualizado = metodoEnvioRepository.save(m);
        return toResponseDTO(actualizado);
    }

    // ================== PATCH ==================

    public MetodoEnvioResponseDTO patch(Long id, MetodoEnvioUpdateDTO in) {
        MetodoEnvioModel m = findEntityById(id);

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

        MetodoEnvioModel actualizado = metodoEnvioRepository.save(m);
        return toResponseDTO(actualizado);
    }

    // ================== DELETE ==================

    public void deleteById(Long id) {
        findEntityById(id);
        metodoEnvioRepository.deleteById(id);
    }
}