package com.example.NoLimits.Multimedia.repository.review;

import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUsuarioAndObraId(
            UsuarioModel usuario,
            String obraId
    );

    Optional<Review> findByUsuarioAndObraIdAndParentReviewIsNull(
            UsuarioModel usuario,
            String obraId
    );

    List<Review> findByObraId(String obraId);

    void deleteByUsuarioAndObraId(
            UsuarioModel usuario,
            String obraId
    );

    void deleteByParentReview(Review parentReview);

    List<Review> findByUsuario_Id(Long usuarioId);
}