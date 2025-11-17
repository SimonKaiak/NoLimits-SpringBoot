package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    /* ================= CRUD BÁSICO ================= */

    public List<ProductoModel> findAll() {
        return productoRepository.findAll();
    }

    public ProductoModel findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
    }

    public ProductoModel save(ProductoModel producto) {
        return productoRepository.save(producto);
    }

    // PUT: reemplaza datos principales
    public ProductoModel update(Long id, ProductoModel productoDetalles) {
        ProductoModel productoExistente = findById(id);

        productoExistente.setNombre(productoDetalles.getNombre());
        productoExistente.setPrecio(productoDetalles.getPrecio());
        productoExistente.setTipoProducto(productoDetalles.getTipoProducto());
        productoExistente.setClasificacion(productoDetalles.getClasificacion());
        productoExistente.setEstado(productoDetalles.getEstado());

        return productoRepository.save(productoExistente);
    }

    // PATCH: solo campos no nulos
    public ProductoModel patch(Long id, ProductoModel productoDetalles) {
        ProductoModel productoExistente = findById(id);

        if (productoDetalles.getNombre() != null) {
            productoExistente.setNombre(productoDetalles.getNombre());
        }
        if (productoDetalles.getPrecio() != null) {
            productoExistente.setPrecio(productoDetalles.getPrecio());
        }
        if (productoDetalles.getTipoProducto() != null) {
            productoExistente.setTipoProducto(productoDetalles.getTipoProducto());
        }
        if (productoDetalles.getClasificacion() != null) {
            productoExistente.setClasificacion(productoDetalles.getClasificacion());
        }
        if (productoDetalles.getEstado() != null) {
            productoExistente.setEstado(productoDetalles.getEstado());
        }

        return productoRepository.save(productoExistente);
    }

    public void deleteById(Long id) {
        // 404 si no existe
        findById(id);

        // 409 si tiene movimientos en detalle_venta
        boolean tieneMovimientos = !detalleVentaRepository.findByProducto_Id(id).isEmpty();
        if (tieneMovimientos) {
            throw new IllegalStateException(
                    "No se puede eliminar: el producto tiene movimientos en ventas."
            );
        }

        productoRepository.deleteById(id);
    }

    /* ================= BÚSQUEDAS ================= */

    public List<ProductoModel> findByNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }

    public List<ProductoModel> findByNombreContainingIgnoreCase(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<ProductoModel> findByTipoProducto(Long tipoProductoId) {
        return productoRepository.findByTipoProducto_Id(tipoProductoId);
    }

    public List<ProductoModel> findByClasificacion(Long clasificacionId) {
        return productoRepository.findByClasificacion_Id(clasificacionId);
    }

    public List<ProductoModel> findByEstado(Long estadoId) {
        return productoRepository.findByEstado_Id(estadoId);
    }

    public List<ProductoModel> findByTipoProductoAndEstado(Long tipoProductoId, Long estadoId) {
        return productoRepository.findByTipoProducto_IdAndEstado_Id(tipoProductoId, estadoId);
    }

    /* ================= RESUMEN ================= */

    // ID, nombre, precio, tipo, estado
    public List<Map<String, Object>> obtenerProductosConDatos() {
        List<Object[]> resultados = productoRepository.obtenerProductosResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID", fila[0]);
            datos.put("Nombre", fila[1]);
            datos.put("Precio", fila[2]);
            datos.put("Tipo Producto", fila[3]);
            datos.put("Estado", fila[4]);
            lista.add(datos);
        }
        return lista;
    }
}