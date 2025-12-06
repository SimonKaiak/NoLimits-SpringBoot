package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.PlataformaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.PlataformaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.PlataformaUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PlataformaService {

    @Autowired
    private PlataformaRepository plataformaRepository;

    public List<PlataformaResponseDTO> findAll() {
        return plataformaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PlataformaResponseDTO findById(Long id) {
        PlataformaModel model = plataformaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Plataforma no encontrada con ID: " + id));
        return toResponseDTO(model);
    }

    public PlataformaResponseDTO save(PlataformaRequestDTO p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la plataforma es obligatorio");
        }

        String nombre = p.getNombre().trim();

        PlataformaModel model = new PlataformaModel();
        model.setNombre(nombre);

        PlataformaModel guardada = plataformaRepository.save(model);
        return toResponseDTO(guardada);
    }

    public PlataformaResponseDTO update(Long id, PlataformaUpdateDTO in) {
        PlataformaModel p = plataformaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Plataforma no encontrada con ID: " + id));

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            p.setNombre(nuevo);
        }

        PlataformaModel actualizada = plataformaRepository.save(p);
        return toResponseDTO(actualizada);
    }

    public PlataformaResponseDTO patch(Long id, PlataformaUpdateDTO in) {
        // mismo comportamiento que update, separado por semántica
        return update(id, in);
    }

    public void deleteById(Long id) {
        plataformaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Plataforma no encontrada con ID: " + id));
        plataformaRepository.deleteById(id);
    }

    // ========== HELPERS ==========

    private PlataformaResponseDTO toResponseDTO(PlataformaModel model) {
        PlataformaResponseDTO dto = new PlataformaResponseDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        return dto;
    }

    // ========== PAGINACIÓN ==========

    public PagedResponse<PlataformaResponseDTO> listarPaginado(int page, int size, String search) {

    Pageable pageable = PageRequest.of(page - 1, size);
    Page<PlataformaModel> pagina;

    if (search != null && !search.isBlank()) {
        pagina = plataformaRepository.findByNombreContainingIgnoreCase(search.trim(), pageable);
    } else {
        pagina = plataformaRepository.findAll(pageable);
    }

    List<PlataformaResponseDTO> contenido = pagina.getContent()
            .stream()
            .map(this::toResponseDTO)
            .toList();

    return new PagedResponse<>(
            contenido,
            pagina.getNumber() + 1,
            pagina.getTotalPages(),
            pagina.getTotalElements()
        );
    }
}