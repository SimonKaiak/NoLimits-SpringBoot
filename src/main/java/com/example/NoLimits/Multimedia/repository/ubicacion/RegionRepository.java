package com.example.NoLimits.Multimedia.repository.ubicacion;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;

@Repository
public interface RegionRepository extends JpaRepository<RegionModel, Long> {

    @Query("""
        SELECT r.id, r.nombre
        FROM RegionModel r
    """)
    List<Object[]> obtenerRegionesResumen();

    List<RegionModel> findByNombreContainingIgnoreCase(String nombre);
    Optional<RegionModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}