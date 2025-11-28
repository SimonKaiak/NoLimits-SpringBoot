package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;

@Repository
public interface PlataformaRepository extends JpaRepository<PlataformaModel, Long> {

    @Query("""
        SELECT p.id, p.nombre
        FROM PlataformaModel p
    """)
    List<Object[]> obtenerPlataformasResumen();

    List<PlataformaModel> findByNombreContainingIgnoreCase(String nombre);
    Optional<PlataformaModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}