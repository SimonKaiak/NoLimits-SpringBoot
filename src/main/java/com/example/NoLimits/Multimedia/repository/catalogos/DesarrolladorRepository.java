// Ruta: src/main/java/com/example/NoLimits/Multimedia/repository/DesarrolladorRepository.java
package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;

@Repository
public interface DesarrolladorRepository extends JpaRepository<DesarrolladorModel, Long> {

    @Query("""
        SELECT d.id, d.nombre, d.activo
        FROM DesarrolladorModel d
        ORDER BY d.nombre ASC
    """)
    List<Object[]> obtenerDesarrolladoresResumen();

    List<DesarrolladorModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<DesarrolladorModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    Page<DesarrolladorModel> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}