// Ruta: src/main/java/com/example/NoLimits/Multimedia/repository/DesarrolladoresRepository.java
package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladoresModel;

@Repository
public interface DesarrolladoresRepository extends JpaRepository<DesarrolladoresModel, Long> {

    List<DesarrolladoresModel> findByProducto_Id(Long productoId);

    List<DesarrolladoresModel> findByDesarrollador_Id(Long desarrolladorId);

    boolean existsByProducto_IdAndDesarrollador_Id(Long productoId, Long desarrolladorId);

    long deleteByProducto_IdAndDesarrollador_Id(Long productoId, Long desarrolladorId);

    @Query("""
        SELECT d.id, p.id, p.nombre, dev.id, dev.nombre
        FROM DesarrolladoresModel d
        JOIN d.producto p
        JOIN d.desarrollador dev
        WHERE (:productoId IS NULL OR p.id = :productoId)
          AND (:desarrolladorId IS NULL OR dev.id = :desarrolladorId)
    """)
    List<Object[]> obtenerResumen(
            @Param("productoId") Long productoId,
            @Param("desarrolladorId") Long desarrolladorId);
}