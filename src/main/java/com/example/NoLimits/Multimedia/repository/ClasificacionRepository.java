package com.example.NoLimits.Multimedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.ClasificacionModel;

@Repository
public interface ClasificacionRepository extends JpaRepository<ClasificacionModel, Long> {

    // Resumen simple (id, nombre, descripcion, activo)
    @Query("""
        SELECT c.id, c.nombre, c.descripcion, c.activo
        FROM ClasificacionModel c
    """)
    List<Object[]> obtenerClasificacionesResumen();

    // Búsquedas por nombre
    List<ClasificacionModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<ClasificacionModel> findByNombreIgnoreCase(String nombre);

    // Filtros por estado de actividad
    List<ClasificacionModel> findByActivoTrue();

    List<ClasificacionModel> findByActivoFalse();

    // Existencia
    boolean existsByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}