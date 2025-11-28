package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;

@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProductoModel, Long> {

    // Resumen (id, nombre, descripcion, activo)
    @Query("""
        SELECT tp.id, tp.nombre, tp.descripcion, tp.activo
        FROM TipoProductoModel tp
    """)
    List<Object[]> obtenerTipoProductoResumen();

    // Búsquedas por nombre
    List<TipoProductoModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<TipoProductoModel> findByNombreIgnoreCase(String nombre);

    // Validación duplicados
    boolean existsByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    // Activos / inactivos
    List<TipoProductoModel> findByActivoTrue();

    List<TipoProductoModel> findByActivoFalse();
}