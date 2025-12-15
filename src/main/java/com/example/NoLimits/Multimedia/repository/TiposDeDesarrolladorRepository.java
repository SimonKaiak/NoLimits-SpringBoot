// Ruta: src/main/java/com/example/NoLimits/Multimedia/repository/TiposDeDesarrolladorRepository.java
package com.example.NoLimits.Multimedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.TiposDeDesarrolladorModel;

@Repository
public interface TiposDeDesarrolladorRepository extends JpaRepository<TiposDeDesarrolladorModel, Long> {

    List<TiposDeDesarrolladorModel> findByDesarrollador_Id(Long desarrolladorId);

    List<TiposDeDesarrolladorModel> findByTipoDeDesarrollador_Id(Long tipoDesarrolladorId);

    long deleteByDesarrollador_IdAndTipoDeDesarrollador_Id(Long desarrolladorId, Long tipoDesarrolladorId);

    @Query("""
        SELECT rel.id, d.id, d.nombre, td.id, td.nombre
        FROM TiposDeDesarrolladorModel rel
        JOIN rel.desarrollador d
        JOIN rel.tipoDeDesarrollador td
        WHERE (:desarrolladorId IS NULL OR d.id = :desarrolladorId)
          AND (:tipoDesarrolladorId IS NULL OR td.id = :tipoDesarrolladorId)
    """)
    List<Object[]> obtenerResumen(
            @Param("desarrolladorId") Long desarrolladorId,
            @Param("tipoDesarrolladorId") Long tipoDesarrolladorId);

    // Para bloquear borrado de TipoDeDesarrollador si está usado
    boolean existsByTipoDeDesarrollador_Id(Long tipoId);

    // Para evitar duplicar la relación (desarrollador, tipo)
    boolean existsByDesarrollador_IdAndTipoDeDesarrollador_Id(Long desarrolladorId, Long tipoId);

    // Para hacer unlink(desarrolladorId, tipoId)
    Optional<TiposDeDesarrolladorModel> findByDesarrollador_IdAndTipoDeDesarrollador_Id(
            Long desarrolladorId,
            Long tipoId);
}