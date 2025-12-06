package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.DesarrolladorRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.DesarrolladorResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.DesarrolladorUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladorRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DesarrolladorService {

    @Autowired
    private DesarrolladorRepository desarrolladorRepository;

    // ==========================
    // MÉTODOS PÚBLICOS CON DTOs
    // ==========================

    /**
     * Listar todos los desarrolladores (catálogo completo).
     */
    public List<DesarrolladorResponseDTO> findAll() {
        return desarrolladorRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un desarrollador por ID (como DTO de respuesta).
     */
    public DesarrolladorResponseDTO findById(Long id) {
        DesarrolladorModel model = findEntityById(id);
        return toResponseDTO(model);
    }

    /**
     * Crear un nuevo desarrollador a partir de un RequestDTO.
     */
    public DesarrolladorResponseDTO save(DesarrolladorRequestDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del desarrollador es obligatorio");
        }

        String normalizado = dto.getNombre().trim();

        if (desarrolladorRepository.existsByNombreIgnoreCase(normalizado)) {
            throw new IllegalArgumentException("Ya existe un desarrollador con ese nombre");
        }

        DesarrolladorModel entity = new DesarrolladorModel();
        entity.setNombre(normalizado);

        // Si desde el front no envían nada, queda true por defecto.
        // Si el DTO trae explícitamente false, se respeta.
        if (dto.getActivo() == null) {
            entity.setActivo(true);
        } else {
            entity.setActivo(dto.getActivo());
        }

        DesarrolladorModel guardado = desarrolladorRepository.save(entity);
        return toResponseDTO(guardado);
    }

    /**
     * Actualizar un desarrollador (PUT completo) usando UpdateDTO.
     */
    public DesarrolladorResponseDTO update(Long id, DesarrolladorUpdateDTO in) {
        DesarrolladorModel d = findEntityById(id);

        // Actualizar nombre si viene en el body
        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (!nuevo.equalsIgnoreCase(d.getNombre())
                    && desarrolladorRepository.existsByNombreIgnoreCase(nuevo)) {
                throw new IllegalArgumentException("Ya existe un desarrollador con ese nombre");
            }
            d.setNombre(nuevo);
        }

        // Actualizar activo si viene en el DTO (Boolean wrapper para diferenciar null)
        if (in.getActivo() != null && in.getActivo() != d.isActivo()) {
            d.setActivo(in.getActivo());
        }

        DesarrolladorModel actualizado = desarrolladorRepository.save(d);
        return toResponseDTO(actualizado);
    }

    /**
     * Actualización parcial (PATCH) de un desarrollador usando el mismo UpdateDTO.
     */
    public DesarrolladorResponseDTO patch(Long id, DesarrolladorUpdateDTO in) {
        DesarrolladorModel d = findEntityById(id);

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (!nuevo.equalsIgnoreCase(d.getNombre())
                    && desarrolladorRepository.existsByNombreIgnoreCase(nuevo)) {
                throw new IllegalArgumentException("Ya existe un desarrollador con ese nombre");
            }
            d.setNombre(nuevo);
        }

        // Para patch, activo también se maneja como Boolean:
        // solo se aplica si viene explícito en el JSON.
        if (in.getActivo() != null && in.getActivo() != d.isActivo()) {
            d.setActivo(in.getActivo());
        }

        DesarrolladorModel actualizado = desarrolladorRepository.save(d);
        return toResponseDTO(actualizado);
    }

    /**
     * Eliminar un desarrollador por ID.
     */
    public void deleteById(Long id) {
        findEntityById(id); // valida existencia
        desarrolladorRepository.deleteById(id);
    }

    /**
     * Búsqueda por nombre (filtro parcial, ignore case).
     */
    public List<DesarrolladorResponseDTO> findByNombre(String nombre) {
        String filtro = (nombre == null) ? "" : nombre.trim();
        return desarrolladorRepository.findByNombreContainingIgnoreCase(filtro)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PagedResponse<DesarrolladorResponseDTO> listarPaginado(int page, int size, String search) {

    PageRequest pageable = PageRequest.of(page - 1, size);

    Page<DesarrolladorModel> paginaBD;

    if (search != null && !search.trim().isEmpty()) {
        paginaBD = desarrolladorRepository.findByNombreContainingIgnoreCase(search.trim(), pageable);
    } else {
        paginaBD = desarrolladorRepository.findAll(pageable);
    }

    List<DesarrolladorResponseDTO> contenido = paginaBD.getContent()
            .stream()
            .map(this::toResponseDTO)
            .toList();

    return new PagedResponse<>(
            contenido,
            page,
            paginaBD.getTotalPages(),
            paginaBD.getTotalElements()
        );
    }

    // ==========================
    // MÉTODOS PRIVADOS DE APOYO
    // ==========================

    /**
     * Obtener la entidad real desde BD o lanzar excepción.
     */
    private DesarrolladorModel findEntityById(Long id) {
        return desarrolladorRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Desarrollador no encontrado con ID: " + id));
    }

    /**
     * Mapear entidad JPA → DTO de respuesta.
     */
    private DesarrolladorResponseDTO toResponseDTO(DesarrolladorModel m) {
        DesarrolladorResponseDTO dto = new DesarrolladorResponseDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setActivo(m.isActivo());
        return dto;
    }
}