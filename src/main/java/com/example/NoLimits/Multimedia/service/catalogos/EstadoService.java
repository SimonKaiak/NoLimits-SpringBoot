package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.EstadoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EstadoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EstadoUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    // ======================================================
    // =============== MAPPER ENTIDAD ↔ DTO =================
    // ======================================================

    /**
     * Convierte un EstadoModel en EstadoResponseDTO.
     */
    private EstadoResponseDTO toResponseDTO(EstadoModel e) {
        if (e == null) return null;

        EstadoResponseDTO dto = new EstadoResponseDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setDescripcion(e.getDescripcion());
        dto.setActivo(e.getActivo());
        return dto;
    }

    /**
     * Normaliza y valida el nombre para operaciones de creación / actualización.
     */
    private String validarYNormalizarNombre(String nombre, Long idActual) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado es obligatorio");
        }

        String nombreNormalizado = nombre.trim();

        // Verificar duplicado con otro registro distinto al actual (si aplica)
        estadoRepository.findByNombreIgnoreCase(nombreNormalizado)
                .ifPresent(otro -> {
                    if (idActual == null || !otro.getId().equals(idActual)) {
                        throw new IllegalArgumentException(
                                "Ya existe un estado con el nombre: " + nombreNormalizado
                        );
                    }
                });

        return nombreNormalizado;
    }

    // ================== CRUD BÁSICO ==================

    /**
     * Devuelve todos los estados como DTOs.
     */
    public List<EstadoResponseDTO> findAll() {
        return estadoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un estado por ID y lo devuelve como DTO.
     */
    public EstadoResponseDTO findById(Long id) {
        EstadoModel estado = estadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + id));

        return toResponseDTO(estado);
    }

    /**
     * Crea un nuevo estado a partir de un DTO de request.
     */
    public EstadoResponseDTO save(EstadoRequestDTO dto) {

        // Validar y normalizar nombre (sin ID actual porque es creación)
        String nombreNormalizado = validarYNormalizarNombre(dto.getNombre(), null);

        EstadoModel e = new EstadoModel();
        e.setNombre(nombreNormalizado);
        e.setDescripcion(dto.getDescripcion());

        // Si no viene definido, por defecto true
        if (dto.getActivo() == null) {
            e.setActivo(true);
        } else {
            e.setActivo(dto.getActivo());
        }

        EstadoModel guardado = estadoRepository.save(e);
        return toResponseDTO(guardado);
    }

    /**
     * Actualización tipo PUT: se espera que vengan todos los campos obligatorios en el DTO.
     */
    public EstadoResponseDTO update(Long id, EstadoRequestDTO in) {
        EstadoModel existente = estadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + id));

        // nombre (obligatorio en PUT)
        String nombreNormalizado = validarYNormalizarNombre(in.getNombre(), id);
        existente.setNombre(nombreNormalizado);

        // descripción (puede ser null)
        existente.setDescripcion(in.getDescripcion());

        // activo: si viene null, dejamos el valor anterior, si no, lo actualizamos
        if (in.getActivo() != null) {
            existente.setActivo(in.getActivo());
        }

        EstadoModel actualizado = estadoRepository.save(existente);
        return toResponseDTO(actualizado);
    }

    /**
     * Actualización parcial tipo PATCH: solo modifica los campos enviados.
     */
    public EstadoResponseDTO patch(Long id, EstadoUpdateDTO in) {
        EstadoModel existente = estadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + id));

        // nombre (opcional)
        if (in.getNombre() != null) {
            String nombreNormalizado = validarYNormalizarNombre(in.getNombre(), id);
            existente.setNombre(nombreNormalizado);
        }

        // descripción (opcional)
        if (in.getDescripcion() != null) {
            existente.setDescripcion(in.getDescripcion());
        }

        // activo (opcional)
        if (in.getActivo() != null) {
            existente.setActivo(in.getActivo());
        }

        EstadoModel actualizado = estadoRepository.save(existente);
        return toResponseDTO(actualizado);
    }

    public void deleteById(Long id) {
        // 404 si no existe
        if (!estadoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Estado no encontrado con ID: " + id);
        }
        estadoRepository.deleteById(id);
    }

    // ================== CONSULTAS ADICIONALES ==================

    /**
     * Busca estados por nombre (LIKE, ignore case) y devuelve DTOs.
     */
    public List<EstadoResponseDTO> findByNombreLike(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }
        return estadoRepository.findByNombreContainingIgnoreCase(nombre.trim())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un estado por nombre exacto (ignore case) y devuelve DTO.
     */
    public EstadoResponseDTO findByNombreExact(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el nombre del estado");
        }
        EstadoModel estado = estadoRepository.findByNombreIgnoreCase(nombre.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Estado no encontrado con nombre: " + nombre));

        return toResponseDTO(estado);
    }

    /**
     * Devuelve solo estados activos como DTOs.
     */
    public List<EstadoResponseDTO> findActivos() {
        return estadoRepository.findByActivoTrue()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve solo estados inactivos como DTOs.
     */
    public List<EstadoResponseDTO> findInactivos() {
        return estadoRepository.findByActivoFalse()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Resumen tipo tabla (id, nombre, descripcion, activo)
    public List<Map<String, Object>> obtenerEstadosResumen() {
        List<Object[]> resultados = estadoRepository.obtenerEstadosResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("ID", fila[0]);
            map.put("Nombre", fila[1]);
            map.put("Descripcion", fila[2]);
            map.put("Activo", fila[3]);
            lista.add(map);
        }

        return lista;
    }

    // ================== PAGINACIÓN ==================

    public PagedResponse<EstadoResponseDTO> listarPaginado(int page, int size, String search) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<EstadoModel> paginaEstados;

        if (search != null && !search.isBlank()) {
            paginaEstados = estadoRepository.findByNombreContainingIgnoreCase(search.trim(), pageable);
        } else {
            paginaEstados = estadoRepository.findAll(pageable);
        }

        List<EstadoResponseDTO> contenido = paginaEstados
                .getContent()
                .stream()
                .map(this::toResponseDTO)
                .toList();

        return new PagedResponse<>(
                contenido,
                paginaEstados.getNumber() + 1,  // página actual (1-based)
                paginaEstados.getTotalPages(),  // total de páginas
                paginaEstados.getTotalElements() // total de registros
        );
    }
}