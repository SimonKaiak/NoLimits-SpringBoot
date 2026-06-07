package com.example.NoLimits.model.review;

import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.review.ReviewReaction;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Review Tests")
class ReviewTest {

    @Nested
    @DisplayName("Constructor")
    class ConstructorTest {

        @Test
        void debeCrearReviewVacia() {

            Review review = new Review();

            assertNotNull(review.getReplies());
            assertNotNull(review.getThreadReplies());
            assertNotNull(review.getReactions());
        }
    }

    @Nested
    @DisplayName("Getters y Setters básicos")
    class GettersSetters {

        @Test
        void debeAsignarValoresCorrectamente() {

            UsuarioModel usuario = new UsuarioModel();

            Review review = new Review();

            review.setUsuario(usuario);
            review.setObraId("123");
            review.setContenido("Excelente película");
            review.setRating(5);

            assertEquals(usuario, review.getUsuario());
            assertEquals("123", review.getObraId());
            assertEquals("Excelente película", review.getContenido());
            assertEquals(5, review.getRating());
        }
    }

    @Nested
    @DisplayName("Relaciones")
    class Relaciones {

        @Test
        void debeAsignarParentReview() {

            Review padre = new Review();
            Review hijo = new Review();

            hijo.setParentReview(padre);

            assertEquals(padre, hijo.getParentReview());
        }

        @Test
        void debeAsignarRootReview() {

            Review root = new Review();
            Review reply = new Review();

            reply.setRootReview(root);

            assertEquals(root, reply.getRootReview());
        }

        @Test
        void debeAsignarReplies() {

            Review review = new Review();

            List<Review> replies = new ArrayList<>();
            replies.add(new Review());

            review.setReplies(replies);

            assertEquals(1, review.getReplies().size());
        }

        @Test
        void debeAsignarThreadReplies() {

            Review review = new Review();

            List<Review> replies = new ArrayList<>();
            replies.add(new Review());

            review.setThreadReplies(replies);

            assertEquals(1, review.getThreadReplies().size());
        }

        @Test
        void debeAsignarReactions() {

            Review review = new Review();

            List<ReviewReaction> reactions = new ArrayList<>();
            reactions.add(new ReviewReaction());

            review.setReactions(reactions);

            assertEquals(1, review.getReactions().size());
        }
    }

    @Nested
    @DisplayName("Fechas automáticas")
    class FechasAutomaticas {

        @Test
        void prePersistDebeAsignarFechas() {

            Review review = new Review();

            review.prePersist();

            assertNotNull(review.getFechaCreacion());
            assertNotNull(review.getFechaActualizacion());
        }

        @Test
        void preUpdateDebeActualizarFecha() {

            Review review = new Review();

            review.prePersist();

            LocalDateTime fechaAnterior =
                    review.getFechaActualizacion();

            review.preUpdate();

            assertNotNull(review.getFechaActualizacion());
            assertTrue(
                    !review.getFechaActualizacion().isBefore(fechaAnterior)
            );
        }
    }
}