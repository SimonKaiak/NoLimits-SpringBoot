package com.example.NoLimits.Multimedia.service.review;

import com.example.NoLimits.Multimedia.dto.review.request.ReviewRequestDTO;
import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.review.ReviewRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.dto.review.request.ReviewReactionRequestDTO;
import com.example.NoLimits.Multimedia.model.review.ReviewReaction;
import com.example.NoLimits.Multimedia.repository.review.ReviewReactionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReviewReactionRepository reviewReactionRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            UsuarioRepository usuarioRepository,
            ReviewReactionRepository reviewReactionRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.usuarioRepository = usuarioRepository;
        this.reviewReactionRepository = reviewReactionRepository;
    }

    public Review guardarReview(Long usuarioId, ReviewRequestDTO request) {

        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Review review;

        // ── Editar review/respuesta existente ─────────────────
        if (request.getReviewId() != null) {

        review = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        if (!review.getUsuario().getId().equals(usuario.getId())) {
                throw new RuntimeException("No puedes editar esta review");
        }

        // ── Si es respuesta nueva ─────────────────────────────
        } else if (request.getParentReviewId() != null) {

        Review parentReview = reviewRepository.findById(request.getParentReviewId())
                .orElseThrow(() -> new RuntimeException("Comentario padre no encontrado"));

        review = new Review();

        review.setParentReview(parentReview);

        // ── Root review tipo YouTube ─────────────────
        if (parentReview.getRootReview() != null) {
        review.setRootReview(parentReview.getRootReview());
        } else {
        review.setRootReview(parentReview);
        }

        // ── Review principal ──────────────────────────────────
        } else {

        review = reviewRepository
                .findByUsuarioAndObraIdAndParentReviewIsNull(
                        usuario,
                        request.getObraId()
                )
                .orElse(new Review());
        }

        review.setUsuario(usuario);
        review.setObraId(request.getObraId());
        review.setContenido(request.getContenido());
        review.setRating(request.getRating());

        return reviewRepository.save(review);
    }

    public List<Review> obtenerReviewsPorObra(String obraId) {
        return reviewRepository.findByObraId(obraId);
    }

    @Transactional
    public void eliminarReview(Long usuarioId, String obraId) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Review review = reviewRepository
                .findByUsuarioAndObraId(usuario, obraId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        reviewReactionRepository.deleteByReview(review);

        reviewRepository.delete(review);
    }

    public void reaccionarReview(Long reviewId, Long usuarioId, ReviewReactionRequestDTO request) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        ReviewReaction reaction = reviewReactionRepository
                .findByReviewAndUsuario(review, usuario)
                .orElse(new ReviewReaction());

        reaction.setReview(review);
        reaction.setUsuario(usuario);
        reaction.setTipoReaccion(request.getTipoReaccion());

        reviewReactionRepository.save(reaction);
    }

    @Transactional
    public void eliminarReviewPorId(Long usuarioId, Long reviewId) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        if (!review.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No puedes eliminar una reseña que no es tuya");
        }

        reviewRepository.deleteByParentReview(review);

        reviewReactionRepository.deleteByReview(review);

        reviewRepository.delete(review);
    }
}