package com.example.NoLimits.model.review;

import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.review.ReviewReaction;
import com.example.NoLimits.Multimedia.model.review.TipoReaccion;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReviewReaction Tests")
class ReviewReactionTest {

    @Nested
    @DisplayName("Constructor")
    class ConstructorTest {

        @Test
        void debeCrearObjetoVacio() {

            ReviewReaction reaction = new ReviewReaction();

            assertNull(reaction.getReview());
            assertNull(reaction.getUsuario());
            assertNull(reaction.getTipoReaccion());
            assertNull(reaction.getFechaCreacion());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        void debeAsignarValoresCorrectamente() {

            Review review = new Review();
            UsuarioModel usuario = new UsuarioModel();

            ReviewReaction reaction = new ReviewReaction();

            reaction.setReview(review);
            reaction.setUsuario(usuario);
            reaction.setTipoReaccion(TipoReaccion.LIKE);

            assertEquals(review, reaction.getReview());
            assertEquals(usuario, reaction.getUsuario());
            assertEquals(TipoReaccion.LIKE, reaction.getTipoReaccion());
        }
    }

    @Nested
    @DisplayName("PrePersist")
    class PrePersistTest {

        @Test
        void debeAsignarFechaCreacion() {

            ReviewReaction reaction = new ReviewReaction();

            reaction.prePersist();

            assertNotNull(reaction.getFechaCreacion());
        }

        @Test
        void fechaCreacionDebeSerReciente() {

            ReviewReaction reaction = new ReviewReaction();

            reaction.prePersist();

            assertTrue(
                    reaction.getFechaCreacion()
                            .isBefore(LocalDateTime.now().plusSeconds(1))
            );
        }
    }

    @Test
    @DisplayName("TipoReaccion puede cambiar")
    void tipoReaccionPuedeCambiar() {

        ReviewReaction reaction = new ReviewReaction();

        reaction.setTipoReaccion(TipoReaccion.LIKE);
        assertEquals(TipoReaccion.LIKE, reaction.getTipoReaccion());

        reaction.setTipoReaccion(TipoReaccion.DISLIKE);
        assertEquals(TipoReaccion.DISLIKE, reaction.getTipoReaccion());
    }
}