package com.example.NoLimits.Multimedia.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {

    // Resumen (id, nombre, precio, tipo, estado)
    @Query("""
        SELECT p.id, p.nombre, p.precio, tp.nombre, e.nombre
        FROM ProductoModel p
        JOIN p.tipoProducto tp
        JOIN p.estado e
    """)
    List<Object[]> obtenerProductosResumen();

    // Búsquedas por nombre
    List<ProductoModel> findByNombre(String nombre);
    List<ProductoModel> findByNombreContainingIgnoreCase(String nombre);

    // Por tipo
    List<ProductoModel> findByTipoProducto_Id(Long tipoProductoId);

    // Por clasificación
    List<ProductoModel> findByClasificacion_Id(Long clasificacionId);

    // Por estado
    List<ProductoModel> findByEstado_Id(Long estadoId);

    // Combinado tipo + estado (ej: juegos activos de cierto tipo)
    List<ProductoModel> findByTipoProducto_IdAndEstado_Id(Long tipoProductoId, Long estadoId);

    // Existencia
    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByTipoProducto_Id(Long tipoId);
    boolean existsByEstado_Id(Long estadoId);
}