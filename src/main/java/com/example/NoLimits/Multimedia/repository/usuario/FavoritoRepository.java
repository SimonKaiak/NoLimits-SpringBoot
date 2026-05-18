package com.example.NoLimits.Multimedia.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.usuario.FavoritoModel;

@Repository
public interface FavoritoRepository extends JpaRepository<FavoritoModel, Long> {

    List<FavoritoModel> findByUsuario_Id(Long usuarioId);

    Optional<FavoritoModel> findByUsuario_IdAndObraId(Long usuarioId, String obraId);

    boolean existsByUsuario_IdAndObraId(Long usuarioId, String obraId);
}