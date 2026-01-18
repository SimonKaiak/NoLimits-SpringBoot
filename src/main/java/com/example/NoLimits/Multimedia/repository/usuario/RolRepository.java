package com.example.NoLimits.Multimedia.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;

@Repository
public interface RolRepository extends JpaRepository<RolModel, Long> {

    @Query("""
        SELECT r.id, r.nombre
        FROM RolModel r
    """)
    List<Object[]> obtenerRolesResumen();

    List<RolModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<RolModel> findByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);

    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END
        FROM UsuarioModel u
        WHERE u.rol.id = :rolId
    """)
    boolean existeUsuarioConRol(Long rolId);
}