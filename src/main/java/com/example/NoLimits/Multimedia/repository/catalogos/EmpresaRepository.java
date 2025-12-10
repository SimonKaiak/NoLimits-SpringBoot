package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaModel, Long> {

    @Query("""
        SELECT e.id, e.nombre, e.activo
        FROM EmpresaModel e
    """)
    List<Object[]> obtenerEmpresasResumen();


    List<EmpresaModel> findByNombreContainingIgnoreCase(String nombre);
    Optional<EmpresaModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}