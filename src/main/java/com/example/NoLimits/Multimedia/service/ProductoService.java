package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.ClasificacionRepository;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.TipoProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @Autowired
    private EstadoRepository estadoRepository;


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

        if (producto.getTipoProducto() == null || producto.getTipoProducto().getId() == null)
            throw new RecursoNoEncontradoException("Debe indicar un tipo de producto válido.");

        if (producto.getClasificacion() == null || producto.getClasificacion().getId() == null)
            throw new RecursoNoEncontradoException("Debe indicar una clasificación válida.");

        if (producto.getEstado() == null || producto.getEstado().getId() == null)
            throw new RecursoNoEncontradoException("Debe indicar un estado válido.");

        producto.setTipoProducto(
            tipoProductoRepository.findById(producto.getTipoProducto().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Tipo de producto no encontrado con ID: " + producto.getTipoProducto().getId()))
        );

        producto.setClasificacion(
            clasificacionRepository.findById(producto.getClasificacion().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Clasificación no encontrada con ID: " + producto.getClasificacion().getId()))
        );

        producto.setEstado(
            estadoRepository.findById(producto.getEstado().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Estado no encontrado con ID: " + producto.getEstado().getId()))
        );

        return productoRepository.save(producto);
    }

    // PUT: reemplaza datos principales
    public ProductoModel update(Long id, ProductoModel productoDetalles) {
        ProductoModel productoExistente = findById(id);

        productoExistente.setNombre(productoDetalles.getNombre());
        productoExistente.setPrecio(productoDetalles.getPrecio());

        if (productoDetalles.getTipoProducto() == null || productoDetalles.getTipoProducto().getId() == null)
            throw new RecursoNoEncontradoException("Debe indicar un tipo de producto válido.");

        if (productoDetalles.getClasificacion() == null || productoDetalles.getClasificacion().getId() == null)
            throw new RecursoNoEncontradoException("Debe indicar una clasificación válida.");

        if (productoDetalles.getEstado() == null || productoDetalles.getEstado().getId() == null)
            throw new RecursoNoEncontradoException("Debe indicar un estado válido.");

        productoExistente.setTipoProducto(
            tipoProductoRepository.findById(productoDetalles.getTipoProducto().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Tipo de producto no encontrado con ID: " + productoDetalles.getTipoProducto().getId()))
        );

        productoExistente.setClasificacion(
            clasificacionRepository.findById(productoDetalles.getClasificacion().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Clasificación no encontrada con ID: " + productoDetalles.getClasificacion().getId()))
        );

        productoExistente.setEstado(
            estadoRepository.findById(productoDetalles.getEstado().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Estado no encontrado con ID: " + productoDetalles.getEstado().getId()))
        );

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
        if (productoDetalles.getTipoProducto() != null && productoDetalles.getTipoProducto().getId() != null) {
            productoExistente.setTipoProducto(
                tipoProductoRepository.findById(productoDetalles.getTipoProducto().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Tipo de producto no encontrado con ID: " + productoDetalles.getTipoProducto().getId()))
            );
        }
        if (productoDetalles.getClasificacion() != null && productoDetalles.getClasificacion().getId() != null) {
            productoExistente.setClasificacion(
                clasificacionRepository.findById(productoDetalles.getClasificacion().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Clasificación no encontrada con ID: " + productoDetalles.getClasificacion().getId()))
            );
        }
        if (productoDetalles.getEstado() != null && productoDetalles.getEstado().getId() != null) {
            productoExistente.setEstado(
                estadoRepository.findById(productoDetalles.getEstado().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Estado no encontrado con ID: " + productoDetalles.getEstado().getId()))
            );
        }

        return productoRepository.save(productoExistente);
    }


    public void deleteById(Long id) {
        findById(id);

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