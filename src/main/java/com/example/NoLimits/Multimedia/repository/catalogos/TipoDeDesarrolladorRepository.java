// Ruta: src/main/java/com/example/NoLimits/Multimedia/repository/TipoDeDesarrolladorRepository.java
package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;

@Repository
public interface TipoDeDesarrolladorRepository extends JpaRepository<TipoDeDesarrolladorModel, Long> {

    @Query("""
        SELECT td.id, td.nombre
        FROM TipoDeDesarrolladorModel td
    """)
    List<Object[]> obtenerTiposDesarrolladorResumen();

    List<TipoDeDesarrolladorModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<TipoDeDesarrolladorModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}