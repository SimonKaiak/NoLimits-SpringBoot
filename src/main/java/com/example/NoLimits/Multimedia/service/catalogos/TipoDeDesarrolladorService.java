package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoDeDesarrolladorRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoDeDesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoDeDesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposDeDesarrolladorRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;


import java.util.List;

@Service
@Transactional
public class TipoDeDesarrolladorService {

    @Autowired
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;

    @Autowired
    private TiposDeDesarrolladorRepository tiposDeDesarrolladorRepository;

    public PagedResponse<TipoDeDesarrolladorResponseDTO> findAllPaged(int page, int size) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("nombre").ascending());

    Page<TipoDeDesarrolladorModel> resultado =
            tipoDeDesarrolladorRepository.findAll(pageable);

    List<TipoDeDesarrolladorResponseDTO> contenido =
            resultado.getContent().stream()
                    .map(this::toResponseDTO)
                    .toList();

    return new PagedResponse<>(
        contenido,
        resultado.getNumber() + 1,     // página actual
        resultado.getTotalPages(),     // total de páginas
        resultado.getTotalElements()   // total de elementos
    );

}

    /**
     * Buscar por nombre con paginación real.
     */
    public PagedResponse<TipoDeDesarrolladorResponseDTO> findByNombrePaged(
            String nombre, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("nombre").ascending());

        Page<TipoDeDesarrolladorModel> pagina =
                tipoDeDesarrolladorRepository.findByNombreContainingIgnoreCase(nombre, pageable);

        List<TipoDeDesarrolladorResponseDTO> contenido = pagina.getContent()
                .stream()
                .map(this::toResponseDTO)
                .toList();

        return new PagedResponse<>(
                contenido,
                page,
                pagina.getTotalPages(),
                pagina.getTotalElements()
        );
    }

    public List<TipoDeDesarrolladorResponseDTO> findAll() {
        return tipoDeDesarrolladorRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TipoDeDesarrolladorResponseDTO findById(Long id) {
        TipoDeDesarrolladorModel model = tipoDeDesarrolladorRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de desarrollador no encontrado: " + id));
        return toResponseDTO(model);
    }

    public TipoDeDesarrolladorResponseDTO save(TipoDeDesarrolladorRequestDTO t) {
        if (t.getNombre() == null || t.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        String normalizado = t.getNombre().trim();
        if (tipoDeDesarrolladorRepository.existsByNombreIgnoreCase(normalizado)) {
            throw new IllegalArgumentException("Ya existe un tipo de desarrollador con ese nombre");
        }

        TipoDeDesarrolladorModel model = new TipoDeDesarrolladorModel();
        model.setNombre(normalizado);

        TipoDeDesarrolladorModel guardado = tipoDeDesarrolladorRepository.save(model);
        return toResponseDTO(guardado);
    }

    public TipoDeDesarrolladorResponseDTO update(Long id, TipoDeDesarrolladorUpdateDTO in) {
        TipoDeDesarrolladorModel t = tipoDeDesarrolladorRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de desarrollador no encontrado: " + id));

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

        TipoDeDesarrolladorModel actualizado = tipoDeDesarrolladorRepository.save(t);
        return toResponseDTO(actualizado);
    }

    public TipoDeDesarrolladorResponseDTO patch(Long id, TipoDeDesarrolladorUpdateDTO in) {
        // misma lógica que update, separado por semántica
        return update(id, in);
    }

    public void deleteById(Long id) {
        tipoDeDesarrolladorRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de desarrollador no encontrado: " + id));

        if (tiposDeDesarrolladorRepository.existsByTipoDeDesarrollador_Id(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar: hay relaciones en tipos_de_desarrollador.");
        }
        tipoDeDesarrolladorRepository.deleteById(id);
    }

    // ================= HELPERS =================

    private TipoDeDesarrolladorResponseDTO toResponseDTO(TipoDeDesarrolladorModel model) {
        TipoDeDesarrolladorResponseDTO dto = new TipoDeDesarrolladorResponseDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        return dto;
    }

}