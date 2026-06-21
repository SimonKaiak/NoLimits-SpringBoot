package com.example.NoLimits.integration.review;

import com.example.NoLimits.Multimedia.model.review.Review;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.review.ReviewReactionRepository;
import com.example.NoLimits.Multimedia.repository.review.ReviewRepository;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:nolimits_review_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.id.new_generator_mappings=false"
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("T-Integración · Reviews")
class ReviewIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ReviewReactionRepository reviewReactionRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;

    private UsuarioModel usuario;

    @BeforeEach
    void prepararDatos() {
        reviewReactionRepository.deleteAll();
        reviewRepository.deleteAll();
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();

        RolModel rol = new RolModel();
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);
        rol = rolRepository.save(rol);

        usuario = new UsuarioModel();
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("juan.review@test.com");
        usuario.setTelefono(912345678L);
        usuario.setPassword("$2a$10$hashedpassword");
        usuario.setRol(rol);
        usuario = usuarioRepository.save(usuario);
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionReview {

        @Test
        @DisplayName("it: guarda una review y la persiste")
        void guardarReview_persisteEnBaseDeDatos() throws Exception {
            String body = """
                    {
                      "obraId": "movie-123",
                      "contenido": "Muy buena película",
                      "rating": 5
                    }
                    """;

            mockMvc.perform(post("/api/v1/reviews/" + usuario.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.usuarioId").value(usuario.getId()))
                    .andExpect(jsonPath("$.obraId").value("movie-123"))
                    .andExpect(jsonPath("$.contenido").value("Muy buena película"))
                    .andExpect(jsonPath("$.rating").value(5));

            assertTrue(reviewRepository.findByUsuario_Id(usuario.getId()).size() > 0);
        }

        @Test
        @DisplayName("it: lista reviews por obra con contadores de reacciones")
        void listarReviewsPorObra_retornaDatosPersistidos() throws Exception {
            Review review = new Review();
            review.setUsuario(usuario);
            review.setObraId("anime-456");
            review.setContenido("Excelente anime");
            review.setRating(4);
            reviewRepository.save(review);

            mockMvc.perform(get("/api/v1/reviews/obra/anime-456"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].usuarioId").value(usuario.getId()))
                    .andExpect(jsonPath("$[0].obraId").value("anime-456"))
                    .andExpect(jsonPath("$[0].contenido").value("Excelente anime"))
                    .andExpect(jsonPath("$[0].likes").value(0))
                    .andExpect(jsonPath("$[0].dislikes").value(0));
        }

        @Test
        @DisplayName("it: reacciona con LIKE a una review")
        void reaccionarReview_like_persisteReaccion() throws Exception {
            Review review = new Review();
            review.setUsuario(usuario);
            review.setObraId("game-789");
            review.setContenido("Buen juego");
            review.setRating(5);
            review = reviewRepository.save(review);

            String body = """
                    {
                      "tipoReaccion": "LIKE"
                    }
                    """;

            mockMvc.perform(post("/api/v1/reviews/" + review.getId() + "/reaction/" + usuario.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/reviews/obra/game-789"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].likes").value(1))
                    .andExpect(jsonPath("$[0].dislikes").value(0));
        }
    }
}