package com.example.NoLimits.Multimedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.TipoEmpresaModel;

@Repository
public interface TipoEmpresaRepository extends JpaRepository<TipoEmpresaModel, Long> {

    @Query("""
        SELECT te.id, te.nombre
        FROM TipoEmpresaModel te
    """)
    List<Object[]> obtenerTiposEmpresaResumen();

    List<TipoEmpresaModel> findByNombreContainingIgnoreCase(String nombre);
    Optional<TipoEmpresaModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}