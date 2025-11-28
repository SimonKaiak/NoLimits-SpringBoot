package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;

@Repository
public interface EstadoRepository extends JpaRepository<EstadoModel, Long> {

    // Resumen (id, nombre, descripción, activo)
    @Query("""
        SELECT e.id, e.nombre, e.descripcion, e.activo
        FROM EstadoModel e
    """)
    List<Object[]> obtenerEstadosResumen();

    // Búsquedas por nombre
    List<EstadoModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<EstadoModel> findByNombreIgnoreCase(String nombre);

    // Validación de duplicados
    boolean existsByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    // Filtros por activo/inactivo
    List<EstadoModel> findByActivoTrue();

    List<EstadoModel> findByActivoFalse();
}