package com.example.NoLimits.Multimedia.service.catalogos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.MetodoPagoRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.MetodoPagoResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.MetodoPagoUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    // ================== HELPERS INTERNOS ==================

    private MetodoPagoModel getEntityById(Long id) {
        return metodoPagoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Método de pago no encontrado con ID: " + id));
    }

    private String normalizar(String s) {
        return s == null ? null : s.trim();
    }

    private MetodoPagoResponseDTO toResponse(MetodoPagoModel model) {
        MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setActivo(model.getActivo());
        return dto;
    }

    // ================== CRUD BÁSICO (DTOs) ==================

    public List<MetodoPagoResponseDTO> findAll() {
        return metodoPagoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MetodoPagoResponseDTO findById(Long id) {
        return toResponse(getEntityById(id));
    }

    // Guardar un nuevo método de pago (valida nombre y duplicados)
    public MetodoPagoResponseDTO save(MetodoPagoRequestDTO request) {
        String nombre = normalizar(request.getNombre());
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (metodoPagoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe un método de pago con ese nombre");
        }

        MetodoPagoModel entity = new MetodoPagoModel();
        entity.setNombre(nombre);
        // Si no viene definido, por defecto true
        if (request.getActivo() == null) {
            entity.setActivo(true);
        } else {
            entity.setActivo(request.getActivo());
        }

        MetodoPagoModel guardado = metodoPagoRepository.save(entity);
        return toResponse(guardado);
    }

    public MetodoPagoResponseDTO update(Long id, MetodoPagoRequestDTO request) {
        MetodoPagoModel existente = getEntityById(id);

        String nuevoNombre = normalizar(request.getNombre());
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        // Validar duplicado con otro registro
        if (!nuevoNombre.equalsIgnoreCase(existente.getNombre())
                && metodoPagoRepository.existsByNombreIgnoreCase(nuevoNombre)) {
            throw new IllegalArgumentException("Ya existe un método de pago con ese nombre");
        }

        existente.setNombre(nuevoNombre);

        if (request.getActivo() != null) {
            existente.setActivo(request.getActivo());
        }

        MetodoPagoModel actualizado = metodoPagoRepository.save(existente);
        return toResponse(actualizado);
    }

    public MetodoPagoResponseDTO patch(Long id, MetodoPagoUpdateDTO request) {
        MetodoPagoModel existente = getEntityById(id);

        // nombre opcional
        if (request.getNombre() != null) {
            String nuevoNombre = normalizar(request.getNombre());
            if (nuevoNombre.isBlank()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }

            if (!nuevoNombre.equalsIgnoreCase(existente.getNombre())
                    && metodoPagoRepository.existsByNombreIgnoreCase(nuevoNombre)) {
                throw new IllegalArgumentException("Ya existe un método de pago con ese nombre");
            }

            existente.setNombre(nuevoNombre);
        }

        // activo opcional
        if (request.getActivo() != null) {
            existente.setActivo(request.getActivo());
        }

        MetodoPagoModel actualizado = metodoPagoRepository.save(existente);
        return toResponse(actualizado);
    }

    public void deleteById(Long id) {
        MetodoPagoModel existente = getEntityById(id);

        boolean enUso = !ventaRepository.findByMetodoPagoModel_Id(id).isEmpty();
        if (enUso) {
            throw new IllegalStateException(
                "No se puede eliminar: está asociado a una o más ventas."
            );
        }

        metodoPagoRepository.delete(existente);
    }

    // ================== CONSULTAS ADICIONALES ==================

    // Buscar por nombre (para el endpoint /buscar/{nombre})
    public Optional<MetodoPagoResponseDTO> findByNombre(String nombre) {
        if (nombre == null) {
            return Optional.empty();
        }

        String normalizado = normalizar(nombre);
        if (normalizado == null || normalizado.isBlank()) {
            return Optional.empty();
        }

        return metodoPagoRepository.findByNombreIgnoreCase(normalizado)
                .map(this::toResponse);
    }

    // Resumen (IDs, nombre, [activo si está en el SELECT])
    public List<Map<String, Object>> obtenerMetodoPagoConDatos() {
        List<Object[]> resultados = metodoPagoRepository.getMetodoPagoResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID", fila[0]);
            datos.put("Nombre", fila[1]);
            if (fila.length > 2) {
                datos.put("Activo", fila[2]);
            }
            lista.add(datos);
        }
        return lista;
    }
}