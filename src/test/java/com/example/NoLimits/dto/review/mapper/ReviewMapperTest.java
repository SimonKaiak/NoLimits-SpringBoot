package com.example.NoLimits.dto.review.mapper;

import com.example.NoLimits.Multimedia.dto.review.mapper.ReviewMapper;
import com.example.NoLimits.Multimedia.dto.review.response.ReviewResponseDTO;
import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReviewMapper")
class ReviewMapperTest {

    @Nested
    @DisplayName("describe: toResponseDTO")
    class ToResponseDTO {

        @Test
        @DisplayName("it: debería mapear review editada cuando fechaActualizacion es distinta a fechaCreacion")
        void deberiaMapearReviewEditada() {
            LocalDateTime fechaCreacion = LocalDateTime.of(2026, 6, 1, 10, 0);
            LocalDateTime fechaActualizacion = LocalDateTime.of(2026, 6, 2, 12, 0);

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(1L);
            usuario.setNombre("Juan");
            usuario.setApellidos("Pérez");

            Review review = new Review();
            ReflectionTestUtils.setField(review, "id", 10L);
            ReflectionTestUtils.setField(review, "fechaCreacion", fechaCreacion);
            ReflectionTestUtils.setField(review, "fechaActualizacion", fechaActualizacion);
            review.setUsuario(usuario);
            review.setObraId("movie-123");
            review.setContenido("Muy buena obra");
            review.setRating(5);

            ReviewResponseDTO dto = ReviewMapper.toResponseDTO(review, 7L, 2L);

            assertNotNull(dto);
            assertEquals(10L, dto.getId());
            assertEquals(1L, dto.getUsuarioId());
            assertEquals("Juan Pérez", dto.getNombreUsuario());
            assertEquals("movie-123", dto.getObraId());
            assertEquals("Muy buena obra", dto.getContenido());
            assertEquals(5, dto.getRating());
            assertTrue(dto.getEditado());
            assertEquals(fechaCreacion, dto.getFechaCreacion());
            assertEquals(fechaActualizacion, dto.getFechaActualizacion());
            assertEquals(7L, dto.getLikes());
            assertEquals(2L, dto.getDislikes());
            assertNull(dto.getParentReviewId());
            assertNull(dto.getRootReviewId());
        }

        @Test
        @DisplayName("it: debería mapear review no editada cuando las fechas son iguales")
        void deberiaMapearReviewNoEditada() {
            LocalDateTime fecha = LocalDateTime.of(2026, 6, 1, 10, 0);

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(2L);
            usuario.setNombre("Ana");
            usuario.setApellidos("Gómez");

            Review review = new Review();
            ReflectionTestUtils.setField(review, "id", 20L);
            ReflectionTestUtils.setField(review, "fechaCreacion", fecha);
            ReflectionTestUtils.setField(review, "fechaActualizacion", fecha);
            review.setUsuario(usuario);
            review.setObraId("book-456");
            review.setContenido("Comentario sin editar");
            review.setRating(4);

            ReviewResponseDTO dto = ReviewMapper.toResponseDTO(review, 3L, 1L);

            assertNotNull(dto);
            assertEquals(20L, dto.getId());
            assertEquals(2L, dto.getUsuarioId());
            assertEquals("Ana Gómez", dto.getNombreUsuario());
            assertFalse(dto.getEditado());
            assertEquals(3L, dto.getLikes());
            assertEquals(1L, dto.getDislikes());
            assertNull(dto.getParentReviewId());
            assertNull(dto.getRootReviewId());
        }

        @Test
        @DisplayName("it: debería mapear parentReviewId y rootReviewId cuando existen")
        void deberiaMapearParentReviewIdYRootReviewIdCuandoExisten() {
            LocalDateTime fecha = LocalDateTime.of(2026, 6, 1, 10, 0);

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(3L);
            usuario.setNombre("Carlos");
            usuario.setApellidos("López");

            Review parentReview = new Review();
            ReflectionTestUtils.setField(parentReview, "id", 100L);

            Review rootReview = new Review();
            ReflectionTestUtils.setField(rootReview, "id", 200L);

            Review review = new Review();
            ReflectionTestUtils.setField(review, "id", 30L);
            ReflectionTestUtils.setField(review, "fechaCreacion", fecha);
            ReflectionTestUtils.setField(review, "fechaActualizacion", fecha);
            review.setUsuario(usuario);
            review.setObraId("game-789");
            review.setContenido("Respuesta a review");
            review.setRating(null);
            review.setParentReview(parentReview);
            review.setRootReview(rootReview);

            ReviewResponseDTO dto = ReviewMapper.toResponseDTO(review, 0L, 0L);

            assertNotNull(dto);
            assertEquals(30L, dto.getId());
            assertEquals(100L, dto.getParentReviewId());
            assertEquals(200L, dto.getRootReviewId());
            assertFalse(dto.getEditado());
        }

        @Test
        @DisplayName("it: debería marcar editado false cuando fechaActualizacion es null")
        void deberiaMarcarEditadoFalseCuandoFechaActualizacionEsNull() {
            LocalDateTime fechaCreacion = LocalDateTime.of(2026, 6, 1, 10, 0);

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(4L);
            usuario.setNombre("Pedro");
            usuario.setApellidos("Soto");

            Review review = new Review();
            ReflectionTestUtils.setField(review, "id", 40L);
            ReflectionTestUtils.setField(review, "fechaCreacion", fechaCreacion);
            ReflectionTestUtils.setField(review, "fechaActualizacion", null);
            review.setUsuario(usuario);
            review.setObraId("anime-999");
            review.setContenido("Review sin actualización");
            review.setRating(3);

            ReviewResponseDTO dto = ReviewMapper.toResponseDTO(review, 1L, 0L);

            assertNotNull(dto);
            assertFalse(dto.getEditado());
            assertNull(dto.getFechaActualizacion());
        }
    }
}