package com.example.NoLimits.Multimedia.dto.review.mapper;

import com.example.NoLimits.Multimedia.dto.review.response.ReviewResponseDTO;
import com.example.NoLimits.Multimedia.model.review.Review;

public class ReviewMapper {

    public static ReviewResponseDTO toResponseDTO(
            Review review,
            Long likes,
            Long dislikes
    ) {

        boolean editado = review.getFechaActualizacion() != null
                && review.getFechaCreacion() != null
                && !review.getFechaActualizacion().equals(review.getFechaCreacion());

        return new ReviewResponseDTO(
                review.getId(),
                review.getUsuario().getId(),
                review.getUsuario().getNombreCompleto(),
                review.getObraId(),
                review.getContenido(),
                review.getRating(),
                editado,
                review.getFechaCreacion(),
                review.getFechaActualizacion(),
                likes,
                dislikes
        );
    }
}