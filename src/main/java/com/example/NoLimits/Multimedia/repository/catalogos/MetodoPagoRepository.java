package com.example.NoLimits.Multimedia.repository.catalogos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPagoModel, Long> {

    // Resumen simple
    @Query("""
       SELECT mp.id, mp.nombre, mp.activo
       FROM MetodoPagoModel mp
       """)
    List<Object[]> getMetodoPagoResumen();

    // Búsquedas por nombre
    Optional<MetodoPagoModel> findByNombre(String nombre);
    Optional<MetodoPagoModel> findByNombreIgnoreCase(String nombre);

    // Validación duplicados
    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);

    // Activos
    List<MetodoPagoModel> findByActivoTrue();
}