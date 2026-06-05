package com.example.NoLimits.service.review;

import com.example.NoLimits.Multimedia.dto.review.request.ReviewReactionRequestDTO;
import com.example.NoLimits.Multimedia.dto.review.request.ReviewRequestDTO;
import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.review.ReviewReaction;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.review.TipoReaccion;
import com.example.NoLimits.Multimedia.repository.review.ReviewReactionRepository;
import com.example.NoLimits.Multimedia.repository.review.ReviewRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.service.review.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ReviewReactionRepository reviewReactionRepository;

    @InjectMocks
    private ReviewService reviewService;

    private UsuarioModel usuario;
    private Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Juan");

        review = new Review();
        review.setUsuario(usuario);
        review.setObraId("movie-1");
        review.setContenido("Muy buena");
        review.setRating(5);
    }

    @Test
    void guardarReview_NuevaReview_OK() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setObraId("movie-1");
        request.setContenido("Excelente");
        request.setRating(5);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Review result = reviewService.guardarReview(1L, request);

        assertNotNull(result);
        assertEquals(usuario, result.getUsuario());
        assertEquals("movie-1", result.getObraId());
        assertEquals("Excelente", result.getContenido());
        assertEquals(5, result.getRating());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void guardarReview_UsuarioNoExiste_LanzaRuntime() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setObraId("movie-1");

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.guardarReview(99L, request));

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void guardarReview_EditaReviewExistente_OK() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setReviewId(10L);
        request.setObraId("movie-1");
        request.setContenido("Editada");
        request.setRating(4);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Review result = reviewService.guardarReview(1L, request);

        assertEquals("Editada", result.getContenido());
        assertEquals(4, result.getRating());
        verify(reviewRepository).save(review);
    }

    @Test
    void guardarReview_EditaReviewDeOtroUsuario_LanzaRuntime() {
        UsuarioModel otro = new UsuarioModel();
        otro.setId(2L);

        Review reviewOtro = new Review();
        reviewOtro.setUsuario(otro);

        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setReviewId(20L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.findById(20L)).thenReturn(Optional.of(reviewOtro));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.guardarReview(1L, request));

        assertEquals("No puedes editar esta review", ex.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void guardarReview_RespuestaNuevaConParent_OK() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setParentReviewId(10L);
        request.setObraId("movie-1");
        request.setContenido("Respuesta");
        request.setRating(5);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Review result = reviewService.guardarReview(1L, request);

        assertNotNull(result);
        assertEquals(review, result.getParentReview());
        assertEquals(review, result.getRootReview());
        assertEquals("Respuesta", result.getContenido());
    }

    @Test
    void obtenerReviewsPorObra_OK() {
        when(reviewRepository.findByObraId("movie-1"))
                .thenReturn(List.of(review));

        List<Review> result = reviewService.obtenerReviewsPorObra("movie-1");

        assertEquals(1, result.size());
        assertEquals("movie-1", result.get(0).getObraId());
    }

    @Test
    void eliminarReview_OK() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.findByUsuarioAndObraId(usuario, "movie-1"))
                .thenReturn(Optional.of(review));

        reviewService.eliminarReview(1L, "movie-1");

        verify(reviewReactionRepository).deleteByReview(review);
        verify(reviewRepository).delete(review);
    }

    @Test
    void eliminarReview_NoExiste_LanzaRuntime() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.findByUsuarioAndObraId(usuario, "movie-1"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.eliminarReview(1L, "movie-1"));

        assertEquals("Reseña no encontrada", ex.getMessage());
    }

    @Test
    void reaccionarReview_NuevaReaccion_OK() {
        ReviewReactionRequestDTO request = new ReviewReactionRequestDTO();
        request.setTipoReaccion(TipoReaccion.LIKE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        when(reviewReactionRepository.findByReviewAndUsuario(review, usuario))
                .thenReturn(Optional.empty());

        reviewService.reaccionarReview(10L, 1L, request);

        verify(reviewReactionRepository).save(any(ReviewReaction.class));
    }

    @Test
    void reaccionarReview_ActualizaReaccionExistente_OK() {
        ReviewReactionRequestDTO request = new ReviewReactionRequestDTO();
        request.setTipoReaccion(TipoReaccion.DISLIKE);

        ReviewReaction reaction = new ReviewReaction();
        reaction.setReview(review);
        reaction.setUsuario(usuario);
        reaction.setTipoReaccion(TipoReaccion.LIKE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        when(reviewReactionRepository.findByReviewAndUsuario(review, usuario))
                .thenReturn(Optional.of(reaction));

        reviewService.reaccionarReview(10L, 1L, request);

        assertEquals(TipoReaccion.DISLIKE, reaction.getTipoReaccion());
        verify(reviewReactionRepository).save(reaction);
    }

    @Test
    void eliminarReviewPorId_OK() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));

        reviewService.eliminarReviewPorId(1L, 10L);

        verify(reviewRepository).deleteByParentReview(review);
        verify(reviewReactionRepository).deleteByReview(review);
        verify(reviewRepository).delete(review);
    }

    @Test
    void eliminarReviewPorId_NoEsDelUsuario_LanzaRuntime() {
        UsuarioModel otro = new UsuarioModel();
        otro.setId(2L);

        Review reviewOtro = new Review();
        reviewOtro.setUsuario(otro);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reviewRepository.findById(20L)).thenReturn(Optional.of(reviewOtro));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.eliminarReviewPorId(1L, 20L));

        assertEquals("No puedes eliminar una reseña que no es tuya", ex.getMessage());
        verify(reviewRepository, never()).delete(reviewOtro);
    }
}