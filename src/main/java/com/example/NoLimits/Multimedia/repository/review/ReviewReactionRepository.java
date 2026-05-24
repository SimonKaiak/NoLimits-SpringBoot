package com.example.NoLimits.Multimedia.repository.review;

import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.review.ReviewReaction;
import com.example.NoLimits.Multimedia.model.review.TipoReaccion;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewReactionRepository extends JpaRepository<ReviewReaction, Long> {

    Optional<ReviewReaction> findByReviewAndUsuario(
            Review review,
            UsuarioModel usuario
    );

    long countByReviewAndTipoReaccion(
            Review review,
            TipoReaccion tipoReaccion
    );

    void deleteByReview(Review review);

    void deleteByUsuario_Id(Long usuarioId);
}