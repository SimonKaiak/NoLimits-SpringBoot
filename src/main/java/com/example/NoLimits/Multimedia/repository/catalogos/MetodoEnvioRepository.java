package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;

@Repository
public interface MetodoEnvioRepository extends JpaRepository<MetodoEnvioModel, Long> {

    @Query("""
        SELECT me.id, me.nombre, me.descripcion, me.activo
        FROM MetodoEnvioModel me
    """)
    List<Object[]> obtenerMetodosEnvioResumen();

    List<MetodoEnvioModel> findByNombreContainingIgnoreCase(String nombre);
    Optional<MetodoEnvioModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);

    List<MetodoEnvioModel> findByActivoTrue();

    Page<MetodoEnvioModel> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}