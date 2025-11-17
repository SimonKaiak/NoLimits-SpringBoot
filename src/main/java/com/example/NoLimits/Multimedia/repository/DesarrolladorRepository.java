// Ruta: src/main/java/com/example/NoLimits/Multimedia/repository/DesarrolladorRepository.java
package com.example.NoLimits.Multimedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.DesarrolladorModel;

@Repository
public interface DesarrolladorRepository extends JpaRepository<DesarrolladorModel, Long> {

    @Query("""
        SELECT d.id, d.nombre
        FROM DesarrolladorModel d
    """)
    List<Object[]> obtenerDesarrolladoresResumen();

    List<DesarrolladorModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<DesarrolladorModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}