package com.example.NoLimits.Multimedia.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    // Resumen básico
    @Query("""
        SELECT u.id, u.nombre, u.apellidos, u.correo, u.telefono
        FROM UsuarioModel u
    """)
    List<Object[]> obtenerUsuariosResumen();

    // Búsquedas
    List<UsuarioModel> findByApellidos(String apellidos);
    List<UsuarioModel> findByNombreAndApellidos(String nombre, String apellidos);
    List<UsuarioModel> findByNombre(String nombre);

    Optional<UsuarioModel> findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    // Por rol
    List<UsuarioModel> findByRol_Id(Long rolId);
    // NUEVO: para bloquear borrado de Rol si hay usuarios con ese rol
    boolean existsByRol_Id(Long rolId);

    List<UsuarioModel> findByNombreContainingIgnoreCase(String nombre);
}