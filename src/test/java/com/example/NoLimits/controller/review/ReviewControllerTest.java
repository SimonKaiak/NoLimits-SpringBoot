package com.example.NoLimits.controller.review;

import com.example.NoLimits.Multimedia.controller.review.ReviewController;
import com.example.NoLimits.Multimedia.dto.review.request.ReviewReactionRequestDTO;
import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.review.TipoReaccion;
import com.example.NoLimits.Multimedia.repository.review.ReviewReactionRepository;
import com.example.NoLimits.Multimedia.service.review.ReviewService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ReviewControllerTest — Endpoints de reseñas de productos")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewReactionRepository reviewReactionRepository;

    private Review reviewBase() {
        // Usuario mock
        com.example.NoLimits.Multimedia.model.usuario.UsuarioModel usuario =
            new com.example.NoLimits.Multimedia.model.usuario.UsuarioModel();
        usuario.setNombre("Usuario Test");

        Review r = new Review();
        r.setObraId("obra-123");
        r.setContenido("Muy buena obra");
        r.setRating(5);
        r.setUsuario(usuario);
        return r;
    }

    @Test
    @DisplayName("POST /reviews/{usuarioId} → 200 OK al guardar una review")
    void guardarReview_valido_retorna200() throws Exception {
        when(reviewService.guardarReview(eq(1L), any())).thenReturn(reviewBase());
        when(reviewReactionRepository.countByReviewAndTipoReaccion(any(), any())).thenReturn(0L);

        mockMvc.perform(post("/api/v1/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"obraId\":\"obra-123\",\"contenido\":\"Muy buena obra\",\"rating\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.obraId").value("obra-123"))
                .andExpect(jsonPath("$.contenido").value("Muy buena obra"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    @DisplayName("POST /reviews/{usuarioId} → 200 OK al editar una review existente")
    void guardarReview_edicion_retorna200() throws Exception {
        Review editada = reviewBase();
        editada.setContenido("Contenido editado");

        when(reviewService.guardarReview(eq(1L), any())).thenReturn(editada);
        when(reviewReactionRepository.countByReviewAndTipoReaccion(any(), any())).thenReturn(0L);

        mockMvc.perform(post("/api/v1/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"obraId\":\"obra-123\",\"contenido\":\"Contenido editado\",\"rating\":4,\"reviewId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido").value("Contenido editado"));
    }

    @Test
    @DisplayName("POST /reviews/{usuarioId} → 200 OK al crear respuesta a otra review")
    void guardarReview_respuesta_retorna200() throws Exception {
        Review respuesta = reviewBase();
        respuesta.setContenido("Respuesta a la review");

        when(reviewService.guardarReview(eq(1L), any())).thenReturn(respuesta);
        when(reviewReactionRepository.countByReviewAndTipoReaccion(any(), any())).thenReturn(0L);

        mockMvc.perform(post("/api/v1/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"obraId\":\"obra-123\",\"contenido\":\"Respuesta a la review\",\"parentReviewId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido").value("Respuesta a la review"));
    }

    @Test
    @DisplayName("GET /reviews/obra/{obraId} → 200 OK con lista de reviews")
    void obtenerReviewsPorObra_conReviews_retorna200() throws Exception {
        Review r1 = reviewBase();
        Review r2 = reviewBase();
        r2.setContenido("Otra opinión");

        when(reviewService.obtenerReviewsPorObra("obra-123")).thenReturn(List.of(r1, r2));
        when(reviewReactionRepository.countByReviewAndTipoReaccion(any(), eq(TipoReaccion.LIKE))).thenReturn(3L);
        when(reviewReactionRepository.countByReviewAndTipoReaccion(any(), eq(TipoReaccion.DISLIKE))).thenReturn(1L);

        mockMvc.perform(get("/api/v1/reviews/obra/obra-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].obraId").value("obra-123"));
    }

    @Test
    @DisplayName("GET /reviews/obra/{obraId} → 200 OK con lista vacía")
    void obtenerReviewsPorObra_sinReviews_retornaListaVacia() throws Exception {
        when(reviewService.obtenerReviewsPorObra("obra-sin-reviews")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/reviews/obra/obra-sin-reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /reviews/obra/{obraId} → likes y dislikes correctos")
    void obtenerReviewsPorObra_conteoLikesDislikes_correcto() throws Exception {
        Review r = reviewBase();

        when(reviewService.obtenerReviewsPorObra("obra-123")).thenReturn(List.of(r));
        when(reviewReactionRepository.countByReviewAndTipoReaccion(any(), eq(TipoReaccion.LIKE))).thenReturn(10L);
        when(reviewReactionRepository.countByReviewAndTipoReaccion(any(), eq(TipoReaccion.DISLIKE))).thenReturn(2L);

        mockMvc.perform(get("/api/v1/reviews/obra/obra-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].likes").value(10))
                .andExpect(jsonPath("$[0].dislikes").value(2));
    }

    @Test
    @DisplayName("DELETE /reviews/{usuarioId}/{obraId} → 204 No Content")
    void eliminarReview_porObra_retorna204() throws Exception {
        doNothing().when(reviewService).eliminarReview(1L, "obra-123");

        mockMvc.perform(delete("/api/v1/reviews/1/obra-123"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /reviews/{usuarioId}/review/{reviewId} → 204 No Content")
    void eliminarReviewPorId_retorna204() throws Exception {
        doNothing().when(reviewService).eliminarReviewPorId(1L, 5L);

        mockMvc.perform(delete("/api/v1/reviews/1/review/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /reviews/{reviewId}/reaction/{usuarioId} → 200 OK con LIKE")
    void reaccionarReview_like_retorna200() throws Exception {
        doNothing().when(reviewService).reaccionarReview(eq(1L), eq(2L), any(ReviewReactionRequestDTO.class));

        mockMvc.perform(post("/api/v1/reviews/1/reaction/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipoReaccion\":\"LIKE\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /reviews/{reviewId}/reaction/{usuarioId} → 200 OK con DISLIKE")
    void reaccionarReview_dislike_retorna200() throws Exception {
        doNothing().when(reviewService).reaccionarReview(eq(1L), eq(2L), any(ReviewReactionRequestDTO.class));

        mockMvc.perform(post("/api/v1/reviews/1/reaction/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipoReaccion\":\"DISLIKE\"}"))
                .andExpect(status().isOk());
    }
}