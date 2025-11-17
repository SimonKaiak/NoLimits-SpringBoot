package com.example.NoLimits.Multimedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.ComunaModel;

@Repository
public interface ComunaRepository extends JpaRepository<ComunaModel, Long> {

    @Query("""
        SELECT c.id, c.nombre, r.id, r.nombre
        FROM ComunaModel c
        JOIN c.region r
    """)
    List<Object[]> obtenerComunasResumen();

    List<ComunaModel> findByRegion_Id(Long regionId);

    List<ComunaModel> findByNombreContainingIgnoreCase(String nombre);
    Optional<ComunaModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    // Para bloquear borrado de Region si tiene comunas
    boolean existsByRegion_Id(Long regionId);
}