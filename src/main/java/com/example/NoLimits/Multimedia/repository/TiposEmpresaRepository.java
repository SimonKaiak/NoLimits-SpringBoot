package com.example.NoLimits.Multimedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.TiposEmpresaModel;

@Repository
public interface TiposEmpresaRepository extends JpaRepository<TiposEmpresaModel, Long> {

    List<TiposEmpresaModel> findByEmpresa_Id(Long empresaId);
    List<TiposEmpresaModel> findByTipoEmpresa_Id(Long tipoEmpresaId);

    long deleteByEmpresa_IdAndTipoEmpresa_Id(Long empresaId, Long tipoEmpresaId);

    @Query("""
        SELECT rel.id, e.id, e.nombre, te.id, te.nombre
        FROM TiposEmpresaModel rel
        JOIN rel.empresa e
        JOIN rel.tipoEmpresa te
        WHERE (:empresaId IS NULL OR e.id = :empresaId)
          AND (:tipoEmpresaId IS NULL OR te.id = :tipoEmpresaId)
    """)
    List<Object[]> obtenerResumen(
            @Param("empresaId")     Long empresaId,
            @Param("tipoEmpresaId") Long tipoEmpresaId
    );

    // Para bloquear borrado de TipoEmpresa si está usada en la TP
    boolean existsByTipoEmpresa_Id(Long tipoId);

    // Para evitar duplicar la relación (empresa, tipo)
    boolean existsByEmpresa_IdAndTipoEmpresa_Id(Long empresaId, Long tipoId);

    // Para hacer unlink(empresaId, tipoId)
    Optional<TiposEmpresaModel> findByEmpresa_IdAndTipoEmpresa_Id(Long empresaId, Long tipoId);
}