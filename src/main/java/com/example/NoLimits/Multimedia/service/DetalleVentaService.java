package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.VentaRepository;
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
                .orElseThrow(() -> new RecursoNoEncontradoException("Detalle de venta no encontrado con ID: " + id));
    }

    public DetalleVentaModel save(DetalleVentaModel detalle) {
        if (detalle.getVenta() == null || detalle.getVenta().getId() == null)
            throw new IllegalArgumentException("Debe especificar una venta válida");
        if (detalle.getProducto() == null || detalle.getProducto().getId() == null)
            throw new IllegalArgumentException("Debe especificar un producto válido");

        detalle.setVenta(ventaRepository.findById(detalle.getVenta().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada")));
        detalle.setProducto(productoRepository.findById(detalle.getProducto().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado")));

        return detalleVentaRepository.save(detalle);
    }

    public void deleteById(Long id) {
        DetalleVentaModel existente = findById(id);
        detalleVentaRepository.delete(existente);
    }

    public List<DetalleVentaModel> findByVenta(Long idVenta) {
        return detalleVentaRepository.findByVenta_Id(idVenta);
    }
}