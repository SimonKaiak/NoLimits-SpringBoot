package com.example.NoLimits.Multimedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.EmpresasModel;

@Repository
public interface EmpresasRepository extends JpaRepository<EmpresasModel, Long> {

    List<EmpresasModel> findByProducto_Id(Long productoId);
    List<EmpresasModel> findByEmpresa_Id(Long empresaId);

    boolean existsByProducto_IdAndEmpresa_Id(Long productoId, Long empresaId);

    long deleteByProducto_IdAndEmpresa_Id(Long productoId, Long empresaId);

    @Query("""
        SELECT rel.id, p.id, p.nombre, e.id, e.nombre
        FROM EmpresasModel rel
        JOIN rel.producto p
        JOIN rel.empresa e
        WHERE (:productoId IS NULL OR p.id = :productoId)
          AND (:empresaId  IS NULL OR e.id = :empresaId)
    """)
    List<Object[]> obtenerResumen(
            @Param("productoId") Long productoId,
            @Param("empresaId")  Long empresaId
    );
}