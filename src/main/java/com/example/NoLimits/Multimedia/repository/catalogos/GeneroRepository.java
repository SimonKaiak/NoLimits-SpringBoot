package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;

@Repository
public interface GeneroRepository extends JpaRepository<GeneroModel, Long> {

    @Query("""
        SELECT g.id, g.nombre
        FROM GeneroModel g
        ORDER BY g.nombre ASC
    """)
    List<Object[]> obtenerGenerosResumen();

    List<GeneroModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<GeneroModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}