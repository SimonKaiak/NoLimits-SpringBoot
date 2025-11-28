package com.example.NoLimits.Multimedia.repository.producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaModel, Long> {

    List<DetalleVentaModel> findByVenta_Id(Long ventaId);
    List<DetalleVentaModel> findByProducto_Id(Long productoId);

    long deleteByVenta_Id(Long ventaId);

    // Total calculado en DB para una venta (cantidad * precioUnitario)
    @Query("""
        SELECT COALESCE(SUM(d.cantidad * d.precioUnitario), 0)
        FROM DetalleVentaModel d
        WHERE d.venta.id = :ventaId
    """)
    Float calcularTotalPorVenta(Long ventaId);
}