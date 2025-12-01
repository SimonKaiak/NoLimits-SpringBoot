package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoProductoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoProductoUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoProductoRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TipoProductoService {

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // ================== CRUD BÁSICO (DTO) ==================

    public List<TipoProductoResponseDTO> findAll() {
        return tipoProductoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public TipoProductoResponseDTO findById(Long idTipoProducto) {
        TipoProductoModel model = tipoProductoRepository.findById(idTipoProducto)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de producto no encontrado con ID: " + idTipoProducto));
        return toDTO(model);
    }

    /**
     * Búsqueda "amigable": contiene, ignore case.
     */
    public List<TipoProductoResponseDTO> findByNombre(String nombre) {
        return tipoProductoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Búsqueda por nombre exacto (ignore case).
     */
    public TipoProductoResponseDTO findByNombreExactIgnoreCase(String nombre) {
        TipoProductoModel model = tipoProductoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de producto no encontrado con nombre: " + nombre));
        return toDTO(model);
    }

    public TipoProductoResponseDTO save(TipoProductoRequestDTO dto) {
        TipoProductoModel entity = new TipoProductoModel();
        aplicarDatosRequest(entity, dto, false);
        TipoProductoModel guardado = tipoProductoRepository.save(entity);
        return toDTO(guardado);
    }

    public TipoProductoResponseDTO update(Long id, TipoProductoRequestDTO dto) {
        TipoProductoModel existente = tipoProductoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de producto no encontrado con ID: " + id));

        aplicarDatosRequest(existente, dto, false);
        TipoProductoModel actualizado = tipoProductoRepository.save(existente);
        return toDTO(actualizado);
    }

    /**
     * PATCH parcial. Solo aplica campos no nulos del DTO.
     */
    public TipoProductoResponseDTO patch(Long id, TipoProductoUpdateDTO dto) {
        TipoProductoModel existente = tipoProductoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de producto no encontrado con ID: " + id));

        aplicarDatosParciales(existente, dto);
        TipoProductoModel actualizado = tipoProductoRepository.save(existente);
        return toDTO(actualizado);
    }

    public void deleteById(Long idTipoProducto) {
        // 404 si no existe
        TipoProductoModel existente = tipoProductoRepository.findById(idTipoProducto)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de producto no encontrado con ID: " + idTipoProducto));

        // 409 si hay productos asociados a este tipo
        if (productoRepository.existsByTipoProducto_Id(idTipoProducto)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar: existen productos asociados a este tipo de producto."
            );
        }

        tipoProductoRepository.delete(existente);
    }

    // ================== BÚSQUEDAS POR ESTADO ==================

    public List<TipoProductoResponseDTO> findActivos() {
        return tipoProductoRepository.findByActivoTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TipoProductoResponseDTO> findInactivos() {
        return tipoProductoRepository.findByActivoFalse()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ================== RESUMEN ==================

    public List<Map<String, Object>> obtenerTipoProductoConNombres() {
        List<Object[]> resultados = tipoProductoRepository.obtenerTipoProductoResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> datos = new HashMap<>();
            // SELECT tp.id, tp.nombre, tp.descripcion, tp.activo
            datos.put("ID", fila[0]);
            datos.put("Nombre", fila[1]);
            datos.put("Descripcion", fila[2]);
            datos.put("Activo", fila[3]);
            lista.add(datos);
        }
        return lista;
    }

    // ================== HELPERS ==================

    private TipoProductoResponseDTO toDTO(TipoProductoModel model) {
        if (model == null) {
            return null;
        }
        TipoProductoResponseDTO dto = new TipoProductoResponseDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setDescripcion(model.getDescripcion());
        dto.setActivo(model.getActivo());
        return dto;
    }

    private void aplicarDatosRequest(TipoProductoModel entity, TipoProductoRequestDTO dto, boolean esPatch) {
        if (dto == null) {
            return;
        }

        // Nombre (obligatorio en create/update)
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            if (!esPatch) {
                throw new IllegalArgumentException("El nombre del tipo de producto es obligatorio");
            }
        } else {
            String nombreNormalizado = dto.getNombre().trim();

            // Validar duplicados solo si cambia el nombre
            if (entity.getId() == null ||
                    !nombreNormalizado.equalsIgnoreCase(entity.getNombre())) {

                if (tipoProductoRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
                    throw new IllegalArgumentException(
                            "Ya existe un tipo de producto con el nombre: " + nombreNormalizado
                    );
                }
            }

            entity.setNombre(nombreNormalizado);
        }

        // Descripción
        if (dto.getDescripcion() != null || !esPatch) {
            entity.setDescripcion(dto.getDescripcion());
        }

        // Activo
        if (dto.getActivo() != null) {
            entity.setActivo(dto.getActivo());
        } else if (!esPatch && entity.getActivo() == null) {
            // En create, si viene null, por defecto true
            entity.setActivo(true);
        }
    }

    private void aplicarDatosParciales(TipoProductoModel entity, TipoProductoUpdateDTO dto) {
        if (dto == null) {
            return;
        }

        // Nombre (opcional)
        if (dto.getNombre() != null) {
            String nombreNormalizado = dto.getNombre().trim();
            if (nombreNormalizado.isBlank()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }

            if (!nombreNormalizado.equalsIgnoreCase(entity.getNombre())
                    && tipoProductoRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
                throw new IllegalArgumentException(
                        "Ya existe un tipo de producto con el nombre: " + nombreNormalizado
                );
            }

            entity.setNombre(nombreNormalizado);
        }

        // Descripción (opcional)
        if (dto.getDescripcion() != null) {
            entity.setDescripcion(dto.getDescripcion());
        }

        // Activo (opcional)
        if (dto.getActivo() != null) {
            entity.setActivo(dto.getActivo());
        }
    }
}