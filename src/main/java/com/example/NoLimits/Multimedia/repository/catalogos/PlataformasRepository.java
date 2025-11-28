package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.PlataformasModel;

@Repository
public interface PlataformasRepository extends JpaRepository<PlataformasModel, Long> {

    List<PlataformasModel> findByProducto_Id(Long productoId);
    List<PlataformasModel> findByPlataforma_Id(Long plataformaId);

    boolean existsByProducto_IdAndPlataforma_Id(Long productoId, Long plataformaId);

    long deleteByProducto_IdAndPlataforma_Id(Long productoId, Long plataformaId);

    @Query("""
        SELECT rel.id, p.id, p.nombre, pl.id, pl.nombre
        FROM PlataformasModel rel
        JOIN rel.producto p
        JOIN rel.plataforma pl
        WHERE (:productoId IS NULL OR p.id = :productoId)
          AND (:plataformaId IS NULL OR pl.id = :plataformaId)
    """)
    List<Object[]> obtenerResumen(Long productoId, Long plataformaId);
}