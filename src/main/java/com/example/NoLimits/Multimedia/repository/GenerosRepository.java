package com.example.NoLimits.Multimedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.GenerosModel;

@Repository
public interface GenerosRepository extends JpaRepository<GenerosModel, Long> {

    List<GenerosModel> findByProducto_Id(Long productoId);

    List<GenerosModel> findByGenero_Id(Long generoId);

    boolean existsByProducto_IdAndGenero_Id(Long productoId, Long generoId);

    long deleteByProducto_IdAndGenero_Id(Long productoId, Long generoId);

    @Query("""
        SELECT rel.id, p.id, p.nombre, g.id, g.nombre
        FROM GenerosModel rel
        JOIN rel.producto p
        JOIN rel.genero g
        WHERE (:productoId IS NULL OR p.id = :productoId)
          AND (:generoId    IS NULL OR g.id = :generoId)
    """)
    List<Object[]> obtenerResumen(
            @Param("productoId") Long productoId,
            @Param("generoId") Long generoId
    );
}