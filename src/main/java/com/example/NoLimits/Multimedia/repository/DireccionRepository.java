package com.example.NoLimits.Multimedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.DireccionModel;

@Repository
public interface DireccionRepository extends JpaRepository<DireccionModel, Long> {

    Optional<DireccionModel> findByUsuarioModel_Id(Long usuarioId);
    boolean existsByUsuarioModel_Id(Long usuarioId);

    List<DireccionModel> findByComuna_Id(Long comunaId);

    @Query("""
        SELECT d.id, d.calle, d.numero, c.id, c.nombre, r.id, r.nombre, u.id, u.nombre
        FROM DireccionModel d
        JOIN d.comuna c
        JOIN c.region r
        LEFT JOIN d.usuarioModel u
    """)
    List<Object[]> obtenerDireccionesResumen();
}