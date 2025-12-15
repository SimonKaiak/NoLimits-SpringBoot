package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.TipoProductoModel;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.TipoProductoRepository;
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

    // ================== CRUD BÁSICO ==================

    public List<TipoProductoModel> findAll() {
        return tipoProductoRepository.findAll();
    }

    public TipoProductoModel findById(Long idTipoProducto) {
        return tipoProductoRepository.findById(idTipoProducto)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de producto no encontrado con ID: " + idTipoProducto));
    }

    /**
     * Búsqueda "amigable": contiene, ignore case.
     */
    public List<TipoProductoModel> findByNombre(String nombre) {
        return tipoProductoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Búsqueda por nombre exacto (ignore case).
     */
    public TipoProductoModel findByNombreExactIgnoreCase(String nombre) {
        return tipoProductoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Tipo de producto no encontrado con nombre: " + nombre));
    }

    public TipoProductoModel save(TipoProductoModel tipoProducto) {
        if (tipoProducto.getNombre() == null || tipoProducto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del tipo de producto es obligatorio");
        }

        String nombreNormalizado = tipoProducto.getNombre().trim();
        if (tipoProductoRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new IllegalArgumentException("Ya existe un tipo de producto con el nombre: " + nombreNormalizado);
        }

        tipoProducto.setNombre(nombreNormalizado);

        // Si activo viene null, lo dejamos en true por defecto
        if (tipoProducto.getActivo() == null) {
            tipoProducto.setActivo(true);
        }

        return tipoProductoRepository.save(tipoProducto);
    }

    public TipoProductoModel update(Long id, TipoProductoModel tipoProductoDetalles) {
        TipoProductoModel existente = findById(id);

        // Nombre
        if (tipoProductoDetalles.getNombre() != null && !tipoProductoDetalles.getNombre().isBlank()) {
            String nombreNormalizado = tipoProductoDetalles.getNombre().trim();

            // Validar duplicados solo si cambia el nombre
            if (!nombreNormalizado.equalsIgnoreCase(existente.getNombre())
                    && tipoProductoRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
                throw new IllegalArgumentException("Ya existe un tipo de producto con el nombre: " + nombreNormalizado);
            }

            existente.setNombre(nombreNormalizado);
        }

        // Descripción
        if (tipoProductoDetalles.getDescripcion() != null) {
            existente.setDescripcion(tipoProductoDetalles.getDescripcion());
        }

        // Activo
        if (tipoProductoDetalles.getActivo() != null) {
            existente.setActivo(tipoProductoDetalles.getActivo());
        }

        return tipoProductoRepository.save(existente);
    }

    /**
     * PATCH parcial. Reutilizamos la lógica de update para evitar duplicar validaciones.
     */
    public TipoProductoModel patch(Long id, TipoProductoModel tipoProductoParcial) {
        return update(id, tipoProductoParcial);
    }

    public void deleteById(Long idTipoProducto) {
        // 404 si no existe
        TipoProductoModel existente = findById(idTipoProducto);

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

    public List<TipoProductoModel> findActivos() {
        return tipoProductoRepository.findByActivoTrue();
    }

    public List<TipoProductoModel> findInactivos() {
        return tipoProductoRepository.findByActivoFalse();
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
}