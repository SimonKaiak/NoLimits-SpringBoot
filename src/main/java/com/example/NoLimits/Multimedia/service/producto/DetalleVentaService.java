package com.example.NoLimits.Multimedia.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<DetalleVentaModel> findAll() {
        return detalleVentaRepository.findAll();
    }

    public DetalleVentaModel findById(Long id) {
        return detalleVentaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Detalle de venta no encontrado con ID: " + id));
    }

    /** ===============================================================
     *  SAVE con nuevo modelo (precioUnitario obligatorio, subtotal calculado)
     *  =============================================================== */
    public DetalleVentaModel save(DetalleVentaModel detalle) {

        // Validación Venta
        if (detalle.getVenta() == null || detalle.getVenta().getId() == null)
            throw new IllegalArgumentException("Debe especificar una venta válida");

        VentaModel venta = ventaRepository.findById(detalle.getVenta().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada"));

        // Validación Producto
        if (detalle.getProducto() == null || detalle.getProducto().getId() == null)
            throw new IllegalArgumentException("Debe especificar un producto válido");

        ProductoModel producto = productoRepository.findById(detalle.getProducto().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        // Validación cantidad
        if (detalle.getCantidad() == null || detalle.getCantidad() < 1)
            throw new IllegalArgumentException("La cantidad mínima es 1");

        // Validación precio unitario
        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario() < 0)
            throw new IllegalArgumentException("Debe indicar un precio unitario válido");

        // Reasignar entidades cargadas
        detalle.setVenta(venta);
        detalle.setProducto(producto);

        return detalleVentaRepository.save(detalle);
    }

    /** ===============================================================
     *  PATCH — actualización parcial con el nuevo modelo
     *  =============================================================== */
    public DetalleVentaModel patch(Long id, DetalleVentaModel entrada) {

        DetalleVentaModel existente = findById(id);

        // PATCH Venta
        if (entrada.getVenta() != null && entrada.getVenta().getId() != null) {
            VentaModel venta = ventaRepository.findById(entrada.getVenta().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada"));
            existente.setVenta(venta);
        }

        // PATCH Producto
        if (entrada.getProducto() != null && entrada.getProducto().getId() != null) {
            ProductoModel producto = productoRepository.findById(entrada.getProducto().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
            existente.setProducto(producto);
        }

        // PATCH cantidad
        if (entrada.getCantidad() != null) {
            if (entrada.getCantidad() < 1)
                throw new IllegalArgumentException("La cantidad mínima es 1");

            existente.setCantidad(entrada.getCantidad());
        }

        // PATCH precioUnitario
        if (entrada.getPrecioUnitario() != null) {
            if (entrada.getPrecioUnitario() < 0)
                throw new IllegalArgumentException("El precio unitario no puede ser negativo");

            existente.setPrecioUnitario(entrada.getPrecioUnitario());
        }

        // Subtotal NO SE PATCHEA → es calculado
        // existente.getSubtotal() se recalcula automáticamente

        return detalleVentaRepository.save(existente);
    }

    public void deleteById(Long id) {
        DetalleVentaModel existente = findById(id);
        detalleVentaRepository.delete(existente);
    }

    public List<DetalleVentaModel> findByVenta(Long idVenta) {
        return detalleVentaRepository.findByVenta_Id(idVenta);
    }
}