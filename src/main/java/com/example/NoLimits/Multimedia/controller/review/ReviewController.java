package com.example.NoLimits.Multimedia.controller.review;

import com.example.NoLimits.Multimedia.dto.review.mapper.ReviewMapper;
import com.example.NoLimits.Multimedia.dto.review.request.ReviewReactionRequestDTO;
import com.example.NoLimits.Multimedia.dto.review.request.ReviewRequestDTO;
import com.example.NoLimits.Multimedia.dto.review.response.ReviewResponseDTO;
import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.review.TipoReaccion;
import com.example.NoLimits.Multimedia.repository.review.ReviewReactionRepository;
import com.example.NoLimits.Multimedia.service.review.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewReactionRepository reviewReactionRepository;

    public ReviewController(
            ReviewService reviewService,
            ReviewReactionRepository reviewReactionRepository
    ) {
        this.reviewService = reviewService;
        this.reviewReactionRepository = reviewReactionRepository;
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<ReviewResponseDTO> guardarReview(
            @PathVariable Long usuarioId,
            @RequestBody ReviewRequestDTO request
    ) {

        Review reviewGuardada = reviewService.guardarReview(usuarioId, request);

        return ResponseEntity.ok(
                ReviewMapper.toResponseDTO(reviewGuardada, 0L, 0L)
        );
    }

    @GetMapping("/obra/{obraId}")
    public ResponseEntity<List<ReviewResponseDTO>> obtenerReviewsPorObra(
            @PathVariable String obraId
    ) {

        List<Review> reviews = reviewService.obtenerReviewsPorObra(obraId);

        List<ReviewResponseDTO> response = reviews.stream()
                .map(review -> {

                    Long likes = reviewReactionRepository
                            .countByReviewAndTipoReaccion(
                                    review,
                                    TipoReaccion.LIKE
                            );

                    Long dislikes = reviewReactionRepository
                            .countByReviewAndTipoReaccion(
                                    review,
                                    TipoReaccion.DISLIKE
                            );

                    return ReviewMapper.toResponseDTO(
                            review,
                            likes,
                            dislikes
                    );
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{usuarioId}/{obraId}")
    public ResponseEntity<Void> eliminarReview(
            @PathVariable Long usuarioId,
            @PathVariable String obraId
    ) {
        reviewService.eliminarReview(usuarioId, obraId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reviewId}/reaction/{usuarioId}")
    public ResponseEntity<Void> reaccionarReview(
            @PathVariable Long reviewId,
            @PathVariable Long usuarioId,
            @RequestBody ReviewReactionRequestDTO request
    ) {
        reviewService.reaccionarReview(reviewId, usuarioId, request);
        return ResponseEntity.ok().build();
    }
}