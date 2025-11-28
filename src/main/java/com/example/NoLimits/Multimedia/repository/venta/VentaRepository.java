package com.example.NoLimits.Multimedia.repository.venta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.venta.VentaModel;

@Repository
public interface VentaRepository extends JpaRepository<VentaModel, Long> {

    // Resumen de ventas (id, fecha, hora, usuario, pago, envío, estado)
    @Query("""
        SELECT v.id,
               v.fechaCompra,
               v.horaCompra,
               u.id,
               u.nombre,
               mp.nombre,
               me.nombre,
               e.nombre
        FROM VentaModel v
        JOIN v.usuarioModel u
        JOIN v.metodoPagoModel mp
        JOIN v.metodoEnvioModel me
        JOIN v.estado e
    """)
    List<Object[]> obtenerVentasResumen();

    // Por fecha / hora
    List<VentaModel> findByFechaCompra(LocalDate fechaCompra);
    List<VentaModel> findByHoraCompra(LocalTime horaCompra);

    // Filtros por FK
    List<VentaModel> findByUsuarioModel_Id(Long usuarioId);
    List<VentaModel> findByMetodoPagoModel_Id(Long metodoPagoId);
    List<VentaModel> findByMetodoEnvioModel_Id(Long metodoEnvioId);
    List<VentaModel> findByEstado_Id(Long estadoId);

    // Combinados
    List<VentaModel> findByUsuarioModel_IdAndMetodoPagoModel_Id(Long idUsuario, Long idMetodoPago);
    long countByUsuarioModel_Id(Long usuarioId);
}