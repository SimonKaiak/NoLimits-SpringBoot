// Ruta: src/main/java/com/example/NoLimits/Multimedia/service/MetodoPagoService.java
package com.example.NoLimits.Multimedia.service.catalogos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    // Obtener todos los métodos de pago
    public List<MetodoPagoModel> findAll() {
        return metodoPagoRepository.findAll();
    }

    // Obtener un método de pago por ID
    public MetodoPagoModel findById(Long id) {
        return metodoPagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado con ID: " + id));
    }

    // Guardar un nuevo método de pago (valida nombre y duplicados)
    public MetodoPagoModel save(MetodoPagoModel metodoPago) {
        String nombre = normalizar(metodoPago.getNombre());
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (metodoPagoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe un método de pago con ese nombre");
        }
        metodoPago.setNombre(nombre);
        if (metodoPago.getActivo() == null) {
            metodoPago.setActivo(true);
        }
        return metodoPagoRepository.save(metodoPago);
    }

    public MetodoPagoModel update(Long id, MetodoPagoModel metodoPagoDetails) {
        MetodoPagoModel existente = findById(id);

        String nuevoNombre = normalizar(metodoPagoDetails.getNombre());
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (!nuevoNombre.equalsIgnoreCase(existente.getNombre())
                && metodoPagoRepository.existsByNombreIgnoreCase(nuevoNombre)) {
            throw new IllegalArgumentException("Ya existe un método de pago con ese nombre");
        }

        existente.setNombre(nuevoNombre);
        if (metodoPagoDetails.getActivo() != null) {
            existente.setActivo(metodoPagoDetails.getActivo());
        }
        return metodoPagoRepository.save(existente);
    }

    public MetodoPagoModel patch(Long id, MetodoPagoModel metodoPagoDetails) {
        MetodoPagoModel existente = findById(id);

        if (metodoPagoDetails.getNombre() != null) {
            String nuevoNombre = normalizar(metodoPagoDetails.getNombre());
            if (nuevoNombre.isBlank()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (!nuevoNombre.equalsIgnoreCase(existente.getNombre())
                    && metodoPagoRepository.existsByNombreIgnoreCase(nuevoNombre)) {
                throw new IllegalArgumentException("Ya existe un método de pago con ese nombre");
            }
            existente.setNombre(nuevoNombre);
        }

        if (metodoPagoDetails.getActivo() != null) {
            existente.setActivo(metodoPagoDetails.getActivo());
        }

        return metodoPagoRepository.save(existente);
    }

    public void deleteById(Long id) {
        findById(id);

        boolean enUso = !ventaRepository.findByMetodoPagoModel_Id(id).isEmpty();
        if (enUso) {
            throw new IllegalStateException(
                "No se puede eliminar: está asociado a una o más ventas."
            );
        }

        metodoPagoRepository.deleteById(id);
    }

    // Buscar por nombre (para el endpoint /buscar/{nombre})
    public Optional<MetodoPagoModel> findByNombre(String nombre) {
        if (nombre == null) {
            return Optional.empty();
        }

        String normalizado = normalizar(nombre);
        if (normalizado == null || normalizado.isBlank()) {
            return Optional.empty();
        }

        return metodoPagoRepository.findByNombreIgnoreCase(normalizado);
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

    // Helpers
    private String normalizar(String s) {
        return s == null ? null : s.trim();
    }
}