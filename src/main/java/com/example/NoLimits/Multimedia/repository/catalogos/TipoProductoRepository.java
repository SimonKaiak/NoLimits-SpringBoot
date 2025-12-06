// Ruta: src/main/java/com/example/NoLimits/Multimedia/repository/catalogos/TipoProductoRepository.java
package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;

@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProductoModel, Long> {

    @Query("""
        SELECT tp.id, tp.nombre, tp.descripcion, tp.activo
        FROM TipoProductoModel tp
    """)
    List<Object[]> obtenerTipoProductoResumen();

    List<TipoProductoModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<TipoProductoModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    List<TipoProductoModel> findByActivoTrue();

    List<TipoProductoModel> findByActivoFalse();

    // Versión paginada correcta
    Page<TipoProductoModel> findByNombreContainingIgnoreCase(
        String nombre,
        Pageable pageable
    );
}